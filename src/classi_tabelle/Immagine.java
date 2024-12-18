package classi_tabelle;

import java.sql.Blob;

/**
 * Class to define the properties of an image
 */
public class Immagine {

	private int id_immagine;	// Id image
	private String nome;		// Image name
	private Blob immagine;		// image
	
	public Immagine(int id_immagine, String nome, Blob immagine) {
		super();
		this.id_immagine = id_immagine;
		this.nome = nome;
		this.immagine = immagine;
	}

	public Immagine() {}

	public int getId_immagine() {
		return id_immagine;
	}

	public void setId_immagine(int id_immagine) {
		this.id_immagine = id_immagine;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Blob getImmagine() {
		return immagine;
	}

	public void setImmagine(Blob immagine) {
		this.immagine = immagine;
	}

	@Override
	public String toString() {
		return "Immagine [id_immagine=" + id_immagine + ", nome=" + nome + ", immagine=" + immagine + "]";
	}
	
}
