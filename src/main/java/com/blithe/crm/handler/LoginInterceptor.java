package com.blithe.crm.handler;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 14:25
 * Description:
 */

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 由于Interceptor拦截器无法拦截jsp页面，因此放弃使用interceptor拦截器来实现拦截session
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    /*
        System.out.println("进入到验证有没有登陆过的过滤器");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return false;
        }
        return true;

     */
        return true;
    }
}
