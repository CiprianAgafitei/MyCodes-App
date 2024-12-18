package admin;

/**
 * Class of a user and its subject assigned
 */
public class UtenteMateria {

	private String materia;		// Subject
	private String utente;		// User
	
	public UtenteMateria(String materia, String utente) {
		super();
		this.materia = materia;
		this.utente = utente;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	@Override
	public String toString() {
		return "UtenteMateria [materia=" + materia + ", utente=" + utente + "]";
	}
	
}
