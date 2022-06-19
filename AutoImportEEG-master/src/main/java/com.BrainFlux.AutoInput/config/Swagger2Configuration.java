package com.BrainFlux.AutoInput.config;


import com.BrainFlux.AutoInput.common.http.EnumStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    //api接口包扫描路径
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.BrainFlux.AutoInput";
    public static final String VERSION = "1.0.0";
    @Bean
    public Docket createRestApi() {

        //添加全局响应状态码
        //  http://localhost:8080/swagger-ui.html
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(EnumStatus.values()).forEach(enumStatus -> {
            responseMessageList.add(
                    new ResponseMessageBuilder().code(enumStatus.getStatus()).message(enumStatus.getMessage()).responseModel(
                            new ModelRef(enumStatus.getMessage())).build()
            );
        });
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring boot  Api ") //设置文档的标题
                .description("Spring boot  Api 接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
                .build();
    }
}