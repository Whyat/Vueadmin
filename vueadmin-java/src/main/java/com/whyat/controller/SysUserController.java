package com.whyat.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whyat.common.dto.PassDto;
import com.whyat.common.dto.SubMenuDto;
import com.whyat.common.lang.Const;
import com.whyat.common.lang.Result;
import com.whyat.entity.SysRole;
import com.whyat.entity.SysUser;
import com.whyat.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    //security默认的密码加密器
    @Autowired
    BCryptPasswordEncoder passsEncoder;

    /**
     * 获取前端子菜单列表和其拥有权限
     *
     * @return
     */
    @GetMapping("/menu")
    public Result menu(Principal principal) {
        String id = principal.getName();
        Long userId = Long.valueOf(id);
        //1.获取权限信息
        String authInfo = sysUserService.getUserAuthInfo(userId);
        //按security的权限信息字符串形式解析成权限字符数组
        String[] authArr = org.springframework.util.StringUtils.tokenizeToStringArray(authInfo, ",");

        //2.获取导航栏信息
        List<SubMenuDto> subMenuDtos = sysUserService.getCurrentUserSubMenuList(userId);

        //3.返回结果
        return Result.success(MapUtil.builder()
                .put("permList", authArr)
                .put("subMenuList", subMenuDtos).build());
    }


    /**
     * 校验用户信息必要字段及格式
     *
     * @param sysUser 系统用户实体
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:save')")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated SysUser sysUser) {
        //1.设置基本信息
        //1.1设置创建时间
        sysUser.setCreated(LocalDateTime.now());
        //1.2设置头像url
        String avatar = sysUser.getAvatar();
        avatar = StringUtils.isNotBlank(avatar) ? avatar : Const.DEFAULT_SYSUSER_AVATAR;
        sysUser.setAvatar(avatar);
        //1.3设置密码
        String pwd = sysUser.getPassword();
        //判断密码是否为空，不为空则使用传参密码加密，为空则使用默认密码加密
        pwd = StringUtils.isNotBlank(pwd) ? passsEncoder.encode(pwd) : passsEncoder.encode(Const.DEFAULT_SYSUSER_PASSWORD);
        sysUser.setPassword(pwd);
        sysUser.setPassword(passsEncoder.encode(Const.DEFAULT_SYSUSER_PASSWORD));
        //2.设置好的用户信息插入数据库
        sysUserService.save(sysUser);
        return Result.success(sysUser);
    }

    /**
     * 可支持单删除和批量删除
     * @param userIds 用户id数组，可以为单个元素
     * @return
     */
    @Transactional
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long userIds[]) {
        //1.删除sys_user表中的数据
        sysUserService.removeByIds(Arrays.asList(userIds));
        //2.删除中间表的信息sys_user_role
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", userIds));
        return Result.success(2000, "系统用户删除成功", null);
    }

    /**
     * 修改系统用户信息
     *
     * @param sysUser 系统用户实体
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated SysUser sysUser) {
        //1.设置更新时间
        sysUser.setUpdated(LocalDateTime.now());
        //2.插入数据库
        boolean outcome = sysUserService.updateById(sysUser);

        return Result.success(outcome);
    }

    /**
     * 查询角色信息，在分配角色选项的时候用到
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/info/{userId}")
    public Result info(@PathVariable Long userId) {
        //1.根据用户id查询用户
        SysUser sysUser = sysUserService.getById(userId);
        //断言查询出的数据不为空，为空就会抛出IlleaglArgumentsException
        //在全局异常处理类中捕获返回错误信息
        Assert.notNull(sysUser, "找不到该管理员用户");
        //2.查询用户相关联的角色
        List<SysRole> sysroles = sysRoleService.getRolesWithUser(sysUser);
        sysUser.setSysRoles(sysroles);
        return Result.success(sysUser);
    }

    /**
     * 批量查询，支持分页模糊查询
     *
     * @param username 用户名
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list(String username) {
        //1.不为空则启用模糊查询用户的分页数据
        Page<SysUser> userPage = sysUserService.page(this.getPage(),
                new QueryWrapper
                        <SysUser>().like(StringUtils.isNotBlank(username), "username", username));
        //2.根据每一个用户查询其关联的角色，添加在SysUser的SysRole属性中
        List<SysUser> sysUsers = userPage.getRecords();
        sysUsers.forEach(user -> {
            List<SysRole> sysRoles = sysRoleService.getRolesWithUser(user);
            user.setSysRoles(sysRoles);
        });
        //3.返回分页数据
        return Result.success(userPage);
    }

    /**
     * 给用户分配角色
     * @param userId 要分配的用户id
     * @param roleIds 角色id数组
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:role')")
    @Transactional
    @PostMapping("/role/{userId}")
    public Result update(@PathVariable("userId")Long userId, @RequestBody Long roleIds[]) {
        //1.根据用户id和传参的角色id生成中间表实体集合
        ArrayList<SysUserRole> sysUserRoles = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId ->{
            SysUserRole sysUserRole = new SysUserRole().setUserId(userId).setRoleId(roleId);
            sysUserRoles.add(sysUserRole);
        });
        //2.删除原来的中间表的数据
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //3.删除redis用户权限缓存
        sysUserService.clearSysUserAuthInfo(sysUserService.getById(userId).getUsername());
        //3.插入中间表
        boolean b = sysUserRoleService.saveBatch(sysUserRoles);
        return Result.success("");
    }

    @PreAuthorize("hasAuthority('sys:user:repass')")
    @PostMapping("/resetpass")
    public Result resetpass(@RequestBody Long userId) {
        SysUser sysUser = sysUserService.resetPass(userId);
        return Result.success(sysUser);
    }

    @PostMapping("/updatePass")
    public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {

        SysUser sysUser = sysUserService.getById(principal.getName());

        boolean matches = passsEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            return Result.fail("旧密码不正确");
        }

        sysUser.setPassword(passsEncoder.encode(passDto.getPassword()));
        sysUser.setUpdated(LocalDateTime.now());

        sysUserService.updateById(sysUser);
        return Result.success("密码修改成功");
    }


}
