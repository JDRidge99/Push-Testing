package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "mailinglists")
public class MailingListWrapper {

    private List<MailingList> mailingLists;

    /**
     *
     * @return
     */
    @XmlElement(name = "mailinglist")    
    public List<MailingList> getMailingList() {
        return mailingLists;
    }

    /**
     *
     * @param mailingLists
     */
    public void setMailingList(List<MailingList> mailingLists) {
        this.mailingLists = mailingLists;
    }
}