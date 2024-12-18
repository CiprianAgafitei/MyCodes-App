package docenti_verifiche;

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
import classi_tabelle.Classe;
import classi_tabelle.Domanda;
import classi_tabelle.IoOperations;
import classi_tabelle.Risposta;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VerificheController implements Initializable {

	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_DOCENTE;
		
	@FXML private StackPane rootPane;
	@FXML private BorderPane borderPane;
    @FXML private Label nomeCognomeLabel;
    @FXML private ChoiceBox<String> choiceAnno;
    @FXML private TableView<Verifica> tabellaVerificheAssegnate;
    @FXML private TableColumn<Verifica, String> materiaColumn;
    @FXML private TableColumn<Verifica, String> tempoColumn;
    @FXML private TableColumn<Verifica, String> scadenzaColumn;
    @FXML private TableColumn<Verifica, String> assegnataColumn;
    @FXML private JFXTextField textfieldCercaVerificaAssegnata;
    @FXML private JFXButton buttonMoreOptions;
    @FXML private JFXButton backButton;

    ObservableList<Verifica> valori = FXCollections.observableArrayList();
    ObservableList<Domanda> domande = FXCollections.observableArrayList();
    ObservableList<Classe> classi = FXCollections.observableArrayList();
    JFXTextField cerca_domanda;
    Verifica verifica;
    MenuItem item1;
    MenuItem item2;
    MenuItem item3;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ID_DOCENTE = SchermataDocentiController.getID_DOCENTE();
		
		//Inserimento verifiche nela tabella delle verifiche
		ArrayList<Verifica> verifiche = new ArrayList<Verifica>();
		verifiche = IoOperations.getVerifiche(ID_DOCENTE);

		// Creazione elenco utenti e preparazione per caricarli sulla tabella
		inserimentoVerifiche(verifiche);
		
		materiaColumn.setCellValueFactory(new PropertyValueFactory<>("argomento"));
		tempoColumn.setCellValueFactory(new PropertyValueFactory<>("tempo"));
		scadenzaColumn.setCellValueFactory(new PropertyValueFactory<>("scadenza"));
		assegnataColumn.setCellValueFactory(new PropertyValueFactory<>("is_assegnata"));
		tabellaVerificheAssegnate.setItems(valori);

		//Ricerca campi verifica
		FilteredList<Verifica> filteredData2 = new FilteredList<>(valori, b -> true);

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
	  			return false;
	  		});
	  	});
	  	SortedList<Verifica> sortedData2 = new SortedList<>(filteredData2);
	  	sortedData2.comparatorProperty().bind(tabellaVerificheAssegnate.comparatorProperty());
	  	tabellaVerificheAssegnate.setItems(sortedData2);
	  	
	  	//PROVA PER GESTIONE DIRETTA DELLE OPZIONI DI CIASCUNA RIGA DELLA TABELLA        
        tabellaVerificheAssegnate.setRowFactory(tva -> {
        	final TableRow<Verifica> row = new TableRow<>();
	        final ContextMenu rowMenu = new ContextMenu();
	        ContextMenu tableMenu = tabellaVerificheAssegnate.getContextMenu();
	        if (tableMenu != null) {
	        	rowMenu.getItems().addAll(tableMenu.getItems());
	        	rowMenu.getItems().add(new SeparatorMenuItem());
	        }
	        item1 = new MenuItem("Visualizza");
	        item2 = new MenuItem("Rimuovi");
	        item3 = new MenuItem("Assegna");
	        
	        rowMenu.getItems().addAll(item1, item2, item3);
	        row.contextMenuProperty().bind(
	        		Bindings.when(Bindings.isNotNull(row.itemProperty()))
	        		.then(rowMenu)
	        		.otherwise((ContextMenu) null));
	            
	        item1.disableProperty().bind(Bindings.isEmpty(tabellaVerificheAssegnate.getSelectionModel().getSelectedItems()));
	        item1.setOnAction(e -> {
	        	visualizzaVerifica();
	        });
	        item2.disableProperty().bind(Bindings.isEmpty(tabellaVerificheAssegnate.getSelectionModel().getSelectedItems()));
	        item2.setOnAction(e -> {
	        	rimuoviVerifica();
	        });
	        item3.disableProperty().bind(Bindings.isEmpty(tabellaVerificheAssegnate.getSelectionModel().getSelectedItems()));
	        item3.setOnAction(e -> {
	        	assegnaVerifica();
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

	private void inserimentoVerifiche(ArrayList<Verifica> verifiche) {
		for (int i = 0; i < verifiche.size(); i++) {
			valori.add(new Verifica(verifiche.get(i).getId_verifica(), verifiche.get(i).getArgomento(),
					verifiche.get(i).getIs_eseguita(), verifiche.get(i).getIs_assegnata(),
					verifiche.get(i).getPunteggio_ottenuto(), verifiche.get(i).getPunteggio_totale(),
					verifiche.get(i).getVoto(), verifiche.get(i).getTempo(), verifiche.get(i).getScadenza(),
					verifiche.get(i).getId_classe(), verifiche.get(i).getId_utente(),
					verifiche.get(i).getId_materia_docente()));
		}
	}

	@FXML
	public void pulsanteIndietro() {
		try {
			Parent schermataDocenti;
			schermataDocenti = (StackPane) FXMLLoader.load(getClass().getResource("/docenti/schermata_docenti.fxml"));
			
			Scene scene = new Scene(schermataDocenti);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(scene);
			stage.setTitle("Menù principale docenti");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(VerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML
	public void pulsanteOpzioni() {
		VBox creazioneVerificaScene = null;
		try {
			creazioneVerificaScene = (VBox) FXMLLoader.load(getClass().getResource("/docenti_verifiche/MenuOpzioniVerifiche.fxml"));
		} catch (IOException e) {
			Logger.getLogger(VerificheController.class.getName()).log(Level.SEVERE, null, e);
		}

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialogLayout.setBody(creazioneVerificaScene);
		dialog.show();
	}
	
	/**METODO PER LA GESTIONE DELL'ASSEGNAZIONE DELLA VERIFICA AD UNA CLASSE O CONTROLLO DEL SUO STATO
	 * 								(se assegnata o no)
	 * @param v è la verifica per la quale controllare lo stato e, se non già assegnata, gestire la sua assegnazione
	 */
	private void assegnaVerifica() {
		//Ottenimento della verifica selezionata
        verifica = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem();
        
		//Inizializzazione della finestra di dialogo
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("Annulla");
		JFXButton button2 = new JFXButton("Assegna");
		button1.setStyle("-fx-background-color:red; -fx-text-fill: white;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		button2.setStyle("-fx-background-color:#1976D2; -fx-text-fill:white;");
		button2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
			
		//Inizializzazione del corpo della finestra di dialogo
		VBox vbox = new VBox();
		
		HBox h1 = new HBox();
		Label l1h1 = new Label();
		l1h1.setText(verifica.getArgomento());
		h1.getChildren().add(l1h1);
		h1.setAlignment(Pos.CENTER);
		
		HBox h2 = new HBox();
		h2.setPadding(new Insets(20, 0, 10, 0));
		Label l1h2 = new Label();
		l1h2.setText("Classe: " + IoOperations.getClasseFromIDClasse(verifica.getId_classe()) + "             ");
		Label l2h2 = new Label();
		l2h2.setText("NR. domande: " + IoOperations.contaDomandeVerifica(verifica));
		h2.setAlignment(Pos.CENTER);
		h2.getChildren().addAll(l1h2, l2h2);
		
		HBox h3 = new HBox();
		
		h3.setPadding(new Insets(10, 0, 20, 0));
		h3.setAlignment(Pos.CENTER);
		//Elenco classi
		ChoiceBox<String> cb1_classe = new ChoiceBox<String>();
		ObservableList<String> elenco_classi = FXCollections.observableArrayList();
		ArrayList<Classe> classi = IoOperations.getElencoClassi();
		for(int i = 0; i < classi.size(); i++) {
			elenco_classi.add(classi.get(i).getClasse());
		}
		cb1_classe.setItems(elenco_classi);
		//Elenco possibili suggerimenti di domande
		ChoiceBox<Integer> cb2_nd_domande = new ChoiceBox<Integer>();
		ObservableList<Integer> elenco_nr_domande= FXCollections.observableArrayList();
		for(int i = IoOperations.contaDomandeVerifica(verifica); i > IoOperations.contaDomandeVerifica(verifica) / 2; i--) {
			elenco_nr_domande.add(i);
		}
		cb2_nd_domande.setItems(elenco_nr_domande);
		cb2_nd_domande.setValue(IoOperations.contaDomandeVerifica(verifica));
		
		h3.getChildren().addAll(cb1_classe, new Label("             "), cb2_nd_domande);
		
		HBox h4 = new HBox();
		Label messaggio_errore = new Label("Campo classe vuoto.");
		h4.getChildren().add(messaggio_errore);
		h4.setVisible(false);
		
		HBox h5 = new HBox();
		Label messaggio_aiuto = new Label("Scegliendo un numero di domande inferiore al totale, verranno scelte casualmente quelle di ciascuno studente.");
		messaggio_aiuto.setWrapText(true);
		h5.getChildren().add(messaggio_aiuto);
		
		vbox.getChildren().addAll(h1, h2, h3, h5, h4);
		
		Label l = new Label("Assegnazione verifica");
		l.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		dialogLayout.setHeading(l);
		dialogLayout.setBody(vbox);
		
		//Gestione pulsante conferma assegnazione
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			if(cb1_classe.getValue() == null || cb1_classe.getValue().equals("")) {
				cb2_nd_domande.setStyle("-fx-border-color:red;");
				h4.setVisible(true);
			}
			else {
				//Assegnazione agli studenti
				IoOperations.assegnaVerificaATuttiGliStudenti(verifica.getArgomento(), cb2_nd_domande.getValue(), cb1_classe.getValue(), verifica.getId_verifica(), verifica.getTempo(), verifica.getScadenza(), ID_DOCENTE, IoOperations.getDomandeVerifica(verifica.getId_verifica()), IoOperations.getMateriaDocente(verifica.getId_materia_docente()).getMateria());
			
				JFXDialogLayout dialogLayout2 = new JFXDialogLayout();
				JFXButton button3 = new JFXButton("Ok");
				button3.setStyle("-fx-background-color:red; -fx-text-fill: white;"); 
				button3.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
				JFXDialog dialog2 = new JFXDialog(rootPane, dialogLayout2, JFXDialog.DialogTransition.TOP);
				button3.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent2) -> {
					dialog2.close();
					
					//Ritorno alla schermata principale
					pulsanteIndietro();
				});
				dialogLayout2.setHeading(new Label("Assegnazione verifica"));
				dialogLayout2.setBody(new Label("Verifica assegnata con successo!"));
				dialogLayout2.setActions(button3, new Label("                                                 "));
				dialog2.show();
			}
		});
		
		dialogLayout.setActions(button1, new Label("             "), button2, new Label("                      "));
		dialog.show();

		if(verifica.getId_classe() > 0) 
		{
			button2.setDisable(true);
			cb1_classe.setDisable(true);
			cb2_nd_domande.setDisable(true);
		}
	}
	
	public void visualizzaVerifica() {
		//Ottenimento della verifica selezionata
        verifica = tabellaVerificheAssegnate.getSelectionModel().getSelectedItem();
		
		VBox contenuto_scrollpane = new VBox();
		
		//Inserimento Primi due elementi comuni a tutte le verifiche
		HBox intestazione = new HBox();
		intestazione.setAlignment(Pos.CENTER);
		intestazione.getChildren().add(new Label(verifica.getArgomento()));
		
		HBox informazioni = new HBox();
		informazioni.getChildren().addAll(new Label("Classe: " + IoOperations.getClasseFromIDClasse(verifica.getId_classe())), new Label(" Tempo: "), new Label(verifica.getTempo().toString()), new Label(" Scadenza: "), new Label(verifica.getScadenza()));
		
		contenuto_scrollpane.getChildren().addAll(intestazione, informazioni);
		
		char[]opzioni = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		//Ottenimento dell'elenco delle domande della verifica da modificare/visualizzare
		ArrayList<Domanda> domande_verifica = IoOperations.getDomandeVerifica(verifica.getId_verifica());
		
		//Inserimento componenti il cui numero varia da verifica a verifica
		for(int i = 0; i < domande_verifica.size(); i++) {
			HBox h_testo_domanda = new HBox();
			h_testo_domanda.getChildren().addAll(new Label("    " + (i+1) + ") "), new Label(domande_verifica.get(i).getTesto()));
			//Inserimento della riga con il testo della domanda
			contenuto_scrollpane.getChildren().add(h_testo_domanda);
			
			//Ottenimento dell'elenco delle risposte relative alla domanda selezionata
			ArrayList<Risposta> risposte_domanda = IoOperations.getRisposteEsecuzioneVerifica(domande_verifica.get(i).getId_domanda());
			
			for(int j = 0; j < risposte_domanda.size(); j++) {
				HBox h_testo_risposta = new HBox();
				h_testo_risposta.getChildren().addAll(new Label("        " + opzioni[j] + ". "), new Label(risposte_domanda.get(j).getTesto()));
				//Inserimento della riga con il testo della risposta
				contenuto_scrollpane.getChildren().add(h_testo_risposta);
			}
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
		dialogLayout.setHeading(new Label("Visualizzazione verifica"));
		dialogLayout.setBody(sp);
		dialogLayout.setActions(button1, new Label("                                                 "));
		dialog.show();
	}
	
	public void rimuoviVerifica() {
		if(tabellaVerificheAssegnate.getSelectionModel().getSelectedItem().getIs_assegnata().equals("SI")) 
		{
			// Le verifiche assegnate non si possono rimuovere
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button1 = new JFXButton("Ok");
			button1.setStyle("-fx-background-color:red; -fx-text-fill: white;"); 
			button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione della verifica"));
			
			Label l = new Label("Attenzione! La verifica è già stata assegnata. Perciò non è possibile rimuovere la verifica.");
			l.setWrapText(true);
			
			dialogLayout.setBody(l);
			dialogLayout.setActions(button1, new Label("                                                 "));
			dialog.show();
		}
		else 
		{
			// Rimozione della verifica
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
				// Rimozione verifica
				IoOperations.rimuoviVerifica(tabellaVerificheAssegnate.getSelectionModel().getSelectedItem());
				valori = FXCollections.observableArrayList();
				ArrayList<Verifica> verifiche = IoOperations.getVerifiche(ID_DOCENTE);
				inserimentoVerifiche(verifiche);
				tabellaVerificheAssegnate.setItems(valori);
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Rimozione della verifica"));
			
			Label l = new Label("Si desidera confermare la rimozione della verifica?");
			l.setWrapText(true);
			
			dialogLayout.setBody(l);
			dialogLayout.setActions(button1, new Label("                               "), button2, new Label("             "));
			dialog.show();
		}
	}

	public boolean controlloAnnoPresente(ArrayList<String>elenco, String anno) {
		for(int i = 0; i < elenco.size(); i++) {
			if(elenco.get(i).equals(anno))
				return true;
		}
		return false;
	}
	
	public static int getID_DOCENTE() {
		return ID_DOCENTE;
	}

}
