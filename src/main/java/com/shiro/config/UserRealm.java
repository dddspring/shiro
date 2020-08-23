package com.shiro.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiro.entity.User;
import com.shiro.mapper.UserMapper;
import com.shiro.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
//    @Autowired
//    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权");
        //给资源型进行授权
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获得认证通过的对象
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        //添加资源授权字符串
        simpleAuthorizationInfo.addStringPermission(user.getPerms());
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证");
        UsernamePasswordToken authenticationToken1 = (UsernamePasswordToken) authenticationToken;
        //数据库用户
        /**
         * 查询数据库获得用户
         */
        User users = userMapper.selectOne(new QueryWrapper<User>().lambda().like(User::getName,authenticationToken1.getUsername()));


        if(users == null){
            //用户名不存在
            return null;//shiro底层会抛出UnknownAccountException
        }
        return new SimpleAuthenticationInfo(users,users.getPassword(),"");

    }
}
