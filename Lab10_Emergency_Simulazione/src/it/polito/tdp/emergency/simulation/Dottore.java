package it.polito.tdp.emergency.simulation;

public class Dottore {
	
	private int id;
	private boolean disponibile;
	private static final int maxID=2000;
	
	public Dottore(int id, boolean disponibile) {
		super();
		this.id = id+maxID;
		this.disponibile = disponibile;
	}

	public boolean getDisponibile() {
		return disponibile;
	}

	public void setSDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Dottore [id=" + id + ", disponibile=" + disponibile + "]";
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
		Dottore other = (Dottore) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
