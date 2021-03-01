package com.webservice.app.service;

import java.util.List;
import java.util.Set;

import com.webservice.app.model.User;

public interface UserService {
	
	User createUser(User user);
	
	boolean deleteUser(long id);
	
	User getUserById(long id);
	
	Set<User> getAllUsers();	

}
