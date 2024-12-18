package docenti_voti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import classi_tabelle.Domanda;
import classi_tabelle.IoOperations;
import classi_tabelle.Risposta;
import classi_tabelle.Utente;
import classi_tabelle.Verifica;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VotiVerificaClasseController implements Initializable {

	@FXML private StackPane rootPane;
	@FXML private Label labelVerifica;
	@FXML private LineChart<String, Number> grafico;
    @FXML private JFXButton indietroButton;
    @FXML private CategoryAxis studenti;
    @FXML private NumberAxis voti;
    @FXML private JFXButton scaricaButton;
	
    //Codice ID per individuare il docente che ha effettuato l'accesso
  	public static int ID_DOCENTE;
    ArrayList<Utente> studenti_verifica;
    private int ID_VERIFICA;
    
    private Verifica verificaSelezionata;
    ArrayList<Verifica> verifiche_studenti;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ID_DOCENTE = RisultatiVerificheController.getID_DOCENTE();
		ID_VERIFICA = RisultatiVerificheController.getID_VERIFICA();
		
		// Id della verifica selezionata
		verificaSelezionata = IoOperations.getVerificaFromId(ID_VERIFICA);
		
		// Ottenimento classe a cui è stata assegnata la verifica
		String classe = IoOperations.getClasseFromIDOfVerifica(ID_VERIFICA).getClasse();
		labelVerifica.setText("Risultati verifica di " + verificaSelezionata.getArgomento());
		labelVerifica.setAlignment(Pos.CENTER);

		// Ottenimento studenti della classe della verifica
		studenti_verifica = IoOperations.getStudentiDaClasse(verificaSelezionata.getId_classe());

		// Ottenimento delle verifiche che hanno id_classe e argomento uguali a quelli della verifica selezionata
		verifiche_studenti = IoOperations.getElencoVerificheSpecifiche(verificaSelezionata.getId_classe(), verificaSelezionata.getArgomento());
		
		// Impostati i valori dei nomi e dei voti sul grafico		
		XYChart.Series<String, Number> andamento = new XYChart.Series<>();
		andamento.setName("Classe " + classe);

		for (int i = 0; i < verifiche_studenti.size(); i++) {
			// Controllo se lo studente ha eseguito la verifica: se non così allora non viene mostrato il suo risultato
			if(verifiche_studenti.get(i).getIs_eseguita().equals("SI"))
				andamento.getData().add(new XYChart.Data<String, Number>(IoOperations.getUtenteDatoID(verifiche_studenti.get(i).getId_utente()).getNome() + " " + IoOperations.getUtenteDatoID(verifiche_studenti.get(i).getId_utente()).getCognome(), verifiche_studenti.get(i).getVoto()));
		}
		grafico.getData().add(andamento);
	}

	@FXML
	public void esciButtonPremuto() {
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
			Logger.getLogger(VotiVerificaClasseController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@FXML 
	public void scaricaVerifica() {
		// Selezione della cartella in cui inserire le verifiche
		FileChooser fileChooser = new FileChooser();

		// Ottenimento del "file" che verrà convertito in cartella
		File directory = fileChooser.showSaveDialog(null);
		
		// Creazione cartella
		directory.mkdir();
		
		for(int i = 0; i < verifiche_studenti.size(); i ++) {
			// Creazione nuovo file con Cognome_Nome dello studente
			File file = new File(directory.getPath() + "/" + IoOperations.getUtenteDatoID(verifiche_studenti.get(i).getId_utente()).getCognome() + "_" + IoOperations.getUtenteDatoID(verifiche_studenti.get(i).getId_utente()).getNome());
			
			if (file != null) {
				salvataggioSuFile(file, verifiche_studenti.get(i).getId_verifica());
			}
		}
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("Ok");
		button.setStyle("-fx-background-color:#1976D2; -fx-border-color:#1976D2; -fx-text-fill: white;");
		JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			dialog.close();
		});
		dialogLayout.setHeading(new Label("Salvataggio su file"));
		dialogLayout.setBody(new Label("Salvataggio su file avvenuto con successo!"));
		dialogLayout.setActions(button);
		dialog.show();
	}
	
	/** METODOD PER SALVARE UNA VERIFICA DI UNA STUDENTE SU UN FILE
	 */
	private void salvataggioSuFile(File file, int id_verifica) {
		// Ottenimento della verifica dato un id di una verifica
		Verifica v = IoOperations.getVerificaFromId(id_verifica);
		// Ottenimento elenco domande della verifica
		ArrayList<Domanda> domande = IoOperations.getDomandeVerifica(id_verifica);
		
		char[]opzioni = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		try {
			FileWriter fw = new FileWriter(file);

			fw.write("\t\t" + v.getArgomento() + " \n");
			fw.write("-------------------------------------------------------\n");
			fw.append("NOME: " + IoOperations.getUtenteDatoID(v.getId_utente()).getNome() + "\t" + "COGNOME: " + IoOperations.getUtenteDatoID(v.getId_utente()).getCognome() + "\n\n\n");
			
			for (int i = 0; i < domande.size(); i++) {
				// Scrittura su file della domanda
				fw.append((i + 1) + ". " + domande.get(i).getTesto() + "\n");
				
				// Ottenimento elenco risposte di una domanda
				ArrayList<Risposta> risposte = IoOperations.getRisposteEsecuzioneVerifica(domande.get(i).getId_domanda());
				for(int j = 0; j < risposte.size(); j++) {
					/* Scrittura su file delle risposte con controllo e inserimento punteggio solo delle 
					 * risposte selezionate dall'utente e indicato anche se ciascuna risposta è corretta */
					if(risposte.get(j).getIs_selezionata().equals("SI"))
						fw.append(opzioni[j] + ") " + risposte.get(j).getTesto() + "\t\t (Corretta:" + risposte.get(j).getCorretta() + "  PT ott.:" + risposte.get(j).getPunteggio_risposta() + ")\n");
					else
						fw.append(opzioni[j] + ") " + risposte.get(j).getTesto() + "\t\t (Corretta:" + risposte.get(j).getCorretta() + ")\n");
				}
				fw.append("\n\n");
			}
			fw.append("PT Ottenuto: " + v.getPunteggio_ottenuto() + " - PT Totale: " + v.getPunteggio_totale() + " - Voto: " + v.getVoto());
			
			fw.flush();
			fw.close();
		} catch (IOException e) {}
	}
}
