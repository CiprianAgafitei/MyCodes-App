package studenti;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import classi_tabelle.IoOperations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import login.LoginController;
import login.PrimoAccessoController;

/** Classe relativa alla schermata principale degli studenti, dopo aver effettuato il corretto accesso all'account.
 * 
 * Prevede 3 opzioni principali tra cui scegliere:
 * 	- Visualizza verifiche da eseguire
 *  - Visualizza verifiche gi� eseguite
 *  - Visualizza impostazioni account personale
 *  
 *  L'utente ha inoltre l'opzione di uscita dal proprio account, in alto a destra
 *  
 * @author Ciprian Agafitei
 *
 */
public class SchermataPrincipaleStudentiController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_STUDENTE;
	
	@FXML private StackPane rootPane;
	@FXML private Label nomeCognomeLabel;
    @FXML private JFXButton verificheButton;
    @FXML private JFXButton votiButton;
    @FXML private JFXButton logout;
    @FXML private JFXButton userSettings;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_STUDENTE = LoginController.getID_UTENTE();
		if(ID_STUDENTE == 0)
			ID_STUDENTE = PrimoAccessoController.getID_UTENTE();
		String[] nome_cognome_studente = new String[2];
		nome_cognome_studente[0] = IoOperations.getUtenteDatoID(ID_STUDENTE).getNome();
		nome_cognome_studente[1] = IoOperations.getUtenteDatoID(ID_STUDENTE).getCognome();
		nome_cognome_studente[0] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[0]);
		nome_cognome_studente[1] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[1]);
		nomeCognomeLabel.setText("Benvenuto " + nome_cognome_studente[0] + " " + nome_cognome_studente[1]);
	}
	
	public boolean controlloAnnoPresente(ArrayList<String>elenco, String anno) {
		for(int i = 0; i < elenco.size(); i++) {
			if(elenco.get(i).equals(anno))
				return true;
		}
		return false;
	}
	
	@FXML
	public void pulsanteVerificheSelezionato() {
		try {
			Parent paginaVerifiche;
			paginaVerifiche = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_verifiche/StudentiVerifiche.fxml"));
			
			Scene verifiche = new Scene(paginaVerifiche);
			verifiche.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(verifiche);
			//stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(SchermataPrincipaleStudentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void pulsanteVotiSelezionato() {
		try {
			Parent paginaVoti;
			paginaVoti = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_voti/StudentiVoti.fxml"));
			
			Scene voti = new Scene(paginaVoti);
			voti.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(voti);
			//stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(SchermataPrincipaleStudentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static int getID_STUDENTE() {
		return ID_STUDENTE;
	}
	
	@FXML
	public void logOut() {		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("No");
		JFXButton button2 = new JFXButton("Si");
		button1.setStyle("-fx-background-color:RED; -fx-border-color:red; -fx-text-fill:#fff;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		button2.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill:#fff;"); 
		button2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			try {
				Parent ritornoPaginaAdmin;
				ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
				
				Scene admin = new Scene(ritornoPaginaAdmin);
				
				Stage stage = (Stage) rootPane.getScene().getWindow();
				
				stage.setScene(admin);
				stage.setResizable(false);
				stage.setTitle("MyTest App");
			} catch (IOException e) {
				Logger.getLogger(SchermataPrincipaleStudentiController.class.getName()).log(Level.SEVERE, null, e);
			}
			
		});
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		Label l = new Label("Sei sicuro di voler uscire dal tuo account?");
		l.setFont(Font.font("Verdana", 18));
		h.getChildren().add(l);
		dialogLayout.setHeading(h);
		dialogLayout.setActions(button1, new Label("       "), button2, new Label("                                  "));
		dialog.show();
	}
	
	@FXML
	public void opzioneImpostazioniStudenti() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti/ImpostazioniStudente.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setTitle("Area riservata");
		} catch (IOException e) {
			Logger.getLogger(SchermataPrincipaleStudentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
