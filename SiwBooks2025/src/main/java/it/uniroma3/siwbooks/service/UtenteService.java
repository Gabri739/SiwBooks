package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.repository.UtenteRepository;

@Service
public class UtenteService {
	
	@Autowired
	private UtenteRepository utenterepository;
	
	@Autowired
	private CredenzialiService credenzialiservice;
	
	private Utente UtenteById(Long Id) {
		// Implementazione per ottenere un utente per ID
		return utenterepository.findById(Id).orElse(null);
	}
	
	private void saveUtente(Utente utente) {
		// Implementazione per salvare un utente
		utenterepository.save(utente);
	}
	
	private Utente getUtenteCorrente() {
		// Implementazione per ottenere l'utente corrente
		return credenzialiservice.getUtenteCorrente();
	}
}
