package canopy.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "appointments")
public class AppointmentV1Wrapper {

    private List<Appointment_v1> appointments;

    /**
     *
     * @return
     */
    @XmlElement(name = "appointment")
    public List<Appointment_v1> getAppointments() {
        return appointments;
    }

    /**
     *
     * @param appointments
     */
    public void setAppointments(List<Appointment_v1> appointments) {
    	System.out.println("setting appointments");
        this.appointments = appointments;
    	System.out.println("set appointments");
    }
}