package com.quest.etna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.utils.Constants;
import com.quest.etna.utils.CustomException;

@Service
public class UserService implements IUserService<User> {
	
    @Autowired
    private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	private final static Logger log = Logger.getLogger(UserService.class);
    private final static String className = User.class.getSimpleName();
    private List<User> userList;

	@Override
	public List<User> findAllUsers() {
		userList = new ArrayList<User>();
        try {
        	userList = (List<User>) userRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return userList;
	}
	
	@Override
	public List<User> findUserListPerPage(Integer page, Integer limit) {
		userList = this.findAllUsers();
		
		if (!userList.isEmpty()) {
			try {
				PageRequest pageable = PageRequest.of(page, limit);
				userList = userRepository.getListPerPage(pageable);
			} catch (Exception e) {
	            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
	        }
		}

		return userList;
	}

	@Override
	public User findUserById(Integer id) {
		Optional<User> userOpt = userRepository.findById(id);
		
		if (userOpt.isPresent()) {
			return userOpt.get();
		}
		
		return null;
	}
    
	@Override
	public User findUserByUsername(String username) {
    	Optional<User> userOpt = userRepository.findByUsername(username);
		
		if (userOpt.isPresent()) {
			return userOpt.get();
		}
		
		return null;
	}

	@Override
	public List<User> findAllByRole(String role) {
		UserRole roleToFind = UserRole.ROLE_USER;
		if (role.toLowerCase().equals("admin")) {
			roleToFind = UserRole.ROLE_ADMIN;
		}
    	List<User> userList = new ArrayList<User>();
        try {
        	userList = userRepository.findAllByUserRole(roleToFind);
        } catch (Exception e) {
            e.getStackTrace();
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return userList;
	}

	@Override
	public String authenticate(String username, String password) {
		String token = null;
	
		try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);
            token = jwtTokenUtil.generateToken(jwtUserDetails);
        } catch (Exception e) {
        	throw new CustomException("identifiant ou mot de passe incorrect", Constants.ER_WRONG_EMAIL_PASS);
        }
		return token;
	}

	@Override
	public User createUser(User user) {
		User userCreated = null;
		User userExist = this.findUserByUsername(user.getUsername());
		if (userExist != null) {
//			throw new CustomException("username existe deja dans la base de données ", Constants.EXCEPTION_CODE_USER_ALREADY_EXIST);
		}
		
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			log.debug(user);
			userCreated = userRepository.save(user);
        } catch (Exception e) {
//            throw new CustomException("une erreur s'est produite lors de la création de " + className.toLowerCase(), Constants.ER_CREATION);
        }
        
        return userCreated;
	}

	@Override
	public User updateUser(Integer userId, User userToUpdate, Boolean isAdmin) {
		if (userToUpdate.getUsername() != null) {
			User userExist = this.findUserByUsername(userToUpdate.getUsername());
			
			if (userExist != null) {
				throw new CustomException("username existe deja dans la base de données ", Constants.EXCEPTION_CODE_USER_ALREADY_EXIST);
			}
		}

		User oldUser = null;
		try {
			oldUser = this.findUserById(userId);
			if (userToUpdate.getUsername() != null) {
				oldUser.setUsername(userToUpdate.getUsername());
			}
			if (userToUpdate.getPassword() != null) {
				oldUser.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
			}
			if (isAdmin && userToUpdate.getUserRole() != null) {
				oldUser.setUserRole(userToUpdate.getUserRole());
			}
			userRepository.save(oldUser);
		} catch(Exception e) {
			throw new CustomException("une erreur s'est produite lors de la modification de " + className.toLowerCase(), Constants.ER_UPDATE);
		}
		
		return oldUser;
	}

	@Override
	public Boolean deleteUser(Integer userId) {
		try {
			User user = this.findUserById(userId);
			userRepository.delete(user);
		}catch(Exception e) {
			throw new CustomException("une erreur s'est produite lors de la suppression de " + className.toLowerCase(), Constants.ER_DELETEE);
		}
		return true;
	}

}
