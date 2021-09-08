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
public class Licence {
	
    private final ObjectProperty<LocalDate> endDate;
	private final IntegerProperty daysuntilexpiry;	
	private final StringProperty ID;
	private final IntegerProperty type;
	// 0 = small
	// 1 = medium
	// 2 = large
	// 3 = unlimited
	
    /**
     *
     */
    public Licence() { // Default constructor
		this.endDate= new SimpleObjectProperty<LocalDate>();
		this.daysuntilexpiry = new SimpleIntegerProperty(99999);
		this.ID = new SimpleStringProperty();
		this.type = new SimpleIntegerProperty(3);

	}
	
    /**
     *
     * @param enddate
     * @param ID
     */
    public Licence(LocalDate enddate, String ID, Integer type) { // Default constructor
		this.endDate = new SimpleObjectProperty<LocalDate>(enddate); 		
		this.daysuntilexpiry = new SimpleIntegerProperty((int) ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.get()));
		this.ID = new SimpleStringProperty(ID);
		this.type = new SimpleIntegerProperty(type);
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
    	System.out.println("From Date: " + LocalDate.now().toString());
    	System.out.println("To Date: " + this.endDate.get().toString());
    	System.out.println("Days Between 1: " + String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.get())));
    	
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

   @XmlJavaTypeAdapter(value = LicenceIntegerXmlAdapter.class)        
   public Integer getType() {
       return this.type.get();
   }

   /**
    *
    * @param number
    */
   public void setType(Integer number) {
       this.type.set(number);
   }
}

	