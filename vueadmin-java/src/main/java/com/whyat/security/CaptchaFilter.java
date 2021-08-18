package com.whyat.security;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.whyat.common.exception.CaptchaException;
import com.whyat.common.lang.Const;
import com.whyat.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 验证码拦截器
 */
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        // log.info("请求的url是【" + url + "】");
        //判断是不是登录请求
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            //校验验证码
            try {
                validate(request);
            } catch (CaptchaException e) {
                //验证码失败抛了异常就交给登录异常处理器去处理
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        //校验成功或者不是登录请求就放行
        filterChain.doFilter(request, response);
    }

    //校验验证码逻辑
    private void validate(HttpServletRequest request) throws IOException {
        //1.获取验证码和key
        //key=验证码的唯一标识

        //post方法不能直接getParameter

        //把读取出来的json字符串参数再用fastjson转成map获取参数
        // BufferedReader br = request.getReader();
        // String str = "";
        // String paramString = "";
        // while ((str = br.readLine()) != null) {
        //     paramString += str;
        // }
        //
        // HashMap<String,String> paramMap = JSON.parseObject(paramString, HashMap.class);
        //
        //
        // String key = paramMap.get("key");
        // String captchaCode = paramMap.get("captchaCode");

        String key = request.getParameter("key");
        String captchaCode = request.getParameter("captchaCode");
        //2.1先校验key和验证码是否为空
        //可能出现验证码还没刷出来的时候就发出登录请求
        if (StringUtils.isBlank(key) || StringUtils.isBlank(captchaCode)) {
            throw new CaptchaException("验证码为空");
        }

        //2.2判断redis中的key是否存在验证码
        //两种情况=> 1.key可能输错 2.验证码过期
        //通过网页访问一般只可能是第一种情况
        String code = (String) redisUtil.hget(Const.CAPTCHA, key);
        if (StringUtils.isBlank(code))
            throw new CaptchaException("验证码已过期，请重新获取");

        //2.3校验正确或失败，都要删除验证码
        //节约内存

        redisUtil.hdel(Const.CAPTCHA, key);
        //2.4取出验证码之后比对传过来的验证码是否正确
        if (!code.equals(captchaCode)){
            throw new CaptchaException("验证码输入错误，请重试！");
        }

        //校验成功退出方法


    }
}
