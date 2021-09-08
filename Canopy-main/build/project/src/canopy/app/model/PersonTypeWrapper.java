package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "persontypes")
public class PersonTypeWrapper {

    private List<PersonType> persontypes;

    /**
     *
     * @return
     */
    @XmlElement(name = "persontype")    
    public List<PersonType> getPersonTypes() {
        return persontypes;
    }

    /**
     *
     * @param types
     */
    public void setPersonTypes(List<PersonType> types) {
        this.persontypes = types;
    }
}