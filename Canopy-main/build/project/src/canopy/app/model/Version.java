package canopy.app.model;

import javax.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Andy
 */
@XmlRootElement
public class Version {
	private final SimpleIntegerProperty version;
	
    /**
     *
     */
    public Version(){
		version = new SimpleIntegerProperty(0);
	}
	
    /**
     *
     * @return
     */
    public Integer getVersion() {
		return version.get();
	}

    /**
     *
     * @param number
     */
    public void setVersion(Integer number) {
		this.version.set(number);
	}


}
