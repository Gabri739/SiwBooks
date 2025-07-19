package it.uniroma3.siwbooks.authentication;

import static it.uniroma3.siwbooks.model.Credenziali.ADMIN_ROLE;
import static it.uniroma3.siwbooks.model.Credenziali.DEFAULT_ROLE;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import it.uniroma3.siwbooks.model.Credenziali;
import it.uniroma3.siwbooks.service.CredenzialiService;


@Configuration
@EnableWebSecurity
public class AuthConfiguration {

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	public CredenzialiService credenzialiService;

	//Questo metodo definisce le query SQL per ottenere username e password
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.authoritiesByUsernameQuery("SELECT username, ruolo as role FROM credenziali WHERE username = ?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credenziali WHERE username = ?");
	}

	//Questo metodo definisce il componente "passwordEncoder", usato per criptare e decriptare la password nella sorgente dati.
	
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
				// Consentiti a tutti (occasionali)
				.requestMatchers(HttpMethod.GET, "/", "/index", "/libri/**", "/autori/**",
				"/login", "/register", "/css/**", "/images/**", "favicon.ico").permitAll()
				.requestMatchers(HttpMethod.POST, "/register", "/login")
				.permitAll()

				// Solo ADMIN_ROLE
				.requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)

				// Solo DEFAULT_ROLE
				.requestMatchers("/user/**").hasAuthority(DEFAULT_ROLE)

				// Qualunque altra richiesta: autenticazione
				.anyRequest().authenticated().and().formLogin().loginPage("/login") // Pagina di login di default
																							// per tutti
				.loginProcessingUrl("/login") // URL di submit form login user
				.usernameParameter("username").passwordParameter("pwd")
				.successHandler((request, response, authentication) -> {
					// Success handler custom: redirect in base al ruolo dell'utente
					var principal = authentication.getPrincipal();
					// Recupero id utente dal Principal
					Long idUtente = null;
					String username = null;
					Credenziali credenziali = null;
					if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
						// Ottieni ID utente qui secondo la tua implementazione
						// Esempio: CredentialsService -> trova utente per username
						username = userDetails.getUsername();
						credenziali = this.credenzialiService.getCredenzialiByUsername(username);
						if (credenziali != null && credenziali.getUtente() != null) {
							idUtente = credenziali.getUtente().getId();
						}
					}
					boolean isAdmin = false;
					// Controllo ruolo dell'utente
					if (credenziali != null && credenziali.getRuolo() != null) {
						isAdmin = credenziali.getRuolo().equals(ADMIN_ROLE);
					}
					if (isAdmin) {
						// Se ADMIN, redirect operatore (sostituisci idUtente)
						response.sendRedirect(idUtente != null ? "/admin/" + idUtente : "/login");
					} else {
						// Se Utente, redirect utente (sostituisci idUtente)
						response.sendRedirect(idUtente != null ? "/user/" + idUtente : "/login");
					}
				}).failureUrl("/login?error=true").permitAll().and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).permitAll();
		return httpSecurity.build();
	}
}
