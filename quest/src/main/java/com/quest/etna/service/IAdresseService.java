package com.quest.etna.service;

import java.util.List;

import com.quest.etna.model.Adresse;
import com.quest.etna.model.User;

public interface IAdresseService<Adresse> {

	public List<Adresse> findAllAdresses();
	
	public List<Adresse> findAdresseListPerPage(Integer page, Integer limit);

    public Adresse findAdresseById(Integer id);
    
    public List<Adresse> findAdressesByUser(User user);
    
    public List<Adresse> findAdressesByCodePostal(String codePostal);
    
    public List<Adresse> findAdressesByRue(String rue);

    public List<Adresse> findAdressesByCity(String city);
    
    public List<Adresse> findAdressesByCountry(String country);

    public Adresse createAdresse(Adresse adresse);

    public Adresse updateAdresse(Integer id, Adresse adresse);

    public Boolean deleteAdresse(Integer id);

}
