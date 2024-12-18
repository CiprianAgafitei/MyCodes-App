package login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Classe per la gestione della schermata di primo accesso con metodi per
 * il controllo dell'esistenza dell'username inserito
 * 
 * @author Ciprian
 *
 */
public class PrimoAccessoController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_UTENTE;
	public static String codice;

	//----PRIMO ACCESSO scene----//
    @FXML private StackPane rootPane;
	@FXML private JFXTextField nomeUtentePrimoAccesso;
	@FXML private JFXButton continuaButtonPrimoAccesso;
    @FXML private JFXButton indietroButtonPrimoAccesso;
    @FXML private AnchorPane anchorPane;
    
    private static String FROM;			//email mittente
    private static String FROMNAME;		//Nome mittente
    private static String TO;			//email destinatario
    private static String SUBJECT;	 	//Oggetto della mail
    private static String BODY;			//Corpo della mail 
	
    private static final String SMTP_USERNAME = "gestione.mycode.app@gmail.com";	// Replace smtp_username with your SES SMTP username.
    private static final String SMTP_PASSWORD = "My#Code27&11";	// Replace smtp_password with your SES SMTP password.
    private static final String HOST = "smtp.gmail.com";	// SES SMTP host name.
    private static final int PORT = 587;	// The port you will connect to on SES SMTP endpoint. 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rootPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(ev.getCode() == KeyCode.ENTER) {
				finishRegistration();
			}
		});
	}
	
	@FXML 
	public void changeSceneToLogin(ActionEvent event) {
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
			Logger.getLogger(PrimoAccessoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void finishRegistration() {
		// Controllo esistenza username inserito
		if(IoOperations.controlloEsistenzaUsername(nomeUtentePrimoAccesso.getText())) {
			codice = generazioneCodice();
			try {
				sendEmailRequest(IoOperations.getEmailFromIDUser(IoOperations.getIDFromUsername(nomeUtentePrimoAccesso.getText())), codice);
				setIdUtenteToLoggedIn();
				
				try {
					Parent mainScene;
					mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/ControlloCodice.fxml"));
					
					Scene login = new Scene(mainScene);
					Stage pAStage = (Stage) rootPane.getScene().getWindow();
					
					pAStage.setScene(login);
				} catch (IOException e) {
					Logger.getLogger(PrimoAccessoController.class.getName()).log(Level.SEVERE, null, e);
				}
			} 
			catch (UnsupportedEncodingException e) {} 
			catch (MessagingException e) {}
		}
		else {
			// Comunicazione: utente non trovato
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
    		JFXButton button = new JFXButton("Ok");
    		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
    		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
    		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
    			dialog.close();
    		});
    		dialogLayout.setHeading(new Label("Inserimento nome utente"));
    		dialogLayout.setBody(new Label("Attenzione! Il nome utente inserito non esiste."));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}
	
	/**INVIO DELLA EMAIL DI RICHIESTA PER LA REGISTRAZIONE AL SISTEMA
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void sendEmailRequest(String email, String codice) throws MessagingException, UnsupportedEncodingException {
		// Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);        
        Transport transport = session.getTransport();	 // Create a transport
        
        //Preparazione dati per l'invio della richiesta
		TO = email;
		FROM = "gestione.mycode.app@gmail.com";
		FROMNAME = "Sistema di registrazione MyTest App";
		SUBJECT = "Richiesta di modifica della password da " + email;
		BODY = "Il codice necessario per la modifica della password è " + codice;
		
		MimeMessage msg = new MimeMessage(session);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setSubject(SUBJECT);
        msg.setText(BODY);
		
        try{			
            //Connessione al SES tramite username e password SMTP sopra specificate 
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Invio della email.
            transport.sendMessage(msg, msg.getAllRecipients());
        }catch (Exception ex) {
        	//Preparazione finestra di caricamento
            BoxBlur blur = new BoxBlur(3, 3, 3);
    		JFXDialogLayout dialogLayout = new JFXDialogLayout();
    		JFXButton button = new JFXButton("Ok");
    		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
    		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
    		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
    			dialog.close();
    			mostraLogin();
    		});
    		dialog.show();
			dialogLayout.setHeading(new Label("Errore nell'invio dell'email. Si prega di riprovare. " +
					"\nMessaggio d'errore: " + ex.getMessage() + "\nSe l'errore persiste provare più tardi."));
			dialogLayout.setActions(button);
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
        }finally{
            transport.close();	 // Close and terminate the connection.
        }
	}
	
	/** GENERAZIONE DEL CODICE DI SICUREZZA DA INVIARE VIA MAIL ALL'UTENTE DI CUI SI CONOSCE L'USERNAME
	 * @return la stringa con il codice da spedire
	 */
	private String generazioneCodice() {
		String top_secret = "";
		
		for(int i = 0; i < 6; i++) {
			int n = (int)(Math.random()*10);
			top_secret += n;
		}
		
		return top_secret;
	}
	
	public void setIdUtenteToLoggedIn() {
		ID_UTENTE = IoOperations.getIDFromUsername(nomeUtentePrimoAccesso.getText());
	}

	public static int getID_UTENTE() {
		return ID_UTENTE;
	}

	public static String getCodice() {
		return codice;
	}
	
}
