package admin_utenti;

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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class to print the list of students in admin-users-section
 */
public class StampaStudentiController implements Initializable {

	@FXML private StackPane rootPane;
    @FXML private TableView<Classe> tabellaClassi;
    @FXML private TableColumn<Classe, String> classeColumn;
    @FXML private TableColumn<Classe, String> nrStudentiColumn;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton confermaStampa;
	
    private ObservableList<Classe> elenco_classi = FXCollections.observableArrayList();
    List<String> fileTypes;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		fileTypes = new ArrayList<String>();
		fileTypes.add("*.doc");
		fileTypes.add("*.docx");
		fileTypes.add("*.DOC");
		fileTypes.add("*.DOCX");
		
		ArrayList<Classe> classi = new ArrayList<>();
		classi = IoOperations.getElencoClassi();
		
		int i = 0;
		while(i < classi.size()) {
			elenco_classi.add(new Classe(classi.get(i).getId_classe(), classi.get(i).getClasse(), classi.get(i).getAnno_scolastico(), classi.get(i).getNumero_studenti()));
		 	i++;
		}
		
		classeColumn.setCellValueFactory(new PropertyValueFactory<>("classe"));
	    nrStudentiColumn.setCellValueFactory(new PropertyValueFactory<>("numero_studenti"));
	    tabellaClassi.setItems(elenco_classi);
	    
	    // Multi-selection of the rows
	    tabellaClassi.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	@FXML
	/** Method to manage back button selected */
	public void pulsanteAnullaCliccato() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene login = new Scene(mainScene);
			login.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(StampaStudentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage confirmation of the print of students */
	public void confermaStampaSelezionati() {
		// Check if at least one row selected
		if(tabellaClassi.getSelectionModel().getSelectedIndex() == -1) 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Salvataggio su file"));
			dialogLayout.setBody(new Label("Attenzione! Non sono state selezionate classi!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else 
		{
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
	}
	
	/** Method to save the selected student on a file */
	private void salvataggioSuFile(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			
			// Inserimento studenti classi selezionate
			ObservableList<Classe> classi = FXCollections.observableArrayList();
			classi = tabellaClassi.getSelectionModel().getSelectedItems();
			
			for(int i = 0; i < classi.size(); i++) {
				// Ottenimento studenti delle classe
				ArrayList<Utente> studenti_classe = new ArrayList<Utente>();
				studenti_classe = IoOperations.getStudentiDaClasse(classi.get(i).getId_classe());
				
				// Scrittura su file
				fw.write("\t\t\tSTUDENTI CLASSE " + classi.get(i).getClasse() + "\n\n");
				fw.write("-------------------------------------------------------\n");
				fw.append("    " + "NOME" + "\t\t\t" + "COGNOME" + "\t\t" + "USERNAME" + "\n");
				for(int j = 0; j < studenti_classe.size(); j++) {
					fw.append((j + 1) + ". " + studenti_classe.get(j).getNome() + "\t\t\t" + studenti_classe.get(j).getCognome() + "\t\t\t" + studenti_classe.get(j).getUsername() + "\n");
				}
				fw.append("\n\n\n");
			}
			fw.flush();
			fw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
