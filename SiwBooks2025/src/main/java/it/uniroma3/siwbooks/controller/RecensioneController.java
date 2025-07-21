package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwbooks.model.Libro;
import it.uniroma3.siwbooks.model.Recensione;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.service.CredenzialiService;
import it.uniroma3.siwbooks.service.LibroService;
import it.uniroma3.siwbooks.service.RecensioneService;
import it.uniroma3.siwbooks.service.UtenteService;

@Controller
public class RecensioneController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private RecensioneService recensioneService;


    @Autowired
    private CredenzialiService credenzialiService;

    private boolean verificaLibroeUtente(Libro libro, Utente utente) {
		return utente != null && utente != null;
	}
	
	private boolean verificaId(Long idUrl, Long idUtente) {
		return idUtente!= null && idUrl == idUtente;
	}

    	
	private boolean verificaAdmin(Utente admin) {
		return credenzialiService.isAdminCorrente(admin);
	}


    @PostMapping("/utente/{utenteId}/libri/{libroId}/recensione")
    public String aggiungiRecensione(@PathVariable("utenteId") Long utenteId, @PathVariable("libroId") Long libroId, @RequestParam("titolo") String titolo,
                          @RequestParam("testo") String testo,
                          @RequestParam("voto") Integer voto,
                          Model model) {
        
        Libro libro = this.libroService.getLibro(libroId);
        Utente utente = this.utenteService.getUtenteCorrente();
        if (!verificaLibroeUtente(libro, utente) || !verificaId(utente.getId(), utenteId)) {
            return "redirect:/login";
        }
        try {
            this.recensioneService.saveRecensione((new Recensione(titolo, voto, testo, utente, libro)));
            return "redirect:/utente/libri/" + libroId;
            
        } catch (Exception e) {
        	model.addAttribute("libro", libro);
        	model.addAttribute("utente", utente);
            model.addAttribute("errorMessage", "Errore nell'inserimento della recensione: " + e.toString());
            return "utente/libro.html";
        }
    }
    
    @PostMapping("/utente/libri/{libroId}/recensione/{recensioneId}/cancella")
    public String cancellaRencensione(@PathVariable("libroId") Long libroId,
    						@PathVariable("recensioneId") Long recensioneId,
                          Model model) {
        
        Libro libro = this.libroService.getLibro(libroId);
        Utente utente = this.utenteService.getUtenteCorrente();
        Recensione recensione = this.recensioneService.getRecensione(recensioneId);
        if (libro == null || recensione == null || !recensione.getUtente().equals(utente)) {
            return "redirect:/login";
        }
        try {
        	libro.getRecensioni().remove(recensione);
            this.recensioneService.deleteRecensione(recensione);
            this.libroService.saveLibro(libro);
            return "redirect:/utente/libri/" + libroId;
            
        } catch (Exception e) {
        	model.addAttribute("libro", libro);
        	model.addAttribute("utente", utente);
            model.addAttribute("errorMessage", "Errore nella cancellazione della recensione: " + e.toString());
            return "utente/libro.html";
        }
    }

    @PostMapping("/admin/libri/{libroId}/recensione/{recensioneId}/cancella")
    public String cancellaRecensionePerAdmin(@PathVariable("libroId") Long libroId,
    						@PathVariable("recensioneId") Long recensioneId,
                          Model model) {
        
        Libro libro = this.libroService.getLibro(libroId);
        Utente utente = this.utenteService.getUtenteCorrente();
        Recensione recensione = this.recensioneService.getRecensione(recensioneId);
        if (libro == null || recensione == null || !verificaAdmin(utente)) {
            return "redirect:/login";
        }
        try {
        	libro.getRecensioni().remove(recensione);
            this.recensioneService.deleteRecensione(recensione);
            this.libroService.saveLibro(libro);
            return "redirect:/admin/libri/" + libroId;
            
        } catch (Exception e) {
        	model.addAttribute("libro", libro);
        	model.addAttribute("utente", utente);
            model.addAttribute("errorMessage", "Errore nella cancellazione della recensione: " + e.toString());
            return "admin/libro.html";
        }
    }
}
