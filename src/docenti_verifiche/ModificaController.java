package docenti_verifiche;

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
import classi_tabelle.Verifica;
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

public class ModificaController implements Initializable {

	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_DOCENTE;
	
	@FXML private StackPane rootPane;
	@FXML private ChoiceBox<String> choiceVerifica;
    @FXML private TableView<Domanda> tabellaDomande;
    @FXML private TableColumn<Domanda, String> testoDomandaColumn;
    @FXML private JFXButton annullaButton;
    @FXML private JFXButton inserisciButton;
    @FXML private JFXButton rimuoviButton;
    @FXML private JFXButton salvaButton;
    
    private JFXTextField cerca_domanda;
    private TableView<Domanda> elenco_domande;
    private int id_verifica;
    private ArrayList<Verifica> verifiche;
    
    ObservableList<Domanda> domande = FXCollections.observableArrayList();
    ObservableList<Domanda> domande_generali = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ID_DOCENTE = SchermataDocentiController.getID_DOCENTE();
		elenco_domande = new TableView<Domanda>();
		
		// Ottenimento delle verifiche del docente in modo da poter poi inserire il riferimento nel choicebox
		verifiche = new ArrayList<Verifica>();
		verifiche = IoOperations.getVerifiche(ID_DOCENTE);
		
		// Elenco degli argomenti delle verifiche del docente
		ObservableList<String> elenco_verifiche = FXCollections.observableArrayList();
		
		// Inserimento degli argomenti delle verifiche nell'elenco
		for(int i = 0; i < verifiche.size(); i++) {	
			// Inserimento delle verifiche non ancora assegnate
			if(verifiche.get(i).getIs_assegnata().equals("NO"))
				elenco_verifiche.add(verifiche.get(i).getArgomento());
		}
		choiceVerifica.setItems(elenco_verifiche);
		
		testoDomandaColumn.setCellValueFactory(new PropertyValueFactory<>("testo"));
		
		choiceVerifica.setOnAction(e -> {
			// Ottenimento dell'id della verifica selezionata
			if(!choiceVerifica.getValue().isEmpty()) {
				this.domande = FXCollections.observableArrayList();
				id_verifica = getIdVerificaDaTesto(verifiche, choiceVerifica.getValue());
			
				// Ottenimento delle domande della verifica selezionata
				ArrayList<Domanda> domande = IoOperations.getDomandeVerifica(id_verifica);
				
				// Inserimento nella tabella delle domande della verifica selezionata
				for(int i = 0; i < domande.size(); i++) {
					this.domande.add(domande.get(i));
				}
				tabellaDomande.setItems(this.domande);
			}
		});
		
		// Ottenimento domande possibili
		ArrayList<Domanda> questioni = new ArrayList<>();
	  	questioni = IoOperations.getDomande();
	  	questioni = eliminaDomandeDuplicate(questioni);
	  	
	  	//Creazione elenco domande e preparazione per caricarli sulla tabella
	    for(int i = 0; i < questioni.size(); i++) {
	  		domande_generali.add(new Domanda(questioni.get(i).getId_domanda(),
	  			questioni.get(i).getTesto(), questioni.get(i).getMateria(), questioni.get(i).getId_immagine(),
	  			questioni.get(i).getId_verifica()));
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
		
		for(int i = 0; i < domande.size() - 1; i++)
		{
			boolean presente = false;
			
			for(int j = 0; j < nuove.size(); j++)
			{
				if(domande.get(i).getTesto().equalsIgnoreCase(nuove.get(j).getTesto())) {
					presente = true;
				}
			}
			if(!presente)
				nuove.add(domande.get(i));
		}
		return nuove;
	}
	
	/** METODO CHE DATO L'ELENCO DELLE VERIFICHE E UN TESTO CONTENENTE UN ARGOMENTO DELLE VERIFICHE,
	 * RESTITUISCE L'ID DELLA VERIFICA CONTENENTE L'ARGOMENTO SPECIFICATO
	 * @param v è l'elenco delle verifiche
	 * @param testo è il testo da cercare
	 * @return l'id della verifica che ha come argomento il testo
	 */
	private int getIdVerificaDaTesto(ArrayList<Verifica>v, String testo) {
		for(int i = 0; i < v.size(); i++) {
			if(v.get(i).getArgomento().equals(testo))
				return v.get(i).getId_verifica();
		}
		return 0;
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
			Parent ritornoPaginaPrecedente;
			ritornoPaginaPrecedente = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_verifiche/Verifiche.fxml"));
			
			Scene scena = new Scene(ritornoPaginaPrecedente);
			scena.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(scena);
			stage.setTitle("Verifiche docente");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(ModificaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void pulsanteInserisciDomandaCliccato() {
		// Controllo che sia stata scelta una verifica dove inserire nuove domande
		if(choiceVerifica.getValue() != null)
		{
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
		  	vbox.getChildren().add(elenco_domande);
			
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
		else
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Inserimento di nuove domande"));
			
			Label l = new Label("Attenzione! Per aggiungere nuove domande bisogna selezionare una verifica.");
			l.setWrapText(true);
			l.setFont(Font.font("Verdana", 15));
			dialogLayout.setBody(l);
			
			dialogLayout.setActions(button);
			dialog.show();
		}
	}
	
	@FXML
	public void pulsanteEliminaDomndaCliccato() {
		// Rimozione dall'elenco della tabella
		tabellaDomande.getItems().remove(tabellaDomande.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void pulsanteSalvaVerificaCliccato() {
		/* Salvataggio modifica verifica: verifica attuale sostituita con quella modificata
		 * Vengono rimosse dal database le domande e risposte della verifica prima delle modifiche
		 * e inserita la nuova */
		
		// Salvataggio verifica prima di rimuoverla per poter prendere le informazioni utili per la modifica
		Verifica v = IoOperations.getVerificaFromId(id_verifica);	
		
		// Ottenimento domande della tabella
		ObservableList<Domanda> domande = tabellaDomande.getItems();
		ArrayList<Domanda> questions = new ArrayList<Domanda>();
		questions.addAll(domande);
		
		// Salvataggio della verifica con le modifiche apportate
		IoOperations.creazioneVerifica(v.getArgomento(), ID_DOCENTE, v.getTempo(), v.getScadenza(), domande, IoOperations.getMateriaDocente(v.getId_materia_docente()).getMateria(), 0, 0);
		
		// Rimozione dal database della verifica con tutte le domande e le relative risposte
		IoOperations.rimuoviVerifica(v);
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			ritornaPaginaDocente();
		});
		dialogLayout.setHeading(new Label("Salvataggio modifiche"));
		dialogLayout.setBody(new Label("Modifiche salvate con successo!"));
		dialogLayout.setActions(button);
		dialog.show();
	}

}
