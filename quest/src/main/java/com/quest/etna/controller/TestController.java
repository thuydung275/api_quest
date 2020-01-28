package com.quest.etna.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.UserService;

@RestController
public class TestController {
	
	private final static Logger log = Logger.getLogger(UserService.class);
    private final static String className = User.class.getSimpleName();
    private List<User> userList;
    
    @Autowired
    private UserService userService;
        
	@GetMapping(value="/test")
	public void testUsers() {
    	List<User> userList = userService.findAllUsers();
    	log.debug(userList);
    	log.debug(userList == null);
	}
    
	@GetMapping(value="/testSuccess")
	@ResponseStatus(HttpStatus.OK)
	public String testSuccess() {
		return "success";
	}
	
	@GetMapping(value="/testNotFound")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String testNotFound() {
		return "not found";
	}
	
	@GetMapping(value="/testError")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String testError() {
		return "error";
	}
}
