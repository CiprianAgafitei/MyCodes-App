package docenti;

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

/**
 * Class for the settings of teachers in teacher-section
 */
public class ImpostazioniDocentiController implements Initializable{
	
	public static int ID_DOCENTE;			// Teacher ID to identify who accessed
		
	@FXML private StackPane rootPane;
    @FXML private BorderPane borderPane;
    @FXML private JFXButton indietroButton;
    @FXML private JFXTextField nome;
    @FXML private JFXTextField cognome;
    @FXML private JFXTextField username;
    @FXML private JFXTextField email;
    @FXML private Hyperlink cambiaPassword;
    
    private static String FROM;
    private static String FROMNAME;
    private static String TO;
    private static String SUBJECT;
    private static String BODY;
	
    private static final String SMTP_USERNAME = "gestione.mycode.app@gmail.com";	// Replace smtp_username with your SES SMTP username.
    private static final String SMTP_PASSWORD = "My#Code27&11";	// Replace smtp_password with your SES SMTP password.
    private static final String HOST = "smtp.gmail.com";	// SES SMTP host name.
    private static final int PORT = 587;	// The port you will connect to on SES SMTP endpoint. 

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_DOCENTE = SchermataDocentiController.getID_DOCENTE();
		
		nome.setText(IoOperations.rendiMaiuscolaPrimaLetteraNomi(IoOperations.getUtenteDatoID(ID_DOCENTE).getNome()));
		cognome.setText(IoOperations.rendiMaiuscolaPrimaLetteraNomi(IoOperations.getUtenteDatoID(ID_DOCENTE).getCognome()));
		username.setText(IoOperations.getUsernameFromIDUser(ID_DOCENTE));
		email.setText(IoOperations.getEmailFromIDUser(ID_DOCENTE));
		
		cambiaPassword.setOnAction(e -> {
			messaggioAvvertenzaCambioPassword();
		});
	}
    
    @FXML
    void pulsanteTornaIndietro() {
    	try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/docenti/schermata_docenti.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setTitle("Menu principale docenti");
		} catch (IOException e) {
			Logger.getLogger(ImpostazioniDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
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
			
			// Sending of the mail to admin
			try {
				sendEmailRequest();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});
		dialogLayout.setHeading(new Label("Opzione cambio password selezionata. Desidera procedere?"));
		dialogLayout.setBody(new Label("Procedendo verra inviata la richiesta di cambio password all'amministratore, che procedera con la fase di reset. Ricevera una mail."));
		dialogLayout.setActions(button1, new Label("       "), button2, new Label("                                  "));
		dialog.show();
	}
    
    /** SENDING OF THE MAIL FOR THE REQUEST */
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
        
		TO = "gestione.mycode.app@gmail.com";
		FROM = IoOperations.getEmailFromIDUser(ID_DOCENTE);
		FROMNAME = nome.getText() + " " + cognome.getText();
		SUBJECT = "Richiesta di modifica della password da " + IoOperations.getEmailFromIDUser(ID_DOCENTE);
		BODY = "RICHIESTA di MODIFICA DELLA PASSWORD di " + username.getText() + " .";
		
		MimeMessage msg = new MimeMessage(session);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setSubject(SUBJECT);
        msg.setText(BODY);
        
        // Preparation of the screen loading
        BoxBlur blur = new BoxBlur(3, 3, 3);
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			pulsanteTornaIndietro();
		});
        
        try
        {
        	dialogLayout.setHeading(new Label("Invio ..."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
        	
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            
            transport.sendMessage(msg, msg.getAllRecipients());
            
            dialogLayout.setBody(new Label("Richiesta inviata all'amministratore per il cambio di password. \nRiceverete una mail con le istruzioni."
					+ "\nATTENZIONE: � possibile che la mail finisca nella SPAM."));
        }
        catch(javax.mail.MessagingException uhe) 
        {
        	dialogLayout.setHeading(new Label("E' necessaria una connessione Internet per l'invio della richiesta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
        catch (Exception ex) 
        {
			dialogLayout.setHeading(new Label("Errore nell'invio dell'email. Si prega di riprovare. " +
					"Messaggio d'errore: " + ex.getMessage() + "\nSe l'errore persiste provare pi� tardi."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
        }
        finally
        {
            transport.close();	 // Close and terminate the connection.
        }
	}
	
}
