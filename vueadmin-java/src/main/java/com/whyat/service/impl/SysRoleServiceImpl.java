package com.whyat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whyat.entity.SysRole;
import com.whyat.entity.SysUser;
import com.whyat.mapper.SysRoleMapper;
import com.whyat.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> getRolesWithUser(SysUser sysUser) {
        List<SysRole> sysRoles = this.list(
                new QueryWrapper<SysRole>().
                        inSql("id", "select role_id from sys_user_role where user_id =" + sysUser.getId()));
        return sysRoles;
    }
}
