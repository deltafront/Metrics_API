package companyB.metrics.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Main extends SpringBootServletInitializer
{
    @Value("${jdbc.username}")
    private  String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driverManager")
    private String driverManager;
    @Value("${api.version}")
    private String apiVersion;
    @Value("${api.title}")
    private String apiTitle;
    @Value("${api.description}")
    private String apiDescription;
    @Value("${api.license.name}")
    private String license;
    @Value("${api.license.url}")
    private String licenseUrl;
    @Value("${api.contact}")
    private String apiContact;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder
                .banner(new ResourceBanner(new DefaultResourceLoader().getResource("banner.txt")))
                .sources(Main.class);

        return builder;
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                apiTitle,
                apiDescription,
                apiVersion,
                "Terms of service",
                apiContact,
                license,
                licenseUrl);
    }
}