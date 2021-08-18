package com.whyat.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 前端的子菜单格式类
 *
 * ===返回的json格式===
 *
 * {
 *     name: 'SysDict',
 *     title: '数字字典',
 *     icon: 'el-icon-s-order',
 *     path: '/sys/tool',
 *     component: 'sys/SysTool'
 * }
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SubMenuDto implements Serializable {

    private static final long serialVersionUID = 1338465083821171616L;

    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SubMenuDto> children = new ArrayList<>();

}
