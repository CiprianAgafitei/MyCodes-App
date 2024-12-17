package admin_nuova_classe;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import classi_tabelle.IoOperations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import login.LoginController;

public class NuovaClasseController implements Initializable {

	@FXML private StackPane rootPane;
	@FXML private BorderPane borderPane;
	@FXML private ChoiceBox<Integer> choiceboxAnno;
	@FXML private ChoiceBox<String> choiceboxClasse;
	@FXML private JFXButton annullaButton;
	@FXML private JFXButton confermaButton;
	@FXML private JFXTextField textfieldAltro;
 
	private String anno_scolastico;

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		int primo_anno;
		int secondo_anno;	
		if(Calendar.getInstance().get(Calendar.MONTH) <= 12 && Calendar.getInstance().get(Calendar.MONTH) >= 9) {
			primo_anno = Calendar.getInstance().get(Calendar.YEAR);
			secondo_anno = primo_anno + 1;
		}else {
			secondo_anno = Calendar.getInstance().get(Calendar.YEAR);
			primo_anno = secondo_anno - 1;
		}
		this.anno_scolastico = primo_anno + "/" + secondo_anno;
		
		choiceboxAnno.getItems().addAll(1, 2, 3, 4, 5);
		choiceboxClasse.getItems().addAll("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");
	}
	
	@FXML
	public void closeThisWindow() {
		mostraSchermataAdmin();
	}
	
	private void mostraSchermataAdmin() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void creClasse() {
		BoxBlur blur = new BoxBlur(3, 3, 3);

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		//Campi vuoti
		if (choiceboxAnno.getValue() == null || choiceboxClasse.getValue() == null) {
			dialogLayout.setHeading(new Label("Attenzione! Non tutti i campi sono stati completati."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		else if(IoOperations.controlloEsistenzaClasseDatoAnnoESezione(choiceboxAnno.getValue() + choiceboxClasse.getValue()).equals("-")){
			dialogLayout.setHeading(new Label("Attenzione! La classe scelta esiste già."));
			dialogLayout.setActions(button);
			dialog.show();
			dialog.setOnDialogClosed((JFXDialogEvent event) -> {
				borderPane.setEffect(null);
			});
			borderPane.setEffect(blur);
		}
		else {
			String classe = choiceboxAnno.getValue() + choiceboxClasse.getValue() + " " + textfieldAltro.getText();
			IoOperations.creaClasse(classe, anno_scolastico);
			mostraSchermataAdmin();
		}
	}
	
}
