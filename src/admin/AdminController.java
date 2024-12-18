package admin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.Classe;
import classi_tabelle.IoOperations;
import classi_tabelle.Materia_Docenti;
import classi_tabelle.Utente;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/** 
 * Admin Main Class to manage all the admin-section
*/
public class AdminController implements Initializable {
	    
    //----ADMIN scene----//  
	@FXML private StackPane rootPane;
	@FXML private JFXTabPane tabpane;
	
    @FXML private Tab tabUtenti;
    @FXML private TableView<UtenteClasse> tabellaUtenti;
    @FXML private TableColumn<UtenteClasse, String> nomeC;
    @FXML private TableColumn<UtenteClasse, String> cognomec;
    @FXML private TableColumn<UtenteClasse, String> usernameC;
    @FXML private TableColumn<UtenteClasse, String> passwordC;
    @FXML private TableColumn<UtenteClasse, String> tipologiaC;
    @FXML private TableColumn<UtenteClasse, String> classeC;
    @FXML private TableColumn<UtenteClasse, String> emailC;
    @FXML private JFXTextField cercaNomeUtente;
    @FXML private JFXButton moreOptions1;
    @FXML private JFXButton logOut;
    @FXML private JFXButton loadFile;
    
    @FXML private Tab tabClassi;
    @FXML private TableView<Classe> tabellaClassi;
    @FXML private TableColumn<Classe, String> classeClassi;
    @FXML private TableColumn<Classe, String> annoScolasticoClassi;
    @FXML private TableColumn<Classe, Integer> nrStudentiClasse;
    @FXML private JFXTextField cercaClasse;
    @FXML private JFXButton moreOptions2;
    
    @FXML private Tab tabMaterieDocenti;
    @FXML private TableView<UtenteMateria> tabellaMaterieDocenti;
    @FXML private TableColumn<UtenteMateria, String> materiaDocenti;
    @FXML private TableColumn<UtenteMateria, String> docenteID;
    @FXML private JFXTextField cercaMateria;
    @FXML private JFXButton moreOptions3;
    
    ObservableList<UtenteClasse> values = FXCollections.observableArrayList();
    ObservableList<Classe> valori = FXCollections.observableArrayList();
    ObservableList<UtenteMateria> materieDocenti = FXCollections.observableArrayList();
    
    MenuItem item;
            
