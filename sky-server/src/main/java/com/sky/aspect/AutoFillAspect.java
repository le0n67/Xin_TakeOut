package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Date：2024/9/8  9:04
 * Description：自定义切面
 *
 * @author Leon
 * @version 1.0
 */

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFIllPointCut() {
    }

    /**
     * 前置通知,进行公共字段赋值
     */
    @Before("autoFIllPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行自动填充");
        //获取数据库操作类型和实体对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = annotation.value();
        //实体对象
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) return;
        Object entity = args[0];
        //准备公共字段数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据操作类型判断赋值内容
         if (value==OperationType.INSERT){
             try {
                 //反射获取set方法
                 Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                 Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                 Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                 Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                 //反射赋值
                 setCreateTime.invoke(entity,now);
                 setCreateUser.invoke(entity,currentId);
                 setUpdateTime.invoke(entity,now);
                 setUpdateUser.invoke(entity,currentId);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }else if (value==OperationType.UPDATE){

             try {
                 Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                 Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                 //反射赋值
                 setUpdateTime.invoke(entity,now);
                 setUpdateUser.invoke(entity,currentId);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }
}
