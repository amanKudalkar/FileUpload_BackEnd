package com.adityaBirla.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.adityaBirla.entity.User;
import com.adityaBirla.entity.UserDocument;
import com.adityaBirla.entity.UserDocumentI;
import com.adityaBirla.entity.UserRole;
import com.adityaBirla.service.RoleService;
import com.adityaBirla.service.UserService;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @Value("${adityaBirla.folder}")
    private String uploadFolder;
    
    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
    public ResponseEntity<?> profile(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        if(null == user){
        	return new ResponseEntity("User not found with perticular email address", HttpStatus.BAD_REQUEST);
        }else{
        	return new ResponseEntity(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signupPost(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email) {
    	if(userService.checkUserExists(username, email))  {
            String errorMsg = "";
    		if (userService.checkEmailExists(email)) {
    			errorMsg = "Email";
            }
            if (userService.checkUsernameExists(username)) {
            	if(errorMsg.length()> 1){errorMsg += ",";}
            	errorMsg += "userName";
            }
            return new ResponseEntity("Already register "+errorMsg, HttpStatus.BAD_REQUEST);
        }else{	 
        	 User user = new User();
        	 user.setUsername(username);
        	 user.setEmail(email);
        	 user.setPassword(password);
        	 
        	 Set<UserRole> userRoles = new HashSet<>();
             userRoles.add(new UserRole(user, roleService.findByName("USER")));
             user = userService.createUser(user, userRoles);
             return new ResponseEntity("SignUp successfully.Link has been sent to your email address."
             		+ "kindly click on the link to verify email address", HttpStatus.OK);
        }
    }
    
    @RequestMapping(value = "/verifyEmail", method = RequestMethod.GET)
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email,@RequestParam("randomPkt") String randomPkt) {
        	if(userService.verifyEmail(randomPkt, email)){
        		return new ResponseEntity("Account has been activated.You can login now.", HttpStatus.OK);
        	}else{
        		 return new ResponseEntity("Account activation fail.", HttpStatus.BAD_REQUEST);
        	}
    }
    
    
    @RequestMapping(value = "/user/uploadFile", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam("username") String username,@RequestPart("uploadFile") MultipartFile uploadfile) {
			 if (uploadfile.isEmpty()) {
		         return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
		     }
			 String fileNm[] = uploadfile.getOriginalFilename().split("\\.");
			 if (fileNm[fileNm.length-1].equalsIgnoreCase("DOC") 
				|| fileNm[fileNm.length-1].equalsIgnoreCase("DOCX")
				|| fileNm[fileNm.length-1].equalsIgnoreCase("PDF")) {
				 try {
					 userService.saveUploadedFiles(uploadfile, username);
		         } catch (IOException e) {
		        	 return new ResponseEntity("Error occur while uploading the file", HttpStatus.BAD_REQUEST);
		         }
				 return new ResponseEntity("Successfully uploaded - " +
		                uploadfile.getOriginalFilename(), HttpStatus.OK);
		     }else{
		    	 return new ResponseEntity("Only word and PDF files are allowed", HttpStatus.BAD_REQUEST);
		     }	 
    }
    
    @RequestMapping(value = "/admin/getDocuments", method = RequestMethod.GET)
    public ResponseEntity<?> getDocuments() {
    	List<UserDocumentI>  UserDocuments = userService.findAllDoc();
    	return new ResponseEntity(UserDocuments, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestParam("username") String username,@RequestParam("password") String password) {
    	User user = userService.findByUsername(username);
        if(null == user){
        	return new ResponseEntity("User not found with perticular email address", HttpStatus.BAD_REQUEST);
        }else{
        	return new ResponseEntity(user, HttpStatus.OK);
        }
    }
    
    @RequestMapping(value = "/logoutAll", method = RequestMethod.GET)
    public ResponseEntity<?> logout() {
    	return new ResponseEntity("logout successfully", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/admin/downloadFile", method = RequestMethod.POST)
    public ResponseEntity<?> downloadFile(@RequestParam("fileName") String fileName) {
    	File file2download = new File(uploadFolder+fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(file2download));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file2download.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }
    
    
    
    
}

