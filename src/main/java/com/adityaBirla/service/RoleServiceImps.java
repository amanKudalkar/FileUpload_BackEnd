package com.adityaBirla.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adityaBirla.dao.RoleDao;
import com.adityaBirla.entity.Role;

@Service
@Transactional
public class RoleServiceImps implements RoleService{

	@Autowired
    private RoleDao roleDao;
	
	@Override
	public Role findByName(String name) {
		return roleDao.findByName(name);
	}

}
