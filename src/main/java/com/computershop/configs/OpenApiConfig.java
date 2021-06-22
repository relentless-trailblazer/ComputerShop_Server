package com.computershop.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
    	List<Server> listServer = new ArrayList<Server>();
		Server server = new Server();
		server.url("https://computer-shop-hit.herokuapp.com/");
        return new OpenAPI()
                .servers(listServer)
                .info(new Info().title("Loda Application API")
                                .description("Sample OpenAPI 3.0")
                                .contact(new Contact()
                                                 .email("tuanmc2885@gmail.com@gmail.com")
                                                 .name("tuan")
                                                 .url(""))
                                .license(new License()
                                                 .name("Apache 2.0")
                                                 .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                                				 .version("1.0.0"));
    }
}