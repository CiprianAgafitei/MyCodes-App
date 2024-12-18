package docenti_voti;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import classi_tabelle.Materia_Docenti;
import classi_tabelle.Verifica;
import docenti.SchermataDocentiController;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RisultatiVerificheController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_DOCENTE;
	// Verifica selezionata dal docente per la visualizzazione
	private static Verifica verifica;
	private static int ID_VERIFICA;
		
	@FXML private StackPane rootPane;
    @FXML private BorderPane borderPane;
    @FXML private JFXButton backButton;
    @FXML private ChoiceBox<String> choiceAnno;
    @FXML private TableView<VerificaStudente> tabellaVerificheAssegnate;
    @FXML private TableColumn<VerificaStudente, String> materiaColumn;
    @SuppressWarnings("rawtypes")
	@FXML private TableColumn classeColumn;
    @FXML private TableColumn<VerificaStudente, String> annoSezione;
    @SuppressWarnings("rawtypes")
	@FXML private TableColumn utente;
    @FXML private TableColumn<VerificaStudente, String> nomeUtente;
    @FXML private TableColumn<VerificaStudente, String> cognomeUtente;
    @FXML private TableColumn<VerificaStudente, String> scadenzaColumn;
    @FXML private TableColumn<VerificaStudente, String> eseguitaColumn;
    @FXML private JFXButton generaleButton;
    @FXML private JFXTextField textfieldCercaVerificaAssegnata;
    @FXML private JFXButton visualizzaButton;
    
    ObservableList<VerificaStudente> valori = FXCollections.observableArrayList();
    MenuItem item;
    DatePicker nuova_scadenza;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_DOCENTE = SchermataDocentiController.getID_DOCENTE();
				
		inserisciVerifiche();
		materiaColumn.setCellValueFactory(new PropertyValueFactory<>("argomento"));
		annoSezione.setCellValueFactory(new PropertyValueFactory<>("classe"));
		nomeUtente.setCellValueFactory(new PropertyValueFactory<>("nome"));
		cognomeUtente.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		scadenzaColumn.setCellValueFactory(new PropertyValueFactory<>("scadenza"));
		eseguitaColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
		tabellaVerificheAssegnate.setItems(valori);
		
		//Ricerca campi verifica
		FilteredList<VerificaStudente> filteredData2 = new FilteredList<VerificaStudente>(valori, b -> true);

	    textfieldCercaVerificaAssegnata.textProperty().addListener((observable, oldValue, newValue) -> {
	  		filteredData2.setPredicate(verifica -> {

	  			if (newValue == null || newValue.isEmpty()) {
	  				return true;
	  			}
	  			String lowerCaseFilter = newValue.toLowerCase();

	  			if (verifica.getArgomento().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	  				return true;
	  			}
	  			else if(verifica.getScadenza().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	  				return true;
	  			}
	  			else if(verifica.getClasse().indexOf(lowerCaseFilter) != -1) {
	  				return true;
	  			}
	  			else if(verifica.getNome().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	  				return true;
	  			}
	  			else if(verifica.getCognome().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	  				return true;
	  			}
	  			return false;
	  		});
	  	});
	  	SortedList<VerificaStudente> sortedData2 = new SortedList<VerificaStudente>(filteredData2);
	  	sortedData2.comparatorProperty().bind(tabellaVerificheAssegnate.comparatorProperty());
	  	tabellaVerificheAssegnate.setItems(sortedData2);
	  	
	  	// Gestione doppio click sulle righe della tabella
	  	tabellaVerificheAssegnate.setRowFactory( tv -> {
	        TableRow<VerificaStudente> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (event.getClickCount() == 2 && (!row.isEmpty()) )
	            {	                
	                int id_verifica = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getId_verifica();
			  		// Controllo che lo studente abbia eseguito la verifica
			  		if(tabellaVerificheAssegnate.getSelectionModel().getSelectedIndex() != -1 && IoOperations.getVerificaFromId(id_verifica).getIs_eseguita().equals("NO")) 
			  		{
			  			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			  			JFXButton button1 = new JFXButton("Ok");
			  			button1.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			  			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			  			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			  				dialog.close();
			  			});
			  			dialogLayout.setHeading(new Label("Visualizzazione della verifica"));
			  			dialogLayout.setBody(new Label("Lo studente non ha ancora eseguito la verifica."));
			  			dialogLayout.setActions(button1);
			  			dialog.show();
			  			visualizzaButton.setVisible(false);
			  			generaleButton.setVisible(false);
			  		}
			  		else if(tabellaVerificheAssegnate.getSelectionModel().getSelectedIndex() != -1 && IoOperations.getVerificaFromId(id_verifica).getIs_eseguita().equals("SI")) {
				  		visualizzaButton.setVisible(true);
				  		generaleButton.setVisible(true);
			  		}
			  		else {
				  		visualizzaButton.setVisible(false);
				  		generaleButton.setVisible(false);
			  		}
	            }
	        });
	        
	        final ContextMenu rowMenu = new ContextMenu();
	        ContextMenu tableMenu = tabellaVerificheAssegnate.getContextMenu();
	        if (tableMenu != null) {
	        	rowMenu.getItems().addAll(tableMenu.getItems());
	        	rowMenu.getItems().add(new SeparatorMenuItem());
	        }
	        item = new MenuItem("Riassegna");
	        
	        rowMenu.getItems().addAll(item);
	        row.contextMenuProperty().bind(
	        		Bindings.when(Bindings.isNotNull(row.itemProperty()))
	        		.then(rowMenu)
	        		.otherwise((ContextMenu) null));
	            
	        item.disableProperty().bind(Bindings.isEmpty(tabellaVerificheAssegnate.getSelectionModel().getSelectedItems()));
	        item.setOnAction(e -> {
	        	// COntrollo se lo studente ha già eseguito la verifica -> se no, può essere riassegnata
	        	if(tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getEseguita().equals("SI")) 
	        	{
	        		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		  			JFXButton button1 = new JFXButton("Ok");
		  			button1.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		  			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		  			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
		  				dialog.close();
		  			});
		  			dialogLayout.setHeading(new Label("Assegnazione verifica su singolo studente"));
		  			dialogLayout.setBody(new Label("Attenzione! Lo studente ha già eseguito la verifica. Non è possibile riassegnarla."));
		  			dialogLayout.setActions(button1);
		  			dialog.show();
	        	}
	        	else 
	        	{
	        		VBox vbox = new VBox();
	        		vbox.setAlignment(Pos.CENTER);
	        		nuova_scadenza = new DatePicker();
	        		Label l = new Label("Selezionare la nuova data di scadenza.");
	        		vbox.getChildren().addAll(l, nuova_scadenza);
	        			
	        		JFXDialogLayout dialogLayout = new JFXDialogLayout();
	        		JFXButton button0 = new JFXButton("Annulla");
		  			JFXButton button1 = new JFXButton("Conferma");
		  			button0.setStyle("-fx-background-color:red; -fx-border-color:#1976D2; -fx-text-fill: white;");
		  			button0.setFocusTraversable(false);
		  			button1.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
		  			button1.setFocusTraversable(false);
		  			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		  			button0.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
		  				dialog.close();
		  			});
		  			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
		  				if(nuova_scadenza.getValue() == null) {
		  					dialog.close();
		  					
		  					JFXDialogLayout dialogLayout2 = new JFXDialogLayout();
				  			JFXButton button2 = new JFXButton("Ok");
				  			button2.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
				  			JFXDialog dialog2 = new JFXDialog(rootPane, dialogLayout2, JFXDialog.DialogTransition.TOP);
				  			button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent2) -> {
				  				dialog2.close();
				  			});
				  			dialogLayout2.setHeading(new Label("Seleziona data"));
				  			dialogLayout2.setBody(new Label("Attenzione! Campo data di nuova scadenza vuoto."));
				  			dialogLayout2.setActions(button2);
				  			dialog2.show();
		  				}
		  				else if(nuova_scadenza.getValue().isBefore(LocalDate.now())) {
		  					dialog.close();
		  					
		  					JFXDialogLayout dialogLayout2 = new JFXDialogLayout();
				  			JFXButton button2 = new JFXButton("Ok");
				  			button2.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
				  			JFXDialog dialog2 = new JFXDialog(rootPane, dialogLayout2, JFXDialog.DialogTransition.TOP);
				  			button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent2) -> {
				  				dialog2.close();
				  			});
				  			dialogLayout2.setHeading(new Label("Seleziona data"));
				  			dialogLayout2.setBody(new Label("Attenzione! La nuova data non può riferirsi ad un giorno passato."));
				  			dialogLayout2.setActions(button2);
				  			dialog2.show();
		  				}
		  				else 
		  				{
			  				// Riassegnazione verifica: viene aggiornato il flag del database che indica se lo studente ha eseguito o meno la verifica
			        		int id_verifica = getIdVerificaSelezionata(tabellaVerificheAssegnate.getSelectionModel().getSelectedItem());
			        		Verifica v = IoOperations.getVerificaFromId(id_verifica);
			        		// Metodo per la riassegnazione
			        		IoOperations.riassegnaVerifica(v, nuova_scadenza.getValue().toString());
			        		dialog.close();
			        		
			        		// Aggiornamento valori verifiche-studente e inserimento nella tabella
			        		tabellaVerificheAssegnate.getItems().removeAll();
			        		valori = FXCollections.observableArrayList();
			        		inserisciVerifiche();
			        		tabellaVerificheAssegnate.setItems(valori);
		  				}
		  			});
		  			dialogLayout.setHeading(new Label("Assegnazione verifica su singolo studente"));
		  			dialogLayout.setBody(vbox);
		  			dialogLayout.setActions(button0, new Label("                             "), button1, new Label("                "));
					dialog.show();
				}
			});
			return row;
		});

		// Ottenimento di tutte le verifiche del docente
		ArrayList<Verifica> verifiche_docente = IoOperations.getVerifiche(ID_DOCENTE);
		ArrayList<String> anni_verifica = new ArrayList<String>();

		// Inserimento date delle verifiche per riconoscere gli anni
		for (int i = 0; i < verifiche_docente.size(); i++) {
			int anno_selezionato = LocalDate.parse(verifiche_docente.get(i).getScadenza()).getYear();
			String confronto = "";
			if (LocalDate.parse(verifiche_docente.get(i).getScadenza()).getMonthValue() <= 8)
				confronto = (anno_selezionato - 1) + "/" + anno_selezionato;
			else
				confronto = anno_selezionato + "/" + (anno_selezionato - 1);

			// controllo se la data è stat già inserita
			if (!controlloAnnoPresente(anni_verifica, confronto)) {
				anni_verifica.add(confronto);
			}
		}

		// Impostazione anni
		String anno_attuale;
		if (LocalDate.now().getMonthValue() > 8 && LocalDate.now().getMonthValue() < 12) {
			anno_attuale = LocalDate.now().getYear() + "/" + (LocalDate.now().getYear() + 1);
		} else {
			anno_attuale = (LocalDate.now().getYear() - 1) + "/" + LocalDate.now().getYear();
		}
		choiceAnno.setValue(anno_attuale);

		ObservableList<String> date = FXCollections.observableArrayList();

		// Controllo se il docente non ha mai avuto verifiche
		if (anni_verifica.size() == 0) {
			/*
			 * Controllo data in cui si sta accedendo: se compreso tra settembre e novembre,
			 * viene creata una data a partire dall'anno attuale con il successivo
			 */
			if (LocalDate.now().getMonthValue() > 8 && LocalDate.now().getMonthValue() < 12) {
				date.add(LocalDate.now().getYear() + "/" + (LocalDate.now().getYear() + 1));
			} else {
				date.add((LocalDate.now().getYear() - 1) + "/" + LocalDate.now().getYear());
			}
		} else {
			date.addAll(anni_verifica);
		}
		choiceAnno.setItems(date);
	}

	/**
	 * METODO PER INSERIRE LE VERIFICHE NELL'ELENCO
	 */
	private void inserisciVerifiche() {
		// Ottenimento id-materie-docenti
		ArrayList<Materia_Docenti> materie_docenti = IoOperations.getElencoMaterieDocente(ID_DOCENTE);

		// Inserimento verifiche nela tabella delle verifiche
		ArrayList<Verifica> verifiche = new ArrayList<Verifica>();
		for (int i = 0; i < materie_docenti.size(); i++) {
			verifiche.addAll(IoOperations.getVerificheAssegnateSuDocente(materie_docenti.get(i).getId_materia_docenti(),
					ID_DOCENTE));
		}

		// Creazione elenco utenti e preparazione per caricarli sulla tabella
		int i = 0;
		while (i < verifiche.size()) {
			valori.add(new VerificaStudente(verifiche.get(i).getId_verifica(), verifiche.get(i).getArgomento(),
					IoOperations.getClasseFromIDClasse(verifiche.get(i).getId_classe()),
					IoOperations.getUtenteDatoID(verifiche.get(i).getId_utente()).getNome(),
					IoOperations.getUtenteDatoID(verifiche.get(i).getId_utente()).getCognome(),
					verifiche.get(i).getScadenza(), verifiche.get(i).getIs_eseguita(), verifiche.get(i).getVoto()));
			i++;
		}
	}

	@FXML
	public void pulsanteIndietro() {
		try {
			Parent ritornoPaginaPrincipale;
			ritornoPaginaPrincipale = (StackPane) FXMLLoader.load(getClass().getResource("/docenti/schermata_docenti.fxml"));
			
			Scene scena = new Scene(ritornoPaginaPrincipale);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(scena);
			stage.setTitle("Menù principale docenti");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(RisultatiVerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void pulsanteVisualizzaVerifica() {
		int id_verifica = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getId_verifica();
		verifica = IoOperations.getVerificaFromId(id_verifica);
		
		try {
			Parent visualizzaVerifica;
			visualizzaVerifica = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_voti/VisualizzazioneVerifica.fxml"));
			
			Scene admin = new Scene(visualizzaVerifica);
			
			Stage scena = (Stage) rootPane.getScene().getWindow();
			
			scena.setScene(admin);
			scena.setTitle("Menù principale docenti");
			scena.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(RisultatiVerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void pulsanteVisualizzaSituazioneGenerale() {
		int id_verifica = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getId_verifica();
		verifica = IoOperations.getVerificaFromId(id_verifica);
		
		try {
			ID_VERIFICA = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getId_verifica();
			
			Parent visualizzazioneGenerale;
			visualizzazioneGenerale = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_voti/VotiVerificaClasse.fxml"));
			
			Scene scena = new Scene(visualizzazioneGenerale);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(scena);
			stage.setTitle("Menù principale docenti");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(RisultatiVerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/** Metodo per ottenere l'id della verifica selezionata dalla tabella
	 * @param argomento è l'argomento della verifica selezionata
	 * @param nome dello studente a cui è stata assegnata la verifica
	 * @param cognome dello studente a cui è stata assegnata la verifica
	 * @param scadenza della verifica
	 * @return l'id della verifica dopo averne controllato la presenza nell'elenco
	 */
	private int getIdVerificaSelezionata(VerificaStudente vs) {
		for(int i = 0; i < valori.size(); i++) {
			if(valori.get(i).getArgomento().equalsIgnoreCase(vs.getArgomento()) && valori.get(i).getNome().equalsIgnoreCase(vs.getNome()) &&
					valori.get(i).getCognome().equalsIgnoreCase(vs.getCognome()) && valori.get(i).getScadenza().equals(vs.getScadenza()))
				return valori.get(i).getId_verifica();
		}
		return 0;
	}
	
	public boolean controlloAnnoPresente(ArrayList<String>elenco, String anno) {
		for(int i = 0; i < elenco.size(); i++) {
			if(elenco.get(i).equals(anno))
				return true;
		}
		return false;
	}

	public static Verifica getVerifica() {
		return verifica;
	}

	public static int getID_DOCENTE() {
		return ID_DOCENTE;
	}

	public static int getID_VERIFICA() {
		return ID_VERIFICA;
	}
	
}
