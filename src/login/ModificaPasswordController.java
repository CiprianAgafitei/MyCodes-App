package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import classi_tabelle.IoOperations;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ModificaPasswordController implements Initializable {

	public static int ID_UTENTE;
	
	@FXML private StackPane rootPane;
    @FXML private AnchorPane anchorPane;
    @FXML private JFXPasswordField nuovaPassword;
    @FXML private JFXPasswordField confermaPassword;
    @FXML private JFXButton indietroButtonPrimoAccesso;
    @FXML private JFXButton continuaButtonPrimoAccesso;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	ID_UTENTE = ControlloCodiceController.getID_UTENTE();
	}
    
    @FXML
    void changeSceneToLogin() {
    	FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		
		fadeTransition.setOnFinished((ActionEvent event) -> {
			try {
				Parent mainScene;
				mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
				
				Scene login = new Scene(mainScene);
				Stage pAStage = (Stage) rootPane.getScene().getWindow();
				
				pAStage.setScene(login);
			} catch (IOException e) {
				Logger.getLogger(ModificaPasswordController.class.getName()).log(Level.SEVERE, null, e);
			}
		});
		fadeTransition.play();
    }

    @FXML
    void finishRegistration() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		
		if(nuovaPassword.getText() == null || confermaPassword.getText().length() == 0) 
		{	
			Label label = new Label("Attenzione! Non tutti i campi sono stati completati.");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
		}
		else if(nuovaPassword.getText().length() < 5 || confermaPassword.getText().length() > 15) 
		{
			Label label = new Label("Attenzione! La password deve essere di lunghezza compresa tra 5 e 15");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
		}
		else if(!nuovaPassword.getText().equals(confermaPassword.getText())) 
		{
			Label label = new Label("Attenzione! Le due password non coincidono.");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
		}
		else {
			Label label = new Label("Password modificata con successo");
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			label.setWrapText(true);
			hbox.getChildren().add(label);
			dialogLayout.setHeading(hbox);
			dialogLayout.setActions(button);
			dialog.show();
			
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				
				//Salvataggio modifica password
				IoOperations.terminaRegistrazioneUtente(IoOperations.getUsernameFromIDUser(ID_UTENTE), nuovaPassword.getText());
					
				//Accesso su schermata dell'utente della tipologia rispettiva				
				String tipologia_utente = IoOperations.getTipologiaUtente(IoOperations.getUsernameFromIDUser(ID_UTENTE));
								
				if(tipologia_utente.equalsIgnoreCase("ADMIN") || tipologia_utente.equalsIgnoreCase("AMMINISTRATORE") || tipologia_utente.equalsIgnoreCase("ADMINISTRATOR")) //Accesso da amministratore
				{	
					FadeTransition fadeTransition = new FadeTransition();
					fadeTransition.setDuration(Duration.millis(1000));
					fadeTransition.setNode(rootPane);
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					
					fadeTransition.setOnFinished((ActionEvent event) -> {
						try {
							Parent root = FXMLLoader.load(getClass().getResource("/admin/admin.fxml"));
							
							Scene admin = new Scene(root);
							
							admin.getStylesheets().add(getClass().getResource("/application/Style.css").toExternalForm());
							
							Stage pAStage = (Stage) rootPane.getScene().getWindow();
							pAStage.sizeToScene();
							pAStage.setScene(admin);
						} catch (IOException e) {
							Logger.getLogger(ModificaPasswordController.class.getName()).log(Level.SEVERE, null, e);
						}
					});
					fadeTransition.play();
				}
				else if(tipologia_utente.equalsIgnoreCase("DOCENTE") || tipologia_utente.equalsIgnoreCase("TEACHER")) 	//Accesso da docente
				{
					FadeTransition fadeTransition = new FadeTransition();
					fadeTransition.setDuration(Duration.millis(1000));
					fadeTransition.setNode(rootPane);
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					
					fadeTransition.setOnFinished((ActionEvent event) -> {
						try {						
							Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
							
							Scene docenti = new Scene(root);
													
							Stage pAStage = (Stage) rootPane.getScene().getWindow();
							pAStage.setScene(docenti);
						} catch (IOException e) {
							Logger.getLogger(ModificaPasswordController.class.getName()).log(Level.SEVERE, null, e);
						}
					});
					fadeTransition.play();
				}
				else if(tipologia_utente.equalsIgnoreCase("STUDENTE") || tipologia_utente.equalsIgnoreCase("STUDENT")) 	//Accesso da studente
				{
					FadeTransition fadeTransition = new FadeTransition();
					fadeTransition.setDuration(Duration.millis(1000));
					fadeTransition.setNode(rootPane);
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					
					fadeTransition.setOnFinished((ActionEvent event) -> {
						try {
							Parent root = FXMLLoader.load(getClass().getResource("/studenti/SchermataPrincipaleStudenti.fxml"));
							
							Scene admin = new Scene(root);
							Stage pAStage = (Stage) rootPane.getScene().getWindow();
							pAStage.setScene(admin);
						} catch (IOException e) {
							Logger.getLogger(ModificaPasswordController.class.getName()).log(Level.SEVERE, null, e);
						}
					});
					fadeTransition.play();
				}
			});
		}
    }
    
}
