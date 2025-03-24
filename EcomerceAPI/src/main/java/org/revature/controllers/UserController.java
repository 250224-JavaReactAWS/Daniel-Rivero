package org.revature.controllers;

import io.javalin.http.Context;
import org.revature.dtos.response.ErrorMessage;
import org.revature.models.User;
import org.revature.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void registerUserHandler(Context context) {

        //get the context body as a class
        User requestUser = context.bodyAsClass(User.class);
        //check if the username is available
        if(!userService.isMailAvailable(requestUser.getEmail())){
            context.status(409);
            context.result("Mail is already taken");
            logger.warn("RegisterUser-Mail not available");
            return;
        }
        boolean validRol=false;
        if(requestUser.getRole().equals("ADMIN")||requestUser.getRole().equals("CUSTOMER"))
            validRol=true;
        if(!validRol){
            context.status(400);
            context.json(new ErrorMessage("Invalid Role"));
            logger.warn("RegisterUser-Invalid Role");
            return;
        }
        User registeredUser = userService.registerUser(requestUser);

        if (registeredUser == null){
            context.status(500);
            context.json(new ErrorMessage("Something went wrong!"));
            logger.info("cannot register user with mail: " + requestUser.getEmail());
            return;
        }

        logger.info("New user registered with mail: " + registeredUser.getEmail());

        context.status(201);
        context.json(registeredUser);
    }

    public void loginHandler(Context context){
        // Get the user from the body
        User requestUser = context.bodyAsClass(User.class);
        // Attempt to login
        User returnedUser = userService.loginUser(requestUser.getEmail(), requestUser.getPassword());

        // If invalid let them know username or password incorrect
        if (returnedUser == null){
            context.json(new ErrorMessage("Username or Password Incorrect"));
            context.status(400);
            logger.warn("user login error " + requestUser.getUserId());
            return;
        }
        // If valid return the user and add them to the session
        context.status(200);
        context.json(returnedUser);
        logger.info("user login successfully " + returnedUser.getEmail());
        // Add the userId to the session
        context.sessionAttribute("userId", returnedUser.getUserId());
        context.sessionAttribute("role", returnedUser.getRole());
    }

    public void getAllUsersHandler(Context context) {
    }

    public void updateUserHandler(Context context) {

        User requestedUser= context.bodyAsClass(User.class);
        if(context.sessionAttribute("userId") == null){
            // This means the user is NOT logged in
            context.status(401); // UNAUTHORIZED -> Unauthenticated -> We don't know who you are
            context.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        if (requestedUser.getUserId() != (int) context.sessionAttribute("userId")){
            context.status(403); // FORBIDDEN -> Authenticated but not authorized
            context.json(new ErrorMessage("You are not authorized to update this user!"));
            return;
        }
        boolean updated = userService.updateUser(requestedUser);
        if(updated){
            context.status(200);
            context.json(requestedUser);
            logger.info("user update successfully " + requestedUser.getUserId());
        }else{
            context.status(500);
            context.json(new ErrorMessage("Something went wrong!"));
            logger.warn("user update error " + requestedUser.getUserId());
        }


    }


}
