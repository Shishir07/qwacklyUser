//package com.qwackly.user.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.handler.MappedInterceptor;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@EnableSwagger2
//@Configuration
//public class SwaggerConfig extends WebMvcConfigurerAdapter {
//    @Bean
//    public Docket productsApi(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.qwackly.user"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(metaData());
//    }
//    private ApiInfo metaData() {
//        return new ApiInfoBuilder()
//                .title("Qwackly REST API Documentation")
//                .description("\"V1 version of Qwackly\"")
//                .version("1.0.0")
//                .license("Apache License Version 2.0")
//                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
//                .build();
//    }
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        // Make Swagger meta-data available via <baseURL>/v2/api-docs/
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        // Make Swagger UI available via <baseURL>/swagger-ui.html
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
//    }
//
//}