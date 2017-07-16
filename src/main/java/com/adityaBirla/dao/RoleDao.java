package com.adityaBirla.dao;

import org.springframework.data.repository.CrudRepository;
import com.adityaBirla.entity.Role;

public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
