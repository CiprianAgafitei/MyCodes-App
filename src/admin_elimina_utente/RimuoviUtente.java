package admin_elimina_utente;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;

import admin.TendineMenuAdmin;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RimuoviUtente implements Initializable {
	
	@FXML private StackPane rootPane;
    @FXML private ChoiceBox<String> sceltaUtenteDaRimuovere;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton confermaButton;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		//Otteniento e inserimento degli utenti nella choicebox
		ArrayList<Utente> elencoUtenti = IoOperations.getUtenti();
		ObservableList<String> utenti = FXCollections.observableArrayList();
		
		int i = 0;
		while(i < elencoUtenti.size()) {
			utenti.add(elencoUtenti.get(i).getUsername());
			i++;
		}
		sceltaUtenteDaRimuovere.setItems(utenti);
	}
	
	@FXML
	public void pulsanteAnnullaCliccato() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Image icona = new Image("/resources/icona2.jpg");
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.getIcons().add(icona);
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(TendineMenuAdmin.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**Eliminazione dell'utente per il quale si indica l'username, con controllo che non 
	 * si stia eliminando un account amministratore.
	 */
	@FXML
	public void eliminaUtente() {
		if(sceltaUtenteDaRimuovere.getValue() == null){			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			
			dialogLayout.setHeading(new Label("Rimozione di un account"));
			dialogLayout.setBody(new Label("Il campo nome utente � vuoto. Si prega di selezionare un utente da rimuovere."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else if(IoOperations.getTipologiaUtente(sceltaUtenteDaRimuovere.getValue()) == 1) {			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			
			dialogLayout.setHeading(new Label("Rimozione di un account"));
			dialogLayout.setBody(new Label("ATTENZIONE! Non si possono rimuovere account amministratori!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else {
			IoOperations.removeUser(sceltaUtenteDaRimuovere.getValue());
						
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			
			dialogLayout.setHeading(new Label("Rimozione di un account"));
			dialogLayout.setBody(new Label("Eliminazione effettuata con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				pulsanteAnnullaCliccato();
			});
		}
	}
	
}
