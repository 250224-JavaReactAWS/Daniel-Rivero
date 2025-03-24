import org.junit.*;
import org.mockito.Mockito;
import org.revature.models.Product;
import org.revature.repos.ProductDAO;
import org.revature.services.ProductService;

import java.util.ArrayList;


import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductDAO productDAO;


    private ProductService productService;

    @Before
    public void setup(){
        // In this before class method I want to mock the userDAO class so that way I can prevent calls that
        // affect the database
        productDAO = Mockito.mock(ProductDAO.class);
        // The mock DAO is a fake version of the original class and now I'll pass it to our UserService
        productService = new ProductService(productDAO);
    }

    @Test
    public void testGetAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        when(productDAO.getAllProducts()).thenReturn(products);

        ArrayList<Product> result = productService.getAllProducts();

        Assert.assertNotNull(result);
        Assert.assertEquals(products, result);
        verify(productDAO, times(1)).getAllProducts();
    }

    @Test
    public void testGetProducts() {
        ArrayList<Product> products = new ArrayList<>();
        when(productDAO.getActiveProducts()).thenReturn(products);

        ArrayList<Product> result = productService.getProducts();

        Assert.assertNotNull(result);
        Assert.assertEquals(products, result);
        verify(productDAO, times(1)).getActiveProducts();
    }

    @Test
    public void testGetProductById() {
        int productId = 1;
        Product product = new Product();
        when(productDAO.getProductById(productId)).thenReturn(product);

        Product result = productService.getProductById(productId);

        Assert.assertNotNull(result);
        Assert.assertEquals(product, result);
        verify(productDAO, times(1)).getProductById(productId);
    }

    @Test
    public void testAddProduct() {
        Product product = new Product();

        productService.addproduct(product);

        verify(productDAO, times(1)).addProduct(product);
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product();

        productService.updateProduct(product);

        verify(productDAO, times(1)).updateProduct(product);
    }
}
