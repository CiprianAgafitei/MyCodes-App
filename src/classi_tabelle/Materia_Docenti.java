package classi_tabelle;

/**
 * Class of the represantion of teacher and the subject attributed
 */
public class Materia_Docenti {

	private int id_materia_docenti;
	private String materia;
	private int id_docente;
	
	public Materia_Docenti(int id_materia_docenti, String materia, int id_docente) {
		super();
		this.id_materia_docenti = id_materia_docenti;
		this.materia = materia;
		this.id_docente = id_docente;
	}

	public Materia_Docenti() {}

	public int getId_materia_docenti() {
		return id_materia_docenti;
	}

	public void setId_materia_docenti(int id_materia_docenti) {
		this.id_materia_docenti = id_materia_docenti;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getId_docente() {
		return id_docente;
	}

	public void setId_docente(int id_docente) {
		this.id_docente = id_docente;
	}

	@Override
	public String toString() {
		return "Materia_Docenti [id_materia_docenti=" + id_materia_docenti + ", materia=" + materia + ", id_docente="
				+ id_docente + "]";
	}
	
}
