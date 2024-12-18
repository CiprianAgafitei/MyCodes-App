package admin_utenti;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Class to manage the edits on a user in admin-users section
 */
public class ModificaUtente implements Initializable {
	
    @FXML private StackPane rootPane;
	@FXML private ChoiceBox<String> choiceboxUtente;
	@FXML private JFXButton annullaButton;
	@FXML private JFXButton confermaButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<Utente> elencoUtenti = IoOperations.getUtenti();
		ObservableList<String> utenti = FXCollections.observableArrayList();
				
		int i = 0;
		while(i < elencoUtenti.size()) {
			utenti.add(elencoUtenti.get(i).getUsername());
			i++;
		}
		choiceboxUtente.setItems(utenti);
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
			Logger.getLogger(ModificaUtente.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage confirmation of the edit of a password */
	public void pulsanteConfermaModificaPassword() {
		if(choiceboxUtente.getValue() == null) {
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Modifica di un account"));
			dialogLayout.setBody(new Label("Il campo nome utente � vuoto. Si prega di selezionare un utente da rimuovere."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else {
			IoOperations.resetPassword(choiceboxUtente.getValue());
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				pulsanteAnnullaCliccato();
			});
			dialogLayout.setHeading(new Label("Reset della password"));
			dialogLayout.setBody(new Label("Il reset della password � stato completato con successo"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}
}
