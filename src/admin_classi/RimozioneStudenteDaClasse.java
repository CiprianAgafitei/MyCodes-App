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
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import classi_tabelle.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Class to manage the remove of a student from a class in admin-classes-section
 */
public class RimozioneStudenteDaClasse implements Initializable {
	
	@FXML private StackPane rootPane;
	@FXML private TableView<Utente> tabellaStudenti;
    @FXML private TableColumn<Utente, String> usernameColumn;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton confermaButton;
    @FXML private JFXTextField cercaUtente;
    
    ObservableList<Utente> valori = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ArrayList<Utente> utenti = new ArrayList<>();
		utenti = IoOperations.getStudentiCheAppartengonoAdUnaClasse();
		
		// Create the list of users and prepare to load them in the table
		int i = 0;
		while(i < utenti.size()) {
			valori.add(new Utente(utenti.get(i).getMatricola(), utenti.get(i).getNome(), utenti.get(i).getCognome(),
		 		utenti.get(i).getUsername(), utenti.get(i).getPassword(), utenti.get(i).getTipologia(),
		 		utenti.get(i).getClasse(), utenti.get(i).getEmail()));
		 	i++;
		}
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
		tabellaStudenti.setItems(valori);

		tabellaStudenti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Classes fields search
		FilteredList<Utente> filteredData2 = new FilteredList<>(valori, b -> true);

		cercaUtente.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData2.setPredicate(utente -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (utente.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		SortedList<Utente> sortedData2 = new SortedList<>(filteredData2);
		sortedData2.comparatorProperty().bind(tabellaStudenti.comparatorProperty());
		tabellaStudenti.setItems(sortedData2);
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
			Logger.getLogger(RimozioneStudenteDaClasse.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	/** Method to manage the confirmation of the remove of a student */
	public void pulsanteConfermaRimozioneStudente() {
		if (tabellaStudenti.getSelectionModel().getSelectedIndex() == -1) {
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione studenti dalla classe"));
			dialogLayout.setBody(new Label("Attenzione! Si deve selezionare almeno una riga."));
			dialogLayout.setActions(button);
			dialog.show();
		} else {
			ObservableList<Utente> elenco = tabellaStudenti.getSelectionModel().getSelectedItems();
			for (int i = 0; i < elenco.size(); i++) {
				IoOperations.removeStudentFromClass(elenco.get(i).getUsername());
			}
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				pulsanteAnnullaCliccato();
			});
			dialogLayout.setHeading(new Label("Rimozione studenti dalla classe"));
			dialogLayout.setBody(new Label("Gli studenti selezionati sono stati rimossi dalle loro classi."));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}

}
