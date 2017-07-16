package com.adityaBirla.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.adityaBirla.entity.User;
import com.adityaBirla.entity.UserDocument;
import com.adityaBirla.entity.UserDocumentI;

public interface UserDocumentDao extends CrudRepository<UserDocument, Long> {

	
	List<UserDocumentI> findAllByOrderByDocIdDesc();
	
}
