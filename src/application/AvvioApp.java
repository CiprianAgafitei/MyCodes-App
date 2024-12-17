package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AvvioApp extends Application {
    
	private final static String IMAGE_LOCATION = "/resources/icona2.jpg";
	
    @Override
    public void start(Stage stage) throws Exception {       
    	Image icona = new Image(IMAGE_LOCATION);
    	Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
    	        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(getClass().getResource("/application/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("MyCodes T.R.M.");
        stage.getIcons().add(icona);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}