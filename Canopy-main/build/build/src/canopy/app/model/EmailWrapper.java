package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "emails")
public class EmailWrapper {

    private List<String> emails;

    /**
     *
     * @return
     */
    @XmlElement(name = "email")
    public List<String> getEmails() {
        return emails;
    }

    /**
     *
     * @param emails
     */
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}