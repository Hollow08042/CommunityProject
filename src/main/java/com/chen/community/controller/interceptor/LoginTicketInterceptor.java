package com.chen.community.controller.interceptor;

import com.chen.community.entity.LoginTicket;
import com.chen.community.entity.User;
import com.chen.community.service.IUserService;
import com.chen.community.util.CookieUtil;
import com.chen.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Description 登录状态拦截器
 * @date 2023-08-05 15:56
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    // private static final Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // logger.debug("preHandle"+handler.toString());
        //获取cookie里的ticket
        String ticket = CookieUtil.getValue(request,"ticket");
        //根据ticket查询用户信息
        if (ticket != null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUsers(user);
            }
        }
        return true;
    }

    //在模板之前调用获取用户信息
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //获取user
        User user = hostHolder.getUsers();
        //存入模板
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    //模板执行完后清除user
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
