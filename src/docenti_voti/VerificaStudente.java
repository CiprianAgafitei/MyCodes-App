package docenti_voti;

/** Classe di supporto per poter creare una tabella contenente i dati di tre tabelle, ovvero:
 * 		- verifica: l'argomento e la scadenza della verifica
 * 		- classe: l'anno e la sezione
 * 		- utente: nome e cognome degli studenti di ciascuna verifica
 * 
 * @author Agafitei Cipriam
 *
 */
public class VerificaStudente {

	private int id_verifica;
	private String argomento;
	private String classe;
	private String nome;
	private String cognome;
	private String scadenza;
	private String eseguita;
	private String voto;
	
	public VerificaStudente(int id_verifica, String argomento, String classe, String nome, String cognome, String scadenza, String eseguita, double voto) {
		super();
		this.id_verifica = id_verifica;
		this.argomento = argomento;
		this.classe = classe;
		this.nome = nome;
		this.cognome = cognome;
		this.scadenza = scadenza;
		this.eseguita = eseguita;
		setVoto(voto);
	}
	
	public int getId_verifica() {
		return id_verifica;
	}

	public void setId_verifica(int id_verifica) {
		this.id_verifica = id_verifica;
	}

	public String getArgomento() {
		return argomento;
	}

	public void setArgomento(String argomento) {
		this.argomento = argomento;
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

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

	public String getEseguita() {
		return eseguita;
	}

	public void setEseguita(String eseguita) {
		this.eseguita = eseguita;
	}

	public String getVoto() {
		return voto;
	}

	public void setVoto(double voto) {
		if(voto > 0.0)
			this.voto = voto + "";
		else
			this.voto = "";
	}

	@Override
	public String toString() {
		return "VerificaStudente [id_verifica=" + id_verifica + ", argomento=" + argomento + ", classe=" + classe
				+ ", nome=" + nome + ", cognome=" + cognome + ", scadenza=" + scadenza + ", eseguita=" + eseguita
				+ ", voto=" + voto + "]";
	}
	
}
