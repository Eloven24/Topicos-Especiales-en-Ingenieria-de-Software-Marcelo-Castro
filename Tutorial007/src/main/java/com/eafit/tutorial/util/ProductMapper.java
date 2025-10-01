package com.eafit.tutorial.util;
import com.eafit.tutorial.dto.CreateProductDTO;
import com.eafit.tutorial.dto.ProductDTO;
import com.eafit.tutorial.dto.UpdateProductDTO;
import com.eafit.tutorial.model.Product;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
/**

* Mapper para convertir entre entidades Product y DTOs

*

* Centraliza la lógica de conversión y mapeo de datos.

*/

@Component
public class ProductMapper {
    /**

* Convierte una entidad Product a ProductDTO

*/

public ProductDTO toDTO(Product product) {

if (product == null) {

return null;

}


return new ProductDTO(

product.getId(),

product.getName(),

product.getDescription(),

product.getPrice(),

product.getCategory(),

product.getStock(),

product.getActive(),

product.getCreatedAt(),

product.getUpdatedAt()

);

}


/**

* Convierte una lista de entidades Product a lista de DTOs

*/

public List<ProductDTO> toDTOList(List<Product> products) {

return products.stream()

.map(this::toDTO)

.collect(Collectors.toList());

}


/**

* Convierte un CreateProductDTO a entidad Product

*/

public Product toEntity(CreateProductDTO createDTO) {

if (createDTO == null) {

return null;

}


Product product = new Product();

product.setName(createDTO.getName());
product.setDescription(createDTO.getDescription());

product.setPrice(createDTO.getPrice());

product.setCategory(createDTO.getCategory());

product.setStock(createDTO.getStock());

product.setActive(true); // Nuevo producto siempre activo
return product;

}


/**

* Actualiza una entidad Product con datos de UpdateProductDTO

*/

public void updateEntity(Product product, UpdateProductDTO updateDTO) {

if (product == null || updateDTO == null) {

return;

}
if (updateDTO.getName() != null) {

product.setName(updateDTO.getName());

}

if (updateDTO.getDescription() != null) {

product.setDescription(updateDTO.getDescription());

}

if (updateDTO.getPrice() != null) {
product.setPrice(updateDTO.getPrice());

}

if (updateDTO.getCategory() != null) {

product.setCategory(updateDTO.getCategory());

}

if (updateDTO.getStock() != null) {

product.setStock(updateDTO.getStock());

}

}

}
