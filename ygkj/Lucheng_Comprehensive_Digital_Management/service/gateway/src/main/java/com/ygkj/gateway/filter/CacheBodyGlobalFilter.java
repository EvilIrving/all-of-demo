//package com.ygkj.gateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * @Author: GuanYZ
// * @Description: 这个过滤器解决body不能重复读的问题，为后续的XssRequestGlobalFilter重写post|put请求的body做准备
// * @Date: Created in 2020/5/1.
// * <p>
// * 没把body的内容放到attribute中去，因为从attribute取出body内容还是需要强转成 Flux<DataBuffer>,然后转换成String,和直接读取body没有什么区别
// */
//@Component
//public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {
//    private static final String APPLICATION_FORM_URLENCODED_UTF8_VALUE = "application/x-www-form-urlencoded;charset=UTF-8";
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpMethod method = exchange.getRequest().getMethod();
//        String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
//        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
//            if (APPLICATION_FORM_URLENCODED_UTF8_VALUE.equalsIgnoreCase(contentType)
//                    || MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)
//                    || MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)
//                    || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
//                return DataBufferUtils.join(exchange.getRequest().getBody())
//                        .flatMap(dataBuffer -> {
//                            DataBufferUtils.retain(dataBuffer);
//                            Flux<DataBuffer> cachedFlux = Flux
//                                    .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
//                            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
//                                    exchange.getRequest()) {
//                                @Override
//                                public Flux<DataBuffer> getBody() {
//                                    return cachedFlux;
//                                }
//                            };
//                            return chain.filter(exchange.mutate().request(mutatedRequest).build());
//                        });
//            }
//
//        }
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}