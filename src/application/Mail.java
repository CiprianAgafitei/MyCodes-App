package application;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Mail {

    // Sender mail
    static String FROM;
    // Sender name
    static String FROMNAME;
    // Receiver mail
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
    // Mail subject
    static String SUBJECT;
    // Mail Text
    static String BODY;
    
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
