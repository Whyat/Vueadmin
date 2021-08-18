package com.whyat.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.whyat.entity.SysUser;
import com.whyat.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    SysUserService sysUserService;

    /**
     * 交给Sucurity用户信息，让它去比对传进来的用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUserName(username);
        //没有查询到，抛出异常，secuirty会处理
        if (sysUser == null) {
            //security处理并没有抛出这个异常，抛了个AuthException，所以打印不出这个异常信息
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new CustomUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthorities(sysUser.getId()));
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthorities(Long userId) {
        //返回用户的角色(ROLE_角色名)+权限(sys:user:list)字符串
        //字符串格式：ROLE_admin,ROLE_normal,sys:user:list,...
        String autoInfo = sysUserService.getUserAuthInfo(userId);
        //就算没有权限也不能全空白，否则会抛异常
        //不这样写的话白名单里面的login等也会无法正常进行
        if(StringUtils.isBlank(autoInfo)){
            autoInfo = ".";
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(autoInfo);
    }

}
