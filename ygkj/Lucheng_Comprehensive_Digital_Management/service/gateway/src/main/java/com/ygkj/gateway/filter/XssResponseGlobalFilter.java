//package com.ygkj.gateway.filter;
//
//
//import com.ygkj.gateway.utils.XssCleanRuleUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import java.nio.charset.Charset;
//
///**
// * @Author: GuanYZ
// * @Description: 重写Response，防止xss攻击
// * @Date: Created in 2020/5/1.
// */
//@Slf4j
//@Component
//public class XssResponseGlobalFilter implements Ordered, GlobalFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpResponse originalResponse = exchange.getResponse();
//        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                String contentType = getDelegate().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
//                Boolean flag = MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType);
//                if (body instanceof Flux && flag) {
//                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
//                    return super.writeWith(fluxBody.map(dataBuffer -> {
//                        // probably should reuse buffers
//                        byte[] content = new byte[dataBuffer.readableByteCount()];
//                        dataBuffer.read(content);
//                        //释放掉内存
//                        DataBufferUtils.release(dataBuffer);
//                        String result = new String(content, Charset.forName("UTF-8"));
//                        //result就是response的值，对result进行去XSS
//                        result = XssCleanRuleUtils.xssClean(result);
//                        byte[] uppedContent = new String(result.getBytes(), Charset.forName("UTF-8")).getBytes();
//                        return bufferFactory.wrap(uppedContent);
//                    }));
//                }
//                // if body is not a flux. never got there.
//                return super.writeWith(body);
//            }
//        };
//        // replace response with decorator
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return -50;
//    }
//}
