package it.polito.tdp.emergency.simulation;

public class Assistente {
	
	private Integer id; 
	private boolean disponibile;
	
	public Assistente(Integer id, boolean disponibile) {
		super();
		this.id = id+3000; //per non confonderlo con gli id di pazienti e dottori
		this.disponibile = disponibile;
	}

	public boolean isDisponibile() {
		return disponibile;
	}

	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assistente other = (Assistente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Assistente [id=" + id + ", disponibile=" + disponibile + "]";
	}
	
	

}
