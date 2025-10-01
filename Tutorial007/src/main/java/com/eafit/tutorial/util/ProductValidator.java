package com.eafit.tutorial.util;
import com.eafit.tutorial.dto.CreateProductDTO;

import com.eafit.tutorial.exception.ValidationException;

import org.springframework.stereotype.Component;


import java.math.BigDecimal;

import java.util.HashMap;

import java.util.Map;
/**

* Validador personalizado para lógica de negocio de productos

*

* Implementa validaciones complejas que van más allá de las anotaciones básicas.

*/
@Component
public class ProductValidator {
    private static final String[] FORBIDDEN_WORDS = {"test", "prueba", "demo", "temporal"};

private static final BigDecimal MAX_PRICE = new BigDecimal("100000.00");

private static final int MAX_STOCK = 10000;


/**

* Valida reglas de negocio para creación de productos

*/

public void validateForCreation(CreateProductDTO productDTO) {

Map<String, String> errors = new HashMap<>();


// Validar nombre no contenga palabras prohibidas

if (containsForbiddenWords(productDTO.getName())) {

errors.put("name", "El nombre del producto no puede contener palabras prohibidas como 'test', 'demo', etc.");

}
// Validar precio máximo

if (productDTO.getPrice().compareTo(MAX_PRICE) > 0) {

errors.put("price", "El precio no puede exceder $" + MAX_PRICE);

}


// Validar stock máximo

if (productDTO.getStock() > MAX_STOCK) {

errors.put("stock", "El stock no puede exceder " + MAX_STOCK + " unidades");

}


// Validar coherencia precio-categoría

validatePriceCategoryCoherence(productDTO.getPrice(), productDTO.getCategory(), errors);


if (!errors.isEmpty()) {

throw new ValidationException("Error de validación de reglas de negocio", errors);

}

}


/**

* Verifica si el nombre contiene palabras prohibidas

*/
private boolean containsForbiddenWords(String name) {

if (name == null) return false;


String lowerName = name.toLowerCase();

for (String forbidden : FORBIDDEN_WORDS) {

if (lowerName.contains(forbidden)) {

return true;

}

}

return false;

}


/**

* Valida coherencia entre precio y categoría

*/

private void validatePriceCategoryCoherence(BigDecimal price, String category, Map<String, String> errors) {

if (price == null || category == null) return;


String lowerCategory = category.toLowerCase();


// Electrónicos deben ser caros

if (lowerCategory.contains("electrón") && price.compareTo(new BigDecimal("50.00")) < 0) {
    errors.put("price", "Los productos electrónicos deben tener un precio mínimo de $50.00");

}


// Libros no pueden ser muy caros

if (lowerCategory.contains("libro") && price.compareTo(new BigDecimal("200.00")) > 0) {

errors.put("price", "Los libros no pueden exceder $200.00");

}


// Ropa debe tener precio razonable

if (lowerCategory.contains("ropa") || lowerCategory.contains("vestimenta")) {

if (price.compareTo(new BigDecimal("10.00")) < 0 || price.compareTo(new BigDecimal("1000.00")) > 0) {

errors.put("price", "La ropa debe tener un precio entre $10.00 y $1,000.00");

}

}

}


/**

* Valida que el stock sea apropiado para la categoría

*/

public void validateStockForCategory(String category, Integer stock) {

if (category == null || stock == null) return;
String lowerCategory = category.toLowerCase();


// Productos digitales no deberían tener stock limitado

if ((lowerCategory.contains("digital") || lowerCategory.contains("software")) && stock < 1000) {

throw new ValidationException("Los productos digitales deberían tener stock alto (mínimo 1000)");

}


// Productos perecederos no deberían tener stock muy alto

if ((lowerCategory.contains("comida") || lowerCategory.contains("alimento")) && stock > 100) {

throw new ValidationException("Los productos perecederos no deberían tener stock mayor a 100");

}

}
}
