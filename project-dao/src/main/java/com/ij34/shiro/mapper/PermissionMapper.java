package com.ij34.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ij34.shiro.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-08-15
 */
@Component
public interface PermissionMapper extends BaseMapper<Permission> {

}
