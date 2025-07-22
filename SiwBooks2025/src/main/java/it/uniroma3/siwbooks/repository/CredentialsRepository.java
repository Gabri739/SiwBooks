package it.uniroma3.siwbooks.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Credentials;
import it.uniroma3.siwbooks.model.User;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
	
	public Optional<Credentials> findByUser(User user);
	
	public boolean existsByUsername(String username);
}