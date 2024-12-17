package classi_tabelle;

/**Classe corrispondente alla tabella della classe di una sezione.
 * 
 	+---------------+--------+-----------------+
  	| codice_classe | classe | anno_scolastico |
  	+---------------+--------+-----------------+
 * 
 * usata quando viene assegnata una verifica ad una classe
 * 
 * @author Ciprian
 *
 */
public class Classe {

	private int codice_classe;		//codice univoco della classe
	private String classe;			//nome della classe
	private String anno_scolastico;	//anno scolastico della classe
	
	public Classe(int codice_classe, String classe, String anno_scolastico) {
		super();
		this.codice_classe = codice_classe;
		this.classe = classe;
		this.anno_scolastico = anno_scolastico;
	}

	public Classe() {
		// TODO Auto-generated constructor stub
	}

	public int getCodice_classe() {
		return codice_classe;
	}

	public void setCodice_classe(int codice_classe) {
		this.codice_classe = codice_classe;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getAnno_scolastico() {
		return anno_scolastico;
	}

	public void setAnno_scolastico(String anno_scolastico) {
		this.anno_scolastico = anno_scolastico;
	}

	@Override
	public String toString() {
		return "Classe [codice_classe=" + codice_classe + ", classe=" + classe + ", anno_scolastico=" + anno_scolastico
				+ "]";
	}
	
}
