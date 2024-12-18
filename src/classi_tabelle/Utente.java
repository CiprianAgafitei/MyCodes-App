package classi_tabelle;

/** 
 * Class corresponding to the general table of users
 * 
 	+-----------+------+---------+----------+----------+-----------+-----------+-------+
  	| matricola | nome | cognome | username | password | tipologia | id_classe | email |
  	+-----------+------+---------+----------+----------+-----------+-----------+-------+
  
 * @author Ciprian
 *
 */
public class Utente {
	
	private int matricola;			// User id
	private String nome;			// User first name
	private String cognome;			// User surname
	private String username;		// User surname
	private String password;		// User password
	private String tipologia;		// User type
	private int classe;				// User class
	private String email;			// User mail

	public Utente(int matricola, String nome, String cognome, String username, String password, String tipologia,
			int classe, String email) {
		super();
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		setPassword(password);
		this.tipologia = tipologia;
		this.classe = classe;
		this.email = email;
	}

	public Utente() {
	}
	
	/**
	 * Constructor method
	 * @param nome the user first name
	 * @param cognome the user surname
	 * @param email the user mail
	 * @param tipologia the user type: 1=admin, 2=teacher o 3=student
	 */
	public Utente(String nome, String cognome, String email, int tipologia) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		setTipologiaDaNumero(tipologia);
	}
	
	/** Method to set the tipology given the code
	 * @param t is the tipology code: 1= admin; 2= teacher; 3= student
	 */
	private void setTipologiaDaNumero(int t) {
		if(t == 1)
			this.tipologia = "ADMIN";
		else if(t == 2)
			this.tipologia = "DOCENTE";
		else if(t == 3)
			this.tipologia = "STUDENTE";
	}
	
	/** Method to return tipology number-code given the tupology name
	 * @return the code-tipology number (integer)
	 */
	public int getNumeroTipologia(String tipologia) {
		if(tipologia.equals("ADMIN"))
			return 1;
		else if(tipologia.equals("DOCENTE"))
			return 2;
		else
			return 3;
	}

	public int getMatricola() {
		return matricola;
	}

	public void setMatricola(int matricola) {
		this.matricola = matricola;
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

	public int getClasse() {
		return classe;
	}

	public void setClasse(int id_classe) {
		this.classe = id_classe;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Utenti [matricola=" + matricola + ", nome=" + nome + ", cognome=" + cognome + ", username=" + username
				+ ", password=" + password + ", tipologia=" + tipologia + ", id_classe=" + classe + ", email="
				+ email + "]";
	}

}
