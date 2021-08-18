package com.whyat.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whyat.common.dto.SubMenuDto;
import com.whyat.common.lang.Result;
import com.whyat.entity.SysRole;
import com.whyat.entity.SysUser;
import com.whyat.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    /**
     *获取前端子菜单列表和其拥有权限
     * @return
     */
    @GetMapping("/menu")
    public Result menu(Principal principal){
        String id = principal.getName();
        Long userId = Long.valueOf(id);
        //1.获取权限信息
        String authInfo = sysUserService.getUserAuthInfo(userId);
        String[] authArr = StringUtils.tokenizeToStringArray(authInfo, ",");

        //2.获取导航栏信息
        List<SubMenuDto> subMenuDtos = sysUserService.getCurrentUserSubMenuList(userId);

        //3.返回结果
        return Result.success(MapUtil.builder()
                .put("permList", authArr)
                .put("subMenuList", subMenuDtos).build());
    }


    @GetMapping("/save")
    public Result save() {
        return Result.success("");
    }

    @GetMapping("/delete")
    public Result delete() {
        return Result.success("");
    }

    @GetMapping("/update")
    public Result update() {
        return Result.success("");
    }

    /**
     * 查询角色信息，在分配角色选项的时候用到
     * @param userId
     * @return
     */
    @GetMapping("/info/{userId}")
    public Result info(@PathVariable Long userId) {
        //1.根据用户id查询用户
        SysUser sysUser = sysUserService.getById(userId);
        //断言查询出的数据不为空，为空就会抛出IlleaglArgumentsException
        //在全局异常处理类中捕获返回错误信息
        Assert.notNull(sysUser, "找不到该管理员用户");
        //2.查询用户相关联的角色
        List<SysRole> sysroles = sysRoleService.getRolesWithUser(sysUser);
        return Result.success(sysUser);
    }

    @GetMapping("/list")
    public Result list(String username) {

        return Result.success("");
    }

    @GetMapping("/resetpass")
    public Result resetpass() {
        return Result.success("");
    }

}
