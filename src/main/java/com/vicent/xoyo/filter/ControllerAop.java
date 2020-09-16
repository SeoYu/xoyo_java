package com.vicent.xoyo.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.vicent.xoyo.entity.Log;
import com.vicent.xoyo.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author vincent
 * @description 接口切片
 * @Date 2020/9/16 1:10 下午
 */
@Component
@Slf4j
@Aspect
public class ControllerAop {
    private final String executeExpr = "execution(* com.vicent.xoyo..*Controller.*(..))";

    @Autowired
    ILogService logService;

    /**
     * @param joinPoint:
     * @Author: TheBigBlue
     * @Description: 环绕通知，拦截controller，输出请求参数、响应内容和响应时间
     * @Date: 2019/6/17
     * @Return:
     **/
    @Around(executeExpr)
    public Object processLog(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法名称
        String methodName = method.getName();
        //获取参数名称
        LocalVariableTableParameterNameDiscoverer paramNames = new LocalVariableTableParameterNameDiscoverer();
        String[] params = paramNames.getParameterNames(method);
        //获取参数
        Object[] args = joinPoint.getArgs();
        //过滤掉request和response,不能序列化
        List<Object> filteredArgs = Arrays.stream(args)
            .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
            .collect(Collectors.toList());
        JSONObject rqsJson = new JSONObject();
        rqsJson.put("rqsMethod", methodName);
        rqsJson.put("rqsTime", LocalDateTime.now());
        String account = null;
        if (CollUtil.isEmpty(filteredArgs)){
            rqsJson.put("rqsParams", null);
        } else {
            //拼接请求参数
            Map<String, Object> rqsParams = IntStream.range(0, filteredArgs.size())
                .boxed()
                .collect(Collectors.toMap(j -> params[j], j -> filteredArgs.get(j)));
            rqsJson.put("rqsParams", rqsParams);
        }
        // 获取 ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Real-IP");
        if (StrUtil.isEmpty(ip) && StrUtil.isEmpty(ip = request.getHeader("x-forwarded-for"))){
            ip = request.getRemoteAddr();
        }
        Log logEntity = Log.builder()
            .createTime(new Date())
            .url(methodName)
            .ip(ip)
            .param(rqsJson.toString())
            .method(methodName)
            .build();
        log.info(methodName + "请求信息为：" + rqsJson.toString());
        Object resObj = null;
        String reqID = IdUtil.fastUUID();
        StopWatch stopWatch = new StopWatch(reqID);
        stopWatch.start(reqID);
        try {
            //执行原方法
            resObj = joinPoint.proceed(args);
        } catch (Throwable e) {
            log.error(methodName + "方法执行异常!", e);
        }
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        logEntity.setAccount(Convert.toStr(lastTaskTimeMillis));
        logService.insert(logEntity);
        log.info("{} {}",reqID, lastTaskTimeMillis);

        return resObj;
    }
}
