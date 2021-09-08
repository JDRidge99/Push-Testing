package canopy.app.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "notetypes")
public class NoteTypeWrapper {

    private List<NoteType> notetypes;

    /**
     *
     * @return
     */
    @XmlElement(name = "notetype")    
    public List<NoteType> getNoteTypes() {
        return notetypes;
    }

    /**
     *
     * @param types
     */
    public void setNoteTypes(List<NoteType> types) {
        this.notetypes = types;
    }
}