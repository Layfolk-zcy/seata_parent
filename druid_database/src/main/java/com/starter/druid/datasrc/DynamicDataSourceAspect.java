package com.starter.druid.datasrc;

import com.starter.druid.inter.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 1.@Order(-1)保证该AOP在@Transactional之前执行
 * @date 2022/6/26 下午13:14
 */
@Aspect
@Component
@Order(-1)
public class DynamicDataSourceAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.starter.druid.inter.DataSource)"
            + "|| @within(com.starter.druid.inter.DataSource)")
    public void dsPointCut()  {
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Method targetMethod = this.getTargetMethod(point);
        //获取要切换的数据源
        DataSource dataSource = targetMethod.getAnnotation(DataSource.class);
        if (dataSource != null)  {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.name().name());
        }
        try {
            return point.proceed();
        }
        finally  {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.removeDataSourceType();
        }
    }

    /**
     * 获取目标方法
     */
    private Method getTargetMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method agentMethod = methodSignature.getMethod();
        return pjp.getTarget().getClass().getMethod(agentMethod.getName(), agentMethod.getParameterTypes());
    }
}
