package com.adityaBirla.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.adityaBirla.dao.RoleDao;
import com.adityaBirla.dao.UserDao;
import com.adityaBirla.entity.Role;
import com.adityaBirla.entity.User;
import com.adityaBirla.entity.UserRole;

@Component
public class DataLoader implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		
		insertMasterRole();
		
		insertAdminUser();
	}

	
	private void insertMasterRole(){
		Role roleAdmin = roleDao.findByName("ADMIN");
		if(null == roleAdmin){
			roleAdmin = new Role();
			roleAdmin.setName("ADMIN");
			roleDao.save(roleAdmin);
		}
		
		Role roleUser = roleDao.findByName("USER");
		if(null == roleUser){
			roleUser = new Role();
			roleUser.setName("USER");
			roleDao.save(roleUser);
		}
	}
	
	private void insertAdminUser(){
		User user = userDao.findByUsername("admin");
		if(null == user){
			user = new User();
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("123456"));
			user.setEmail("admin@gmail.com");
			user.setEnabled(true);
			user.setRandomPkt("123456");
			UserRole userRole = new UserRole();
			userRole.setUser(user);
			userRole.setRole(roleDao.findByName("ADMIN"));
			user.getUserRoles().add(userRole);
			userDao.save(user);
		}
		
	}
	
	
	
}
