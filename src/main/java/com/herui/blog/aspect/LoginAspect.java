package com.herui.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.herui.blog.web..*.*(..))")
    public void log(){

    }
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURI().toString();
        String ip =  request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLogs requestLogs = new RequestLogs(url,ip,classMethod,args);
        logger.info("Request:{}",requestLogs);
    }

    @After("log()")
    public void doAfter(){
//        logger.info("------------doAfter----------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Result : {]" +result);
    }

    private class RequestLogs {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] arg;

        public RequestLogs(String url, String ip, String classMethod, Object[] arg) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.arg = arg;
        }
    }
}