package com.xzp.util;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/9 17:31
 * @Version 1.0
 */
public class TokenHelper {
    private static long tokenExpiration=24*60*60*1000;
    private static String SignKey="cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=";

    public static String createToken(Long useid,String username){
        //jdk11 先得到解码后的字节数组，
        byte[] keyBytes = Decoders.BASE64.decode(SignKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String token= Jwts.builder()
                .setSubject("ytxzp")
                .setExpiration(new Date(System.currentTimeMillis()+tokenExpiration))
                .claim("userid",useid)
                .claim("username",username)
                .signWith(key)
                .compact();
        return token;
    }
    public static Long getUserId(String token){
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(SignKey).build().parseClaimsJws(token);
        Claims body = jws.getBody();
        Long userid = (Long) body.get("userid");
        return userid;
    }
    public static String getUserName(String token){
        if(StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(SignKey).build().parseClaimsJws(token);
        Claims body = jws.getBody();
       String username = (String) body.get("username");
        return username;
    }
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5dHh6cCIsImV4cCI6MTY2ODIzNzk5MSwidXNlcmlkIjoxNTkwNTIzMDMzMDI0NDU0NjU4LCJ1c2VybmFtZSI6IjE1MDgzMTExNjY5In0.wz7DyCy2QK36KoTn_xRKDgraEA0EhfdR_Z--pXr_kNc";
        System.out.println(getUserId(token));
        System.out.println(getUserName(token)); }
}
