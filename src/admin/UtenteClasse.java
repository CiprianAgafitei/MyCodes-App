package admin;

/**
 * Class of a user assigned to a class
 */
public class UtenteClasse {

	private String nome;		// first name
	private String cognome;		// surname
	private String username;	// username
	private String password;	// password
	private String tipologia;	// type
	private String classe;		// class
	private String email;		// mail
	
	public UtenteClasse(String nome, String cognome, String username, String password, String tipologia, String classe,
			String email) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.tipologia = tipologia;
		this.classe = classe;
		this.email = email;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UtenteClasse [nome=" + nome + ", cognome=" + cognome + ", username=" + username + ", password="
				+ password + ", tipologia=" + tipologia + ", classe=" + classe + ", email=" + email + "]";
	}
	
}
