package it.uniroma3.siwbooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.model.Survey;
import it.uniroma3.siwbooks.model.User;

import java.util.Optional;


public interface SurveyRepository extends CrudRepository<Survey, Long> {

	public Optional<Survey> findByUserAndBook(User user, Book book);
}