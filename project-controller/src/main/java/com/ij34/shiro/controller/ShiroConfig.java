package com.ij34.shiro.controller;

import com.ij34.shiro.config.service.CustomAuthorizationFilter;
import com.ij34.shiro.config.service.CustomRealm;
import com.ij34.shiro.config.service.CustomSessionManager;
import com.ij34.shiro.model.Permission;
import com.ij34.shiro.service.IPermissionService;
import com.ij34.shiro.service.ShiroService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description ShiroConfig
 * @Date 2021/8/15
 * @Created by www.ij34.com
 */
@Configuration
public class ShiroConfig {


    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShiroService shiroService;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        log.info("ShiroFilterFactoryBean");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/pub/loginPage");
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/noPermit");

        //设置自定义Filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("roleOrFilter", new CustomAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //拦截路径，必须使用:LinkedHashMap，要不然拦截效果会时有时无，因为使用的是无序的Map
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //key=正则表达式路径，value=org.apache.shiro.web.filter.mgt.DefaultFilter
        //退出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        //匿名可以访问，游客模式/
        filterChainDefinitionMap.put("/pub/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        //放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui/index.html", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v3/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");

        //登录用户才可以访问
        filterChainDefinitionMap.put("/authc/**", "authc");
        //管理员角色才能访问
//        filterChainDefinitionMap.put("/admin/**", "roles[admin,user]");
        filterChainDefinitionMap.put("/admin/**", "roleOrFilter[admin]");
        //有编辑权限才能访问
//        filterChainDefinitionMap.put("/article/update", "perms[article_update]");
//        filterChainDefinitionMap.put("/article/add", "perms[article_add]");
        for (Permission permission : shiroService.getAllPermission()) {
            filterChainDefinitionMap.put(permission.getUrl(), "perms[" + permission.getName() + "]");
        }


        //authc：url必须通过认证才可以访问
        //anon：url可以匿名访问
        //过滤链是顺序执行，从上而下，一般把/**，放到最下面
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Resource
    CustomRealm customRealm;

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //如果不是前后端分离，不用设置setSessionManager
        securityManager.setSessionManager(sessionManager());
        //使用自定义cacheManager
        securityManager.setCacheManager(cacheManager());


        securityManager.setRealm(customRealm);
        return securityManager;
    }

    private CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        //设置缓存过期时间
        redisCacheManager.setExpire(60);
        return redisCacheManager;
    }

    private SessionManager sessionManager() {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());
        return customSessionManager;
    }

    private SessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(getRedisManager());
        return redisSessionDAO;
    }

    private IRedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();

        redisManager.setHost(host);

        redisManager.setDatabase(database);
        return redisManager;
    }

    @Value("${redis.address}")
    private String host;
    @Value("${redis.database}")
    private int database;

}
