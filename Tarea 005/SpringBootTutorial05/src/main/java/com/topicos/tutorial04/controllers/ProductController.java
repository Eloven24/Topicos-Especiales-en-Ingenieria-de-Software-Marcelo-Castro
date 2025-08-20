package com.topicos.tutorial04.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

@Controller

public class ProductController {

    private static final List<Map<String, String>> products = new ArrayList<>(List.of(

            Map.of("id", "1", "name", "TV", "description", "Best TV", "price", "$499"),

            Map.of("id", "2", "name", "iPhone", "description", "Best iPhone", "price", "$999"),

            Map.of("id", "3", "name", "Chromecast", "description", "Best Chromecast", "price", "$35"),

            Map.of("id", "4", "name", "Glasses", "description", "Best Glasses", "price", "$99")

    ));

    @GetMapping("/products")

    public String index(Model model) {

        model.addAttribute("title", "Products - Online Store");

        model.addAttribute("subtitle", "List of products");

        model.addAttribute("products", products);

        return "product/index";

    }

    @GetMapping("/products/{id}")

    public String show(@PathVariable String id, Model model) {

        int productId = Integer.parseInt(id) - 1;

        if (productId < 0 || productId >= products.size()) {

            return "redirect:/";

        }

        Map<String, String> product = products.get(productId);

        String priceStr = product.get("price").replace("$", "");

        double priceNum = Double.parseDouble(priceStr);

        model.addAttribute("title", product.get("name") + " - Online Store");

        model.addAttribute("subtitle", product.get("name") + " - Product Information");

        model.addAttribute("product", product);

        model.addAttribute("price", product.get("price"));

        model.addAttribute("priceNum", priceNum);

        return "product/show";

    }

    @GetMapping("/products/create")

    public String create(Model model) {

        model.addAttribute("title", "Create Product");

        model.addAttribute("productForm", new ProductForm());

        return "product/create";

    }

    @PostMapping("/products/save")

    public String save(@Valid @ModelAttribute("productForm") ProductForm productForm, BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("title", "Create Product");

            return "product/create";

        }

        // Simulación de guardar el producto en la lista (sin persistencia en DB)

        Map<String, String> newProduct = new HashMap<>();

        newProduct.put("id", String.valueOf(products.size() + 1));

        newProduct.put("name", productForm.getName());

        newProduct.put("description", "Price: $" + productForm.getPrice());

        products.add(newProduct);

        return "product/created";

    }

}