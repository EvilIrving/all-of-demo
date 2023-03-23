package com.ygkj.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.ygkj.gateway.constants.Constants;
import com.ygkj.gateway.properties.ExternalAllowPath;
import com.ygkj.gateway.properties.IgnoreAuthPath;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import io.jsonwebtoken.JwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: fml
 * @Date: 2020/8/13 10:08
 * @Description: 自定义全局过滤器，登录认证，此处暂时简写
 */

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 忽略的URL
     */
    @Value("${referer.whitelist}")
    private List<String> excludes;
    /**
     * 没有refere也可以过的请求
     */
    @Value("${referer.ignorelist}")
    private List<String> ignorelist;

    @Autowired
    private IgnoreAuthPath ignoreAuthPath;

    @Autowired
    private ExternalAllowPath externalAllowPath;

    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //其中ServerWebExchange是Spring封装了javaweb中的相关的对象比如request和response,session等对象
        //获取http请求对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        List<String> referers = request.getHeaders().getOrEmpty("Referer");
        if (CollectionUtils.isEmpty(referers)){
            String path = request.getURI().getPath();
            String fullPath = obtainRequestPath(exchange);
            AntPathMatcher matcher = new AntPathMatcher();
            if (ignorelist.stream().anyMatch(candidatePath -> matcher.match(candidatePath, path)
                    || matcher.match(candidatePath, fullPath))){

            }else {
                CommonResult result = CommonResult.failed(CommonCode.NOT_FOUND,CommonCode.NOT_FOUND.message());
                return responseResult(response, result,HttpStatus.NOT_FOUND);
            }
        } else{
            String path = request.getURI().getPath();
            String fullPath = obtainRequestPath(exchange);
            AntPathMatcher matcher = new AntPathMatcher();
            if (ignorelist.stream().anyMatch(candidatePath -> matcher.match(candidatePath, path)
                    || matcher.match(candidatePath, fullPath))){

            }else {
                String referer = referers.get(0);
                URI uri = request.getURI();
                try {
                    URL url = new URL(referer);
                } catch (Exception e) {
                    CommonResult result = CommonResult.failed(CommonCode.NOT_FOUND, CommonCode.NOT_FOUND.message());
                    return responseResult(response, result, HttpStatus.NOT_FOUND);
                }
                String host = uri.getHost();
                String replaceAll = referer.replaceAll("/", ":");
                String[] split = replaceAll.split(":");
                // 判断是否存在外链请求本站
                if (!excludes.contains(split[3]) && !split[3].startsWith("192.168.2")) {
                    if (!split[3].equals(host)) {
                        throw new Exception("访问来源异常");
                    }
                }
            }
        }
        if (request.getHeaders().containsKey("doNotAuth")) {
            //2021.04.06 暂定当前请求头带有doNotAuth标致可绕过验证
            return chain.filter(exchange);
        }
        //获取请求的url
        String path = request.getURI().getPath();
        //获取请求的全路径带服务前缀
        String fullPath = obtainRequestPath(exchange);
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> whiteList = ignoreAuthPath.getPaths();
        if (whiteList.stream().anyMatch(candidatePath -> matcher.match(candidatePath, path)
                || matcher.match(candidatePath, fullPath))) {
            return chain.filter(exchange);
        }
        //如果上面判断没有放行就执行到这里进行登陆的判断
//        ServerHttpResponse response = exchange.getResponse();
        //获取请求头中的token信息
        String jwt = request.getHeaders().getFirst(Constants.AUTH_KEY);
        //如果获取出来的token为空就返回未登录的Response响应
        if (StringUtils.isEmpty(jwt)) {
            CommonResult result = CommonResult.unauthorized("当前用户暂未登录！");
            return responseResult(response, result);
        }
        if (JwtTokenUtil.external(jwt)) {
            //外部用户只能指定路径访问
            List<String> externalAllowPaths = externalAllowPath.getPaths();
            if (externalAllowPaths.stream().noneMatch(allowPath -> matcher.match(allowPath, fullPath))) {
                CommonResult result = CommonResult.forbidden(StringUtils.EMPTY);
                return responseResult(response, result);
            }
        }
        try {
            long durationMills = JwtTokenUtil.getRemainingTime(jwt);
            String key = Constants.AUTH_KEY.concat(jwt);
            long redisExpireMills = redisTemplate.getExpire(key, TimeUnit.MINUTES).longValue();
            log.info("当前token剩余时间：{}毫秒", durationMills);
            log.info("当前token在redis剩余时间：{}分钟", redisExpireMills);
            if (JwtTokenUtil.isTokenExpired(jwt)) {
                //硬性token过期


                return responseResult(response, CommonResult.unauthorized("登录已过期"));
            } else if (!redisTemplate.hasKey(key)) {
                //超过3小时没有进行操作
                return responseResult(response, CommonResult.unauthorized("登录已过期"));
            }
//            if (JwtTokenUtil.getUserType(jws).equals("2")){
//                //企业用户需要按钮鉴权
//                //统一的接口访问鉴权逻辑
//                String userId = JwtTokenUtil.getUserId(jws);
//                JSONArray array = JSONArray.parseArray(String.valueOf(redisTemplate.opsForValue().get(Constants.PERMISSION_CODE_KEY.concat(userId))));
//                //找到当前用户允许访问的功能对应的url
//                List<String> hasPermissionUrls = array.stream().map(jsonobject ->String.valueOf(((JSONObject)jsonobject).get("url")))
//                        .collect(Collectors.toList());
//                long count = hasPermissionUrls.stream().filter(url -> path.endsWith(url)).count();
//                if (count != 1){
//                    return responseResult(response, CommonResult.validateFailed("当前用户暂无权限！"));
//                }
//            }
            //刷新token
            redisTemplate.opsForValue().set(key, jwt, Duration.ofHours(3l));
            //让当前请求通过这个过滤器,进入下一个过滤器
            return chain.filter(exchange);
        } catch (JwtException e) {
            //如果解析失败,就返回登陆已过期的响应
            log.error("登录已过期", e);
            //返回失败结果
            return responseResult(response, CommonResult.unauthorized("登录已过期"));
        }
    }


    /**
     * 过虑器序号，越小越被优先执行
     */
    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * @Author Huang.zh
     * @Description 对请求结果进行封装
     * @Date 2020/9/11 11:36
     */
    public static Mono<Void> responseResult(ServerHttpResponse response, CommonResult result) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(HttpStatus.OK);
        DataBufferFactory bufferFactory = response.bufferFactory();
        byte[] bytes = JSON.toJSONString(result).getBytes();
        DataBuffer buffer = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static Mono<Void> responseResult(ServerHttpResponse response, CommonResult result,HttpStatus status) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(status);
        DataBufferFactory bufferFactory = response.bufferFactory();
        byte[] bytes = JSON.toJSONString(result).getBytes();
        DataBuffer buffer = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private String obtainRequestPath(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        //获取网关匹配前缀
        String prefix = route.getId();
        String path = uri.getPath();
        return "/".concat(prefix).concat(path);
    }
}
