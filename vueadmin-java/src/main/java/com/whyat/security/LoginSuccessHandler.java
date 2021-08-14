package com.whyat.security;

import cn.hutool.json.JSONUtil;
import com.whyat.common.lang.Result;
import com.whyat.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;
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

        //生成主体为用户名的'Authoriaztion'的headervalue=token参数，放到响应头header，
        String token = jwtUtils.generateToken(authentication.getName());
        log.info("authentication参数是：" + authentication);
        resp.setHeader(jwtUtils.getTokenName(), token);


        //返回响应
        Result result = Result.success(2000, "认证登录成功,header中携带了名称为authorization的token", token);
        String jsonStr = JSONUtil.toJsonStr(result);
        byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);

        // outputStream.print(jsonStr);[
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
