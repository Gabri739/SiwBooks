package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}