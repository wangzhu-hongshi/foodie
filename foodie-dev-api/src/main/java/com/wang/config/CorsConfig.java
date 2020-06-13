package com.wang.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * 跨域配置类
 */
@Configuration
public class CorsConfig {
    public CorsConfig(){}

    @Bean
    public CorsFilter corsFilter(){
        //1.添加Cors配置信息
        CorsConfiguration config=new CorsConfiguration();
        config.addAllowedOrigin("*");//设置访问权限

        //设置是否可以发送cookie信息
        config.setAllowCredentials(true);

        //设置可以访问的请求方式
        config.addAllowedMethod("*");

        //设置所允许的header
        config.addAllowedHeader("*");

        //2.为url添加映射配置
        UrlBasedCorsConfigurationSource corsSource=new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);

        //3.返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }
}
