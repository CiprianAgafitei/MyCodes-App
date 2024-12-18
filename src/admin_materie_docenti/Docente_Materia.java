package admin_materie_docenti;

/**
 * Class of each teahcer with their subject assigned
 */
public class Docente_Materia {

	private String nome;		// teacher first name
	private String cognome;		// teacher surname
	private String username;	// teacher username
	private String materia;		// teacher subject
	
	public Docente_Materia(String nome, String cognome, String username, String materia) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.materia = materia;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	@Override
	public String toString() {
		return "Docente_Materia [nome=" + nome + ", cognome=" + cognome + ", username=" + username + ", materia="
				+ materia + "]";
	}
	
}
