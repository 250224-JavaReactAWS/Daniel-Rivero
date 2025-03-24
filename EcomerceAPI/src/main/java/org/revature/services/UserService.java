package org.revature.services;

import com.fasterxml.jackson.core.JsonToken;
import org.revature.models.User;
import org.revature.repos.UserDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    //public UserService(UserDAO mockDAO) {
    //}

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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String hashedPassword = encoder.encode(password);
        //System.out.println(hashedPassword);
        //System.out.println(loggedInUser.getPassword());
        if(encoder.matches(password, loggedInUser.getPassword())){//password is correct
            return loggedInUser;
        }else{
            return null;
        }

    }

    public boolean updateUser(User requestedUser) {
        return userDAO.updateUser(requestedUser);
    }
}
