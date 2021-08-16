package com.whyat.config;

import com.whyat.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Configuration
//加载security相关配置
@EnableWebSecurity
//请求之前进行权限校验
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //登录成功和失败过滤器
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    //
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    //自定义验证码过滤器
    @Autowired
    private CaptchaFilter captchaFilter;

    //用于给Security提供封装用户信息和权限的service
    //这个Service专门封装的类是UserDetails的实现类
    @Autowired
    UserDetailService userDetailService;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    //访问URL白名单
    private static final String[] URL_WHITELIST = {
            "/captcha",
            "/login",
            "/logout",
            "/favicon.ico"
    };

    /**
     * 配置给security加密形式
     * @return
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //登录配置
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)

                //禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //不需要验证白名单的权限，例如登入、登出、验证码等和业务无关的东西都放行
                //除白名单之外的全部拦截
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                .and()
                //验证token
                .addFilter(jwtAuthenticationFilter())
                //在用户名密码验证过滤器之前验证验证码
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
