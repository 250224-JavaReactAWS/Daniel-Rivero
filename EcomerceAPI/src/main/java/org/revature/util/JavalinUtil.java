package org.revature.util;

import io.javalin.Javalin;
import org.revature.controllers.CartItemController;
import org.revature.controllers.ProductController;
import org.revature.controllers.UserController;
import org.revature.repos.CartItemDAO;
import org.revature.repos.ProductDAO;
import org.revature.repos.UserDAO;
import org.revature.services.CartItemService;
import org.revature.services.ProductService;
import org.revature.services.UserService;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {


    // This class is not explicitly necessary, this can all be done in the main class but since the main class is
    // just for starting the app I'll do my config information here
    // The parent path for all of our resources has been http://localhost:7070
    public static Javalin create(int port){
        // Create all of our variables
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(productService);

        CartItemDAO cartItemDAO = new CartItemDAO();
        CartItemService cartItemService = new CartItemService(cartItemDAO);
        CartItemController cartItemController = new CartItemController(cartItemService);

        //OrderDAO orderDAO = new OrderDAO();
        //OrderService orderService = new OrderService(orderDAO);
        //OrderController orderController = new OrderController(orderService);



        return Javalin.create(config -> {
                    // Inside of here I have a config variable, this can be used for things like CORS configuration
                    // This can also be used to set up a set of paths
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController:: registerUserHandler);
                            post("/login", userController:: loginHandler);
                            put("/update", userController:: updateUserHandler);
                            get("/", userController::getAllUsersHandler);


                        });
                        path("/products", () -> {
                            get("/", productController::getProductsHandler);
                            get("/details", productController::getDetailsProductHandler);
                            get("/getAll", productController::getAllProductsHandler);
                            post("/addItem", productController::addProductHandler);
                            put("/updateItem", productController::updateProductHandler);
                        });
                        path("/cart", () -> {
                            post("/addItem", cartItemController::postCartItemHandler);
                            post("/removeItem", cartItemController::removeCartItemHandler);
                            post("/updateItem", cartItemController::updateCartItemHandler);
                        });
                        //path("/order", () -> {
                        //    get("/", orderController::getProductsHandler);
                        //});
                    });
                })
//                .post("/users/register", ctx -> {userController.registerUserHandler(ctx);})
                // Method Reference Syntax
//                .post("/users/register", userController::registerUserHandler)
//                .post("/users/login", userController::loginHandler)
//                .get("/users", userController::getAllUsersHandler)
                .start(port);
    }
}