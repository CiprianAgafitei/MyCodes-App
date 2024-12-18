package studenti_verifiche;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import classi_tabelle.Verifica;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import studenti.SchermataPrincipaleStudentiController;

public class StudentiVerificheController implements Initializable {
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_STUDENTE;
	public static Verifica verifica_selezionata;	
	
	@FXML private StackPane rootPane;
	@FXML private TableView<Verifica> tabellaVerificheEsegui;
	@FXML private TableColumn<Verifica, String> materiaVerifica;
	@FXML private TableColumn<Verifica, Time> tempoVerifica;
	@FXML private TableColumn<Verifica, String> scadenzaVerifica;
    @FXML private Label benvenutoLabel;
    @FXML private ChoiceBox<String> sceltaAnnoScolastico;
    @FXML private JFXTextField textfieldCercaVerificaExe;
    @FXML private JFXButton backButton;
    @FXML private JFXButton eseguiButton;
    
    ObservableList<Verifica> valori = FXCollections.observableArrayList();
    MenuItem item;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ID_STUDENTE = SchermataPrincipaleStudentiController.getID_STUDENTE();
				
		// Controllo se si proviene dalla fine dell'esecuzione di una verifica
		if(ID_STUDENTE == 0)
			ID_STUDENTE = EsecuzioneVerificheController.getIdstudente();
		
		eseguiButton.setVisible(false);
		
