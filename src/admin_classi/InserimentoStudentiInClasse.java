package admin_classi;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Class to manage the insertion of a student in a class in admin-class section
 */
public class InserimentoStudentiInClasse implements Initializable {
	
	 @FXML private StackPane rootPane;
	 @FXML private ChoiceBox<String> elencoClassi;
	 @FXML private TableView<Utente> tabellaStudenti;
	 @FXML private TableColumn<Utente, String> usernameColumn;
	 @FXML private JFXButton annullaButton;
	 @FXML private JFXButton confermaButton;
	 
	 ObservableList<Utente> valori = FXCollections.observableArrayList();
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<Utente> utenti = new ArrayList<Utente>();
		utenti = IoOperations.getStudentiCheNONAppartengonoAdUnaClasse();
				
		//Creazione elenco utenti e preparazione per caricarli sulla tabella
		int i = 0;
		while(i < utenti.size()) {
			valori.add(new Utente(utenti.get(i).getMatricola(), utenti.get(i).getNome(), utenti.get(i).getCognome(),
		 		utenti.get(i).getUsername(), utenti.get(i).getPassword(), utenti.get(i).getTipologia(),
		 		utenti.get(i).getClasse(), utenti.get(i).getEmail()));
		 	i++;
		}
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
	    tabellaStudenti.setItems(valori);
	    
	    tabellaStudenti.getSelectionModel().setSelectionMode(
	    	SelectionMode.MULTIPLE
	    );
	    
	    //Otteniento e inserimento delle classi nella choicebox
	  	ArrayList<Classe> elencco_classi = IoOperations.getElencoClassi();
	  	ObservableList<String> classi = FXCollections.observableArrayList();				
	  	i = 0;
	  	while(i < elencco_classi.size()) {
	  		classi.add(elencco_classi.get(i).getClasse());
	  		i++;
	  	}
	  	elencoClassi.setItems(classi);
	}
	
	@FXML
	/** Method to manage the Back button clicked from insertion user scene */
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
			Logger.getLogger(InserimentoStudentiInClasse.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage the click on confirmation in insertion of a student in a class scene */
	public void pulsanteConfermaInserimentoStudente() {
		if(elencoClassi.getValue() == null) {
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Inserimento studenti in una classe"));
			dialogLayout.setBody(new Label("Campo classe vuoto. Per continuare selezionare una classe in cui si inseriscano gli studenti."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else if(tabellaStudenti.getSelectionModel().getSelectedIndex() == -1) {
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Inserimento studenti in una classe"));
			dialogLayout.setBody(new Label("Attenzione! Si deve selezionare almeno una riga. per continuare."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else {
			ObservableList<Utente> elenco = tabellaStudenti.getSelectionModel().getSelectedItems();
			for(int i = 0; i < elenco.size(); i++) {
				IoOperations.inserisciStudenteInClasse(elenco.get(i).getUsername(), elencoClassi.getValue());
			}
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				pulsanteAnnullaCliccato();
			});
			dialogLayout.setHeading(new Label("Inserimento studenti in una classe"));
			dialogLayout.setBody(new Label("Inserimento degli studenti selezionati avvenuto con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}

}
