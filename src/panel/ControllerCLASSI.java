package panel;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * CLASS TO MANAGE THE 4 OPTIONS FROM LATERAL MENU THAT THE ADMIN CAN DO IN HIS SCREEN:
 * 
 * - b1: new class
 * - b2: add student
 * - b3: remove student
 * - b4: edit a class
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
