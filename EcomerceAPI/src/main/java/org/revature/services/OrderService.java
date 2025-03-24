package org.revature.services;

import org.revature.models.Order;
import org.revature.models.User;
import org.revature.repos.OrderDAO;


import java.util.ArrayList;

public class OrderService {
    private final OrderDAO orderDAO;
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public ArrayList<Order> getOrderByUser(int userId) {

        return orderDAO.getOrderByUser(userId);
    }

    public boolean updateOrder(Order order) {
        return  orderDAO.UpdateOrder(order);
    }

    public boolean createOrder(User user) {
        return orderDAO.CreateOrder(user);
    }

    public ArrayList<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
