package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.service.CredenzialiService;
import jakarta.validation.Valid;


@Controller
public class MainController {

	@Autowired
	private CredenzialiService credenzialiService;
	
	
	@GetMapping("/")
	public String homepage() {
		return "index.html";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "register.html";
	}
	
	@PostMapping("/register")
	public String registerUtente(@Valid @ModelAttribute("user") Utente user,BindingResult utenteBindingResult,@Valid @ModelAttribute("credenziali") Credenziali credenziali,
	                             @Valid @RequestParam(name = "confirmPwd") String confermaPwd,
	                             BindingResult credentialsBindingResult,
	                             Model model) {
	        
	        // Validation check for both objects
	        if (utenteBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
	            model.addAttribute("msgError", "Errore nella validazione dei dati");
	            return "register.html";
	        }
	        // Check if username already exists
	        if (credenzialiService.existsByUsername(credenziali.getUsername())) {
	            model.addAttribute("msgError", "Username già in uso");
	            return "register.html";
	        }
	        if(!credenziali.getPassword().equals(confermaPwd)) {
	        	model.addAttribute("msgError", "Le 2 password scritte non sono uguali");
	            return "register.html";
	        }
	        try {
	            
	            // Link utente to credentials
	            credenziali.setUtente(user);
	            
	            // Save credentials
	            credenzialiService.saveCredenziali(credenziali);
	            
	            model.addAttribute("msgSuccess", "Registrazione completata con successo");
	            return "redirect:/login";
	            
	        } catch (Exception e) {
	            model.addAttribute("msgError", "Errore durante la registrazione");
	            return "register.html";
	        }
	}

	@ModelAttribute("utenteinfo")
	public UserDetails getUtente() {
	    UserDetails utente = null;
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      utente = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    }
	    return utente;
	  }
	
}
