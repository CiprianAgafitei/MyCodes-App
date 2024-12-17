package classi_tabelle;

/** Classe corrispondente alla tabella delle risposte.
 * 
 	+---------------------------+-------+--------------------+-------------+----------+
 	| ID_domanda_di_riferimento | testo | punteggio_risposta | selezionata | corretta |
 	+---------------------------+-------+--------------------+-------------+----------+
 * 
 * il punteggio totale sarà dato dalla somma dei punteggi delle singole risposte:
 * 
 * 	- se corretta la risposta selezionata, si sommerà punteggio_se_corretta
 *  - se sbagliata si sommerà punteggio_se_sbagliata (sarà negativo)
 * 
 * @author ciprian
 *
 */
public class Risposta {

	private int ID_dd_riferimento;			//codice che fa riferimento alla domanda della risposta
	private String testo;					//testo della risposta
	private double punteggio_risposta;		//punteggio per la risposta: se questa è corretta per la domanda, sarà positivo; altrimenti negativo
	private String selezionata;				//Indica se la risposta è stata selezionata
	private String corretta;				//Insica se la risposta è corretta
	
	public Risposta(int ID_DD_riferimento, String testo, double punteggio_se_corretta, String selezionata, String corretta) {
		super();
		this.ID_dd_riferimento = ID_DD_riferimento;
		this.testo = testo;
		this.punteggio_risposta = punteggio_se_corretta;
		this.selezionata = selezionata;
		this.corretta = corretta;
	}

	public Risposta() {
		// TODO Auto-generated constructor stub
	}

	public int getCodice_risposta() {
		return ID_dd_riferimento;
	}

	public void setCodice_risposta(int codice_risposta) {
		this.ID_dd_riferimento = codice_risposta;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public double getPunteggio_risposta() {
		return punteggio_risposta;
	}

	public void setPunteggio_risposta(double punteggio_risposta) {
		this.punteggio_risposta = punteggio_risposta;
	}

	public String isSelezionata() {
		return selezionata;
	}

	public void setSelezionata(String selezionata) {
		this.selezionata = selezionata;
	}

	public String isCorretta() {
		return corretta;
	}

	public void setCorretta(String corretta) {
		this.corretta = corretta;
	}

	@Override
	public String toString() {
		return "Risposta [codice_risposta=" + ID_dd_riferimento + ", testo=" + testo + ", punteggio_se_corretta="
				+ punteggio_risposta + ", selezionata=" + selezionata + ", corretta=" + corretta + "]";
	}
	
}
