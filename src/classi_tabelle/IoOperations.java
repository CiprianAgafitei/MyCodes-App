package classi_tabelle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.*;

/**
 * 
 * ELENCO TABELLE: 
 * - classe			-> tabella con l'insieme degli studenti di una classe
 * - utenti 		-> tabella generale con admin, studenti, docenti : ordinati in ordine crescente in base alla matricola
 * - studenti 		-> tabella studenti alla quale si giunge dalla tabella utenti 
 * - verifica		-> tabella dei test generali dei docenti: possono creare una nuova verifica e
 * 					assegnarla ad una classe (o visualizzare verifiche già effettuate <-OPZIONALE
 * 					dopo fine versione base). COMUNE A TUTTI I DOCENTI.
 * - domande		-> tabella elenco di tutte le domande
 * - risposte		-> tabella elenco di tutte le risposte
 * 
 * 
 * FUNZIONAMENTO DEL COLLEGAMENTO TRA TABELLE:
 * 
 * 1) Da "utenti" si può accedere alle verifiche personali nel caso si fosse uno studente, quindi ad una propria tabella "verifiche";
 * 2) Da "utenti" si può accedere alla tabella "verifiche" di tutti i docenti: da qui si può creare una nuova verifica collegandosi dunque 
 * 		alla tabella doamnde e poi a quella risposte nella creazione della verifica;
 * 3) Una volta creata una verifica questa verrà assegnata ad una classe e automaticamente a tutti gli studenti che appartengono ad essa
 * 
 * @author Ciprian
 *
 */
public class IoOperations {

