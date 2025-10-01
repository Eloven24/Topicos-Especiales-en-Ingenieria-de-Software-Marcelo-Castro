package com.eafit.tutorial.exception;

import com.eafit.tutorial.dto.ApiResponse;
import com.eafit.tutorial.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejo global de excepciones para toda la aplicación
 *
 * Captura y maneja diferentes tipos de excepciones,
 * proporcionando respuestas consistentes al cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de @Valid en request body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR",
                "Error de validación en los datos enviados",
                errors,
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Datos de entrada inválidos", 400));
    }

    /**
     * Maneja errores de validación de @Validated en parámetros
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolation(
            ConstraintViolationException ex) {
        logger.warn("Error de validación de parámetros: {}",
                ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        ErrorResponse errorResponse = new ErrorResponse(
                "PARAMETER_VALIDATION_ERROR",
                "Error de validación en parámetros",
                errors,
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Parámetros inválidos", 400));
    }

    /**
     * Maneja ProductNotFoundException
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleProductNotFound(
            ProductNotFoundException ex, WebRequest request) {
        logger.warn("Producto no encontrado: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "PRODUCT_NOT_FOUND",
                ex.getMessage(),
                Map.of("path", request.getDescription(false).replace("uri=",
                        "")),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Recurso no encontrado", 404));
    }

    /**
     * Maneja ProductAlreadyExistsException
     */
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleProductAlreadyExists(
            ProductAlreadyExistsException ex) {
        logger.warn("Producto ya existe: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "PRODUCT_ALREADY_EXISTS",
                ex.getMessage(),
                Map.of("suggestion", "Use un nombre diferente o actualice elproducto existente"),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Conflicto de recurso", 409));
    }

    /**
     * Maneja ValidationException personalizada
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            ValidationException ex) {
        logger.warn("Error de validación personalizado: {}",
                ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "CUSTOM_VALIDATION_ERROR",
                ex.getMessage(),
                ex.getErrors() != null ? ex.getErrors() : Map.of(),
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error de validación", 400));
    }

    /**
     * Maneja errores de tipo de dato incorrecto
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        logger.warn("Error de tipo de dato: {}", ex.getMessage());
        String message = String.format("El parámetro '%s' debe ser de tipo%s",
                ex.getName(),
                ex.getRequiredType().getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse(
                "TYPE_MISMATCH_ERROR",
                message,
                Map.of("parameter", ex.getName(), "expectedType",
                        ex.getRequiredType().getSimpleName()),
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Tipo de dato inválido", 400));
    }

    /**
     * Maneja errores de JSON malformado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        logger.warn("Error de formato JSON: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "MALFORMED_JSON",
                "El formato JSON enviado es inválido",
                Map.of("suggestion", "Verifique la sintaxis JSON del cuerpo de lapetición"),
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Formato JSON inválido", 400));
    }

    /**
     * Maneja IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalArgument(
            IllegalArgumentException ex) {
        logger.warn("Argumento ilegal: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "ILLEGAL_ARGUMENT",
                ex.getMessage(),
                Map.of("suggestion", "Verifique los valores enviados en lapetición"),
                LocalDateTime.now());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Argumento inválido", 400));
    }

    /**
     * Maneja cualquier excepción no controlada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGlobalException(
            Exception ex, WebRequest request) {
        logger.error("Error interno no controlado: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Ha ocurrido un error interno en el servidor",
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "path", request.getDescription(false).replace("uri=", "")),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor", 500));
    }
}