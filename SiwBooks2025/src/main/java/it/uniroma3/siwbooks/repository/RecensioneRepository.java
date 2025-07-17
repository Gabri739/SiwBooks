package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Recensione;
import it.uniroma3.siwbooks.model.Libro;
import java.util.Optional;


public interface RecensioneRepository extends CrudRepository<Recensione, Long>{

    public Optional<Recensione> findByUtenteIdAndLibro(Object utenteId, Libro libro);
}
