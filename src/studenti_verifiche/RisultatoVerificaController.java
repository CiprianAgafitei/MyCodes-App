package studenti_verifiche;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import classi_tabelle.IoOperations;
import classi_tabelle.Verifica;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RisultatoVerificaController implements Initializable {

	// Ottenimento della verifica per la quale visuallizare i risultati
	private Verifica verifica_eseguita;
	
	@FXML private StackPane rootPane;
    @FXML private Label labelTitolo;
    @FXML private Label nrDomande;
    @FXML private Label voto;
    @FXML private Label ptOttenuto;
    @FXML private Label ptTotale;
    @FXML private JFXButton esciButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		verifica_eseguita = EsecuzioneVerificheController.getVerifica();
		
		// Vengono impostate le varie informazioni della verifica
		labelTitolo.setText(verifica_eseguita.getArgomento());
		nrDomande.setText(IoOperations.getDomandeVerifica(verifica_eseguita.getId_verifica()).size() + "");
		voto.setText(verifica_eseguita.getVoto() + "");
		ptOttenuto.setText(verifica_eseguita.getPunteggio_ottenuto() + "");
		ptTotale.setText(verifica_eseguita.getPunteggio_totale() + "");
	}
	
	@FXML
	public void esciButtonPremuto() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_verifiche/StudentiVerifiche.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setResizable(false);
			stage.setTitle("Verifiche docente");
		} catch (IOException e) {
			Logger.getLogger(RisultatoVerificaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
}
