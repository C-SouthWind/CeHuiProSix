package com.chj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：chj
 * @date ：Created in 2020/5/27 18:12
 * @params :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLogAnnotation {

    /** 方法描述
     * @Description: 需要执行的操作
     * @Param: []
     * @return: java.lang.String
     * @Author: chj
     * @Date: 2020/5/27
     */
    String operationType() default "";
    /** 方法描述
     * @Description: 需要执行的操作内容
     * @Param: []
     * @return: java.lang.String
     * @Author: chj
     * @Date: 2020/5/27
     */
    String operationName() default "";
}
