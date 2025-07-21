package it.uniroma3.siwbooks.service;

import org.springframework.stereotype.Service;

import static it.uniroma3.siwbooks.model.Credenziali.ADMIN_ROLE;
import static it.uniroma3.siwbooks.model.Credenziali.DEFAULT_ROLE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.repository.CredenzialiRepository;
import jakarta.validation.Valid;

@Service
public class CredenzialiService {

	@Autowired
	private CredenzialiRepository credenzialiRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Credenziali getCredenzialiByUsername(String username) {
		return this.credenzialiRepository.findByUsername(username);// .orElse(null);
	}

	public boolean existsByUsername(String username) {
		return this.credenzialiRepository.existsByUsername(username);
	}

	public void saveCredenziali(@Valid Credenziali credenziali) {
		credenziali.setRuolo(DEFAULT_ROLE);
		credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
		this.credenzialiRepository.save(credenziali);

	}

	public Credenziali getCredenzialiByUtente(Utente utente) {
		return this.credenzialiRepository.findByUtente(utente); // .orElse(null);
	}

	public boolean isAdminCorrente(Utente admin) {
		try {
			return this.getCredenzialiByUtente(admin).getRuolo().equals(ADMIN_ROLE);
		} catch (Exception e) {
			return false;
		}
	}

	public Utente getUtenteCorrente() {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.getCredenzialiByUsername(userDetails.getUsername()).getUtente();
	}
}
