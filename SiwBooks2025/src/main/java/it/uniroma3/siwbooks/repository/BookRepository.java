package it.uniroma3.siwbooks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siwbooks.model.Author;
import it.uniroma3.siwbooks.model.Book;
import jakarta.transaction.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {
	@Query(value = "DELETE FROM book_authors WHERE books_id = :bookId AND authors_id = :authorId", nativeQuery = true)
	void deleteBookAuthorRelation(@Param("bookId") Long bookId, @Param("authorId") Long authorId);

	// Restituisce tutti i libri che NON sono associati all'autore con un certo id
    @Query("SELECT b FROM Book b WHERE :author NOT MEMBER OF b.authors")
    List<Book> findBooksNotAssignedToAuthor(@Param("author") Author author);

    // Variante con solo id (più efficiente per entità grandi):
    @Query("SELECT b FROM Book b WHERE :authorId NOT IN (SELECT a.id FROM b.authors a)")
    List<Book> findBooksNotAssignedToAuthorId(@Param("authorId") Long authorId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO book_authors (authors_id, books_id) VALUES (:authorId, :bookId)", nativeQuery = true)
    void addBookToAuthor(@Param("bookId") Long bookId, @Param("authorId") Long authorId);
    
    boolean existsByIdAndImagesId(Long bookId, Long imageId);
    
    
}