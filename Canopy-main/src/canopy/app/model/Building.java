package canopy.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
public class Building {
	
	 private final StringProperty address;
	    private final StringProperty city;
	    private final StringProperty postcode;
	    private final StringProperty name;

    /**
     *
     */
    public Building() {
	        this(null, null, null,null);
	    }

    /**
     *
     * @param address
     * @param city
     * @param postcode
     * @param name
     */
    public Building(String address, String city, String postcode, String name) {
	        this.address = new SimpleStringProperty(address);
	        this.city = new SimpleStringProperty(city);
	        this.postcode = new SimpleStringProperty(postcode);
	        this.name = new SimpleStringProperty(name);
	    }

    /**
     *
     * @return
     */
    public String getAddress() {
	        return address.get();
	    }

    /**
     *
     * @param firstName
     */
    public void setAddress(String firstName) {
	        this.address.set(firstName);
	    }

    /**
     *
     * @return
     */
    public StringProperty addressProperty() {
	        return address;
	    }

    /**
     *
     * @return
     */
    public String getCity() {
	        return city.get();
	    }

    /**
     *
     * @param lastName
     */
    public void setCity(String lastName) {
	        this.city.set(lastName);
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
    public String getPostcode() {
	        return postcode.get();
	    }

    /**
     *
     * @param postcode
     */
    public void setPostcode(String postcode) {
	        this.postcode.set(postcode);
	    }

    /**
     *
     * @return
     */
    public StringProperty postCodeProperty() {
	        return this.postcode;
	    }
	    
    /**
     *
     * @return
     */
    public String getName() {
	        return name.get();
	    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
	        this.name.set(name);
	    }

    /**
     *
     * @return
     */
    public StringProperty nameProperty() {
	        return name;
	    }
	    
	}
