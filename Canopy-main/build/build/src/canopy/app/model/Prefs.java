package canopy.app.model;

import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
@XmlRootElement
public class Prefs {
	private final StringProperty filepath;
	
	private final StringProperty keyOne;
	private final StringProperty keyTwo;
	private final StringProperty keyThree;
	private final StringProperty keyFour;
	private final StringProperty keyFive;
	
	private final BooleanProperty accountssaved;
	private final BooleanProperty appointmentssaved;
	private final BooleanProperty appointmenttypessaved;
	private final BooleanProperty patientssaved;
	private final BooleanProperty roomssaved;
	private final BooleanProperty buildingssaved;
	private final BooleanProperty mailinglistssaved;
	private final BooleanProperty peoplesaved;
	private final BooleanProperty persontypessaved;
	
	private final IntegerProperty appointmentid;	
	private final StringProperty deletedappointments;
	
	private final IntegerProperty personid;	
	private final StringProperty deletedpeople;
	
	private final IntegerProperty roomid;	
	private final StringProperty deletedrooms;
	
	private final IntegerProperty mailid;	
	private final StringProperty deletedmailinglists;
	
	private final IntegerProperty noteid;	
	private final StringProperty deletednotes;	

	private final IntegerProperty appointmenttypeid;
	private final StringProperty deletedappointmenttypes;
	
	private final BooleanProperty keysscrambled; // Have the keys been scrambled?
	
	private final StringProperty automatedsendername;
	private final StringProperty automatedsenderemail;
	private final BooleanProperty autosendstaff;
	private final BooleanProperty autosendpatients;

	
    /**
     *
     */
    public Prefs() { // Default constructor
		this.filepath = new SimpleStringProperty();
		
		this.keyOne = new SimpleStringProperty();
		this.keyTwo = new SimpleStringProperty();
		this.keyThree = new SimpleStringProperty();
		this.keyFour = new SimpleStringProperty();
		this.keyFive = new SimpleStringProperty();
		
		this.accountssaved = new SimpleBooleanProperty();
		this.appointmentssaved = new SimpleBooleanProperty();
		this.appointmenttypessaved = new SimpleBooleanProperty();
		this.patientssaved = new SimpleBooleanProperty();
		this.roomssaved = new SimpleBooleanProperty();
		this.buildingssaved = new SimpleBooleanProperty();
		this.mailinglistssaved = new SimpleBooleanProperty();
		this.peoplesaved = new SimpleBooleanProperty();
		this.persontypessaved = new SimpleBooleanProperty();
		
		this.appointmentid = new SimpleIntegerProperty(0);
		this.deletedappointments = new SimpleStringProperty("");
		
		this.personid = new SimpleIntegerProperty(0);
		this.deletedpeople = new SimpleStringProperty("");
		
		this.roomid = new SimpleIntegerProperty(0);
		this.deletedrooms = new SimpleStringProperty("");
		
		this.mailid = new SimpleIntegerProperty(0);
		this.deletedmailinglists = new SimpleStringProperty("");
		
		this.noteid = new SimpleIntegerProperty(0);
		this.deletednotes = new SimpleStringProperty("");
		
		this.appointmenttypeid = new SimpleIntegerProperty(0);
		this.deletedappointmenttypes = new SimpleStringProperty("");
		
		this.automatedsendername = new SimpleStringProperty("");
		this.automatedsenderemail = new SimpleStringProperty("");
		this.autosendstaff = new SimpleBooleanProperty();
		this.autosendpatients = new SimpleBooleanProperty();
		
		this.keysscrambled = new SimpleBooleanProperty(false);

		
		
	}

    /**
     *
     * @return Returns FilePath ( DEPRICATED )
     */
    public String getFilePath() {
		return filepath.get();
	}

