package com.zisheng.Inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.zisheng.Pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zisheng.MyUtils.JWTUtils;
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private  JWTUtils jwtUtils;
    // 在目标方法执行之前执行,根据函数的返回值来决定是否执行该方法,返回true执行，返回false则不执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if(url.contains("login"))
        {
            log.info("登录请求，直接放行");
            return true;
        }
        // 不是登录请求
        String JWT = request.getHeader("Token");
        log.info("JWT令牌为：" + JWT);
        if(!StringUtils.hasLength(JWT))
        {
            log.info("JWT令牌不合法，登录失败");
            Result result = Result.error("未登录");
            String json = JSONObject.toJSONString(result);
            response.getWriter().write(json);
            return false;
        }
        // 令牌存在，解析JWT令牌
        try {
            jwtUtils.parseJWT(JWT);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JWT令牌不合法，登录失败");
            Result result = Result.error("未登录");
            String json = JSONObject.toJSONString(result);
            response.getWriter().write(json);
            return false;
        }
        log.info("JWT令牌合法，登陆成功!!!");
        return true;
    }
    // 在目标方法执行之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle方法执行了");
    }
    // 在视图渲染完毕之后执行该方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion方法执行了");
    }
}
