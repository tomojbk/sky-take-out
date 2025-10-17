package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import com.sky.exception.AspectAutoFillException;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Description AutoFillAspect
 * @Author tomLe
 * @Date 2025-10-16
 */
//@Aspect代表该类是切面类
//@Component注册到Springboot
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
//    @Around("execution ")
    //@annotation()用于定义一个 切点（Pointcut），匹配所有被某个 自定义注解 标记的方法。
    //@Pointcut(
    //execution(* com.example.service.*.*(..))	匹配 service 包下所有类的所有方法
    //@annotation(com.example.MyLog)	匹配标注了 @MyLog 的方法
    //within(com.example.service.*)	匹配 service 包下所有类的方法
    // )
    // 定义一个 切点表达式，可以被多个通知（如 @Before、@After、@Around）复用。
    //execution(* com.sky.mapper.*.*(..)) &&
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    //@Before("autoFillPointCut()")在 匹配 autoFillPointCut()
    // 切点的方法执行之前，先执行这个通知方法。
    @Before("autoFillPointCut()")
    //JoinPoint拿到实体方法
    public void autoFill(JoinPoint joinPoint) {
        log.info("修改公共字段");
        //获取方法参数
        Object entity = joinPoint.getArgs()[0];
        // 获取连接点的方法签名信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        try {
            if (operationType == OperationType.INSERT) {
                //插入
                //反射获取方法
                Class clazz = entity.getClass();
                Method setCreateUser = clazz.getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCreateTime = clazz.getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = clazz.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = clazz.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //调用方法
                setCreateUser.invoke(entity, BaseContext.getCurrentId());
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
                setCreateTime.invoke(entity, LocalDateTime.now());
                setUpdateTime.invoke(entity, LocalDateTime.now());
            } else if (operationType == OperationType.UPDATE) {
                //修改
                Class clazz = entity.getClass();
                Method setUpdateTime = clazz.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = clazz.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //调用方法
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
                setUpdateTime.invoke(entity, LocalDateTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AspectAutoFillException("aop自动填充异常");

        }


    }
}
