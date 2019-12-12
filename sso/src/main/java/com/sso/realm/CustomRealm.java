package com.sso.realm;

import com.sso.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName CustomRealm
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/6 17:53
 * @Version 1.0
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;


    /**
     * 获取身份验证信息
     * Shiro中，最终是通过Realm来获取应用程序中的用户、角色及权限信息的
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("--------------------身份认证方法---------------------");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从数据库中获取对应的用户名密码
        String password = userMapper.getPassword(token.getUsername());
        if (null == password) {
            throw new AccountException("用户名不正确");
        }else if (!password.equals(new String(token.getPassword()))){
            throw new AccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        log.info("-----------权限认证--------------");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取该用户角色
        String role = userMapper.getRole(username);
        Set<String> set = new HashSet<>();
        // 需要将role封装到set作为info.setRoles（）的参数
        set.add(role);
        // 设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }


}