    /**
     *
     * @param string ( DEPRICATED )
     */
    public void setFilePath(String string) {
		this.filepath.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty firstNameProperty() {
		return filepath;
	}
	
	// KEYS	

    /**
     *
     * @return
     */
	public String getKeyOne() {
		return keyOne.get();
	}

    /**
     *
     * @param string
     */
    public void setKeyOne(String string) {
		this.keyOne.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty keyOneProperty() {
		return keyOne;
	}
	
    /**
     *
     * @return
     */
    public String getKeyTwo() {
		return keyTwo.get();
	}

    /**
     *
     * @param string
     */
    public void setKeyTwo(String string) {
		this.keyTwo.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty keyTwoProperty() {
		return keyTwo;
	}
	
    /**
     *
     * @return
     */
    public String getKeyThree() {
		return keyThree.get();
	}

    /**
     *
     * @param string
     */
    public void setKeyThree(String string) {
		this.keyThree.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty keyThreeProperty() {
		return keyThree;
	}
	
    /**
     *
     * @return
     */
    public String getKeyFour() {
		return keyFour.get();
	}

    /**
     *
     * @param string
     */
    public void setKeyFour(String string) {
		this.keyFour.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty keyFourProperty() {
		return keyFour;
	}
	
    /**
     *
     * @return
     */
    public String getKeyFive() {
		return keyFive.get();
	}

    /**
     *
     * @param string
     */
    public void setKeyFive(String string) {
		this.keyFive.set(string);
	}

    /**
     *
     * @return
     */
    public StringProperty keyFiveProperty() {
		return keyFive;
	}
	
    /**
     *
     * @return
     */
    public Boolean getAccountsSaved() {
		return accountssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setAccountsSaved(Boolean bool) {
		System.out.println("setting accounts saved");
		this.accountssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty accountsSavedProperty() {
		return accountssaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getAppointmentsSaved() {
		return appointmentssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setAppointmentsSaved(Boolean bool) {
		System.out.println("setting appointments saved");
		this.appointmentssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty appointmentsSavedProperty() {
		return appointmentssaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getAppointmentTypesSaved() {
		return appointmenttypessaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setAppointmentTypesSaved(Boolean bool) {
		this.appointmenttypessaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty appointmenttypesSavedProperty() {
		return appointmenttypessaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getPatientsSaved() {
		return patientssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setPatientsSaved(Boolean bool) {
		System.out.println("setting patients saved");
		this.patientssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty patientsSavedProperty() {
		return patientssaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getPeopleSaved() {
		return peoplesaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setPeopleSaved(Boolean bool) {
		this.peoplesaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty peopleSavedProperty() {
		return peoplesaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getTypesSaved() {
		return persontypessaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setTypesSaved(Boolean bool) {
		this.persontypessaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty typesSavedProperty() {
		return persontypessaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getRoomsSaved() {
		return roomssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setRoomsSaved(Boolean bool) {
		System.out.println("setting rooms saved");
		this.roomssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty roomsSavedProperty() {
		return roomssaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getBuildingsSaved() {
		return buildingssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setBuildingsSaved(Boolean bool) {
		System.out.println("setting buildings saved");
		this.buildingssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty buildingsSavedProperty() {
		return buildingssaved;
	}
	
    /**
     *
     * @return
     */
    public Boolean getMailingListsSaved() {
		return mailinglistssaved.get();
	}

    /**
     *
     * @param bool
     */
    public void setMailingListsSaved(Boolean bool) {
		System.out.println("setting mailinglists saved");
		this.mailinglistssaved.set(bool);
	}

    /**
     *
     * @return
     */
    public BooleanProperty mailingListsSavedProperty() {
		return mailinglistssaved;
	}
	
	// Appointments

    /**
     *
     * @return
     */
	
	public Integer getAppointmentID() {
		return appointmentid.get();
	}

    /**
     *
     * @param integer
     */
    public void setAppointmentID(Integer integer) {
		this.appointmentid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedAppointments() {
		return deletedappointments.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedAppointments(String string) {
		this.deletedappointments.set(string);
	}
	
	// People

    /**
     *
     * @return
     */
	
	public Integer getPersonID() {
		return personid.get();
	}

    /**
     *
     * @param integer
     */
    public void setPersonID(Integer integer) {
		this.personid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedPeople() {
		return deletedpeople.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedPeople(String string) {
		this.deletedpeople.set(string);
	}
	
	// Rooms

    /**
     *
     * @return
     */
	
	public Integer getRoomID() {
		return roomid.get();
	}

    /**
     *
     * @param integer
     */
    public void setRoomID(Integer integer) {
		this.roomid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedRooms() {
		return deletedrooms.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedRooms(String string) {
		this.deletedrooms.set(string);
	}
	
	// Mailing Lists

    /**
     *
     * @return
     */
	
	public Integer getMailID() {
		return mailid.get();
	}

    /**
     *
     * @param integer
     */
    public void setMailID(Integer integer) {
		this.mailid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedMail() {
		return deletedmailinglists.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedMail(String string) {
		this.deletedmailinglists.set(string);
	}
	
	// Notes

    /**
     *
     * @return
     */
	
	public Integer getNoteID() {
		return noteid.get();
	}

    /**
     *
     * @param integer
     */
    public void setNoteID(Integer integer) {
		this.noteid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedNotes() {
		return deletednotes.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedNotes(String string) {
		this.deletednotes.set(string);
	}
	
    /**
     *
     * @return
     */
    public Integer getAppTypeID() {
		return appointmenttypeid.get();
	}

    /**
     *
     * @param integer
     */
    public void setAppTypeID(Integer integer) {
		this.appointmenttypeid.set(integer);
	}
	
    /**
     *
     * @return
     */
    public String getDeletedAppointmentTypes() {
		return deletedappointmenttypes.get();
	}

    /**
     *
     * @param string
     */
    public void setDeletedAppointmentTypes(String string) {
		this.deletedappointmenttypes.set(string);
	}
    
    /**
    *
    * @return Returns the sender name for automated emails and events
    */
	public String getAutomatedSenderName() {
		return automatedsendername.get();
	}

   /**
    *
    * @param string
    */
   public void setAutomatedSenderName(String string) {
		this.automatedsendername.set(string);
	}

   /**
    *
    * @return
    */
   public StringProperty automatedSenderNameProperty() {
		return automatedsendername;
	}
	
   /**
   *
   * @return Returns the sender name for automated emails and events
   */
	public String getAutomatedSenderEmail() {
		return automatedsenderemail.get();
	}

  /**
   *
   * @param string
   */
  public void setAutomatedSenderEmail(String string) {
		this.automatedsenderemail.set(string);
	}

  /**
   *
   * @return
   */
  public StringProperty automatedSenderEmailProperty() {
		return automatedsenderemail;
	}
  
  /**
  *
  * @return Returns true if staff are to be automatically emailed appointment invites
  */
 public Boolean getAutoEmailStaff() {
		return autosendstaff.get();
	}

 /**
  *
  * @param bool
  */
 public void setAutoEmailStaff(Boolean bool) {
		this.autosendstaff.set(bool);
	}
	
 /**
 *
 * @return Returns true if patients are to be automatically emailed appointment invites
 */
public Boolean getAutoEmailPatients() {
		return autosendpatients.get();
	}

/**
 *
 * @param bool
 */
public void setAutoEmailPatients(Boolean bool) {
		this.autosendpatients.set(bool);
	}

/**
* Sets if the keys have been scrambled yet.
* @param bool
*/
public void setKeysScrambled(Boolean bool) {
		this.keysscrambled.set(bool);
	}
	
/**
*
* @return Returns true if the keys have been scrambled.
*/
public Boolean getKeysScrambled() {
		return keysscrambled.get();
	}

	
	

	
	
	
	
}

	