package com.eafit.tutorial.service;

import com.eafit.tutorial.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**

* Interface del servicio de productos

*

* Define las operaciones de negocio disponibles para productos.

*/

public interface ProductService {


/**

* Obtiene todos los productos activos

*/

List<Product> getAllProducts();
/**

* Obtiene productos activos con paginación

*/

Page<Product> getAllProducts(Pageable pageable);


/**

* Obtiene un producto por ID

*/

Optional<Product> getProductById(Long id);


/**

* Crea un nuevo producto

*/

Product createProduct(Product product);
/**

* Actualiza un producto existente

*/

Product updateProduct(Long id, Product product);


/**

* Elimina un producto (soft delete)

*/
void deleteProduct(Long id);


/**

* Busca productos por categoría

*/

List<Product> getProductsByCategory(String category);


/**

* Busca productos por rango de precio

*/

List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);


/**

* Busca productos por nombre

*/

List<Product> searchProductsByName(String name);


/**

* Obtiene productos con stock bajo

*/

List<Product> getProductsWithLowStock(Integer minStock);


/**
 * * Verifica si un producto existe
*/

boolean existsProduct(Long id);


/**

* Actualiza el stock de un producto

*/

Product updateStock(Long id, Integer newStock);

}