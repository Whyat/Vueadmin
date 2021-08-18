package com.whyat.service;

import com.whyat.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whyat.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> getRolesWithUser(SysUser sysUser);
}
