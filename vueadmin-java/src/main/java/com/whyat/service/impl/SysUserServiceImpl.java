package com.whyat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whyat.entity.SysUser;
import com.whyat.mapper.SysUserMapper;
import com.whyat.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

}
