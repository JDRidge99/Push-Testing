package canopy.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import canopy.app.MainApp;
 
/**
 *
 * @author Andy
 */
public class LicenceLocalDateXmlAdapter extends XmlAdapter<String,LocalDate> {
 
        private final PBEStringEncryptor encryptorOne = new StandardPBEStringEncryptor();
 
 
        /**
         * Encrypts the value to be placed back in XML
         */
        String string = "January 2, 2010";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH);
        /**
         * Constructor. Reads the password from a key file on disk
         */
        public LicenceLocalDateXmlAdapter() {
            Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
            String key = prefs.get("AdminKey", "AdminKey");
            encryptorOne.setPassword(key);
        }
        
        @Override
        public String marshal(LocalDate date) throws Exception {
        	
        	String plaintext = date.format(formatter);
        	plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorOne);             
            return plaintext;
            
        }
 
        /**
         * Decrypts the string value
     * @return 
         */
        @Override
        public LocalDate unmarshal(String cyphertext) throws Exception {                
                if (PropertyValueEncryptionUtils.isEncryptedValue(cyphertext)) {
                	cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorOne);              	
                	}
                LocalDate date = LocalDate.parse(cyphertext, formatter);
                return date;
        }
}