package com.chen.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description 利用request获取cookie,封装利于复用
 * @date 2023-08-05 16:02
 **/
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        //空值处理
        if (request == null ||name == null){
            throw new IllegalArgumentException("参数为空！");
        }
        //获取cookie中name的value
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
