package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Recensione;
import it.uniroma3.siwbooks.repository.RecensioneRepository;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    public void saveRecensione(Recensione recensione) {
        // Implementazione per salvare una recensione
        recensioneRepository.save(recensione);
    }
    
    public void deleteRecensione(Recensione recensione) {
        // Implementazione per eliminare una recensione
        recensioneRepository.delete(recensione);
    }

    public void deleteRecensione(Long id) {
        // Implementazione per eliminare una recensione
        recensioneRepository.deleteById(id);
    }

    public Iterable<Recensione> findAllRecensioni() {
        // Implementazione per ottenere tutte le recensioni
        return recensioneRepository.findAll();
    }

    public Recensione getRecensione(Long id) {
        // Implementazione per ottenere una recensione per ID
        return recensioneRepository.findById(id).orElse(null);
    }

    public boolean existsRecensionePerLibro(Long id , Long idlibro) {
        // Implementazione per verificare se una recensione esiste
        Recensione recensione = recensioneRepository.findById(idlibro).orElse(null);
        // Controlla se la recensione esiste e se il libro associato ha l'ID specificato
        return recensione != null && recensione.getLibro().getId().equals(id);
    }
}
