package studenti_verifiche;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EsecuzioneVerificheController implements Initializable {

	// Verifica selezionata dal docente per la visualizzazione
	private static Verifica verifica;
	private static int id_studente;
	
	// Gestione timer countdown
	Timer timer;
	int hour;
	int second;
	int minute;
	DecimalFormat dFormat = new DecimalFormat("00");
	String ddSecond, ddMinute, ddHour;
	
	@FXML private StackPane rootPane;
	@FXML private ScrollPane scroolPane;
	@FXML private Label labelTitoloVerifica;
    @FXML private Label labelDomanda;
    @FXML private Text tempo;
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
    @FXML private ScrollPane andamentoVerifica;
	
    private ArrayList<Domanda> domande_verifica;
    private int nr_domande;
    private int nr_domanda_corrente;
    private ArrayList<Risposta> risposte;
    
    private ObservableList<JFXCheckBox> elenco_checkbox;
    private ObservableList<HBox> elenco_hbox;
    private ObservableList<Label> elenco_label_id;
    private ObservableList<ImageView> elenco_immagini;
    
    // Elenco righe svolgimento test
    private ArrayList<HBox> elencoHbox;
    private ArrayList<FontAwesomeIcon> elencoIcone;
    private ArrayList<JFXButton> elencoButtons;
    private ArrayList<Boolean> buttonPremuto;
    
    private double pt_ottenuto;
    private double voto;
    private boolean verifica_finita = false;
        
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		verifica = StudentiVerificheController.getVerifica_selezionata();
		id_studente = verifica.getId_utente();
		
		indietroButton.setDisable(true);
		
		elenco_checkbox = FXCollections.observableArrayList();
		elenco_hbox = FXCollections.observableArrayList();
		elenco_label_id = FXCollections.observableArrayList();
		elenco_immagini = FXCollections.observableArrayList();
		
		// Impostata materia e argomento verifica
		labelTitoloVerifica.setText("Verifica di " + verifica.getArgomento());
		labelTitoloVerifica.setAlignment(Pos.CENTER);
		// Ottenimento elenco domande della verifica
		domande_verifica = IoOperations.getDomandeVerifica(verifica.getId_verifica());
		// Calcolo numero domande totali della verifica
		nr_domande = domande_verifica.size();
		// Impostato il numero totale delle domande
		labelNrDomande.setText(nr_domande + "");
		
		nr_domanda_corrente = 0;
		// Impostato il numero della domanda corrente
		labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
		
		// Inserimento checkboxes della schermata
		elenco_checkbox.addAll(check1, check2, check3, check4, check5, check6, check7, check8);
		// Inserimento hboxes della schermata relative ai checkbox
		elenco_hbox.addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7, hbox8);
		// Inserimento label contenente gli id delle risposte della domanda
		elenco_label_id.addAll(id1, id2, id3, id4, id5, id6, id7, id8);
		// Inserimento immagini delle risposte
		elenco_immagini.addAll(img1, img2, img3, img4, img5, img6, img7, img8);

		if (nr_domande == 1)
			AvantiButton.setDisable(true);
		
		// Creazione elenco righe
		elencoHbox = new ArrayList<HBox>();
		preparazioneElencoRigheScorrimentoVerifica();
		andamentoVerifica.setFitToHeight(true);
		andamentoVerifica.setPrefWidth(100);

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		for (int i = 0; i < elencoHbox.size(); i++) {
			vbox.getChildren().add(elencoHbox.get(i));
		}
		andamentoVerifica.setContent(vbox);

		// Avviata la visualizzazione della prima domanda
		scorrimentoDomande();
	
		// Assegnazione tempo verifica
		LocalTime time = verifica.getTempo().toLocalTime();
		
		// tempo di countdown
		hour = time.getHour();
		String s = Integer.toString(hour) + ":"; 
		minute = time.getMinute();
		s += Integer.toString(minute) + ":00";
		tempo.setText(s);
		second = 0;
		countDownTimer();
		timer.start();
	}
	
	/** METODO PER LA PREPARAZIONE DELL'ELENCO DELLE RIGHE UTILI PER LO SCORRIMENTO DELLA VERIFICA
	 */
	private void preparazioneElencoRigheScorrimentoVerifica() {
		elencoButtons = new ArrayList<JFXButton>();
		elencoIcone = new ArrayList<FontAwesomeIcon>();
		buttonPremuto = new ArrayList<Boolean>();
		
		for(int i = 0; i < nr_domande; i++) 
		{
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			
			Label nr_domanda = new Label((i + 1) + ".   ");
			nr_domanda.setFont(Font.font("Verdana", 18));
			FontAwesomeIcon faiv1 = new FontAwesomeIcon();
			faiv1.setSize("20");
			
			elencoIcone.add(faiv1);
			
			JFXButton jfxb = new JFXButton();
			FontAwesomeIcon faiv2 = new FontAwesomeIcon();
			faiv2.setSize("20");
			faiv2.setGlyphName("FLAG");
			jfxb.setGraphic(faiv2);
			//EYE - QUESTION - CHECK
			
			jfxb.setOnAction(e -> {
				if(!buttonPremuto.get(nr_domanda_corrente)) 
				{
					elencoButtons.get(nr_domanda_corrente).setStyle("-fx-background-color:orange;");
					buttonPremuto.set(nr_domanda_corrente, true);
				}
				else {
					elencoButtons.get(nr_domanda_corrente).setStyle("-fx-background-color:transparent;");
					buttonPremuto.set(nr_domanda_corrente, false);
				}
			});
			
			elencoButtons.add(jfxb);
			if(i != nr_domanda_corrente)
				elencoButtons.get(i).setDisable(true);
			buttonPremuto.add(false);
			
			elencoButtons.get(i).setFocusTraversable(false);
			
			faiv1.setGlyphName("QUESTION");	// Impostato su non visualizzato
			
			hbox.getChildren().addAll(nr_domanda, faiv1, new Label("   "), jfxb);	// Inserimento elementi
			elencoHbox.add(hbox);
		}
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
		
		// Controllo se la domanda prevede immagini
		if(d.getId_immagine() == 0)
			buttonViewImage.setVisible(false);
		else
			buttonViewImage.setVisible(true);
		
		// Ottenimento risposte della domanda
		risposte = IoOperations.getRisposteEsecuzioneVerifica(d.getId_domanda());
		
		// Nascoste le righe non utili
		hideHBoxesNotUsed(risposte.size());
		
		// Inserimento testi risposte
		for(int i = 0; i < risposte.size(); i++) {
			// Inserimento del testo della domanda
			elenco_checkbox.get(i).setText(risposte.get(i).getTesto());
			
			// Inserimento id delle risposte
			elenco_label_id.get(i).setText(risposte.get(i).getId_risposta() + "");
									
			// Inserimento dell'eventuale immagine
			if(risposte.get(i).getId_immagine() > 0)
				elenco_immagini.get(i).setImage(IoOperations.getImmagineDatoId(risposte.get(i).getId_immagine()));
					
			// Selezione delle risposte effettuate dallo studente
			elenco_checkbox.get(i).setSelected(risposte.get(i).getIs_selezionata().equals("SI"));
		}
		
		// Controllo se sono state effettuate selezioni per la domanda
		if(controlloAlmenoUnaRispostaSelezionata())
			elencoIcone.get(nr_domanda_corrente).setGlyphName("CHECK");
		else
			elencoIcone.get(nr_domanda_corrente).setGlyphName("EYE");
	}
	
	/** CONTROLLO SE PER UNA DATA DOMANDA SONO STATE SELEZIONATE RISPOSTE
	 * @return TRUE se sono state selezionate delle risposte; FALSE se non lo sono
	 */
	private boolean controlloAlmenoUnaRispostaSelezionata() {
		for(int i = 0; i < elenco_checkbox.size(); i++) {
			if(elenco_checkbox.get(i).isSelected())
				return true;
		}
		return false;
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
	
	/** METODO PER MOSTRARE NUOVAMENTE TUTTE LE RIGHE ESISTENTI RELATIVE ALLE RISPOSTE E
	 * TOGLIERE LE IMMAGINI DELLE EVENTUALI RISPOSTE CHE LE PREVEDEVANO
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
		// Salvataggio risposte scelte
		salvaRisposteScelte();
		
		// Fermato il timer
		timer.stop();
		
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

			// La verifica viene impostata come eseguita
			IoOperations.aggiornaStatoVerificaEseguita(verifica);

			// Calcolo punteggi ottenuti e voti
			correggiVerifica();

			verifica.setPunteggio_ottenuto(pt_ottenuto);
			verifica.setVoto(voto);

			// Aggiornamento in database del punteggio ottenuto e del voto
			IoOperations.aggiornaPunteggioOttenutoEVoto(verifica, pt_ottenuto, voto);

			try {
				Parent ritornoPaginaAdmin;
				ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_verifiche/RisultatoVerifica.fxml"));
				
				Scene admin = new Scene(ritornoPaginaAdmin);
				
				Stage stage = (Stage) rootPane.getScene().getWindow();
				
				stage.setScene(admin);
				stage.setTitle("Menù principale docenti");
				stage.setResizable(false);
			} catch (IOException e) {
				Logger.getLogger(EsecuzioneVerificheController.class.getName()).log(Level.SEVERE, null, e);
			}
		});
		dialogLayout.setHeading(new Label("Consegna verifica"));
		
		Label l = new Label("Sei sicuro di voler consegnare la verifica?");
		l.setWrapText(true);
		
		dialogLayout.setBody(l);
		dialogLayout.setActions(button1, new Label("                               "), button2, new Label("             "));
		dialog.show();
	}

	@FXML
	public void pulsanteIndietro() {
		if(!verifica_finita) {
			// Salvataggio risposte scelte
			salvaRisposteScelte();
			
			elencoButtons.get(nr_domanda_corrente).setDisable(true);
			
			if(controlloAlmenoUnaRispostaSelezionata())
				elencoIcone.get(nr_domanda_corrente).setGlyphName("CHECK");
			else
				elencoIcone.get(nr_domanda_corrente).setGlyphName("EYE");
			
			// Decremento della variabile che indica la domanda corrente
			nr_domanda_corrente--;
			labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
			
			elencoButtons.get(nr_domanda_corrente).setDisable(false);
					
			// Controllo pulsanti se ci si trova a inizio test
			if(nr_domanda_corrente + 1 == 1)
				indietroButton.setDisable(true);
			if(AvantiButton.isDisable())
				AvantiButton.setDisable(false);
			
			// Visualizzazione della domanda precedente
			scorrimentoDomande();
		}
		else {
			fineTempo();
		}
	}
	
	@FXML 
	public void pulsanteAvanti() {
		if(!verifica_finita) {
			// Salvataggio risposte scelte
			salvaRisposteScelte();
			
			elencoButtons.get(nr_domanda_corrente).setDisable(true);
			
			if(controlloAlmenoUnaRispostaSelezionata())
				elencoIcone.get(nr_domanda_corrente).setGlyphName("CHECK");
			else
				elencoIcone.get(nr_domanda_corrente).setGlyphName("EYE");
			
			// Incremento della variabile che indica la domanda corrente
			nr_domanda_corrente++;
			labelNrDomandaCorrente.setText((nr_domanda_corrente + 1) + "");
			
			elencoButtons.get(nr_domanda_corrente).setDisable(false);
			
			// Controllo pulsanti se ci si trova a fine test
			if(nr_domanda_corrente + 1 == nr_domande)
				AvantiButton.setDisable(true);
			if(indietroButton.isDisable())
				indietroButton.setDisable(false);
			
			// Visualizzazione della domanda successiva
			scorrimentoDomande();
		}
		else {
			fineTempo();
		}
	}
	
	/** METODO PER CREARE IL CONTO ALLA ROVESCIA DEL TEMPO DELLA VERIFICA
	 */
	private void countDownTimer() {
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				ddHour = dFormat.format(hour);
				tempo.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
				
				if(second == -1) {
					second = 59;
					minute--;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					ddHour = dFormat.format(hour);
					tempo.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
				}
				if(minute == -1) {
					minute = 59;
					hour--;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					ddHour = dFormat.format(hour);
					tempo.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
				}
				
				if(minute == 0 && second == 0) {
					timer.stop();
					verifica_finita = true;
				}
			}
		});
	}
	
	private void fineTempo() {
		// Messaggio prima di tornare alla schermata principale
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button1 = new JFXButton("Continua");
		button1.setStyle("-fx-background-color:red; -fx-text-fill: white;"); 
		button1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
			
			try {
				Parent ritornoPaginaAdmin;
				ritornoPaginaAdmin = (StackPane) FXMLLoader.load(getClass().getResource("/studenti_verifiche/StudentiVerifiche.fxml"));

				Scene admin = new Scene(ritornoPaginaAdmin);

				Stage stage = (Stage) rootPane.getScene().getWindow();

				stage.setScene(admin);
				stage.setTitle("Menù principale docenti");
				stage.setResizable(false);
			} catch (IOException ex) {
				Logger.getLogger(EsecuzioneVerificheController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		dialogLayout.setHeading(new Label("Consegna verifica"));
		
		Label l = new Label("Tempo finito. La verifica è stata consegnata. Per vedere i risultati, entrare nella sezione voti.");
		l.setWrapText(true);
		
		dialogLayout.setBody(l);
		dialogLayout.setActions(button1, new Label("                               "));
		dialog.show();
	}
	
	/** SALVATAGGIO DELLE RISPOSTE SELEZIONATE DALLO STUDENTE NEL DATABASE
	 */
	private void salvaRisposteScelte() {
		for (int i = 0; i < risposte.size(); i++) {
			// Controllo se opzione selezionata o meno
			if (elenco_checkbox.get(i).isSelected()) 
			{
				risposte.get(i).setIs_selezionata("SI");
			} else
				risposte.get(i).setIs_selezionata("NO");	
		}		
		// Salvataggio della scelta nel database
		IoOperations.aggiornaRisposteStudente(risposte);
	}

	public static Verifica getVerifica() {
		return verifica;
	}
	
	/** METODO PER LA CORREZIONE DELLA VERIFICA E DETERMINAZIONE DEL PUNTEGGIO OTTENUTO E DEL VOTO
	 */
	private void correggiVerifica() {
		pt_ottenuto = 0;
		voto = 0;
		// Somma punteggi ottenuti in corrispondenza alla correttezza delle risposte scelte
		for(int i = 0; i < domande_verifica.size(); i++) {
			// ottenimento risposte
			risposte = IoOperations.getRisposteEsecuzioneVerifica(domande_verifica.get(i).getId_domanda());
			
			for(int j = 0; j < risposte.size(); j++) {
				if(risposte.get(j).getIs_selezionata().equals("SI"))
					pt_ottenuto += risposte.get(j).getPunteggio_risposta();
			}
		}
		// Calcolo voto
		voto = round(pt_ottenuto / verifica.getPunteggio_totale() * 10);
	}
	
	private static double round (double value) {
	    int scale = (int) Math.pow(10, 1);
	    return (double) Math.round(value * scale) / scale;
	}
	
	public static int getIdstudente() {
		return id_studente;
	}
	
}
