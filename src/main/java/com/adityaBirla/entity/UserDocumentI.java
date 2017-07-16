package com.adityaBirla.entity;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;

public interface UserDocumentI {

	Long getDocId();
	
	@Value("#{target.user.username}")
	String getUsername();
	
	Timestamp getDocTimeStamp();
	
	String getDocName();
	
}
