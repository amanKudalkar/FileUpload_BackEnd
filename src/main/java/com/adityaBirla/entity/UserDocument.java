package com.adityaBirla.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class UserDocument {

	   @Id
	   @GeneratedValue(generator = "increment")
	   @GenericGenerator(name = "increment", strategy = "increment")
	   @Column(name = "docId", nullable = false, updatable = false)
	   private Long docId;
	   @OneToOne(fetch = FetchType.EAGER ,cascade = CascadeType.ALL, optional=false)
	   @JoinColumn(name = "USER_ID",nullable=false )
	   @JsonBackReference
	   private User user = new User();
	   @Type(type = "java.sql.Timestamp")
	   private Timestamp docTimeStamp;
	   private String docName;
	   
	   	public String getDocName() {
	   		return docName;
		}
		public void setDocName(String docName) {
			this.docName = docName;
		}
		public Long getDocId() {
	   		return docId;
		}
		public void setDocId(Long docId) {
			this.docId = docId;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Timestamp getDocTimeStamp() {
			return docTimeStamp;
		}
		public void setDocTimeStamp(Timestamp docTimeStamp) {
			this.docTimeStamp = docTimeStamp;
		}
	   
}
