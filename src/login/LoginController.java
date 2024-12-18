package login;

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

import admin.AdminController;
import classi_tabelle.IoOperations;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/** 
 * CLASS FOR THE LOGIN MANAGEMENT SCREEN with the relative methods
 * to check the existence of users and the selection of the relative fields
 * 
 * @author Ciprian
 *
 */
public class LoginController implements Initializable {
	
	//----LOGIN scene----//
    @FXML private StackPane rootPane;
	@FXML private VBox VBoxLogin;
	@FXML private HBox HBox1Login;
	@FXML private ImageView iconaNomeLogin;
	@FXML private Label accediLabelLogin;
	@FXML private JFXTextField usernameLogin;
	@FXML private JFXPasswordField passwordLogin;
	@FXML private Hyperlink primoAccessoLabelLogin;	// If clicked -> initializationof the first access screen
	@FXML private Label newAccountLabelLogin;
	@FXML private Hyperlink registerLabelLogin;
	@FXML private JFXButton avantiButtonLogin;
	@FXML private AnchorPane anchorPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	@FXML 
	public void changeSceneToPrimoAccesso(ActionEvent event) {
		mostraPrimoAccesso();
	}
	
	/** METHOD TO PREPARE THE TRANSITION TO THE FIRST ACCESS SCREEN */
	private void mostraPrimoAccesso() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadPrimoAccesso();
		});
		fadeTransition.play();
	}
	
	/** METHOD TO LOAD THE SCREEN OF NEW ACCESS */
	private void loadPrimoAccesso() {
		try {
			Parent primoAccessoScene;
			primoAccessoScene = (StackPane) FXMLLoader.load(getClass().getResource("/primoAccesso/Primo_Accesso.fxml"));
			
			Scene primoAccesso = new Scene(primoAccessoScene);
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(primoAccesso);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML 
	public void changeSceneToRegistrazione(ActionEvent event) {
		mostraRegistrazione();
	}
	
	/** METHOD TO MAKE THE TRANSITION OF THE SCREEN TO THE ONE ABOUT THE REGISTRATION */
	private void mostraRegistrazione() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadRegistrazione();
		});
		
		fadeTransition.play();
	}
	
	/** METHOD TO LOAD THE REGISTRATION SCREEN */
	private void loadRegistrazione() {
		try {
			Parent registrazioneScene;
			registrazioneScene = (StackPane) FXMLLoader.load(getClass().getResource("/registrazione/Registrazione.fxml"));
			
			Scene registrazione = new Scene(registrazioneScene);
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(registrazione);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** METHOD TO MANAGE THE CLICK ON LOGIN BUTTON AND THE FIELDS CHECK */
	public void tryToLogIn() {	
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});

		// Username and Password fields check
		if(usernameLogin.getText() == null || usernameLogin.getText().length() == 0 ||
				passwordLogin.getText() == null || passwordLogin.getText().length() == 0 ) {
				
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}else {
			tryToConnect();
		}
	}
	
	/** METHOD FOR THE CONNECTION TO THE DATABASE AND TO CHECK USER EXISTENCE */
	private void tryToConnect() {
		if(IoOperations.login(usernameLogin.getText().toLowerCase(), passwordLogin.getText())){
			// The user exists -> identification of the user type
			int tipologia_utente = IoOperations.getTipologiaUtente(usernameLogin.getText().toLowerCase());
						
			// Admin -> classes/users screen
			if(tipologia_utente == 1) {
				mostraAdminScene();
			}
			// Teachers -> Test creation/vizualization scren
			else if(tipologia_utente == 2) {
				mostraDocentiScene();
			}
			// Students -> Test visualization/execution screen
			else {
				mostraStudentiScene();
			}
		}else {
			// User not found section code
			BoxBlur blur = new BoxBlur(3, 3, 3);
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			
			dialogLayout.setHeading(new Label("Utente non trovato. Controllare le credenziali."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
	}
	
	/** METOHOD TO PREPARE THE TRANSITION TO ADMIN SCENE */
	public void mostraAdminScene() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadAdminScene();
		});
		fadeTransition.play();
	}
	
	/** METHOD TO LOAD THE ADMIN SCENE */
	private void loadAdminScene() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene admin = new Scene(root);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(admin);
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** METOHOD TO PREPARE THE TRANSITION TO TEACHER SCENE */
	public void mostraDocentiScene() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadDocentiScene();
		});
		fadeTransition.play();
	}
	
	/** METHOD TO LOAD THE TEACHER SCENE */
	private void loadDocentiScene() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/docenti/docenti.fxml"));
			
			Scene admin = new Scene(root);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(admin);
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**METOHOD TO PREPARE THE TRANSITION TO STUDENT SCENE */
	public void mostraStudentiScene() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			loadStudentiScene();
		});
		fadeTransition.play();
	}
	
	/** METHOD TO LOAD THE STUDENT SCENE */
	private void loadStudentiScene() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/studenti/studenti.fxml"));
			
			Scene admin = new Scene(root);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(admin);
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
