package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siwbooks.model.Libro;
import it.uniroma3.siwbooks.model.Recensione;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.service.CredenzialiService;
import it.uniroma3.siwbooks.service.LibroService;
import it.uniroma3.siwbooks.service.RecensioneService;
import it.uniroma3.siwbooks.service.UtenteService;

@Controller
public class LibroController {
    @Autowired
	private LibroService libroService;
	
	@Autowired
	private RecensioneService recensioneService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CredenzialiService credenzialiService;;
	
	private boolean verificaLibroeUtente(Libro libro, Utente utente) {
		return utente != null && utente != null;
	}
	
	private boolean verificaAdmin(Utente admin) {
		return credenzialiService.isAdminCorrente(admin);
	}
	
	@GetMapping("/libri")
	public String showLibri(Model model) {
		model.addAttribute("libri", this.libroService.getLibri());
		return "libri.html";
	}
	
	@GetMapping("/libri/{id}")
	public String showLibro(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book", this.libroService.getLibro(id));
		return "libro.html";
	}
	
	@GetMapping("/utente/libri")
	public String showLibriPerUtente(Model model) {
		Utente utente = utenteService.getUtenteCorrente(); 
		if(utente == null)
			return "redirect:/login";
		model.addAttribute("utente", utente);
		model.addAttribute("libri", this.libroService.getLibri());
		return "utente/libri.html";
	}
    
    @GetMapping("/utente/libri/{id}")
    public String showLibroPerUtente(@PathVariable("id") Long id, Model model) {
        Libro libro = this.libroService.getLibro(id);
        Utente utente = utenteService.getUtenteCorrente(); 
        if (!verificaLibroeUtente(libro, utente)) {
            return "redirect:/login";
        }
        
        // Verifica se l'utente corrente ha già recensito questo libro
        Recensione recensioneUtente = this.recensioneService.findByUtenterAndLibro(utente, libro);
        model.addAttribute("utente", utente);
        model.addAttribute("libro", libro);
        model.addAttribute("recensioneUtente", recensioneUtente);
        
        return "utente/libro.html";
    }

    @GetMapping("/admin/libri")
	public String showLibriPerAdmin(Model model) {
		Utente utente = utenteService.getUtenteCorrente(); 
		if(!this.verificaAdmin(utente))
			return "redirect:/login";
		model.addAttribute("utente", utente);
		model.addAttribute("libri", this.libroService.getLibri());
		return "admin/libri.html";
	}

    @GetMapping("/admin/libri/{id}")
    public String showLibroPerAdmin(@PathVariable("id") Long id, Model model) {
        Libro libro = this.libroService.getLibro(id);
        Utente utente = utenteService.getUtenteCorrente(); 
        if (!verificaLibroeUtente(libro, utente) || !this.verificaAdmin(utente)) {
            return "redirect:/login";
        }
        
        // Verifica se l'utente corrente ha già recensito questo libro
        Recensione recensione = this.recensioneService.findByUtenterAndLibro(utente, libro);
        model.addAttribute("utente", utente);
        model.addAttribute("libro", libro);
        model.addAttribute("recensione", recensione);
        
        return "admin/libro.html";
    }
	
}
