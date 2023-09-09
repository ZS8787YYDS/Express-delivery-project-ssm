package com.zisheng.Filter;

import com.alibaba.fastjson.JSONObject;
import com.zisheng.MyUtils.JWTUtils;
import com.zisheng.Pojo.Result;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * 定义一个类，实现Filter接口，重写所有的方法，
 * 在启动类上加上ServletComponentScan注解，开启Servlet组件支持
 */
// 设置过滤器拦截的路径，/*表示拦截所有请求
@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);// 创建日志记录对象
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化方法执行了!!!");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        // 获取请求的url
//        String requestUrl = httpServletRequest.getRequestURL().toString();
//        log.info("utl:{}",requestUrl);
        String requestURI = httpServletRequest.getRequestURI();
        log.info("urI:{}", requestURI);
        if (requestURI.contains("login") || requestURI.contains("logout")) {
            log.info("请求不需要处理: {}", requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 判断是否已经登录
        if (httpServletRequest.getSession().getAttribute("employee") != null) {
            log.info("已经登陆，id为：{}", httpServletRequest.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info("用户未登录");
        // 创建Result对象
        Result result = Result.error("NOTLOGIN");
        // 将对象转化为JSON格式的字符串
        String jsonStr = JSONObject.toJSONString(result);
        // 获取字符打印流，调用write方法将字符串响应给前端
        httpServletResponse.getWriter().write(jsonStr);
    }

    @Override
    public void destroy() {
        log.info("销毁方法执行了！！！");
    }
}
