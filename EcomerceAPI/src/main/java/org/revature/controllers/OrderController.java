package org.revature.controllers;

import io.javalin.http.Context;
import org.revature.dtos.response.ErrorMessage;
import org.revature.models.Order;
import org.revature.models.User;
import org.revature.services.OrderService;

import java.util.ArrayList;

public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrderHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        User requestedUser = new User();
        int userId = context.sessionAttribute("userId");
        requestedUser.setUserId(userId);

        if(orderService.createOrder(requestedUser)) {
            context.status(201); // CREATED
            return;
        }
        context.status(400); // BAD REQUEST
        context.json(new ErrorMessage("Order creation failed!"));
    }

    public void getAllOrdersHandler(Context context) {
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
        context.json(orderService.getAllOrders());
    }

    public void updateOrderHandler(Context context) {
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
        if(orderService.updateOrder(context.bodyAsClass(Order.class))) {
            context.status(200);
        }
    }

    public void getOrderByUserHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ArrayList<Order> orders =
        orderService.getOrderByUser((int)context.sessionAttribute("userId"));
        if (orders == null){
            context.status(404);
            context.json(new ErrorMessage("Orders not found"));
            return;
        }else{
            context.status(200);
            context.json(orders);
            return;
        }

    }


}

