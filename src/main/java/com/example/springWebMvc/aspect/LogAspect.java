package com.example.springWebMvc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
//@Component
//@Aspect
public class LogAspect {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
//    @Pointcut("within(com.example.springWebMvc.service.impl.*)")
//    public void logController(){
//    }
//    @Around("logController()")
//    public void handleLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        Long start = System.currentTimeMillis();
//        joinPoint.proceed();
//        Long end = System.currentTimeMillis();
//        LOGGER.info("LOG ASPECT : {} executed in {}",joinPoint.getSignature(),end-start);
//    }
}
