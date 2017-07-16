package com.adityaBirla.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.adityaBirla.dao.RoleDao;
import com.adityaBirla.dao.UserDao;
import com.adityaBirla.dao.UserDocumentDao;
import com.adityaBirla.entity.User;
import com.adityaBirla.entity.UserDocument;
import com.adityaBirla.entity.UserDocumentI;
import com.adityaBirla.entity.UserRole;



@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
	private UserDocumentDao userDocumentDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JavaMailSender sender;

    @Value("${adityaBirla.folder}")
    private String uploadFolder;
    
    @Value("${adityaBirla.angularAppAddress}")
    private String angularAppAddress;
    
    
	public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
    
    
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());
        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            user.getUserRoles().addAll(userRoles);
            Random randomGenerator = new Random();
            user.setRandomPkt(String.valueOf(randomGenerator.nextInt(100000)));
            localUser = userDao.save(user);
            sendEmail(localUser.getEmail(),localUser.getRandomPkt());
        }
        
        return localUser;
    }
    
    private void sendEmail(String recipient,String randomPkt){
        try {
			MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        
	        helper.setTo(recipient);
	        helper.setSubject("Verify AdityaBirla Account");
	        StringBuilder builder = new StringBuilder();
	        builder.append("<html>Dear Sir, ");
	        builder.append("<br><br>"
	        		+ "	<a href="+angularAppAddress+"/verifyEmail/"+recipient+"/"+randomPkt+">Please click here to verify your email address</a>"
	        		+ "<br><br>");
	        builder.append("Thanks and regards,<br>");
	        builder.append("AdityaBirla Team.</html>");
	        helper.setText(builder.toString(), true);
	        sender.send(message);    
        } catch (Exception e) {
        	LOG.info("Exception while sending mail -- >>  ", e);
		}
    }
    
    
    public boolean checkUserExists(String username, String email){
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }
        return false;
    }
    
    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
    }

    public User saveUser (User user) {
        return userDao.save(user);
    }
    
    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }

	@Override
	public boolean verifyEmail(String randomPkt, String email) {
		User user = findByEmail(email);
		if(randomPkt.trim().equals(user.getRandomPkt())){
			user.setEnabled(true);
			userDao.save(user);
			return true;
		}else{
			return false;
		}
	}
	

	private String saveUploadedFiles(MultipartFile file) throws IOException {
			SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");  
			byte[] bytes = file.getBytes();
	        String fileName =  file.getOriginalFilename().split("\\.")[0]+"_"+formatter.format(new Date())
	        +"."+file.getOriginalFilename().split("\\.")[1];
	        Path path = Paths.get(uploadFolder + fileName);
	        Files.write(path, bytes);
	        return fileName;
	        
	}

	@Override
	public void saveUploadedFiles(MultipartFile file, String username) throws IOException {
			String fileName = saveUploadedFiles(file);
			User user = findByUsername(username);
			UserDocument document = new UserDocument();
			document.setDocName(fileName);
			document.setDocTimeStamp(new Timestamp(System.currentTimeMillis()));
			document.setUser(user);
			user.getUserDocuments().add(document);
			userDao.save(user);
	}

	@Override
	public List<UserDocumentI> findAllDoc() {
		return userDocumentDao.findAllByOrderByDocIdDesc();
	}
	
}
