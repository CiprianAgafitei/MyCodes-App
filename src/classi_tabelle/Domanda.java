package classi_tabelle;

/** 
 * Class corresponding to the table of the questions.
 * 
 	+----------------+-------+-------------+
  	| codice_domanda | testo | id_verifica |
 	+----------------+-------+-------------+
 * 
 * @author Ciprian
 *
 */
public class Domanda {

	private int id_domanda;		// Question ID
	private String testo;		// Question text
	private String materia;		// Question subject
	private int id_immagine;	// Question id image (if pertinent)
	private int id_verifica;	// Question id of Test

	public Domanda(int id_domanda, String testo, String materia, int id_immagine, int id_verifica) {
		super();
		this.id_domanda = id_domanda;
		this.testo = testo;
		this.materia = materia;
		this.id_immagine = id_immagine;
		this.id_verifica = id_verifica;
	}

	public Domanda() {}

	public int getId_domanda() {
		return id_domanda;
	}

	public void setId_domanda(int id_domanda) {
		this.id_domanda = id_domanda;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getId_immagine() {
		return id_immagine;
	}

	public void setId_immagine(int id_immagine) {
		this.id_immagine = id_immagine;
	}

	public int getId_verifica() {
		return id_verifica;
	}

	public void setId_verifica(int id_verifica) {
		this.id_verifica = id_verifica;
	}

	@Override
	public String toString() {
		return "Domanda [id_domanda=" + id_domanda + ", testo=" + testo + ", materia=" + materia + ", id_immagine="
				+ id_immagine + ", id_verifica=" + id_verifica + "]";
	}

}
