package zup.com.desafiofinal.listtodo.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(" Sistema de Gerenciamento de Tarefas (List to do)")
                        .version("0.0.1")
                        .description("Desenvolvimento web fullStack- API first"));
    }
}