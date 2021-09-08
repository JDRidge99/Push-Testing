package canopy.app.util;

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
public class LicenceIntegerXmlAdapter extends XmlAdapter<String,Integer> { // Adapter for licence int
 
        private final PBEStringEncryptor encryptorOne = new StandardPBEStringEncryptor();
 
        /**
         * Constructor. Reads the password from a key file on disk
         */
        public LicenceIntegerXmlAdapter() {
    	Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
                        String key = prefs.get("AdminKey", "AdminKey");
                        encryptorOne.setPassword(key); 
        }
 
        /**
         * Encrypts the value to be placed back in XML
     * @return 
         */
        @Override
        public String marshal(Integer plainint) throws Exception {
        	String plaintext = plainint.toString();        	
        	plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorOne);            
            return plaintext;
        }
 
        /**
         * Decrypts the string value
     * @return 
         */
        @Override
        public Integer unmarshal(String cyphertext) throws Exception {
                // Perform decryption operations as needed and store the new values
                if (PropertyValueEncryptionUtils.isEncryptedValue(cyphertext)) {
                	cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorOne);}                
                Integer integer = Integer.valueOf(cyphertext);
                return integer;
        }
 
}