package org.revature.repos;

import org.revature.models.OrderItem;
import org.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderItemDAO {
    public void SaveOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getProductId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setDouble(4, orderItem.getPrice());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
