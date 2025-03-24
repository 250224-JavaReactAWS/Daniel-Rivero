
import org.junit.*;
import org.mockito.Mockito;
import org.revature.models.Order;
import org.revature.repos.OrderDAO;
import org.revature.repos.UserDAO;
import org.revature.services.OrderService;
import org.revature.services.UserService;

import java.util.ArrayList;


import static org.mockito.Mockito.*;

public class OrderServiceTest {


    private OrderDAO orderDAO;

    private OrderService orderService;

    @Before
    public void setup(){
        // In this before class method I want to mock the userDAO class so that way I can prevent calls that
        // affect the database
        orderDAO = Mockito.mock(OrderDAO.class);
        // The mock DAO is a fake version of the original class and now I'll pass it to our UserService
        orderService = new OrderService(orderDAO);
    }

    @Test
    public void testGetAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        when(orderDAO.getAllOrders()).thenReturn(orders);

        ArrayList<Order> result = orderService.getAllOrders();

        Assert.assertNotNull(result);
        Assert.assertEquals(orders, result);
        verify(orderDAO, times(1)).getAllOrders();
    }

    @Test
    public void testGetOrderByUser() {
        int userId = 1;
        ArrayList<Order> orders = new ArrayList<>();
        when(orderDAO.getOrderByUser(userId)).thenReturn(orders);

        ArrayList<Order> result = orderService.getOrderByUser(userId);

        Assert.assertNotNull(result);
        Assert.assertEquals(orders, result);
        verify(orderDAO, times(1)).getOrderByUser(userId);
    }



    @Test
    public void testUpdateOrder() {
        Order order = new Order();

        orderService.updateOrder(order);

        verify(orderDAO, times(1)).UpdateOrder(order);
    }
}
