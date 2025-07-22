package it.uniroma3.siwbooks.service;

import static it.uniroma3.siwbooks.model.Credentials.DEFAULT_ROLE;
import static it.uniroma3.siwbooks.model.Credentials.ADMIN_ROLE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siwbooks.model.Credentials;
import it.uniroma3.siwbooks.model.User;
import it.uniroma3.siwbooks.repository.CredentialsRepository;
import jakarta.validation.Valid;

@Service
public class CredentialsService {

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Credentials getCredentialsByUsername(String username) {
		return this.credentialsRepository.findByUsername(username).orElse(null);
	}

	public boolean existsByUsername(String username) {
		return this.credentialsRepository.existsByUsername(username);
	}

	public void saveCredentials(@Valid Credentials credentials) {
		credentials.setRole(DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		this.credentialsRepository.save(credentials);

	}

	public Credentials getCredentialsByUser(User user) {
		return this.credentialsRepository.findByUser(user).orElse(null);
	}

	public boolean isAdminCurrent(User admin) {
		try {
			return this.getCredentialsByUser(admin).getRole().equals(ADMIN_ROLE);
		} catch (Exception e) {
			return false;
		}
	}

	public User getCurrentUser() {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.getCredentialsByUsername(userDetails.getUsername()).getUser();
	}
}