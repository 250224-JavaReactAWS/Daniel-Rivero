package org.revature.services;

import org.revature.models.User;
import org.revature.repos.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public User registerUser(User requestUser) {//could be boolean
        return userDAO.addUser(requestUser);
    }

    public boolean isMailAvailable(String email) {
        //if the user is not found then the email is available
        return userDAO.getUserByEmail(email) == null;
    }

    public User loginUser(String email, String password) {

        User loggedInUser = userDAO.getUserByEmail(email);
        if(loggedInUser ==null){//if the user is not found
            return null;
        }
        //System.out.println(loggedInUser.toString());
        // TODO hash the password
        if(loggedInUser.getPassword().equals(password)){//password is correct
            return loggedInUser;
        }else{
            return null;
        }

    }

    public boolean updateUser(User requestedUser) {
        return userDAO.updateUser(requestedUser);
    }
}
