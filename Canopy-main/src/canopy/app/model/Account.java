package canopy.app.model;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import canopy.app.util.LocalTimeAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Andy
 */
public class Account { // Account data for each staff member

    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty title;
	private final StringProperty firstname;
	private final StringProperty lastname;
	private final StringProperty role;
	private final StringProperty email;
	private final StringProperty phonenumber;
    private final IntegerProperty staffnumber;
    private final IntegerProperty permission;
    //PERMISSION LEVELS:
    //0 - Master Admin: Everything, password unblocked
    //1 - Admin: Everything except new staff & edit staff, password blocked
    //2 - Self Timetabling Staff: can see everything except some staff data, and can only add new appointments
    //3 - Observing Staff: can see but not touch
    //4 - Non interacting Staff: no log in details, simply a placeholder
    //5 - Non clinical Master Staff: can observe and modify everything non patient related
    //6 - Non clinical Staff: can observer everything not patient related
    private final ObjectProperty<LocalTime> starttime;
    private final ObjectProperty<LocalTime> endtime;
    private final StringProperty holiday;
    private final StringProperty avaliabledays;
    private final StringProperty Key;

    /**
     *
     */
    public Account() { // Default account constructor
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.firstname = new SimpleStringProperty();
        this.lastname = new SimpleStringProperty();
        this.holiday = new SimpleStringProperty("");
        this.avaliabledays = new SimpleStringProperty("Y;Y;Y;Y;Y;N;N");
        this.role = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.staffnumber = new SimpleIntegerProperty();
        this.permission = new SimpleIntegerProperty();
        this.starttime = new SimpleObjectProperty<LocalTime>();
        this.endtime = new SimpleObjectProperty<LocalTime>();
        this.phonenumber = new SimpleStringProperty();
        this.Key = new SimpleStringProperty();
    }
    
    /**
     *
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @param role
     * @param title
     * @param code
     * @param starttime
     * @param endtime
     * @param email
     * @param phonenumber
     * @param permissionlevel
     */
    public Account(String username, String password, String firstname, String lastname, String role, String title, int code, LocalTime starttime, LocalTime endtime, String email,String phonenumber, Integer permissionlevel) {
        // Populated Constructor
    	this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.role = new SimpleStringProperty(role);
        this.title = new SimpleStringProperty(title);
        this.email = new SimpleStringProperty(email);
        this.staffnumber = new SimpleIntegerProperty(code); // TODO FIX
        this.permission = new SimpleIntegerProperty(permissionlevel);
        this.starttime = new SimpleObjectProperty<LocalTime>(starttime);
        this.endtime = new SimpleObjectProperty<LocalTime>(endtime);
        this.phonenumber = new SimpleStringProperty(phonenumber);
        this.holiday = new SimpleStringProperty("");
        this.avaliabledays = new SimpleStringProperty("Y;Y;Y;Y;Y;N;N");
        this.Key = new SimpleStringProperty();
        
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)   
    public String getUserName() {
        return username.get();
    }

    /**
     *
     * @param firstName
     */
    public void setUserName(String firstName) {
        this.username.set(firstName);
    }

    /**
     *
     * @return
     */
    public StringProperty userNameProperty() {
        return username;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)   
    public String getPassword() {
        return password.get();
    }

    /**
     *
     * @param lastName
     */
    public void setPassword(String lastName) {
        this.password.set(lastName);
    }

    /**
     *
     * @return
     */
    public StringProperty passwordProperty() {
        return password;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)       
    public String getFirstName() {
        return firstname.get();
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstname.set(firstName);
    }
       
    /**
     *
     * @return
     */
    public StringProperty firstNameProperty() {
        return firstname;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)       
    public String getTitle() {
        return title.get();
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)   
    public String getLastName() {
        return lastname.get();
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastname.set(lastName);
    }

    /**
     *
     * @return
     */
    public StringProperty lastNameProperty() {
        return lastname;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)       
    public String getEmail() {
        return email.get();
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     *
     * @return
     */
    public StringProperty emailNameProperty() {
        return email;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)       
    public String getPhoneNumber() {
        return phonenumber.get();
    }

    /**
     *
     * @param phonenumber
     */
    public void setPhoneNumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }

    /**
     *
     * @return
     */
    public StringProperty phoneNumberProperty() {
        return phonenumber;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)    
    public String getRole() {
        return role.get();
    }

