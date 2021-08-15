package com.ij34.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ij34.shiro.mapper.RolePermissionMapper;
import com.ij34.shiro.model.RolePermission;
import com.ij34.shiro.service.IRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-权限 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-08-15
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
