package com.whyat.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.whyat.entity.SysUser;
import com.whyat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailService userDetailService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //请求头中获取jwt，header中的jwt参数是Authentication
        String jwt = request.getHeader(jwtUtils.getTokenName());
        //没有jwt交给后面过滤器处理
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        //解析jwt
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token过期,重新登录");
        }
        //jwt验证成功取出主体，主体中存放的是用户名和用户id的json字符串
        String userInfo = claim.getSubject();
        SysUser sysUser = JSON.parseObject(userInfo, SysUser.class);
        //根据用户id获取用户拥有的权限
        List<GrantedAuthority> authInfo = userDetailService.getUserAuthorities(sysUser.getId());

        //把用户的id和权限放置在Authentication中，UsernamePasswordAuthenticationToken是1个Authentication实现类
        //用户id是放在principal主体中的
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(sysUser.getId(), null, authInfo);
        //不加这句代码，会进入到AuthenticationEntryPoint，代表了jwt认证失败
        SecurityContextHolder.getContext().setAuthentication(token);
        //进入下一个过滤器
        chain.doFilter(request, response);
    }
}
