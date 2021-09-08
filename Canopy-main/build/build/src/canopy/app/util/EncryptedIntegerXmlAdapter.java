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
public class EncryptedIntegerXmlAdapter extends XmlAdapter<String, Integer> { // Adapts encrypted integers

	private final PBEStringEncryptor encryptorOne = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorTwo = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorThree = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorFour = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorFive = new StandardPBEStringEncryptor();

	/**
	 * Constructor. Reads the password from a key file on disk
	 */
	public EncryptedIntegerXmlAdapter() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		boolean keysupdated = prefs.getBoolean("keysUpdated", false);
		

		if (true) { // Replace with <keysupdated> if needs be.
//			System.out.println("Keys Updated: Pre update one: " + prefs.get("KeyOne", ""));
			String key = EncryptionUtils.fromKey(prefs.get("KeyOne", ""));
//			System.out.println("Keys Updated: Post update one: " + key);
			encryptorOne.setPassword(key);
			key = EncryptionUtils.fromKey(prefs.get("KeyTwo", ""));
			encryptorTwo.setPassword(key);
			key = EncryptionUtils.fromKey(prefs.get("KeyThree", ""));
			encryptorThree.setPassword(key);
			key = EncryptionUtils.fromKey(prefs.get("KeyFour", ""));
			encryptorFour.setPassword(key);
			key = EncryptionUtils.fromKey(prefs.get("KeyFive", ""));
			encryptorFive.setPassword(key);
		} else {
			String key = prefs.get("KeyOne", "");
			encryptorOne.setPassword(key);
			key = prefs.get("KeyTwo", "");
			encryptorTwo.setPassword(key);
			key = prefs.get("KeyThree", "");
			encryptorThree.setPassword(key);
			key = prefs.get("KeyFour", "");
			encryptorFour.setPassword(key);
			key = prefs.get("KeyFive", "");
			encryptorFive.setPassword(key);
		}

	}

	/**
	 * Encrypts the value to be placed back in XML
	 * 
	 * @return
	 */
	@Override
	public String marshal(Integer plainint) throws Exception {
		String plaintext = plainint.toString();
		plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorThree);
		plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorOne);
		plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorFive);
		plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorFour);
		plaintext = PropertyValueEncryptionUtils.encrypt(plaintext, encryptorTwo);
		return plaintext;
	}

	/**
	 * Decrypts the string value
	 * 
	 * @return
	 */
	@Override
	public Integer unmarshal(String cyphertext) throws Exception {
		// Perform decryption operations as needed and store the new values
		if (PropertyValueEncryptionUtils.isEncryptedValue(cyphertext)) {
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorTwo);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorFour);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorFive);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorOne);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorThree);
		}
		Integer integer = Integer.valueOf(cyphertext);
		return integer;
	}

}