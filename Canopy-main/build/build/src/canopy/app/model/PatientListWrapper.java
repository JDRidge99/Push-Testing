package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "patients")
public class PatientListWrapper {

    private List<Patient> patients;

    /**
     *
     * @return
     */
    @XmlElement(name = "patient")
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     *
     * @param patients
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}