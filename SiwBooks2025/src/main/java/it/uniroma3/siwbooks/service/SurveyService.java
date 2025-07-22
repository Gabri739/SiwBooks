package it.uniroma3.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.model.Survey;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class SurveyService {

	@Autowired
	private SurveyRepository surveyRepository;
	
	@Autowired
	private BookService bookService;

	public Survey findByUserAndBook(User user, Book book) {
		return this.surveyRepository.findByUserAndBook(user, book).orElse(null);
	}

	@Transactional
	public void saveSurvey(@Valid Survey survey) {
		this.surveyRepository.save(survey);
	}

	public Survey getSurvey(Long surveyId) {
		return this.surveyRepository.findById(surveyId).orElse(null);
	}


	public void deleteSurvey(Survey survey) {
		this.surveyRepository.delete(survey);
		
	}

	public boolean existsSurveyForBook(Long surveyId, Long bookId) {
		Survey survey = this.surveyRepository.findById(bookId).orElse(null);
		return survey != null && survey.getBook().getId() == bookId;
	}

	public void deleteSurveyById(Long surveyId) {
		this.surveyRepository.deleteById(surveyId);
		
	}
}