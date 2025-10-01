package com.eafit.tutorial.config;

import io.swagger.v3.oas.models.examples.Example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

/**
 * Configuración de ejemplos reutilizables para OpenAPI
 */
@Configuration
public class OpenApiExamples {
    @Bean
    public Map<String, Example> globalExamples() {
        return Map.of(
                "SuccessResponse", createSuccessExample(),
                "ErrorResponse", createErrorExample(),
                "ValidationErrorResponse", createValidationErrorExample(),
                "PaginatedResponse", createPaginatedExample());
    }

    private Example createSuccessExample() {
        return new Example()
                .summary("Respuesta exitosa")
                .description("Formato estándar de respuesta exitosa")
                .value("""
                        {
                        "success": true,
                        "message": "Operación completada exitosamente",
                        "data": { "id": 1, "name": "Producto ejemplo" },
                        "timestamp": "2024-01-15T10:30:00",
                        "statusCode": 200
                        }
                        """);
    }

    private Example createErrorExample() {
        return new Example()
                .summary("Respuesta de error")
                .description("Formato estándar de respuesta de error")
                .value("""
                        {
                        "success": false,
                        "message": "Error en la operación",
                        "data": {
                        "errorCode": "PRODUCT_NOT_FOUND",
                        "message": "Producto no encontrado",
                        "details": { "productId": 123 },
                        "timestamp": "2024-01-15T10:30:00"
                        },
                        "timestamp": "2024-01-15T10:30:00",
                        "statusCode": 404
                        }
                        """);
    }

    private Example createValidationErrorExample() {
        return new Example()
                .summary("Error de validación")
                .description("Respuesta cuando hay errores de validación en losdatos")
                .value("""
                        {
                        "success": false,
                        "message": "Error de validación en los datos enviados",
                        "data": {
                        "errorCode": "VALIDATION_ERROR",
                        "message": "Error de validación en los datos enviados",
                        "details": {
                        "name": "El nombre del producto es obligatorio",
                        "price": "El precio debe ser mayor a 0",
                        "category": "La categoría es obligatoria"
                        },
                        "timestamp": "2024-01-15T10:30:00"
                        },
                        "timestamp": "2024-01-15T10:30:00",
                        "statusCode": 400
                        }
                        """);
    }

    private Example createPaginatedExample() {
        return new Example()
                .summary("Respuesta paginada")
                .description("Formato de respuesta con paginación")
                .value("""
                        {
                        "success": true,
                        "message": "Productos paginados obtenidos exitosamente",
                        "data": {
                        "content": [
                        { "id": 1, "name": "Producto 1", "price": 99.99 },
                        { "id": 2, "name": "Producto 2", "price": 149.99 }
                        ],
                        "page": {
                        "number": 0,
                        "size": 20,
                        "totalElements": 150,
                        "totalPages": 8,
                        "first": true,
                        "last": false,
                        "hasNext": true,
                        "hasPrevious": false
                        }
                        },
                        "timestamp": "2024-01-15T10:30:00",
                        "statusCode": 200
                        }
                        """);
    }
}