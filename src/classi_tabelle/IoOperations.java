package classi_tabelle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.*;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**ELENCO TABELLE: 
 * - classi			-> tabella con l'insieme degli studenti di una classe
 * - utenti 		-> tabella generale con admin, studenti, docenti : ordinati in ordine crescente in base alla matricola
 * - verifiche		-> tabella dei test generali dei docenti: possono creare una nuova verifica e
 * 					assegnarla ad una classe. Ciascun docente vede le proprie verifiche
 * - domande		-> tabella elenco di tutte le domande
 * - risposte		-> tabella elenco di tutte le risposte
 * 
 * 
 * FUNZIONAMENTO DEL COLLEGAMENTO TRA TABELLE:
 * 
 * 1) Da "utenti" si puo accedere alle verifiche personali nel caso si fosse uno studente, quindi ad una propria tabella "verifiche";
 * 2) Da "utenti" si puo accedere alla tabella "verifiche" di tutti i docenti, essendo un docente: da qui si puo creare una nuova 
 * verifica collegandosi dunque alla tabella doamnde e poi a quella risposte nella creazione della verifica;
 * 3) Una volta creata una verifica questa verr� potr� essere assegnata ad una classe e automaticamente a tutti gli studenti che appartengono ad essa
 * 
 * @author Ciprian Agafitei
 *
 */
public class IoOperations {

