package com.quest.etna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import com.quest.etna.model.Adresse;
import com.quest.etna.model.User;
import com.quest.etna.repository.AdresseRepository;
import com.quest.etna.utils.Constants;
import com.quest.etna.utils.CustomException;

@Service
public class AdresseService implements IAdresseService<Adresse> {
	
	private final static Logger log = Logger.getLogger(AdresseService.class);
    private final static String className = AdresseService.class.getSimpleName();

	
	@Autowired
    private AdresseRepository adresseRepository;
	
	private List<Adresse> adList;
	
	@Override
	public List<Adresse> findAllAdresses() {
		adList = new ArrayList<Adresse>();
        try {
        	adList = (List<Adresse>) adresseRepository.findAll();
        } catch (Exception e) {
        	log.debug(e);
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}

	@Override
	public List<Adresse> findAdresseListPerPage(Integer page, Integer limit) {
		adList = this.findAllAdresses();
		
		if (!adList.isEmpty()) {
			try {
				PageRequest pageable = PageRequest.of(page, limit);
				adList = adresseRepository.getListPerPage(pageable);
			} catch (Exception e) {
	            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
	        }
		}

		return adList;
	}

	@Override
	public Adresse findAdresseById(Integer id) {
		Optional<Adresse> adresseOpt = adresseRepository.findById(id);
		
		if (adresseOpt.isPresent()) {
			return adresseOpt.get();
		}
		
		return null;
	}

	@Override
	public List<Adresse> findAdressesByRue(String rue) {
		adList = new ArrayList<Adresse>();
        try {
        	adList = (List<Adresse>) adresseRepository.findAllByRue(rue);
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}
	
	@Override
	public List<Adresse> findAdressesByCodePostal(String postalCode) {
		adList = new ArrayList<Adresse>();
        try {
        	adList = (List<Adresse>) adresseRepository.findAllByPostalCode(postalCode);
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}

	@Override
	public List<Adresse> findAdressesByCity(String city) {
		adList = new ArrayList<Adresse>();
        try {
        	adList = (List<Adresse>) adresseRepository.findAllByCity(city);
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}

	@Override
	public List<Adresse> findAdressesByCountry(String country) {
		adList = new ArrayList<Adresse>();
        try {
        	adList = (List<Adresse>) adresseRepository.findAllByCountry(country);
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}
	
	
	@Override
	public List<Adresse> findAdressesByUser(User user) {
		adList = new ArrayList<Adresse>();
        try {
        	adList = adresseRepository.findAllByUser(user);
        } catch (Exception e) {
        	log.debug(e);
            throw new CustomException("une erreur s'est produite lors de l'affichage des " + className.toLowerCase(), Constants.ER_NOT_FOUND);
        }

        return adList;
	}

	@Override
	public Adresse createAdresse(Adresse adresse) {
		Adresse adCreated = null;
		try {
			adCreated = adresseRepository.save(adresse);
        } catch (Exception e) {
            throw new CustomException("une erreur s'est produite lors de la création de " + className.toLowerCase(), Constants.ER_CREATION);
        }
        
        return adCreated;
	}

	@Override
	public Adresse updateAdresse(Integer id, Adresse adrToUpdate) {
		Adresse oldAdresse = null;
		
		try {
			oldAdresse = this.findAdresseById(id);
			oldAdresse.setRue(adrToUpdate.getRue());
			oldAdresse.setPostalCode(adrToUpdate.getPostalCode());
			oldAdresse.setCity(adrToUpdate.getCity());
			oldAdresse.setCountry(adrToUpdate.getCountry());
			if (adrToUpdate.getUser() != null) {
				oldAdresse.setUser(adrToUpdate.getUser());
			}
			adresseRepository.save(oldAdresse);
			
		} catch(Exception e) {
			log.debug(e);
			throw new CustomException("une erreur s'est produite lors de la modification de " + className.toLowerCase(), Constants.ER_UPDATE);
		}
		
		return oldAdresse;
	}

	@Override
	public Boolean deleteAdresse(Integer adresseId) {
		try {
			Adresse adresse = this.findAdresseById(adresseId);
			adresseRepository.delete(adresse);
		}catch(Exception e) {
			throw new CustomException("une erreur s'est produite lors de la suppression de " + className.toLowerCase(), Constants.ER_DELETEE);
		}
		return true;
	}

}
