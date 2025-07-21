package it.uniroma3.siwbooks.controller;

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


import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.model.Utente;
import it.uniroma3.siwbooks.service.CredenzialiService;
import it.uniroma3.siwbooks.service.UtenteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CredenzialiService credenzialiService;
	
    // Metodo per verificare se l'utente corrente è lo stesso di quello passato come parametro
	private boolean verificaId(Long idUrl, Long idUtente) {
		return idUtente!= null && idUrl == idUtente;
	}

    	
	@GetMapping("/{id}")
	public String showProfiloCorrente(@PathVariable("id") Long id, 
			@RequestParam(value = "showPasswordModal", required = false, defaultValue = "false") boolean showPasswordModal,
			Model model) {
		Utente utente = utenteService.getUtenteCorrente();
		if(!verificaId(id, utente.getId()))
			return "redirect:/login";
		model.addAttribute("utente", utente);
		model.addAttribute("showPasswordModal", showPasswordModal);
		return "utente/profilo.html";
	}
	
	@PostMapping("/{id}/cambiaPassword")
	public String aggiornaCredenziali(@PathVariable("id") Long id, @RequestParam @Valid String confirmPwd, @RequestParam @Valid String newPwd, Model model) {
		if(newPwd == null || confirmPwd == null || newPwd.equals("") || confirmPwd.equals("") || !newPwd.equals(confirmPwd)) {
			model.addAttribute("msgError", "Il campo della nuova password è vuota");
			return "utente/profilo.html";
		}
		Utente utente = utenteService.getUtenteCorrente();
		if (!verificaId(id, utente.getId()))
			return "redirect:/login";
		Credenziali credenziali = this.credenzialiService.getCredenzialiByUtente(utente);
		credenziali.setPassword(newPwd);
		this.credenzialiService.saveCredenziali(credenziali);
		return "redirect:/utente/" + utente.getId();
	}

    	
	@GetMapping("/{id}/formModificaUtente")
	public String showformModificaUtente(@PathVariable("id") Long id, Model model) {
		if (!verificaId(id, utenteService.getUtenteCorrente().getId()))
			return "redirect:/login";
		model.addAttribute("utente", utenteService.findById(id));
		return "utente/formModificaUtente.html";
	}
	
	@PostMapping("/{id}/modificaUtente")
	public String updateInfo(@PathVariable("id") Long id, @ModelAttribute @Valid Utente utente, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors())
			return "utente/formModificaUtente.html";
		if (!verificaId(id, utenteService.getUtenteCorrente().getId()))
			return "redirect:/login";
		this.utenteService.saveUtente(utente);
		return "redirect:/utente/" + utente.getId();
	}
	
	
}
