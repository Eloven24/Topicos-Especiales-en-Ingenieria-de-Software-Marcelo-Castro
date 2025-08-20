package com.topicos.tutorial04.controllers;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductForm {

    @NotEmpty(message = "The product name is required")

    private String name;

    @NotNull(message = "The price is required")

    @PositiveOrZero(message = "The price must be positive or zero")
    private double price;

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public Double getPrice() {

        return price;

    }

    public void setPrice(Double price) {

        this.price = price;

    }

}
