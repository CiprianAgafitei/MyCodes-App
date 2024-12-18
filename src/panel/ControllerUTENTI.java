package panel;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * CLASS TO MANAGE THE 3 OPERATIONS ON THE USER TABLE BY THE ADMIN:
 * 
 * - b1: new user
 * - b2: remove user
 * - b3: edit user
 * 
 * @author Ciprian
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
