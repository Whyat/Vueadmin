package com.whyat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class SysRoleMenu {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;


}
