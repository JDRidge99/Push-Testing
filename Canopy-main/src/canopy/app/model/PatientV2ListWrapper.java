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
public class PatientV2ListWrapper {

    private List<Patient_v2> patients;

    /**
     *
     * @return
     */
    @XmlElement(name = "patient")
    public List<Patient_v2> getPatients() {
        return patients;
    }

    /**
     *
     * @param patients
     */
    public void setPatients(List<Patient_v2> patients) {
        this.patients = patients;
    }
}