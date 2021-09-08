package canopy.app.model;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedLocalDateXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Andy
 */
public class Patient_v1 {
	private final StringProperty firstname;
	private final StringProperty lastname;
	private final StringProperty carerfirstname;
	private final StringProperty carerlastname;
	private final StringProperty diagnosis;
	private final IntegerProperty patientnumber;
	private final StringProperty address;
	private final StringProperty postalcode;
	private final StringProperty city;
	private final StringProperty emails;
	private final StringProperty numbers;
	private final ObjectProperty<LocalDate> dob;

    /**
     *
     */
    public Patient_v1() { // Default constructor
		this.firstname = new SimpleStringProperty();
		this.lastname = new SimpleStringProperty();
		this.carerfirstname = new SimpleStringProperty();
		this.carerlastname = new SimpleStringProperty();
		this.diagnosis = new SimpleStringProperty();
		this.patientnumber = new SimpleIntegerProperty();
		this.address = new SimpleStringProperty();
		this.city = new SimpleStringProperty();
		this.postalcode = new SimpleStringProperty();
		this.dob = new SimpleObjectProperty<LocalDate>();
		this.emails = new SimpleStringProperty();
		this.numbers = new SimpleStringProperty();
	}

    /**
     *
     * @param firstname
     * @param lastname
     * @param patientnumber
     * @param address
     * @param city
     * @param postcode
     * @param dob
     * @param emails
     * @param numbers
     * @param staffmembercodes
     */
    public Patient_v1(String firstname, String lastname,String carerfirstname, String carerlastname, int patientnumber, String address, String city, String postcode,
			LocalDate dob, String emails, String numbers,
			ObservableList<Integer> staffmembercodes) {
		this.firstname = new SimpleStringProperty(firstname);
		this.lastname = new SimpleStringProperty(lastname);
		this.carerfirstname = new SimpleStringProperty(carerfirstname);
		this.carerlastname = new SimpleStringProperty(carerlastname);
		this.patientnumber = new SimpleIntegerProperty(patientnumber);
		this.diagnosis = new SimpleStringProperty();
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.postalcode = new SimpleStringProperty(postcode);
		this.dob = new SimpleObjectProperty<LocalDate>(dob);
		this.emails = new SimpleStringProperty(emails);
		this.numbers = new SimpleStringProperty(numbers);

	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getFirstName() {
		return firstname.get();
	}

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
		this.firstname.set(firstName);
	}

    /**
     *
     * @return
     */
    public StringProperty firstNameProperty() {
		return firstname;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getLastName() {
		return lastname.get();
	}

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
		this.lastname.set(lastName);
	}

    /**
     *
     * @return
     */
    public StringProperty lastNameProperty() {
		return lastname;
	}
    
    /**
    *
    * @return
    */
   @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getCarerFirstName() {
		return carerfirstname.get();
	}

   /**
    *
    * @param firstName
    */
   public void setCarerFirstName(String firstName) {
		this.carerfirstname.set(firstName);
	}

   /**
    *
    * @return
    */
   public StringProperty carerfirstNameProperty() {
		return carerfirstname;
	}

   /**
    *
    * @return
    */
   @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getCarerLastName() {
		return carerlastname.get();
	}

   /**
    *
    * @param lastName
    */
   public void setCarerLastName(String lastName) {
		this.carerlastname.set(lastName);
	}

   /**
    *
    * @return
    */
   public StringProperty carerlastNameProperty() {
		return carerlastname;
	}

    /**
     *
     * @return
     */
    public StringProperty diagnosisProperty() {
		return diagnosis;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getDiagnosis() {
		return diagnosis.get();
	}

    /**
     *
     * @param diagnosis
     */
    public void setDiagnosis(String diagnosis) {
		this.diagnosis.set(diagnosis);
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getPatientNumber() {
		return patientnumber.get();
	}

    /**
     *
     * @param number
     */
    public void setPatientNumber(Integer number) {
		this.patientnumber.set(number);
	}

    /**
     *
     * @return
     */
    public IntegerProperty patientNumberProperty() {
		return this.patientnumber;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getStreet() {
		return address.get();
	}

    /**
     *
     * @param street
     */
    public void setStreet(String street) {
		this.address.set(street);
	}

    /**
     *
     * @return
     */
    public StringProperty streetProperty() {
		return address;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getPostalCode() {
		return postalcode.get();
	}

    /**
     *
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
		this.postalcode.set(postalCode);
	}

    /**
     *
     * @return
     */
    public StringProperty postalCodeProperty() {
		return postalcode;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getCity() {
		return city.get();
	}

    /**
     *
     * @param city
     */
    public void setCity(String city) {
		this.city.set(city);
	}

    /**
     *
     * @return
     */
    public StringProperty cityProperty() {
		return city;
	}

    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedLocalDateXmlAdapter.class)
	public LocalDate getBirthday() {
		return dob.get();
	}

    /**
     *
     * @param birthday
     */
    public void setBirthday(LocalDate birthday) {
		this.dob.set(birthday);
	}

    /**
     *
     * @return
     */
    public ObjectProperty<LocalDate> birthdayProperty() {
		return dob;
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getEmails() {
		return emails.get();
	}

    /**
     *
     * @param emails
     */
    public void setEmails(String emails) {
		this.emails.set(emails);
	}

    /**
     *
     * @return
     */
    public StringProperty emailsProperty() {
		return emails;
	}
	
    /**
     *
     * @param email
     */
    public void addEmail(String email) {
		if(getEmails() != null) {
		this.emails.set(getEmails() + ";" + email);}
		else {this.emails.set(email);}
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getNumbers() {
		return numbers.get();
	}

    /**
     *
     * @param numbers
     */
    public void setNumbers(String numbers) {
		this.numbers.set(numbers);
	}

    /**
     *
     * @return
     */
    public StringProperty numbersProperty() {
		return numbers;
	}
	
    /**
     *
     * @param number
     */
    public void addNumber(String number) {
		if(getNumbers() != null) {
		this.numbers.set(getNumbers() + ";" + number);}
		else {this.numbers.set(number);}
	}

}
