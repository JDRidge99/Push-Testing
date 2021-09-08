package canopy.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedLocalDateXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import canopy.app.util.LocalTimeAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
public class Appointment_v1 {
	private final ObjectProperty<LocalDate> date;
	private final ObjectProperty<LocalTime> time;
	private final IntegerProperty type;
	// 0 = Patient Appointment
	// 1 = Meeting
	// 2 = Gap
	private Room room;
	private final StringProperty description;
	private IntegerProperty durationmins;
	private IntegerProperty staffmember;
	private IntegerProperty patient;
	private IntegerProperty appointmentType;
	private IntegerProperty appointmentID;
	private BooleanProperty paid;
	private BooleanProperty reminderSent;

	private SimpleStringProperty currency;
	private SimpleDoubleProperty price;

	/**
	 *
	 */
	public Appointment_v1() { // Default Constructor
		this.patient = new SimpleIntegerProperty();
		this.staffmember = new SimpleIntegerProperty();
		this.date = new SimpleObjectProperty<LocalDate>();
		this.time = new SimpleObjectProperty<LocalTime>();
		this.room = null;
		this.type = new SimpleIntegerProperty();
		this.description = new SimpleStringProperty();
		this.durationmins = new SimpleIntegerProperty();
		this.appointmentType = new SimpleIntegerProperty();
		this.appointmentID = new SimpleIntegerProperty();
		this.paid = new SimpleBooleanProperty();
		this.price = new SimpleDoubleProperty();
		this.currency = new SimpleStringProperty();
		this.reminderSent = new SimpleBooleanProperty(false);
	}

	/**
	 *
	 * @param patient
	 * @param staffmember
	 * @param date
	 * @param time
	 * @param room
	 * @param type
	 * @param description
	 * @param duration
	 * @param appointmentType
	 */
	public Appointment_v1(Integer patient, Integer staffmember, LocalDate date, LocalTime time, Room room, int type, String description, int duration, int appointmentType) {
		this.patient = new SimpleIntegerProperty(patient);
		this.staffmember = new SimpleIntegerProperty(staffmember);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.time = new SimpleObjectProperty<LocalTime>(time);
		this.room = room;
		this.type = new SimpleIntegerProperty(type);
		this.description = new SimpleStringProperty(description);
		this.durationmins = new SimpleIntegerProperty(duration);
		this.appointmentType = new SimpleIntegerProperty(appointmentType);
		this.paid = new SimpleBooleanProperty(false);
		this.appointmentID = new SimpleIntegerProperty();
		this.price = new SimpleDoubleProperty();
		this.currency = new SimpleStringProperty();
		this.reminderSent = new SimpleBooleanProperty(false);
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedLocalDateXmlAdapter.class)
	public LocalDate getDate() {
		return date.get();
	}

	/**
	 *
	 * @param date
	 */
	public void setDate(LocalDate date) {
		this.date.set(date);
	}

	/**
	 *
	 * @return
	 */
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(LocalTimeAdapter.class)
	public LocalTime getTime() {
		return time.get();
	}

	/**
	 *
	 * @param time
	 */
	public void setTime(LocalTime time) {
		this.time.set(time);
	}

	/**
	 *
	 * @return
	 */
	public ObjectProperty<LocalTime> timeProperty() {
		return time;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getPatient() {
		return patient.get();
	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Integer patient) {
		this.patient.set(patient);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty patient() {
		return patient;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getStaffMember() {
		return staffmember.get();
	}

	/**
	 *
	 * @param account
	 */
	public void setStaffMember(Integer account) {
//		System.out.println("Setting Account: " + String.valueOf(account));
		this.staffmember.set(account);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty staffMember() {
		return staffmember;
	}

	/**
	 *
	 * @return
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 *
	 * @param room
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getType() {
		return type.get();
	}

	/**
	 *
	 * @param type
	 */
	public void setType(Integer type) {
		this.type.set(type);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty typeProperty() {
		return type;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getDescription() {
		return description.get();
	}

	/**
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description.set(description);
	}

	/**
	 *
	 * @return
	 */
	public StringProperty descriptionProperty() {
		return description;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getDuration() {
		return durationmins.get();
	}

	/**
	 *
	 * @param durationmins
	 */
	public void setDuration(Integer durationmins) {
		this.durationmins.set(durationmins);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty durationProperty() {
		return durationmins;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getAppType() {
		return appointmentType.get();
	}

	/**
	 *
	 * @param appointmentType
	 */
	public void setAppType(Integer appointmentType) {
		this.appointmentType.set(appointmentType);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty apptypeProperty() {
		return appointmentType;
	}

	/**
	 *
	 * @return
	 */
	public Boolean getPaid() {
		return paid.get();
	}

	/**
	 *
	 * @param paid
	 */
	public void setPaid(Boolean paid) {
		this.paid.set(paid);
	}

	/**
	 *
	 * @return
	 */
	public Boolean getReminded() {
		return reminderSent.get();
	}

	/**
	 *
	 * @param paid
	 */
	public void setReminded(Boolean reminded) {
		this.reminderSent.set(reminded);
	}

	/**
	 *
	 * @return
	 */
	public BooleanProperty paidProperty() {
		return paid;
	}

	/**
	 *
	 * @return
	 */
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getID() {
		return appointmentID.get();
	}

	/**
	 *
	 * @param patient
	 */
	public void setID(Integer patient) {
		this.appointmentID.set(patient);
	}

	/**
	 *
	 * @return
	 */
	public Double getPrice() {
		return price.get();
	}

	/**
	 *
	 * @param num
	 */
	public void setPrice(Double num) {
		this.price.set(num);
	}

	public String getCurrency() {
		return currency.get();
	}

	/**
	 *
	 * @param curr
	 */
	public void setCurrency(String curr) {
		this.currency.set(curr);
	}

}
