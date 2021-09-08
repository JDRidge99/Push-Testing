package canopy.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "patients")
public class NoteListWrapperv1 {

    private List<Note_v1> notes;

    /**
     *
     * @return
     */
    @XmlElement(name = "note")
    public List<Note_v1> getNotes() {
    	 return notes;   
    }

    /**
     *
     * @param notes
     */
    public void setNotes(List<Note_v1> notes) {
        this.notes = notes;
    }
}