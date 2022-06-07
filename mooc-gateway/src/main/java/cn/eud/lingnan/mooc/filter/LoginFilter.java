package cn.eud.lingnan.mooc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author xmz
 * @date: 2020/11/17
 * 网关登录拦截器，验证是否已经登录，只有登录的用户才能通过
 */
public class LoginFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI requestURI = request.getURI();
        String url = requestURI.toString();
        String ipAddress = getIpAddress(request);
        log.info("请求url={}, ip={}", url, ipAddress);


        //webflux

        // 1、请求头获取token
        String token = request.getHeaders().getFirst("Authorization");
        if(token == null) {
            return Mono.defer(() -> {
                //response.mutate().header("Content-Type","application/json;charset=utf-8").build();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                String respBody = "{\"status\":\"401\",\"msg\":\"无效token\",\"data\":null,\"success\":false}";
                byte[] bytes = respBody.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                //response.getHeaders().set("permValid", "false");
                log.warn("请求不合法，没有携带token");
                return response.writeWith(Flux.just(buffer));
            });
        }

        // 2、去redis验证token是否有效


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private static String getIpAddress(ServerHttpRequest request) {

        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().toString();
        }
        return ip;
    }


}
