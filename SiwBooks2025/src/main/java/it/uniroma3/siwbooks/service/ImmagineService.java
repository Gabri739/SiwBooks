package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Immagine;
import it.uniroma3.siwbooks.repository.ImmagineRepository;

@Service
public class ImmagineService {
    
    
	@Autowired
    private ImmagineRepository immagineRepository;
	
    public Immagine getImmagine(Long id) {
        return immagineRepository.findById(id).orElse(null);
    }
}
