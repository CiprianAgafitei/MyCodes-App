package docenti_verifiche;

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

public class MenuOpzioniController implements Initializable {

	@FXML private VBox rootPane;
	@FXML private JFXButton nuovaDomanda;
	@FXML private JFXButton nuovaVerifica;
	@FXML private JFXButton modificaVerifica;
	@FXML private JFXButton scaricaVerifica;
	@FXML private JFXButton exitMenu;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
	}
	   
	@FXML
	public void nuovaDomandaClicked() {
		try {
			Parent schermataNuovaDomanda;
			schermataNuovaDomanda = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_nuova_domanda/nuovaDomandaDocenti.fxml"));
			
			Scene scena = new Scene(schermataNuovaDomanda);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setTitle("Creazione di una domanda");
			stage.setScene(scena);
			stage.centerOnScreen();
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	public void nuovaVerificaClicked() {	
		try {
			Parent schermataNuovaVerifica;
			schermataNuovaVerifica = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/nuovaVerifica.fxml"));
			
			Scene scena = new Scene(schermataNuovaVerifica);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setTitle("Creazione di una verifica");
			stage.setScene(scena);
			stage.sizeToScene();
			stage.centerOnScreen();
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	public void stampaVerifica() {	
		try {
			Parent schermataStampaVerifica;
			schermataStampaVerifica = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/StampaVerifica.fxml"));
			
			Scene scena = new Scene(schermataStampaVerifica);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setTitle("Stampa di una verifica");
			stage.setScene(scena);
			stage.sizeToScene();
			stage.centerOnScreen();
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void modificaCliccato() {
		try {
			Parent modificaVerifica;
			modificaVerifica = (StackPane) FXMLLoader
					.load(getClass().getResource("/docenti_verifiche/ModificaVerifica.fxml"));

			Scene scena = new Scene(modificaVerifica);

			Stage stage = (Stage) rootPane.getScene().getWindow();

			stage.setTitle("Modifica di una verifica");
			stage.setScene(scena);
			stage.sizeToScene();
			stage.centerOnScreen();
		} catch (IOException e) {
			Logger.getLogger(MenuOpzioniController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

}
