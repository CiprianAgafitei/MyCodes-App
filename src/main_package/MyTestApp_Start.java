package main_package;
	
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class MyTestApp_Start extends Application {
	
	@Override
	public void start(Stage stage) throws IOException, URISyntaxException {		
		Image icona = new Image(getClass().getResource("/images/Icona.png").toURI().toString());
    	Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
    	
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("MyTest App");
        stage.getIcons().add(icona);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
