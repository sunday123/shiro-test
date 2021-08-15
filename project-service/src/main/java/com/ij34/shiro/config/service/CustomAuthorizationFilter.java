package com.ij34.shiro.config.service;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * @Description CustomRolesOrAuthorizationFilter
 * @Date 2021/8/15
 * @Created by www.ij34.com
 */
public class CustomAuthorizationFilter extends AuthorizationFilter {
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        Subject subject = getSubject(request, response);
        //filterChainDefinitionMap.put("/admin/**", "roles[admin,user]"); mappedValue <==> admin,user
        String[] rolesArray = (String[]) o;
        if (rolesArray == null || rolesArray.length == 0) {
            log.info("null 允许");
            return true;
        }
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        //当前subject是roles中的任意一个，则有权限访问
        for (String role : roles) {
            if (subject.hasRole(role)) {
                log.info("包含 允许");
                return true;
            }
        }
        log.info("不允许");
        return false;
    }
}