	/**Method to INITIALIZE THE ADMIN SCREEN and manage the two menu:
	 * @param url -> null value because not used (method argument)
	 * @param rb  -> null value because not used (method argument)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ArrayList<Utente> utenti = new ArrayList<>();
		utenti = IoOperations.getUtenti();
		
		// Creation of the list of users and preparation to load them in the table
		int i = 0;
		while(i < utenti.size()) {
			values.add(new UtenteClasse(utenti.get(i).getNome(), utenti.get(i).getCognome(),
		 		utenti.get(i).getUsername(), utenti.get(i).getPassword(), utenti.get(i).getTipologia(),
		 		IoOperations.getClasseFromIDClasse(utenti.get(i).getClasse()), utenti.get(i).getEmail()));
		 	i++;
		}

	    nomeC.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    nomeC.setSortable(false);
	    cognomec.setCellValueFactory(new PropertyValueFactory<>("cognome"));
	    cognomec.setSortable(false);
	    usernameC.setCellValueFactory(new PropertyValueFactory<>("username"));
	    usernameC.setSortable(false);
	    passwordC.setCellValueFactory(new PropertyValueFactory<>("password"));
	    passwordC.setSortable(false);
	    tipologiaC.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
	    tipologiaC.setSortable(false);
	    classeC.setCellValueFactory(new PropertyValueFactory<>("classe"));
	    classeC.setSortable(false);
	    emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
	    emailC.setSortable(false);
	    tabellaUtenti.setItems(values);
	    
	    ArrayList<Classe> classi = new ArrayList<>();
		classi = IoOperations.getElencoClassi();
		
		// Creation of the list of classes and preparation to load them in the table
		i = 0;
		while(i < classi.size()) {
			valori.add(new Classe(classi.get(i).getId_classe(), classi.get(i).getClasse(), classi.get(i).getAnno_scolastico(), classi.get(i).getNumero_studenti()));
		 	i++;
		}
		
	    classeClassi.setCellValueFactory(new PropertyValueFactory<>("classe"));
	    classeClassi.setSortable(false);
	    annoScolasticoClassi.setCellValueFactory(new PropertyValueFactory<>("anno_scolastico"));
	    annoScolasticoClassi.setSortable(false);
	    nrStudentiClasse.setCellValueFactory(new PropertyValueFactory<>("numero_studenti"));
	    nrStudentiClasse.setSortable(false);
	    tabellaClassi.setItems(valori);
	    
	    // List of the subjects and the teachers of each one
		ArrayList<Materia_Docenti> materie_docenti = new ArrayList<Materia_Docenti>();
		materie_docenti = IoOperations.getElencoMaterieDocenti();

		// Creation of the list of users and preparation to load them in the table
		i = 0;
		while (i < materie_docenti.size()) {
			materieDocenti.add(new UtenteMateria(materie_docenti.get(i).getMateria(), IoOperations.getUsernameFromIDUser(materie_docenti.get(i).getId_docente())));
			i++;
		}

		materiaDocenti.setCellValueFactory(new PropertyValueFactory<>("materia"));
		materiaDocenti.setSortable(false);
		docenteID.setCellValueFactory(new PropertyValueFactory<>("utente"));
		docenteID.setSortable(false);
		tabellaMaterieDocenti.setItems(materieDocenti);
	    
	    // User fields search
	    FilteredList<UtenteClasse> filteredData = new FilteredList<UtenteClasse>(values, b -> true);

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
				} else if (utente.getTipologia().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		SortedList<UtenteClasse> sortedData = new SortedList<UtenteClasse>(filteredData);
		sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());
		tabellaUtenti.setItems(sortedData);
		
		// Classes fields search
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
		
		// Teacher feilds search
	    FilteredList<UtenteMateria> filteredData3 = new FilteredList<UtenteMateria>(materieDocenti, b -> true);

	    cercaMateria.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData3.setPredicate(materia_docente -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (materia_docente.getMateria().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				else if(materia_docente.getUtente().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				
				return false;
			});
		});
		SortedList<UtenteMateria> sortedData3 = new SortedList<UtenteMateria>(filteredData3);
		sortedData3.comparatorProperty().bind(tabellaMaterieDocenti.comparatorProperty());
		tabellaMaterieDocenti.setItems(sortedData3);
		
		tabellaUtenti.setRowFactory(tva -> {
        	final TableRow<UtenteClasse> row = new TableRow<>();
	        final ContextMenu rowMenu = new ContextMenu();
	        ContextMenu tableMenu = tabellaUtenti.getContextMenu();
	        if (tableMenu != null) {
	        	rowMenu.getItems().addAll(tableMenu.getItems());
	        	rowMenu.getItems().add(new SeparatorMenuItem());
	        }
	        item = new MenuItem("Rimuovi");
	        
	        rowMenu.getItems().addAll(item);
	        row.contextMenuProperty().bind(
	        		Bindings.when(Bindings.isNotNull(row.itemProperty()))
	        		.then(rowMenu)
	        		.otherwise((ContextMenu) null));
	            
	        item.disableProperty().bind(Bindings.isEmpty(tabellaUtenti.getSelectionModel().getSelectedItems()));
	        item.setOnAction(e -> {
	        	rimuoviUtente();
	        });
	        return row;
        });
	}
	
	@FXML
	/** Method to load the options about the users management */
	public void pulsanteOpzioniUtenti() {
		VBox creazioneVerificaScene = null;
		try {
			creazioneVerificaScene = (VBox) FXMLLoader.load(getClass().getResource("/admin/MenuOpzioniUtenti.fxml"));
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialogLayout.setBody(creazioneVerificaScene);
		dialog.show();
	}
	
	@FXML
	/** Method to load file with users */
	public void pulsanteCaricaFileConUtenti() {
		try {
			Parent mainScene;
			mainScene = (StackPane) FXMLLoader.load(getClass().getResource("/admin/InsertElencoUtenti.fxml"));
			
			Scene login = new Scene(mainScene);
			Stage pAStage = (Stage) rootPane.getScene().getWindow();
			
			pAStage.setScene(login);
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to load the options for classes management */
	public void pulsanteOpzioniClassi() {
		VBox creazioneVerificaScene = null;
		try {
			creazioneVerificaScene = (VBox) FXMLLoader.load(getClass().getResource("/admin/MenuOpzioniClassi.fxml"));
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialogLayout.setBody(creazioneVerificaScene);
		dialog.show();
	}
	
	@FXML
	/** Method to load options for Subjects and Teachers management */
	public void pulsanteOpzioniMaterieDocenti() {
		VBox creazioneVerificaScene = null;
		try {
			creazioneVerificaScene = (VBox) FXMLLoader.load(getClass().getResource("/admin/MenuOpzioniMaterieDocenti.fxml"));
		} catch (IOException e) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
		}

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialogLayout.setBody(creazioneVerificaScene);
		dialog.show();
	}

	/** Method to remove a selected user from the table - Check not to be an ADMIN */
	private void rimuoviUtente() {
		if(tabellaUtenti.getSelectionModel().getSelectedItem().getTipologia().equalsIgnoreCase("ADMIN") ||
				tabellaUtenti.getSelectionModel().getSelectedItem().getTipologia().equalsIgnoreCase("ADMINISTRATOR")) 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button1 = new JFXButton("Ok");
			button1.setStyle("-fx-background-color:red; -fx-text-fill: white;");
			button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione dell'utente"));
	
			Label l = new Label("ATTENZIONE! Non � possibile rimuovere account admin.");
			l.setWrapText(true);
	
			dialogLayout.setBody(l);
			dialogLayout.setActions(button1, new Label("                               "));
			dialog.show();
		}
		else 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button1 = new JFXButton("Annulla");
			JFXButton button2 = new JFXButton("Conferma");
			button1.setStyle("-fx-background-color:red; -fx-text-fill: white;");
			button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			button2.setStyle("-fx-background-color:#1E90FF; -fx-text-fill: white;");
			button2.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				// User remove
				IoOperations.removeUser(tabellaUtenti.getSelectionModel().getSelectedItem().getUsername());
				tabellaUtenti.getItems().removeAll();
				values.remove(tabellaUtenti.getSelectionModel().getSelectedItem());
				tabellaUtenti.setItems(values);
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione dell'utente"));
	
			Label l = new Label("Si desidera confermare la rimozione dell'utente?");
			l.setWrapText(true);
	
			dialogLayout.setBody(l);
			dialogLayout.setActions(button1, new Label("                               "), button2, new Label("             "));
			dialog.show();
		}
	}
	
	@FXML
	/** Method for Logout management from admin section */
	public void logOut() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("No");
		JFXButton button2 = new JFXButton("Si");
		button1.setStyle("-fx-background-color:RED; -fx-border-color:red; -fx-text-fill:#fff;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		button2.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill:#fff;"); 
		button2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			try {
				Parent ritornoPaginaAdmin;
				ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
				
				Scene admin = new Scene(ritornoPaginaAdmin);
				
				Stage stage = (Stage) rootPane.getScene().getWindow();
				
				stage.setScene(admin);
				stage.setResizable(false);
				stage.setTitle("MyTest App");
			} catch (IOException e) {
				Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
			}
			
		});
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		Label l = new Label("Sei sicuro di voler uscire dal tuo account?");
		l.setFont(Font.font("Verdana", 18));
		h.getChildren().add(l);
		dialogLayout.setHeading(h);
		dialogLayout.setActions(button1, new Label("       "), button2, new Label("                                  "));
		dialog.show();
	}

}