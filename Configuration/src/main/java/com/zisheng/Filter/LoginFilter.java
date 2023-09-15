package com.zisheng.Filter;

import com.alibaba.fastjson.JSONObject;
import com.zisheng.MyUtils.JWTUtils;
import com.zisheng.MyUtils.ThreadUtils;
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
    // 创建路径匹配器对象，支持通配符进行匹配
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化方法执行了!!!");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        // 获取请求的url
        String requestUrl = httpServletRequest.getRequestURL().toString();
        log.info("utl:{}",requestUrl);
        // 获取请求的URI地址，即接口
        String requestURI = httpServletRequest.getRequestURI();
        log.info("urI:{}", requestURI);
        // 该数组用于存储一些不需要处理的请求
        String[] paths = new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/user/sendMsg",
                "/user/login"
        };
        // 如果说请求不需要进行处理的话
        if (check(paths,requestURI)) {
            log.info("请求不需要处理: {}", requestURI);
            // 直接放行，去执行目标方法
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 判断后端是否已经登录
        if (httpServletRequest.getSession().getAttribute("employee") != null) {
            // 调用工具库类，将登录成功之后的存储在Session中用户的id保存在ThreadLocal变量中
            ThreadUtils.setThreadLocal((Long) httpServletRequest.getSession().getAttribute("employee"));
            log.info("已经登陆，id为：{}", httpServletRequest.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 判断移动端是否已经登陆
        if(httpServletRequest.getSession().getAttribute("user") != null)
        {
            Long id = (Long) httpServletRequest.getSession().getAttribute("user");
            // 将移动端登陆成功之后的id存储到ThreadLocal局部变量当中
            ThreadUtils.setThreadLocal(id);
            log.info("已经登录！！！" + "id为" + id);
            // 直接放行
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 用户没登陆，返回未登录的结果
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
    // 请求匹配方法
    public boolean check(String[] paths,String uri)
    {
        for(String path : paths)
        {
            // 如果说匹配成功的话，是不不需要进行处理的，返回true
            if(PATH_MATCHER.match(path,uri)) return true;
        }
        // 未匹配成功，返回false
        return false;
    }
}
