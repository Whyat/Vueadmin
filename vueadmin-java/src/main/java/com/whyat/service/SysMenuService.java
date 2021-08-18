package com.whyat.service;

import com.whyat.entity.SysMenu;
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
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getAuthTree();
}
