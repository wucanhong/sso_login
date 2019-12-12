package com.sso.config;

import com.sso.realm.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/6 17:09
 * @Version 1.0
 */
@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 过滤器默认权限表 {anon=anon, authc=authc, authcBasic=authcBasic, logout=logout,
     * noSessionCreation=noSessionCreation, perms=perms, port=port,
     * rest=rest, roles=roles, ssl=ssl, user=user}
     * <p>
     * anon, authc, authcBasic, user 是第一组认证过滤器
     * perms, port, rest, roles, ssl 是第二组授权过滤器
     * <p>
     * user 和 authc 的不同：当应用开启了rememberMe时, 用户下次访问时可以是一个user, 但绝不会是authc,
     * 因为authc是需要重新认证的, user表示用户不一定已通过认证, 只要曾被Shiro记住过登录状态的用户就可以正常发起请求,比如rememberMe
     * 以前的一个用户登录时开启了rememberMe, 然后他关闭浏览器, 下次再访问时他就是一个user, 而不会authc
     *
     * @param securityManager 初始化 ShiroFilterFactoryBean 的时候需要注入 SecurityManager
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或者 "/login"映射
        shiroFilterFactoryBean.setLoginUrl("/notLogin");
        // 设置没有权限时跳转的 url
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");

        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 游客，开发权限
        filterChainDefinitionMap.put("/guest/**", "anon");
        // 用户，需要角色权限 "user"
        filterChainDefinitionMap.put("/user/**", "roles[user]");
        // 管理员，需要角色权限 "admin"
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        // 开发登陆接口
        filterChainDefinitionMap.put("/login", "anon");
        // 其余接口一律拦截
        // 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        log.info("*********************Shiro拦截器工厂类注入成功************************");
        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(customRealm);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 记住我的配置
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间 ,单位秒;-->
        simpleCookie.setMaxAge(120);
        return simpleCookie;
    }
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie 加密的密钥 建议每个项目都不一样，默认AES算法 密钥长度(128 256 512 位)
        rememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return rememberMeManager;
    }

}
