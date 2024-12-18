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
 * Class to manage the option about classes edits by the admin
 */
public class MenuOpzioniControllerClassi implements Initializable {
	
	@FXML private VBox rootPane;
    @FXML private JFXButton nuovaClasse;
    @FXML private JFXButton inserisciStudente;
    @FXML private JFXButton rimuoviStudente;
    @FXML private JFXButton aggiornamentoClassi;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
    
	@FXML
	/** Method to add a new class */
	private void nuovaClasse() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_classi/nuovaClasse.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerClassi.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to add a student in the class */
	private void inserisciStudenteInClasse() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_classi/inserimentoStudenti.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerClassi.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to remove a student from a class */
	private void rimuoviStudenteDaClasse() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_classi/rimozioneStudenti.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerClassi.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to update the classes */
	private void aggiornaClassi() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin_classi/modificaClasse.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerClassi.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
