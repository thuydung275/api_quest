package com.quest.etna;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.quest.etna.model.User;
import com.quest.etna.service.AdresseService;
import com.quest.etna.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTest.sql") })
@AutoConfigureMockMvc
public class ControllerTests {
	
    @Autowired
    private MockMvc mockMvc;
    
	@Autowired
    private AdresseService adresseService;
	
	@Autowired
    private UserService userService;

    private static String token = null;
    private static String adminToken = null;
    
    @Test
    public void testAuthenticate() throws Exception {
    	// La route /register répond bien en 201
    	this.mockMvc
    		.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
    				.content("{\"username\":\"user\",\"password\":\"user\",\"userRole\":\"ROLE_USER\"}"))
    		.andExpect(status().isCreated())
    		.andDo(print())
    		.andReturn();
    	 
    	// Que si vous rappelez /register avec les mêmes paramètres, vous obtenez une
    	// réponse 500 car l’utilisateur existe déjà.
    	this.mockMvc
         	.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
         			.content("{\"username\":\"user\",\"password\":\"user\",\"role\":\"ROLE_USER\"}"))
         	.andExpect(status().isInternalServerError())
         	.andDo(print())
         	.andReturn();
         
         // La route /authenticate renvoie un statut 200 et retourne bien votre token.
         MvcResult mvcResult = this.mockMvc
                 .perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
                         .content("{\"username\":\"user\",\"password\":\"user\"}"))
                 .andExpect(status().isOk())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                 .andDo(print())
                 .andReturn();

         JSONObject jsonObj = new JSONObject(mvcResult.getResponse().getContentAsString());
         ControllerTests.token = jsonObj.getString("token");

         // La route /me retourne un statut 200 avec les informations de l’utilisateur
         this.mockMvc
                 .perform(MockMvcRequestBuilders.get("/me")
                         .header("Authorization", "Bearer " + token)
                 )
                 .andExpect(status().isOk())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value("ROLE_USER"))
                 .andDo(print())
                 .andReturn();
    }
    
    @Test
    public void testUser() throws Exception {
    	// get token
    	this.getUserToken();
    	this.getAdminToken();

	     // La route /user retourne bien un statut 401 sans Token Bearer
	     this.mockMvc
	  		.perform(MockMvcRequestBuilders.get("/user"))
	  		.andExpect(status().isUnauthorized())
	  		.andDo(print())
	  		.andReturn();
	
	     // La route /user retourne bien un statut 200 avec Token Bearer valide.
		this.mockMvc
		     .perform(MockMvcRequestBuilders.get("/user").header("Authorization", "Bearer " + ControllerTests.token))
		     .andExpect(status().isOk())
		     .andDo(print())
		     .andReturn();
	
		// Avec un ROLE_USER, la suppression retourne un statut 401
		
		this.mockMvc
     	.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
     			.content("{\"username\":\"usertest\",\"password\":\"usertest\",\"role\":\"ROLE_USER\"}"))
     	.andExpect(status().isCreated())
     	.andDo(print())
     	.andReturn();
		
		User userToDelete = userService.findUserByUsername("usertest");
		
		this.mockMvc
		    .perform(MockMvcRequestBuilders.delete("/user/delete/{id}", userToDelete.getId())
		    		.contentType(MediaType.APPLICATION_JSON)
		    		.header("Authorization", "Bearer " + ControllerTests.token)
		    		)
		    .andExpect(status().isUnauthorized())
		    .andDo(print())
		    .andReturn();
		
		// Avec un ROLE_ADMIN, la suppression retourne bien un statut 200.
		this.mockMvc
		     .perform(MockMvcRequestBuilders.delete("/user/delete/{id}", userToDelete.getId())
		    		 .contentType(MediaType.APPLICATION_JSON)
		    		 .header("Authorization", "Bearer " + ControllerTests.adminToken)
		    		 )
		     .andExpect(status().isOk())
		     .andDo(print())
		     .andReturn();
    }
    
    @Test
    public void testAdress() throws Exception {
    	// get token
    	this.getUserToken();
    	this.getAdminToken();

		// La route /adresse retourne bien un statut 401 sans Token Bearer.
    	this.mockMvc
	        .perform(MockMvcRequestBuilders.get("/adresse"))
	        .andExpect(status().isUnauthorized())
	        .andDo(print())
	        .andReturn();
    	
		// L’ajout d’une adresse retourne bien un statut 201
		this.mockMvc
	     	.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
	     			.content("{\"username\":\"usertest\",\"password\":\"usertest\",\"role\":\"ROLE_USER\"}"))
	     	.andExpect(status().isCreated())
	     	.andDo(print())
	     	.andReturn();
 
		MvcResult mvcResult = this.mockMvc
	         .perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
	                 .content("{\"username\":\"usertest\",\"password\":\"usertest\"}"))
	         .andExpect(status().isOk())
	         .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
	         .andDo(print())
	         .andReturn();

		 JSONObject jsonObj = new JSONObject(mvcResult.getResponse().getContentAsString());
		 String tokenToCreateAdress = jsonObj.getString("token");
		 
		 this.mockMvc
	        .perform(MockMvcRequestBuilders.post("/adresse/create")
	        		.header("Authorization", "Bearer " + tokenToCreateAdress)
	        		.contentType(MediaType.APPLICATION_JSON)
	                .content("{\"rue\":\"rue\",\"postalCode\":\"75\",\"city\":\"city\",\"country\":\"France\"}")
	        )
	        .andExpect(status().isCreated())
	        .andDo(print())
	        .andReturn();
    	
    	// La route /adresse retourne bien un statut 200 avec un Token Bearer
		this.mockMvc
		     .perform(MockMvcRequestBuilders.get("/adresse").header("Authorization", "Bearer " + ControllerTests.token))
		     .andExpect(status().isOk())
		     .andDo(print())
		     .andReturn();
		

		
		// Avec un ROLE_USER, la suppression d’une adresse qui n’est pas la sienne retourne un statut 401
		this.mockMvc
	        .perform(MockMvcRequestBuilders.delete("/adresse/delete/{id}",1).header("Authorization", "Bearer " + ControllerTests.token))
	        .andExpect(status().isUnauthorized())
	        .andDo(print())
	        .andReturn();
    	
		// Avec un ROLE_ADMIN, la suppression d’une adresse qui n’est pas la sienne retourne un status 200
		this.mockMvc
		     .perform(MockMvcRequestBuilders.delete("/adresse/delete/{id}", 1)
		    		 .contentType(MediaType.APPLICATION_JSON)
		    		 .header("Authorization", "Bearer " + ControllerTests.adminToken)
		    		 )
		     .andExpect(status().isOk())
		     .andDo(print())
		     .andReturn();
    }
    
    private void getUserToken() throws Exception {
    	this.mockMvc
	     	.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
	     			.content("{\"username\":\"user\",\"password\":\"user\",\"role\":\"ROLE_USER\"}"))
	     	.andExpect(status().isCreated())
	     	.andDo(print())
	     	.andReturn();
     
	     MvcResult mvcResult = this.mockMvc
	             .perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
	                     .content("{\"username\":\"user\",\"password\":\"user\"}"))
	             .andExpect(status().isOk())
	             .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
	             .andDo(print())
	             .andReturn();
	
	     JSONObject jsonObj = new JSONObject(mvcResult.getResponse().getContentAsString());
	     ControllerTests.token = jsonObj.getString("token");
    }
    
    private void getAdminToken() throws Exception {
    	this.mockMvc
			.perform(MockMvcRequestBuilders.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
	 			.content("{\"username\":\"admin\",\"password\":\"admin\",\"userRole\":\"ROLE_ADMIN\"}"))
			.andExpect(status().isCreated())
			.andDo(print())
			.andReturn();
	 
		MvcResult mvcAdminResult = this.mockMvc
	         .perform(MockMvcRequestBuilders.post("/authenticate")
	        		 .contentType(MediaType.APPLICATION_JSON)
	                 .content("{\"username\":\"admin\",\"password\":\"admin\"}"))
	         .andExpect(status().isOk())
	         .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
	         .andDo(print())
	         .andReturn();
	
	     JSONObject adminJsonObj = new JSONObject(mvcAdminResult.getResponse().getContentAsString());
	     ControllerTests.adminToken = adminJsonObj.getString("token");
    }

}
