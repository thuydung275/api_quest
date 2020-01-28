package com.quest.etna.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Adresse;
import com.quest.etna.model.User;

@Repository
public interface AdresseRepository extends PagingAndSortingRepository<Adresse, Integer> {
	
	@Query("SELECT ad FROM Adresse ad WHERE ad.rue LIKE %:rue%")
	public List<Adresse> findAllByRue(String rue);
	
	@Query("SELECT ad FROM Adresse ad WHERE ad.city LIKE %:city%")
	public List<Adresse> findAllByCity(String city);
	
	@Query("SELECT ad FROM Adresse ad WHERE ad.postalCode LIKE %:postalCode%")
	public List<Adresse> findAllByPostalCode(String postalCode);
	
	@Query("SELECT ad FROM Adresse ad WHERE ad.country LIKE %:country%")
	public List<Adresse> findAllByCountry(String country);
	
	@Query("SELECT ad FROM Adresse ad WHERE ad.user = :user")
	public List<Adresse> findAllByUser(User user);
	
	@Query("SELECT ad FROM Adresse ad ORDER BY ad.id ASC")
	public List<Adresse> getListPerPage(Pageable pageable);
}
