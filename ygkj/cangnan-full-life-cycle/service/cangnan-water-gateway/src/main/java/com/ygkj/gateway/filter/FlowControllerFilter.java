package com.ygkj.gateway.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.ygkj.gateway.properties.SensitivePath;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class FlowControllerFilter implements GlobalFilter, Ordered {

    @Autowired
    private SensitivePath sensitivePath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> paths = sensitivePath.getPaths();
        if (paths.stream().anyMatch(sensitivePath -> matcher.match(sensitivePath,path))){
//            String token = request.getHeaders().getFirst(Constants.AUTH_KEY);
//            String userId = JwtTokenUtil.getUserId(token);
            // TODO: 2021/8/14  热点限流参数暂时写死
            // TODO: 系统按钮级别权限和api配置完成之后，可通过权限表获取每个api自定义的热点参数配置
            loadRule(path,5);
            Entry entry = null;
            try {
                ContextUtil.enter(path);
                entry = SphU.entry(path, EntryType.IN,1);
            } catch (BlockException e) {
                e.printStackTrace();
                return AuthGlobalFilter.responseResult(exchange.getResponse(), CommonResult.flowLimited());
            } finally {
                if (entry != null){
                    entry.exit(1);
                }
                ContextUtil.exit();
            }
        }
        return chain.filter(exchange);
    }

    private synchronized void loadRule(String path,int count){
        if (!FlowRuleManager.hasConfig(path)){
            FlowRule rule = new FlowRule(path);
            rule.setCount(count);
            rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
            rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
            rule.setWarmUpPeriodSec(2000);
//            ParamFlowRule rule = new ParamFlowRule(path)
//                    .setParamIdx(index)
//                    .setCount(count);
            FlowRuleManager.loadRules(Collections.singletonList(rule));
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
