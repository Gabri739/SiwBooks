package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long>{

}
