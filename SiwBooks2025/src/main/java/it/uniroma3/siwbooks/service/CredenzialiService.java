package it.uniroma3.siwbooks.service;

import org.springframework.stereotype.Service;

import static it.uniroma3.siwbooks.model.Credenziali.ADMIN_ROLE;
import static it.uniroma3.siwbooks.model.Credenziali.DEFAULT_ROLE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.repository.CredenzialiRepository;

@Service
public class CredenzialiService {

	   @Autowired
	   private CredenzialiRepository credenzialiRepository;

	   @Autowired
	   private PasswordEncoder passwordEncoder;

	   public Utente getUtenteCorrente() {
			   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			   if (authentication == null || !authentication.isAuthenticated()) {
					   return null;
			   }
			   Object principal = authentication.getPrincipal();
			   String username = null;
			   if (principal instanceof UserDetails) {
					   username = ((UserDetails) principal).getUsername();
			   } else if (principal instanceof String) {
					   username = (String) principal;
			   }
			   if (username == null) {
					   return null;
			   }
			   Credenziali credenziali = credenzialiRepository.findByUsername(username);
			   if (credenziali != null) {
					   return credenziali.getUtente();
			   }
			   return null;
	   }

	   public Credenziali getCredenzialiByUsername(String username) {
			   return credenzialiRepository.findByUsername(username);
	}

	   	public Credenziali getCredenzialiByUtente(Utente utente) {
			return credenzialiRepository.findByUtente(utente);
		}

	   public boolean isAdminCorrente(String username) {
			Credenziali credenziali = credenzialiRepository.findByUsername(username);
			if (credenziali != null && credenziali.getRuolo() != null) {
					return credenziali.getRuolo().equals(ADMIN_ROLE);
			}
			return false;
	   }

	   public boolean existsByUsername(String username) {
			return credenzialiRepository.existsByUsername(username);
	   }

	   public void saveCredenziali(Credenziali credenziali) {
			credenziali.setRuolo(DEFAULT_ROLE); // Imposta il ruolo come DEFAULT_ROLE di base
			credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword())); // Codifica la passord e la cripta
			credenzialiRepository.save(credenziali);
	   }




}
