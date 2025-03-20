package org.revature.services;

import org.revature.models.CartItem;
import org.revature.repos.CartItemDAO;

public class CartItemService {
    private final CartItemDAO cartItemDAO;
    public CartItemService(CartItemDAO cartItemDAO) {
        this.cartItemDAO = cartItemDAO;
    }

    public boolean addCartItem(CartItem requestCartItem) {
        return cartItemDAO.addCartItem(requestCartItem);
    }

    public boolean removeCartItem(int userId, int cartItemId) {
    return cartItemDAO.deleteCartItem( userId, cartItemId);
    }

    public boolean updateCartItem( CartItem requestCartItem) {
        return cartItemDAO.updateCartItem( requestCartItem);
    }
}
