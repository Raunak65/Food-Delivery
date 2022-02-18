package com.learning.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dto.Role;
import com.learning.exception.IdNotFoundException;
import com.learning.repository.RoleRepository;
import com.learning.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String addRole(Role role) {
		// TODO Auto-generated method stub
		Role role2 = roleRepository.save(role);
		if (role2!=null)
			return "Success";
		else
			return "Fail";
	}

	@Override
	public void deleteRole(int roleId) throws IdNotFoundException {
		// TODO Auto-generated method stub
		Optional<Role> optional = roleRepository.findById(roleId);
		if (optional.isEmpty())
			throw new IdNotFoundException("Record not found");
		roleRepository.deleteById(roleId);
	}

}
