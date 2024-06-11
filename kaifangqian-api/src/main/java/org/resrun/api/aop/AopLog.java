package org.resrun.api.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.resrun.api.utils.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * create by zhenghuihan at 2021/9/29 4:56 PM
 *
 * @param
 * @description 根据controller，记录详细日志到日志文件
 * @return
 * @throws
 */
@Aspect
@Component
@Order(value = 0)
public class AopLog {

    private  Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * JSON输出过滤器
     */
    PropertyFilter profilter = new PropertyFilter() {
        @Override
        public boolean apply(Object object, String name, Object value) {
            if (name.equalsIgnoreCase("image") || name.equalsIgnoreCase("file") || name.equalsIgnoreCase("p10") || name.equalsIgnoreCase("p7b")
                    || name.equalsIgnoreCase("documentFile") || name.equalsIgnoreCase("signatureFile") ||name.equalsIgnoreCase("signatureFile")||name.equalsIgnoreCase("pfx")
                || name.equalsIgnoreCase("entSeal")
            ) {
                return false;
            }
            return true;
        }
    };
    private static final String START_TIME = "request-start";

    /**
     * 切入点
     * 根据路径切入
     */
    @Pointcut("execution(public * org.resrun.api.controller.*Controller.*(..))")
    public void log() {
    }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) {
        Long start = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        log.info("【请求 URL】：{}", request.getRequestURL());
        log.info("【请求 IP】：{}", IPUtil.getIpAddr(request));
        log.info("【请求类名】：{}，【请求方法名】：{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        Map<String, Object> params = methodBefore(point);
        log.info("【请求参数】：{}，", JSON.toJSONString(params, profilter));
        request.setAttribute(START_TIME, start);
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        log.info("【返回值】：{}", JSON.toJSONString(result, profilter));
        return result;
    }

    /**
     * 后置操作
     */
    @AfterReturning("log()")
    public void afterReturning() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        Long start = (Long) request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        Long resTime = end - start;
        log.info("【请求耗时】：{}毫秒", resTime);
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> methodBefore(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            /**
             *下面两个数组中，参数值和参数名的个数和位置是一一对应的
             */
            Object[] objs = joinPoint.getArgs();
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] instanceof BindingResult || objs[i] instanceof HttpServletResponse || objs[i] instanceof Model) {
                    continue;
                } else if (objs[i] instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) objs[i];
                    Map<String, String[]> params = request.getParameterMap();
                    paramMap.put(argNames[i], params);
                } else {
                    paramMap.put(argNames[i], objs[i]);
                }
            }
        } catch (Exception e) {
            log.error("日志切面有异常", e);
        }
        return paramMap;
    }
}
