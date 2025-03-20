package org.revature.repos;

import org.revature.models.CartItem;
import org.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartItemDAO {
    //metods to interact with the database
    //update, delete, add, get
    public boolean addCartItem(CartItem cartItem) {
        String sql = "INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
                ){
            pstmt.setInt(1, cartItem.getUserId());
            pstmt.setInt(2, cartItem.getProductId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean updateCartItem( CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, cartItem.getQuantity());
            pstmt.setInt(2, cartItem.getUserId());
            pstmt.setInt(3, cartItem.getProductId());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteCartItem(int userId, int productId) {
        String sql = "DELETE FROM cart_item WHERE user_id = ? AND product_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteCart(int userId) {
        String sql = "DELETE FROM cart_item WHERE user_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<CartItem> getCart(int userId) {
        ArrayList<CartItem> cart = new ArrayList<>();
        String sql = "SELECT * FROM cart_item WHERE user_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setUserId(rs.getInt("user_id"));
                cartItem.setProductId(rs.getInt("product_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cart.add(cartItem);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }
}
