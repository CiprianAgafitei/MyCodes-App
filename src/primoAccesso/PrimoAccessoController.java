package primoAccesso;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import classi_tabelle.IoOperations;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.LoginController;

/**
 * CLASS TO MANAGE THE FIRST ACCESS SCREEN WITH METHODS TO CHECK IF A USERNAME ALREADY EXISTS
 * 
 * @author Ciprian
 *
 */
public class PrimoAccessoController implements Initializable {

	//----PRIMO ACCESSO scene----//
    @FXML private StackPane rootPane;
	@FXML private JFXTextField nomeUtentePrimoAccesso;
	@FXML private JFXPasswordField nuovaPasswordPrimoAccesso;
	@FXML private JFXButton continuaButtonPrimoAccesso;
    @FXML private JFXButton indietroButtonPrimoAccesso;
    @FXML private AnchorPane anchorPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML 
	public void changeSceneToLogin(ActionEvent event) {
		mostraLogin();
	}

	/* Show Login screen */
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

	/* Load login scene */
	private void loadMainView() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/* Check conditions to finalize the registration */
	public void finishRegistration() {
		controllNotEmptyTextFields();
	}

	/* Check not empty fields
 	 * User field -> not empty & is the one given by the admin
   	 * Password field -> not empty & length: > 5 & < 10
   	*/
	private void controllNotEmptyTextFields() {
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});

		// Check not empty fields of user name and password
		if(nomeUtentePrimoAccesso.getText() == null || nomeUtentePrimoAccesso.getText().length() == 0 ||
				nuovaPasswordPrimoAccesso.getText() == null || nuovaPasswordPrimoAccesso.getText().length() == 0) {
			
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
		// Check password length
		else if(nuovaPasswordPrimoAccesso.getText().length() < 5 || nuovaPasswordPrimoAccesso.getText().length() > 10) {
			
			dialogLayout.setHeading(new Label("Attenzione! La password deve essere\n di lunghezza compresa tra 5 e 10"));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
		// Username not recognized
		else if(!IoOperations.controlloEsistenzaUsername(nomeUtentePrimoAccesso.getText().toLowerCase())) {
			
			dialogLayout.setHeading(new Label("Username non riconosciuto. Controllare che sia quello fornito dall'amministratore."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
		else {
			// Load the user page by its tipology
			
		}
	}
	
}
