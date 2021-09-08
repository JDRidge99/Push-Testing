package canopy.app.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedLocalDateXmlAdapter;
import canopy.app.util.LicenceIntegerXmlAdapter;
import canopy.app.util.LicenceLocalDateXmlAdapter;
import canopy.app.util.LocalDateAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
@XmlRootElement
public class Licence_v1 {
	
    private final ObjectProperty<LocalDate> endDate;
	private final IntegerProperty daysuntilexpiry;	
	private final StringProperty ID;
	
    /**
     *
     */
    public Licence_v1() { // Default constructor
		this.endDate= new SimpleObjectProperty<LocalDate>();
		this.daysuntilexpiry = new SimpleIntegerProperty(99999);
		this.ID = new SimpleStringProperty();
	}
	
    /**
     *
     * @param enddate
     * @param ID
     */
    public Licence_v1(LocalDate enddate, String ID) { // Default constructor
		this.endDate = new SimpleObjectProperty<LocalDate>(enddate); 		
		this.daysuntilexpiry = new SimpleIntegerProperty((int) ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.get()));
		this.ID = new SimpleStringProperty(ID);
	}
	
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = LicenceLocalDateXmlAdapter.class)        
    public LocalDate getEndDate() {
        return this.endDate.get();
    }

    /**
     *
     * @param date
     */
    public void setEndDate(LocalDate date) {
        this.endDate.set(date);
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = LicenceIntegerXmlAdapter.class)        
    public Integer getDaysUntilExpiry() {
        return this.daysuntilexpiry.get();
    }

    /**
     *
     * @param number
     */
    public void setDaysUntilExpiry(Integer number) {
        this.daysuntilexpiry.set(number);
    }
	
    /**
     *
     */
    public void calculateDaysUntilExpiry() {
//    	System.out.println("From Date: " + LocalDate.now().toString());
//    	System.out.println("To Date: " + this.endDate.get().toString());
//    	System.out.println("Days Between 1: " + String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.get())));
    	
    	this.daysuntilexpiry.set((int) ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.get()));
    }
    
    /**
     *
     * @return
     */
    public String getID() {
		return ID.get();
	}

    /**
     *
     * @param string
     */
    public void setID(String string) {
		this.ID.set(string);
	}
}

	