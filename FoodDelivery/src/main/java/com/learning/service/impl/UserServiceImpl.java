package com.learning.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dto.User;
import com.learning.exception.AlreadyExistsException;
import com.learning.exception.IdNotFoundException;
import com.learning.repository.UserRepository;
import com.learning.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(rollbackFor = AlreadyExistsException.class)
	public User addUser(User user) throws AlreadyExistsException {
		// TODO Auto-generated method stub
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new AlreadyExistsException("Email already registered.");
		}
		User user2 = userRepository.save(user);
		return user2;
		
	}

	@Override
	public boolean authenticate(String email,String password) {
		// TODO Auto-generated method stub
		boolean check = userRepository.existsByEmailAndPassword(email, password);
		return check;
	}

	@Override
	public User getUserById(Long id) throws IdNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> optional =  userRepository.findById(id);
		if(optional.isEmpty()) {
			throw new IdNotFoundException("Id not found Exception.");
		}
		return optional.get();
	}

	@Override
	public User updateUserById(Long id,User user) throws IdNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> optional = userRepository.findById(id);
		if(optional.isEmpty()) {
			throw new IdNotFoundException("Id not found Exception.");
		}else {
			user.setId(id);
			User user2 = userRepository.save(user);
			return user2;
		}
		
		
	}

	@Override
	public boolean deleteUserById(Long id) throws IdNotFoundException {
		// TODO Auto-generated method stub
		try {
			User user = this.getUserById(id);
			if(user == null) {
				throw new IdNotFoundException("record not found");
			}
			else {
				userRepository.deleteById(id);
				return true;
			}
		} catch ( IdNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IdNotFoundException(e.getMessage());
		}
	}

	@Override
	public Optional<List<User>> getAllUsers() {
		// TODO Auto-generated method stub
		return Optional.ofNullable(userRepository.findAll());
	}
	
	
}
