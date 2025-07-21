package it.uniroma3.siwbooks.controller;

import static it.uniroma3.siwbooks.model.Credenziali.ADMIN_ROLE;

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
@RequestMapping("/admin")
public class AdminController {
    
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CredenzialiService credenzialiService;
	
	private boolean verificaAdmin(Long id, Utente utente) {
		return utente!= null && id == utente.getId() && this.credenzialiService.getCredenzialiByUtente(utente).getRuolo().equals(ADMIN_ROLE);
	}
	
		
	@GetMapping("/{id}/modificaUtente")
	public String showFormModificaInfo(@PathVariable("id") Long id, Model model) {
		if (!verificaAdmin(id, utenteService.getUtenteCorrente()))
			return "redirect:/login";
		model.addAttribute("utente", utenteService.findById(id));
		return "admin/formModificaUtente.html";
	}
	
	
	@PostMapping("/{id}/ModificaUtente")
	public String updateInfo(@PathVariable("id") Long id, @ModelAttribute @Valid Utente utente, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors())
			return "admin/formModificaUtente.html";
		if (!verificaAdmin(id, utenteService.getUtenteCorrente()))
			return "redirect:/login";
		this.utenteService.saveUtente(utente);
		return "redirect:/admin/" + utente.getId();
	}


	@GetMapping("/{id}")
	public String getMethodName(@PathVariable("id") Long id, 
			@RequestParam(value = "showPasswordModal", required = false, defaultValue = "false") boolean showPasswordModal,
			Model model) {
		Utente utente = utenteService.getUtenteCorrente();
		if(!verificaAdmin(id, utente))
			return "redirect:/login";
		model.addAttribute("utente", utente);
		model.addAttribute("showPasswordModal", showPasswordModal);
		return "admin/profilo.html";
	}

	
	@PostMapping("/{id}/cambiaPassword")
	public String updateCredenziali(@PathVariable("id") Long id, @RequestParam @Valid String confirmPwd, @RequestParam @Valid String newPwd, Model model) {
		if(newPwd == null || confirmPwd == null || newPwd.equals("") || confirmPwd.equals("") || !newPwd.equals(confirmPwd)) {
			model.addAttribute("msgError", "Il campo della nuova password è vuota");
			return "admin/profilo.html";
		}
		Utente utente = utenteService.getUtenteCorrente();
		if (!verificaAdmin(id, utente))
			return "redirect:/login";
		Credenziali credenziali = this.credenzialiService.getCredenzialiByUtente(utente);
		this.credenzialiService.saveCredenziali(credenziali);
		return "redirect:/admin/" + utente.getId();
	}
}
