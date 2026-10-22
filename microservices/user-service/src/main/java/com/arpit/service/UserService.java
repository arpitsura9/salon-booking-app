package com.arpit.service;

import com.arpit.exception.UserException;
import com.arpit.modal.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUserById(long id) throws UserException;
    List<User> getAllUsers();
    void deleteUser(Long id) throws UserException;
    User updateUser(Long id,User user) throws UserException;
    User getUserFromJwt(String jwt) throws Exception;
}
