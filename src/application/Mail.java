package application;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Mail {

	//email mittente
    static String FROM;
    //Nome mittente
    static String FROMNAME;
    //email destinatario
    static String TO;//
    
    // Replace smtp_username with your Amazon SES SMTP username.
    static final String SMTP_USERNAME = "smtp_username";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "smtp_password";
    
    // The name of the Configuration Set to use for this message.
    static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the Stati Uniti occidentali (Oregon) region.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";
    // The port you will connect to on SES SMTP endpoint. 
    static final int PORT = 587;
    //Oggetto della mail
    static String SUBJECT;
    //Corpo della mail
    static String BODY;
    
    /** VERIFICA DELLA VALIDITA' DIN UN INDIRIZZO EMAIL
     * 
     * @param email è l'email da verificare
     * @return true se lindirizzo email è corretto; false se non corretto.
     */
    public static boolean isValidEmailAddress(String email) {
      boolean result = true;
      try {
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
      } catch (AddressException ex) {
        result = false;
      }
      return result;
    }
	
}
