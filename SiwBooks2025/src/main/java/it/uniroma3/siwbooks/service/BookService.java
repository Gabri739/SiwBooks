package it.uniroma3.siwbooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwbooks.model.Author;
import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.repository.BookRepository;
import jakarta.validation.Valid;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> getBooks() {
		return (List<Book>) this.bookRepository.findAll();
	}

	public Book getBook(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	public void saveBook(@Valid Book book) {
		this.bookRepository.save(book);
	}

	public boolean removeAuthorFromBook(Long authorId, Long bookId) {
		try {
			this.bookRepository.deleteBookAuthorRelation(bookId, authorId);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public boolean existsBook(Long idBook) {
		return this.bookRepository.existsById(idBook);
	}

	public List<Book> getAvailableBooks(Long idAuthor) {
		return bookRepository.findBooksNotAssignedToAuthorId(idAuthor);
	}

	public void addBookToAuthor(Long idBook, Long idAuthor) {
		this.bookRepository.addBookToAuthor(idBook, idAuthor);
	}

	public void deleteBook(Book book) {
		this.bookRepository.delete(book);
	}

	public boolean existsBookWithImage(Long bookId, Long imgId) {

		return this.bookRepository.existsByIdAndImagesId(bookId, imgId);
	}

}