package com.eafit.tutorial.exception;
/**

* Excepción lanzada cuando se intenta crear un producto que ya existe

*/
public class ProductAlreadyExistsException extends RuntimeException {


public ProductAlreadyExistsException(String message, Throwable cause) {

super(message, cause);

}

public ProductAlreadyExistsException(String productName) {

super("Ya existe un producto con el nombre: " + productName);

}

}