package org.revature.repos;

import org.revature.models.Product;
import org.revature.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;


public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    public ArrayList<Product> getAllProducts(){

        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = ConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setStock(rs.getInt("stock"));
                products.add(product);
            }

        } catch (
    SQLException e) {
        e.printStackTrace();
    }
        return products;
    }

    public ArrayList<Product> getActiveProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, name, price, stock FROM product where stock > 0";

        try (Connection conn = ConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                //product.setDescription(rs.getString("product_description"));
                products.add(product);
            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public Product getProductById(int productId) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE product_id = ? and stock > 0";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            try(
            ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setDescription(rs.getString("description"));
                    product.setStock(rs.getInt("stock"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            }
        if(product == null){
            logger.warn("Product not found or out of stock");
        }
        return product;

    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO product (name, description, price, stock) VALUES (?, ?, ?, ?) ";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            //System.out.println(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, stock = ? WHERE product_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5, product.getProductId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
