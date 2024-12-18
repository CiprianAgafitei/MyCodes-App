package admin_materie_docenti;

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
import classi_tabelle.Materia_Docenti;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class to manage the remove the assigned subject from a teacher in admin subject-teachers-section
 */
public class RimuoviAssegnazioneController implements Initializable {

	@FXML private StackPane rootPane;
    @FXML private BorderPane borderPane;
    @FXML private ChoiceBox<String> choiceDocente;
    @FXML private ChoiceBox<String> choiceMateria;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton confermaButton;
    
    private boolean operazione_effettuata = false;
    ObservableList<String> values;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<Utente> docenti = IoOperations.getDocenti();
		values = FXCollections.observableArrayList();
		
		// Creation of the list of teachers and prepare them to load in the choicebox
		int i = 0;
		while (i < docenti.size()) {
			values.add(docenti.get(i).getUsername());
			i++;
		}
		choiceDocente.setItems(values);
		
		choiceDocente.setOnAction(e -> {
			if(choiceDocente.getValue() != null) {
				ArrayList<Materia_Docenti> materie = IoOperations.getElencoMaterieDocente(IoOperations.getIDFromUsername(choiceDocente.getValue()));
				ObservableList<String> valori = FXCollections.observableArrayList();
				
				// Creation of the users list and load them in the table
				int j = 0;
				while (j < materie.size()) {
					valori.add(materie.get(j).getMateria());
					j++;
				}
				choiceMateria.setItems(valori);
			}
		});
	}

	@FXML
	/** Method to manage back button clicked */
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
			Logger.getLogger(RimuoviAssegnazioneController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage confirmation button clicked */
	public void pulsanteConferma() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			if(operazione_effettuata)
				pulsanteAnnullaCliccato();
		});
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		Label label = new Label("Assegnazione materia ad un docente");
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		label.setWrapText(true);
		hbox.getChildren().add(label);
		dialogLayout.setHeading(hbox);
		dialogLayout.setActions(button);
		
		if(choiceDocente.getValue() == null || choiceMateria.getValue() == null) {
			dialogLayout.setBody(new Label("Si prega di completare tutti i campi richiesti."));
			dialog.show();
		}
		else {
			// Remove the assigned subject from the teacher
			IoOperations.rimuoviAssegnazioneMateriaDocente(IoOperations.getIDFromUsername(choiceDocente.getValue()), choiceMateria.getValue());
			
			operazione_effettuata = true;
			dialogLayout.setBody(new Label("L'associazione materia-docente selezionata e stata rimossa con successo!"));
			dialog.show();
		}
	}
	
}