	/**Metodo che restituisce tutti gli utenti del database utenti
	 * @return un ArrayList con tutti gli utenti del database
	 */
	public static ArrayList<Utente> getUtenti () {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utenti WHERE nome <> '1'"
				+ " AND nome <> '2' AND nome <> '3' AND nome <> '4' AND nome <> '5';";	//Visualizzazione di tutti gli utenti della tabella
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			utenti = new ArrayList<Utente>();

			while (resultSet.next()) {
				Utente utente = new Utente();
				utente.setMatricola(resultSet.getInt("matricola"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setUsername(resultSet.getString("username"));
				utente.setPassword(resultSet.getString("password"));
				utente.setTipologia(resultSet.getString("tipologia"));
				utente.setClasse(resultSet.getString("classe"));
				utente.setEmail(resultSet.getString("email"));
				utenti.add(utente);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utenti;
	}
	
	/**Metodo che restituisce tutti gli studenti del database utenti
	 * @return un ArrayList con tutti gli studenti del database
	 */
	public static ArrayList<Utente> getStudenti() {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utenti WHERE tipologia='" + 3 + "'"
				+ " AND nome <> '1' AND nome <> '2' AND nome <> '3' AND nome <> '4' AND nome <> '5';";	//Visualizzazione di tutti gli utenti della tabella
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			utenti = new ArrayList<Utente>();

			while (resultSet.next()) {
				Utente utente = new Utente();
				utente.setMatricola(resultSet.getInt("matricola"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setUsername(resultSet.getString("username"));
				utente.setPassword(resultSet.getString("password"));
				utente.setTipologia(resultSet.getString("tipologia"));
				utente.setClasse(resultSet.getString("classe"));
				utente.setEmail(resultSet.getString("email"));
				utenti.add(utente);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utenti;
	}
		
	/**Metodo che restituisce tutte le verifiche del database verifiche GENERALE
	 * dei DOCENTI: solo i docenti vedranno solo alcuni elementi della tabella verifica
	 * 
	 * 	LE VERIFICHE SONO VUOTE, SENZA COMPLETAMENTI -> valori nulli
	 * 
	 * @return un ArrayList con tutti gli utenti del database
	 */
	public ArrayList<Verifica> visualizzaVerificheSuDocenti () {
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT DISTINCT * FROM verifica WHERE codice_verifica";	//Selezionata casualmente una verifica
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setCodice_verifica(resultSet.getInt("codice_verifica"));
				verifica.setMateria(resultSet.getString("materia_argomento"));
				verifica.setClasse(resultSet.getString("classe"));
				verifica.setMatricola_docente(resultSet.getInt("matricola_docente"));
				verifiche.add(verifica);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifiche;
	}
	
	/**Metodo che restituisce tutte le domande della tabella domande 
	 * dei DOCENTI: solo i docenti vedranno solo alcuni elementi della tabella verifica
	 * 
	 * @return un ArrayList con tutti gli utenti del database
	 */
	public ArrayList<Verifica> visualizzaVerificheSuStudenti (int matricola_studente) {
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica where matricola_studente=" + matricola_studente;
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setCodice_verifica(resultSet.getInt("codice_verifica"));
				verifica.setMateria(resultSet.getString("materia_argomento"));
				verifica.setClasse(resultSet.getString("classe"));
				verifica.setTest_eseguito(resultSet.getString("test_eseguito"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setVoto(resultSet.getInt("voto"));
				
				verifiche.add(verifica);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifiche;
	}
	
	/**Metodo che restituisce tutte le domande della tabella domande 
	 * dei DOCENTI: solo i docenti vedranno solo alcuni elementi della tabella verifica
	 * 
	 * @return un ArrayList con tutti gli utenti del database
	 */
	public ArrayList<Domanda> getDomande () {
		ArrayList<Domanda> domande = null;
		String query = "SELECT * FROM domanda";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			domande = new ArrayList<Domanda>();

			while (resultSet.next()) {
				Domanda domanda = new Domanda();
				domanda.setCodice_domanda(resultSet.getInt("codice_domanda"));
				domanda.setTesto(resultSet.getString("testo"));
				domande.add(domanda);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return domande;
	}
	
	/**	METODO PER IL LOGIN ALLA TABELLA UTENTI
	 * 
	 * Metodo per il controllo che un utente sia registrato nel database date le sue credenziali
	 * @param usr è il primo fattore delle credenziali necessarie per accedere: USERNAME
	 * @param pass è il secondo fattore delle credenziali necessarie per accedere: PASSWORD
	 * @return false se l'utente non è registrato nel database; true se lo è.
	 * 
	 * IL DATABASE IN CUI SI CERCA è QUELLO GENERALE di tutti gli utenti.
	 */
	public static boolean login (String usr, String pass) {
		String query = "SELECT * FROM utenti WHERE username='" + usr + "' AND password='" + pass + "'";
		DbQuery dbQuery = new DbQuery();
		boolean result = true;
		
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			result = resultSet.next();	//resultSet.next() scorre la tabella
						
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** METODO PER L'OTTENIMENTO DELLA TIPOLOGIA UTENTE DALLA TABELLA UTENTI
	 * 
	 * Dati username e passsword si cerca nella tabella la riga corrispondente e se ne preleva la tipologia
	 * @param username è l'username dell'utente da cercare
	 * @param password è la password dell'utente da cercare
	 * @return 1 se si tratta di ADMIN; 2 se si tratta di DOCENTE; 3 se si tratta di STUDENTE.
	 */
	public static int getTipologiaUtente (String username) {
		String query = "SELECT tipologia FROM utenti WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		int tipologia = 0;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet .next()) {
				tipologia = resultSet.getInt("tipologia");
			}
			dbQuery.releaseConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tipologia;
	}
	
	/** METODO PER L'OTTENIMENTO DELLA MATRICOLA UTENTE DALLA TABELLA UTENTI
	 * 
	 * Dati username e passsword si cerca nella tabella la riga corrispondente e se ne preleva la MATRICOLA
	 * @param username è l'username dell'utente da cercare
	 * @param password è la password dell'utente da cercare
	 * @return un intero che rappresenterà la matricola dell'utente
	 */
	public static int getMatricolaUtente (String username) {
		String query = "SELECT matricola FROM utenti WHERE username='" + username + "'";
		DbQuery dbQuery = new DbQuery();
		int tipologia = 0;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet .next()) {
				tipologia = resultSet.getInt("matricola");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tipologia;
	}

	/** METODO PER LA CREAZIONE DI UN NUOVO ACCOUNT per la tabella UTENTI
	 * 
	 * Metodo che dato nome e cognome crea l'username per il nuovo account e la password passata
	 * sarà quella per accedere al proprio account. L'username sarà composto da:
	 * 								nome.cognome
	 * 
	 * @param nome da inserire per l'username
	 * @param cognome da inserire nell'username 
	 * return un array con 2 stringhe:
	 * 			 un boolean: true se la registrazione è andata a buon fine; false se ci sono stati problemi
	 * 			 una stringa: contenente l'username del nuovo utente registrato
	 */
	public static String[] createAccount (String nome, String cognome, int tipologia, String email) {		
		String[]array = new String[2];
		boolean is_registrazione_senza_errori = true;	
		String username = nome + "." + cognome;
		String password = null;
		//Query per la creazione di un nuovo account
		String query = "INSERT INTO utenti (nome, cognome, username, password, tipologia, classe, email)"
+ " VALUES ('" + nome + "', '" + cognome + "', '" + username + "', '" + password + "', '" + tipologia + "', '" + 0 + "', '" + email + "');";
		
		DbQuery dbQuery = new DbQuery();
		
		try {
			//Creazione account
			if(DbQuery.setData(query, true)) {
			
				//Ottenimento matricola
				int matricola = getMatricolaUtente(username);
				username += matricola;
				
				//Query per rendere l'username univoco
				String query2 = "UPDATE utenti SET username='" + username + "' WHERE matricola=" + matricola + ";" ;
			
				//Finalizzazione creazione account
				if(!DbQuery.setData(query2, true)) {
					is_registrazione_senza_errori = false;
				}
			}else {
				is_registrazione_senza_errori = false;
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		array[0] = "" + is_registrazione_senza_errori;
		array[1] = username;
		return array;
	}
	
	/**METODO PER VERIFICARE L'ESISTENZA DI UN UTENTE DATO L'USERNAME
	 * @param username è l'username da ricercare
	 * @return
	 */
	public static boolean controlloEsistenzaUsername(String username) {
		//Query per verificare l'esistenza dell'utente
		String query = "SELECT * FROM utenti WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		String nome = null;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				nome = resultSet.getString("username");
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(nome != null)
			return true;
		else
			return false;
	}
 
	/** METODO PER LA FINALIZZAZIONE DELLA CREAZIONE DI UN ACCOUNT MANUALMENTE DALL'AMMINISTRATORE
	 * 
	 * Metodo che essenzialmente permette di inserire la password dell'utente che ha effettuato
	 * il primo accesso con le credenziali fornite dall'amministratore del sistema.
	 * 
	 * @param username è l'usernme dell'utente per cui impostare la password
	 * @param password_nuova è la password da impostare per l'account dell'utente
	 */
	public void primoAccessoOREditPassword (String username, String password_nuova) {
		//Query per verificare l'esistenza dell'utente e ottenerne l'username
		String query = "SELECT * FROM utenti WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		String nome = null;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				nome = resultSet.getString("username");
			}
			
			//Se l'utente esiste
			if(nome != null) {
				//Query per l'aggiornamento della password con quella del nuovo utente
				query = "UPDATE utenti SET password ='" + password_nuova + "' WHERE username='" + username + "'";
				
				if(!DbQuery.setData(query, true)) ;
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER DETERMINARE DATA UNA MATRICOLA SE QUESTA ESISTE E APPARTIENTE AD UN UNTENTE DELLA TABELLA
	 * 
	 * Data una matricola si determina se questa appartiene o meno ad un utente registrato.
	 * 
	 * @param matricola è la matricola da cercare
	 * @return true se esiste un utente con tale matricola; false se non esiste
	 */
	public boolean controlloEsistenzaMatricolaInUtenti (int matricola) {
		String query = "SELECT * FROM utenti WHERE matricola='" + matricola + "';";
		DbQuery dbQuery = new DbQuery();
		boolean result = true;
		
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			result = resultSet.next();
			
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** METODO CHE DATO l'ID DI UNO STUDENTE NE RESTITUISCE L'USERNAME
	 * 
	 * @param ID_user è la matricola dello studente
	 * @return
	 */
	public static String getUsernameFromIDUser (int ID_user) {
		String query = "SELECT username FROM utenti WHERE matricola='" + ID_user + "';";
		DbQuery dbQuery = new DbQuery();
		String username = "";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet .next()) {
				username = resultSet.getString("username");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return username;
	}
	
	/** METODO PER LA RIMOZIONE DI UN UTENTE DALLA TABELLA SELEZIONATA
	 * 
	 * Dato l'username dell'utente che si vuole rimuovere viene eseguita tale query sulla tabella degli utenti
	 * 
	 * @param matricola_utente_da_rimuovere_int è la matricola dell'utente da rimuovere
	 */
	public static void removeUser(String username) {
		String query = "DELETE FROM utenti WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		
		try {
			DbQuery.setData(query, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER RIMUOVERE UNO STUDENTE DA UNA CLASSE ELIMINANDO SEMPLICEMENTE L'ID_classe
	 * 
	 * @param matricola_utente_da_rimuovere_int è l'ID dello studente da rimuovere
	 */
	public static void removeStudentFromClass(String username) {
		String trattino = "-";
		String query = "UPDATE utenti SET classe='" + trattino + "' WHERE username='" + username + "';";	//Indica che lo studente non appartiene ad una classe
		DbQuery dbQuery = new DbQuery();
		
		try {
			DbQuery.setData(query, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER RESETTARE LA PASSWORD DI UN UTENTE DATO
	 * 
	 * Dato un sername viene azzerrata la password per tale utente
	 * 
	 * @param username è l'username dell'utente da modificare
	 */
	public static void resetPassword(String username) {
		String nuovaPassword = null;
		String query = "UPDATE utenti SET password='" + nuovaPassword + "' WHERE username='" + username + "';";//forse mancano ; alla fine
		DbQuery dbQuery = new DbQuery();
		
		try {
			DbQuery.setData(query, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO DI CONTROLLO SE UNA VERIFICA SELEZIONATA DALLO STUDENTE ESISTE
	 * 
	 * Data la matricola dello studente e il codice della verifica si effettua il controllo nella tabella personale dello studente
	 * cercando l'esistenza della verifica in base al suo codice univoco.
	 * 
	 * @param codice_verifica è il codice della verifica da controllare
	 * @param matricola_studente è la matricola dello studente che ha selezionato una verifica
	 * @param operatore serve per indicare l'operazione da effettuare, di uguaglianza o disuguaglianza
	 * 					con la stringa che indica se la veifica è stata eseguta
	 * @return true se esiste la verifica; false se non esiste
	 */
	public boolean controlloEsistenzaVerificaPerStudente (int codice_verifica, int matricola_studente, String operatore) {
		String query = "SELECT * FROM verifica WHERE codice_verifica='" + codice_verifica + "' "
				+ "AND matricola_studente='" + matricola_studente + "'"
				+ "AND test_eseguito" + operatore + "'NO';";
		DbQuery dbQuery = new DbQuery();
		boolean result = true;
		
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			result = resultSet.next();	//resultSet.next() scorre la tabella
			
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** METODO PER LA CREAZIONE DI UNA RISPOSTA DATE LE INFORMAZIONI DAL DOCENTE
	 * 
	 * Metodo che dato il testo, il punteggio nel caso fosse corretta, punteggio nel caso fosse erratta e definito se
	 * la risposta è corretta in riferimento alla domanda, viene creata ed inserita nella tabella
	 * 
	 * @param testo è il testo della risposta
	 * @param punteggio_risposta è il punteggio che verrà assegnato alla risposta nella correzione:
	 * 				positivo se la risposta è corretta; negativo se è erratta.
	 * @param isCorretta indica se la risposta inserita è quella o una di quelle corrette per la domanda
	 * return la matricola della risposta dalla tabella risposte
	 */
	public int creaRisposta (String testo, double punteggio_risposta, String isCorretta, int ID_domanda) {
		int codice_risposta = 0;
		
		if(isCorretta.contains("S"))
			isCorretta = "SI";
		else
			isCorretta = "NO";
		
		//Controllo se il punteggio è stato inserito con il meno; altrimenti viene aggiunto
		if(isCorretta.equals("NO") && punteggio_risposta > 0)
			punteggio_risposta *= -1;
		
		//Inserimento nella tabella
		String query = "INSERT INTO risposta (ID_domanda_di_riferimento, testo, punteggio_risposta, selezionata, corretta) VALUES "
		+ "('" + ID_domanda + "', '" + testo + "', '" + punteggio_risposta + "', '" + "N" + "', " + "'" + isCorretta + "')"; 
		DbQuery dbQuery = new DbQuery();
		
		try {
			DbQuery.setData(query, true);

			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return codice_risposta;
	}
	
	/** METODO PER LA CREAZIONE DI UNA DOMANDA DATO IL TESTO E I CODICI DELLE DOMANDE
	 * 
	 * Dato il testo e i codici delle risposte inserite in una stringa separate da "|" ,
	 * si crea una domanda che verrà inserita nella tabella di tutte le domande.
	 * 
	 * @param testo è il testo della domanda
	 * @param codici_risposte è l'insieme delle risposte possibili per la domanda
	 * @return l'identificativo della domanda appena creata
	 */
	public int creaDomanda (String testo, int ID_verifica) {
		int codice_domanda = 0;
		
		//Inserimento nella tabella
		String query = "INSERT INTO domanda (testo, ID_verifica) VALUES " + "('" + testo + "', '" + ID_verifica + "')"; 
		DbQuery dbQuery = new DbQuery();
		
		String query2 = "SELECT codice_domanda FROM domanda WHERE testo='" + testo + "';";
		
		try {
			if(DbQuery.setData(query, true)) {
				Connection conn = DbSingleConn.getConnection();
				Statement stat = conn.createStatement();
				ResultSet resultSet = stat.executeQuery(query2);
		
				while (resultSet .next()) {
					codice_domanda = resultSet.getInt("codice_domanda");
				}
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return codice_domanda;
	}
			
	/** METODO PER LA CREAZIONE DI UNA TABELLA CLASSE CON NOME L'ANNO E LA SEZIONE + PROFILO DI SUPPORTO
	 * 
	 * Viene creata una nuova classe e una tabella nel database con il nome di questa l'anno e la sezione della classe.
	 * Viene inoltre inserito uno studente di supporto, con valori della classe, utile per l'assegnazione delle verifiche.
	 * @param classe è l'insieme composto da anno e sezione della classe
	 * @param codici_tabella_studenti è l'insieme delle matricole degli studenti che vi appartengono
	 * @param anno_scolastico è l'anno di svolgimento scolastico
	 */
	public static void creaClasse (String classe, String anno_scolastico) {		
		String query = "INSERT INTO classe (classe, anno_scolastico) VALUES ('" + classe + "', '" + anno_scolastico + "')";
		DbQuery dbQuery = new DbQuery();
		
		try {
			if(DbQuery.setData(query, true)) {
				String query2 = "INSERT INTO utenti (nome, cognome, username, password, tipologia, classe, email) "
						+ "VALUES ('" + classe.charAt(0) +  "', '" + classe.charAt(1) + "', '" + classe.charAt(0) + "', '" + classe.charAt(1) + "', '" + 3 + "', '" + classe + "', '" + null + "');";
				DbQuery.setData(query2, true);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER L'OTTENIMENTO DEL NOME DELLA CLASSE DATO l'ID DELLA VERIFICA
	 * 
	 * Viene cercata la verifica tramite l'ID e poi si preleva la classe a cui è
	 * stata assegnata.
	 * 
	 * @param ID_verifica è l'ID della verifica per cui cercare la classe
	 * @return la stringa con il nome della classe
	 */
	public String getClasseFromIDOfVerifica (int ID_verifica) {
		String id_classe = "";
		
		DbQuery dbQuery = new DbQuery();	
		String query = "SELECT DISTINCT classe FROM verifica WHERE codice_verifica='" + ID_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_classe = resultSet.getString("classe");
			}
			dbQuery.releaseConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return id_classe;
	}
	
	/** METODO PER L'OTTENIMENTO DELL'ID DELLA CLASSE A PARTIRE DAL NOME DELLA CLASSE (ex. 1A)
	 * 
	 * @param classe è la classe per la quale ottenere l'ID
	 * @return l'intero con l'ID della classe
	 */
	public static String getIDClasseFromStudente (String username) {
		String classe = null;
		
		//Query per ottenere il codice della classe
		String query = "SELECT classe FROM utenti WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet .next()) {
				classe = resultSet.getString("classe");
			}
			dbQuery.releaseConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return classe;
	}
	
	/** METODO PER LA VISUALIZZAZIONE DELLO STATO DI ESECUZIONE O COME SONO ANDATE LE VERIFICHE DAL DOCENTE
	 * 
	 * Struttura di visualizzazione:
	 * 
	 * +------+---------+--------------------+------------------+------+
	 * | nome | cognome | punteggio_ottenuto | punteggio_totale | voto |
	 * +------+---------+--------------------+------------------+------+
	 * 
	 * @param ID_verifica è il codice della verifica da visualizzare
	 * @return
	 */
	public ArrayList<Verifica> visualizzazioneStatoVerificaAssegnata (int ID_verifica) {
		ArrayList<Verifica> verifiche = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		//Ottenimento del nome della classe dalla verifica
		String ID_classe = getClasseFromIDOfVerifica(ID_verifica);
		
		//Prelevati dalla tabella verifica la matricola dello studente che ha fatto la verifica, il punteggio ottenuto, qeullo totale ed il voto
		String query = "SELECT punteggio_ottenuto, punteggio_totale, voto, matricola_studente "
					 + "FROM verifica "
					 + "WHERE codice_verifica='" + ID_verifica + "' AND classe='" + ID_classe + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				
				verifica.setMatricola_studente(resultSet.getInt("matricola_studente"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setVoto(resultSet.getDouble("voto"));
				
				verifiche.add(verifica);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return verifiche;
	}
	
	/** Dato il nome della classe e la matricola dello studente da aggiungere alla classe, viene fatta l'assegnazione
	 *  dello studente alla classe ponendo la matricola della classe nel campo Classe dell'utente
	 * 
	 * @param matricola_studente è la matricola dello studente da inserire
	 * @param nome_classe è il nome della classe dove inserire lo studente
	 * @param anno_scolastico è l'anno scolastico attuale
	 */
	public static void inserisciStudenteInClasse (String username, String ID_classe) {
		String query = "UPDATE utenti SET classe='" + ID_classe + "' WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		
		try {
			DbQuery.setData(query, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER CONTROLLARE SE UNO STUDENTE DI CUI SI CONOSCE LA MATRICOLA APPARTIENE O MENO AD UNA CLASSE
	 * 
	 * @param matricola_studente è la matricola dello studente da verificare
	 * @return true se lo studente appartiene ad una classe; false se non
	 */
	public boolean controlloSeStudenteAppartieneAClasse (int matricola_studente) {
		String query = "SELECT classe FROM utenti WHERE matricola='" + matricola_studente + "';";
		DbQuery dbQuery = new DbQuery();
		boolean result = true;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			String classe = null;
			
			while (resultSet.next()) {
				classe = resultSet.getString("classe");
			}
			
			if(classe == null)
				result = false;
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** METODO PER L'OTTENIMENTO DI TUTTE LE MATRICOLE DEGLI STUDENTI APPARTENENTI AD UNA DATA CLASSE
	 *  AD ECCEZZIONE DELLO STUDENTE DI SUPPORTO. VIENE USATO SOLO COME SUPPORTO NELL'ASSEGANZIONE DELLE 
	 *  VERIFICHE.
	 * 
	 * Data una classe restituisce un elenco con tutti gli studenti di quella classe
	 * @param classe è l'ID classe della quale prelevare le matricole degli studenti
	 * @return un arralist di interi contenente le matricole degli studenti della classe
	 */
	public ArrayList<Integer> getStudentiFromSpecificClass(String ID_classe){
		ArrayList<Integer> clas = new ArrayList<>();
				
		//Query per l'ottenimento degli studenti che hanno come ID_classe quello della classe indicata
		String query = "SELECT matricola FROM utenti WHERE classe='" + ID_classe + "';";
		
		DbQuery dbQuery = new DbQuery();
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
		
			while (resultSet.next()) {
				clas.add(resultSet.getInt("matricola"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return clas;
	}
	
	/** METODO PER OTTENERE IL NOME ED IL COGNOME DI UN UTENTE DATO IL SUO ID
	 * 
	 * @param id_utente è la matricola dell'utente per il quale cercare nome e cognome
	 * @return un array di due stringhe, con al primo posto il nome e al secondo il cognome
	 */
	public String[] getNomeANDCognomeFormID(int id_utente) {
		String[] nome_cognome = new String[2];
		DbQuery dbQuery = new DbQuery();
		
		//Prelevati nome e cognome dello studento che ha svolto la verifica
		String query = "SELECT nome, cognome FROM utenti WHERE matricola='" + id_utente + "'";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				nome_cognome[0] = resultSet.getString("nome");
				nome_cognome[1] = resultSet.getString("cognome");
			}
			
			dbQuery.releaseConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return nome_cognome;
	}
	
	/** METODO PER OTTENERE L'ELENCO DELLE CLASSI ESISTENTI DALLA TABELLA
	 * @return un arraylist con tutte le classi
	 */
	public static ArrayList<Classe> getElencoClassi() {
		ArrayList<Classe> classi = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		String query = "SELECT * FROM classe;";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Classe classe = new Classe();
				
				classe.setCodice_classe(resultSet.getInt("codice_classe"));
				classe.setClasse(resultSet.getString("classe"));
				classe.setAnno_scolastico(resultSet.getString("anno_scolastico"));
				
				classi.add(classe);
			}
		
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return classi;
	}
	
	/** METODO PER CONTROLLARE SE UNA CLASSE ESISTE DATI ANNO E SEZIONE
	 * 
	 * @param classe è la stringa con la classe da cercare
	 * @return 0 se la classe non esiste; un altro numero con l'ID della classe se esiste
	 */
	public static String controlloEsistenzaClasseDatoAnnoESezione(String classe) {
		String query = "SELECT codice_classe FROM classe WHERE classe='" + classe + "';";
		DbQuery dbQuery = new DbQuery();
		String ID_classe = null;	//Se rimane 0 allora la classe cercata non esiste; altrimenti ci sarà l'ID della classe
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			
			try {
				ResultSet resultSet = stat.executeQuery(query);
				
				while (resultSet .next()) {
					ID_classe = resultSet.getString("codice_classe");
				}
			} catch (SQLSyntaxErrorException e) {
				e.printStackTrace();
			}
	
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ID_classe;
	}
	
	/** METODO PER OTTENERE L'ELENCO DELLE DOMANDE PER L'ESECUZIONE DELLA VERIFICA DATO L'ID DELLA VERIFICA
	 * 
	 * @param id_verifica è l'id della verifica che si vuole eseguire
	 * @return
	 */
	public ArrayList<Domanda> getDomandeEsecuzioneVerifica(int id_verifica){
		ArrayList<Domanda> domande = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle domande della verifica indicata
		String query = "SELECT * FROM domanda WHERE ID_verifica='" + id_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Domanda domanda = new Domanda();
				
				domanda.setCodice_domanda(resultSet.getInt("codice_domanda"));
				domanda.setTesto(resultSet.getString("testo"));
				domanda.setID_verifica(resultSet.getInt("ID_verifica"));
				
				domande.add(domanda);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return domande;
	}
	
	/**	METODO PER OTTENERE LE RISPOSTE DI UNA DATA DOMANDA, CONOSCENDO L'ID DELLA DOMANDA DA CERCARE
	 * 
	 * @param id_domanda è l'id della domanda per cui cercare le risposte
	 * @return
	 */
	public ArrayList<Risposta> getRisposteEsecuzioneVerifica(int id_domanda){
		ArrayList<Risposta> risposte = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle risposta della domanda indicata
		String query = "SELECT * FROM risposta WHERE ID_domanda_di_riferimento='" + id_domanda + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Risposta risposta = new Risposta();
				
				risposta.setCodice_risposta(resultSet.getInt("ID_domanda_di_riferimento"));
				risposta.setTesto(resultSet.getString("testo"));
				risposta.setPunteggio_risposta(resultSet.getDouble("punteggio_risposta"));
				risposta.setSelezionata(resultSet.getString("selezionata"));
				risposta.setCorretta(resultSet.getString("corretta"));
				
				risposte.add(risposta);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return risposte;
	}

	/** METODO PER OTTENERE UNA VERIFICA DATO IL SUO ID
	 * 
	 * @param id_verifica è l'id della verifica da cercare
	 * @return la verifica
	 */
	public Verifica getVerificaFromID(int id_verifica) {
		Verifica verifica = new Verifica();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle verifica tramite l'ID indicato
		String query = "SELECT * FROM verifica WHERE codice_verifica='" + id_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				verifica.setCodice_verifica(resultSet.getInt("codice_verifica"));
				verifica.setMateria(resultSet.getString("materia_argomento"));
				verifica.setClasse(resultSet.getString("classe"));
				verifica.setTest_eseguito(resultSet.getString("test_eseguito"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setMatricola_docente(resultSet.getInt("matricola_docente"));
				verifica.setMatricola_studente(resultSet.getInt("matricola_studente"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifica;
	}
	
	/** METODO PER LA CREAZIONE DELLA VERIFICA DA PARTE DEL DOCENTE. VERIFICA DI RIFERIMENTO PER QUELLE DEGLI STUDENTI.
	 * 	VERRA' SALVATA SULLO STUDENTE DI SUPPORTO DELLA CLASSE.
	 * 
	 * Viene creata la verifica di riferimento in base alla quale si creerano quelle degli studenti
	 * @param materia è la materia della verifica
	 * @param argomento è l'argomento della verifica
	 * @param classe è la classe a cui verrà assegnata la verifica
	 * @param matricola_docente è l'ID del docente che ha creato la verifica
	 * return l'ID della verifica appena creata.
	 */
	public int creazioneVerificaIniziale(String materia, String argomento, String classe, int matricola_docente, int matricola_studente) {
		DbQuery dbQuery = new DbQuery();
		String materia_argomento = materia + " - " + argomento;
		String query = "";
		String query2 = "";
		
		if(matricola_studente == -9876) {
			int id_studenteSupporto = getIDStudenteDiSupportoDellaClasse(classe);
			query = "INSERT INTO verifica (materia_argomento, classe, test_eseguito, punteggio_ottenuto, punteggio_totale, voto, matricola_docente, matricola_studente) "
				 + "VALUES "
				 + "('" + materia_argomento + "', '" + classe + "', '" + "NO" + "', '" + 0.0 + "', '" + 0.0 + "', '" + 0 + "', '" + matricola_docente + "', '" + id_studenteSupporto + "');";
			query2 = "SELECT codice_verifica FROM verifica WHERE materia_argomento='" + materia_argomento + "' AND classe='" + classe + "' AND test_eseguito='" + "NO" + "' AND punteggio_ottenuto='" + 0.0
					+ "' AND punteggio_totale='" + 0.0 + "' AND voto='" + 0 + "' AND matricola_docente='" + matricola_docente + "' AND matricola_studente='" + id_studenteSupporto + "';";
		}else {
			query = "INSERT INTO verifica (materia_argomento, classe, test_eseguito, punteggio_ottenuto, punteggio_totale, voto, matricola_docente, matricola_studente) "
					 + "VALUES "
					 + "('" + materia_argomento + "', '" + classe + "', '" + "NO" + "', '" + 0.0 + "', '" + 0.0 + "', '" + 0 + "', '" + matricola_docente + "', '" + matricola_studente + "');";
			query2 = "SELECT codice_verifica FROM verifica WHERE materia_argomento='" + materia_argomento + "' AND classe='" + classe + "' AND test_eseguito='" + "NO" + "' AND punteggio_ottenuto='" + 0.0
					+ "' AND punteggio_totale='" + 0.0 + "' AND voto='" + 0 + "' AND matricola_docente='" + matricola_docente + "' AND matricola_studente='" + matricola_studente + "';";
		}	
		int ID_verifica = 0;
		
		try {
			DbQuery.setData(query, true);
			
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query2);
			
			while (resultSet.next()) {
				ID_verifica = resultSet.getInt("codice_verifica");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return ID_verifica;
	}
	
	/** METODOD PER OTTENERE, DATO L'ID DI UNA CLASSE, L'ID DELL'ACCOUNT STUDENTE DI 
	 * 	SUPPORTO DALLA CLASSE.
	 * 
	 * @param id_classe è l'id della classe da dove prelevare lo studente di supporto
	 * @return l'id dello studente di supporto
	 */
	public int getIDStudenteDiSupportoDellaClasse (String classe) {
		DbQuery dbQuery = new DbQuery();
		int id_studente_supp = 0;
		String query = "SELECT matricola FROM utenti WHERE classe='" + classe + "' "
				+ "AND nome='" + classe.charAt(0) + "' AND cognome='" + classe.charAt(1) + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_studente_supp = resultSet.getInt("matricola");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return id_studente_supp;
	}
	
	/** METODO PER L'ASSEGNAZIONE DELLA VERIFICA INDICATA AGLI STUDENTI DI UNA CLASSE
	 * 
	 * Il metodo estrae le domande della veridica, le salva in un arraylist;
	 * 			 estrae le risposte di ciascuna domanda;
	 *			 crea un elenco con gli ID delle nuove verifiche che vengono assegnate agli studenti 
	 *			 della classe indicata.
	 * 
	 * 			 Con questo ulteriore elemento si passa alla creazione di una nuova domanda, con 
	 * 			 stessi elementi della precedente ma diverso ID_verifica, che sarà quello della verifica
	 * 		 	 assegnata allo studente.
	 * 
	 * 			 In questo modo si creereranno anche le risposte per ciascuna domanda.
	 * 
	 * @param materia è la materia della verifica
	 * @param argomento è l'argomento della verifica
	 * @param domande_assegnate è il numero di domande che ciascuno studente dovrà eseguire
	 * @param classe è la classe a cui verrà assegnata la verifica
	 * @param matricola_docente è la matricola del docente che ha creato la verifica
	 * @param id_verifica_da_assegnare è l'id della verifica originale e che si deve assegnare agli studenti
	 */
	public void assegnazioneVerificaAllaClasse (String materia, String argomento, int domande_assegnate, String classe, int matricola_docente, int id_verifica_da_assegnare) {
		//Ottenimento delle matricole degli studenti della classe
		ArrayList<Integer> ID_studenti = getStudentiFromSpecificClass(classe);
		//Elenco degli ID delle verifiche di ciascuno studente
		ArrayList<Integer> ID_nuove_verifiche_studenti = new ArrayList<>();
				
		for(int i = 0; i < ID_studenti.size(); i++) {
			//Assegnazione delle verifiche a ciascuno studente e ottenimento ID
			ID_nuove_verifiche_studenti.add(creazioneVerificaIniziale(materia, argomento, classe, matricola_docente, ID_studenti.get(i)));	//duplicazione della verifica in una nuova
		}
		
		DbQuery dbQuery = new DbQuery();
					
		//query per ottenere le domande della verifica tramite id e loro duplicazione con nuovo id verifica -> output in ArrayList<Domanda>
		String query_getDomande = "SELECT * FROM domanda WHERE ID_verifica='" + id_verifica_da_assegnare + "';";
			
		ArrayList<Domanda> domande = new ArrayList<>();
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query_getDomande);
				
			while (resultSet.next()) {
				Domanda d = new Domanda();
					
				d.setCodice_domanda(resultSet.getInt("codice_domanda"));
				d.setTesto(resultSet.getString("testo"));
				d.setID_verifica(resultSet.getInt("ID_verifica"));
					
				domande.add(d);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		//Scelta casuale delle domande e poste in un nuovo elenco
		domande = preparaDomandePerStudente(domande, domande_assegnate);
			
		for (int j = 0; j < domande.size(); j++) {
			//Ottenimento delle risposte per la domanda a partire dall'id di questa
			ArrayList<Risposta> risposte = getRisposteEsecuzioneVerifica(domande.get(j).getCodice_domanda());
		
			//------INIZIO FASE DI DUPLICAZIONE DELLE DOMANDE E RISPOSTE PER CIASCUNO STUDENTE DELLA CLASSE--------//
			for(int i = 0; i < ID_nuove_verifiche_studenti.size(); i++) {	
				//duplicazione domanda con nuovo id verifica=quello della verifica dello studente
				int ID_domanda_duplicata = creaDomanda(domande.get(j).getTesto(), ID_nuove_verifiche_studenti.get(i));
				
				for(int k = 0; k < risposte.size(); k++) {	
					//duplicazione risposta con nuovo id domanda=quello della domanda della verifica dello studente
					creaRisposta(risposte.get(k).getTesto(), risposte.get(k).getPunteggio_risposta(), risposte.get(k).isCorretta(), ID_domanda_duplicata);
				}
			}
		}			
	}
	
	/**Metodo per la preparazione delle domande per ciascuno studente: vengono prese in modo casuale
	 * tante domande quante il docente ha indicato che vengano assegnate agli studenti.
	 * 
	 * @param domande è l'elenco delle domande da dove prelevare in modo casuale
	 * @param nr_domande è il numero di domande da prendere dall'elenco
	 * @return l'elenco con le nuove domande preparate in modo casuale
	 */
	public ArrayList<Domanda> preparaDomandePerStudente (ArrayList<Domanda>domande, int nr_domande) {
		ArrayList<Domanda> domandeXstudente = new ArrayList<>();
		//Inserimento domande
		for(int i = 0; i < nr_domande; i++) {
			int x = (int)(Math.random() * domandeXstudente.size());
			//Controllo domanda già inserita
			while(controlloPresenzaDomandaInArrayList(domandeXstudente, domande.get(x).getCodice_domanda())) {
				x = (int)(Math.random() * domandeXstudente.size() + 1);
			}
			domandeXstudente.add(domande.get(x));
		}
		return domandeXstudente;
	}
	
	/**Controllo se nell'elenco delle domande è stata già inserita una specifica domanda identificata dall'id
	 * 
	 * @param domande è l'elenco delle domande in cui effettuare il controllo
	 * @param id_domanda è l'id della domanda della quale verificare la presenza nell'elenco
	 * @return true se esiste già la domanda; false se non esiste.
	 */
	public boolean controlloPresenzaDomandaInArrayList (ArrayList<Domanda>domande, int id_domanda) {
		int i = 0;
		while(i < domande.size()) {
			if(domande.get(i).getCodice_domanda() == id_domanda)
				return true;
			i++;
		}
		return false;
	}

}// :~ class