package canopy.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedLocalDateXmlAdapter;
import canopy.app.util.EncryptedLocalTimeXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
public class Note_v1 {	
	    private SimpleIntegerProperty patient;
	    private SimpleIntegerProperty staffcode;
	    private final ObjectProperty<LocalDate> date;
	    private final ObjectProperty<LocalTime> time;
	    private final StringProperty text;
	    private final IntegerProperty id;
	    private SimpleStringProperty file;
	    private SimpleStringProperty filename;
	    
	    // Default Constructor 

    /**
     *
     */
	    
	    public Note_v1() {
	        this.patient = new SimpleIntegerProperty();
	        this.staffcode = new SimpleIntegerProperty();
	        this.date = new SimpleObjectProperty<LocalDate>();
	        this.time = new SimpleObjectProperty<LocalTime>();
	        this.text = new SimpleStringProperty();
	        this.id = new SimpleIntegerProperty();	
	        this.file = new SimpleStringProperty();
	        this.filename = new SimpleStringProperty();
	    }

    /**
     *
     * @param patient
     * @param staffcode
     * @param date
     * @param time
     * @param text
     */
    public Note_v1(Patient patient, int staffcode, LocalDate date, LocalTime time, String text) {
	        this.patient = new SimpleIntegerProperty(patient.getPatientNumber());
	        this.staffcode = new SimpleIntegerProperty(staffcode);
	        this.date = new SimpleObjectProperty<LocalDate>(date);
	        this.time = new SimpleObjectProperty<LocalTime>(time);
	        this.text = new SimpleStringProperty(text);	 
	        this.id = new SimpleIntegerProperty();	    	
	        this.file = new SimpleStringProperty();
	        this.filename = new SimpleStringProperty();
	    }
	    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(EncryptedLocalDateXmlAdapter.class)
	    public LocalDate getDate() {
	        return date.get();
	    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
	        this.date.set(date);
	    }

    /**
     *
     * @return
     */
    public ObjectProperty<LocalDate> dateProperty() {
	        return date;
	    }
	    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(EncryptedLocalTimeXmlAdapter.class)
	    public LocalTime getTime() {
	        return time.get();
	    }

    /**
     *
     * @param time
     */
    public void setTime(LocalTime time) {
	        this.time.set(time);
	    }

    /**
     *
     * @return
     */
    public ObjectProperty<LocalTime> timeProperty() {
	        return time;
	    }
	    
	    //@XmlJavaTypeAdapter(EncryptedIntegerXmlAdapter.class)	    

    /**
     *
     * @return
     */
	    public Integer getPatient() {
	    	return this.patient.get();
	    }
	    
    /**
     *
     * @param patient
     */
    public void setPatient(Integer patient) {
	    	this.patient.set(patient);
	    }	    

	    //@XmlJavaTypeAdapter(EncryptedIntegerXmlAdapter.class)	

    /**
     *
     * @return
     */
	    public Integer getStaffCode() {
	    	return this.staffcode.get();
	    }    
	    
    /**
     *
     * @param code
     */
    public void setStaffCode(Integer code) {
	    	staffcode.set(code);
	    }
	    
	  //@XmlJavaTypeAdapter(EncryptedStringXmlAdapter.class)		    

    /**
     *
     * @return
     */
	    public String getText() {
	        return text.get();
	    }

    /**
     *
     * @param description
     */
    public void setText(String description) {
	        this.text.set(description);
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
	    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(EncryptedStringXmlAdapter.class)
	    public String getFile() {
	        return file.get();
	    }

    /**
     *
     * @param file
     */
    public void setFile(String file) {
	        this.file.set(file);
	    }
	    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(EncryptedStringXmlAdapter.class)
	    public String getFileName() {
	        return filename.get();
	    }

    /**
     *
     * @param filename
     */
    public void setFileName(String filename) {
	        this.filename.set(filename);
	    }


}
