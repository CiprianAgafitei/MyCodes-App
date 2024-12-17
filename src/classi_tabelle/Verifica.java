package classi_tabelle;

/**Classe corrispondente alla tabella delle verifiche: SONO CONTENUTE TUTTE LE VERIFICHE SVOLTE DAGLI STUDENTI

 !NOTA! Nella visualizzazione verranno mostrate solo le colonne necessarie, escluse dunque:
 	- test eseguito
  	- punteggio_ottenuto
  	- punteggio_totale
  	- voto
  
+-----------------+-------------------+--------+---------------+--------------------+------------------+------+-------------------+--------------------+
| codice_verifica | materia_argomento | classe | test_eseguito | punteggio_ottenuto | punteggio_totale | voto | matricola_docente | matricola_studente |
+-----------------+-------------------+--------+---------------+--------------------+------------------+------+-------------------+--------------------+
  
  STRUTTURA VISUALIZZAZIONE VERIFICA AI DOCENTI:
  
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
  		
  	STRUTTURA ESECUZIONE VERIFICA DA PARTE DEGLI STUDENTI:
  	
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

	private int codice_verifica;		//codice progressivo ed univovo
	private String materia_argomento;	//Materia e argomento del test
	private String classe;				//classe a cui sarà assegnato il test
	private String test_eseguito;		//Indica se il test è stato eseguito o meno: SI o NO
	private double punteggio_ottenuto;
	private double punteggio_totale;
	private double voto;
	private int matricola_docente;		//FK per collegarsi al docente che ha creato il test
	private int matricola_studente;		//FK matricola dello studente a cui appartiene la verifica

	public Verifica(int codice_verifica, String materia, String classe, String test_eseguito,
			double punteggio_ottenuto, double punteggio_totale, double voto, int matricola_docente, int matricola_studente) {
		super();
		this.codice_verifica = codice_verifica;
		this.materia_argomento = materia;
		this.classe = classe;
		this.test_eseguito = test_eseguito;
		this.punteggio_ottenuto = punteggio_ottenuto;
		this.punteggio_totale = punteggio_totale;
		this.voto = voto;
		this.matricola_docente = matricola_docente;
		this.matricola_studente = matricola_studente;
	}

	public Verifica() {	}

	public int getCodice_verifica() {
		return codice_verifica;
	}

	public void setCodice_verifica(int codice_verifica) {
		this.codice_verifica = codice_verifica;
	}

	public String getMateria() {
		return materia_argomento;
	}

	public void setMateria(String materia) {
		this.materia_argomento = materia;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String isTest_eseguito() {
		return test_eseguito;
	}

	public void setTest_eseguito(String test_eseguito) {
		this.test_eseguito = test_eseguito;
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

	public int getMatricola_docente() {
		return matricola_docente;
	}

	public void setMatricola_docente(int matricola_docente) {
		this.matricola_docente = matricola_docente;
	}

	public int getMatricola_studente() {
		return matricola_studente;
	}
	
	public void setMatricola_studente(int matricola_studente) {
		this.matricola_studente = matricola_studente;
	}

	@Override
	public String toString() {
		return "Verifica [codice_verifica=" + codice_verifica + ", materia_argomento=" + materia_argomento + ", classe="
				+ classe + ", test_eseguito=" + test_eseguito + ", punteggio_ottenuto=" + punteggio_ottenuto
				+ ", punteggio_totale=" + punteggio_totale + ", voto=" + voto + ", matricola_docente="
				+ matricola_docente + ", matricola_studente=" + matricola_studente + "]";
	}

}
