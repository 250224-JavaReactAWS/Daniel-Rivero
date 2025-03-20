package org.revature.services;

import org.revature.models.Product;
import org.revature.repos.ProductDAO;

import java.util.ArrayList;

public class ProductService {
    private  final ProductDAO productDAO;
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    public ArrayList<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public ArrayList<Product> getProducts() {
        return productDAO.getActiveProducts();
    }

    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    public void addproduct(Product product) {
        productDAO.addProduct(product);
    }

    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }
}
