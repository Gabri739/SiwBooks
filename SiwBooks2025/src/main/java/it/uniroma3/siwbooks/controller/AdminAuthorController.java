package it.uniroma3.siwbooks.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwbooks.model.Author;
import it.uniroma3.siwbooks.model.ImageEntity;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.service.AuthorService;
import it.uniroma3.siwbooks.service.BookService;
import it.uniroma3.siwbooks.service.CredentialsService;
import it.uniroma3.siwbooks.service.ImageEntityService;
import it.uniroma3.siwbooks.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AdminAuthorController {
	@Autowired
	public AuthorService authorService;
	@Autowired
	public UserService userService;
	@Autowired
	public CredentialsService credentialsService;
	@Autowired
	public BookService bookService;
	@Autowired
	public ImageEntityService imageEntityService;
	
	private boolean verifyAdmin(User admin) {
		return credentialsService.isAdminCurrent(admin);
	}

	private boolean verifyAuthorAndBook(Long idAuthor, Long idBook) {
		return this.bookService.existsBook(idBook) && this.authorService.existsAuthor(idAuthor);
	}
	
	private void addAttributeNewAuthor(Model model, User user) {
		model.addAttribute("user", user);
		model.addAttribute("author", new Author());
	}
	
	private void addAttributeAuthorPage(Long id, Author author, Model model) {
		model.addAttribute("user", this.userService.getCurrentUser());
		model.addAttribute("author", author);
		model.addAttribute("availableBooks", bookService.getAvailableBooks(id));
	}

	@GetMapping("/admin/authors")
	public String showAuthorsForAdmin(Model model) {
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		model.addAttribute("authors", this.authorService.getAuthors());
		model.addAttribute("user", user);
		return "admin/authors.html";
	}

	@PostMapping("/admin/authors/save")
	public String saveAuthor(@Valid @ModelAttribute("author") Author author, BindingResult bindingResult,
			@RequestParam(required = true) MultipartFile photoFile, Model model) {
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		model.addAttribute("user", user);
		if (author.getDateDeath() != null && author.getDateBirth() != null) {
			if (author.getDateDeath().isBefore(author.getDateBirth())) {
				bindingResult.rejectValue("dateDeath", "error.dateDeath",
						"La data di morte non può essere precedente alla data di nascita");
			}
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "Errore con i validations");
			return "admin/formNewAuthor.html";
		}

		try {
			if (photoFile != null && !photoFile.isEmpty()) {
				if (!photoFile.getContentType().startsWith("image/")) {
					model.addAttribute("errorMessage", "Per favore carica solo file immagine");
					return "admin/formNewAuthor.html";
				}
				String name = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
				ImageEntity photo = new ImageEntity(name);
				this.imageEntityService.savePhysicalImage(photoFile.getBytes(), name);
				author.setPhoto(photo);
				authorService.saveAuthor(author);
				return "redirect:/admin/authors";
			}
			model.addAttribute("errorMessage", "Per favore carica l'immagine");
			addAttributeNewAuthor(model, user);
			return "admin/formNewAuthor.html";

		} catch (Exception e) {
			addAttributeNewAuthor(model, user);
			model.addAttribute("errorMessage",
					"Si è verificato un errore durante il salvataggio dell'autore: " + e.getMessage());
			return "admin/formNewAuthor.html";
		}
	}

	@GetMapping("/admin/authors/{id}")
	public String showAuthorForAdmin(@PathVariable("id") Long id, Model model) {
		Author author = this.authorService.getAuthor(id);
		if (author == null)
			return "error.html";
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		model.addAttribute("user", user);
		model.addAttribute("author", author);
		model.addAttribute("availableBooks", bookService.getAvailableBooks(id));
		return "admin/author.html";
	}

	@GetMapping("/admin/authors/new")
	public String showFormNewAuthor(Model model) {
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		addAttributeNewAuthor(model, user);
		return "admin/formNewAuthor.html";
	}

	@GetMapping("/admin/authors/{id}/delete")
	public String deleteAuthor(@PathVariable("id") Long id, Model model) {
		Author author = this.authorService.getAuthor(id);
		if (author == null)
			return "error.html";
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		this.imageEntityService.deletePhysicalImage(author.getPhoto());
		this.authorService.deleteAuthor(author);
		return "redirect:/admin/authors";
	}

	@GetMapping("/admin/authors/{idAuthor}/book/{idBook}/remove")
	public String removeBookFromAuthor(@PathVariable("idAuthor") Long idAuthor, @PathVariable("idBook") Long idBook,
			Model model) {
		if (!verifyAuthorAndBook(idAuthor, idBook))
			return "error.html";
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		this.bookService.removeAuthorFromBook(idAuthor, idBook);
		return "redirect:/admin/authors/" + idAuthor;
	}

	@PostMapping("/admin/authors/{id}/book/add")
	public String addBookToAuthor(@PathVariable("id") Long idAuthor, @RequestParam("bookId") Long idBook) {
		if (!verifyAuthorAndBook(idAuthor, idBook))
			return "error.html";
		User user = userService.getCurrentUser();
		if (!verifyAdmin(user))
			return "redirect:/login";
		this.bookService.addBookToAuthor(idBook, idAuthor);
		return "redirect:/admin/authors/" + idAuthor;
	}

	@PostMapping("/admin/authors/{id}/edit")
	public String editAuthor(
	        @PathVariable("id") Long id,
	        @Valid @ModelAttribute Author authorModified,
	        @RequestParam(required = false) MultipartFile photoFile,
	        BindingResult bindingResult,
	        Model model) {

	    // 1. Verifica se l'autore esiste
	    Author existingAuthor = authorService.getAuthor(id);
	    if (existingAuthor == null) {
	    	return "error.html";
	    }

	    // 2. Validazione form
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("errorMessage", "Errore nei dati inseriti");
	        addAttributeAuthorPage(id, authorModified, model);
	        return "redirect:/admin/authors";
	    }

	    // 3. Validazione date
	    if (authorModified.getDateDeath() != null && 
	        authorModified.getDateBirth() != null && 
	        authorModified.getDateDeath().isBefore(authorModified.getDateBirth())) {
	        model.addAttribute("errorMessage", "La data di morte non può essere precedente alla data di nascita");
	        addAttributeAuthorPage(id, authorModified, model);
	        return "redirect:/admin/authors";
	    }

	    try {
	        // 4. Gestione foto se presente
	        if (photoFile != null && !photoFile.isEmpty()) {
	            if (!photoFile.getContentType().startsWith("image/")) {
	                model.addAttribute("errorMessage", "Il file caricato non è un'immagine valida");
	                return "redirect:/admin/authors/" + authorModified.getId();
	            }
	            
	            String name = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
				ImageEntity photo = new ImageEntity(name);
				this.imageEntityService.savePhysicalImage(photoFile.getBytes(), name);
				this.imageEntityService.deletePhysicalImage(this.authorService.getAuthor(id).getPhoto());
				authorModified.setPhoto(photo);
				authorService.saveAuthor(authorModified);
	        } else {
	            // Mantieni la foto esistente se non ne è stata caricata una nuova
	            authorModified.setPhoto(existingAuthor.getPhoto());
	        }

	        // 5. Mantieni i libri esistenti
	        authorModified.setBooks(existingAuthor.getBooks());
	        
	        // 6. Salva le modifiche
	        authorService.saveAuthor(authorModified);
	        
	        return "redirect:/admin/authors/" + id;

	    } catch (Exception e) {
	        return "error.html";
	    }
	}
	
}