package br.com.senai.cronoanalise.comum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Cronoanálise API", version = "2.0",
                description = "Cronoanálise industrial: cadastros de empresas e produtos, registro dos tempos cronometrados e cálculo de "
                        + "tempo normal, tempo padrão, ciclos necessários e capacidade. Projeto acadêmico (SENAI). "
                        + "Para testar: faça login em POST /api/auth/login e use o token no botão Authorize."),
        security = @SecurityRequirement(name = "bearer"))
@SecurityScheme(name = "bearer", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {
}
