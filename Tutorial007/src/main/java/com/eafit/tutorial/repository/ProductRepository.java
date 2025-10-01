package com.eafit.tutorial.repository;
import com.eafit.tutorial.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**

* Repository para Product

*

* Interface que extiende JpaRepository para proporcionar

* operaciones CRUD básicas y consultas personalizadas.

*/

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {


/**

* Encuentra productos activos

*/
List<Product> findByActiveTrue();


/**

* Encuentra productos activos con paginación

*/

Page<Product> findByActiveTrue(Pageable pageable);


/**

* Encuentra productos por categoría

*/

List<Product> findByCategoryIgnoreCaseAndActiveTrue(String category);
/**

* Encuentra productos por rango de precio

*/

@Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.active = true")

List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,

@Param("maxPrice") BigDecimal maxPrice);


/**

* Busca productos por nombre (búsqueda parcial)

*/

@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
List<Product> findByNameContainingIgnoreCase(@Param("name") String name);


/**

* Encuentra productos con stock bajo

*/

List<Product> findByStockLessThanAndActiveTrue(Integer minStock);


/**

* Cuenta productos por categoría

*/

@Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category AND p.active = true")

Long countByCategory(@Param("category") String category);

/**

* Verifica si existe un producto con el mismo nombre (excluyendo el ID actual)

*/

boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);


/**

* Encuentra un producto activo por ID

*/

Optional<Product> findByIdAndActiveTrue(Long id);

}