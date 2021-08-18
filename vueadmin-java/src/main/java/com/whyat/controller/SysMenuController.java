package com.whyat.controller;


import com.whyat.common.lang.Const;
import com.whyat.common.lang.Result;
import com.whyat.entity.SysMenu;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    @PreAuthorize("hasAuthority('sys:menu:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysMenu sysMenu){
        //设置创建事件
        sysMenu.setCreated(LocalDateTime.now());
        sysMenuService.save(sysMenu);
        return Result.success(sysMenu);
    }


    /**
     * 根据menuId获取menu信息
     * @param menuId
     * @return
     */
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable(name = "id") Long menuId){
        SysMenu sysMenu = sysMenuService.getById(menuId);
        return Result.success(sysMenu);
    }

    /**
     * 返回树状结构的权限列表
     * @return
     */
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/list")
    public Result list(){
        List<SysMenu> sysAuthTree = sysMenuService.getAuthTree();
        return Result.success(sysAuthTree);
    }
}
