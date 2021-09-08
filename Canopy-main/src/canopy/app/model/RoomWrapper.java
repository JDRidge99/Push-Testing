package canopy.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "rooms")
public class RoomWrapper {

    private List<Room> rooms;

    /**
     *
     * @return
     */
    @XmlElement(name = "room")    
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     *
     * @param rooms
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}