    /**
     *
     * @param lastName
     */
    public void setRole(String lastName) {
        this.role.set(lastName);
    }

    /**
     *
     * @return
     */
    public StringProperty roleProperty() {
        return role;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)        
    public Integer getStaffNumber() {
        return staffnumber.get();
    }

    /**
     *
     * @param number
     */
    public void setStaffNumber(Integer number) {
        this.staffnumber.set(number);
    }

    /**
     *
     * @return
     */
    public IntegerProperty staffNumberProperty() {
        return this.staffnumber;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)        
    public Integer getPermission() {
        return permission.get();
    }

    /**
     *
     * @param number
     */
    public void setPermission(Integer number) {
        this.permission.set(number);
    }

    /**
     *
     * @return
     */
    public IntegerProperty permissionProperty() {
        return this.permission;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getStartTime() {
        return starttime.get();
    }

    /**
     *
     * @param time
     */
    public void setStartTime(LocalTime time) {
        this.starttime.set(time);
    }

    /**
     *
     * @return
     */
    public ObjectProperty<LocalTime> startTimeProperty() {
        return starttime;
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getEndTime() {
        return endtime.get();
    }

    /**
     *
     * @param time
     */
    public void setEndTime(LocalTime time) {
        this.endtime.set(time);
    }

    /**
     *
     * @return
     */
    public ObjectProperty<LocalTime> endTimeProperty() {
        return endtime;
    }
    
    /**
     *
     * @return
     */
    public long getMinutes() {
    	return getStartTime().until(getEndTime(), MINUTES);
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)    
    public String getHoliday() {
        return holiday.get();
    }

    /**
     *
     * @param holiday
     */
    public void setHoliday(String holiday) {
        this.holiday.set(holiday);
    }

    /**
     *
     * @return
     */
    public StringProperty holidayProperty() {
        return holiday;
    }
    
    /**
     *
     * @return
     */
    @XmlTransient    
    public ObservableList<LocalDate> getHolidayList(){
    	ObservableList<LocalDate> holidays = FXCollections.observableArrayList();
//		System.out.println("Get Holiday List: ");

    	
    	String[] holidayarray = this.holiday.get().split(";");
    	
    	for(String day:holidayarray) {
//    		System.out.println("Day string: " + day);

    		if( !"".equals(day.trim())) {
    		LocalDate date = LocalDate.parse(day);
//    		System.out.println("Date to String: " + date.toString());

    		holidays.add(date);}
    	}
    	
    	return holidays;
    }
    
    /**
     *
     * @param list
     */
    public void setHolidayList(ObservableList<LocalDate> list) {
//		System.out.println("Set Holiday List: ");

    	String holiday = "";
        holiday = list.stream().map((date) -> date.toString()).map((day) -> {
//            System.out.println("Date to string: " + day);
            return day;
        }).map((day) -> day + ";").reduce(holiday, String::concat);
//		System.out.println("Total Holiday String: " + holiday);

    	setHoliday(holiday);
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)    
    public String getDays() {
        return avaliabledays.get();
    }

    /**
     *
     * @param avaliabledays
     */
    public void setDays(String avaliabledays) {
        this.avaliabledays.set(avaliabledays);
    }

    /**
     *
     * @return
     */
    public StringProperty avaliableDaysProperty() {
        return avaliabledays;
    }
        
    /**
     *
     * @param day
     * @return
     */
    public boolean avaliableOnDay(int day) {
    	String[] days = this.avaliabledays.get().split(";");
//		System.out.println("Avaliable Days: " + this.avaliabledays.get());

    	
        return days[day].equals("Y");
    }
    
    /**
     *
     * @param day
     * @param avaliable
     */
    public void setAvaliableOnDay(int day, boolean avaliable) {
    	String[] days = this.avaliabledays.get().split(";");
    	String avaliabledays = "";
    	
    	if(avaliable) {
    		days[day] = "Y";
    	}
    	else {
    		days[day] = "N";
		}
    	
    	for(String s:days) {
    		avaliabledays += s + ";";
    	}
    	this.avaliabledays.set(avaliabledays);
    }
    
    /**
     *
     * @return
     */
    @XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)    
    public String getKey() {
        //System.out.println("UPDATE KEY: " + this.Key.get());
        return this.Key.get();
    }

    /**
     *
     * @param key
     */
    public void setKey(String key) {
        this.Key.set(key);
    }


}
