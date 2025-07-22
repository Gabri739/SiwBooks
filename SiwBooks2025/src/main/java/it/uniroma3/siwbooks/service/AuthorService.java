package it.uniroma3.siwbooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siwbooks.model.Author;
import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.repository.AuthorRepository;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	
	public List<Author> getAuthors() {
		return (List<Author>) authorRepository.findAll();
	}

	public Author getAuthor(Long id) {
		return this.authorRepository.findById(id).orElse(null);
	}

	public void saveAuthor(Author author) {
		this.authorRepository.save(author);
		
	}

	public void deleteAuthor(Author author) {
		this.authorRepository.delete(author);
	}

	public boolean existsAuthor(Long idAuthor) {
		return this.authorRepository.existsById(idAuthor);
	}

	public List<Author> getAvaibleAuthors(Long idBook) {
		return this.authorRepository.findAuthorsNotAssignedToBookId(idBook);
	}
	
}