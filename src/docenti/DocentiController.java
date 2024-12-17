package docenti;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class DocentiController implements Initializable {
	
	@FXML private TableView<?> tabellaVerificheAssegnate;
    @FXML private JFXTextField textfieldCercaVerificaAssegnata;
    @FXML private JFXButton cercaVerificaAssegnataButton;
    @FXML private JFXButton visualizzaButton;
    @FXML private JFXHamburger menuVisualizzaVerifiche;
    @FXML private JFXDrawer tendinaVerificheAssegnate;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		HamburgerSlideCloseTransition task0 = new HamburgerSlideCloseTransition(menuVisualizzaVerifiche);
		task0.setRate(-1);
		menuVisualizzaVerifiche.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
			task0.setRate(task0.getRate()*-1);
			task0.play();
			if(!tendinaVerificheAssegnate.isOpened()) {
				tendinaVerificheAssegnate.open();
			}else {
				tendinaVerificheAssegnate.close();
			}
		});
	}
    
}
