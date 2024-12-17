package panel;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**Classe per gestire le tre operazioni sulla tabella degli utenti, da parte dell'amministratore,
 * ovvero:
 * 
 * - b1: nuovo utente
 * - b2: rimuovi utente
 * - b3: modifica utente
 * 
 * @author cipri
 *
 */
public class ControllerUTENTI implements Initializable {
	
	@FXML private JFXButton b1;
	@FXML private JFXButton b2;
	@FXML private JFXButton b3;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void exit(ActionEvent event) {
		System.exit(0);
	}
}
