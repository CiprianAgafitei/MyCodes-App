package studenti;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class StudentiController implements Initializable {
	
	@FXML private TableView<?> tabellaVerificheEsegui;
    @FXML private JFXTextField textfieldCercaVerificaExe;
    @FXML private JFXButton cercaVerificaExeButton;
    @FXML private JFXButton eseguiButton;
    @FXML private JFXTextField textfieldCercaVerificaView;
    @FXML private TableView<?> tabellaVerificheVisualizza;
    @FXML private JFXButton cercaVerificaViewButton;
    @FXML private JFXButton viewButton;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}
    
}
