package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

import java.util.List;

/**
 * Created by nitin on 9/19/14.
 */
public interface UserDaoInterface {

public boolean saveUser(UserModel user);
    public boolean checkUser(UserModel user);
    public UserModel findUser(String userName);
    public List<UserModel> getAllUsers();
}
