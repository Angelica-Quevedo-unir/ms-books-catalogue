package es.unir.relatosdepapel.books.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${eureka.instance.hostname:localhost}")
    private String gatewayHostname;

    @Value("${server.port.gateway:80}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        String baseUrl = "http://" + gatewayHostname + ":" + serverPort;
        return new OpenAPI()
                .info(new Info()
                        .title("Books Catalogue")
                        .version("1.0")
                        .description("Books catalogue management for Relatos de Papel")
                )
                .servers(List.of(
                        new Server().url(baseUrl + "/book-catalogue").description("Servidor configurado dinámicamente")
                ));
    }
}