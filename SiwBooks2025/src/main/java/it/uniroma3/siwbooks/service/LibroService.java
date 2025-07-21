package it.uniroma3.siwbooks.service;

import it.uniroma3.siwbooks.model.Libro;
import it.uniroma3.siwbooks.repository.LibroRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroService {
	
	@Autowired
	private LibroRepository libroRepository;
	
	public List<Libro> getLibri() {
		return (List<Libro>) this.libroRepository.findAll();
	}

	public Libro getLibro(Long id) {
		return libroRepository.findById(id).get();
	}

	public void saveLibro(Libro libro) {
		this.libroRepository.save(libro);
	}


}
