package docenti_nuova_domanda;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import classi_tabelle.Materia_Docenti;
import docenti_verifiche.VerificheController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class to manage the add of a new Question in teachers section
 */
public class NuovaDomanda implements Initializable {
	
	private int ID_DOCENTE = 0;			// ID of the teacher who wants to create a new question
	
	@FXML private StackPane rootPane;
	@FXML private VBox vbox;
	@FXML private ChoiceBox<String> choiceMateria;
    @FXML private JFXTextField testoDomanda;
    @FXML private JFXButton caricaImmagine;
    @FXML private JFXTextField r1;
    @FXML private JFXCheckBox check1;
    @FXML private ChoiceBox<Double> choice1;
    @FXML private JFXButton carica1;
    @FXML private HBox hbox2;
    @FXML private JFXTextField r2;
    @FXML private JFXCheckBox check2;
    @FXML private ChoiceBox<Double> choice2;
    @FXML private JFXButton carica2;
    @FXML private HBox hbox3;
    @FXML private JFXTextField r3;
    @FXML private JFXCheckBox check3;
    @FXML private ChoiceBox<Double> choice3;
    @FXML private JFXButton carica3;
    @FXML private HBox hbox4;
    @FXML private JFXTextField r4;
    @FXML private JFXCheckBox check4;
    @FXML private ChoiceBox<Double> choice4;
    @FXML private JFXButton carica4;
    @FXML private HBox hbox5;
    @FXML private JFXTextField r5;
    @FXML private JFXCheckBox check5;
    @FXML private ChoiceBox<Double> choice5;
    @FXML private JFXButton carica5;
    @FXML private HBox hbox6;
    @FXML private JFXTextField r6;
    @FXML private JFXCheckBox check6;
    @FXML private ChoiceBox<Double> choice6;
    @FXML private JFXButton carica6;
    @FXML private HBox hbox7;
    @FXML private JFXTextField r7;
    @FXML private JFXCheckBox check7;
    @FXML private ChoiceBox<Double> choice7;
    @FXML private JFXButton carica7;
    @FXML private HBox hbox8;
    @FXML private JFXTextField r8;
    @FXML private JFXCheckBox check8;
    @FXML private ChoiceBox<Double> choice8;
    @FXML private JFXButton carica8;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton aggiungiRisposta;
    @FXML private JFXButton salvaButton;
    
    private FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
    private FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
    private FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
    
