module JavaApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.media;
	requires javafx.swing;
	requires javafx.web;
	requires javafx.swt;
	requires com.jfoenix;
	requires java.logging;
	requires java.sql;
	requires java.mail;
	requires fontawesomefx;
	requires commons.codec;
	
	opens classi_tabelle to javafx.base;
	opens login to javafx.graphics, javafx.fxml;
	opens admin to javafx.fxml, javafx.base;
	opens admin_classi to javafx.graphics, javafx.fxml;
	opens admin_materie_docenti to javafx.graphics, javafx.fxml, javafx.base;
	opens admin_utenti to javafx.graphics, javafx.fxml;
	opens docenti to javafx.graphics, javafx.fxml;
	opens docenti_nuova_domanda to javafx.graphics, javafx.fxml;
	opens docenti_verifiche to javafx.graphics, javafx.fxml;
	opens docenti_voti to javafx.graphics, javafx.fxml, javafx.base;
	opens studenti to javafx.graphics, javafx.fxml;
	opens studenti_verifiche to javafx.graphics, javafx.fxml;
	opens studenti_voti to javafx.graphics, javafx.fxml;
	opens main_package to javafx.graphics, javafx.fxml;
}