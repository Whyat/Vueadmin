package com.whyat;

import com.whyat.entity.SysRole;
import com.whyat.entity.SysUser;
import com.whyat.service.SysRoleService;
import com.whyat.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VueadminJavaApplicationTests {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;

    @Test
    void contextLoads() {
        SysUser sysUser = new SysUser();
        sysUser.setId(1l);
        List<SysRole> rolesWithUser = sysRoleService.getRolesWithUser(sysUser);
        System.out.println(rolesWithUser);
    }

}
