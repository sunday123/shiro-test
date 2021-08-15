package com.ij34.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ij34.shiro.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 角色-权限 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-08-15
 */
@Component
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
