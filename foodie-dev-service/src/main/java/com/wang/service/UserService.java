package com.wang.service;


import com.wang.bo.UsersBO;
import com.wang.pojo.Users;



public interface UserService {

     Boolean queryUsernameIsExist(String username);

     Users createUser(UsersBO usersBO);

     Users queryUserForLogin(String username,String password);
}
