package com.eafit.tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para creación de productos
 *
 * Contiene solo los campos necesarios para crear un producto.
 * Incluye validaciones específicas para la creación.
 */
@Schema(description = "Datos requeridos para crear un nuevo producto", example = """
        {
        "name": "Laptop Gaming Pro",
        "description": "Laptop de alto rendimiento para gaming con RTX 4070",
        "price": 2999.99,
        "category": "Electrónicos",
        "stock": 15
        }
        """)
public class CreateProductDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100caracteres")
    @Schema(description = "Nombre único del producto", example = "Smartphone Pro Max", minLength = 2, maxLength = 100, required = true)
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder 500caracteres")
    @Schema(description = "Descripción detallada del producto (opcional)", example = "Smartphone de última generación con cámara de 108MP ybatería de larga duración", maxLength = 500)
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato de precioinválido")
    @Schema(description = "Precio del producto en la moneda local", example = "1299.99", minimum = "0.01", maximum = "99999999.99", required = true, format = "decimal")
    private BigDecimal price;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "La categoría solo puedecontener letras y espacios")
    @Schema(description = "Categoría del producto", example = "Electrónicos", pattern = "^[A-Za-z\\s]+$", required = true, allowableValues = {
            "Electrónicos", "Libros", "Ropa", "Hogar",
            "Deportes", "Salud", "Automóviles" })
    private String category;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad inicial en inventario", example = "50", minimum = "0", maximum = "10000", required = true)
    private Integer stock;

    // Constructores
    public CreateProductDTO() {
    }

    public CreateProductDTO(String name, String description, BigDecimal price, String category, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}