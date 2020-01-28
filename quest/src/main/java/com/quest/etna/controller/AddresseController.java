package com.quest.etna.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.Adresse;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;
import com.quest.etna.service.AdresseService;
import com.quest.etna.service.UserService;
import com.quest.etna.tdo.AdresseTDO;

@RestController
@RequestMapping("/adresse")
public class AddresseController {
	
	@Autowired
    private AdresseService adresseService;
	
	@Autowired
    private UserService userService;
	
	private final static Logger log = Logger.getLogger(UserController.class);
    private final static String className = UserController.class.getSimpleName();
	
	/**
	 * 
	 * @param page
	 * @param limit
	 * @param jwtUser
	 * @return List<Adresse>
	 */
	@GetMapping("/list")
	public @ResponseBody List<Adresse> getListPerPage(
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="3") Integer limit, 
			@AuthenticationPrincipal JwtUserDetails jwtUser) {
		if (jwtUser.getUserRole().equals(UserRole.ROLE_ADMIN)) {
			return adresseService.findAdresseListPerPage(page - 1, limit);
		} 
		return null;
	}
	
	/**
	 * 
	 * @return List<Adresse>
	 */
	@GetMapping(path="")
	public ResponseEntity findAllAdresses(@AuthenticationPrincipal JwtUserDetails jwtUser) {
		List<Adresse> adresseList = adresseService.findAllAdresses();
		return new ResponseEntity(adresseList, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return List<Adresse>
	 */
	@GetMapping(path="/all")
	public @ResponseBody List<Adresse> findAll(@AuthenticationPrincipal JwtUserDetails jwtUser) {
		if (jwtUser.getUserRole().equals(UserRole.ROLE_ADMIN)) {
			return adresseService.findAllAdresses();
		} 
		return null;
	}
	
	/**
	 * route: "adresse/id/2"
	 * @param id
	 * @param jwtUser
	 * @return Adresse
	 */
	@GetMapping("/id/{id}")
	public @ResponseBody Adresse findById(@PathVariable("id") Integer adresseId, @AuthenticationPrincipal JwtUserDetails jwtUser) {
		if (this.canDo(adresseId, jwtUser) == false) {
			return null;
		}
		
		return adresseService.findAdresseById(adresseId);
	}
	
	/**
	 * route: "adresse/user/3"
	 * @param userId
	 * @param jwtUser
	 * @return List<Adresse>
	 */
	@GetMapping(path="/user/{id}")
	public @ResponseBody List<Adresse> findByUser(@PathVariable("id") Integer userId, @AuthenticationPrincipal JwtUserDetails jwtUser) {
		Boolean isAdmin = jwtUser.getUserRole().equals(UserRole.ROLE_ADMIN);
		User user = userService.findUserById(userId);
		
		if (isAdmin || (user != null && userId == jwtUser.getId())) {
			return adresseService.findAdressesByUser(user);
		} 
		return null;
	}
	
	@PostMapping("/create")
	public ResponseEntity<Adresse> createAdresse(@RequestBody AdresseTDO adresseTDO, @AuthenticationPrincipal JwtUserDetails jwtUser) {
		Integer userId = jwtUser.getId();
		User user = userService.findUserById(userId);
		Adresse adresse = new Adresse(adresseTDO.getRue(), adresseTDO.getPostalCode(), adresseTDO.getCity(), adresseTDO.getCountry());
		if (user.getUserRole().equals(UserRole.ROLE_ADMIN) && adresseTDO.getUserId() != null) {
			try {
				User userForAdresse = userService.findUserById(adresseTDO.getUserId());
				if (userForAdresse != null) {
					adresse.setUser(userForAdresse);
				}
			} catch(Exception e) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
		} else {
			adresse.setUser(user);
		}
		Adresse createdAd = adresseService.createAdresse(adresse);
		return new ResponseEntity(createdAd, HttpStatus.CREATED);
	}
	
	/**
	 * ex: {
	 *		"rue": "bercy",
	 *		"postalCode": "75012",
	 *		"city": "Paris",
	 *		"country": "France",
	 *		"userId": 8
	 * }
	 * @param id
	 * @param adresse
	 * @param jwtUser
	 * @return Adresse
	 */
	@PutMapping("/update/{id}")
	public @ResponseBody Adresse updateAdresse(
			@PathVariable("id") Integer adresseId,
			@RequestBody AdresseTDO adresse,
			@AuthenticationPrincipal JwtUserDetails jwtUser) 
	{
		if (this.canDo(adresseId, jwtUser) == false) {
			return null;
		}
		User user = userService.findUserById(adresse.getUserId());
		if (user == null) {
			return null;
		}
		Adresse updateAdresse = new Adresse(adresseId, adresse.getRue(), adresse.getPostalCode(), adresse.getCity(), adresse.getCountry(), user);
		return adresseService.updateAdresse(adresseId, updateAdresse);
	}
	
	/**
	 * route: "adresse/delete/3"
	 * @param id 
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> deleteAdresse(@PathVariable("id") Integer adresseId, @AuthenticationPrincipal JwtUserDetails jwtUser) {
		Adresse adresse = adresseService.findAdresseById(adresseId);
		if (this.canDo(adresseId, jwtUser) == false) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Boolean result = adresseService.deleteAdresse(adresseId);
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	private Boolean canDo(Integer id, JwtUserDetails jwtUser) {
		Boolean isAdmin = jwtUser.getUserRole().equals(UserRole.ROLE_ADMIN);
		Adresse adresseFromDB = adresseService.findAdresseById(id);
		if (adresseFromDB == null) {
			return false;
		}
		if (isAdmin || adresseFromDB.getUser().getId() == jwtUser.getId()) {
			return true;
		}
		return false;
	}
}
