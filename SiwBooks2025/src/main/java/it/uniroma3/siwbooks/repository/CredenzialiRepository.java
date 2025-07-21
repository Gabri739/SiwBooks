package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.model.Utente;

public interface CredenzialiRepository extends CrudRepository<Credenziali, Long>{

    public <Optional>Credenziali findByUsername(String username);

    public <Optional>Credenziali findByUtente(Utente utente);

    public boolean existsByUsername(String username);
}
