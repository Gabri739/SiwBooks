package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.model.Utente;

public interface CredenzialiRepository extends CrudRepository<Credenziali, Long>{

    public Credenziali findByUsername(String username);

    public Credenziali findByUtente(Utente utente);

    public boolean existsByUsername(String username);
}
