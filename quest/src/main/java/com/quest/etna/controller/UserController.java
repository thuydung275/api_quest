package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.JwtRequest;
import com.quest.etna.model.JwtResponse;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;
import com.quest.etna.model.UserDetails;
import com.quest.etna.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	private List<UserDetails> userDetailsList;
	
	private final static Logger log = Logger.getLogger(UserController.class);
    private final static String className = UserController.class.getSimpleName();
	
	/**
	 * example route with GET parameters "/user/list?page=1&limit=3" OR "user/list"
	 * @param page
	 * @param limit
	 * @param JwtUser
	 * @return List<UserDetails>
	 */
	@GetMapping(path="/list")
	public @ResponseBody List<UserDetails> getListPerPage(
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="3") Integer limit,
			@AuthenticationPrincipal JwtUserDetails JwtUser)
	{
		List<User> userList = userService.findUserListPerPage(page - 1, limit);
		return this.toUserDetailsList(userList);
	}
	
	/**
	 * 
	 * @return List<UserDetails>
	 */
	@GetMapping(path="")
	public @ResponseBody List<UserDetails> findUsers(@AuthenticationPrincipal JwtUserDetails JwtUser) {
		List<User> userList = userService.findAllUsers();
		return this.toUserDetailsList(userList);
	}
	
	/**
	 * 
	 * @return List<UserDetails>
	 */
	@GetMapping(path="/all")
	public @ResponseBody List<UserDetails> findAll(@AuthenticationPrincipal JwtUserDetails JwtUser) {
		List<User> userList = userService.findAllUsers();
		return this.toUserDetailsList(userList);
	}
	
	/**
	 * 
	 * @param id
	 * @return UserDetails
	 */
	@GetMapping(path="/id/{id}")
	public @ResponseBody UserDetails findById(@PathVariable("id") Integer id, @AuthenticationPrincipal JwtUserDetails JwtUser) {
		User user = userService.findUserById(id);
		return this.toUserDetails(user);
	}
	
	/**
	 * 
	 * @param username
	 * @return UserDetails
	 */
	@GetMapping(path="/username/{username}")
	public @ResponseBody UserDetails findByUsername(@PathVariable("username") String username, @AuthenticationPrincipal JwtUserDetails JwtUser) {
		User user = userService.findUserByUsername(username);
		return this.toUserDetails(user);
	}
	
	/**
	 * example route "/user/role/admin", "user/role/user"
	 * @param role 
	 * @return List<UserDetails>
	 */
	@GetMapping(path="/role/{role}")
	public @ResponseBody List<UserDetails> findByRole(@PathVariable("role") String role, @AuthenticationPrincipal JwtUserDetails JwtUser) {
		List<User> userList = userService.findAllByRole(role);
		return this.toUserDetailsList(userList);
	}
	
	/**
	 * route example:  "user/update/2"
	 * @param userId
	 * @param user :
	 * {
	 * 		"username": "updatedUser",
	 * 		"password": "user",
	 * 		"userRole" "ROLE_ADMIN"
	 * }
	 * 
	 * user: {
	 * 		"password": "updateOnlyPassword",
	 * }
	 * 
	 * user : {
	 * 		"username": "updatedOnlyUsername",
	 * }
	 * 
	 * @return
	 */
    @PutMapping(value="/update/{id}")
    public @ResponseBody UserDetails updateUser(@RequestBody User user, @PathVariable("id") Integer userId, @AuthenticationPrincipal JwtUserDetails JwtUser) {
    	User userUpdated = null;
    	Boolean isAdmin = JwtUser.getUserRole().equals(UserRole.ROLE_ADMIN);
    	if (isAdmin || userId == JwtUser.getId()) {
    		userUpdated = userService.updateUser(userId, user, isAdmin);
    	}
    	return this.toUserDetails(userUpdated);
    }
    
    /**
     * route example: "user/delete/2"
     * @param userId
     * @param JwtUser
     * @return Boolean (true=removed)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer userId, @AuthenticationPrincipal JwtUserDetails JwtUser) {
    	Boolean isAdmin = JwtUser.getUserRole().equals(UserRole.ROLE_ADMIN);
    	Boolean result = false;
    	if (userId != JwtUser.getId() && !isAdmin) {
    		return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    	}
    	result = this.userService.deleteUser(userId);
    	
    	return new ResponseEntity(result, HttpStatus.OK);
    }
	
	private List<UserDetails> toUserDetailsList(List<User> userList) {
		userDetailsList = new ArrayList<UserDetails>();
		if (!userList.isEmpty()) {
			for (User user : userList) {
				UserDetails userDetails = new UserDetails(user.getId(), user.getUsername(), user.getUserRole());
				userDetailsList.add(userDetails);
			}
		}
		return userDetailsList;
	}
	
	private UserDetails toUserDetails(User user) {
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetails(user.getId(), user.getUsername(), user.getUserRole());
		}
		return userDetails;
	}
	
}
