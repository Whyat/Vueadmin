package com.whyat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whyat.common.dto.SubMenuDto;
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

import java.util.ArrayList;
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

    /**
     * 获取用户的子菜单列表
     * @param userId 用户id
     * @return
     */
    @Override
    public List<SubMenuDto> getCurrentUserSubMenuList(Long userId) {
        //1.获取用户对应的权限列表
        List<Long> menusIds = sysUserMapper.getMenusIds(userId);
        List<SysMenu> menus = sysMenuService.listByIds(menusIds);
        //2.转成DTO对象

        //2.1 SysMenu 转成 SysMenuTree对象
        List<SysMenu> sysMenuTree =  buildMenuTree(menus);

        return convert(sysMenuTree);
    }

    /**
     * 删除角色id是roleId的权限缓存
     * @param roleId
     */
    @Override
    public void clearRedisUserAuthInfoBySysRoleId(Long roleId) {
        //根据角色id在关联表中查询关联用户
        List<SysUser> sysUsers =
                list(new QueryWrapper<SysUser>()
                        .inSql("id", "select user_id from sys_user_role where role_id =" + roleId));
        //删除用户的缓存
        sysUsers.forEach(
                user -> redisUtil.hdel(Const.GRANTED_AUTHORITY, user.getUsername())
        );
    }

    /**
     * 把SysMenu实体类列表转化成树状结构
     * @param menus SysMenu集合
     * @return
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        //用于存放SysMenu树的集合
        List<SysMenu> sysMenuTree = new ArrayList<>();
        //1.遍历每一个SysMenu
        for (SysMenu menu : menus) {
            //2.继续第二次遍历，找到父循环遍历元素的子节点
            //根据id判断
            for (SysMenu m : menus) {
                //不能用==因为是包装类，有缓存127以后就不行了
                if (m.getParentId().equals(menu.getId())){
                    menu.getChildren().add(m);
                }
            }
            //3.每次父循环遍历完成判断是不是根节点，是就放入树集合中
            if (menu.getParentId() == 0 )
                sysMenuTree.add(menu);
        }
        return sysMenuTree;
    }

    /**
     * 把SysMenu的树状结构对应到前端属性
     * @param sysMenuTree SysMenu树结构，被填充了children属性的SysMenu
     * @return
     */
    public List<SubMenuDto> convert(List<SysMenu> sysMenuTree) {
        //新建用于存储前端接收的集合菜单树subMenuDtos
        List<SubMenuDto> subMenuDtoTree = new ArrayList<>();
        //1.把sysMenuTree转成subMenuDtos
        sysMenuTree.forEach(menuTree -> {
            SubMenuDto subMenuDto = new SubMenuDto()
                    .setId(menuTree.getId())
                    .setName(menuTree.getPerms())
                    .setTitle(menuTree.getName())
                    .setIcon(menuTree.getIcon())
                    .setPath(menuTree.getPath())
                    .setComponent(menuTree.getComponent());
            //2.如果menuTree有子节点,递归调用当前方法
            if (menuTree.getChildren().size() > 0){
                subMenuDto.setChildren(convert(menuTree.getChildren()));
            }
            //3.每次遍历完成添加到前端的dto集合菜单树
            subMenuDtoTree.add(subMenuDto);
        });
        return subMenuDtoTree;
    }

}
