package com.eafit.tutorial.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación automática
 *
 * Define la información general de la API, esquemas de seguridad
 * y respuestas comunes reutilizables.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Products API - Tutorial Spring Boot", version = "2.0", description = """
         **API REST completa para gestión de productos**
         Esta API demuestra las mejores prácticas en el desarrollo de
        servicios REST con Spring Boot:
         ### Características principales:
         - **CRUD completo** con validaciones robustas
         - **Paginación** y filtrado avanzado
         - **Manejo de errores** centralizado y consistente
         - **DTOs** para separación de responsabilidades
         - **Soft delete** para mantener integridad de datos
         - **Documentación automática** con ejemplos interactivos
         ### Cómo usar esta API:
         1. **Explorar endpoints** usando la interfaz interactiva
         2. **Probar operaciones** con los ejemplos incluidos
         3. **Revisar esquemas** de datos en la sección Schemas
         4. **Entender errores** con las respuestas de ejemplo
         ### Recursos adicionales:
         - Código fuente: [GitHub Repository](#)
         - Tutorial completo: [Spring Boot Guide](#)
         - Curso SOA: [Universidad EAFIT](#)
         """, contact = @Contact(name = "Sebastián Gómez", email = "sgomez@eafit.edu.co", url = "https://eafit.edu.co"), license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")), servers = {
        @Server(description = "Servidor de desarrollo", url = "http://localhost:8080"),
        @Server(description = "Servidor de pruebas", url = "https://api-test.eafit.edu.co"),
        @Server(description = "Servidor de producción", url = "https://api.eafit.edu.co")
})
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer", description = "Ingrese el token JWT en el formato: Bearer {token}")
@SecurityScheme(name = "API Key", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "X-API-Key", description = "API Key para autenticación de servicios externos")
public class OpenApiConfig {
    /**
     * Configuración personalizada de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        // Respuestas reutilizables
                        .addResponses("NotFound", createNotFoundResponse())
                        .addResponses("BadRequest", createBadRequestResponse())
                        .addResponses("InternalServerError",
                                createInternalServerErrorResponse())
                        .addResponses("Unauthorized", createUnauthorizedResponse())
                        .addResponses("Forbidden", createForbiddenResponse())
                        // Esquemas reutilizables
                        .addSchemas("ValidationError", createValidationErrorSchema())
                        .addSchemas("BusinessError", createBusinessErrorSchema()));
    }

    /**
     * Respuesta estándar para errores 404
     */
    private ApiResponse createNotFoundResponse() {
        return new ApiResponse()
                .description("Recurso no encontrado")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .example(createErrorExample(
                                        "PRODUCT_NOT_FOUND",
                                        "Producto no encontrado con ID: 123",
                                        "El producto solicitado no existe o ha sidoeliminado"))));
    }

    /**
     * Respuesta estándar para errores 400
     */
    private ApiResponse createBadRequestResponse() {
        return new ApiResponse()
                .description("Solicitud inválida")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .example(createValidationErrorExample())));
    }

    /**
     * Respuesta estándar para errores 500
     */
    private ApiResponse createInternalServerErrorResponse() {
        return new ApiResponse()
                .description("Error interno del servidor")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .example(createErrorExample(
                                        "INTERNAL_SERVER_ERROR",
                                        "Ha ocurrido un error interno en el servidor",
                                        "Error inesperado durante el procesamiento"))));
    }

    /**
     * Respuesta estándar para errores 401
     */
    private ApiResponse createUnauthorizedResponse() {
        return new ApiResponse()
                .description("No autorizado - Token inválido o expirado")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .example(createErrorExample(
                                        "UNAUTHORIZED",
                                        "Token de acceso inválido o expirado",
                                        "Proporcione un token válido en el header Authorization"))));
    }

    /**
     * Respuesta estándar para errores 403
     */
    private ApiResponse createForbiddenResponse() {
        return new ApiResponse()
                .description("Acceso prohibido - Permisos insuficientes")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .example(createErrorExample(
                                        "FORBIDDEN",
                                        "No tiene permisos para realizar esta operación",
                                        "Su nivel de acceso no permite esta acción"))));
    }

    /**
     * Esquema para errores de validación
     */
    private io.swagger.v3.oas.models.media.Schema createValidationErrorSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>()
                .type("object")
                .description("Error de validación con detalles específicos porcampo")
                .addProperty("success", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("boolean")
                        .example(false))
                .addProperty("message", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("string")
                        .example("Error de validación"))
                .addProperty("timestamp", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("string")
                        .format("date-time"))
                .addProperty("data", new io.swagger.v3.oas.models.media.Schema<>().type("object")
                        .description("Detalles del error por campo"));
    }

    /**
     * Esquema para errores de negocio
     */
    private io.swagger.v3.oas.models.media.Schema createBusinessErrorSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>()
                .type("object")
                .description("Error de lógica de negocio")
                .addProperty("success", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("boolean")
                        .example(false))
                .addProperty("message", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("string")
                        .example("Error de negocio"))
                .addProperty("errorCode", new io.swagger.v3.oas.models.media.Schema<>()
                        .type("string")
                        .example("BUSINESS_RULE_VIOLATION"));
    }

    /**
     * Ejemplo genérico de error
     */
    private Object createErrorExample(String code, String message, String detail) {
        return java.util.Map.of(
                "success", false,
                "message", message,
                "timestamp", "2024-01-15T10:30:00",
                "statusCode", 404,
                "data", java.util.Map.of(
                        "errorCode", code,
                        "message", message,
                        "details", java.util.Map.of("info", detail),
                        "timestamp", "2024-01-15T10:30:00"));
    }

    /**
     * Ejemplo de error de validación
     */
    private Object createValidationErrorExample() {
        return java.util.Map.of(
                "success", false,
                "message", "Error de validación en los datos enviados",
                "timestamp", "2024-01-15T10:30:00",
                "statusCode", 400,
                "data", java.util.Map.of(
                        "errorCode", "VALIDATION_ERROR",
                        "message", "Error de validación en los datos enviados",
                        "details", java.util.Map.of(
                                "name", "El nombre del producto es obligatorio",
                                "price", "El precio debe ser mayor a 0",
                                "category", "La categoría es obligatoria"),
                        "timestamp", "2024-01-15T10:30:00"));
    }
}