    private int id_img_domanda;		private File file_d;
    private int id_img_risposta1;	private File file_r1;
    private int id_img_risposta2;	private File file_r2;
    private int id_img_risposta3;	private File file_r3;
    private int id_img_risposta4;	private File file_r4;
    private int id_img_risposta5;	private File file_r5;
    private int id_img_risposta6;	private File file_r6;
    private int id_img_risposta7;	private File file_r7;
    private int id_img_risposta8;	private File file_r8;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		ID_DOCENTE = DomandeDocentiController.getID_UTENTE();
		if(ID_DOCENTE == 0)
			ID_DOCENTE = VerificheController.getID_DOCENTE();
			
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterJPEG);
		
		file_d = null; file_r1 = null; file_r2 = null; file_r3 = null; file_r4 = null;
		file_r5 = null; file_r6 = null; file_r7 = null; file_r8 = null;
		
		hbox3.setVisible(false);
		hbox4.setVisible(false);
		hbox5.setVisible(false);
		hbox6.setVisible(false);
		hbox7.setVisible(false);
		hbox8.setVisible(false);
				
		ArrayList<Double> elenco_punteggi = new ArrayList<Double>();
		elenco_punteggi.add(-1.0);
		elenco_punteggi.add(-0.5);
		elenco_punteggi.add(-0.25);
		elenco_punteggi.add(0.0);
		elenco_punteggi.add(0.25);
		elenco_punteggi.add(0.5);
		elenco_punteggi.add(1.0);
		elenco_punteggi.add(1.5);
		elenco_punteggi.add(2.0);
		ObservableList<Double> punteggi = FXCollections.observableArrayList();
		punteggi.addAll(elenco_punteggi);
		
		choice1.setItems(punteggi);
		choice1.setValue(0.0);
		choice2.setItems(punteggi);
		choice2.setValue(0.0);
		choice3.setItems(punteggi);
		choice3.setValue(0.0);
		choice4.setItems(punteggi);
		choice4.setValue(0.0);
		choice5.setItems(punteggi);
		choice5.setValue(0.0);
		choice6.setItems(punteggi);
		choice6.setValue(0.0);
		choice7.setItems(punteggi);
		choice7.setValue(0.0);
		choice8.setItems(punteggi);
		choice8.setValue(0.0);
		
		check1.setOnAction(e -> {
			if(check1.isSelected() && choice1.getValue() == 0.0)
				choice1.setValue(1.0);
			else if(!check1.isSelected() && choice1.getValue() > 0.0)
				choice1.setValue(0.0);
		});
		
		check2.setOnAction(e -> {
			if(check2.isSelected() && choice2.getValue() == 0.0)
				choice2.setValue(1.0);
			else if(!check2.isSelected() && choice2.getValue() > 0.0)
				choice2.setValue(0.0);
		});
		
		check3.setOnAction(e -> {
			if(check3.isSelected() && choice3.getValue() == 0.0)
				choice3.setValue(1.0);
			else if(!check3.isSelected() && choice3.getValue() > 0.0)
				choice3.setValue(0.0);
		});
		
		check4.setOnAction(e -> {
			if(check4.isSelected() && choice4.getValue() == 0.0)
				choice4.setValue(1.0);
			else if(!check4.isSelected() && choice4.getValue() > 0.0)
				choice4.setValue(0.0);
		});
		
		check5.setOnAction(e -> {
			if(check5.isSelected() && choice5.getValue() == 0.0)
				choice5.setValue(1.0);
			else if(!check5.isSelected() && choice5.getValue() > 0.0)
				choice5.setValue(0.0);
		});
		
		check6.setOnAction(e -> {
			if(check6.isSelected() && choice6.getValue() == 0.0)
				choice6.setValue(1.0);
			else if(!check6.isSelected() && choice6.getValue() > 0.0)
				choice6.setValue(0.0);
		});
		
		check7.setOnAction(e -> {
			if(check7.isSelected() && choice7.getValue() == 0.0)
				choice7.setValue(1.0);
			else if(!check7.isSelected() && choice7.getValue() > 0.0)
				choice7.setValue(0.0);
		});
		
		check8.setOnAction(e -> {
			if(check8.isSelected() && choice8.getValue() == 0.0)
				choice8.setValue(1.0);
			else if(!check8.isSelected() && choice8.getValue() > 0.0)
				choice8.setValue(0.0);
		});
		
		ArrayList<Materia_Docenti> materie = IoOperations.getElencoMaterieDocente(IoOperations.getIDFromUsername(IoOperations.getUsernameFromIDUser(ID_DOCENTE)));
		ObservableList<String> valori = FXCollections.observableArrayList();
		
		//Creazione elenco utenti e preparazione per caricarli sulla tabella
		int j = 0;
		while (j < materie.size()) {
			valori.add(materie.get(j).getMateria());
			j++;
		}
		choiceMateria.setItems(valori);
	}
	
	@FXML
	/** Methdo to manage back button clicked */
	public void pulsanteAnnullaCliccato() {
		try {
			Parent schermataVerifiche;
			schermataVerifiche = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_nuova_domanda/DomandeDocenti.fxml"));
			
			Scene scena = new Scene(schermataVerifiche);
			scena.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(scena);
			stage.setResizable(false);
			stage.setTitle("Verifiche docente");
		} catch (IOException e) {
			Logger.getLogger(NuovaDomanda.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** 
	 * Method to check and remove the apostrophes
	 * @param testo the text to check
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
	
	@FXML
	/** Method to add answer */
	public void aggiungiRisposta() {
		Parent root = aggiungiRisposta.getScene().getRoot();
		
		if(r1.getText().length() == 0 || r2.getText().length() == 0) 
		{
			// Field 1 and 2 must be filled
			mostraAvviso("Campo della risposta 1 o 2 devono essere completati prima si aggiungere altre risposte");
		}
		else if(r1.getText().length() != 0 && r2.getText().length() != 0 && !hbox3.isVisible()) {
			hbox3.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);		
		}
		else if(r3.isVisible() && r3.getText().length() == 0)
		{
			// Field 3 must be completed
			mostraAvviso("Campo della risposta 3 deve essere completato prima di aggiungere altre risposte.");
		}
		else if(r3.getText().length() != 0 && !hbox4.isVisible()) {
			hbox4.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);
		}
		else if(r4.isVisible() && r4.getText().length() == 0)
		{
			// Field 4 must be completed
			mostraAvviso("Campo della risposta 4 deve essere completato prima di aggiungere altre risposte.");
		}
		else if(r4.getText().length() != 0 && !hbox5.isVisible()) {
			hbox5.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);
		}
		else if(r5.isVisible() && r5.getText().length() == 0)
		{
			// Field 5 must be completed
			mostraAvviso("Campo della risposta 5 deve essere completato prima di aggiungere altre risposte.");
		}
		else if(r5.getText().length() != 0 && !hbox6.isVisible()) {
			hbox6.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);
		}
		else if(r6.isVisible() && r6.getText().length() == 0) 
		{
			// Field 6 must be completed
			mostraAvviso("Campo della risposta 6 deve essere completato prima di aggiungere altre risposte.");
		}
		else if(r6.getText().length() != 0 && !hbox7.isVisible()) {
			hbox7.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);
		}
		else if(r7.isVisible() && r7.getText().length() == 0) 
		{
			// Field 7 must be completed
			mostraAvviso("Campo della risposta 7 deve essere completato prima di aggiungere altre risposte.");
		}
		else if(r7.getText().length() != 0 && !hbox8.isVisible()) {
			hbox8.setVisible(true);
			aggiungiRisposta.getScene().setRoot(root);
		}
		else {
			mostraAvviso("Numero di risposte massimo raggiunto.");
		}
	}
	
	public void mostraAvviso(String s) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		dialogLayout.setHeading(new Label("Creazione domanda"));
		dialogLayout.setBody(new Label(s));
		dialogLayout.setActions(button);
		dialog.show();
	}
	
	@FXML
	public void pulsanteSalvaDomanda() {
		if(testoDomanda.getText().length() == 0 || testoDomanda.getText() == null)
		{
			// Question field empty
			mostraAvviso("Attenzione! Campo testo della domanda vuoto.");
		}
		else if(choiceMateria.getValue() == null) {
			// Subject field empty
			mostraAvviso("Attenzione! Campo materia della domanda vuoto.");
		}
		else if(!check1.isSelected() && !check2.isSelected() && !check3.isSelected() && !check4.isSelected() && 
				!check5.isSelected() && !check6.isSelected() && !check7.isSelected() && !check8.isSelected()) 
		{
			// No asnwer selected as correct
			mostraAvviso("Attenzione! Non e stata selezionata nessuna risposta corretta");
		}
		else if((check1.isSelected() && choice1.getValue() <= 0.0) || (check2.isSelected() && choice2.getValue() <= 0.0) ||
				(check3.isSelected() && choice3.getValue() <= 0.0) || (check4.isSelected() && choice4.getValue() <= 0.0) ||
				(check5.isSelected() && choice5.getValue() <= 0.0) || (check6.isSelected() && choice6.getValue() <= 0.0) ||
				(check7.isSelected() && choice7.getValue() <= 0.0) || (check8.isSelected() && choice8.getValue() <= 0.0)) 
		{
			//Controllo se risposte selezionate come corrette hanno punteggio > 0.0
			mostraAvviso("Attenzione! Le risposte selezionate come corrette devono avere un punteggio maggiore di 0.");
		}
		else {
			try {
				id_img_domanda = IoOperations.inserisciImmagine(file_d.getName(), file_d.getPath());
			}catch(NullPointerException npe) {}
			int ID_domanda = IoOperations.creaDomanda(controlloApostrofi(testoDomanda.getText()), choiceMateria.getValue(), 0, id_img_domanda);
			
			//Creazione risposte con controllo che i campi delle risposte non siano vuoti
			if(r1.getText().length() != 0) {
				try {
					id_img_risposta1 = IoOperations.inserisciImmagine(file_r1.getName(), file_r1.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r1.getText()), choice1.getValue(), (check1.isSelected() + ""), ID_domanda, id_img_risposta1);
			}
			if(r2.getText().length() != 0) {
				try {
					id_img_risposta2 = IoOperations.inserisciImmagine(file_r2.getName(), file_r2.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r2.getText()), choice2.getValue(), (check2.isSelected() + ""), ID_domanda, id_img_risposta2);
			}
			if(r3.getText().length() != 0) {
				try {
					id_img_risposta3 = IoOperations.inserisciImmagine(file_r3.getName(), file_r3.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r3.getText()), choice3.getValue(), (check3.isSelected() + ""), ID_domanda, id_img_risposta3);
			}
			if(r4.getText().length() != 0) {
				try {
					id_img_risposta4 = IoOperations.inserisciImmagine(file_r4.getName(), file_r4.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r4.getText()), choice4.getValue(), (check4.isSelected() + ""), ID_domanda, id_img_risposta4);
			}
			if(r5.getText().length() != 0) {
				try {
					id_img_risposta5 = IoOperations.inserisciImmagine(file_r5.getName(), file_r5.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r5.getText()), choice5.getValue(), (check5.isSelected() + ""), ID_domanda, id_img_risposta5);
			}
			if(r6.getText().length() != 0) {
				try {
					id_img_risposta6 = IoOperations.inserisciImmagine(file_r6.getName(), file_r6.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r6.getText()), choice6.getValue(), (check6.isSelected() + ""), ID_domanda, id_img_risposta6);
			}
			if(r7.getText().length() != 0) {
				try {
					id_img_risposta7 = IoOperations.inserisciImmagine(file_r7.getName(), file_r7.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r7.getText()), choice7.getValue(), (check7.isSelected() + ""), ID_domanda, id_img_risposta7);
			}
			if(r8.getText().length() != 0) {
				try {
					id_img_risposta8 = IoOperations.inserisciImmagine(file_r8.getName(), file_r8.getPath());
				}catch(NullPointerException npe) {}
				IoOperations.creaRisposta(controlloApostrofi(r8.getText()), choice8.getValue(), (check8.isSelected() + ""), ID_domanda, id_img_risposta8);
			}
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				pulsanteAnnullaCliccato();
			});
			dialogLayout.setHeading(new Label("Creazione domanda"));
			dialogLayout.setBody(new Label("Domanda creata con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}
	
	@FXML
	public void caricaImmagineDomanda() { 
		file_d = fileChooser.showOpenDialog(null);  
	}
	
	@FXML
	public void caricaImmagineRisposta1() { 
		file_r1 = fileChooser.showOpenDialog(null);       
	}
	
	@FXML
	public void caricaImmagineRisposta2() { 
		file_r2 = fileChooser.showOpenDialog(null);  
	}
	
	@FXML
	public void caricaImmagineRisposta3() { 
		file_r3 = fileChooser.showOpenDialog(null);       
	}
	
	@FXML
	public void caricaImmagineRisposta4() { 
		file_r4 = fileChooser.showOpenDialog(null); 
	}
	
	@FXML
	public void caricaImmagineRisposta5() { 
		file_r5 = fileChooser.showOpenDialog(null); 
	}
	
	@FXML
	public void caricaImmagineRisposta6() { 
		file_r6 = fileChooser.showOpenDialog(null);    
	}
	
	@FXML
	public void caricaImmagineRisposta7() { 
		file_r7 = fileChooser.showOpenDialog(null);  
	}
	
	@FXML
	public void caricaImmagineRisposta8() { 
		file_r8 = fileChooser.showOpenDialog(null); 
	}
	
}
