package com.webservice.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.webservice.app.model.User;

@Service
public class UserServiceImpl implements UserService{
	Set<User> users = new HashSet<>();
	{
		users.add(new User(24, "Harshal", new Date()));
		users.add(new User(26, "Anjali", new Date()));
		users.add(new User(22, "Solao", new Date()));
		users.add(new User(28, "Bhoge", new Date()));
	}

	@Override
	public User createUser(User user) {
		if(users.add(user)) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteUser(long id) {
		return users.remove(new User(id,"",new Date()));
	}

	@Override
	public User getUserById(long id) {
		Optional<User> user = users.stream().filter(i -> i.getId() == id).findAny();
		if(user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}

	@Override
	public Set<User> getAllUsers() {
		return users;
	}
	
	

}
