package adminScene;

import javafx.stage.Stage;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SampleController implements Initializable {
	    
    //----ADMIN scene----//
    @FXML private StackPane rootAdmin;
    @FXML private JFXDrawer drawerUtentiAdmin;	//menu opzioni per tabella utenti
    @FXML private JFXDrawer drawerClassiAdmin;	//menu opzioni per tabella classi
    @FXML private TableView<?> tabellaUtenti;
    @FXML private JFXTextField cercaUtenteTextField;
    @FXML private JFXButton cercaUtentiButton;
    @FXML private JFXHamburger menuUtentiAdmin;
    @FXML private TableView<?> tabellaClassi;
    @FXML private JFXTextField cercaClasseTextField;
    @FXML private JFXButton cercaClasseButton;
    @FXML private JFXHamburger menuClassiAdmin;
    @FXML private Tab TabClassi;	//Schermata tabella classi
    @FXML private Tab TabUtenti;	//Schermata tabella utenti
	
	/**Change the scene from the login scene to the first access's scene
	 * @param event
	 * @throws IOException
	*/
	public void changeScreenToPrimoAccesso (ActionEvent event) throws IOException {
	    Parent primoAccesso = FXMLLoader.load(getClass().getResource("Primo_Accesso.fxml"));
	    Scene scenaPrimoAccesso = new Scene(primoAccesso);
	    	
	    //aggiungere controlli textfield username e password
	    	
	    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	    window.setScene(scenaPrimoAccesso);
	    window.show();
	}
	
	/**Metodo per INIZIALIZZARE LA SCHERMATA DELL'AMMINISTRATORE e gestire i due menù
	 * @param url -> valore null perché non usati (Argomento del metodo)
	 * @param rb  -> valore null perché non usati (Argomento del metodo)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("adminScene.fxml"));
			VBox box = loader.load();
			if(TabUtenti.isSelected())	//Se posizione sulla schermata della tabella utenti
				drawerUtentiAdmin.setSidePane(box);
			else 	//Se posizione sulla schermata della tabella classi
				drawerClassiAdmin.setSidePane(box);
		} catch (IOException ex) {
			Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
		}
				
		if(TabUtenti.isSelected()) {
			HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuUtentiAdmin);
			transition.setRate(-1);
			menuUtentiAdmin.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
				transition.setRate(transition.getRate() * -1);
				transition.play();

				if (drawerUtentiAdmin.isOpened()) {
					drawerUtentiAdmin.close();
				} else {
					drawerUtentiAdmin.open();
				}
			});
		}else {
			HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuClassiAdmin);
			transition.setRate(-1);
			menuClassiAdmin.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
				transition.setRate(transition.getRate() * -1);
				transition.play();
	
				if (drawerClassiAdmin.isOpened()) {
					drawerClassiAdmin.close();
				} else {
					drawerClassiAdmin.open();
				}
			});
		}
	}
	
	/**Metoto per l'attivazione del menu con le opzioni sulla tabella utenti
	 */
	public void attivaMenuSuTabUtenti () {
		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuUtentiAdmin);
		transition.setRate(-1);
		menuUtentiAdmin.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();

			if (drawerUtentiAdmin.isOpened()) {
				drawerUtentiAdmin.close();
			} else {
				drawerUtentiAdmin.open();
			}
		});
	}
	
	/**Metodo per l'attivazione del menu con le opzioni sulla tabella classi
	 */
	public void attivaMenuSuTabClassi () {
		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuClassiAdmin);
		transition.setRate(-1);
		menuClassiAdmin.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();

			if (drawerClassiAdmin.isOpened()) {
				drawerClassiAdmin.close();
			} else {
				drawerClassiAdmin.open();
			}
		});
	}
	    
}
