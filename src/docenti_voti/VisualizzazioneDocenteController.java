package docenti_voti;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import classi_tabelle.Domanda;
import classi_tabelle.IoOperations;
import classi_tabelle.Risposta;
import classi_tabelle.Verifica;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VisualizzazioneDocenteController implements Initializable {

	// Verifica selezionata dal docente per la visualizzazione
	private static Verifica verifica;
	
	@FXML private StackPane rootPane;
	@FXML private ScrollPane scroolPane;
	@FXML private Label labelTitoloVerifica;
    @FXML private Label labelDomanda;
    @FXML private HBox hbox1;
    @FXML private JFXCheckBox check1;
    @FXML private Label id1;
    @FXML private ImageView img1;
    @FXML private HBox hbox3;
    @FXML private JFXCheckBox check3;
    @FXML private Label id3;
    @FXML private ImageView img3;
    @FXML private HBox hbox5;
    @FXML private JFXCheckBox check5;
    @FXML private Label id5;
    @FXML private ImageView img5;
    @FXML private HBox hbox7;
    @FXML private JFXCheckBox check7;
    @FXML private Label id7;
    @FXML private ImageView img7;
    @FXML private HBox hbox2;
    @FXML private JFXCheckBox check2;
    @FXML private Label id2;
    @FXML private ImageView img2;
    @FXML private HBox hbox4;
    @FXML private JFXCheckBox check4;
    @FXML private Label id4;
    @FXML private ImageView img4;
    @FXML private HBox hbox6;
    @FXML private JFXCheckBox check6;
    @FXML private Label id6;
    @FXML private ImageView img6;
    @FXML private HBox hbox8;
    @FXML private JFXCheckBox check8;
    @FXML private Label id8;
    @FXML private ImageView img8;
    @FXML private JFXButton buttonViewImage;
    @FXML private Label labelNrDomandaCorrente;
    @FXML private Label labelNrDomande;
    @FXML private JFXButton indietroButton;
    @FXML private JFXButton terminaButton;
    @FXML private JFXButton AvantiButton;
	
    private ArrayList<Domanda> domande_verifica;
    private int nr_domande;
    private int nr_domanda_corrente;
    
    private ObservableList<JFXCheckBox> elenco_checkbox;
    private ObservableList<HBox> elenco_hbox;
    private ObservableList<Label> elenco_label_id;
    private ObservableList<ImageView> elenco_immagini;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		verifica = RisultatiVerificheController.getVerifica();
		
		indietroButton.setDisable(true);
		
		elenco_checkbox = FXCollections.observableArrayList();
		elenco_hbox = FXCollections.observableArrayList();
		elenco_label_id = FXCollections.observableArrayList();
		elenco_immagini = FXCollections.observableArrayList();
		
		// Impostata materia e argomento verifica
		labelTitoloVerifica.setText("Verifica di " + verifica.getArgomento());
		// Ottenimento elenco domande della verifica
		domande_verifica = IoOperations.getDomandeVerifica(verifica.getId_verifica());
		// Calcolo numero domande totali della verifica
		nr_domande = domande_verifica.size();
		// Impostato il numero totale delle domande
		labelNrDomande.setText(nr_domande + "");
		
		nr_domanda_corrente = 0;
		// Impostato il numero della domanda corrente
		labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
		
		// Disabilitazione della selezione dei checkbox dato che la verifica la si sta visualizzando
		check1.setDisable(true);check2.setDisable(true);check3.setDisable(true);check4.setDisable(true);
		check5.setDisable(true);check6.setDisable(true);check7.setDisable(true);check8.setDisable(true);
		
		// Inserimento checkboxes della schermata
		elenco_checkbox.addAll(check1, check2, check3, check4, check5, check6, check7, check8);
		// Inserimento hboxes della schermata relative ai checkbox
		elenco_hbox.addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7, hbox8);
		// Inserimento label contenente gli id delle risposte della domanda
		elenco_label_id.addAll(id1, id2, id3, id4, id5, id6, id7, id8);
		// Inserimento immagini delle risposte
		elenco_immagini.addAll(img1, img2, img3, img4, img5, img6, img7, img8);
		
		// Avviata la visualizzazione della prima domanda
		scorrimentoDomande();
	}
	
	/** METODO PER LA PRESENTAZIONE DELLE DOMANDE DELLA VERIFICA
	 */
	private void scorrimentoDomande() {
		// Vengono rese visibili tutte le righe delle risposte
		showAllHboxes();
		
		// Ottenimento domanda
		Domanda d = domande_verifica.get(nr_domanda_corrente);
		// inserimento del testo della domanda nella label della schermata
		labelDomanda.setText(d.getTesto());
		
		if(d.getId_immagine() == 0)
			buttonViewImage.setVisible(false);
		else
			buttonViewImage.setVisible(true);
		
		// Ottenimento risposte della domanda
		ArrayList<Risposta> risposte_domanda = IoOperations.getRisposteEsecuzioneVerifica(d.getId_domanda());
		
		// Nascoste le righe non utili
		hideHBoxesNotUsed(risposte_domanda.size());
		
		// Inserimento testi risposte
		for(int i = 0; i < risposte_domanda.size(); i++) 
		{
			// Inserimento del testo della domanda
			elenco_checkbox.get(i).setText(risposte_domanda.get(i).getTesto());
			
			// Inserimento id delle risposte
			elenco_label_id.get(i).setText(risposte_domanda.get(i).getId_risposta() + "");
			
			// Inserimento dell'eventuale immagine
			if(risposte_domanda.get(i).getId_immagine() > 0)
				elenco_immagini.get(i).setImage(IoOperations.getImmagineDatoId(risposte_domanda.get(i).getId_immagine()));	
			
			// Selezione delle risposte effettuate dallo studente
			elenco_checkbox.get(i).setSelected(risposte_domanda.get(i).getIs_selezionata().equals("SI"));
			
			// Inserimento colori basato sulla correttezza delle risposte date
			if(risposte_domanda.get(i).getCorretta().equals("SI"))
				elenco_hbox.get(i).setStyle("-fx-background-color: #a4d6a3;");
			else if(risposte_domanda.get(i).getCorretta().equals("NO") && risposte_domanda.get(i).getIs_selezionata().equals("SI"))
				elenco_hbox.get(i).setStyle("-fx-background-color: #ff8080;");
		}
	}
	
	/** METODO CHE DATO IL NUMERO DI RISPOSTE, SE NON VIENE RAGGIUNTO IL NUMERO MASSIMO
	 * DI 8 RISPOSTE UTILIZZATE PER LA DOMANDA, VENGONO NASCOSTE LE RIGHE
	 * @param n
	 */
	private void hideHBoxesNotUsed(int n) {
		while(n < 8) {
			elenco_hbox.get(n).setVisible(false);
			elenco_hbox.get(n).setStyle("-fx-background-color: transparent;");
			n++;
		}
	}
	
	/** METODO PER MOSTRARE NUOVAMENTE TUTTE LE RIGHE ESISTENTI RELATIVE ALLE RISPOSTE
	 */
	private void showAllHboxes() {
		for(int i = 0; i < elenco_hbox.size(); i++) {
			elenco_hbox.get(i).setVisible(true);
			elenco_hbox.get(i).setStyle("-fx-background-color: #fff;");
			elenco_immagini.get(i).setImage(null);
		}
	}
	
	@FXML
	public void pulsanteTermina() {
		try {
			Parent ritornoPaginaPrecedente;
			ritornoPaginaPrecedente = (StackPane) FXMLLoader.load(getClass().getResource("/docenti_voti/RisultatiVerifiche.fxml"));
			
			Scene scena = new Scene(ritornoPaginaPrecedente);
			scena.getStylesheets().add(getClass().getResource("/main_package/Style.css").toExternalForm());
			
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(scena);
			stage.setTitle("Menù principale docenti");
			stage.setResizable(false);
		} catch (IOException e) {
			Logger.getLogger(VisualizzazioneDocenteController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@FXML
	public void pulsanteIndietro() {
		// Decremento della variabile che indica la domanda corrente
		nr_domanda_corrente--;
		labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
				
		// Controllo pulsanti se ci si trova a inizio test
		if(nr_domanda_corrente + 1 == 1)
			indietroButton.setDisable(true);
		if(AvantiButton.isDisable())
			AvantiButton.setDisable(false);
		
		// Visualizzazione della domanda precedente
		scorrimentoDomande();
	}
	
	@FXML 
	public void pulsanteAvanti() {
		// Incremento della variabile che indica la domanda corrente
		nr_domanda_corrente++;
		labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
		
		// Controllo pulsanti se ci si trova a fine test
		if(nr_domanda_corrente + 1 == nr_domande)
			AvantiButton.setDisable(true);
		if(indietroButton.isDisable())
			indietroButton.setDisable(false);
		
		// Visualizzazione della domanda successiva
		scorrimentoDomande();
	}
	
	@FXML
	public void showImmagine() {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton();
		button.setStyle("-fx-text-fill:transparent; ");
		FontAwesomeIcon fntIcon = new FontAwesomeIcon();
		fntIcon.setGlyphName("TIMES");
		fntIcon.setSize("20px");
		fntIcon.setStyle("-fx-fill:white");
		
		button.setGraphic(fntIcon);
		button.setStyle("-fx-background-color:red; -fx-border-color:red; -fx-text-fill: white;"); 
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		
		ImageView img = new ImageView();
		img.setFitWidth(350);
		img.setFitHeight(350);
		img.setImage(IoOperations.getImmagineDatoId(domande_verifica.get(nr_domanda_corrente).getId_immagine()));
		
		dialogLayout.setBody(img);
		dialogLayout.setActions(button, new Label("                                                    "));
		dialog.show();
	}
	
}
