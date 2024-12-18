package docenti_nuova_domanda;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.Domanda;
import classi_tabelle.IoOperations;
import classi_tabelle.Materia_Docenti;
import classi_tabelle.Risposta;
import docenti.SchermataDocentiController;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
 * Class from teacher section to manage all the questions
 */
public class DomandeDocentiController implements Initializable {
	
	public static int ID_UTENTE;
	
	@FXML private StackPane rootPane;
    @FXML private Label nomeCognomeLabel;
    @FXML private TableView<Domanda> tabellaDomande;
    @FXML private TableColumn<Domanda, String> testoDomanda;
    @FXML private JFXTextField textfieldCercaDomanda;
    @FXML private JFXButton buttonAddQuestion;
    @FXML private JFXButton backButton;
    @FXML private JFXButton caricaDomande;

    ObservableList<Domanda> domande = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		ID_UTENTE = SchermataDocentiController.getID_DOCENTE();
		
		// Insertion of the questions in the questions table
		ArrayList<Domanda> questioni = new ArrayList<Domanda>();
	  	ArrayList<Materia_Docenti> subjects = IoOperations.getElencoMaterieDocente(ID_UTENTE);
	  	
	  	for(int i = 0; i < subjects.size(); i++) {
	  		// Insertion of the all question of a specific subject
	  		questioni.addAll(IoOperations.getDomandeFromMateria(subjects.get(i).getMateria()));
	  	}
	  	questioni = eliminaDomandeDuplicate(questioni);
	  				
	  	// Creation of the list and preparation to load them in the table
	    int i = 0;
	  	while(i < questioni.size()) {
	  		domande.add(new Domanda(questioni.get(i).getId_domanda(),
	  			questioni.get(i).getTesto(), questioni.get(i).getMateria(), questioni.get(i).getId_immagine(),
	  			questioni.get(i).getId_verifica()));
	  		 i++;
	  	}	
	  	testoDomanda.setCellValueFactory(new PropertyValueFactory<>("testo"));
	  	tabellaDomande.setItems(domande);
	  	
	  	// Check double click on a row table
	  	tabellaDomande.setRowFactory( tv -> {
	        TableRow<Domanda> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                Domanda domanda = row.getItem();
	                
	                // View of the question and its answers
	                visualizzaDomanda(domanda);
	            }
	        });
	        return row ;
	    });
	  	
	    // Question fields search
	    FilteredList<Domanda> filteredData = new FilteredList<>(domande, b -> true);

	    textfieldCercaDomanda.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(domanda -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (domanda.getTesto().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		SortedList<Domanda> sortedData3 = new SortedList<>(filteredData);
		sortedData3.comparatorProperty().bind(tabellaDomande.comparatorProperty());
		tabellaDomande.setItems(sortedData3);
	}
	
	private void visualizzaDomanda(Domanda domanda) {
		VBox contenuto_scrollpane = new VBox();
		
		// Insertion of first two elements in common among the tests
		HBox intestazione = new HBox();
		intestazione.setAlignment(Pos.CENTER);
		intestazione.getChildren().add(new Label(domanda.getTesto()));
		
		contenuto_scrollpane.getChildren().addAll(intestazione);
		
		char[]opzioni = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		//  Getting all the questions from the test to see/edit
		ArrayList<Risposta> risposte_domanda = IoOperations.getRisposteEsecuzioneVerifica(domanda.getId_domanda());
		
		// Inserimento componenti il cui numero varia da verifica a verifica
		for(int i = 0; i < risposte_domanda.size(); i++) {
			HBox h = new HBox();
			Label l = new Label("    " + opzioni[i] + ") " + risposte_domanda.get(i).getTesto());
			l.setWrapText(true);
			h.getChildren().addAll(l);
			
			//Inserimento della riga con il testo della risposta
			contenuto_scrollpane.getChildren().add(h);
		}
		ScrollPane sp = new ScrollPane();
		sp.setStyle("-fx-background-color: #fff");
		sp.setContent(contenuto_scrollpane);
		sp.setPrefSize(570, 400);
		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		//---------------------------------------------------------------------
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("Ok");
		button1.setStyle("-fx-background-color:red; -fx-text-fill: white;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		Label title = new Label("Visualizzazione verifica");
		title.setAlignment(Pos.CENTER);
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		dialogLayout.setHeading(title);
		dialogLayout.setBody(sp);
		dialogLayout.setActions(button1, new Label("                                                 "));
		dialog.show();
	}
	
	/** Method to remove duplicated questions */
	private ArrayList<Domanda> eliminaDomandeDuplicate(ArrayList<Domanda>domande) {
		ArrayList<Domanda> nuove = new ArrayList<Domanda>();
		
		for(int i = 0; i < domande.size(); i++){
			boolean presente = false;
			
			for(int j = 0; j < nuove.size(); j++){
				if(domande.get(i).getTesto().equalsIgnoreCase(nuove.get(j).getTesto())) {
					presente = true;
				}
			}
			if(!presente)
				nuove.add(domande.get(i));
		}
		return nuove;
	}
	
	@FXML
	/** Methos to manage back button pressed */
	public void pulsanteIndietro() {
		try {
			Parent ritornoPaginaSchermataDocenti;
			ritornoPaginaSchermataDocenti = (StackPane) FXMLLoader.load(getClass().getResource("/docenti/schermata_docenti.fxml"));
			
			Scene scena = new Scene(ritornoPaginaSchermataDocenti);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(scena);
			stage.setResizable(false);
			stage.setTitle("Men� principale docenti");
		} catch (IOException e) {
			Logger.getLogger(DomandeDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	/** Method to manage new question button clicked -> Show the scene for adding a new question */
	public void nuovaDomandaClicked() {
		try {
			Parent schermataNuovaDomanda;
			schermataNuovaDomanda = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_nuova_domanda/nuovaDomandaDocenti.fxml"));
			
			Scene scena = new Scene(schermataNuovaDomanda);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setTitle("Creazione di una domanda");
			stage.setScene(scena);
			stage.centerOnScreen();
		} catch (IOException e) {
			Logger.getLogger(DomandeDocentiController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static int getID_UTENTE() {
		return ID_UTENTE;
	}

}
