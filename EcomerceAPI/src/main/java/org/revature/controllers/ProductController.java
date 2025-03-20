package org.revature.controllers;

import io.javalin.http.Context;
import org.revature.dtos.response.ErrorMessage;
import org.revature.models.Product;
import org.revature.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProductController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService= productService;
    }
    public ArrayList<Product> getProductsHandler(Context context) {
        // Get all products
        return productService.getProducts();
    }

    public void getDetailsProductHandler(Context context) {
        Product product = productService.getProductById(Integer.parseInt(context.queryParam("productId")));
        if (product == null){
            context.status(404);
            context.json(new ErrorMessage("Product not found"));
            return;
        }
        context.json(product);
        logger.info("Product found successfully " + product.getName());
        return;
    }

    public void getAllProductsHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        // Validate the logged in user is an admin
        if (!context.sessionAttribute("role").equals( "ADMIN")){
            // The user is logged in but they shouldn't be able to access this since they're not an admin
            context.status(403); // FORBIDDEN -> We know who you are but you do not have access to this resource
            context.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }
        context.json(productService.getAllProducts());
    }


    public void updateProductHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        // Validate the logged in user is an admin
        if (!context.sessionAttribute("role").equals("ADMIN") ){
            logger.warn("User is not an admin is a "+context.sessionAttribute("role"));
            // The user is logged in but they shouldn't be able to access this since they're not an admin
            context.status(403); // FORBIDDEN -> We know who you are but you do not have access to this resource
            context.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }
        Product product = context.bodyAsClass(Product.class);
        if(product.getStock()<0){
            context.status(400);
            context.json(new ErrorMessage("Stock must be greater than 0"));
            return;
        }
        productService.updateProduct(product);
        context.status(204);
        logger.info("Product updated successfully " + product.getName());
    }

    public void addProductHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        // Validate the logged in user is an admin
        if (!context.sessionAttribute("role").equals("ADMIN") ){
            logger.warn("User is not an admin is a "+context.sessionAttribute("role"));
            // The user is logged in but they shouldn't be able to access this since they're not an admin
            context.status(403); // FORBIDDEN -> We know who you are but you do not have access to this resource
            context.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }
        Product product= context.bodyAsClass(Product.class);
        if(product.getStock()<0){
            context.status(400);
            context.json(new ErrorMessage("Stock must be greater than 0"));
            return;
        }
        productService.addproduct(product);
        context.status(201);
        logger.info("Product added successfully " + product.getName());
    }
}
