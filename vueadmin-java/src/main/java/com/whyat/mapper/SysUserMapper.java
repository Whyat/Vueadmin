package com.whyat.mapper;

import com.whyat.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Whyat
 * @since 2021-08-12
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getMenusIds(Long userId);
}
