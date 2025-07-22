package it.uniroma3.siwbooks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siwbooks.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE :bookId NOT IN (SELECT b.id FROM a.books b)")
    List<Author> findAuthorsNotAssignedToBookId(@Param("bookId") Long bookId);
}