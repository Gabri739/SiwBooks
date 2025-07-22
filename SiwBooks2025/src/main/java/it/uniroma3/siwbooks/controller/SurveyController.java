package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.model.Survey;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.service.BookService;
import it.uniroma3.siwbooks.service.CredentialsService;
import it.uniroma3.siwbooks.service.SurveyService;
import it.uniroma3.siwbooks.service.UserService;

@Controller
public class SurveyController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	private boolean verifyBookAndUser(Book book, User user) {
		return book != null && user != null;
	}
	
	private boolean verifyId(Long id1, Long id2) {
		return id2!= null && id1 == id2;
	}
	
	private boolean verifyAdmin(User admin) {
		return credentialsService.isAdminCurrent(admin);
	}
	
    /**
     * Gestisce l'inserimento di una nuova recensione
     */
    @PostMapping("/user/{userId}/books/{bookId}/survey")
    public String addSurvey(@PathVariable("userId") Long userId,
    					  @PathVariable("bookId") Long bookId,
                          @RequestParam("title") String title,
                          @RequestParam("text") String text,
                          @RequestParam("mark") Integer mark,
                          Model model) {
        
        Book book = this.bookService.getBook(bookId);
        User user = this.userService.getCurrentUser();
        if (!verifyBookAndUser(book, user) || !verifyId(user.getId(), userId)) {
            return "redirect:/login";
        }
        try {
            this.surveyService.saveSurvey(new Survey(title, mark, text, book, user));
            return "redirect:/user/books/" + bookId;
            
        } catch (Exception e) {
        	model.addAttribute("book", book);
        	
            model.addAttribute("errorMessage", "Errore nell'inserimento della recensione: " + e.toString());
            return "user/book.html";
        }
    }
    
    @GetMapping("/user/books/{bookId}/survey/{surveyId}/delete")
    public String deleteSurvey(@PathVariable("bookId") Long bookId,
    						@PathVariable("surveyId") Long surveyId,
                          Model model) {
        
        User user = this.userService.getCurrentUser();
        Survey survey = this.surveyService.getSurvey(surveyId);
        if (survey == null || !verifyId(survey.getUser().getId(), user.getId())) {
            return "redirect:/login";
        }
        try {
        	this.surveyService.deleteSurveyById(surveyId);
            return "redirect:/user/books/" + bookId;
            
        } catch (Exception e) {
        	model.addAttribute("book", bookService.getBook(bookId));        	
            model.addAttribute("errorMessage", "Errore nella cancellazione della recensione: " + e.toString());
            return "user/book.html";
        }
    }
	
    @GetMapping("/admin/survey/{surveyId}/delete")
    public String deleteSurveyForAdmin(@PathVariable("surveyId") Long surveyId,Model model) {
        
        User user = this.userService.getCurrentUser();
        Survey survey = this.surveyService.getSurvey(surveyId);
        Long bookId = survey.getBook().getId();
        if (!verifyAdmin(user)) {
            return "redirect:/login";
        }
        try {
            this.surveyService.deleteSurveyById(surveyId);
            return "redirect:/admin/books/" + bookId;
            
        } catch (Exception e) {
        	model.addAttribute("book", bookService.getBook(bookId));        	
            model.addAttribute("errorMessage", "Errore nella cancellazione della recensione: " + e.toString());
            return "admin/book.html";
        }
    }
}