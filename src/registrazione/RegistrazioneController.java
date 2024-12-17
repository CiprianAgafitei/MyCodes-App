package registrazione;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.LoginController;

/**Classe per la schermata relativa alla registrazione e suoi passi necessari,
 * con metodi di inoltro di una email di rischiesta di registrazione all'amministratore.
 * 
 * @author Ciprian
 *
 */
public class RegistrazioneController implements Initializable {
	
	//----REGISTRAZIONE scene----//
	@FXML private StackPane rootPane;
	@FXML private JFXTextField nomeRegistrazione;
	@FXML private JFXTextField cognomeRegistrazione;
	@FXML private JFXTextField emailRegistrazione;
	@FXML private JFXButton continuaButtonRegistrazione;
    @FXML private JFXButton indietroButtonRegistrazione;
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
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**L'utente preme sul pulsante "Invia richiesta" all'amministratore per
	 * effettuare la nuova registrazione al sistema
	 * @param event
	 * @throws Exception
	 */
	public void submitRegistrazione() {
		BoxBlur blur = new BoxBlur(3, 3, 3);

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		//Campi vuoti
		if(nomeRegistrazione.getText() == null || nomeRegistrazione.getText().length() == 0 ||
				cognomeRegistrazione.getText() == null || cognomeRegistrazione.getText().length() == 0 ||
				emailRegistrazione.getText() == null || emailRegistrazione.getText().length() == 0) {
			
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}
		//Controllo correttezza sintassi email
		else if(!validateMail(emailRegistrazione.getText())) {
			
			dialogLayout.setHeading(new Label("Attenzione! La sintassi della email non è corretta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}else {
			try {
				sendEmailRequest();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**INVIO DELLA EMAIL DI RICHIESTA PER LA REGISTRAZIONE AL SISTEMA
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void sendEmailRequest() throws MessagingException, UnsupportedEncodingException {
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
		TO = "gestione.mycode.app@gmail.com";
		FROM = emailRegistrazione.getText();
		FROMNAME = nomeRegistrazione.getText() + " " + cognomeRegistrazione.getText();
		SUBJECT = "Richiesta di creazione account da " + emailRegistrazione.getText();
		BODY = "RICHIESTA di REGISTRAZIONE di " + nomeRegistrazione.getText() + " " + cognomeRegistrazione.getText() + " al sistema.";
		
		MimeMessage msg = new MimeMessage(session);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setSubject(SUBJECT);
        msg.setText(BODY);
        
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
        
        try{
        	dialogLayout.setHeading(new Label("Invio ..."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
			
			ProgressIndicator p = new ProgressIndicator(); 
			dialogLayout.setBody(p);
			
            //Connessione al SES tramite username e password SMTP sopra specificate 
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Invio della email.
            transport.sendMessage(msg, msg.getAllRecipients());
            
            dialogLayout.setBody(new Label("Richiesta inviata all'amministratore per la vostra registrazione. \nRiceverete una mail con l'username e i passi da seguire."
					+ "\nATTENZIONE: è possibile che la mail finisca nella SPAM."));
			
        }catch(javax.mail.MessagingException uhe) {
        	dialogLayout.setHeading(new Label("E' necessaria una connessione Internet per l'invio della richiesta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
		}catch (Exception ex) {
			dialogLayout.setHeading(new Label("Errore nell'invio dell'email. Si prega di riprovare. " +
					"Messaggio d'errore: " + ex.getMessage() + "\nSe l'errore persiste provare più tardi."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				anchorPane.setEffect(null);
			});
			anchorPane.setEffect(blur);
        }finally{
            transport.close();	 // Close and terminate the connection.
        }
	}
	
	/** VERIFICA DELLA VALIDITA' DIN UN INDIRIZZO EMAIL
     * 
     * @param email è l'email da verificare
     * @return true se lindirizzo email è corretto; false se non corretto.
     */
	 public static boolean validateMail(String mail){
	    	if (mail == null){
	    		return false;
	    	}
		    Pattern p = Pattern.compile(".+@.+\\.[a-z]+", Pattern.CASE_INSENSITIVE);
		    Matcher m = p.matcher(mail);
		    boolean matchFound = m.matches();
		
		    //Condizioni più restrittive rispetto alle precedenti
		    String  expressionPlus="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		    Pattern pPlus = Pattern.compile(expressionPlus, Pattern.CASE_INSENSITIVE);
		    Matcher mPlus = pPlus.matcher(mail);
		    boolean matchFoundPlus = mPlus.matches();
		             
		    return matchFound && matchFoundPlus;   
	 }
	
}