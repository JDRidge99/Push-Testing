package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "buildings")
public class BuildingWrapper {

    private List<Building> buildings;

    /**
     *
     * @return
     */
    @XmlElement(name = "building")
    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     *
     * @param buildings
     */
    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}