package admin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import classi_tabelle.Classe;
import classi_tabelle.IoOperations;
import classi_tabelle.Utente;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminController implements Initializable {
	    
    //----ADMIN scene----//  
    @FXML private StackPane rootPane;
    @FXML private Tab tabUtenti;
    @FXML private TabPane tabpane;
    @FXML private JFXTextField cercaNomeUtente;
    @FXML private JFXButton buttonCercaUtente;
    @FXML private JFXHamburger menuUtenti;
    @FXML private Tab tabClassi;
    @FXML private JFXTextField cercaClasse;
    @FXML private JFXButton buttonCercaClasse;
    @FXML private JFXHamburger menuClassi;
    @FXML private JFXDrawer tendinaClassi;
    @FXML private JFXDrawer tendinaUtenti;
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableView<Classe> tabellaClassi;
    
    @FXML private TableColumn<Utente, String> nomeC;
    @FXML private TableColumn<Utente, String> cognomec;
    @FXML private TableColumn<Utente, String> usernameC;
    @FXML private TableColumn<Utente, String> passwordC;
    @FXML private TableColumn<Utente, String> tipologiaC;
    @FXML private TableColumn<Utente, Integer> classeC;
    @FXML private TableColumn<Utente, String> emailC;
    
    @FXML private TableColumn<Classe, String> classeClassi;
    @FXML private TableColumn<Classe, String> annoScolasticoClassi;
    
    ObservableList<Utente> values = FXCollections.observableArrayList();
    ObservableList<Classe> valori = FXCollections.observableArrayList();
    
	/**Metodo per INIZIALIZZARE LA SCHERMATA DELL'AMMINISTRATORE e gestire i due menù
	 * @param url -> valore null perché non usati (Argomento del metodo)
	 * @param rb  -> valore null perché non usati (Argomento del metodo)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			VBox vboxClasse = FXMLLoader.load(getClass().getResource("tendinaMenuCLASSI.fxml"));
			tendinaClassi.setSidePane(vboxClasse);
			
			VBox vboxUtenti = FXMLLoader.load(getClass().getResource("tendinaMenuUTENTI.fxml"));
			tendinaUtenti.setSidePane(vboxUtenti);
		} catch (IOException ex) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		HamburgerSlideCloseTransition task0 = new HamburgerSlideCloseTransition(menuUtenti);
		task0.setRate(-1);
		menuUtenti.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
			task0.setRate(task0.getRate()*-1);
			task0.play();
			if(!tendinaUtenti.isOpened()) {
				tendinaUtenti.open();
			}else {
				tendinaUtenti.close();
			}
		});
		
		HamburgerSlideCloseTransition task1 = new HamburgerSlideCloseTransition(menuClassi);
		task1.setRate(-1);
		menuClassi.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			task1.setRate(task1.getRate()*-1);
			task1.play();
			if(!tendinaClassi.isOpened()) {
				tendinaClassi.open();
			}else {
				tendinaClassi.close();
			}
		});
		
		ArrayList<Utente> utenti = new ArrayList<>();
		utenti = IoOperations.getUtenti();
		
		//Creazione elenco utenti e preparazione per caricarli sulla tabella
		int i = 0;
		while(i < utenti.size()) {
			values.add(new Utente(utenti.get(i).getMatricola(), utenti.get(i).getNome(), utenti.get(i).getCognome(),
		 		utenti.get(i).getUsername(), utenti.get(i).getPassword(), utenti.get(i).getTipologia(),
		 		utenti.get(i).getClasse(), utenti.get(i).getEmail()));
		 	i++;
		}
				
	    nomeC.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    cognomec.setCellValueFactory(new PropertyValueFactory<>("cognome"));
	    usernameC.setCellValueFactory(new PropertyValueFactory<>("username"));
	    passwordC.setCellValueFactory(new PropertyValueFactory<>("password"));
	    tipologiaC.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
	    classeC.setCellValueFactory(new PropertyValueFactory<>("classe"));
	    emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
	    tabellaUtenti.setItems(values);
	    
	    ArrayList<Classe> classi = new ArrayList<>();
		classi = IoOperations.getElencoClassi();
		
		//Creazione elenco classi e preparazione per caricarle sulla tabella
		i = 0;
		while(i < classi.size()) {
		 	valori.add(new Classe(classi.get(i).getCodice_classe(), classi.get(i).getClasse(), classi.get(i).getAnno_scolastico()));
		 	i++;
		}
		
	    classeClassi.setCellValueFactory(new PropertyValueFactory<>("classe"));
	    annoScolasticoClassi.setCellValueFactory(new PropertyValueFactory<>("anno_scolastico"));
	    tabellaClassi.setItems(valori);
	    
	    
	    //Ricerca campi utente
	    FilteredList<Utente> filteredData = new FilteredList<>(values, b -> true);

		cercaNomeUtente.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(utente -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (utente.getNome().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (utente.getCognome().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (utente.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (utente.getClasse().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (utente.getTipologia().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}

				return false;
			});
		});
		
		SortedList<Utente> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());
		tabellaUtenti.setItems(sortedData);
		
		
		//Ricerca campi classi
		FilteredList<Classe> filteredData2 = new FilteredList<>(valori, b -> true);

		cercaClasse.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData2.setPredicate(classe -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (classe.getClasse().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (classe.getAnno_scolastico().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}

				return false;
			});
		});
		
		SortedList<Classe> sortedData2 = new SortedList<>(filteredData2);
		sortedData2.comparatorProperty().bind(tabellaClassi.comparatorProperty());
		tabellaClassi.setItems(sortedData2);
	}
	
	

}