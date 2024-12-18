package studenti_voti;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import classi_tabelle.IoOperations;
import classi_tabelle.Verifica;
import docenti_voti.RisultatiVerificheController;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import studenti.SchermataPrincipaleStudentiController;
import studenti_verifiche.StudentiVerificheController;

public class StudentiVotiController implements Initializable{
	
	//Codice ID per individuare il docente che ha effettuato l'accesso
	public static int ID_STUDENTE;
	// Verifica selezionata dallo studente per la visualizzazione
	private static Verifica test;
		
	@FXML private StackPane rootPane;
    @FXML private TableView<Verifica> tabellaVerificheEsegui;
    @FXML private TableColumn<Verifica, String> materiaVerifica;
    @FXML private TableColumn<Verifica, Integer> domandeCorrette;
    @FXML private TableColumn<Verifica, Integer> domandeTotali;
    @FXML private TableColumn<Verifica, Double> votoVerifica;
    @FXML private JFXButton backButton;
    @FXML private Label benvenutoLabel;
    @FXML private ChoiceBox<String> sceltaAnnoScolastico;
    @FXML private JFXTextField textfieldCercaVerificaExe;
    @FXML private JFXButton visualizzaButton;
    
    Verifica verifica;
    ObservableList<Verifica> valori = FXCollections.observableArrayList();
    MenuItem item;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ID_STUDENTE = SchermataPrincipaleStudentiController.getID_STUDENTE();
		
		visualizzaButton.setVisible(false);
		
		String[] nome_cognome_studente = new String[2];
		nome_cognome_studente[0] = IoOperations.getUtenteDatoID(ID_STUDENTE).getNome();
		nome_cognome_studente[1] = IoOperations.getUtenteDatoID(ID_STUDENTE).getCognome();
		nome_cognome_studente[0] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[0]);
		nome_cognome_studente[1] = IoOperations.rendiMaiuscolaPrimaLetteraNomi(nome_cognome_studente[1]);
		benvenutoLabel.setText(nome_cognome_studente[0] + " " + nome_cognome_studente[1]);
		ArrayList<Verifica>verifiche_studente = IoOperations.getVerificheEseguiteStudente(ID_STUDENTE);
		
		// Creazione elenco utenti e preparazione per caricarli sulla tabella
		int i = 0;
		while (i < verifiche_studente.size()) {
			Verifica v = new Verifica(verifiche_studente.get(i).getId_verifica(), verifiche_studente.get(i).getArgomento(),
					verifiche_studente.get(i).getIs_eseguita(), verifiche_studente.get(i).getIs_assegnata(),
					verifiche_studente.get(i).getPunteggio_ottenuto(), verifiche_studente.get(i).getPunteggio_totale(),
					verifiche_studente.get(i).getVoto(), verifiche_studente.get(i).getTempo(),
					verifiche_studente.get(i).getScadenza(), verifiche_studente.get(i).getId_classe(), verifiche_studente.get(i).getId_utente(),
					verifiche_studente.get(i).getId_materia_docente());
			
			//Controllo e aggiunta della verifica alla lista solo se non è stata eseguita
			if(v.getIs_eseguita().equalsIgnoreCase("SI"))
				valori.add(v);
			i++;
		}
		materiaVerifica.setCellValueFactory(new PropertyValueFactory<>("argomento"));
		domandeCorrette.setCellValueFactory(new PropertyValueFactory<>("punteggio_ottenuto"));
		domandeTotali.setCellValueFactory(new PropertyValueFactory<>("punteggio_totale"));
		votoVerifica.setCellValueFactory(new PropertyValueFactory<>("voto"));
		tabellaVerificheEsegui.setItems(valori);
		
		// GESTIONE SELEZIONE DI UNA VERIFICA DELLA TABELLA
		tabellaVerificheEsegui.setRowFactory( tv -> {
	        TableRow<Verifica> row = new TableRow<>();
	        row.setOnMouseClicked(event -> 
	        {
	            if (event.getClickCount() == 2 && (! row.isEmpty()) )
	            {	                
			  		// Controllo che lo studente abbia eseguito la verifica
			  		if(tabellaVerificheEsegui.getSelectionModel().getSelectedIndex() != -1)
			  			visualizzaButton.setVisible(true);
				  	else
				  		visualizzaButton.setVisible(false);
	            }
	        });
	        return row;
		});
		
		// Ottenimento di tutte le verifiche eseguite dello studente
		ArrayList<Verifica> verifiche = IoOperations.getVerificheEseguiteStudente(ID_STUDENTE);
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
				
		// Impostazione anni
		String anno_attuale;
		if(LocalDate.now().getMonthValue() > 8 && LocalDate.now().getMonthValue() < 12) {
			anno_attuale = LocalDate.now().getYear() + "/" + (LocalDate.now().getYear() + 1);
		}
		else{
			anno_attuale = (LocalDate.now().getYear() - 1) + "/" + LocalDate.now().getYear();
		}
		sceltaAnnoScolastico.setValue(anno_attuale);
		
		ObservableList<String> date = FXCollections.observableArrayList();
		
		// Controllo se il docente non ha mai avuto verifiche
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
	
	@FXML
	public void visualizzaVerifica() {
		int id_verifica = tabellaVerificheEsegui.getSelectionModel().getSelectedItem().getId_verifica();
		test = IoOperations.getVerificaFromId(id_verifica);
		
		try {
			Parent ritornoPaginaAdmin;
			ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_voti/VisualizzazioneVerificaStudente.fxml"));
			
			Scene admin = new Scene(ritornoPaginaAdmin);
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			
			stage.setScene(admin);
			stage.setTitle("Menù principale studente");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(RisultatiVerificheController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static Verifica getTest() {
		return test;
	}
	
}
