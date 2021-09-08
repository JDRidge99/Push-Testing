package canopy.app.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(name = "room")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Room {	
	    private final StringProperty roomname;	 
	    private final IntegerProperty building;
	    private final IntegerProperty id;

    /**
     *
     */
    public Room() {
	        this(null,0,0);
	    }	    

    /**
     *
     * @param roomname
     * @param building
     * @param id
     */
    public Room(String roomname, int building, int id) {
	        this.roomname = new SimpleStringProperty(roomname);
	        this.building = new SimpleIntegerProperty(building);
	        this.id = new SimpleIntegerProperty(id);
	    }
	    
    /**
     *
     * @return
     */
    @XmlElement(name = "roomName")
	    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	    public String getRoomName() {
	        return roomname.get();
	    }    

    /**
     *
     * @param roomname
     */
    public void setRoomName(String roomname) {
	        this.roomname.set(roomname);
	    }

    /**
     *
     * @return
     */
    public StringProperty roomNameProperty() {
	        return roomname;
	    }
	    
    /**
     *
     * @return
     */
    public IntegerProperty buildingProperty() {
	    	return building;
	    }	  
	    
    /**
     *
     * @return
     */
    @XmlElement(name = "buildingNumber")
	    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	    public Integer getBuilding() {	    	
	    	return building.get();
	    }

    /**
     *
     * @param building
     */
    public void setBuilding(Integer building) {
	    	this.building.set(building);
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
