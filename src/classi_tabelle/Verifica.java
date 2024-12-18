package classi_tabelle;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Corresponding class to the Tests table: there are all the tests done by the students

 !IMPORTANT! In the view there will be only necessary columns:
 	- Test done
  	- Score obtained
  	- Total Score
  	- Mark
  
+-------------+-----------------+-------------------+--------+---------------+--------------------+------------------+------+-------------------+--------------------+
| id_verifica | codice_verifica | materia_argomento | classe | test_eseguito | punteggio_ottenuto | punteggio_totale | voto | matricola_docente | matricola_studente |
+-------------+-----------------+-------------------+--------+---------------+--------------------+------------------+------+-------------------+--------------------+
  
  TEST VIZUALIZATION STRUCTURE TO TEACHERS:
  
  | codice_verifica | materia_argomento | classe | matricola_docente |
  
  		| testo_domanda1 | 
  		| risposta1 | risposta2 | ... | rispostaN |
  		+----------+-----------+-----+-----------+ 
  		| testo_domanda2 | 
  		| risposta1 | risposta2 | ... | rispostaN | 
  		+----------+-----------+-----+-----------+ 
  		| testo_domanda3 | 
  		| risposta1 | risposta2 | ... | rispostaN | 
  		+----------+-----------+-----+-----------+ 
  		| testo_domanda4 | 
  		| risposta1 | risposta2 | ... | rispostaN | 
  		+----------+-----------+-----+-----------+ 
  		   	....	    ...		 ...	  ...
  		+----------+-----------+-----+-----------+ 
  		| testo_domanda5 | 
  		| risposta1 | risposta2 | ... | rispostaN | 
  		+----------+-----------+-----+-----------+ 
  		
  	TEST EXECUTION VIEW TO STUDENTS:
  	
  	| codice_verifica | materia_argomento | classe | matricola_studente | punteggio_ottenuto | punteggio_totale | voto |
  	
  		| testo_domanda1 | 
  		| risposta1 | risposta2 | ... | rispostaN |
  	   --> risposta_studente						SELEZIONE DELLA RISPOSTA SCELTA DALL'UTENTE
  		| testo_domanda2 | 
  		| risposta1 | risposta2 | ... | rispostaN |
  	   --> risposta_studente						SELEZIONE DELLA RISPOSTA SCELTA DALL'UTENTE
   		| testo_domanda3 | 
  		| risposta1 | risposta2 | ... | rispostaN |
  	   --> risposta_studente 						SELEZIONE DELLA RISPOSTA SCELTA DALL'UTENTE
  	   	   					...
  	   	| testo_domandaN | 
  		| risposta1 | risposta2 | ... | rispostaN |
  	   --> risposta_studente						SELEZIONE DELLA RISPOSTA SCELTA DALL'UTENTE
  	   
 * @author Ciprian
 *
 */
public class Verifica {

	private int id_verifica;			// Test id
	private String argomento;			// Test argument
	private String is_eseguita;			// test done (YES/NO)
	private String is_assegnata;		// Test assigned
	private double punteggio_ottenuto;	// Score obtained
	private double punteggio_totale;	// Total score
	private double voto;				// Mark
	private Time tempo;					// Time
	private String scadenza;			// Final date
	private int id_utente;		// FK to connect to the teacher who created the test
	private int id_classe;				// Id of the class
	private int id_materia_docente;		// Id of the subject and teacher

	public Verifica(int id_verifica, String argomento, String is_eseguita, String is_assegnata,
			double punteggio_ottenuto, double punteggio_totale, double voto, Time tempo, String scadenza,
			int id_classe, int id_utente, int id_materia_docente) {
		super();
		this.id_verifica = id_verifica;
		this.argomento = argomento;
		this.is_eseguita = is_eseguita;
		this.is_assegnata = is_assegnata;
		this.punteggio_ottenuto = punteggio_ottenuto;
		this.punteggio_totale = punteggio_totale;
		this.voto = voto;
		this.tempo = tempo;
		this.scadenza = scadenza;
		this.id_utente = id_utente;
		this.id_classe = id_classe;
		this.id_materia_docente = id_materia_docente;
	}

	public Verifica() {	}
	
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

	public double getPunteggio_ottenuto() {
		return punteggio_ottenuto;
	}

	public void setPunteggio_ottenuto(double punteggio_ottenuto) {
		this.punteggio_ottenuto = punteggio_ottenuto;
	}

	public double getPunteggio_totale() {
		return punteggio_totale;
	}

	public void setPunteggio_totale(double punteggio_totale) {
		this.punteggio_totale = punteggio_totale;
	}

	public double getVoto() {
		return voto;
	}

	public void setVoto(double voto) {
		this.voto = voto;
	}
	
	public String getTest_eseguito() {
		return is_eseguita;
	}

	public Time getTempo() {
		return tempo;
	}

	public void setTempo(Time tempo) {
		this.tempo = tempo;
	}

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

	public String getIs_eseguita() {
		return is_eseguita;
	}

	public void setIs_eseguita(String is_eseguita) {
		this.is_eseguita = is_eseguita;
	}

	public String getIs_assegnata() {
		return is_assegnata;
	}

	public void setIs_assegnata(String is_assegnata) {
		this.is_assegnata = is_assegnata;
	}

	public int getId_utente() {
		return id_utente;
	}

	public void setId_utente(int id_utente) {
		this.id_utente = id_utente;
	}

	public int getId_classe() {
		return id_classe;
	}

	public void setId_classe(int id_classe) {
		this.id_classe = id_classe;
	}

	public int getId_materia_docente() {
		return id_materia_docente;
	}

	public void setId_materia_docente(int id_materia_docente) {
		this.id_materia_docente = id_materia_docente;
	}
	
	public void setPunteggioTotale() {
		double punteggio = 0.0;
		ArrayList<Domanda> domande = IoOperations.getDomandeVerifica(id_verifica);
		
		for(int i = 0; i < domande.size(); i++) {
			ArrayList<Risposta> risposte = IoOperations.getRisposteEsecuzioneVerifica(domande.get(i).getId_domanda());
			for(int j = 0; j < risposte.size(); j++) {
				//Controllo se il punteggio fa riferimento ad una risposta corretta, quindi se positivo
				if(risposte.get(j).getPunteggio_risposta() > 0.0)
					punteggio += risposte.get(j).getPunteggio_risposta();
			}
		}
		this.punteggio_totale = punteggio;
	}

	@Override
	public String toString() {
		return "Verifica [id_verifica=" + id_verifica + ", argomento=" + argomento + ", is_eseguita=" + is_eseguita
				+ ", is_assegnata=" + is_assegnata + ", punteggio_ottenuto=" + punteggio_ottenuto
				+ ", punteggio_totale=" + punteggio_totale + ", voto=" + voto + ", tempo=" + tempo + ", scadenza="
				+ scadenza + ", id_utente=" + id_utente + ", id_classe=" + id_classe + ", id_materia_docente="
				+ id_materia_docente + "]";
	}
	
}
