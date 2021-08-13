package com.whyat.controller;

import com.whyat.common.lang.Result;
import com.whyat.entity.SysUser;
import com.whyat.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/test")
    public Object test() {
        List<SysUser> sysUsers = userService.list();
        return Result.success(sysUsers);
    }

}
