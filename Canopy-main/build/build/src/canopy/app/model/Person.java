package canopy.app.model;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
public class Person {
	private final StringProperty firstname;
	private final StringProperty lastname;
	private final StringProperty address;
	private final StringProperty postalcode;
	private final StringProperty city;
	private final StringProperty email;
	private final StringProperty number;
	private final StringProperty description;
	private final IntegerProperty persontype;
	private final IntegerProperty personid;

    /**
     *
     */
    public Person() { // Default constructor
		this.firstname = new SimpleStringProperty();
		this.lastname = new SimpleStringProperty();
		this.address = new SimpleStringProperty();
		this.city = new SimpleStringProperty();
		this.postalcode = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.number = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.persontype = new SimpleIntegerProperty();
		this.personid = new SimpleIntegerProperty();
	}

    /**
     *
     * @param firstname
     * @param lastname
     * @param address
     * @param city
     * @param postcode
     * @param email
     * @param number
     * @param persontype
     */
    public Person(String firstname, String lastname, String address, String city, String postcode,
			 String email, String number, Integer persontype) {
		this.firstname = new SimpleStringProperty(firstname);
		this.lastname = new SimpleStringProperty(lastname);
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.postalcode = new SimpleStringProperty(postcode);
		this.email = new SimpleStringProperty(email);
		this.number = new SimpleStringProperty(number);
		this.description = new SimpleStringProperty();
		this.persontype = new SimpleIntegerProperty(persontype);
		this.personid = new SimpleIntegerProperty();

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
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getEmail() {
		return email.get();
	}

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
		this.email.set(email);
	}

    /**
     *
     * @return
     */
    public StringProperty emailProperty() {
		return email;
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getNumber() {
		return number.get();
	}

    /**
     *
     * @param numbers
     */
    public void setNumber(String numbers) {
		this.number.set(numbers);
	}

    /**
     *
     * @return
     */
    public StringProperty numberProperty() {
		return number;
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getDescription() {
		return description.get();
	}

    /**
     *
     * @param string
     */
    public void setDescription(String string) {
		this.description.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty descriptionProperty() {
		return description;
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getPersonType() {
		return persontype.get();
	}

    /**
     *
     * @param number
     */
    public void setPersonType(Integer number) {
		this.persontype.set(number);
	}

    /**
     *
     * @return
     */
    public IntegerProperty personTypeProperty() {
		return this.persontype;
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
    public Integer getId() {
    	return personid.get();
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
    	this.personid.set(id);
    }
    
    /**
     *
     * @return
     */
    public IntegerProperty idProperty() {
    	return personid;
    }	

}
