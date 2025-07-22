package it.uniroma3.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siwbooks.model.Credentials;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.service.CredentialsService;
import it.uniroma3.siwbooks.service.UserService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	private boolean verifyId(Long idUrl, Long idUser) {
		return idUser!= null && idUrl == idUser;
	}
	
	@GetMapping("/{id}")
	public String getMethodName(@PathVariable("id") Long id, 
			@RequestParam(value = "showPasswordModal", required = false, defaultValue = "false") boolean showPasswordModal,
			Model model) {
		User user = userService.getCurrentUser();
		if(!verifyId(id, user.getId()))
			return "redirect:/login";
		model.addAttribute("user", user);
		model.addAttribute("showPasswordModal", showPasswordModal);
		return "user/profile.html";
	}
	
	@GetMapping("/{id}/modifyUser")
	public String showFormUpdateInfo(@PathVariable("id") Long id, Model model) {
		if (!verifyId(id, userService.getCurrentUser().getId()))
			return "redirect:/login";
		model.addAttribute("user", userService.findById(id));
		return "user/formModifyUser.html";
	}
	
	@PostMapping("/{id}/modifyUser")
	public String updateInfo(@PathVariable("id") Long id, @ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors())
			return "user/formModifyUser.html";
		if (!verifyId(id, userService.getCurrentUser().getId()))
			return "redirect:/login";
		this.userService.saveUser(user);
		return "redirect:/user/" + user.getId();
	}
	
	@PostMapping("/{id}/changePassword")
	public String updateCredentials(@PathVariable("id") Long id, @RequestParam @Valid String confirmPwd, @RequestParam @Valid String newPwd, Model model) {
		if(newPwd == null || confirmPwd == null || newPwd.equals("") || confirmPwd.equals("") || !newPwd.equals(confirmPwd)) {
			User user = userService.getCurrentUser();
			model.addAttribute("user", user);
			model.addAttribute("showPasswordModal", true);
			model.addAttribute("msgError", "La password è vuota o non coincide.");
			return "user/profile.html";
		}
		User user = userService.getCurrentUser();
		if (!verifyId(id, user.getId()))
			return "redirect:/login";
		Credentials credentials = this.credentialsService.getCredentialsByUser(user);
		credentials.setPassword(newPwd);
		this.credentialsService.saveCredentials(credentials);
		return "redirect:/user/" + user.getId();
	}
	
}
