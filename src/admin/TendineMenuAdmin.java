package admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TendineMenuAdmin implements Initializable {
	
	//Tendina operazione su classe
    @FXML private VBox vboxClasse;
	@FXML private JFXButton nuovaClasseButton;
    @FXML private JFXButton inserisciStudenteButton;
    @FXML private JFXButton rimuoviStudenteButton;
    @FXML private JFXButton modificaClasseButton;
	
	//Tendina operazione su utente
	@FXML private VBox vboxUtenti;
    @FXML private JFXButton nuovoUtenteButton;
    @FXML private JFXButton eliminaUtenteButton;
    @FXML private JFXButton modificaUtenteButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML 
	public void creaClasse(ActionEvent event) {
		loadSchermataAggiuntaClasse();
	}
	
	/** METODO PER CARICARE LA SCHERMATA DI CREAZIONE DELLA PAGINA DI AGGIUNTA DI UNA CLASSE
	 */
	private void loadSchermataAggiuntaClasse() {
		try {
			Parent creazioneClasseScene;
			creazioneClasseScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_nuova_classe/nuovaClasse.fxml"));
			
			Scene creaClasse = new Scene(creazioneClasseScene);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxClasse.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setTitle("Creazione di una nuova classe");
			stage.setScene(creaClasse);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void inserisciStudente(ActionEvent event) {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_inserisci_studente/InserisciStudente.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxClasse.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void rimuoviStudente(ActionEvent event) {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_rimuovi_studente/rimuoviStudente.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxClasse.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void modificaClasse(ActionEvent event) {
		
	}
	
	public void creaUtente(ActionEvent event) {
		loadSchermataCreazioneUtente();
	}
	
	/** METODO PER CARICARE LA SCHERMATA DI CREAZIONE DELL'UTENTE
	 */
	private void loadSchermataCreazioneUtente() {
		try {
			Parent creazioneUtenteScene;
			creazioneUtenteScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_nuovo_utente/nuovoUtente.fxml"));
			
			Scene creaUtente = new Scene(creazioneUtenteScene);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxUtenti.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setTitle("Creazione nuovo account");
			stage.setScene(creaUtente);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void rimuoviUtente(ActionEvent event) {
		loadSchermataRimuoviUtente();
	}
	
	/** METODO PER MOSTRARE LA SCHERMATA DI RIMOZIONE DI UN UTENTE
	 */
	private void loadSchermataRimuoviUtente() {
		try {
			Parent rimuoviUtenteScene;
			rimuoviUtenteScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_elimina_utente/eliminaUtente.fxml"));
			
			Scene creaClasse = new Scene(rimuoviUtenteScene);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxUtenti.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setTitle("Rimozione di un utente");
			stage.setScene(creaClasse);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void modificaUtente(ActionEvent event) {
		loadSchermataModificaUtente();
	}
	
	/** METODO PER MOSTRARE LA SCHERMATA DI MODIFICA DELLA PASSWORD DI UN UTENTE
	 */
	private void loadSchermataModificaUtente() {
		try {
			Parent modificaUtenteScene;
			modificaUtenteScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_modifica_utente/modificaUtente.fxml"));
			
			Scene creaClasse = new Scene(modificaUtenteScene);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) vboxUtenti.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setTitle("Rimozione di un utente");
			stage.setScene(creaClasse);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
