package com.adityaBirla.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.adityaBirla.entity.User;
import com.adityaBirla.entity.UserDocument;
import com.adityaBirla.entity.UserDocumentI;
import com.adityaBirla.entity.UserRole;

public interface UserService {
	User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
    void save (User user);
    
    User createUser(User user, Set<UserRole> userRoles);
    
    User saveUser (User user); 
    
    List<User> findUserList();

    void enableUser (String username);

    void disableUser (String username);
    
    boolean verifyEmail(String randomPkt, String email);
    
    void saveUploadedFiles(MultipartFile file,String username) throws IOException;
    
    List<UserDocumentI> findAllDoc();
    
}
