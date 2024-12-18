package admin_classi;

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
import classi_tabelle.Classe;
import classi_tabelle.IoOperations;
import classi_tabelle.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class to manage the edits on a class in admin-class section
 */
public class ModificaClasseController implements Initializable {
	
	@FXML private StackPane rootPane;
	@FXML private ChoiceBox<String> choiceboxClasse;
    @FXML private Label yearLabel;
    @FXML private TableView<Utente> tabellaStudenti;
    @FXML private TableColumn<Utente, String> idStudente;
    @FXML private TableColumn<Utente, String> nomeStudente;
    @FXML private TableColumn<Utente, String> cognomeStudente;
    @FXML private TableColumn<Utente, String> usernameStudente;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton promuoviButton;

    ObservableList<Utente> studenti_classe = FXCollections.observableArrayList();
	private boolean promossi = false;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ArrayList<Classe> classi = IoOperations.getElencoClassi();
		ObservableList<String> sezioni_classi = FXCollections.observableArrayList();
		
		// Adding the classes names in the list
		for(int i = 0; i < classi.size(); i++) {
			sezioni_classi.add(classi.get(i).getClasse());
		}
		// Adding the values in the choicebox
		choiceboxClasse.setItems(sezioni_classi);
		
		yearLabel.setVisible(false);
		
		idStudente.setCellValueFactory(new PropertyValueFactory<>("matricola"));
		nomeStudente.setCellValueFactory(new PropertyValueFactory<>("nome"));
		cognomeStudente.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		usernameStudente.setCellValueFactory(new PropertyValueFactory<>("username"));
		tabellaStudenti.setItems(studenti_classe);
		
		choiceboxClasse.setOnAction(e -> {
			if(!choiceboxClasse.getValue().isEmpty()) {
				// Remove the students from other classes
				studenti_classe = FXCollections.observableArrayList();
				tabellaStudenti.setItems(studenti_classe);
				
				yearLabel.setVisible(true);
				// Update the label with correct year
				String anno_scolastico = IoOperations.getAnnoScolasticoFromClasse(choiceboxClasse.getValue());
				yearLabel.setText(anno_scolastico);
				
				// Getting the id of the selected class
				int id_classe = IoOperations.getIdClasseFromAnnoESezione(choiceboxClasse.getValue());
				// Getting the students of selected class
				ArrayList<Utente> studenti = IoOperations.getStudentiDaClasse(id_classe);
				
				// Insertion of the students in the list
				for(int i = 0; i < studenti.size(); i++) {
					studenti_classe.add(studenti.get(i));
				}
				tabellaStudenti.setItems(studenti_classe);
			}
		});
		
		tabellaStudenti.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE
		);
	}
	
	@FXML
	/** Method to manage the click on back button */
	public void pulsanteAnnullaCliccato() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			admin.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(ModificaClasseController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage the promotion of students to a next-year-class */
	public void pulsantePromuoviCliccato() {		
		// Get the current year
		String as_successivo = "";
		LocalDate data = LocalDate.now();
		as_successivo += data.getYear() + "/" + (data.getYear() + 1);
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			if(promossi)
				pulsanteAnnullaCliccato();
		});
		dialogLayout.setHeading(new Label("Promozione studenti"));
		
		// Check class field empty
		if(choiceboxClasse.getValue() == null) 
		{
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			Label label = new Label("Si prega di selezionare una classe e gli studenti da promuovere.");
			label.setWrapText(true);
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			dialogLayout.setBody(label);
			dialogLayout.setActions(button);
			dialog.show();
		}
		// Check if selected students to promote
		else if(tabellaStudenti.getSelectionModel().getSelectedIndex() == -1) 
		{
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			Label label = new Label("Non sono stati selezionati studenti da promuovere alla classe successiva.");
			label.setWrapText(true);
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			dialogLayout.setBody(label);
			dialogLayout.setActions(button);
			dialog.show();
		}
		// Check if selected class was already updated to new year
		else if(yearLabel.getText().equals(as_successivo)) 
		{
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			Label label = new Label("Gli studenti della classe selezionata sono gia stati promossi quest'anno.");
			label.setWrapText(true);
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			dialogLayout.setBody(label);
			dialogLayout.setActions(button);
			dialog.show();
		}
		else 
		{
			// Get the students promoted
			ObservableList<Utente> studentiPromossi = FXCollections.observableArrayList();
			studentiPromossi.addAll(tabellaStudenti.getSelectionModel().getSelectedItems());
			
			// Get the next year
			char ch = choiceboxClasse.getValue().charAt(0);
			int anno = Integer.parseInt(ch + "");
			anno += 1;
			
			// If last year
			if(anno > 5) 
			{
				// The student is set as he absolved and cannot be added to a class
				for (int i = 0; i < studentiPromossi.size(); i++) {
					IoOperations.aggiornaTipologiaStudentiDiplomati(studentiPromossi.get(i).getUsername());
					// Remove of the student from a class
					IoOperations.removeStudentFromClass(studentiPromossi.get(i).getUsername());
				}
			}
			else
			{
				String nuovo_anno = anno + "" + choiceboxClasse.getValue().charAt(1);
				
				if(!IoOperations.controlloClasseEsistente(nuovo_anno)) 
				{
					HBox hbox = new HBox();
					hbox.setAlignment(Pos.CENTER);
					Label label = new Label("Attenzione! La classe per l'anno " + nuovo_anno + " non esiste. Si prega di crearla e riprovare.");
					label.setWrapText(true);
					label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
					dialogLayout.setBody(label);
					dialogLayout.setActions(button);
					dialog.show();
				}
				else 
				{
					// Insertion of the student in a class
					for (int i = 0; i < studentiPromossi.size(); i++) {
						IoOperations.removeStudentFromClass(studentiPromossi.get(i).getUsername());
						IoOperations.inserisciStudenteInClasse(studentiPromossi.get(i).getUsername(), nuovo_anno);
					}
					// Updating the year of the class to next one
					IoOperations.aggiornaAnnoScolasticoClasse(nuovo_anno, as_successivo);
					
					HBox hbox = new HBox();
					hbox.setAlignment(Pos.CENTER);
					Label label = new Label("Gli studenti selezionati sono stati promossi con successo!");
					label.setWrapText(true);
					label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
					dialogLayout.setBody(label);
					dialogLayout.setActions(button);
					dialog.show();
					
					promossi = true;
				}
			}
		}
	}

}
