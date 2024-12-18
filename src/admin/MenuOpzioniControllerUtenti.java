package admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class to manage the option about users in admin section
 */
public class MenuOpzioniControllerUtenti implements Initializable {

	@FXML private VBox rootPane;
    @FXML private JFXButton nuovoUtente;
    @FXML private JFXButton caricaListaUtenti;
    @FXML private JFXButton aggiornamentoUtente;
    @FXML private JFXButton downloadButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}
	
	@FXML
	/** Method to load the scene to manage the insertion of a new user */
	private void nuovoUtente() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_utenti/nuovoUtente.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerUtenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the scene to manage the insertion of users from a file */
	private void caricaElencoUtenti() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/InsertElencoUtenti.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerUtenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the scene to manage the reset of the password */
	private void resetPassword() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_utenti/modificaUtente.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerUtenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the scene to manage the print of the students list */
	private void stampaElencoStudenti() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_utenti/StampaStudenti.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerUtenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
