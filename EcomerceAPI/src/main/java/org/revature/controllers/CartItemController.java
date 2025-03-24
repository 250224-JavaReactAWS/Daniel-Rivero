package org.revature.controllers;

import io.javalin.http.Context;
import org.revature.dtos.response.ErrorMessage;
import org.revature.models.CartItem;
import org.revature.services.CartItemService;
import org.revature.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CartItemController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    public void postCartItemHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        // Get the cart item from the body
        CartItem requestCartItem = context.bodyAsClass(CartItem.class);
        requestCartItem.setUserId((int) context.sessionAttribute("userId"));

        boolean addedCartItem = cartItemService.addCartItem(requestCartItem);
        if(addedCartItem){
            context.status(201);
            context.json(requestCartItem);
            logger.info("Cart item added successfully " + requestCartItem.getCartItemId());
            return;
        }else{
            context.status(500);
            context.json(new ErrorMessage("Something went wrong!"));
            logger.warn("Cart item not added " + requestCartItem.getCartItemId());
            return;
        }
    }

    public void removeCartItemHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        // Get the cart item from the body

        int cartItemId = Integer.parseInt(context.queryParam("cartItemId"));
        boolean removedCartItem = cartItemService.removeCartItem((int)context.sessionAttribute("userId"), cartItemId);
        if(removedCartItem){
            context.status(200);
            logger.info("Cart item removed successfully " + cartItemId);
            return;
        }else{
            context.status(500);
            context.json(new ErrorMessage("Something went wrong!"));
            logger.warn("Cart item not removed " + cartItemId);
            return;
        }
    }

    public void updateCartItemHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        // Get the cart item from the body
        CartItem requestCartItem = context.bodyAsClass(CartItem.class);
        requestCartItem.setUserId((int) context.sessionAttribute("userId"));
        ArrayList<CartItem> cart=cartItemService.getCart((int) context.sessionAttribute("userId"));
        boolean cartItemExists=false;
        logger.info("Checking if cart item exists in cart " + requestCartItem.getProductId());
        for (CartItem cartItem:cart){
            if (cartItem.getProductId()==requestCartItem.getProductId()){
                cartItemExists=true;
                logger.info("Car item found in car" + requestCartItem.getCartItemId());
                break;
            }
        }

        boolean updatedCartItem = false;
        if (cartItemExists){
            updatedCartItem = cartItemService.updateCartItem(requestCartItem);
        }

        if(updatedCartItem){
            context.status(200);
            context.json(requestCartItem);
            logger.info("Cart item updated successfully " + requestCartItem.getCartItemId());
            return;
        }else{
            context.status(500);
            context.json(new ErrorMessage("Something went wrong!"));
            logger.warn("Cart item not updated " + requestCartItem.getCartItemId());
            return;
        }
    }

    public void getCartItemsHandler(Context context) {
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ArrayList<CartItem> cart = cartItemService.getCart((int) context.sessionAttribute("userId"));
        if (cart == null){
            context.status(404);
            context.json(new ErrorMessage("Cart not found"));
            return;
        }else{
            context.status(200);
            context.json(cart);
            return;
        }
    }
}
