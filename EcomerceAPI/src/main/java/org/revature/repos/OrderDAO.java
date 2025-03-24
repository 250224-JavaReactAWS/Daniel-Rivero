package org.revature.repos;

import org.revature.models.CartItem;
import org.revature.models.Order;
import org.revature.models.OrderItem;
import org.revature.models.User;
import org.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class OrderDAO {
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    public boolean CreateOrder(User user ) {
        //create order, select from cart, insert into order
        //Order order;

//        String query = "SELECT c.*, p.price, p.price * c.quantity as subtotal  FROM cartitem c join product p on p.product_id = c.product_id  where user_id = ?";
//        String insert = "INSERT INTO order (user_id, totalPrice, status) VALUES (?,?,?) returning order_id";
//        String deleteq = "DELETE FROM cartitem WHERE user_id = ?";
//        ArrayList<CartItem> cart = new ArrayList<>();
//        ArrayList<OrderItem> orderItems = new ArrayList<>();
//        double subTotal = 0;
//        String status="pending";
//        try (Connection conn = ConnectionUtil.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query);
//             PreparedStatement pstmt2 = conn.prepareStatement(insert)
//        ) {
//            pstmt.setInt(1, user.getUserId());
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                //get cart items to the arraylist
//                OrderItem orderItem = new OrderItem();
//                orderItem.setOrderId(rs.getInt("order_id"));
//                orderItem.setProductId(rs.getInt("product_id"));
//                orderItem.setQuantity(rs.getInt("quantity"));
//                orderItem.setPrice(rs.getDouble("price"));
//
//                orderItems.add(orderItem);
//                subTotal += rs.getDouble("subtotal");
//            }
//            //insert into
//            pstmt2.setInt(1, user.getUserId());
//            pstmt2.setDouble(2, subTotal);
//            pstmt2.setString(3, status);
//            ResultSet rs2 = pstmt2.executeQuery();
//            int orderId = 0;
//            while (rs2.next()) {
//                orderId = rs2.getInt("order_id");
//            }
//            for (OrderItem orderItem : orderItems) {
//                orderItem.setOrderId(orderId);
//                orderItemDAO.SaveOrderItem(orderItem);
//            }
//            //delete from cart
//            try (PreparedStatement pstmt3 = conn.prepareStatement(deleteq)) {
//                pstmt3.setInt(1, user.getUserId());
//                pstmt3.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
        //use an stored procedure
        String sql = "CALL ProcessOrder(?)";
        try (Connection conn = ConnectionUtil.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean UpdateOrder(Order order) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, order.getStatus());
            pstmt.setInt(2, order.getOrderId());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Order> getOrderByUser(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        ArrayList<Order> orders = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_Price"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                orders.add(order);
            }
        }catch (SQLException e) {
                e.printStackTrace();
            return null;
            }
            return orders;
    }

    public ArrayList<Order> getAllOrders() {
        String sql="SELECT * FROM orders";
        try{
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_Price"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
