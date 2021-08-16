package com.whyat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.whyat.common.lang.Const;
import com.whyat.entity.SysMenu;
import com.whyat.entity.SysRole;
import com.whyat.entity.SysUser;
import com.whyat.mapper.SysUserMapper;
import com.whyat.service.SysMenuService;
import com.whyat.service.SysRoleService;
import com.whyat.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whyat.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthInfo(Long userId) {
        String username = sysUserMapper.selectById(userId).getUsername();
        String authInfo = "";

        //把用户权限【角色+权限】存入redis缓存中，避免每次访问都去数据库查询
        if (redisUtil.hHasKey(Const.GRANTED_AUTHORITY, username)) {
            authInfo =
                    (String) redisUtil.hget(Const.GRANTED_AUTHORITY, username);
        }else {
            authInfo = "";
            //1.获取角色信息

            //select * from sys_role where id in (
            //     select role_id
            //     from sys_user_role
            //     where user_id = 1
            // );
            List<SysRole> sysRoles = sysRoleService.list(new QueryWrapper<SysRole>().
                    inSql("id", "select role_id from sys_user_role where user_id =" + userId));

            if (sysRoles.size() > 0) {
                //把查出来的角色拼接成'ROLE_XXX,ROLE_XXX'类型字符串
                String roleCodes = sysRoles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authInfo = roleCodes;
            }

            //2.获取权限信息
            List<Long> menuIds = sysUserMapper.getMenusIds(userId);
            if (menuIds.size() > 0) {
                List<SysMenu> menus = sysMenuService.listByIds(menuIds);
                //把每个权限行取其perms字段拼接 sys:dict:list,sys:manage,...
                String menuInfo = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                authInfo = authInfo.concat(",").concat(menuInfo);
            }

            redisUtil.hset(Const.GRANTED_AUTHORITY, username, authInfo, 60 * 60);
        }

        return authInfo;
    }

}
