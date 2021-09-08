package canopy.app.util;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Andy
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

	@Override
	public String marshal(LocalTime v) throws Exception {
		return v.toString();		
	}

	@Override
	public LocalTime unmarshal(String v) throws Exception {
		return LocalTime.parse(v);
	}
	
}