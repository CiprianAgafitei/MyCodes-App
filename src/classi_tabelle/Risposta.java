package classi_tabelle;

/** 
 * Class corresponding to the asnwers tables.
 * 
 	+-------------+-------+--------------------+-------------+----------+------------+
 	| id_risposta | testo | punteggio_risposta | selezionata | corretta | id_domanda |
 	+-------------+-------+--------------------+-------------+----------+------------+
 * 
 * the score will be the sum of the scores of each asnwer:
 * 
 * 	- if selected asnwer is correct, then it will be added "punteggio_se_corretta"
 *  - if wrong, then it will be added "punteggio_se_sbagliata" (it will be negative value)
 * 
 * @author ciprian
 *
 */
public class Risposta {

	private int id_risposta;				// Asnwer ID
	private String testo;					// Answer text
	private int id_immagine;
	private double punteggio_risposta;		// Answer score: if correct, it will be positive; otherways negative
	private String is_selezionata;			// Asnwer selected (YES/NOT)
	private String corretta;				// Asnwer correct or not (YES/NOT)
	private int id_domanda;					// Id of the question

	public Risposta(int id_risposta, String testo, int id_immagine, double punteggio_risposta, String is_selezionata, String corretta,
			int id_domanda) {
		super();
		this.id_risposta = id_risposta;
		this.testo = testo;
		this.id_immagine = id_immagine;
		this.punteggio_risposta = punteggio_risposta;
		this.is_selezionata = is_selezionata;
		this.corretta = corretta;
		this.id_domanda = id_domanda;
	}

	public Risposta() {
	}

	public int getId_risposta() {
		return id_risposta;
	}

	public void setId_risposta(int id_risposta) {
		this.id_risposta = id_risposta;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public int getId_immagine() {
		return id_immagine;
	}

	public void setId_immagine(int id_immagine) {
		this.id_immagine = id_immagine;
	}

	public double getPunteggio_risposta() {
		return punteggio_risposta;
	}

	public void setPunteggio_risposta(double punteggio_risposta) {
		this.punteggio_risposta = punteggio_risposta;
	}

	public String getIs_selezionata() {
		return is_selezionata;
	}

	public void setIs_selezionata(String is_selezionata) {
		this.is_selezionata = is_selezionata;
	}

	public String getCorretta() {
		return corretta;
	}

	public void setCorretta(String corretta) {
		this.corretta = corretta;
	}

	public int getId_domanda() {
		return id_domanda;
	}

	public void setId_domanda(int id_domanda) {
		this.id_domanda = id_domanda;
	}

	@Override
	public String toString() {
		return "Risposta [id_risposta=" + id_risposta + ", testo=" + testo + ", id_immagine=" + id_immagine
				+ ", punteggio_risposta=" + punteggio_risposta + ", is_selezionata=" + is_selezionata + ", corretta="
				+ corretta + ", id_domanda=" + id_domanda + "]";
	}

}