	/**	METODO PER IL LOGIN ALLA TABELLA UTENTI
	 * 
	 * Metodo per il controllo che un utente sia registrato nel database date le sue credenziali
	 * @param usr � il primo fattore delle credenziali necessarie per accedere: USERNAME
	 * @param pass � il secondo fattore delle credenziali necessarie per accedere: PASSWORD
	 * @return false se l'utente non � registrato nel database; true se lo �.
	 * 
	 * IL DATABASE IN CUI SI CERCA � QUELLO GENERALE di tutti gli utenti.
	 */
	public static boolean login (String usr, String pass) {
		pass = DigestUtils.sha1Hex(pass);
		String query = "SELECT * FROM utente WHERE username='" + usr + "' AND password='" + pass + "'";
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
	 * @param username � l'username dell'utente da cercare
	 * @param password � la password dell'utente da cercare
	 * @return 1 se si tratta di ADMIN; 2 se si tratta di DOCENTE; 3 se si tratta di STUDENTE.
	 */
	public static String getTipologiaUtente (String username) {
		String query = "SELECT tipologia FROM utente WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		String tipologia = "";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet .next()) {
				tipologia = resultSet.getString("tipologia");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tipologia;
	}
	
	/** METODO PER L'OTTENIMENTO DELLA MATRICOLA UTENTE DATO USERNAME
	 * 
	 * Dato l'username si cerca nella tabella la riga corrispondente e se ne preleva la MATRICOLA
	 * @param username � l'username dell'utente da cercare
	 * @return un intero che rappresenter� la matricola dell'utente
	 */
	public static int getIDFromUsername (String username) {
		String query = "SELECT matricola FROM utente WHERE username='" + username + "'";
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
	
	/**METODO PER VERIFICARE L'ESISTENZA DI UN UTENTE DATO L'USERNAME
	 * @param username � l'username da ricercare
	 * @return
	 */
	public static boolean controlloEsistenzaUsername(String username) {
		//Query per verificare l'esistenza dell'utente
		String query = "SELECT * FROM utente WHERE username='" + username + "';";
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
	
	/** METODO PER LA CREAZIONE DI UN NUOVO ACCOUNT per la tabella UTENTI
	 * 
	 * Metodo che dato nome e cognome crea l'username per il nuovo account.
	 * L'username sar� composto da:
	 * 								nome.cognome
	 * 
	 * Nel caso l'username sia gi� presente si proceder� ad aggiungere in coda
	 * un numero progressivo.
	 * 
	 * @param nome da inserire per l'username
	 * @param cognome da inserire nell'username 
	 * return un array con 2 stringhe:
	 * 			 un boolean: true se la registrazione � andata a buon fine; false se ci sono stati problemi
	 * 			 una stringa: contenente l'username del nuovo utente registrato
	 */
	public static String[] createAccount (String nome, String cognome, int tipologia, String email) {		
		String[]array = new String[2];
		boolean is_registrazione_senza_errori = true;	
		String username = nome.toLowerCase() + "." + cognome.toLowerCase();
		String password = null;
		
		//Se l'username esiste gi�, si cerca il prossima username disponibile tramite un numero progressivo
		if(controlloEsistenzaUsername(username)) {
			
			int supporto = 1;
			while(controlloEsistenzaUsername(username + supporto)) {
				supporto++;
			}
			username += supporto;
		}
		
		String tipologia_utente;
		if(tipologia == 1)
			tipologia_utente = "ADMIN";
		else if(tipologia == 2)
			tipologia_utente = "TEACHER";
		else
			tipologia_utente = "STUDENT";
		
		//Query per la creazione di un nuovo account
		String query = "INSERT INTO utente (nome, cognome, username, password, tipologia, email)"
+ " VALUES ('" + nome + "', '" + cognome + "', '" + username + "', '" + password + "', '" + tipologia_utente + "', '" + email + "');";
		
		DbQuery dbQuery = new DbQuery();
		
		try {
			//Creazione account
			if(!DbQuery.setData(query, true)) {
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
	
	/**METODO PER COMPLETARE LA REGISTRAZIONE DI UN UTENTE AL SISTEMA. VIENE AGGIORNATA LA PASSWORD
	 * E A QUESTA SI APPLICATA L'ARGORITMO DI HASHING SHA-1
	 * @param username dell'utente che vuole terminare la registrazione
	 * @param password da aggiornare
	*/
	public static void terminaRegistrazioneUtente(String username, String password) {
		password = DigestUtils.sha1Hex(password);
		String query = "UPDATE utente SET password='" + password + "' WHERE username='" + username + "';";
		
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO CHE DATO l'ID DI UNO STUDENTE NE RESTITUISCE L'USERNAME
	 * 
	 * @param ID_user � la matricola dello studente
	 * @return
	 */
	public static String getUsernameFromIDUser (int ID_user) {
		String query = "SELECT username FROM utente WHERE matricola='" + ID_user + "';";
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
	
	/** METODO PER RESETTARE LA PASSWORD DI UN UTENTE DATO
	 * Dato un sername viene azzerrata la password per tale utente
	 * @param username � l'username dell'utente da modificare
	 */
	public static void resetPassword(String username) {
		String nuovaPassword = null;
		String query = "UPDATE utente SET password='" + nuovaPassword + "' WHERE username='" + username + "';";
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
	
	/** Metodo per ottenere l'email di un utente dato il suo id
	 * @param id_user � l'id dell'utente per cui ottenere l'indirizzo email
	 * @return una stringa con l'indirizzo email dell'utente indicato
	 */
	public static String getEmailFromIDUser(int id_user) {
		DbQuery dbQuery = new DbQuery();
		
		String email_utente = "";
		
		String query = "SELECT email FROM utente WHERE matricola='" + id_user + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
				
			//Ottenimento elenco domande della verifica
			while (resultSet.next()) {
				email_utente = resultSet.getString("email");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return email_utente;
	}

	/**METODO PER RENDERE MAIUSCOLE LE INIZIALI DEI NOMI E COGNOMI
	 * @param nome � il nome/cognome per il quale cambiare le iniziali
	 * @return una stringa con il nome che avr� le iniziali cambiate
	 */
	public static String rendiMaiuscolaPrimaLetteraNomi(String nome) {
		String nuova_stringa = "";
		int i = 0;
		char ch;
		while(i < nome.length()) 
		{
			ch = nome.charAt(i);
			String sup = "";
			if(i == 0 || (nome.charAt(i - 1) == ' ')) 
			{
				sup += ch;
				nuova_stringa += sup.toUpperCase();
			}
			else
				nuova_stringa += ch;
			i++;
		}
		return nuova_stringa;
	}
		
	/**Metodo che restituisce tutti gli utenti del database utenti
	 * @return un ArrayList con tutti gli utenti del database
	 */
	public static ArrayList<Utente> getUtenti () {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utente;";	//Visualizzazione di tutti gli utenti della tabella
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
				utente.setEmail(resultSet.getString("email"));
				utente.setClasse(resultSet.getInt("id_classe"));
				
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
		String query = "SELECT * FROM utente WHERE tipologia='STUDENTE' OR tipologia='STUDENT';";	//Visualizzazione di tutti gli utenti della tabella
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
				utente.setClasse(resultSet.getInt("id_classe"));
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
	
	/**Metodo che restituisce tutti i docenti del database utenti
	 * @return un ArrayList con tutti i docenti del database
	 */
	public static ArrayList<Utente> getDocenti() {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utente WHERE tipologia='DOCENTE' OR tipologia='TEACHER';";	//Visualizzazione di tutti gli utenti della tabella
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
				utente.setClasse(resultSet.getInt("id_classe"));
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
	
	/** METODO PER OTTENERE L'ELENCO DELLE MATERIE E I RELATIVI DOCENTI ASSEGNATI
	 * @return un arraylist con le materie e i docenti
	 */
	public static ArrayList<Materia_Docenti> getElencoMaterieDocenti() {
		ArrayList<Materia_Docenti> elenco = new ArrayList<Materia_Docenti>();
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT * FROM materie_docenti;";
			
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			elenco = new ArrayList<Materia_Docenti>();

			while (resultSet.next()) {
				Materia_Docenti materia_docente = new Materia_Docenti();
				materia_docente.setId_materia_docenti(resultSet.getInt("id_materia_docente"));
				materia_docente.setMateria(resultSet.getString("materia"));
				materia_docente.setId_docente(resultSet.getInt("id_docente"));
				elenco.add(materia_docente);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elenco;
	}
	
	/** METODO PER OTTENERE L'ELENCO DELLE RIGHE DELLA TABELLA MATERIE_DOCENTI DI UN DATO DOCENTE
	 * @param id_docente � l'id del docente per il quale ottenere le righe richieste
	 * @return l'elenco delle righe
	 */
	public static ArrayList<Materia_Docenti> getElencoMaterieDocente(int id_docente) {
		ArrayList<Materia_Docenti> elenco = new ArrayList<Materia_Docenti>();
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT * FROM materie_docenti WHERE id_docente='" + id_docente + "';";
			
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			elenco = new ArrayList<Materia_Docenti>();

			while (resultSet.next()) {
				Materia_Docenti materia_docente = new Materia_Docenti();
				materia_docente.setId_materia_docenti(resultSet.getInt("id_materia_docente"));
				materia_docente.setMateria(resultSet.getString("materia"));
				materia_docente.setId_docente(resultSet.getInt("id_docente"));
				elenco.add(materia_docente);
			}
			dbQuery.releaseConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elenco;
	}
	
	/**METODO PER OTTENERE L'ELENCO DEGLI STUDENTI CHE APPARTENGONO AD UNA CLASSE
	 * @return un arraylist con gli studenti che appartengono ad una classe
	 */
	public static ArrayList<Utente> getStudentiCheAppartengonoAdUnaClasse() {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utente WHERE (tipologia='STUDENTE' OR tipologia='STUDENT') AND id_classe IS NOT NULL;";	//Visualizzazione di tutti gli utenti della tabella
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
				utente.setClasse(resultSet.getInt("id_classe"));
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
	
	/**METODO PER OTTENERE L'ELENCO DEGLI STUDENTI CHE NON APPARTENGONO AD UNA CLASSE
	 * @return un arraylist con gli studenti che non appartengono ad una classe
	 */
	public static ArrayList<Utente> getStudentiCheNONAppartengonoAdUnaClasse() {
		ArrayList<Utente> utenti = null;
		String query = "SELECT * FROM utente WHERE (tipologia='STUDENTE' OR tipologia='STUDENT') AND id_classe IS NULL;";	//Visualizzazione di tutti gli utenti della tabella
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
				utente.setClasse(resultSet.getInt("id_classe"));
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
	public static ArrayList<Verifica> visualizzaVerificheSuDocenti (int id_docente) {		
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_utente='" + id_docente + "';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();
			
			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
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
	
	/** METODO CHE RESTITUISCE TUTTE LE VERIFICHE DI UN DATO DOCENTE DATO IL SUO ID
	 * @param id_docente � l'id del docente per il quale ottenere le verifiche
	 * @return un ArrayList con tutte le verifiche del docente che non ha assegnato
	 */
	public static ArrayList<Verifica> visualizzaVerificheSuDocentiNonAssegnate (int id_docente) {		
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_utente='" + id_docente + "' AND is_assegnata='NO';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();
			
			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
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
	
	/**Metodo che restituisce tutte le verifiche di un dato studente. 
	 * @param matricola � la matricola dello studente per il quale ottenere la verifica
	 * @return un ArrayList con tutte le verifiche dello studente
	 */
	public static ArrayList<Verifica> getVerifiche (int matricola) {
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_utente='" + matricola + "';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
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
	
	/**	METODO PER OTTENERE L'ELENCO DELLE VERIFICHE CHE LO STUDENTE NON HA ANCORA ESEGUITO
	 * @param matricola_studente dello studente che vuole vedere le verifiche che pu� eseguire
	 * @return un elenco con le verifiche non ancora eseguite
	 */
	public static ArrayList<Verifica> getVerificheNonEseguiteStudente (int matricola_studente) {	
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_utente='" + matricola_studente + "' AND is_eseguita='NO';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
				
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
	
	/**	METODO PER OTTENERE L'ELENCO DELLE VERIFICHE CHE LO STUDENTE HA ESEGUITO
	 * @param matricola_studente dello studente che vuole vedere le verifiche che ha fatto
	 * @return un elenco con le verifiche eseguite
	 */
	public static ArrayList<Verifica> getVerificheEseguiteStudente (int matricola_studente) {	
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_utente='" + matricola_studente + "' AND is_eseguita='SI';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
				
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
	public static ArrayList<Domanda> getDomande () {
		ArrayList<Domanda> domande = null;
		String query = "SELECT * FROM domanda;";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			domande = new ArrayList<Domanda>();

			while (resultSet.next()) {
				Domanda domanda = new Domanda();
				domanda.setId_domanda(resultSet.getInt("id_domanda"));
				domanda.setTesto(resultSet.getString("testo"));
				domanda.setMateria(resultSet.getString("materia"));
				domanda.setId_immagine(resultSet.getInt("id_immagine"));
				domanda.setId_verifica(resultSet.getInt("id_verifica"));
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
	
	/** METODO CHE DATA UNA MATERIA NE RESTITUISCE TUTTE LE DOMANDE POSSIBILI ED ESISTENTI
	 * @param materia � la materia per cui cercare le domande
	 * @return l'elenco delle domande di una data materia ricercata
	 */
	public static ArrayList<Domanda> getDomandeFromMateria (String materia) {
		ArrayList<Domanda> domande = null;
		String query = "SELECT * FROM domanda WHERE materia='" + materia + "';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			domande = new ArrayList<Domanda>();

			while (resultSet.next()) {
				Domanda domanda = new Domanda();
				domanda.setId_domanda(resultSet.getInt("id_domanda"));
				domanda.setTesto(resultSet.getString("testo"));
				domanda.setMateria(resultSet.getString("materia"));
				domanda.setId_immagine(resultSet.getInt("id_immagine"));
				domanda.setId_verifica(resultSet.getInt("id_verifica"));
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
	
	/**METODO PER LA RIMOZIONE DI TUTTE LE vERIFICHE DI UN DATO STUDENTE
	 * @param username � l'username dello studente per cui rimuovere le verifiche
	 */
	public static void rimuoviVerificheStudente(String username) {
		DbQuery dbQuery = new DbQuery();
		
		//Ottenimento id dato l'username
		int id_studente = getIDFromUsername(username);
				
		//Ottenimento dell'elenco delle verifiche dello studente
		ArrayList<Verifica> elenco_verifiche = getVerifiche(id_studente);
		
		try {
			//rimozione verifiche utente
			String query_RV;
			int i = 0;
			while(i < elenco_verifiche.size()) {
				
				//Rimozione domande della verifica 
				rimuoviDomandeVerifica(elenco_verifiche.get(i));
				
				query_RV = "DELETE FROM verifica WHERE id_verifica='" + elenco_verifiche.get(i).getId_verifica() + "';";
				DbQuery.setData(query_RV, true);
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**METODO PER LA RIMOZIONE DI TUTTE LE DOMANDE DELLA VERIFICA DELLO STUDENTE
	 * @param verifica � la verifica da cui rimuovere le domande
	 */
	public static void rimuoviDomandeVerifica(Verifica verifica) {
		DbQuery dbQuery = new DbQuery();
		
		//Ottenimento dell'elenco delle domande di tutte le verifiche dello studente
		ArrayList<Domanda> elenco_domande = new ArrayList<Domanda>();
		elenco_domande.addAll(getDomandeVerifica(verifica.getId_verifica()));

		for(int i = 0; i < elenco_domande.size(); i++) {
			rimuoviRisposteDomandaVerifica(elenco_domande.get(i));
		}
		
		//rimozione verifiche utente
		String query_RD = "DELETE FROM domanda WHERE id_verifica='" + verifica.getId_verifica() + "';";
		try {
			DbQuery.setData(query_RD, true);
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER LA RIMOZIONE DELLA DOMANDA DAL DATABASE
	 * @param domanda � la domanda da rimuovere
	 */
	public static void rimuoviDomanda(Domanda domanda) {
		DbQuery dbQuery = new DbQuery();
		
		// Rimozione delle risposte della domanda
		rimuoviRisposteDomandaVerifica(domanda);
		
		//rimozione verifica dal database
		String query = "DELETE FROM domanda WHERE id_domanda='" + domanda.getId_domanda() + "';";
		
		try {
			DbQuery.setData(query, true);
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**METODO PER LA RIMOZIONE DI TUTTE LE RISPOSTE DELLA DOMANDA SELEZIONATA
	 * @param domanda � la domanda per la quale rimuovere tutte le risposte
	 */
	public static void rimuoviRisposteDomandaVerifica(Domanda domanda) {
		DbQuery dbQuery = new DbQuery();
		
		//rimozione verifiche utente
		String query_RD = "DELETE FROM risposta WHERE id_domanda='" + domanda.getId_domanda()+ "';";
		try {
			DbQuery.setData(query_RD, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER LA RIMOZIONE DI UN UTENTE DALLA TABELLA SELEZIONATA
	 * 
	 * Dato l'username dell'utente che si vuole rimuovere viene eseguita tale query sulla tabella degli utenti
	 * 
	 * @param username dell'utente da rimuovere
	 */
	public static void removeUser(String username) {
		DbQuery dbQuery = new DbQuery();
		String query = "DELETE FROM utente WHERE username='" + username + "';";
		
		if(getTipologiaUtente(username).equalsIgnoreCase("STUDENTE") || getTipologiaUtente(username).equalsIgnoreCase("STUDENT")) {
			try {
				rimuoviVerificheStudente(username);
			}catch(NullPointerException npe) {}
		}	
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
	 * Viene inoltre aggiornato il numero di studenti della classe
	 * @param matricola_utente_da_rimuovere_int � l'ID dello studente da rimuovere
	 */
	public static void removeStudentFromClass(String username) {
		String query = "UPDATE utente SET id_classe=NULL WHERE username='" + username + "';";	//Indica che lo studente non appartiene ad una classe
		DbQuery dbQuery = new DbQuery();
		
		try {
			// Ottenimento id classe dello studenteda rimuovere
			int id_classe = getIdClasseStudente(username);
			
			// Lo studente viene rimosso dalla classe
			DbQuery.setData(query, true);
			
			// Ottenimento numero attuale di studenti della classe
			int numero_studenti = contaStudentiClasse(id_classe);
			
			//Aggiornamento del valore di numero studenti della classe da cui si ha rimosso l'utente
			String query1 = "UPDATE classe SET nr_studenti='" + numero_studenti + "' WHERE id_classe='" + id_classe + "';";
			DbQuery.setData(query1, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**METODO PER CONTARE IL NUMERO DI STUDENTI PER UNA CLASSE
	 * @param classe dove si vuole contare il numero di studenti
	 * @return
	 */
	public static int contaStudentiClasse(int id_classe){
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT COUNT(*) FROM utente WHERE id_classe='" + id_classe + "';";
		
		int studenti = 0;
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				studenti = resultSet.getInt(1);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return studenti;
	}
	
	/**METODO PER CONTARE IL NUMERO DI DOMANDE PER UNA VERIFICA
	 * @param verifica dove si vuole contare il numero di domande
	 * @return
	 */
	public static int contaDomandeVerifica(Verifica v){
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT COUNT(*) FROM domanda WHERE id_verifica='" + v.getId_verifica() + "';";
		
		int domande = 0;
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				domande = resultSet.getInt(1);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return domande;
	}
	
	/**METODO CHE DATO L'USERNAME DI UNO STUDENTE RESTITUISCE LA SUA CLASSE DI APPARTENENZA
	 * @param username individua lo studente per il quale si vuole conoscere la classe
	 * @return la stringa con la classe
	 */
	public static int getIdClasseStudente(String username) {
		int id_classe = 0;
		String query = "SELECT id_classe FROM utente WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				id_classe = resultSet.getInt("id_classe");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id_classe;
	}
	
	/** METODO PER LA CREAZIONE DI UNA RISPOSTA DATE LE INFORMAZIONI DAL DOCENTE
	 * 
	 * Metodo che dato il testo, il punteggio nel caso fosse corretta, punteggio nel caso fosse erratta e definito se
	 * la risposta � corretta in riferimento alla domanda, viene creata ed inserita nella tabella
	 * 
	 * @param testo � il testo della risposta
	 * @param punteggio_risposta � il punteggio che verr� assegnato alla risposta nella correzione:
	 * 				positivo se la risposta � corretta; negativo se � erratta.
	 * @param isCorretta indica se la risposta inserita � quella o una di quelle corrette per la domanda
	 * return la matricola della risposta dalla tabella risposte
	 */
	public static int creaRisposta (String testo, double punteggio_risposta, String isCorretta, int ID_domanda, int id_immagine) {
		int id_risposta = 0;
				
		/*Controllo se la risposta � proveniente dalla creazione: true e flse si riferiscono al fatto che
		 * sia stato selezionato il pulsante che indica se la risposta � corretta o meno*/
		if(isCorretta.equals("false")) {
			isCorretta = "NO";
		}else if(isCorretta.equals("true")){
			isCorretta = "SI";
		}
		
		//Inserimento nella tabella
		String query;
		
		if(id_immagine > 0)
			query = "INSERT INTO risposta (testo, id_immagine, punteggio_risposta, is_selezionata, is_corretta, id_domanda) VALUES "
		+ "('" + testo + "', '" + id_immagine + "', '" + punteggio_risposta + "', '" + "NO" + "', '" + isCorretta + "', '" + ID_domanda + "')"; 
		else
			query = "INSERT INTO risposta (testo, id_immagine, punteggio_risposta, is_selezionata, is_corretta, id_domanda) VALUES "
					+ "('" + testo + "', NULL, '" + punteggio_risposta + "', '" + "NO" + "', '" + isCorretta + "', '" + ID_domanda + "')"; 
			
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id_risposta;
	}
	
	/** METODO PER LA CREAZIONE DI UNA DOMANDA DATO IL TESTO E IL CODICE DELLA VERIFICA
	 * Dato il testo e il codice della verifica ,
	 * si crea una domanda che verr� inserita nella tabella di tutte le domande.
	 * @param testo � il testo della domanda
	 * @param id_verifica con l'id di riferimento della verifica in cui verr� insertia la domanda
	 * @return l'identificativo della domanda appena creata
	 */
	public static int creaDomanda (String testo, String materia, int id_verifica, int id_immagine) {
		int id_domanda = 0;
		String query;
		// Ottenimento dell'id dell'ultima domanda inserita che equivale a quella appena creata
		String query2 = "SELECT id_domanda FROM domanda ORDER BY id_domanda DESC LIMIT 1;";
		DbQuery dbQuery = new DbQuery();
		
		if(id_verifica > 0 && id_immagine > 0) {
			//Inserimento nella tabella in caso si conosca l'id della verifica e ci sia un'immagine
			query = "INSERT INTO domanda (testo, materia, id_immagine, id_verifica) VALUES " + "('" + testo + "', '" + materia + "', '" + id_immagine + "', '" + id_verifica + "')"; 
		}
		else if(id_verifica > 0 && id_immagine == 0) {
			//Inserimento nella tabella in caso si conosca l'id della verifica senza presenza di immagini
			query = "INSERT INTO domanda (testo, materia, id_immagine, id_verifica) VALUES " + "('" + testo + "', '" + materia + "', NULL, '" + id_verifica + "');"; 
		}
		else if(id_verifica == 0 && id_immagine == 0) {
			//Inserimento nella tabella in caso non si conosca l'id e non ci sia nessuna immagine
			query = "INSERT INTO domanda (testo, materia, id_immagine, id_verifica) VALUES " + "('" + testo + "', '" + materia + "', NULL, NULL);"; 
		}
		else {
			//Inserimento nella tabella nel caso ci sia un'immagine
			query = "INSERT INTO domanda (testo, materia, id_immagine, id_verifica) VALUES " + "('" + testo + "', '" + materia + "', '" + id_immagine + "', NULL);"; 
		}
		
		try {
			if(DbQuery.setData(query, true)) {
				Connection conn = DbSingleConn.getConnection();
				Statement stat = conn.createStatement();
				ResultSet resultSet = stat.executeQuery(query2);
		
				while (resultSet .next()) {
					id_domanda = resultSet.getInt("id_domanda");
				}
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id_domanda;
	}
			
	/** METODO PER LA CREAZIONE DI UNA TABELLA CLASSE CON NOME L'ANNO E LA SEZIONE
	 * Viene creata una nuova classe e una tabella nel database con il nome di questa l'anno e la sezione della classe.
	 * @param classe � l'insieme composto da anno e sezione della classe
	 * @param codici_tabella_studenti � l'insieme delle matricole degli studenti che vi appartengono
	 * @param anno_scolastico � l'anno di svolgimento scolastico
	 */
	public static void creaClasse (String classe, String anno_scolastico) {		
		String query = "INSERT INTO classe (classe, anno_scolastico, nr_studenti) VALUES ('" + classe + "', '" + anno_scolastico + "', " + 0 + ");";
		DbQuery dbQuery = new DbQuery();
		try {
			DbQuery.setData(query, true);
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Se la classe � stata inserita pi� volte
		if(!controlloNumeroPresenzaClasseInTabella(classe)) 
		{
			query = "DELETE FROM classe WHERE classe='" + classe + "';";
			try {
				DbQuery.setData(query, true);
				dbQuery.releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** METODO PER CONTROLLARE QUANTE VOLTE E' STATA INSERITA LA CLASSE NEL DATABASE,
	 *  in caso di bug in cui dovesse essere stata inserita pi� volte
	 * @param classe � la classe da verificare
	 * @return true se � stata inserita una sola volta; false se pi� volte
	 */
	public static boolean controlloNumeroPresenzaClasseInTabella(String classe) {
		String query = "SELECT COUNT(*) FROM classe WHERE classe='" + classe + "';";
		DbQuery dbQuery = new DbQuery();
		int ripetizioni = 0;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			// Ottenimento numero di volte che la classe � stata inserita nella tabella
			while (resultSet.next()) {
				ripetizioni = resultSet.getInt(1);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(ripetizioni == 1)
			return true;
		return false;
	}
	
	/** METODO PER L'OTTENIMENTO DEL NOME DELLA CLASSE DATO l'ID DELLA VERIFICA
	 * Viene cercata la verifica tramite l'ID e poi si preleva la classe a cui �
	 * stata assegnata.
	 * @param ID_verifica � l'ID della verifica per cui cercare la classe
	 * @return la stringa con il nome della classe
	 */
	public static Classe getClasseFromIDOfVerifica (int ID_verifica) {
		Classe classe = new Classe();
		int id_classe = 0;
		
		DbQuery dbQuery = new DbQuery();	
		String query = "SELECT id_classe FROM verifica WHERE id_verifica='" + ID_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_classe = resultSet.getInt("id_classe");
			}
			
			String query2 = "SELECT * FROM classe WHERE id_classe='" + id_classe + "';";
			resultSet = stat.executeQuery(query2);
			
			while (resultSet.next()) {
				classe.setId_classe(resultSet.getInt("id_classe"));
				classe.setClasse(resultSet.getString("classe"));
				classe.setAnno_scolastico(resultSet.getString("anno_scolastico"));
				classe.setNumero_studenti(resultSet.getInt("nr_studenti"));
			}
			
			dbQuery.releaseConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classe;
	}
	
	/** METODO PER OTTENERE LA CLASSE DATO L'ID
	 * @param ID_classe � l'id della classe per la quale ottenere anno e sezione
	 * @return una stringa con la classe
	 */
	public static String getClasseFromIDClasse(int ID_classe) {
		String classe = "";
		
		DbQuery dbQuery = new DbQuery();	
		String query = "SELECT classe FROM classe WHERE id_classe='" + ID_classe + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
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
	
	/** METODO PER L'OTTENIMENTO DELL'ID DELLA CLASSE A PARTIRE DAL NOME DELLA CLASSE (ex. 1A)
	 * 
	 * @param classe � la classe per la quale ottenere l'ID
	 * @return l'intero con l'ID della classe
	 */
	public static int getIDClasseFromStudente (String username) {
		int id_classe = 0;
		
		//Query per ottenere il codice della classe
		String query = "SELECT id_classe FROM utente WHERE username='" + username + "';";
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
	
			while (resultSet.next()) {
				id_classe = resultSet.getInt("id_classe");
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return id_classe;
	}
	
	/** METODO PER LA VISUALIZZAZIONE DELLO STATO DI ESECUZIONE O COME SONO ANDATE LE VERIFICHE DAL DOCENTE
	 * 
	 * Struttura di visualizzazione:
	 * 
	 * +------+---------+--------------------+------------------+------+
	 * | nome | cognome | punteggio_ottenuto | punteggio_totale | voto |
	 * +------+---------+--------------------+------------------+------+
	 * 
	 * @param ID_verifica � il codice della verifica da visualizzare
	 * @return
	 */
	public ArrayList<Verifica> visualizzazioneStatoVerificaAssegnata (int Id_verifica) {
		ArrayList<Verifica> verifiche = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		//Prelevati dalla tabella verifica la matricola dello studente che ha fatto la verifica, il punteggio ottenuto, qeullo totale ed il voto
		String query = "SELECT * FROM verifica WHERE id_verifica='" + Id_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
				
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
	 * @param matricola_studente � la matricola dello studente da inserire
	 * @param nome_classe � il nome della classe dove inserire lo studente
	 * @param anno_scolastico � l'anno scolastico attuale
	 */
	public static void inserisciStudenteInClasse (String username, String classe) {
		String query0 = "SELECT id_classe FROM classe WHERE classe='" + classe + "';";
		DbQuery dbQuery = new DbQuery();
		
		int id_classe = 0;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query0);
			
			while (resultSet.next()) {
				id_classe = resultSet.getInt("id_classe");
			}
			
			String query = "UPDATE utente SET id_classe='" + id_classe + "' WHERE username='" + username + "';";
			DbQuery.setData(query, true);
			
			int numero_studenti = contaStudentiClasse(id_classe);
			String query1 = "UPDATE classe SET nr_studenti='" + numero_studenti + "' WHERE id_classe='" + id_classe + "';";
			DbQuery.setData(query1, true);
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER CONTROLLARE SE UNO STUDENTE DI CUI SI CONOSCE LA MATRICOLA APPARTIENE O MENO AD UNA CLASSE
	 * 
	 * @param matricola_studente � la matricola dello studente da verificare
	 * @return true se lo studente appartiene ad una classe; false se non
	 */
	public boolean controlloSeStudenteAppartieneAClasse (int matricola_studente) {
		String query = "SELECT id_classe FROM utente WHERE matricola='" + matricola_studente + "';";
		DbQuery dbQuery = new DbQuery();
		boolean result = true;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			int classe = 0;
			
			while (resultSet.next()) {
				classe = resultSet.getInt("id_classe");
			}
			
			if(classe <= 0)
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
	 * @param classe � l'ID classe della quale prelevare le matricole degli studenti
	 * @return un arralist di interi contenente le matricole degli studenti della classe
	 */
	public static ArrayList<Integer> getStudentiFromSpecificClass(int ID_classe){
		ArrayList<Integer> clas = new ArrayList<>();
				
		//Query per l'ottenimento degli studenti che hanno come ID_classe quello della classe indicata
		String query = "SELECT matricola FROM utente WHERE id_classe='" + ID_classe + "';";
		
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
	
	/** METODO PER OTTENERE GLI SUTDENTI DI UNA CLASSE 
	 * @param ID_classe � l'id della classe per la quale ottenere gli studenti
	 * @return un arraylist con gli studenti della classe individuata
	 */
	public static ArrayList<Utente> getStudentiDaClasse(int ID_classe){
		ArrayList<Utente> utenti = new ArrayList<Utente>();
				
		//Query per l'ottenimento degli studenti che hanno come ID_classe quello della classe indicata
		String query = "SELECT * FROM utente WHERE id_classe='" + ID_classe + "';";
		
		DbQuery dbQuery = new DbQuery();
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
		
			while (resultSet.next()) {
				Utente utente = new Utente();
				utente.setMatricola(resultSet.getInt("matricola"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setUsername(resultSet.getString("username"));
				utente.setPassword(resultSet.getString("password"));
				utente.setTipologia(resultSet.getString("tipologia"));
				utente.setEmail(resultSet.getString("email"));
				utente.setClasse(resultSet.getInt("id_classe"));
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
				
				classe.setId_classe(resultSet.getInt("id_classe"));
				classe.setClasse(resultSet.getString("classe"));
				classe.setAnno_scolastico(resultSet.getString("anno_scolastico"));
				classe.setNumero_studenti(resultSet.getInt("nr_studenti"));
				
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
	 * @param classe � la stringa con la classe da cercare
	 * @return 0 se la classe non esiste; un altro numero con l'ID della classe se esiste
	 */
	public static int getIdClasseFromAnnoESezione(String classe) {
		String query = "SELECT id_classe FROM classe WHERE classe='" + classe + "';";
		DbQuery dbQuery = new DbQuery();
		int ID_classe = 0;	//Se rimane 0 allora la classe cercata non esiste; altrimenti ci sar� l'ID della classe
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);

			while (resultSet .next()) {
				ID_classe = resultSet.getInt("id_classe");
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
	 * @param id_verifica � l'id della verifica che si vuole eseguire
	 * @return
	 */
	public static ArrayList<Domanda> getDomandeVerifica(int codice_verifica){
		ArrayList<Domanda> domande = new ArrayList<>();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle domande della verifica indicata
		String query = "SELECT * FROM domanda WHERE id_verifica='" + codice_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Domanda domanda = new Domanda();
				domanda.setId_domanda(resultSet.getInt("id_domanda"));
				domanda.setTesto(resultSet.getString("testo"));
				domanda.setMateria(resultSet.getString("materia"));
				domanda.setId_immagine(resultSet.getInt("id_immagine"));
				domanda.setId_verifica(resultSet.getInt("id_verifica"));
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
	 * @param id_domanda � l'id della domanda per cui cercare le risposte
	 * @return
	 */
	public static ArrayList<Risposta> getRisposteEsecuzioneVerifica(int id_domanda){
		ArrayList<Risposta> risposte = new ArrayList<Risposta>();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle risposta della domanda indicata
		String query = "SELECT * FROM risposta WHERE id_domanda='" + id_domanda + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				Risposta risposta = new Risposta();
				
				risposta.setId_risposta(resultSet.getInt("id_risposta"));
				risposta.setTesto(resultSet.getString("testo"));
				risposta.setId_immagine(resultSet.getInt("id_immagine"));			
				risposta.setPunteggio_risposta(resultSet.getDouble("punteggio_risposta"));
				risposta.setIs_selezionata(resultSet.getString("is_selezionata"));
				risposta.setCorretta(resultSet.getString("is_corretta"));
				risposta.setId_domanda(resultSet.getInt("id_domanda"));
				
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
	 * @param codice_verifica � l'id della verifica da cercare
	 * @return la verifica
	 */
	public Verifica getVerificaFromID(int id_verifica) {
		Verifica verifica = new Verifica();
		DbQuery dbQuery = new DbQuery();
		
		//Query per l'ottenimento delle verifica tramite l'ID indicato
		String query = "SELECT * FROM verifica WHERE id_verifica='" + id_verifica + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifica;
	}
	
	/**METODO CHE DATO L'ID DI UN DOCENTE E LA MATERIA PER LA QUALE CREARE UNA VERIFICA, REISTITUISCE
	 * L'ID DELLA TABELLA MATERIA_DOCENTE
	 * 
	 * @param id_docente � l'id docente
	 * @param materie � la materia della nuova verifica
	 * @return un intero con l'id del docente e della sua materia
	 */
	public static int getIdMateriaDocente(int id_docente, String materia) {
		String query = "SELECT id_materia_docente FROM materie_docenti WHERE materia='" + materia + "' AND id_docente='" + id_docente + "'";
		int id_materia_docente = 0;
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_materia_docente = resultSet.getInt("id_materia_docente");
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id_materia_docente;
	}
	
	/**DATO L'ID DI UN DOCENTE SI RESTITUISCE L'ELENCO DELLE SUE MATERIE 
	 * @param id_docente � l'id del docente
	 * @return l'elenco con le materie che il docente pu� gestire
	 */
	public ArrayList<String> getMaterieDocente(int id_docente) {
		String query = "SELECT materia FROM materie_docenti WHERE id_docente='" + id_docente + "'";
		ArrayList<String> materie_docente = new ArrayList<String>();
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				materie_docente.add(resultSet.getString("materia"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return materie_docente;
	}
	
	/** METODO PER OTTENERE UNA RIGA DELLA TABELLA MATERIE-DOCENTI DI UN DATO DOCENTE
	 * @param id_materia_docente � l'id della materia del docente per cui ottenere la riga dalla tabella
	 * @return la riga richiesta
	 */
	public static Materia_Docenti getMateriaDocente(int id_materia_docente) {
		Materia_Docenti md = null;
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT * FROM materie_docenti WHERE id_materia_docente='" + id_materia_docente + "';";
		
		try {
			md = new Materia_Docenti();
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				md.setId_materia_docenti(resultSet.getInt("id_materia_docente"));
				md.setMateria(resultSet.getString("materia"));
				md.setId_docente(resultSet.getInt("id_docente"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return md;
	}
	
	/** METODO PER LA CREAZIONE DELLA VERIFICA DA PARTE DEL DOCENTE. VERIFICA DI RIFERIMENTO PER QUELLE DEGLI STUDENTI.
	 * 	VERRA' SALVATA SUL DOCENTE.
	 * 
	 * Il metodo di compone di 3/4 query:
	 * 		1) Viene creata la verifica con i 4 parametri passati al metodo ed inserita nella tabella verifiche del database;
	 * 			(NOTA: i valori relativi ai restanti campi verranno indicati rispettivamente al valore "null", nel modo nel quale
	 * 					ciascun campo lo richiede. ES: classe->null; test_eseguit->"NO"; punteggio->0.0)
	 * 		2) Si ottiene l'ID della verifica che � appena stata creata, individuabile dal campo codice_verifica="null" e dalla matricola
	 * 			del docente. Sar� l'unica verifica appena creata con tali valori. 
	 * 		   Ottenuto l'ID si costruir� il codice_verifica: una stringa composta da "ID_Docente-ID_verifica". Questa struttura 
	 * 			permetter� di individuare tutte le verifiche di un dato docente e rendere la verifica univoca dato un solo campo, 
	 * 			ma soprattutto permetter� di gestire l'assegnazione delle verifiche agli studenti in un secondo momento, facendo
	 * 			in modo che poi a ciascuno studente appartenga un'unica verifica, con riferimento alla verifica creata dal docente
	 * 			(Il campo ID progressivo, per gli studenti, non sar� ritenuto necessario)
	 * 		3) Viene aggiornato il codice_verifica con quello appena creato e ottenuto dalle 2 query precedenti.
	 * 		4) Viene finalizzata la creazione della verifica con l'assegnazione, alle domande di questa verifica, del rispettivo
	 * 			codice_verifica. Se la domanda era gi� presente e non usata, si effettua semplicemente l'aggiornamento del campo;
	 * 			altrimenti ne viene creata un'altra.
	 * 
	 * @param argomento � la materia/argomento della verifica
	 * @param matricola � l'ID del docente che ha creato la verifica
	 * return il codice della verifica appena creata, in formato stringa: "ID_docente-ID_verifica"
	 */
	public static int creazioneVerifica(String argomento, int matricola, Time tempo, String scadenza, ObservableList<Domanda>domande, String materia, int id_classe, int id_materia_docente) {
		DbQuery dbQuery = new DbQuery();
		
		// Caso del docente
		if(id_materia_docente == 0)
			id_materia_docente = getIdMateriaDocente(matricola, materia);
				
		// Calcolo del punteggio totale della verifica: somma dei punteggi delle risposte corrette
		int punteggio_totale = calcolaPunteggioTotale(domande);
		
		//Inserimento verific
		String query;
		
		if(id_classe > 0)
			query= "INSERT INTO verifica (argomento, is_eseguita, is_assegnata, punteggio_ottenuto, punteggio_totale, voto, tempo, scadenza, id_classe, id_utente, id_materia_docente) "
					 + "VALUES "
					 + "('" + argomento + "', '" + "NO" + "', '" + "NO" + "', '" + 0.0 + "', '" + punteggio_totale + "', '" + 0 + "', '" + tempo + "', '" + scadenza + "', '" + id_classe + "', '" + matricola + "', '" + id_materia_docente +  "');";
		else 
			query= "INSERT INTO verifica (argomento, is_eseguita, is_assegnata, punteggio_ottenuto, punteggio_totale, voto, tempo, scadenza, id_classe, id_utente, id_materia_docente) "
					 + "VALUES "
					 + "('" + argomento + "', '" + "NO" + "', '" + "NO" + "', '" + 0.0 + "', '" + punteggio_totale + "', '" + 0 + "', '" + tempo + "', '" + scadenza + "', NULL, '" + matricola + "', '" + id_materia_docente +  "');";
		
		//Ottenimento ID della verifica appena creata
		String query1 = "SELECT id_verifica FROM verifica ORDER BY id_verifica DESC LIMIT 1;";
		int ID_verifica = 0;
		
		try {
			DbQuery.setData(query, true);
			
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query1);
			
			while (resultSet.next()) {
				ID_verifica = resultSet.getInt("id_verifica");
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		//Duplicazione delle domande e loro aggiornamento in modo che si riferiscano alla verifica appena creata
		for(int i = 0; i < domande.size(); i++) {
			//Ottenimento risposte per la domanda da duplicare
			ArrayList<Risposta> risposte = getRisposteEsecuzioneVerifica(domande.get(i).getId_domanda());
			int ID_domanda_duplicata = creaDomanda(domande.get(i).getTesto(), domande.get(i).getMateria(), ID_verifica, domande.get(i).getId_immagine());	//La domanda viene duplicata con nuovo codice univoco
			
			for(int k = 0; k < risposte.size(); k++) {	
				//duplicazione risposta con nuovo id domanda=quello della domanda della verifica dello studente
				creaRisposta(risposte.get(k).getTesto(), risposte.get(k).getPunteggio_risposta(), risposte.get(k).getCorretta(), ID_domanda_duplicata, risposte.get(k).getId_immagine());
			}
		}
		return ID_verifica;
	}
	
	/** METODO PER IL CALCOLO DEL PUNTEGGIO TOTALE DELLE DOMANDE DI UNA VERIFICA
	 * @param domande � l'elenco delle domande attraverso quale calcolare il punteggio totale
	 * @return il punteggio totale richiesto
	 */
	private static int calcolaPunteggioTotale(ObservableList<Domanda> domande) {
		int pt_tot = 0;
		
		for(int i = 0; i < domande.size(); i++) 
		{
			// Ottenimento risposte di ciascuna domanda
			ArrayList<Risposta> risposte = getRisposteEsecuzioneVerifica(domande.get(i).getId_domanda());
			// Somma dei punteggi corretti di ciascuna risposta corretta => calcolo punteggio massimo
			for(int j = 0; j < risposte.size(); j++) 
			{
				if(risposte.get(j).getCorretta().equals("SI"))
					pt_tot += risposte.get(j).getPunteggio_risposta();
			}
		}
		return pt_tot;
	}
	
	/**METODO PER OTTENERE L'ID DELLA CLASSE DATO IL SUO NOME DALLA TABELLA CLASSI
	 * 
	 * @param classe � la classe da cercare
	 * @return un intero che corrisponder� all'id della classe
	 */
	public static int getIdClasseFromNomeClasse(String classe) {
		DbQuery dbQuery = new DbQuery();
		String query = "SELECT id_classe FROM classe WHERE classe='" + classe + "';";
		
		int id_classe = 0;
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_classe = resultSet.getInt("id_classe");
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return id_classe;
	}
	
	/** METODO PER VISUALIZZARE LE VERIFICHE ASSEGNATE DAL DOCENTE E I RELATIVI RISULTATI
	 * @param id_docente � l'id del docente per il quale ottenere le verifiche assegnate agli studenti
	 * @return l'elenco delle verifiche assegnate dal docente
	 */
	public static ArrayList<Verifica> getVerificheAssegnateSuDocente (int id_materia_docente, int id_docente) {		
		ArrayList<Verifica> verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_materia_docente='" + id_materia_docente + "' AND is_assegnata='SI' AND id_utente<>'" + id_docente + "';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifiche = new ArrayList<Verifica>();
			
			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));		
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
	
	/**METODO PER ASSEGNARE UNA VERIFICA A TUTTI GLI STUDENTI DI UNA DATA CLASSE
	 * 
	 * @param argomento della verifica da assegnare
	 * @param domande_assegnate per ciascuno studente
	 * @param classe a cui assegnare la verifica
	 * @param codice_verifica della verifica da assegnare
	 */
	public static void assegnaVerificaATuttiGliStudenti(String argomento, int domande_assegnate, String classe, int id_verifica, Time tempo, String scadenza, int id_docente, ArrayList<Domanda> domande, String materia) {
		int id_classe = getIdClasseFromNomeClasse(classe);
		
		int id_materia_docente = getIdMateriaDocente(id_docente, materia);
		
		//Ottenimento delle matricole degli studenti della classe
		ArrayList<Integer> ID_studenti = getStudentiFromSpecificClass(id_classe);
				
		//Aggiornamento stato della verifica-> verifica assegnata "SI" e classe != "N.A."
		aggiornaStatoVerificaNonAssegnata(id_verifica, id_classe);
				
		for(int i = 0; i < ID_studenti.size(); i++) {				
			int ID_verifica = assegnazioneVerificaAdUnoStudente((materia + " - " + argomento), id_classe, domande_assegnate, id_verifica, ID_studenti.get(i), tempo, scadenza, id_materia_docente, domande);
			aggiornaStatoVerificaNonAssegnata(ID_verifica, id_classe);
		}
	}
	
	/**AGGIORNAMENTO DELLO STATO DI UNA VERIFICA CHE E' APPENA STATA ASSEGNATA AD UNA CLASSE
	 * 	Modifica dei campi:
	 * 		- classe -> valore della classe a cui � stata assegnata la verifica
	 * 		- test_assegnato -> da "NO" a "SI"
	 * @param codice_verifica della verifica di riferimento 
	 * @param classe alla quale si vuole assegnare la verifica
	 */
	public static void aggiornaStatoVerificaNonAssegnata(int id_verifica, int id_classe) {
		String query1 = "UPDATE verifica SET id_classe='" + id_classe + "' WHERE id_verifica='" + id_verifica + "';";
		String query2 = "UPDATE verifica SET is_assegnata='" + "SI" + "' WHERE id_verifica='" + id_verifica + "';";
		
		try {
			DbQuery.setData(query1, true);
			DbQuery.setData(query2, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER L'ASSEGNAZIONE DELLA VERIFICA INDICATA AGLI STUDENTI DI UNA CLASSE
	 * 
	 * Il metodo estrae le domande della veridica, le salva in un arraylist;
	 * 			 estrae le risposte di ciascuna domanda;
	 *			 crea un elenco con gli ID delle nuove verifiche che vengono assegnate agli studenti 
	 *			 della classe indicata.
	 * 
	 * 			 Con questo ulteriore elemento si passa alla creazione di una nuova domanda, con 
	 * 			 stessi elementi della precedente ma diverso ID_verifica, che sar� quello della verifica
	 * 		 	 assegnata allo studente.
	 * 
	 * 			 In questo modo si creereranno anche le risposte per ciascuna domanda.
	 * 
	 * @param materia � la materia della verifica
	 * @param argomento � l'argomento della verifica
	 * @param domande_assegnate � il numero di domande che ciascuno studente dovr� eseguire
	 * @param classe � la classe a cui verr� assegnata la verifica
	 * @param matricola_docente � la matricola del docente che ha creato la verifica
	 * @param id_verifica_da_assegnare � l'id della verifica originale e che si deve assegnare agli studenti
	 * return una stringa con il codice della verifica appena assegnata
	 */
	public static int assegnazioneVerificaAdUnoStudente (String argomento, int classe, int domande_assegnate, int id_verifica, int ID_studente, Time tempo, String scadenza, int id_materia_docente, ArrayList<Domanda> questions) {							
		//Scelta casuale delle domande e poste in un nuovo ordine
		questions = preparaDomandePerStudente(questions, domande_assegnate);
		ObservableList<Domanda> domande = FXCollections.observableArrayList();
		domande.addAll(questions);
						
		return creazioneVerifica(argomento, ID_studente, tempo, scadenza, domande, getMateriaDocente(id_materia_docente).getMateria(), classe, id_materia_docente);			
	}
	
	/**Metodo per la preparazione delle domande per ciascuno studente: vengono prese in modo casuale
	 * tante domande quante il docente ha indicato che vengano assegnate agli studenti.
	 * 
	 * @param domande � l'elenco delle domande da dove prelevare in modo casuale
	 * @param nr_domande � il numero di domande da prendere dall'elenco
	 * @return l'elenco con le nuove domande preparate in modo casuale
	 */
	public static ArrayList<Domanda> preparaDomandePerStudente (ArrayList<Domanda>domande, int nr_domande) {
		ArrayList<Domanda> domandeXstudente = new ArrayList<Domanda>();
		//Inserimento domande
		for(int i = 0; i < nr_domande; i++) {
			int x = (int)(Math.random() * domandeXstudente.size());
			//Controllo domanda gi� inserita
			while(controlloPresenzaDomandaInArrayList(domandeXstudente, domande.get(x).getId_domanda())) {
				x = (int)(Math.random() * domandeXstudente.size() + 1);
			}
			domandeXstudente.add(domande.get(x));
		}
		return domandeXstudente;
	}
	
	/**Controllo se nell'elenco delle domande � stata gi� inserita una specifica domanda identificata dall'id
	 * 
	 * @param domande � l'elenco delle domande in cui effettuare il controllo
	 * @param id_domanda � l'id della domanda della quale verificare la presenza nell'elenco
	 * @return true se esiste gi� la domanda; false se non esiste.
	 */
	public static boolean controlloPresenzaDomandaInArrayList (ArrayList<Domanda>domande, int id_domanda) {
		int i = 0;
		while(i < domande.size()) {
			if(domande.get(i).getId_domanda() == id_domanda)
				return true;
			i++;
		}
		return false;
	}
	
	/** Aggiornamento delle risposte in base alle selezioni fatte dall'utente
	 * @param risposte � l'elenco delle risposte, il cui valore "selezionata" � relativo
	 * 			al fatto che sia stata selezionata o no la risposta
	 */
	public static void aggiornaRisposteStudente(ArrayList<Risposta> risposte) {
		String query;
		for(int i = 0; i < risposte.size(); i++) {
			query = "UPDATE risposta SET is_selezionata='" + risposte.get(i).getIs_selezionata() + "' WHERE id_risposta='" + risposte.get(i).getId_risposta() + "';";
			try {
				DbQuery.setData(query, true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** METODO PER AGGIORNARE LO STATO DELLA VERIFICA ESEGUITA DA NO A SI
	 * @param verifica � la verifica per la quale modificare il parametro is_eseguita su "SI"
	 */
	public static void aggiornaStatoVerificaEseguita(Verifica verifica) {
		String query = "UPDATE verifica SET is_eseguita='SI' WHERE id_verifica='" + verifica.getId_verifica() + "';";
		
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** METODO PER AGGIORNARE IL PUNTEGGIO OTTENUTO DELLA VERIFICA INDICATA
	 * @param verifica � la verifica sulla quale salvare il punteggio
	 * @param punteggio � il punteggio ottenuto nella verifica
	 */
	public static void aggiornaPunteggioOttenutoEVoto(Verifica verifica, double punteggio, double voto) {
		String query1 = "UPDATE verifica SET punteggio_ottenuto='" + punteggio + "' WHERE id_verifica='" + verifica.getId_verifica() + "';";
		String query2 = "UPDATE verifica SET voto='" + voto + "' WHERE id_verifica='" + verifica.getId_verifica() + "';";
		
		try {
			DbQuery.setData(query1, true);
			DbQuery.setData(query2, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** METODO PER VERIFICARE SE UNA MATERIA ESISTE O MENO
	 * @param materia � la materia da verificare se esiste gi�
	 * @return true se esiste gi�; false se non esiste.
	 */
	public static boolean controlloEsistenzaMateria(String materia) {
		materia = materia.toUpperCase();
		String query = "SELECT * FROM materie_docenti WHERE materia='" + materia + "'";
		DbQuery dbQuery = new DbQuery();
		boolean result = false;
		
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
	
	/** METODO PER CREARE UNA MATERIA
	 * @param materia � la materia da creare
	 */
	public static void creaMateria(String materia) {
		materia = materia.toUpperCase();
		String query = "INSERT INTO materie_docenti(materia, id_docente) VALUES ('" + materia + "', NULL);";
		
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** METODO PER OTTENERE L'ELENCO DI TUTTE LE MATERIE DISPONIBILI ED ESISTENTI
	 * @return un arraylist con tutte le materie esistenti
	 */
	public static ArrayList<String> getElencoMaterieDisponibili() {
		ArrayList<String> elenco_materie = null;
		String query = "SELECT DISTINCT materia FROM materie_docenti;";
		DbQuery dbQuery = new DbQuery();
		
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			elenco_materie = new ArrayList<String>();

			while (resultSet.next()) {
				String materia = resultSet.getString("materia");
				elenco_materie.add(materia);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elenco_materie;
	}
	
	/** METODO PER VERIFICARE SE IL DOCENTE SELEZIONATO GESTISCE GIA' LA MATERIA SELEZIONATA
	 * @return true se gi� gestisce la materia; false se non la gestisce ancora.
	 */
	public static boolean controlloMateriaGi�AssegnataADocente(int id_docente, String materia) {
		materia = materia.toUpperCase();
		String query = "SELECT * FROM materie_docenti WHERE materia='" + materia + "' AND id_docente='" + id_docente + "';";
		DbQuery dbQuery = new DbQuery();
		boolean result = false;
		
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
	
	/** METODO PER CONTROLLARE SE UNA MATERIA � GIA' STATA ASSEGNATA AD UN DOCENTE
	 * @param materia � la materia da verificare
	 * @return true se non � gi� stata assegnata; false se � stata assegnata.
	 */
	public static boolean controlloMateriaNonAssegnataADocenti(String materia) {
		materia = materia.toUpperCase();
		String query = "SELECT * FROM materie_docenti WHERE materia='" + materia + "' AND id_docente IS NULL;";
		DbQuery dbQuery = new DbQuery();
		boolean result = false;  //non conta il valore assengato
		
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
	
	/** METODO PER ASSEGNARE UNA MATERIA AD UN DOCENTE
	 */
	public static void assegnaDocenteAMateria(String materia, int id_docente) {
		String query;
		if(!controlloMateriaNonAssegnataADocenti(materia)) {
			// Materia gi� assegnata
			query = "INSERT INTO materie_docenti (materia, id_docente) VALUES ('" + materia + "', '" + id_docente + "');";
			
			try {
				DbQuery.setData(query, true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		else {
			// Materia non ancora assegnata
			query = "UPDATE materie_docenti SET id_docente='" + id_docente + "' WHERE materia='" + materia + "';";
			
			try {
				DbQuery.setData(query, true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** METODO PER CONTROLLARE SE, DATA UNA MATERIA, QUESTA SIA PRESENTE IN FORMA UNICA NELLA TABELLA O CI SIANO DUPLICATI
	 * @param materia � la materia da verificare
	 * @return true se � presente come duplicato; false se � unica
	 */
	public static boolean controlloSeMateriaHaDuplicato(String materia) {
		materia = materia.toUpperCase();
		String query = "SELECT COUNT(*) FROM materie_docenti WHERE materia='" + materia + "';";
		DbQuery dbQuery = new DbQuery();
		int n = 0;
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
				
			while (resultSet.next()) {
				n = resultSet.getInt(1);
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(n == 1)
			return false;
		else
			return true;
	}
	
	/** METODO PER RIMUOVERE L'ASSEGNAZIONE DI UNA MATERIA DA UN DOCENTE
	 * @param id_docente indica il docente per il quale rimuovere la gestione della materia
	 * @param materia � la materia da rimuovere dalla gestione del docente
	 */
	public static void rimuoviAssegnazioneMateriaDocente(int id_docente, String materia) {
		String query;
		
		//Controllo se materia del docente � un duplicato di un'altra materia o � l'unica
		if(!controlloSeMateriaHaDuplicato(materia)) {
			query = "UPDATE materie_docenti SET id_docente=NULL WHERE materia='" + materia + "' AND id_docente='" + id_docente + "';";
			try {
				DbQuery.setData(query, true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			query = "DELETE FROM materie_docenti WHERE id_docente='" + id_docente + "' AND materia='" + materia + "';";
			try {
				DbQuery.setData(query, true);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** METODO PER OTTENERE L'ANNO SCOLASTICO DI UNA DATA CLASSE
	 * @param classe � la classe per la quale si vuole ottenere l'anno scolastico
	 * @return una stringa con l'anno scolastico in formato YYYY/YYYY
	 */
	public static String getAnnoScolasticoFromClasse(String classe) {
		DbQuery dbQuery = new DbQuery();
		String annoScolastico = "";
		
		String query = "SELECT anno_scolastico FROM classe WHERE classe='" + classe + "';";
		
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);
				
			while (resultSet.next()) {
				annoScolastico = resultSet.getString("anno_scolastico");
			}
			
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return annoScolastico;
	}
	
	/** METODO PER AGGIORNARE LA TIPOLOGIA DEGLI STUDENTI SU DIPLOMATI
	 * @param username individua lo studente che � stato diplomato
	 */
	public static void aggiornaTipologiaStudentiDiplomati(String username) {
		String query = "UPDATE utente SET tipologia='DIPLOMATO' WHERE username='" + username + "';";
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** CONTROLLO SE UNA CLASSE ESISTE DATO ANNO E SEZIONE
	 * @param classe � la classe da verificare
	 * @return true se esiste; false se non esiste
	 */
	public static boolean controlloClasseEsistente(String classe) {
		String query = "SELECT * FROM classe WHERE classe='" + classe + "';";
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
	
	/** METODO CHE DATO LA CLASSE AGGIORNA L'ANNO SCOLASTICO
	 * @param classe � la classe per la quale aggiornare l'anno scolastico
	 */
	public static void aggiornaAnnoScolasticoClasse(String classe, String nuovo_anno) {
		String query = "UPDATE classe SET anno_scolastico='" + nuovo_anno + "' WHERE classe='" + classe + "';";
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** METODO PER OTTENERE UN UTENTE DATO L'ID
	 * @param id � l'id dell'utente da ottenere
	 * @return l'utente richiesto e individuato dall'id
	 */
	public static Utente getUtenteDatoID(int id) {
		String query = "SELECT * FROM utente WHERE matricola='" + id + "';";
		DbQuery dbQuery = new DbQuery();
		Utente utente = new Utente();
		
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			while (resultSet.next()) {
				utente.setMatricola(resultSet.getInt("matricola"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setUsername(resultSet.getString("username"));
				utente.setPassword(resultSet.getString("password"));
				utente.setTipologia(resultSet.getString("tipologia"));
				utente.setClasse(resultSet.getInt("id_classe"));
				utente.setEmail(resultSet.getString("email"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utente;
	}
	
	/** METODO PER LA RIMOZIONE DELLA VERIFICA DAL DATABASE
	 * @param verifica � la verifica da rimuovere
	 */
	public static void rimuoviVerifica(Verifica verifica) {
		// Ottenimento elenco domande della verifica
		ArrayList<Domanda> domandeVerifica = getDomandeVerifica(verifica.getId_verifica());
		
		for(int i = 0; i < domandeVerifica.size(); i++) {
			// Rimozione risposte delle domande
			rimuoviRisposteDomandaVerifica(domandeVerifica.get(i));
		}
		
		// Rimozione domande della verifica
		rimuoviDomandeVerifica(verifica);
		
		DbQuery dbQuery = new DbQuery(); 
		String query = "DELETE FROM verifica WHERE id_verifica='" + verifica.getId_verifica() + "';";
		try {
			DbQuery.setData(query, true);
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** METODO PER L'OTTENIMENTO DI UNA VERIFICA DATO IL SUO ID
	 * @param id_verifica � l'id della verifica da ottenere
	 * @return la verifica cercata
	 */
	public static Verifica getVerificaFromId(int id_verifica) {
		Verifica verifica = null;
		String query = "SELECT * FROM verifica WHERE id_verifica='" + id_verifica + "';";
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			verifica = new Verifica();

			while (resultSet.next()) {
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifica;
	}
	
	/** METODO PER L'INSERIMENTO DI UN'IMMAGINE NEL DATABASE
	 * @param nome � il nome dell'immagine
	 * @param path � il percorso dell'immagine da copiare
	 * @return l'id dell'immagine all'interno del database
	 */
	public static int inserisciImmagine(String nome, String path) {
		int id_immagine = 0;
		DbQuery dbQuery = new DbQuery();
		
		try {
			Connection con = DbSingleConn.getConnection();
			
			File image = new File(path);
			InputStream is = new FileInputStream(image);
			
			PreparedStatement ps = con.prepareStatement("INSERT INTO immagini(nome, immagine) VALUES (?, ?);");			
			ps.setString(1, nome);
			ps.setBinaryStream(2, is);
			ps.executeUpdate();
	
			Statement stat = con.createStatement();
			// Ottenimento dell'id dell'ultima immagine inserita
			String query = "SELECT id_immagine FROM immagini ORDER BY id_immagine DESC LIMIT 1;";
			ResultSet resultSet = stat.executeQuery(query);
			
			while (resultSet.next()) {
				id_immagine = resultSet.getInt("id_immagine");
			}
			is.close();
			ps.close();
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return id_immagine;
	}
	
	/**	METODO PER OTTENERE L'IMMAGINE DATO IL SUO ID
	 * @param id_immagine � l'id dell'immagine da ricercare nel database
	 */
	public static Image getImmagineDatoId(int id_immagine) {
		Image image = null;
		DbQuery dbQuery = new DbQuery(); 
		try{
			ResultSet rs = dbQuery.getData("SELECT * FROM immagini WHERE id_immagine='" + id_immagine + "';", true);
			
			InputStream is = null;
			while(rs.next()) {
				is = rs.getBinaryStream("immagine");
			}
			// Lettura immagine dal binary stream
			BufferedImage bi = ImageIO.read(is);
			image = SwingFXUtils.toFXImage(bi, null);
			
            is.close();
            dbQuery.releaseConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return image;
	}
	
	/** METODO PER OTTENERE L'ELENCO DELLE VERIFICHE DEGLI STUDENTI CHE HANNO LO STESSO ID CLASSE,
	 * STESSO ARGOMENTO E STESSA MATERIA DUNQUE NEL FORMATO: 		MATERIA- ARGOMENTO
	 * @param id_classe � l'id della classe alla quale � stata assegnata la verifica
	 * @param argomento � l'argomento della verifica da ricercare
	 * @param id_materia_docente � l'id della tabella dove ricercare la materia della verifica
	 * @return l'elenco delle verifiche degli studenti cercato
	 */
	public static ArrayList<Verifica> getElencoVerificheSpecifiche(int id_classe, String argomento){
		ArrayList<Verifica> elenco_verifiche = null;
		String query = "SELECT * FROM verifica WHERE id_classe='" + id_classe + "' AND argomento='" + argomento + "';";
		
		DbQuery dbQuery = new DbQuery();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			elenco_verifiche = new ArrayList<Verifica>();

			while (resultSet.next()) {
				Verifica verifica = new Verifica();
				verifica.setId_verifica(resultSet.getInt("id_verifica"));
				verifica.setArgomento(resultSet.getString("argomento"));
				verifica.setIs_eseguita(resultSet.getString("is_eseguita"));
				verifica.setIs_assegnata(resultSet.getString("is_assegnata"));
				verifica.setPunteggio_ottenuto(resultSet.getDouble("punteggio_ottenuto"));
				verifica.setPunteggio_totale(resultSet.getDouble("punteggio_totale"));
				verifica.setTempo(Time.valueOf(resultSet.getString("tempo")));
				verifica.setScadenza(resultSet.getString("scadenza"));	
				verifica.setId_classe(resultSet.getInt("id_classe"));
				verifica.setId_utente(resultSet.getInt("id_utente"));
				verifica.setVoto(resultSet.getDouble("voto"));
				verifica.setId_materia_docente(resultSet.getInt("id_materia_docente"));
				elenco_verifiche.add(verifica);
			}
			dbQuery.releaseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elenco_verifiche;
	}
	
	/** METODO PER LA RIASSEGNAZIONE DI UNA VERIFICA AD UNO STUDENTE
	 * @param verifica � la verifica da riassegnare
	 */
	public static void riassegnaVerifica(Verifica verifica, String scadenza) {
		String query = "UPDATE verifica SET scadenza='" + scadenza + "' WHERE id_verifica='" + verifica.getId_verifica() + "';";
		
		try {
			DbQuery.setData(query, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}