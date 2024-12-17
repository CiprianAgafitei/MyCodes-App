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

/**
 * Admin Menu Layers
*/
public class TendineMenuAdmin implements Initializable {
	
	// Operation layer for class management
    @FXML private VBox vboxClasse;
	@FXML private JFXButton nuovaClasseButton;	// newClassButton
    @FXML private JFXButton inserisciStudenteButton;	// insertStudentButton
    @FXML private JFXButton rimuoviStudenteButton;	// removeStudentButton
    @FXML private JFXButton modificaClasseButton;	// EditClassButton
	
	// Operation layer for user management
	@FXML private VBox vboxUtenti;
    @FXML private JFXButton nuovoUtenteButton;		// newUserButton
    @FXML private JFXButton eliminaUtenteButton;	// deleteUserButton
    @FXML private JFXButton modificaUtenteButton;	// editUserButton
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML 
	/**
	 *  Create class method
 	*/
	public void creaClasse(ActionEvent event) {
		loadSchermataAggiuntaClasse();
	}
	
	/** METHOD TO LOAD THE SCREEN TO CREATE THE PAGE FOR ADDING A CLASS
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
	/** Add new student method */
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
	/** Remove Student Method  **/
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

	/* Edit class method */
	public void modificaClasse(ActionEvent event) {
		
	}

	/* Create user method */
	public void creaUtente(ActionEvent event) {
		loadSchermataCreazioneUtente();
	}
	
	/** METHOD TO LOAD THE SCREEN FOR THE CREATION OF THE NEW USER */
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
	/* REMOVE USER */
	public void rimuoviUtente(ActionEvent event) {
		loadSchermataRimuoviUtente();
	}
	
	/** METHOD TO SHOW THE SCREEN TO REMOVE THE USER */
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
	/* Edit User method */
	public void modificaUtente(ActionEvent event) {
		loadSchermataModificaUtente();
	}
	
	/** METHOD TO LOAD THE SCREEN TO ALLOW EDIT OF THE PASSOWRD OF A USER */
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
