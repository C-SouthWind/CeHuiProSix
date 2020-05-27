package com.chj.annotation;

import com.chj.model.login.User;
import com.chj.service.CHJService;
import com.chj.utils.DateUtils;
import com.chj.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.chj.staticstatus.TimeProperties.TIME_TYPE;

/**
 * @author ：chj
 * @date ：Created in 2020/5/27 18:13
 * @params :
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private CHJService chjService;

    /** 方法描述
     * @Description: 定义一个切面
     * @Param: []
     * @return: void
     * @Author: chj
     * @Date: 2020/5/27
     */
    @Pointcut("@annotation(com.chj.annotation.LoginLogAnnotation)")
    public void pointcut(){

    }

    /** 方法描述
     * @Description: 定义环形切点
     *               ProceedingJoinPoint 封装了目标路径中的所有参数
     * @Param: [proceedingJoinPoint]
     * @return: java.lang.Object
     * @Author: chj
     * @Date: 2020/5/27
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException {
        //定义返回值
        Object result = null;
        try {
            result= proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //获取添加日志的参数信息
        //1. 获取username
        Object[] args = proceedingJoinPoint.getArgs();
        User user = null;
        for (Object arg: args) {
            user = (User) arg;
        }
        //2.获取当前时间
        String datetime = DateUtils.formatDate(new Date(), TIME_TYPE);
        //3.获取ip
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IPUtils.getIpAddr(request);
        //4.获取注解上的opeationType和operationName
        //拿到全限定名
        String name = proceedingJoinPoint.getTarget().getClass().getName();
        //获得方法名
        String targetMethodName = proceedingJoinPoint.getSignature().getName();
        //反射拿信息
        Class targetClass = Class.forName(name);
        //获取类中的方法
        Method[] methods = targetClass.getMethods();
        //对比判断找到方法
        String operationName = "";
        String operationType = "";

        for(Method method : methods){
            //获取方法名
            String methodName = method.getName();
            //相同则匹配
            if (methodName.equals(targetMethodName)) {
                //判断参数个数是否相同
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == args.length) {
                    operationName = method.getAnnotation(LoginLogAnnotation.class).operationName();
                    operationType = method.getAnnotation(LoginLogAnnotation.class).operationType();
                }
            }
        }

        //存入数据库
        Map map = new HashMap();
        map.put("username",user.getUsername());
        map.put("loginTime",datetime);
        map.put("ip",ip);
        map.put("operationType" ,operationType);
        map.put("operationName" ,operationName);

        chjService.loginAddByMap(map);
        return result;
    }
}

