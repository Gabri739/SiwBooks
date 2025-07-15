package it.uniroma3.siwbooks.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Immagine {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nomeImmagine; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeImmagine() {
		return nomeImmagine;
	}

	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nomeImmagine);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Immagine other = (Immagine) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomeImmagine, other.nomeImmagine);
	}
	
	
}
