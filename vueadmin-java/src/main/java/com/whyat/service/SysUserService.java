package com.whyat.service;

import com.whyat.common.dto.SubMenuDto;
import com.whyat.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    String getUserAuthInfo(Long userId);

    List<SubMenuDto> getCurrentUserSubMenuList(Long userId);

    void clearRedisUserAuthInfoBySysRoleId(Long id);

    void clearSysUserAuthInfo(String username);

    SysUser resetPass(Long userId);
}
