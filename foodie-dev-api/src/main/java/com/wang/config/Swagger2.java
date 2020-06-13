package com.wang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 配置 api 文档 整合swagger2
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    /*访问的地址 */
    // Http://localhost:8088/swagger-ui.html  源路径
    //Http://localhost:8088/doc.html
    //配置sagger2 核心配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定api类型为 swagger2
                .apiInfo(apiInfo()) //用于定义 api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.wang.controller"))//扫描controller
                .paths(PathSelectors.any())  //所有controller
                .build();
    }
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("tianchi 接口api")//文档标题
                .contact(new Contact(
                        "wangzhuhongshi",
                        "Http://wangzhu.com",
                        "1094322722@qq.ocm"
                ))
                .description("tianchi 文档")
                .version("1.0.1")
                .termsOfServiceUrl("Http://wangzhu.com")
                .build();
    }
}
