package admin_materie_docenti;

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
import classi_tabelle.IoOperations;
import classi_tabelle.Materia_Docenti;
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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class to manage the print of all teachers list from admin subject-teachers-section
 */
public class StampaDocentiController implements Initializable {

	@FXML private StackPane rootPane;
    @FXML private TableView<Docente_Materia> tabellaDocenti;
    @FXML private TableColumn<Docente_Materia, String> nomeColumn;
    @FXML private TableColumn<Docente_Materia, String> cognomeColumn;
    @FXML private TableColumn<Docente_Materia, String> usernameColumn;
    @FXML private TableColumn<Docente_Materia, String> materiaColumn;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton scaricaButton;
    
    ObservableList<Docente_Materia> materieDocenti = FXCollections.observableArrayList();
    List<String> fileTypes;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
    	fileTypes = new ArrayList<String>();
		fileTypes.add("*.doc");
		fileTypes.add("*.docx");
		fileTypes.add("*.DOC");
		fileTypes.add("*.DOCX");
    	
    	// Teachers and related subject lists
		ArrayList<Materia_Docenti> materie_docenti = new ArrayList<Materia_Docenti>();
		materie_docenti = IoOperations.getElencoMaterieDocenti();
		ArrayList<Utente> docenti = new ArrayList<Utente>();
		docenti = IoOperations.getDocenti();
    	
    	// Insertion of the teachers that have a subject
    	for(int i = 0; i < materie_docenti.size(); i++) 
    	{
    		for(int j = 0; j < docenti.size(); j++) 
    		{
    			// Check if got the selected subject there is a teacher -> add in the list
    			if(materie_docenti.get(i).getId_docente() == docenti.get(j).getMatricola())
    				materieDocenti.add(new Docente_Materia(docenti.get(j).getNome(), docenti.get(j).getCognome(), docenti.get(j).getUsername(), materie_docenti.get(i).getMateria()));
    		}
    	}
    	
    	nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    	materiaColumn.setCellValueFactory(new PropertyValueFactory<>("materia"));
    	tabellaDocenti.setItems(materieDocenti);
	}
    
    @FXML
	/** Method to manage back button clicked */
    public void pulsanteAnullaCliccato() {
    	try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene login = new Scene(mainScene);
			login.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(StampaDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
    }
    
    @FXML
	/** Method to manage File print confirmation */
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
				pulsanteAnullaCliccato();
			});
			dialogLayout.setHeading(new Label("Salvataggio su file"));
			dialogLayout.setBody(new Label("Salvataggio su file avvenuto con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}

	/**
	 * Method to save the students from selected classes on a file
	 */
	private void salvataggioSuFile(File file) {
		try {
			FileWriter fw = new FileWriter(file);

			fw.write("\t\tELENCO DOCENTI - MATERIE \n\n");
			fw.write("-------------------------------------------------------\n");
			fw.append("    " + "NOME" + "\t\t" + "COGNOME" + "\t\t" + "USERNAME" + "\t\t" + "MATERIA" + "\n");
			
			for (int i = 0; i < materieDocenti.size(); i++) {
				// Writing on file
				fw.append((i + 1) + ". " + materieDocenti.get(i).getNome() + "\t\t" + materieDocenti.get(i).getCognome() + "\t\t" + materieDocenti.get(i).getUsername() + "\t\t" + materieDocenti.get(i).getMateria() + "\n");
				
				fw.append("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
