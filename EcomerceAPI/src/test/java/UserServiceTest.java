import org.junit.*;
import org.mockito.Mockito;
import org.revature.models.User;
import org.revature.repos.UserDAO;
import org.revature.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;

    private UserDAO mockDAO;

    @Before
    public void setup(){
        // In this before class method I want to mock the userDAO class so that way I can prevent calls that
        // affect the database
        mockDAO = Mockito.mock(UserDAO.class);
        // The mock DAO is a fake version of the original class and now I'll pass it to our UserService
        userService = new UserService(mockDAO);
    }
    @Test
    public void testRegisterUser() {
        User user = new User();
        when(mockDAO.addUser(user)).thenReturn(user);

        User result = userService.registerUser(user);

        Assert.assertNotNull(result);
        verify(mockDAO, times(1)).addUser(user);
    }

    @Test
    public void testIsMailAvailable() {
        String email = "test@example.com";
        when(mockDAO.getUserByEmail(email)).thenReturn(null);

        boolean result = userService.isMailAvailable(email);

        Assert.assertTrue(result);
        verify(mockDAO, times(1)).getUserByEmail(email);
    }

    @Test
    public void testLoginUser_Success() {
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        when(mockDAO.getUserByEmail(email)).thenReturn(user);

        User result = userService.loginUser(email, password);

        Assert.assertNotNull(result);
        verify(mockDAO, times(1)).getUserByEmail(email);
    }

    @Test
    public void testLoginUser_Failure() {
        String email = "test@example.com";
        String password = "password";
        when(mockDAO.getUserByEmail(email)).thenReturn(null);

        User result = userService.loginUser(email, password);

        Assert.assertNull(result);
        verify(mockDAO, times(1)).getUserByEmail(email);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        when(mockDAO.updateUser(user)).thenReturn(true);

        boolean result = userService.updateUser(user);

        Assert.assertTrue(result);
        verify(mockDAO, times(1)).updateUser(user);
    }
}
