package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Libro;
import it.uniroma3.siwbooks.model.Recensione;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.repository.RecensioneRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class RecensioneService {

	@Autowired
	private RecensioneRepository recensioneRepository;

	public Recensione findByUtenterAndLibro(Utente utente, Libro libro) {
		return this.recensioneRepository.findByUtenteAndLibro(utente, libro).orElse(null);
	}

	@Transactional
	public void saveRecensione(@Valid Recensione recensione) {
		this.recensioneRepository.save(recensione);
	}

	public Recensione getRecensione(Long recensioneID) {
		return this.recensioneRepository.findById(recensioneID).orElse(null);
	}

	public void deleteRecensione(Recensione recensione) {
		this.recensioneRepository.delete(recensione);
		
	}
}
