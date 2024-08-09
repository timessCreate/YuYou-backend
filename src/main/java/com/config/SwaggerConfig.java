//package com.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//
///**
// * 自定义swagger配置类
// */
//@Configuration // 标明是配置类
//@EnableSwagger2 //开启swagger功能
//public class SwaggerConfig {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
//                //.groupName("分布式任务系统") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
//                .apiInfo(apiInfo()) // 用于生成API信息
//                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
//                // 扫描指定包下的接口，最为常用
//                .apis(RequestHandlerSelectors.basePackage("com.xxxx.project.tool.swagger"))
//                //.withClassAnnotation(RestController.class) // 扫描带有指定注解的类下所有接口
//                //.withMethodAnnotation(PostMapping.class) // 扫描带有指定注解的方法接口
//                //.apis(RequestHandlerSelectors.any()) // 扫描所有
//
//                // 选择所有的API,如果你想只为部分API生成文档，可以配置这里
//                .paths(PathSelectors.any()
//                        //.any() // 满足条件的路径，该断言总为true
//                        //.none() // 不满足条件的路径，该断言总为false（可用于生成环境屏蔽 swagger）
//                        //.ant("/user/**") // 满足字符串表达式路径
//                        //.regex("") // 符合正则的路径
//                )
//                .build();
//    }
//
//    /**
//     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
//     * @return
//     */
//    private ApiInfo apiInfo() {
//
//        Contact contact = new Contact(
//                "timess", // 作者姓名
//                "https://github.com/timessCreate/", // 作者网址
//                "xing3336314279@163.com"); // 作者邮箱
//
//        return new ApiInfoBuilder()
//                .title("用户中心") //  可以用来自定义API的主标题
//                .description("接口文档") // 可以用来描述整体的API
//                .termsOfServiceUrl("https://github.com/timessCreate") // 用于定义服务的域名（跳转链接）
//                .version("1.0") // 可以用来定义版本
//                .license("Swagger-的使用教程")
//                .licenseUrl("https://blog.csdn.net/weixin_55337402?type=blog")
//                .contact(contact)
//                .build(); //
//    }
//}

package com.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 自定义swagger配置类
 */
@Configuration // 标明是配置类
@EnableSwagger2 // 开启swagger功能
@EnableKnife4j // 开启 Knife4j 功能
@Profile("dev") //配置默认的环境为dev,指在dev环境下访问该接口文档会显示，
// 如果设置为prod,则无法访问，但环境存在bug,换成prod,程序无法启动
// TODO 可能是 swagger版本和 knife4j 版本不匹配
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.controller")) // 扫描指定包下的接口
                .paths(PathSelectors.any()) // 所有路径
                .build();
    }

    /**
     * 用于定义API主界面的信息
     * @return
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact(
                "timess", // 作者姓名
                "https://github.com/timessCreate/", // 作者网址
                "xing3336314279@163.com"); // 作者邮箱

        return new ApiInfoBuilder()
                .title("伙伴匹配后端接口") // API标题
                .description("接口文档") // 描述
                .termsOfServiceUrl("https://github.com/timessCreate") // 服务条款URL
                .version("1.0") // 版本
                .license("Apache 2.0") // 许可证类型
                .contact(contact)
                .build();
    }
}