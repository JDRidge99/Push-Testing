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
public class EncryptedLocalDateXmlAdapter extends XmlAdapter<String, LocalDate> { // Adapts encrypted local dates

	private final PBEStringEncryptor encryptorOne = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorTwo = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorThree = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorFour = new StandardPBEStringEncryptor();
	private final PBEStringEncryptor encryptorFive = new StandardPBEStringEncryptor();

	/**
	 * Encrypts the value to be placed back in XML
	 */
	String string = "January 2, 2010";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH);

	/**
	 * Constructor. Reads the password from a key file on disk
	 */
	public EncryptedLocalDateXmlAdapter() {
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

	@Override
	public String marshal(LocalDate date) throws Exception {

		String plaintext = date.format(formatter);
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
	public LocalDate unmarshal(String cyphertext) throws Exception {
		if (PropertyValueEncryptionUtils.isEncryptedValue(cyphertext)) {
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorTwo);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorFour);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorFive);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorOne);
			cyphertext = PropertyValueEncryptionUtils.decrypt(cyphertext, encryptorThree);
		}
		LocalDate date = LocalDate.parse(cyphertext, formatter);
		return date;
	}

}