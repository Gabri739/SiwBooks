package it.uniroma3.siwbooks.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Autore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	private String cognome;
	
	private LocalDate AnnoNascita;
	
	private LocalDate AnnoMorte;
	
	private String nazionalita;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Immagine immagine;
	
	@ManyToMany(mappedBy =  "autori")
	private List<Libro> libri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getAnnoNascita() {
		return AnnoNascita;
	}

	public void setAnnoNascita(LocalDate annoNascita) {
		AnnoNascita = annoNascita;
	}

	public LocalDate getAnnoMorte() {
		return AnnoMorte;
	}

	public void setAnnoMorte(LocalDate annoMorte) {
		AnnoMorte = annoMorte;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public Immagine getImmagine() {
		return immagine;
	}

	public void setImmagine(Immagine immagine) {
		this.immagine = immagine;
	}

	public List<Libro> getLibri() {
		return libri;
	}

	public void setLibri(List<Libro> libri) {
		this.libri = libri;
	}

	@Override
	public int hashCode() {
		return Objects.hash(AnnoMorte, AnnoNascita, cognome, id, immagine, libri, nazionalita, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		return Objects.equals(AnnoMorte, other.AnnoMorte) && Objects.equals(AnnoNascita, other.AnnoNascita)
				&& Objects.equals(cognome, other.cognome) && Objects.equals(id, other.id)
				&& Objects.equals(immagine, other.immagine) && Objects.equals(libri, other.libri)
				&& Objects.equals(nazionalita, other.nazionalita) && Objects.equals(nome, other.nome);
	}
	
	
}
