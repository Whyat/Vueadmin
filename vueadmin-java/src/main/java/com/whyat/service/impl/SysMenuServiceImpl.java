package com.whyat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whyat.entity.SysMenu;
import com.whyat.mapper.SysMenuMapper;
import com.whyat.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whyat.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    SysUserService sysUserService;

    @Override
    public List<SysMenu> getAuthTree() {
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        return buildMenuTree(sysMenus);
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

}
