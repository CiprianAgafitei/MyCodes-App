package docenti;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import classi_tabelle.IoOperations;
import classi_tabelle.Verifica;
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

/**
 * Class to manage the screen of the Teachers from teacher sction
 */
public class SchermataDocentiController implements Initializable {
	
	public static int ID_UTENTE;	// Teacher ID
	
	@FXML private StackPane rootPane;
	@FXML private Label nomeCognomeLabel;
    @FXML private JFXButton verificheButton;
    @FXML private JFXButton votiButton;
    @FXML private JFXButton domandeButton;
    @FXML private JFXButton impostazioniButton;
    @FXML private JFXButton logout;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_UTENTE = LoginController.getID_UTENTE();
		if(ID_UTENTE == 0)
			ID_UTENTE = PrimoAccessoController.getID_UTENTE();
		String[] nome_cognome_docente = new String[2];
		nome_cognome_docente[0] = IoOperations.getUtenteDatoID(ID_UTENTE).getNome();
		nome_cognome_docente[1] = IoOperations.getUtenteDatoID(ID_UTENTE).getCognome();
		nome_cognome_docente[0] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_docente[0]);
		nome_cognome_docente[1] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_docente[1]);
		nomeCognomeLabel.setText("Benvenuto " + nome_cognome_docente[0] + " " + nome_cognome_docente[1]);
		
		// Get all the test created by a tracher
		ArrayList<Verifica> verifiche = IoOperations.visualizzaVerificheSuDocentiNonAssegnate(ID_UTENTE);
		ArrayList<String>anni_verifica = new ArrayList<String>();
				
		//Inserimento date delle verifiche per riconoscere gli anni
		for(int i = 0; i < verifiche.size(); i++) {
			int anno_selezionato = LocalDate.parse(verifiche.get(i).getScadenza()).getYear();
			String confronto = "";
			if(LocalDate.parse(verifiche.get(i).getScadenza()).getMonthValue() <= 8)
				confronto = (anno_selezionato - 1) + "/" + anno_selezionato;
			else
				confronto = anno_selezionato + "/" + (anno_selezionato - 1);
				
			// CHeck if data has already been added
			if(!controlloAnnoPresente(anni_verifica, confronto)){
				anni_verifica.add(confronto);
			}
		}
	}
	
	public boolean controlloAnnoPresente(ArrayList<String>elenco, String anno) {
		for(int i = 0; i < elenco.size(); i++) {
			if(elenco.get(i).equals(anno))
				return true;
		}
		return false;
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
				admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
				
				Stage stage = (Stage) rootPane.getScene().getWindow();
				
				stage.setScene(admin);
				stage.setResizable(false);
				stage.setTitle("MyTest App");
			} catch (IOException e) {
				Logger.getLogger(SchermataDocentiController.class.getName()).log(Level.SEVERE, null, e);
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
	public void opzioneDomandeScelto() {
		try {
			Parent paginaDomande;
			paginaDomande = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_nuova_domanda/DomandeDocenti.fxml"));
			
			Scene admin = new Scene(paginaDomande);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setTitle("Area domande");
		} catch (IOException e) {
			Logger.getLogger(SchermataDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void opzioneVerificheScelto() {
		try {
			Parent paginaDomande;
			paginaDomande = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/Verifiche.fxml"));
			
			Scene admin = new Scene(paginaDomande);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setResizable(false);
			stage.setTitle("Verifiche docente");
		} catch (IOException e) {
			Logger.getLogger(SchermataDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static int getID_DOCENTE() {
		return ID_UTENTE;
	}
	
	@FXML
	public void impostazioniUtente() {
		try {
			Parent paginaDomande;
			paginaDomande = (StackPane) FXMLLoader.load(getClass().getResource("/docenti/ImpostazioniDocente.fxml"));
			
			Scene admin = new Scene(paginaDomande);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setResizable(false);
			stage.setTitle("Area personale");
		} catch (IOException e) {
			Logger.getLogger(SchermataDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void opzioneRisultatiVerificheScelto() {
		try {
			Parent paginaDomande;
			paginaDomande = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_voti/RisultatiVerifiche.fxml"));
			
			Scene admin = new Scene(paginaDomande);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setResizable(false);
			stage.setTitle("Risultati verifiche");
		} catch (IOException e) {
			Logger.getLogger(SchermataDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static int getID_UTENTE() {
		return ID_UTENTE;
	}
	
}