		String[] nome_cognome_studente = new String[2];
		nome_cognome_studente[0] = IoOperations.getUtenteDatoID(ID_STUDENTE).getNome();
		nome_cognome_studente[1] = IoOperations.getUtenteDatoID(ID_STUDENTE).getCognome();			
		nome_cognome_studente[0] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[0]);
		nome_cognome_studente[1] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[1]);
		benvenutoLabel.setText(nome_cognome_studente[0] + " " + nome_cognome_studente[1]);
		ArrayList<Verifica>verifiche_studente = IoOperations.getVerificheNonEseguiteStudente(ID_STUDENTE);
				
		//Creazione elenco verifiche e preparazione per caricarle sulla tabella
		int i = 0;
		while (i < verifiche_studente.size()) {
			Verifica v = new Verifica(verifiche_studente.get(i).getId_verifica(), verifiche_studente.get(i).getArgomento(),
					verifiche_studente.get(i).getIs_eseguita(), verifiche_studente.get(i).getIs_assegnata(),
					verifiche_studente.get(i).getPunteggio_ottenuto(), verifiche_studente.get(i).getPunteggio_totale(),
					verifiche_studente.get(i).getVoto(), verifiche_studente.get(i).getTempo(),
					verifiche_studente.get(i).getScadenza(), verifiche_studente.get(i).getId_classe(), verifiche_studente.get(i).getId_utente(),
					verifiche_studente.get(i).getId_materia_docente());
			
			//Controllo e aggiunta della verifica alla lista solo se non è stata eseguita
			if(v.getIs_eseguita().equalsIgnoreCase("NO"))
				valori.add(v);
			i++;
		}
		materiaVerifica.setCellValueFactory(new PropertyValueFactory<>("argomento"));
		tempoVerifica.setCellValueFactory(new PropertyValueFactory<>("tempo"));
		scadenzaVerifica.setCellValueFactory(new PropertyValueFactory<>("scadenza"));
		tabellaVerificheEsegui.setItems(valori);
		
		//GESTIONE SELEZIONE DI UNA VERIFICA DELLA TABELLA
		tabellaVerificheEsegui.setRowFactory( tv -> {
	        TableRow<Verifica> row = new TableRow<>();
	        row.setOnMouseClicked(event -> 
	        {
	            if (event.getClickCount() == 2 && (! row.isEmpty()) )
	            {	                
			  		// Controllo che lo studente abbia eseguito la verifica
			  		if(tabellaVerificheEsegui.getSelectionModel().getSelectedIndex() != -1)
			  			eseguiButton.setVisible(true);
				  	else
				  		eseguiButton.setVisible(false);
	            }
	        });
	        return row;
		});
		
		//Ottenimento di tutte le verifiche dello studente non eseguite
		ArrayList<Verifica> verifiche = IoOperations.getVerificheNonEseguiteStudente(ID_STUDENTE);
		ArrayList<String> anni_verifica = new ArrayList<String>();

		// Inserimento date delle verifiche per riconoscere gli anni
		for (i = 0; i < verifiche.size(); i++) {
			int anno_selezionato = LocalDate.parse(verifiche.get(i).getScadenza()).getYear();
			String confronto = "";
			if (LocalDate.parse(verifiche.get(i).getScadenza()).getMonthValue() <= 8)
				confronto = (anno_selezionato - 1) + "/" + anno_selezionato;
			else
				confronto = anno_selezionato + "/" + (anno_selezionato - 1);

			// controllo se la data è stat già inserita
			if (!controlloAnnoPresente(anni_verifica, confronto)) {
				anni_verifica.add(confronto);
			}
		}
				
		//Impostazione anni
		String anno_attuale;
		if(LocalDate.now().getMonthValue() > 8 && LocalDate.now().getMonthValue() < 12) {
			anno_attuale = LocalDate.now().getYear() + "/" + (LocalDate.now().getYear() + 1);
		}
		else{
			anno_attuale = (LocalDate.now().getYear() - 1) + "/" + LocalDate.now().getYear();
		}
		sceltaAnnoScolastico.setValue(anno_attuale);
		
		ObservableList<String> date = FXCollections.observableArrayList();
		
		//Controllo se il docente non ha mai avuto verifiche
		if(anni_verifica.size() == 0) {
			/*Controllo data in cui si sta accedendo: se compreso tra settembre e novembre, viene creata
				una data a partire dall'anno attuale con il successivo*/
			if(LocalDate.now().getMonthValue() > 8 && LocalDate.now().getMonthValue() < 12) 
			{
				date.add(LocalDate.now().getYear() + "/" + (LocalDate.now().getYear() + 1));
			}
			else
			{
				date.add((LocalDate.now().getYear() - 1) + "/" + LocalDate.now().getYear());
			}
		}
		else {
			date.addAll(anni_verifica);
		}
		sceltaAnnoScolastico.setItems(date);
		
		//Ricerca campi verifica
		FilteredList<Verifica> filteredData2 = new FilteredList<>(valori, b -> true);

		textfieldCercaVerificaExe.textProperty().addListener((observable, oldValue, newValue) -> {
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
		sortedData2.comparatorProperty().bind(tabellaVerificheEsegui.comparatorProperty());
		tabellaVerificheEsegui.setItems(sortedData2);
	}
	
	@FXML
	public void pulsanteBackSelezionato() {
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti/SchermataPrincipaleStudenti.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(admin);
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(StudentiVerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
    
	public boolean controlloAnnoPresente(ArrayList<String>elenco, String anno) {
		for(int i = 0; i < elenco.size(); i++) {
			if(elenco.get(i).equals(anno))
				return true;
		}
		return false;
	}
	
    public static int getID_STUDENTE() {
		return ID_STUDENTE;
	}

	public static Verifica getVerifica_selezionata() {
		return verifica_selezionata;
	}
	
	@FXML
	public void eseguiPremuto() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(tabellaVerificheEsegui.getSelectionModel().getSelectedItem().getScadenza(), formatter);
		
		// Controllo se la verifica non è scaduta
		if(localDate.isBefore(LocalDate.now())) 
		{
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			JFXButton button = new JFXButton("Ok");
			button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;"); 
			JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
			dialogLayout.setHeading(new Label("Scadenza verifica"));
			dialogLayout.setBody(new Label("Attenzione! La verifica selezionata è scaduta. Contattare il docente per la sua riattivazione."));
			dialogLayout.setActions(button);
			dialog.show();
		}
		else {
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
				dialog.close();
				
				int id_verifica = tabellaVerificheEsegui.getSelectionModel().getSelectedItem().getId_verifica();
				verifica_selezionata = IoOperations.getVerificaFromId(id_verifica);
				
				try {
					Parent ritornoPaginaAdmin;
					ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_verifiche/EsecuzioneVerifica.fxml"));
					
					Scene admin = new Scene(ritornoPaginaAdmin);
					
					IoOperations.aggiornaStatoVerificaEseguita(verifica_selezionata);
					
					Stage stage = (Stage) rootPane.getScene().getWindow();
					
					stage.setScene(admin);
					stage.setTitle("Esecuzione verifica");
					stage.setResizable(false);
				} catch (IOException e) {
					Logger.getLogger(StudentiVerificheController.class.getName()).log(Level.SEVERE, null, e);
				}
			});
			dialogLayout.setHeading(new Label("Esecuzione della verifica"));
			
			Label l = new Label("Sei sicuro di voler cominciare la verifica?");
			l.setWrapText(true);
			
			dialogLayout.setBody(l);
			dialogLayout.setActions(button1, new Label("                               "), button2, new Label("             "));
			dialog.show();
		}
	}
}
