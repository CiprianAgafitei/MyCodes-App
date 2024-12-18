package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlloCodiceController implements Initializable {

	public String codice_numerico;
	public static int ID_UTENTE;
	
	@FXML private StackPane rootPane;
    @FXML private AnchorPane anchorPane;
    @FXML private JFXTextField codice;
    @FXML private JFXButton indietroButtonPrimoAccesso;
    @FXML private JFXButton continuaButtonPrimoAccesso;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	codice_numerico = PrimoAccessoController.getCodice();
    	ID_UTENTE = PrimoAccessoController.getID_UTENTE();
	}
    
    @FXML
    void changeSceneToLogin() {
    	mostraLogin();
    }
    
    private void mostraLogin() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadMainView();
		});
		fadeTransition.play();
	}
    
    private void loadMainView() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(ControlloCodiceController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

    @FXML
    void continueRegistration() {
    	// Controllo corrispondeza codice
    	if(codice.getText().equals(codice_numerico)) {
    		try {
    			Parent mainScene;
    			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/ModificaPassword.fxml"));
    			
    			Scene login = new Scene(mainScene);
    			Stage pAStage = (Stage) rootPane.getScene().getWindow();
    			
    			pAStage.setScene(login);
    		} catch (IOException e) {
    			Logger.getLogger(ControlloCodiceController.class.getName()).log(Level.SEVERE, null, e);
    		}
    	}
    	else if(codice.getText().isEmpty()) {
    		// Messaggio di errore -> campo vuoto
    		JFXDialogLayout dialogLayout = new JFXDialogLayout();
    		JFXButton button = new JFXButton("Ok");
    		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
    		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
    		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
    			dialog.close();
    		});
    		dialogLayout.setHeading(new Label("Inserimento codice"));
    		dialogLayout.setBody(new Label("Attenzione! Il campo è vuoto."));
			dialogLayout.setActions(button);
			dialog.show();
    	}
    	else {
    		// Messaggio di errore -> codice incorretto
    		JFXDialogLayout dialogLayout = new JFXDialogLayout();
    		JFXButton button = new JFXButton("Ok");
    		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
    		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
    		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
    			dialog.close();
    		});
    		dialogLayout.setHeading(new Label("Inserimento codice"));
    		dialogLayout.setBody(new Label("Attenzione! Il codice inserito non è corretto!"));
			dialogLayout.setActions(button);
			dialog.show();
    	}
    }
    
	public static int getID_UTENTE() {
		return ID_UTENTE;
	}

}
