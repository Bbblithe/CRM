package com.blithe.crm.web.filter;


import com.blithe.crm.setting.domain.User;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 15:02
 * Description:
 */

public class LoginFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      // System.out.println("进入到验证有没有登陆过的过滤器");
      HttpServletRequest request1 = (HttpServletRequest) request;
      HttpServletResponse response1 = (HttpServletResponse) response;

      /*
      重定向的路径怎么写？
                在实际项目开发中，对于路径的使用，无论操作的是前端还是后端，应该一律使用绝对路径

            关于转发和重定向的路径的写法如下
                转发：
                    使用的是一种特殊的绝对路径的使用方式，这种路径的前面不加/项目名，这种路径也称之为内部路径
                    /login.jsp
                重定向：
                    使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                    /web/login.jsp

            为什么使用重定向，转发不行吗
                转发之后路径会停留在老路径上，而不是跳转之后最新资源的路径
                我们应该为用户跳转到登陆页的同时，将浏览器的地址栏，应该自动设置为当前的登陆页的路径
       */

      String path = request1.getServletPath();
      // 不应该拦截的资源，自动放弃请求
      if("/login.jsp".equals(path)){
         chain.doFilter(request,response);
      }else{
         HttpSession session = request1.getSession();
         User user = (User)session.getAttribute("user");
         if(user!= null){
            chain.doFilter(request,response);
         }else{
            response1.sendRedirect(request1.getContextPath() + "/login.jsp");
         }
      }
   }
}
