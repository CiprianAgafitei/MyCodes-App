package admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import classi_tabelle.IoOperations;
import classi_tabelle.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class to manage the insertion of the list of users from a file
 * 	USER DETAILS:
 * 		- first name
 * 		- surname
 * 		- email
 * 		- type
 */
public class InserimentoElencoUtenti implements Initializable {

	@FXML private StackPane rootPane;
    @FXML private JFXButton infoButton;
    @FXML private JFXButton caricaFile;
    @FXML private Label pathFile;
    @FXML private HBox middleHbox;
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> nomeColumn;
    @FXML private TableColumn<Utente, String> cognomeColumn;
    @FXML private TableColumn<Utente, String> emailColumn;
    @FXML private TableColumn<Utente, String> tipologiaColumn;
    @FXML private JFXButton indietro;
    @FXML private JFXButton rimuoviButton;
    @FXML private JFXButton conferma;
	
    List<String> fileTypes;
    ObservableList<Utente> utenti_nuovi;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	tabellaUtenti.setVisible(false);
    	middleHbox.setVisible(false);
    	middleHbox.managedProperty().bind(middleHbox.visibleProperty());
    	
		fileTypes = new ArrayList<String>();
		fileTypes.add("*.txt");
		fileTypes.add("*.TXT");
		fileTypes.add("*.odt");
		fileTypes.add("*.ODT");
		
		rimuoviButton.setVisible(false);
		
		nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
		cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		tipologiaColumn.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
	}
    
	@FXML
	/** Method to go bacj to admin main scene */
	private void tornaIndietro() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene login = new Scene(mainScene);
			login.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(InserimentoElencoUtenti.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to show details for the completion of new user info */
	public void infoButton() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		dialogLayout.setHeading(new Label("Requisiti file utenti nuovi"));
		dialogLayout.setBody(new Label("Il file deve prevedere 4 campi per ciascun utente: \n- NOME\n- COGNOME\n- EMAIL\n- TIPOLOGIA(A/D/S)\nA=admin; D=docente; S=studente\n(Tra i vari campi ci devono essere almeno 2 spazi)"));
		dialogLayout.setActions(button);
		dialog.show();
	}
	
	@FXML
	/** Method to load a file with the users */
	private void caricaFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		 
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text File", fileTypes);
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
        	try {
				caricamentoFile(file);
				pathFile.setText(file.getName());
				pathFile.setVisible(true);
				middleHbox.setVisible(true);
		    	middleHbox.managedProperty().bind(middleHbox.visibleProperty());
				tabellaUtenti.setVisible(true);
				rimuoviButton.setVisible(true);
			} catch (IOException e) {}
        }
        else {
        	middleHbox.setVisible(false);
        	middleHbox.managedProperty().bind(middleHbox.visibleProperty());
			pathFile.setVisible(false);
			tabellaUtenti.setVisible(false);
			rimuoviButton.setVisible(false);
        }
	}
	
	/** Method to analyze and load every user field info and add to the application */
	private void caricamentoFile(File file) throws IOException {
		utenti_nuovi = FXCollections.observableArrayList();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String nome; 		// user first name
		String cognome; 	// user surname
		String email; 		// user mail
		String tipologia; 	// user typology

		String line;
		while ((line = reader.readLine()) != null) 
		{
			nome = "";
			cognome = "";
			email = "";
			tipologia = "";
			int i = 0;
			int contattore = 0;
			int supporto = -1;
			
			// Extract the info from each row
			while (i < line.length()) 
			{
				char ch = line.charAt(i);
				// Check first first-name character
				if ((ch != ' ' || ch != '\t') && contattore == 0 && supporto == -1) {
					contattore++;
					supporto = i;
				}
				// Check end of fisrt name -> 2 spaces
				else if ((ch == ' ' || ch == '\t') && contattore == 1 && (line.charAt(i + 1) == ' ' || line.charAt(i + 1) == '\t') && nome.equals("")) {
					nome = line.substring(supporto, i);
					nome = eliminaCaratteriNonAccettabili(nome);
					supporto = -1;
				}
				// Check first character of surname
				else if (Character.isLetter(ch) && contattore == 1 && supporto == -1) {
					contattore++;
					supporto = i;
				}
				// Check end of surname
				else if ((ch == ' ' || ch == '\t') && (line.charAt(i + 1) == ' ' || line.charAt(i + 1) == '\t') && contattore == 2 && supporto != -1) {
					cognome = line.substring(supporto, i);
					cognome = eliminaCaratteriNonAccettabili(cognome);
					supporto = -1;
				}
				// Check first character of the email
				else if (Character.isLetter(ch) && contattore == 2 && supporto == -1) {
					contattore++;
					supporto = i;
				}
				// Check end of the mail
				else if ((ch == ' ' || ch == '\t') && contattore == 3 && supporto != -1) {
					email = line.substring(supporto, i);
					supporto = -1;
				}
				// Check first letter of tipology
				else if (Character.isLetter(ch) && contattore == 3 && supporto == -1) {
					tipologia += ch;
					break;
				}				
				i++;
			}
						
			// Check not empty fields -> user insertion
			if (!nome.equals("") && !cognome.equals("") && !email.equals("") && !tipologia.equals("")) {
				int tipo_account;
				tipologia = tipologia.toUpperCase();
				// Check tipology syntax correctness
				if (tipologia.charAt(0) == 'A')
					tipo_account = 1;
				else if (tipologia.charAt(0) == 'S')
					tipo_account = 3;
				else if (tipologia.charAt(0) == 'D' || tipologia.charAt(0) == 'T')
					tipo_account = 2;
				else
					tipo_account = 0;

				// Check maio correctness
				if (!validateMail(email))
					email = null;

				// Creation of new usern and add to db
				if (tipo_account != 0 && email != null) {
					// Creation the the new user adding it to the table
					Utente new_user = new Utente(nome, cognome, email, tipo_account);
					utenti_nuovi.add(new_user);
				}
			}
		}
		// Adding data in the table
		tabellaUtenti.setItems(utenti_nuovi);
		reader.close();
	}
	
	/** Check mail syntax correctness
     * 
     * @param email the mail to verify
     * @return true if the mail is correct; false otherways.
     */
	 private static boolean validateMail(String mail){
	    if (mail == null){
	    	return false;
	    }
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(mail);
		boolean matchFound = m.matches();
		
		// More strictly conditions
		String  expressionPlus="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pPlus = Pattern.compile(expressionPlus, Pattern.CASE_INSENSITIVE);
		Matcher mPlus = pPlus.matcher(mail);
		boolean matchFoundPlus = mPlus.matches();
		             
		return matchFound && matchFoundPlus;   
	 }
	 
	 /** Method to remove not alphanumerical characters from a string
	  * @param s the string from which to remove the not acceptable characters
	  */
	 private String eliminaCaratteriNonAccettabili(String s) {
		 String nuovo = "";
		 for(int i = 0; i < s.length(); i++) 
		 {
			 char ch = s.charAt(i);
			 // Check if the character is a letter
			 if(Character.isLetter(ch)) {
				 nuovo += ch;
				 if(i == 0)
					 nuovo.toUpperCase();
			 }
		 }
		 return nuovo;
	 }
	
	@FXML
	/** Method to confirm the isnertion of the users in the table */
	private void confermaInserimentoUtenti() {
		// Check empty table
		if(tabellaUtenti.getItems().isEmpty()) 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("File non inserito"));
			dialogLayout.setBody(new Label("ATTENZIONE! Non � stato inserito nessun file oppure non sono stati ricnonosciuti i campi richiesti."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		// Insertion of the users list
		else {
			for(int i = 0; i < utenti_nuovi.size(); i++) {
				IoOperations.createAccount(utenti_nuovi.get(i).getNome(), utenti_nuovi.get(i).getCognome(), utenti_nuovi.get(i).getNumeroTipologia(utenti_nuovi.get(i).getTipologia()), utenti_nuovi.get(i).getEmail());
			}
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				tornaIndietro();
			});
			dialogLayout.setHeading(new Label("Inserimento utenti"));
			dialogLayout.setBody(new Label("Inserimento degli utenti avvenuto con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}

	@FXML
	/** Method to remove a user */
	public void rimuoviUtente() {
		if(tabellaUtenti.getSelectionModel().getSelectedIndex() == -1) 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione utente selezionato"));
			dialogLayout.setBody(new Label("ATTENZIONE! Non � stato selezionato nessun utente dalla tabella."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else 
		{
			tabellaUtenti.getItems().removeAll();
			utenti_nuovi.remove(tabellaUtenti.getSelectionModel().getSelectedItem());
			tabellaUtenti.setItems(utenti_nuovi);
		}
	}
	
}
