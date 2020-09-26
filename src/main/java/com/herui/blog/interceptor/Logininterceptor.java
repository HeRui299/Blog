package com.herui.blog.interceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class Logininterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
