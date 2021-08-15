package com.ij34.shiro.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ij34.shiro.model.*;
import com.ij34.shiro.service.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description CustomRealm
 * @Date 2021/8/15
 * @Created by www.ij34.com
 */
@Component
public class CustomRealm extends AuthorizingRealm {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IUsersService userService ;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IRolesService rolesService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRolePermissionService rolePermissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("doGetAuthorizationInfo");
        String username = (String)principals.getPrimaryPrincipal();
        if (username==null){
            return null;
        }
        Set<String> roles = new HashSet<>();
        Set<String> stringPermissions = new HashSet<>();
        Users user=userService.getOne(new QueryWrapper<>(Users.builder().username(username).build()),true);
        if (user==null) return null;
        userRoleService.list(new QueryWrapper<>(UserRole.builder().userId(user.getId()).build()))
                .forEach(userRole -> {
                    Roles role= rolesService.getById(userRole.getRoleId());
                    if (role!=null){
                        roles.add(role.getName());
                        rolePermissionService.list(new QueryWrapper<>(RolePermission.builder().roleId(role.getId()).build()))
                        .forEach(rolePermission -> {
                            Permission permission = permissionService.getById(rolePermission.getPermissionId());
                            stringPermissions.add(permission.getName());
                        });
                    }
                });
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(stringPermissions);
        return authorizationInfo;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("doGetAuthenticationInfo");
        String username = (String)token.getPrincipal();
        if (username==null || username.trim().length()<1){
            return null;
        }
        Users user = userService.getOne (new QueryWrapper<>(Users.builder().username(username).build()));
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
//        if(Boolean.TRUE.equals(user.getLocked())) {
//            throw new LockedAccountException(); //帐号锁定
//        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
//                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }


}