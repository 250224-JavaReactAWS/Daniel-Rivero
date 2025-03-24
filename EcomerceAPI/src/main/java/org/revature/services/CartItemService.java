package org.revature.services;

import org.revature.models.CartItem;
import org.revature.models.Product;
import org.revature.repos.CartItemDAO;
import org.revature.repos.ProductDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CartItemService {
    private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);

    private final CartItemDAO cartItemDAO;


    public CartItemService(CartItemDAO cartItemDAO) {
        this.cartItemDAO = cartItemDAO;

    }

    public boolean addCartItem(CartItem requestCartItem) {
        ProductDAO productDAO = new ProductDAO();
        int productId = requestCartItem.getProductId();
        if(productId==0){
            return false;
        }
        Product product=    productDAO.getProductById(productId);
        if (product==null){
            logger.warn("CartItemService Product not found " + productId);
            return false;
        }

        if(product.getStock()<requestCartItem.getQuantity()){
            logger.warn("CartItemService Not enough stock for product " + productId);
            return false;
        }
        return cartItemDAO.addCartItem(requestCartItem);
    }

    public boolean removeCartItem(int userId, int cartItemId) {
    return cartItemDAO.deleteCartItem( userId, cartItemId);
    }

    public boolean updateCartItem( CartItem requestCartItem) {
        ProductDAO productDAO = new ProductDAO();
        int productId = requestCartItem.getProductId();
        if(productId==0){
            return false;
        }
        Product product=    productDAO.getProductById(productId);
        if (product==null){
            logger.warn("CartItemService Product not found " + productId);
            return false;
        }

        if(product.getStock()<requestCartItem.getQuantity()){
            logger.warn("CartItemService Not enough stock for product " + productId);
            return false;
        }
        return cartItemDAO.updateCartItem( requestCartItem);
    }

    public ArrayList<CartItem> getCart(int userId) {
        return cartItemDAO.getCart(userId);
    }
}
