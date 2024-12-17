package classi_tabelle;

/**Classe corrispondente alla tabella degli utenti generale.
 * 
 	+-----------+------+---------+----------+----------+-----------+-----------+-------+
  	| matricola | nome | cognome | username | password | tipologia | ID_classe | email |
  	+-----------+------+---------+----------+----------+-----------+-----------+-------+
  
 * @author Ciprian
 *
 */
public class Utenti {
	
	private int matricola;
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private String tipologia;	//1=admin;  2=docenti;  3=studenti;
	private String classe;		//Non è la foreign key perché ce la hanno solo gli studenti
	private String email;
	
	public Utenti(int matricola, String nome, String cognome, String username, String password, String tipologia, String classe, String email) {
		super();
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.tipologia = tipologia;
		this.classe = classe;
		this.email = email;
	}

	public Utenti() {
		// TODO Auto-generated constructor stub
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

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
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
				+ ", password=" + password + ", tipologia=" + tipologia + ", ID_classe=" + classe + ", email="
				+ email + "]";
	}

}
