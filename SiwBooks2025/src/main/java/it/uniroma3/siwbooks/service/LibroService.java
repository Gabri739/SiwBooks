package it.uniroma3.siwbooks.service;

import it.uniroma3.siwbooks.model.Libro;
import it.uniroma3.siwbooks.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroService {;

    @Autowired
    private LibroRepository libroRepository;

    public Libro getLibro(Long id) {
        // Implementazione per ottenere un libro per ID
        return libroRepository.findById(id).orElse(null);
    }

    public void saveLibro(Libro libro) {
        // Implementazione per salvare un libro
        libroRepository.save(libro);
    }

    public void deleteLibro(Libro libro) {
        // Implementazione per eliminare un libro per ID
        libroRepository.delete(libro);
    }

    public boolean existsLibro(Long id) {
        // Implementazione per verificare se un libro esiste
        return libroRepository.existsById(id);
    }

    public Iterable<Libro> findAllLibri() {
        // Implementazione per ottenere tutti i libri
        return libroRepository.findAll();
    }


}
