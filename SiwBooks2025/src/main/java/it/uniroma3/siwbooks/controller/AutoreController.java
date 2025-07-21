package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siwbooks.model.Autore;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.service.AutoreService;
import it.uniroma3.siwbooks.service.CredenzialiService;
import it.uniroma3.siwbooks.service.UtenteService;

@Controller
public class AutoreController {
    
	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CredenzialiService credenzialiService;
	
	private boolean verificaAdmin(Utente admin) {
		return credenzialiService.isAdminCorrente(admin);
	}
		
	@GetMapping("/autori/{id}")
	public String showAutore(@PathVariable("id") Long id, Model model) {
		Autore autore = this.autoreService.getAutore(id);
		if(autore == null)
			return "redirect:/autori";
		model.addAttribute("autore", autore);
		return "autore.html";
	}
	
	@GetMapping("/autori")
	public String showAutori(Model model) {
		model.addAttribute("autori", this.autoreService.getAutori());
		return "autori.html";
	}

	@GetMapping("/utente/autori")
	public String showAutoriPerUtente(Model model) {
		Utente utente = utenteService.getUtenteCorrente();
		model.addAttribute("autori", this.autoreService.getAutori());
		model.addAttribute("utente", utente);
		return "utente/autori.html";
	}
	
	@GetMapping("/utente/autori/{id}")
	public String showAutorePerUtente(@PathVariable("id") Long id, Model model) {
		Autore autore = this.autoreService.getAutore(id);
		if(autore == null)
			return "redirect:/utente/autori";
		Utente utente = utenteService.getUtenteCorrente();
		model.addAttribute("utente", utente);
		model.addAttribute("autore", autore);
		return "utente/autore.html";
	}
	
	@GetMapping("/admin/autori")
	public String showAutoriPerAdmin(Model model) {
		Utente utente = utenteService.getUtenteCorrente();
		if(!verificaAdmin(utente))
			return "redirect:/login";
		model.addAttribute("autori", this.autoreService.getAutori());
		model.addAttribute("utente", utente);
		return "admin/autori.html";
	}
	
	@GetMapping("/admin/autori/{id}")
	public String showAutorePerAdmin(@PathVariable("id") Long id, Model model) {
		Autore autore = this.autoreService.getAutore(id);
		if(autore == null)
			return "redirect:/utente/autori";
		Utente utente = utenteService.getUtenteCorrente();
		if(!verificaAdmin(utente))
			return "redirect:/login";
		model.addAttribute("utente", utente);
		model.addAttribute("autore", autore);
		return "admin/autore.html";
	}
}
