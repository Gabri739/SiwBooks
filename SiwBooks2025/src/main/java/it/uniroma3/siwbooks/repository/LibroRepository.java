package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long>{
    // Il CrudRepository fornisce già i metodi di base come save, findById, delete, etc.
    // Puoi aggiungere metodi personalizzati se necessario. 

}
