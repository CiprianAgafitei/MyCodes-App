package studenti;

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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ImpostazioniStudentiController implements Initializable {

	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_STUDENTE;
		
	@FXML private StackPane rootPane;
	@FXML private BorderPane borderPane;
    @FXML private JFXButton indietroButton;
    @FXML private JFXTextField nome;
    @FXML private JFXTextField cognome;
    @FXML private JFXTextField username;
    @FXML private JFXTextField email;
    @FXML private Hyperlink cambiaPassword;
    
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_STUDENTE = SchermataPrincipaleStudentiController.getID_STUDENTE();
				
		nome.setText(IoOperations.rendiMaiuscolaPrimaLetteraNomi(IoOperations.getUtenteDatoID(ID_STUDENTE).getNome()));
		cognome.setText(IoOperations.rendiMaiuscolaPrimaLetteraNomi(IoOperations.getUtenteDatoID(ID_STUDENTE).getCognome()));
		username.setText(IoOperations.getUsernameFromIDUser(ID_STUDENTE));
		email.setText(IoOperations.getEmailFromIDUser(ID_STUDENTE));
		
		cambiaPassword.setOnAction(e -> {
			messaggioAvvertenzaCambioPassword();
		});
	}
	
	private void messaggioAvvertenzaCambioPassword() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("No");
		JFXButton button2 = new JFXButton("Si");
		button1.setStyle("-fx-background-color:RED; -fx-border-color:#1976D2; -fx-text-fill:#fff;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		button2.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill:#fff;"); 
		button2.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			//Invio email all'admin da quella dello studente
			try {
				sendEmailRequest();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});
		dialogLayout.setHeading(new Label("Opzione cambio password selezionata. Desidera procedere?"));
		dialogLayout.setBody(new Label("Procedendo verrà inviata la richiesta di cambio password all'amministratore, che procederà con la fase di reset. Riceverà una mail."));
		dialogLayout.setActions(button1, new Label("       "), button2, new Label("                                  "));
		dialog.show();
	}
	
	@FXML
	public void pulsanteTornaIndietro() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti/SchermataPrincipaleStudenti.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();

			stage.setScene(admin);
			//stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(SchermataPrincipaleStudentiController.class.getName()).log(Level.SEVERE, null, e);
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
		FROM = IoOperations.getEmailFromIDUser(ID_STUDENTE);
		FROMNAME = nome.getText() + " " + cognome.getText();
		SUBJECT = "Richiesta di modifica della password da " + IoOperations.getEmailFromIDUser(ID_STUDENTE);
		BODY = "RICHIESTA di MODIFICA DELLA PASSWORD di " + nome.getText() + " " + cognome.getText() + " .";
		
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
			pulsanteTornaIndietro();
		});
        
        try{
            //Connessione al SES tramite username e password SMTP sopra specificate 
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            dialogLayout.setHeading(new Label("Invio ..."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
            
            // Invio della email.
            transport.sendMessage(msg, msg.getAllRecipients());
            
            dialogLayout.setBody(new Label("Richiesta inviata all'amministratore per il cambio di password. \nRiceverete una mail con le istruzioni."
					+ "\nATTENZIONE: è possibile che la mail finisca nella SPAM."));
			
        }catch(javax.mail.MessagingException uhe) {
        	dialogLayout.setHeading(new Label("E' necessaria una connessione Internet per l'invio della richiesta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}catch (Exception ex) {
			dialogLayout.setHeading(new Label("Errore nell'invio dell'email. Si prega di riprovare. " +
					"Messaggio d'errore: " + ex.getMessage() + "\nSe l'errore persiste provare più tardi."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
        }finally{
            transport.close();	 // Close and terminate the connection.
        }
	}
    
}
