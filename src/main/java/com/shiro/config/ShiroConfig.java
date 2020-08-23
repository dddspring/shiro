package com.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //创建 ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> map =new LinkedHashMap<String,String>();
        map.put("/main","anon");
        map.put("/login","anon");

        /**
         * 添加授权信
         * 当授权的拦截后shiro会自动跳转到未授权的页面
         */
        map.put("/add","perms[addperms]");
        map.put("/update","perms[updateperms]");
        //设置为授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");

        map.put("/**","authc");
        //map.put("/update","authc");
        //修改调整的登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    //创建DefaultWebSecurityManager
    @Bean(name="defaultWebSecurityManager")
    public DefaultWebSecurityManager getSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    //创建Realm
    @Bean(name="userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }

    //配置ShiroDialect ，用于thymeleaf和shiro标签配合使用
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
