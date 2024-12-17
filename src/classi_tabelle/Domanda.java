package classi_tabelle;

/** Classe corrispondente alla tabella delle domande.
 * 
 	+----------------+-------+-------------+
  	| codice_domanda | testo | ID_verifica |
 	+----------------+-------+-------------+
 * 
 * @author Ciprian
 *
 */
public class Domanda {

	private int codice_domanda;		//codice univoco e progressivo
	private String testo;
	private int ID_verifica;

	public Domanda(int codice_domanda, String testo, int ID_verifica) {
		super();
		this.codice_domanda = codice_domanda;
		this.testo = testo;
		this.ID_verifica = ID_verifica;
	}

	public Domanda() {}

	public int getCodice_domanda() {
		return codice_domanda;
	}

	public void setCodice_domanda(int codice_domanda) {
		this.codice_domanda = codice_domanda;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public int getID_verifica() {
		return ID_verifica;
	}

	public void setID_verifica(int iD_verifica) {
		ID_verifica = iD_verifica;
	}

	@Override
	public String toString() {
		return "Domanda [codice_domanda=" + codice_domanda + ", testo=" + testo + ", ID_verifica=" + ID_verifica + "]";
	}

}
