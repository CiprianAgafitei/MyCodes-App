package docenti_verifiche;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CreaNuovaVerifica implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_DOCENTE;
	
	@FXML private StackPane rootPane;							
	@FXML private JFXTextField textfieldMateriaArgomento;
	@FXML private ChoiceBox<String> choiceMateria;
    @FXML private ChoiceBox<Time> choiceTempo;
    @FXML private DatePicker choiceScadenza;
    @FXML private TableView<Domanda> tabellaDomande;
    @FXML private TableColumn<Domanda, String> colonnaDomanda;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton inserisciDomanda;
    @FXML private JFXButton eliminaDomanda;
    @FXML private JFXButton salvaVerifica;
    @FXML private JFXButton confermaInserisciDomanda;
    @FXML private JFXButton annullaInserimentoButton;
    
    private JFXTextField cerca_domanda;

    ObservableList<Domanda> domande = FXCollections.observableArrayList();
    ObservableList<Domanda> domande_generali = FXCollections.observableArrayList();
    
    TableView<Domanda> elenco_domande;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ID_DOCENTE = SchermataDocentiController.getID_DOCENTE();
		
		// Ottenimento materie del docente
		ArrayList<Materia_Docenti> materie_docenti = IoOperations.getElencoMaterieDocente(ID_DOCENTE);
		ObservableList<String> materie = FXCollections.observableArrayList();
		for(int i = 0; i < materie_docenti.size(); i++) {
			materie.add(materie_docenti.get(i).getMateria());
		}
		choiceMateria.setItems(materie);
		
		// Impostazione dei tempi possibili per la verifica
		ObservableList<Time> elenco_tempi = FXCollections.observableArrayList();
		elenco_tempi.add(Time.valueOf("00:10:00"));
		elenco_tempi.add(Time.valueOf("00:15:00"));
		elenco_tempi.add(Time.valueOf("00:20:00"));
		elenco_tempi.add(Time.valueOf("00:25:00"));
		elenco_tempi.add(Time.valueOf("00:30:00"));
		elenco_tempi.add(Time.valueOf("00:45:00"));
		elenco_tempi.add(Time.valueOf("00:60:00"));
		choiceTempo.setItems(elenco_tempi);
			
	  	colonnaDomanda.setCellValueFactory(new PropertyValueFactory<>("testo"));
	  	
		elenco_domande = new TableView<Domanda>();	
	  	//Inserimento domade nela tabella delle domande generali
	  	ArrayList<Domanda> questioni = new ArrayList<Domanda>();
	  	ArrayList<Materia_Docenti> subjects = IoOperations.getElencoMaterieDocente(ID_DOCENTE);
	  	
	  	for(int i = 0; i < subjects.size(); i++) {
	  		// Inserimento di tutte le domande di una data materia
	  		questioni.addAll(IoOperations.getDomandeFromMateria(subjects.get(i).getMateria()));
	  	}
	  	questioni = eliminaDomandeDuplicate(questioni);
	  				
	  	//Creazione elenco domande e preparazione per caricarli sulla tabella
	    int i = 0;
	  	while(i < questioni.size()) {
	  		domande_generali.add(new Domanda(questioni.get(i).getId_domanda(),
	  			questioni.get(i).getTesto(), questioni.get(i).getMateria(),
	  			questioni.get(i).getId_immagine(), questioni.get(i).getId_verifica()));
	  		i++;
	  	}
	  	TableColumn<Domanda, String> colonnaDomanda = new TableColumn<Domanda, String>("Testo domande");
	  	colonnaDomanda.setCellValueFactory(new PropertyValueFactory<>("testo"));
	  	colonnaDomanda.setPrefWidth(370);
	  	colonnaDomanda.setResizable(false);
	  	
	  	elenco_domande.getColumns().add(colonnaDomanda);
	  	elenco_domande.setPrefWidth(370);
	  	elenco_domande.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	  	elenco_domande.setItems(domande_generali);
	  	elenco_domande.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	/**METODO PER ELIMINARE LE DOMANDE DUPLICATE DALL'ELENCO
	 * @param domande
	 */
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
	public void pulsanteAnnullaCliccato() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("Annulla");
		JFXButton button2 = new JFXButton("Continua");
		button1.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			ritornaPaginaDocente();
		});
		dialogLayout.setHeading(new Label("Ritorno alla schermata principale"));
		dialogLayout.setBody(new Label("Attenzione! Continuando si perderanno le eventuali modifiche apportate."));
		dialogLayout.setActions(button1, button2);
		dialog.show();
	}

	/**METODO PER TORNARE ALLA SCHERMATA DEI DOCENTI
	 */
	public void ritornaPaginaDocente() {
		try {
			Parent ritornoPaginaVerifiche;
			ritornoPaginaVerifiche = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/Verifiche.fxml"));
			
			Scene scene = new Scene(ritornoPaginaVerifiche);
			scene.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("Verifiche docente");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(CreaNuovaVerifica.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	public void pulsanteInserisciDomandaCliccato() {		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("Annulla");
		button1.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		JFXButton button2 = new JFXButton("Continua");
		button2.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;");
		button2.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			ObservableList<Domanda>domande_nuove = elenco_domande.getSelectionModel().getSelectedItems();
			domande.addAll(domande_nuove);
			tabellaDomande.setItems(domande);
		});
		dialogLayout.setHeading(new Label("Scelta domande da inserire"));
		
		cerca_domanda = new JFXTextField();
		cerca_domanda.setPromptText("Cerca");
		cerca_domanda.setLabelFloat(true);
		
		VBox vbox = new VBox();
	  	vbox.getChildren().addAll(elenco_domande);
		
	  	HBox riga_pulsanti = new HBox();
	  	riga_pulsanti.setAlignment(Pos.CENTER);
	  	riga_pulsanti.getChildren().addAll(button1, new Label("      "), cerca_domanda, new Label("      "), button2, new Label("      "));
	  	
		dialogLayout.setBody(vbox);
		dialogLayout.setActions(riga_pulsanti);
		dialog.show();
		
		//Ricerca campi domande
	    FilteredList<Domanda> filteredData = new FilteredList<>(domande_generali, b -> true);
	    cerca_domanda.textProperty().addListener((observable, oldValue, newValue) -> {
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
		SortedList<Domanda> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(elenco_domande.comparatorProperty());
		elenco_domande.setItems(sortedData);
	}
	
	@FXML
	public void pulsanteEliminaDomndaCliccato() {
		tabellaDomande.getItems().remove(tabellaDomande.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void pulsanteSalvaVerificaCliccato() {
		LocalDate oggi = LocalDate.now();
				
		if(textfieldMateriaArgomento.getText().isEmpty()) 
		{
			mostraAvvisi("Campo materia/argomento verifica vuoto. Si prega si completarlo.");
		}
		else if(choiceTempo.getValue() == null) 
		{
			mostraAvvisi("Campo durata della verifica vuoto. Si prega di scegliere un tempo da attribuire alla verifica.");
		}
		else if(choiceScadenza.getValue() == null)
		{
			mostraAvvisi("Campo scadenza vuoto. Si prega di selezionare una data di scadenza per la verifica.");
		}
		else if(choiceScadenza.getValue().isBefore(oggi)) 
		{
			mostraAvvisi("Attenzione! La data di scadenza selezionata non può riferirsi ad un giorno passato.");
		}
		else if(domande.size() == 0)
		{
			mostraAvvisi("La verifica non presenta nessuna domanda. Si prega di selezionarne almeno una.");
		}
		else
		{
			String data = choiceScadenza.getValue().toString();
						
			//Creazione verifica
			IoOperations.creazioneVerifica(textfieldMateriaArgomento.getText(), ID_DOCENTE, choiceTempo.getValue(), data, domande, choiceMateria.getValue(), 0, 0);
		
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
				ritornaPaginaDocente();
			});
			dialogLayout.setHeading(new Label("Conferma creazione della verifica"));
			dialogLayout.setBody(new Label("Creazione completata con successo!"));
			dialogLayout.setActions(button);
			dialog.show();
		}
	}
	
	/**METODO PER MOSTRARE GLI AVVISI DI EVENTUALI PROBLEMI, ERRORI 
	 * O CONFERME DI CORRETTA ESECUZIONE DI UN'OPERAZIONE
	 * @param messaggio è il testo che verrà mostrato all'utente
	 */
	public void mostraAvvisi(String messaggio) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		dialogLayout.setHeading(new Label("Conferma creazione della verifica"));
		dialogLayout.setBody(new Label(messaggio));
		dialogLayout.setActions(button);
		dialog.show();
	}
}
