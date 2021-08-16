package com.whyat.controller;

import com.whyat.common.lang.Result;
import com.whyat.entity.SysUser;
import com.whyat.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 15:46
 */
@RestController
public class TestController {
    @Autowired
    SysUserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Object test() {
        List<SysUser> sysUsers = userService.list();
        return Result.success(sysUsers);
    }

    // 普通用户、超级管理员
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/password")
    public Result setPass() {
        String encodedPass = bCryptPasswordEncoder.encode("123");
        boolean matches = bCryptPasswordEncoder.matches("123", encodedPass);
        System.out.println("【密码匹配结果】："+matches);
        return Result.success(encodedPass);
    }



}
