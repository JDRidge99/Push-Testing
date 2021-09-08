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
public class NoteListWrapper {

    private List<Note> notes;

    /**
     *
     * @return
     */
    @XmlElement(name = "note")
    public List<Note> getNotes() {
    	 return notes;   
    }

    /**
     *
     * @param notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}