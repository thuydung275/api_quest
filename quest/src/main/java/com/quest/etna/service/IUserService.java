package com.quest.etna.service;

import java.util.List;

import com.quest.etna.model.User;

public interface IUserService<User> {
	
	public List<User> findAllUsers();
	
	public List<User> findUserListPerPage(Integer page, Integer limit);

    public User findUserById(Integer id);
    
    public User findUserByUsername(String username);
    
    public List<User> findAllByRole(String role);
    
    public String authenticate(String username, String password);

    public User createUser(User user);

    public User updateUser(Integer userId, User user, Boolean isAdmin);
    
    public Boolean deleteUser(Integer userId);
}
