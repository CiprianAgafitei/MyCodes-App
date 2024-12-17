package panel;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**Classe per gestire le quattro opzioni del menu laterale che l'amministratore può eseguire
 * nella propria schermata, ovvero:
 * 
 * - b1: nuova classe
 * - b2: inserisci studente
 * - b3: rimuovi studente
 * - b4: modifica classe
 * 
 * @author Ciprian
 *
 */
public class ControllerCLASSI implements Initializable {

	@FXML private JFXButton b1;
	@FXML private JFXButton b2;
	@FXML private JFXButton b3;
	@FXML private JFXButton b4;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void exit(ActionEvent event) {
		System.exit(0);
	}
}
