package it.uniroma3.siwbooks.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String titolo;
	
	private int voto;
	
	private String testoRecensione;
	
	@ManyToOne
	private Utente utente;
	
	@ManyToOne
	private Libro libro;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public int getVoto() {
		return voto;
	}

	public void setVoto(int voto) {
		this.voto = voto;
	}

	public String getTestoRecensione() {
		return testoRecensione;
	}

	public void setTestoRecensione(String testoRecensione) {
		this.testoRecensione = testoRecensione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, libro, testoRecensione, titolo, utente, voto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return Objects.equals(id, other.id) && Objects.equals(libro, other.libro)
				&& Objects.equals(testoRecensione, other.testoRecensione) && Objects.equals(titolo, other.titolo)
				&& Objects.equals(utente, other.utente) && voto == other.voto;
	}
	
	

}
