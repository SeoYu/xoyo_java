//package com.vicent.xoyo.filter;
//
//
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONUtil;
//import com.vicent.xoyo.entity.Log;
//import com.vicent.xoyo.service.ILogService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author vincent
// * @description 请求日志过滤器
// * @Date 2020/5/18 6:12 下午
// */
//@Slf4j
//@WebFilter(filterName = "logFilter",urlPatterns = "*")
//public class LogFilter implements Filter, ApplicationContextAware {
//
//    private ApplicationContext context;
//
//    @Autowired
//    ILogService logService;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.context = applicationContext;
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("seoyu: 请求记录过滤器");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String ip = request.getHeader("X-Real-IP");
//        String url = request.getRequestURI();
//        String method = request.getMethod();
//        if (StrUtil.isEmpty(ip) && StrUtil.isEmpty(ip = request.getHeader("x-forwarded-for"))){
//            ip = request.getRemoteAddr();
//        }
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        Map<String, Object> param = new HashMap<>();
//        param.putAll(parameterMap);
//        String jsonStr = JSONUtil.toJsonStr(param);
//        log.info("seoyu: 请求过滤日志 {} {} {} {} {} {}"
//            ,LocalDate.now(),LocalTime.now(),ip,url,method,jsonStr);
//        Log log = Log.builder()
//            .account(Convert.toStr(MapUtil.getStr(parameterMap, "id"), ""))
//            .ip(ip)
//            .method(method)
//            .param(jsonStr)
//            .url(url)
//            .createTime(new Date())
//            .build();
//        logService.insert(log);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("------------------init");
//    }
//
//    @Override
//    public void destroy() {
//        log.info("------------------destroy");
//    }
//}
