package com.whyat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whyat.service.*;
import com.whyat.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 15:42
 */
public class BaseController {
    /**
     * 自动注入的req对象，每次都不一样，
     * 注入的是JDK动态代理对象，每次调用req方法的时候会重新获取req
     */
    @Autowired
    HttpServletRequest req;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 获取请求分页参数的方法,没有传参就使用默认值
     * @return Mybati-plus的Page对象
     */
    public Page getPage(){
        //获取不到就使用默认值 页码1，大小10
        int current = ServletRequestUtils.getIntParameter(req, "cuurrent", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);

        return new Page(current, size);
    }


}
