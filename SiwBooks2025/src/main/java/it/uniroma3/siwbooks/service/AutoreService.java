package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Autore;
import it.uniroma3.siwbooks.repository.AutoreRepository;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository; 

    public Iterable<Autore> getAutori() {
        // Implementazione per ottenere tutti gli autori
        return autoreRepository.findAll();
    }

    public Autore getAutore(Long id) {
        // Implementazione per ottenere un autore per ID
        return autoreRepository.findById(id).orElse(null);
    }

    public void saveAutore(Autore autore) {
        // Implementazione per salvare un autore
        autoreRepository.save(autore);
    }

    public void deleteAutore(Autore autore) {
        // Implementazione per eliminare un autore per ID
        autoreRepository.delete(autore);
    }

    public boolean existsAutore(Long id) {
        // Implementazione per verificare se un autore esiste
        return autoreRepository.existsById(id);
    }

    public Iterable<Autore> findAllAutori() {
        // Implementazione per ottenere tutti gli autori
        return autoreRepository.findAll();
    }

}
