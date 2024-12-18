package login;

import java.io.IOException;
import java.net.URISyntaxException;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Classe per la gestione della schermata del login con gli appositi metodi
 * per il ontrollo dell'esistenza degli utenti e di selezione dei vari campi
 * 
 * @author Ciprian
 *
 */
public class LoginController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_UTENTE;
	
	//----LOGIN scene----//
    @FXML private StackPane rootPane;
	@FXML private VBox VBoxLogin;
	@FXML private HBox HBox1Login;
	@FXML private ImageView iconaNomeLogin;
	@FXML private Label accediLabelLogin;
	@FXML private JFXTextField usernameLogin;
	@FXML private JFXPasswordField passwordLogin;
	@FXML private Hyperlink primoAccessoLabelLogin;	//Se cliccato, inizializzazione della scena di primo accesso
	@FXML private Label newAccountLabelLogin;
	@FXML private Hyperlink registerLabelLogin;
	@FXML private JFXButton avantiButtonLogin;
	@FXML private AnchorPane anchorPane;
	
	private boolean dialog_opened = false;
	private static String username;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			iconaNomeLogin.setImage(new Image(getClass().getResource("/images/Icona.png").toURI().toString()));
		} catch (URISyntaxException e) {
		}
		
		rootPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(!dialog_opened && ev.getCode() == KeyCode.ENTER) {
				tryToLogIn();
			}
		});
	}

	@FXML 
	public void changeSceneToPrimoAccesso(ActionEvent event) {
		mostraPrimoAccesso();
	}
	
	/** METODO PER PREPARARE LA TRANSIZIONE SULLA SCHERMATA DI PRIMO ACCESSO
	 */
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
	
	/** METODO PER CARICARE LA SCHERMATA DI PRIMO ACCESSO
	 */
	private void loadPrimoAccesso() {
		try {
			Parent primoAccessoScene;
			primoAccessoScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/Primo_Accesso.fxml"));
			
			Scene primoAccesso = new Scene(primoAccessoScene);
			primoAccesso.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
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
	
	/** METODO PER CONTROLLARE E RIMUOVERE GLI APOSTROFI DEI TESTI
	 * 
	 * @param testo è il testo da controllare
	 */
	private String controlloApostrofi(String testo) {
		String verificata = "";
		for(int i = 0; i < testo.length(); i++) {
			char ch = testo.charAt(i);
			if(ch == '\'')
				verificata += '"';
			else 
				verificata += ch;
		}
		return verificata;
	}
	
	/** METODO PER EFFETTUARE LA TRANSIZIONE DELLA SCHERMATA SU QUELLA DELLA REGISTRAZIONE
	 */
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
	
	/** METDOD PER CARICARE LA SCHERMATA DELLA REGISTRAZIONE
	 */
	private void loadRegistrazione() {
		try {
			Parent registrazioneScene;
			registrazioneScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/Registrazione.fxml"));
			
			Scene registrazione = new Scene(registrazioneScene);
			registrazione.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(registrazione);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**	METODO PER GESTIRE LA PRESSIONE DEL PULSANTE ACCEDI CON CONTROLLO DEI CAMPI
	 */
	public void tryToLogIn(){	
		dialog_opened = true;
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			dialog_opened = false;
		});
						
		if(usernameLogin.getText().length() == 0 || passwordLogin.getText().length() == 0) 
		{		
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			Label label = new Label("Attenzione! Non tutti i campi sono stati completati.");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
		else {
			tryToConnect();
		}
	}
	
	/** METODO PER LA CONNESSIONE AL DATABASE E CONTROLLO ESISTENZA UTENTE
	 */
	private void tryToConnect() {
		if(IoOperations.login(controlloApostrofi(usernameLogin.getText()).toLowerCase(), controlloApostrofi(passwordLogin.getText())) && !controlloApostrofi(passwordLogin.getText()).equals("null")){
			//Utente esiste -> identificazione della tipologia utente
			String tipologia_utente = IoOperations.getTipologiaUtente(controlloApostrofi(usernameLogin.getText()).toLowerCase());
						
			//Admin -> schermata gestione classi/utenti
			if(tipologia_utente.equalsIgnoreCase("ADMIN") || tipologia_utente.equalsIgnoreCase("AMMINISTRATORE") || tipologia_utente.equalsIgnoreCase("ADMINISTRATOR")) {
				mostraAdminScene();
			}
			//Docenti -> schermata creazione/visualizzazione verifiche
			else if(tipologia_utente.equalsIgnoreCase("DOCENTE") || tipologia_utente.equalsIgnoreCase("TEACHER")) {
				mostraDocentiScene();
			}
			//Studenti -> schermata esecuzione/visualizzazione verifiche
			else if(tipologia_utente.equalsIgnoreCase("STUDENTE") || tipologia_utente.equalsIgnoreCase("STUDENT")){
				mostraStudentiScene();
			}
		}else {
			BoxBlur blur = new BoxBlur(3, 3, 3);
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			Label label = new Label("Utente non trovato. Controllare le credenziali.");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
	}
	
	/** METODO PER PREPARARE LA TRANSIZIONE SULLA SCHERMATA DELL'AMMINISTRATORE
	 */
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
	
	/** METODO PER CARICARE LA SCHERMATA DELL'AMMINISTRATORE
	 */
	private void loadAdminScene() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene admin = new Scene(root);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.sizeToScene();
			pAStage.setScene(admin);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** METODO PER PREPARARE LA TRANSIZIONE SULLA SCHERMATA DEL DOCENTE
	 */
	public void mostraDocentiScene() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			username = usernameLogin.getText();
			loadDocentiScene();
		});
		fadeTransition.play();
	}
	
	/** METODO PER CARICARE LA SCHERMATA DEL DOCENTE
	 */
	private void loadDocentiScene() {
		try {
			//Impostato l'ID del docente che ha effettuato l'accesso
			setID_UTENTE();
			
			Parent root = FXMLLoader.load(getClass().getResource("/docenti/schermata_docenti.fxml"));
			
			Scene docenti = new Scene(root);
			docenti.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(docenti);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** METODO PER PREPARARE LA TRANSIZIONE SULLA SCHERMATA DEGLI STUDENTI
	 */
	public void mostraStudentiScene() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			username = usernameLogin.getText();
			loadStudentiScene();
		});
		fadeTransition.play();
	}
	
	/** METODO PER CARICARE LA SCHERMATA DEGLI STUDENTI
	 */
	private void loadStudentiScene() {
		try {
			//Impostato l'ID dello studente che ha effettuato l'accesso
			setID_UTENTE();
			
			Parent root = FXMLLoader.load(getClass().getResource("/studenti/SchermataPrincipaleStudenti.fxml"));
			
			Scene admin = new Scene(root);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(admin);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static int getID_UTENTE() {
		return ID_UTENTE;
	}

	public static void setID_UTENTE() {
		ID_UTENTE = IoOperations.getIDFromUsername(username);
	}
	
}
