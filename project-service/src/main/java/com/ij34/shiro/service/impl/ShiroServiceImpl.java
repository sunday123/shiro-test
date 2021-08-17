package com.ij34.shiro.service.impl;

import com.ij34.shiro.model.Permission;
import com.ij34.shiro.service.IPermissionService;
import com.ij34.shiro.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: ShiroServiceImpl
 * date: 2021/8/15
 * author: by www.ij34.com
 */
@Service
public class ShiroServiceImpl implements ShiroService {


    @Autowired
    private IPermissionService permissionService;

    @Override
    public List<Permission> getAllPermission() {
        return permissionService.list();
    }
}
