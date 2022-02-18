package com.learning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.learning.dto.User;
import com.learning.exception.AlreadyExistsException;
import com.learning.exception.IdNotFoundException;
@Repository
public interface UserService  {

	public User addUser(User user) throws AlreadyExistsException;
	public boolean authenticate(String email,String password);
	public User getUserById(Long id) throws IdNotFoundException;
	public User updateUserById(Long id,User user) throws IdNotFoundException;
	public boolean deleteUserById(Long id) throws IdNotFoundException;
	public Optional<List<User>> getAllUsers();
}
