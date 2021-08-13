package com.whyat.security;

import cn.hutool.json.JSONUtil;
import com.whyat.common.lang.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        Result result = Result.fail(e.getMessage());
        String jsonStr = JSONUtil.toJsonStr(result);
        outputStream.print(jsonStr);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 生成jwt放置到响应头中
     * @param req
     * @param resp
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = resp.getOutputStream();

        //生成jwt，放置到请求头中

        Result result = Result.success("认证登录成功处理器返回的消息");
        String jsonStr = JSONUtil.toJsonStr(result);
        outputStream.print(jsonStr);
        outputStream.flush();
        outputStream.close();
    }
}
