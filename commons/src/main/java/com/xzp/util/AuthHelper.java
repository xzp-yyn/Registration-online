package com.xzp.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/11 9:45
 * @Version 1.0
 */
public class AuthHelper {


    /**
     * 得到用户id
     *
     * @param request 请求
     * @return {@link Long}
     */
    public static Long getUserId(HttpServletRequest request,String token){
//        String token="";
//        String token1 = request.getHeader("token");
        Long userId = TokenHelper.getUserId(token);
        return userId;
    }

    /**
     * 获得用户名
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getUserName(HttpServletRequest request,String token){
//        String token="";
//        String token1 = request.getHeader("token");
        String userName = TokenHelper.getUserName(token);
        return userName;
    }
}
