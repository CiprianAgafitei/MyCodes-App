package admin_materie_docenti;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class to manage the add of a new subject in admin subject-teachers-section
 */
public class NuovaMateriaController implements Initializable {

	@FXML private StackPane rootPane;
    @FXML private BorderPane borderPane;
    @FXML private JFXTextField nuovaMateriaTextfield;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton confermaButton;
	
    private boolean operazione_effettuata = false;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
			Logger.getLogger(NuovaMateriaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to create a new subject */
	public void creaNuovaMateria() {		
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
		Label label = new Label("Creazione materia");
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		label.setWrapText(true);
		hbox.getChildren().add(label);
		dialogLayout.setHeading(hbox);
		dialogLayout.setActions(button);
		
		// Create the new subject if not already exists
		if(nuovaMateriaTextfield.getText().isEmpty()) {
			dialogLayout.setBody(new Label("Il campo materia � vuoto. Si prega di inserire una materia."));
			dialog.show();
		}
		else if(!IoOperations.controlloEsistenzaMateria(nuovaMateriaTextfield.getText())) {
			IoOperations.creaMateria(nuovaMateriaTextfield.getText());
			operazione_effettuata = true;
			dialogLayout.setBody(new Label("Materia creata con successo!"));
			dialog.show();
		}
		else {
			dialogLayout.setBody(new Label("La materia selezionata esiste gi�."));
			dialog.show();
		}
	}
}
