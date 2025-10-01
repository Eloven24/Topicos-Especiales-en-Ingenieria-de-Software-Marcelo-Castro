package com.eafit.tutorial.exception;
/**

* Excepción lanzada cuando no se encuentra un producto

*/

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {

super(message);

}


public ProductNotFoundException(String message, Throwable cause) {

super(message, cause);

}


public ProductNotFoundException(Long productId) {

super("Producto no encontrado con ID: " + productId);

}
}
