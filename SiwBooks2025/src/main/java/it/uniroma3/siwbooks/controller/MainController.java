package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwbooks.model.Credentials;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.service.CredentialsService;
import it.uniroma3.siwbooks.service.UserService;
import jakarta.validation.Valid;


@Controller
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	
	
	@GetMapping("/")
	public String getHome(Model model, java.security.Principal principal) {
		boolean isAdmin = false;
		Long userId = null;
		if (principal != null) {
			Credentials credentials = credentialsService.getCredentialsByUsername(principal.getName());
			if (credentials != null && credentials.getUser() != null) {
				userId = credentials.getUser().getId();
				isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);
			}
		}
		model.addAttribute("userId", userId);
		model.addAttribute("admin", isAdmin);
		return "index.html";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register.html";
	}
	
	 @PostMapping("/register")
		public String registerUser(@Valid @ModelAttribute("user") User user,
								 BindingResult utenteBindingResult,
								 @Valid @ModelAttribute("credentials") Credentials credentials,
								 @Valid @RequestParam(name = "confirmPwd") String confermaPwd,
								 BindingResult credentialsBindingResult,
								 Model model) {
			
			// Validation check for both objects
			if (utenteBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
				model.addAttribute("msgError", "Errore nella validazione dei dati");
				return "register.html";
			}
			// Check if username already exists
			if (credentialsService.existsByUsername(credentials.getUsername())) {
				model.addAttribute("msgError", "Username già in uso");
				return "register.html";
			}
			if(!credentials.getPassword().equals(confermaPwd)) {
				model.addAttribute("msgError", "Le 2 password scritte non sono uguali");
				return "register.html";
			}
			try {
				
				// Link utente to credentials
				credentials.setUser(user);
				// Il ruolo di default viene ora impostato dal costruttore di Credentials
				credentialsService.saveCredentials(credentials);
				
				model.addAttribute("msgSuccess", "Registrazione completata con successo");
				return "redirect:/login";
				
			} catch (Exception e) {
				model.addAttribute("msgError", "Errore durante la registrazione");
				return "register.html";
			}
		}
	
}
