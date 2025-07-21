package it.uniroma3.siwbooks.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

import java.util.*;

@Entity
public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	private String titolo;
	
	@OneToOne
	private Immagine copertina;
	
	private int annodiPubblicazione;

	//molti autori hanno scritto molti libri
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Autore> autori;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Immagine> immagini;
	
	
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Recensione> recensioni;
	
	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

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

	public Immagine getCopertina() {
		return copertina;
	}

	public void setCopertina(Immagine copertina) {
		this.copertina = copertina;
	}

	public int getAnnodiPubblicazione() {
		return annodiPubblicazione;
	}

	public void setAnnodiPubblicazione(int annodiPubblicazione) {
		this.annodiPubblicazione = annodiPubblicazione;
	}

	public Set<Immagine> getImmagini() {
		return immagini;
	}

	public void setImmagini(Set<Immagine> immagini) {
		this.immagini = immagini;
	}

	public Set<Autore> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}

	@Override
	public int hashCode() {
		return Objects.hash(annodiPubblicazione, autori, id, immagini, recensioni, titolo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return annodiPubblicazione == other.annodiPubblicazione && Objects.equals(autori, other.autori)
				&& Objects.equals(id, other.id) && Objects.equals(immagini, other.immagini)
				&& Objects.equals(recensioni, other.recensioni) && Objects.equals(titolo, other.titolo);
	}
	
	 
}
