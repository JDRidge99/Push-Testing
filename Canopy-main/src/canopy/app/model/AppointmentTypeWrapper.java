package canopy.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "appointmenttypes")
public class AppointmentTypeWrapper {

    private List<AppointmentType> appointmenttypes;

    /**
     *
     * @return
     */
    @XmlElement(name = "appointmenttype")
    public List<AppointmentType> getAppointments() {
        return appointmenttypes;
    }

    /**
     *
     * @param appointmenttypes
     */
    public void setAppointments(List<AppointmentType> appointmenttypes) {
        this.appointmenttypes = appointmenttypes;
    }
}