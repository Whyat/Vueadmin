package com.whyat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whyat.common.lang.Const;
import com.whyat.common.lang.Result;
import com.whyat.entity.SysRole;
import com.whyat.entity.SysRoleMenu;
import com.whyat.entity.SysUserRole;
import com.whyat.service.SysUserRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    /**
     * 查询角色及其拥有的权限
     *
     * @param id 角色id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable Long id) {
        //mybatis-plus根据角色id查询角色的信息
        SysRole sysRole = sysRoleService.getById(id);
        //mybatis-plus查询关系表
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = sysRoleMenus.stream().map(rm -> rm.getMenuId()).collect(Collectors.toList());
        //给角色赋值权限集合
        sysRole.setMenuIds(menuIds);
        //返回角色
        return Result.success(sysRole);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(String name) {
        System.out.println(name);
        //getPage()是父类BaseController封装好的获取分页参数的方法
        //name不为空才去执行语句
        boolean condition = StringUtils.isNotBlank(name);
        Page<SysRole> pageData = sysRoleService.page(this.getPage(),
                new QueryWrapper<SysRole>().like(condition, "name", name));
        return Result.success(pageData);
    }

    @PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysRole sysRole) {
        //设置创建事件和激活状态
        sysRole.setCreated(LocalDateTime.now());
        //插入到数据库
        sysRoleService.save(sysRole);
        return Result.success(sysRole);
    }

    /**
     * 更新
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole) {
        sysRole.setUpdated(LocalDateTime.now());
        sysRoleService.updateById(sysRole);
        //清除跟更新的角色关联的用户的缓存
        sysUserService.clearRedisUserAuthInfoBySysRoleId(sysRole.getId());
        return Result.success(sysRole);
    }

    /**
     * 批量删除、删除
     *
     * @param roleIds 角色id数组
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public Result delete(@RequestBody Long roleIds[]) {
        sysRoleService.removeByIds(Arrays.asList(roleIds));
        //清除每一个角色相关的用户的缓存
        Arrays.stream(roleIds).forEach(roleId -> sysUserService.clearRedisUserAuthInfoBySysRoleId(roleId));
        //清除role关联的中间表的数据
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", roleIds));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", roleIds));
        return Result.success("删除成功");
    }

    /**
     * 给角色分配权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @Transactional
    public Result delete(@PathVariable("roleId") Long roleId, @RequestBody Long menuIds[]) {
        //1.清除和此角色相关的用户的权限缓存
        sysUserService.clearRedisUserAuthInfoBySysRoleId(roleId);

        //2.删除原来的角色带有的权限
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", roleId));
        //3.插入中间表权限数据
        ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            //给需要添加的每一条记录设置对应的id
            SysRoleMenu sysRoleMenu = new SysRoleMenu().setMenuId(menuId).setRoleId(roleId);
            //添加到集合中
            sysRoleMenus.add(sysRoleMenu);
        });
        sysRoleMenuService.saveBatch(sysRoleMenus);

        return Result.success(2000, "插入成功", menuIds);
    }

}
