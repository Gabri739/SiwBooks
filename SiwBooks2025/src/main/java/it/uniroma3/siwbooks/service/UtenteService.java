package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.repository.UtenteRepository;

import jakarta.validation.Valid;

@Service
public class UtenteService {

	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private CredenzialiService credenzialiService;

	public Utente getUtenteCorrente() {
		return credenzialiService.getUtenteCorrente();
	}

	public Utente findById(Long id) {
		return utenteRepository.findById(id).orElse(null);
	}

	public void saveUtente(@Valid Utente utente) {
		this.utenteRepository.save(utente);
		
	}
}
