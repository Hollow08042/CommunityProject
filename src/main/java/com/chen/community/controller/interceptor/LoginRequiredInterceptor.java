package com.chen.community.controller.interceptor;

import com.chen.community.annotation.LoginRequired;
import com.chen.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Description 拦截LoginRequired标识的方法，使其登录后才能被访问
 * @date 2023-08-06 19:00
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断拦截的是否是方法，是才继续
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取该方法
            Method method = handlerMethod.getMethod();
            //获取该方法的注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //注解不为空且用户未登录则需要拦截（错误的情况）
            if (loginRequired != null && hostHolder.getUsers() == null){
                //重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
