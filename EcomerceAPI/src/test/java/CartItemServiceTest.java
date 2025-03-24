import org.junit.*;
import org.mockito.Mockito;
import org.revature.models.CartItem;
import org.revature.models.Product;
import org.revature.repos.CartItemDAO;
import org.revature.repos.ProductDAO;
import org.revature.repos.UserDAO;
import org.revature.services.CartItemService;
import org.revature.services.UserService;


import static org.mockito.Mockito.*;

import java.util.ArrayList;

public class CartItemServiceTest {


    private CartItemDAO cartItemDAO;


    //private ProductDAO productDAO;


    private CartItemService cartItemService;

    @Before
    public void setup(){
        // In this before class method I want to mock the userDAO class so that way I can prevent calls that
        // affect the database
        //productDAO = Mockito.mock(ProductDAO.class);
        cartItemDAO = Mockito.mock(CartItemDAO.class);
        // The mock DAO is a fake version of the original class and now I'll pass it to our UserService
        cartItemService = new CartItemService(cartItemDAO);
    }






    @Test
    public void testRemoveCartItem() {
        int userId = 1;
        int cartItemId = 1;

        when(cartItemDAO.deleteCartItem(userId, cartItemId)).thenReturn(true);

        boolean result = cartItemService.removeCartItem(userId, cartItemId);

        Assert.assertTrue(result);
        verify(cartItemDAO, times(1)).deleteCartItem(userId, cartItemId);
    }



    @Test
    public void testGetCart() {
        int userId = 1;
        ArrayList<CartItem> cartItems = new ArrayList<>();

        when(cartItemDAO.getCart(userId)).thenReturn(cartItems);

        ArrayList<CartItem> result = cartItemService.getCart(userId);

        Assert.assertNotNull(result);
        Assert.assertEquals(cartItems, result);
        verify(cartItemDAO, times(1)).getCart(userId);
    }
}