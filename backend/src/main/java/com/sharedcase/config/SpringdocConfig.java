package com.sharedcase.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * ClassName: SwaggerConfig
 * Package: com.sharedcase.config
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 10:54
 */
@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电子病历共享系统 API 文档")
                        .description("基于 Spring Boot + Vue3 + 区块链 的医疗信息共享平台")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("向鹏")
                                .email("xp@example.com")
                                .url("https://github.com/xpeng"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目仓库")
                        .url("https://github.com/xpeng/sharedcase"));
    }
}
