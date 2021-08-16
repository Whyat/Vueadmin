package com.whyat.service;

import com.whyat.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
