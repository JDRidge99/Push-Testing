package canopy.app.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Andy
 */
public class AppointmentType {
	
	SimpleStringProperty currency;	
	SimpleStringProperty name;
	SimpleIntegerProperty id;
	SimpleIntegerProperty duration;
	SimpleDoubleProperty price;
	SimpleStringProperty description;
	
    /**
     *
     */
    public AppointmentType() {
		this.price = new SimpleDoubleProperty();
		this.currency = new SimpleStringProperty();
		this.duration = new SimpleIntegerProperty();
		this.name = new SimpleStringProperty();
		this.id = new SimpleIntegerProperty();
		this.description = new SimpleStringProperty();
	}
	
    /**
     *
     * @param price
     * @param name
     * @param id
     */
    public AppointmentType(Double price, String name, int id, int duration, String currency) {
		this.price = new SimpleDoubleProperty(price);
		this.currency = new SimpleStringProperty(currency);
		this.duration = new SimpleIntegerProperty(duration);
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleIntegerProperty(id);
		this.description = new SimpleStringProperty();
	}
	
    /**
     *
     * @return
     */
    public Double getPrice() {
		return price.get();		
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
     * @param num
     */
    public void setPrice(Double num) {
		this.price.set(num);		
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
    public String getDescription() {
		return description.get();		
	}	
	
    /**
     *
     * @param desc
     */
    public void setDescription(String desc) {
		this.description.set(desc);		
	}
	
    /**
     *
     * @return
     */
    @XmlElement(name = "id")
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
    
    public String getCurrency() {
		return currency.get();		
	}	
	
    /**
     *
     * @param curr
     */
    public void setCurrency(String curr) {
		this.currency.set(curr);		
	}
    
    @XmlElement(name = "duration")
    public Integer getDuration() {
    	return duration.get();
    }

    /**
     *
     * @param duration
     */
    public void setDuration(Integer duration) {
    	this.duration.set(duration);
    }
	

}
