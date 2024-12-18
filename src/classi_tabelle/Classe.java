package classi_tabelle;

/**
 * Class corresponding to the table of a class-section.
 * 
 	+--------+-----------------+-----------------+
  	| classe | anno_scolastico | numero_studenti |	
	+--------+-----------------+-----------------+
 * 
 * used when a test is assgined to a class
 * 
 * @author Ciprian
 *
 */
public class Classe {

	private int idClasse;			// class id
	private String classe;			// class name
	private String anno_scolastico;	// class year
	private int nr_studenti;		// class numeber of students

	public Classe(int id_classe, String classe, String anno_scolastico, int numero_studenti) {
		super();
		this.idClasse = id_classe;
		this.classe = classe;
		this.anno_scolastico = anno_scolastico;
		this.nr_studenti = numero_studenti;
	}

	public Classe() {}

	public int getId_classe() {
		return idClasse;
	}

	public void setId_classe(int id_classe) {
		this.idClasse = id_classe;
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

	public int getNumero_studenti() {
		return nr_studenti;
	}

	public void setNumero_studenti(int nr_studenti) {
		this.nr_studenti = nr_studenti;
	}

	@Override
	public String toString() {
		return "Classe [id_classe=" + idClasse + ", classe=" + classe + ", anno_scolastico=" + anno_scolastico
				+ ", nr_studenti=" + nr_studenti + "]";
	}
	
}
