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
 * Class to manage the options of Subjects and Teahcers in the admin section
 */
public class MenuOpzioniControllerMaterieDocenti implements Initializable {
	
	@FXML private VBox rootPane;
	@FXML private JFXButton nuovaMateria;
	@FXML private JFXButton assegnazioneMateria;
	@FXML private JFXButton rimuoviAssegnazione;
	@FXML private JFXButton stampaAssegnazioni;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	@FXML
	/** Method to load the scene for a new Subject */
	public void nuovaMateria() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_materie_docenti/nuovaMateria.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerMaterieDocenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	/** Method to load the scene to set a subject to a teacher */
	public void assegnaMateriaDocente() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_materie_docenti/AssegnazioneDocenteMateria.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerMaterieDocenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the scene to remove a subject from a teacher */
	public void rimuoviAssegnazioneMateriaDocente() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_materie_docenti/RimuoviAssegnazione.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerMaterieDocenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the scene with all the list of teachers and theirs subjects */
	public void stampaElencoDocentiMaterie() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin_materie_docenti/StampaDocenti.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniControllerMaterieDocenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
