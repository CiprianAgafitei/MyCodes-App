package docenti_verifiche;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.Domanda;
import classi_tabelle.IoOperations;
import classi_tabelle.Risposta;
import classi_tabelle.Verifica;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StampaController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_DOCENTE;
		
	@FXML private StackPane rootPane;
	@FXML private TableView<Verifica> tabellaVerifiche;
    @FXML private TableColumn<Verifica, String> argomentoColonna;
    @FXML private JFXButton indietroButton;
    @FXML private JFXTextField cercaVerifica;
    @FXML private JFXButton stampaButton;

    ObservableList<Verifica> verifiche = FXCollections.observableArrayList();
	List<String> fileTypes;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		ID_DOCENTE = VerificheController.getID_DOCENTE();
		fileTypes = new ArrayList<String>();
		fileTypes.add("*.doc");
		fileTypes.add("*.docx");
		fileTypes.add("*.DOC");
		fileTypes.add("*.DOCX");

		// Inserimento verifiche nela tabella delle verifiche
		ArrayList<Verifica> tests = new ArrayList<Verifica>();
		tests = IoOperations.getVerifiche(ID_DOCENTE);
		
		// Creazione elenco utenti e preparazione per caricarli sulla tabella
		int i = 0;
		while (i < tests.size()) {
			verifiche.add(new Verifica(tests.get(i).getId_verifica(), tests.get(i).getArgomento(),
					tests.get(i).getIs_eseguita(), tests.get(i).getIs_assegnata(),
					tests.get(i).getPunteggio_ottenuto(), tests.get(i).getPunteggio_totale(),
					tests.get(i).getVoto(), tests.get(i).getTempo(), tests.get(i).getScadenza(),
					tests.get(i).getId_classe(), tests.get(i).getId_utente(),
					tests.get(i).getId_materia_docente()));
			i++;
		}
		argomentoColonna.setCellValueFactory(new PropertyValueFactory<>("argomento"));
		tabellaVerifiche.setItems(verifiche);

		// Ricerca campi verifica
		FilteredList<Verifica> filteredData2 = new FilteredList<>(verifiche, b -> true);

		cercaVerifica.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData2.setPredicate(verifica -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (verifica.getArgomento().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		SortedList<Verifica> sortedData2 = new SortedList<Verifica>(filteredData2);
		sortedData2.comparatorProperty().bind(tabellaVerifiche.comparatorProperty());
		tabellaVerifiche.setItems(sortedData2);
	}
	
	@FXML
	public void ritornaPaginaDocente() {
		try {
			Parent ritornoPaginaVerifiche;
			ritornoPaginaVerifiche = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/Verifiche.fxml"));
			
			Scene scena = new Scene(ritornoPaginaVerifiche);
			scena.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(scena);
			stage.setTitle("Verifiche docente");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(StampaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void confermaStampa() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Word File", fileTypes);
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			salvataggioSuFile(file);

			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Salvataggio su file"));
			dialogLayout.setBody(new Label("Salvataggio su file avvenuto con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}

	private void salvataggioSuFile(File file) {
		try {
			FileWriter fw = new FileWriter(file);

			fw.write("\t\t\tVERIFICA DI " + IoOperations.getMateriaDocente(tabellaVerifiche.getSelectionModel().getSelectedItem().getId_materia_docente()).getMateria() + " - " + tabellaVerifiche.getSelectionModel().getSelectedItem().getArgomento() + "\n\n");
			
			fw.append("NOME:" + "\t\t\t\t\t" + "COGNOME:" + "\t\t\t\t\t" + "CLASSE:" + IoOperations.getClasseFromIDClasse(tabellaVerifiche.getSelectionModel().getSelectedItem().getId_classe()) + "\n");
			
			char[] opzioni = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
			
			// Ottenimento elenco domande della verifica
			ArrayList<Domanda> domande = IoOperations.getDomandeVerifica(tabellaVerifiche.getSelectionModel().getSelectedItem().getId_verifica());
			
			for (int i = 0; i < domande.size(); i++) {
				// Scrittura su file della domanda
				fw.append((i + 1) + ". " + domande.get(i).getTesto() + "\n");
				
				// Ottenimento elenco risposte della domanda
				ArrayList<Risposta> risposte = IoOperations.getRisposteEsecuzioneVerifica(domande.get(i).getId_domanda());
				
				for(int j = 0; j < risposte.size(); j++) {
					// Scrittura su file della risposta
					fw.append("    " + opzioni[j] + ") " + risposte.get(j).getTesto() + "\n");
				}
				fw.append("\n\n");
			}
			fw.flush();
			fw.close();
			ritornaPaginaDocente();
		} catch (IOException e) {
			Logger.getLogger(StampaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

}
