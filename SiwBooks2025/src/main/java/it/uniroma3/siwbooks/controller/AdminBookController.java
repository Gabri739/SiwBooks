package it.uniroma3.siwbooks.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.model.ImageEntity;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.service.AuthorService;
import it.uniroma3.siwbooks.service.BookService;
import it.uniroma3.siwbooks.service.CredentialsService;
import it.uniroma3.siwbooks.service.ImageEntityService;
import it.uniroma3.siwbooks.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CredentialsService credentialsService;
    
    @Autowired
    private ImageEntityService imageEntityService;
    
    @Autowired
    private AuthorService authorService;
    
    private boolean verifyAuthorAndBook(Long idAuthor, Long idBook) {
        return this.bookService.existsBook(idBook) && this.authorService.existsAuthor(idAuthor);
    }
    
    private boolean verifyAdmin(User admin) {
        return credentialsService.isAdminCurrent(admin);
    }
    
    
    private boolean verifyBookAndUser(Book book, User user) {
        return book != null && user != null;
    }
    
    
    @GetMapping("")
    public String showBooksForAdmin(Model model) {
        User user = userService.getCurrentUser(); 
        if(!this.verifyAdmin(user))
            return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("books", this.bookService.getBooks());
        return "admin/books.html";
    }
    
    @GetMapping("/new")
    public String getFormNewBook(Model model) {
        User user = this.userService.getCurrentUser();
        if(!verifyAdmin(user))
            return "redirect:/login";
        
        model.addAttribute("user", user);
        model.addAttribute("authors", authorService.getAuthors());
        model.addAttribute("book", new Book());
        return "admin/formNewBook.html";
    }
    
    
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book, 
                          BindingResult bindingResult,
                          @RequestParam("imageFiles") MultipartFile[] imageFiles, 
                          Model model) {
        
        User user = userService.getCurrentUser();
        if (!verifyAdmin(user))
            return "redirect:/login";
            
        
        model.addAttribute("authors", authorService.getAuthors());

        // Validazione
        if (bindingResult.hasErrors()) {
            model.addAttribute("msgError", "Alcuni campi non sono validi");
            return "admin/formNewBook.html";
        }
        
        if(imageFiles.length == 0) {
            model.addAttribute("msgError", "Inserire almeno una foto");
            return "admin/formNewBook.html";
        }

        try {
            // Gestione immagini
            Set<ImageEntity> images = new HashSet<ImageEntity>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    ImageEntity img = new ImageEntity(name);
                    this.imageEntityService.savePhysicalImage(file.getBytes(), name);
                    images.add(img);
                }
            }
            book.setImages(images);
            
            bookService.saveBook(book);
            return "redirect:/admin/books";
            
        } catch (Exception e) {
            model.addAttribute("msgError", "Errore nel salvataggio del libro:\n"+ e.toString());
            return "admin/formNewBook.html";
        }
    }

    /**
     * Gestisce la visualizzazione dettagli libro
     */
    @GetMapping("/{id}")
    public String showBookForAdmin(@PathVariable("id") Long id,
            @RequestParam(value = "showModalAddPhoto", required = false) boolean showModalAddPhoto,
            @RequestParam(value = "showModalEditBook", required = false) boolean showModalEditBook,
            Model model) {
        Book book = this.bookService.getBook(id);
        User user = userService.getCurrentUser(); 
        if (!verifyBookAndUser(book, user) || !this.verifyAdmin(user)) {
            return "redirect:/login";
        }
        
        model.addAttribute("book", book);
        model.addAttribute("showModalAddPhoto", showModalAddPhoto);
        model.addAttribute("showModalEditBook", showModalEditBook);
        model.addAttribute("allAuthors", this.authorService.getAuthors());
        model.addAttribute("user", user);
        return "admin/book.html";
    }
    
    @PostMapping("/{id}/edit")
    public String editBook(@PathVariable("id") Long id, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "yearPubblication", required = false) Integer yearPubblication) {
        if (!verifyAdmin(userService.getCurrentUser()) || !this.bookService.existsBook(id))
            return "redirect:/login";
        Book book = this.bookService.getBook(id);
        if(book == null)
            return "error.html";
        if(title != null && !title.isEmpty())
            book.setTitle(title);
        if(yearPubblication != null)
            book.setYearPubblication(yearPubblication);
        if((title != null && !title.isEmpty()) || yearPubblication != null)
            this.bookService.saveBook(book);
        return "redirect:/admin/books/" + id;
    }
    
    @GetMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id) {
        Book book = this.bookService.getBook(id);
        if(book == null || !verifyAdmin(userService.getCurrentUser()))
            return "redirect:/admin/books";
        for(ImageEntity img : book.getImages())
            if(img != null)
                this.imageEntityService.deletePhysicalImage(img);
        this.bookService.deleteBook(book);
        return "redirect:/admin/books";
    }
    
    @GetMapping("/{id}/authors/add")
    public String showAddingAuthors(@PathVariable("id") Long id, Model model) {
        Book book = this.bookService.getBook(id);
        User user = userService.getCurrentUser(); 
        if (!verifyBookAndUser(book, user) || !this.verifyAdmin(user)) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("book", book);
        model.addAttribute("authors", this.authorService.getAvaibleAuthors(id));
        return "admin/addAuthorToBook";
    }
    
    
    @GetMapping("/{idBook}/authors/{idAuthor}/add")
    public String addBookToAuthor(@PathVariable("idAuthor") Long idAuthor, @PathVariable("idBook") Long idBook) {
        if (!verifyAuthorAndBook(idAuthor, idBook))
            return "error.html";
        User user = userService.getCurrentUser();
        if (!verifyAdmin(user))
            return "redirect:/login";
        this.bookService.addBookToAuthor(idBook, idAuthor);
        return "redirect:/admin/books/" + idBook;
    }
    
    @GetMapping("/{id}/authors/delete")
    public String showRemovingAuthors(@PathVariable("id") Long id, Model model) {
        Book book = this.bookService.getBook(id);
        User user = userService.getCurrentUser(); 
        if (!verifyBookAndUser(book, user) || !this.verifyAdmin(user)) {
            return "redirect:/login";
        }
        
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "admin/removeAuthorToBook";
    }
    
    @GetMapping("/{idBook}/authors/{idAuthor}/delete")
    public String deleteAuthorFromBook(@PathVariable("idAuthor") Long idAuthor, @PathVariable("idBook") Long idBook) {
        if (!verifyAuthorAndBook(idAuthor, idBook))
            return "error.html";
        User user = userService.getCurrentUser();
        if (!verifyAdmin(user))
            return "redirect:/login";
        this.bookService.removeAuthorFromBook(idAuthor, idBook);
        return "redirect:/admin/books/" + idBook;
    }
    
}