package canopy.app.model;

import javax.xml.bind.annotation.XmlElement;
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
public class PersonType {	
	    private final StringProperty typename;
	    private final IntegerProperty id;

    /**
     *
     */
    public PersonType() {
	        this.typename = new SimpleStringProperty();
	        this.id = new SimpleIntegerProperty();
	    }	    

    /**
     *
     * @param name
     * @param Id
     */
    public PersonType(String name, Integer Id) {
	        this.typename = new SimpleStringProperty(name);
	        this.id = new SimpleIntegerProperty(Id);
	    }
	    
    /**
     *
     * @return
     */
    @XmlElement(name = "typeName")
	    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	    public String getTypeName() {
	        return typename.get();
	    }    

    /**
     *
     * @param listname
     */
    public void setTypeName(String listname) {
	        this.typename.set(listname);
	    }

    /**
     *
     * @return
     */
    public StringProperty typeNameProperty() {
	        return typename;
	    }
	    
    /**
     *
     * @return
     */
    @XmlElement(name = "id")
	    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	    public Integer getId() {
	    	return id.get();
	    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
	    	this.id.set(id);
	    }
	    
    /**
     *
     * @return
     */
    public IntegerProperty idProperty() {
	    	return id;
	    }	
	    
	}
