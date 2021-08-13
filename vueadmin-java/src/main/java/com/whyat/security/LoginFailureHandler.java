package com.whyat.security;

import cn.hutool.json.JSONUtil;
import com.whyat.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        Result result = Result.fail(e.getMessage());
        String jsonStr = JSONUtil.toJsonStr(result);
        outputStream.write(jsonStr.getBytes(StandardCharsets.UTF_8)); //必须用这种方式输出流
        // outputStream.print(jsonStr);                               //会报异常Not an ISO 8859-1 character:
        outputStream.flush();
        outputStream.close();
    }
}
