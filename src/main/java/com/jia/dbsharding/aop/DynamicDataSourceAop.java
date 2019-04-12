package com.jia.dbsharding.aop;

import com.jia.dbsharding.config.DataSourceHolder;
import com.jia.dbsharding.config.RoutingDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 拦截所有有 @RoutingDataSource 的方法，动态给它指定数据源
 */
@Aspect
@Component
public class DynamicDataSourceAop {

    /**
     * 在访问添加 @RoutingDataSource 注解前，切换到对应的数据源
     *
     */
    @Before("@annotation(com.jia.dbsharding.config.RoutingDataSource)")
    public void doBeforeSwitchDataSource(JoinPoint point){
        // 获取当前访问的类类型
        Class<?> className = point.getTarget().getClass();

        // 获得访问的方法名
        String methodName = point.getSignature().getName();
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        // 等到方法的参数
        Class[] argClass = methodSignature.getParameterTypes();
        String dataSource = DataSourceHolder.DEFALUT_DATASOURCE;
        try{
            // 获取访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否有 @RoutingDataSource 注解
            if(method.isAnnotationPresent(RoutingDataSource.class)){
                RoutingDataSource annotation = method.getAnnotation(RoutingDataSource.class);
                // 取出注解中的值
                dataSource = annotation.value();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("dataSource: " + dataSource);
        DataSourceHolder.setDB(dataSource);
    }

    @After("@annotation(com.jia.dbsharding.config.RoutingDataSource)")
    public void doAfterSwitchDataSource(JoinPoint point){
        DataSourceHolder.cleanDB();
    }
}
