package com.xzp.config;
import com.alibaba.fastjson.JSONObject;
import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.util.TokenHelper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/10 16:57
 * @Version 1.0
 */
@Configuration
public class AuthFilter implements GlobalFilter, Ordered {
    private AntPathMatcher pathMatcher=new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        //内部服务接口，不允许外部访问
        if(pathMatcher.match("/**/inner/**", path)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCode.PERMISSION); }
        //api接口，异步请求，校验用户必须登录
        //
        if(pathMatcher.match("/api/**/auth/**", path)) {
            if("OPTIONS".equals(request.getMethodValue())){
                return chain.filter(exchange);
            }
            Long userId = this.getUserId(request);
            if(StringUtils.isEmpty(String.valueOf(userId))) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCode.LOGIN_AUTH);
            }
        }
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return 0;
    }
    /**
     * api接口鉴权失败返回数据
     * @param response
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCode resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
    /**
     * 获取当前登录用户id
     * @param request
     * @return
     */
    private Long getUserId(ServerHttpRequest request) {
        String token = "";
        //从header中获取token
        List<String> tokenList = request.getHeaders().get("token");
        if(null != tokenList) {
            token = tokenList.get(0);
        }
        if(!StringUtils.isEmpty(token)) {
            return TokenHelper.getUserId(token);
        }
        return null;
    }
}
