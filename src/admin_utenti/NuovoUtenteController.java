package admin_utenti;

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

import classi_tabelle.IoOperations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class to manage the add of a new user in admin-users section
 */
public class NuovoUtenteController implements Initializable {

	@FXML private StackPane rootPane;
	@FXML private BorderPane borderPane;
    @FXML private JFXTextField textfieldNome;
    @FXML private JFXTextField textfieldCognome;
    @FXML private JFXTextField textfieldEmail;
    @FXML private JFXButton confermaButton;
    @FXML private ChoiceBox<Integer> choiceboxTipologia;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton inviaMailButton;
    
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
		choiceboxTipologia.getItems().addAll(1, 2, 3);
	}
	
	@FXML
	public void closeThisWindow() {
		mostraSchermataAdmin();
	}
	
	/** Method to go back to admin main scene */
	private void mostraSchermataAdmin() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene login = new Scene(mainScene);
			login.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(NuovoUtenteController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the screen to create a new account */
	public void loadCreazioneAccount() throws MessagingException {
		BoxBlur blur = new BoxBlur(3, 3, 3);

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		// Empty fields
		if(textfieldNome.getText() == null || textfieldNome.getText().length() == 0 ||
				textfieldCognome.getText() == null || textfieldCognome.getText().length() == 0 ||
						textfieldEmail.getText() == null || textfieldEmail.getText().length() == 0 ||
								choiceboxTipologia.getValue() == null) 
		{	
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		// Check syntax mail correctness
		else if(!validateMail(textfieldEmail.getText())) 
		{	
			dialogLayout.setHeading(new Label("Attenzione! La sintassi della email non � corretta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		else 
		{
			IoOperations.createAccount(textfieldNome.getText().toLowerCase(), textfieldCognome.getText().toLowerCase(), choiceboxTipologia.getValue(), textfieldEmail.getText().toLowerCase());
			mostraSchermataAdmin();
		}
	}
	
	@FXML
	/** Method to send the mail info request for the creation of a new user */
	public void sendMailInfo() {
		BoxBlur blur = new BoxBlur(3, 3, 3);

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		// Empty fields
		if (textfieldNome.getText() == null || textfieldNome.getText().length() == 0
				|| textfieldCognome.getText() == null || textfieldCognome.getText().length() == 0
				|| textfieldEmail.getText() == null || textfieldEmail.getText().length() == 0
				|| choiceboxTipologia.getValue() == null) 
		{
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		// Check mail correctenss of the syntax
		else if (!validateMail(textfieldEmail.getText())) 
		{
			dialogLayout.setHeading(new Label("Attenzione! La sintassi della email non � corretta."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		else 
		{
			try {
				String[]risposta = IoOperations.createAccount(textfieldNome.getText().toLowerCase(), textfieldCognome.getText().toLowerCase(), choiceboxTipologia.getValue(), textfieldEmail.getText().toLowerCase());
				
				// Check if registrazione completed correctly
				sendConfirmEmailRegistration (risposta[1]);
				mostraSchermataAdmin();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Method to send the email for the registration request */
	private void sendConfirmEmailRegistration(String username) throws MessagingException, UnsupportedEncodingException {
		// Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);        
        Transport transport = session.getTransport();	 // Create a transport
        
        // Data preparation for the mail send
		TO = textfieldEmail.getText();
		FROM = "gestione.mycode.app@gmail.com";
		FROMNAME = "Registrazione MyCodes";
		SUBJECT = "Conferma di avvenuta registrazione al sistema di " + textfieldEmail.getText();
		BODY = "La registrazione da lei richiesta � avvenuta con successo!\n\n"
				+ "Il suo username �: " + username + "\n\nPer terminare la registrazione, al prossimo accesso:\n  - selezionare"
						+ " la voce \"Password dimenticata?\"\n  - inserire il codice che ricever� via mail\n  - creare la nuova password\n  - accedere al sistema";
		
		MimeMessage msg = new MimeMessage(session);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setSubject(SUBJECT);
        msg.setText(BODY);
   
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
		
		try{
			// Connection to the SES through username and password SMTP 
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            
            // Sending of the mail
            transport.sendMessage(msg, msg.getAllRecipients());
                        
            // View of the message
            Label label = new Label("Email spedita");
    		label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
    		label.setWrapText(true);
    		hbox.getChildren().add(label);
    		dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				rootPane.setEffect(null);
			});
			rootPane.setEffect(blur);
		} 
		catch (javax.mail.MessagingException uhe) 
		{
			// Network failure message required to send the mail
            Label label = new Label("E' necessaria una connessione Internet per l'invio della richiesta.");
    		label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
    		label.setWrapText(true);
    		hbox.getChildren().add(label);
    		dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				rootPane.setEffect(null);
			});
			rootPane.setEffect(blur);
		}
		catch (Exception ex) 
		{			
			// Error message sending the mail
            Label label = new Label("Errore nell'invio dell'email. Si prega di riprovare." + "Messaggio d'errore: " + ex.getMessage() + "\nSe l'errore persiste provare pi� tardi.");
    		label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
    		label.setWrapText(true);
    		hbox.getChildren().add(label);
    		dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				rootPane.setEffect(null);
			});
			rootPane.setEffect(blur);
		}
		finally {
			transport.close(); // Close and terminate the connection.
		}
	}
	
	/** Check the correctness of the sending of a mail
     * @param email the mail to check
     * @return true sif the mail syntax is correct; false otherways.
     */
	 public boolean validateMail(String mail){
		if (mail == null) {
			return false;
		}
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(mail);
		boolean matchFound = m.matches();

		String expressionPlus = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pPlus = Pattern.compile(expressionPlus, Pattern.CASE_INSENSITIVE);
		Matcher mPlus = pPlus.matcher(mail);
		boolean matchFoundPlus = mPlus.matches();

		return matchFound && matchFoundPlus;
	}
	
}
