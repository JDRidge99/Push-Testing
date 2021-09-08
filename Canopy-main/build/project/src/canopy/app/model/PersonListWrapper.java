package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "people")
public class PersonListWrapper {

    private List<Person> people;

    /**
     *
     * @return
     */
    @XmlElement(name = "person")
    public List<Person> getPeople() {
        return people;
    }

    /**
     *
     * @param people
     */
    public void setPeople(List<Person> people) {
        this.people = people;
    }
}