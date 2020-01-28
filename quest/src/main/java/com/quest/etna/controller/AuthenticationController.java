package com.quest.etna.controller;


import com.quest.etna.model.JwtRequest;
import com.quest.etna.model.JwtResponse;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    
    @Autowired
    private UserService userService;

    final static Logger log = Logger.getLogger(AuthenticationController.class);

    /**
     * 
     * @param user :
     *	{
	 *		"username": "adminA",
	 *		"password": "adminA",
	 *		"userRole": "ROLE_ADMIN" // optional 
 	 *	}
     * @return UserDetails
     */
    @PostMapping(path="/register", consumes="application/json", produces="application/json")
    public ResponseEntity<UserDetails> createUser(@RequestBody User user) {
    	UserDetails userDetails = null;
    	User userCreated = userService.createUser(user);
    	if (userCreated != null) {
    		userDetails = new UserDetails(user.getId(), userCreated.getUsername(), userCreated.getUserRole());
    		return new ResponseEntity(userDetails, HttpStatus.CREATED);
    	} 
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    	
    }

    /**
     * 
     * @param jwtRequest :
     *	{
	 *		"username": "adminA",
	 *		"password": "adminA"
 	 *	}
     * @return JwtResponse token
     * @throws Exception
     */
    @PostMapping(path="/authenticate")
    public @ResponseBody JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
    	String token = userService.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        return new JwtResponse(token);
    }

    /**
     * 
     * @param null
     * @return UserDetails
     */
    @GetMapping(value = "/me")
    public @ResponseBody UserDetails me(@AuthenticationPrincipal JwtUserDetails user) {
    	UserDetails userDetails = new UserDetails(user.getId(), user.getUsername(), user.getUserRole());
        return userDetails;
    }
}
