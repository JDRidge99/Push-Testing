package canopy.app.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Day;
import canopy.app.model.Patient;
import canopy.app.model.Room;
import canopy.app.util.ComparatorBox;
import canopy.app.util.ComparatorBoxStaff;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.Glow;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author Andy
 */
public class appointmentDialogController { // controller for the appointment dialog

	@FXML
	private TextField hourfield;

	@FXML
	private TextField minutefield;

	@FXML
	private TextField durationfield;

	@FXML
	private DatePicker datepicker;

	@FXML
	private ComboBox<Account> staffpicker;

	@FXML
	private ComboBox<Room> roompicker;

	@FXML
	private ComboBox<AppointmentType> typepicker;

	@FXML
	private ComboBox<Patient> patientpicker;

	@FXML
	private TextField descriptionfield;

	@FXML
	private HBox dayprofile;

	@FXML
	private Label currenttimelabel;

	@FXML
	private Label prevappointment;

	@FXML
	private Label nextappointment;

	@FXML
	private Label starttime;

	@FXML
	private Label endtime;

	@FXML
	private CheckBox recurringcheckbox;

	@FXML
	private HBox recurringbox;

	@FXML
	private HBox recurringbox2;

	@FXML
	private HBox recurringbox3;

	@FXML
	private TextField recurringnumber;

	@FXML
	private ComboBox<String> periodpicker;

	@FXML
	private DatePicker todatepicker;

	@FXML
	private AnchorPane coverpane;

	@FXML
	private TextField pricefield;

	@FXML
	private TextField currencyfield;

	@FXML
	private VBox patientslist;

	@FXML
	private VBox stafflist;

	@FXML
	private VBox patientscontainer;

	@FXML
	private Label patientslabel;

	@FXML
	private Label pricelabel;

	@FXML
	private Label title;

	private boolean saved = false;
	private boolean disablelistener = false;

	ObservableList<String> periods = FXCollections.observableArrayList();
	ObservableList<Appointment> appointments = FXCollections.observableArrayList();
	ObservableList<day> days = FXCollections.observableArrayList();
	ObservableList<LocalDate> holidays = FXCollections.observableArrayList();

	boolean monday = true;
	boolean tuesday = true;
	boolean wednesday = true;
	boolean thursday = true;
	boolean friday = true;
	boolean saturday = true;
	boolean sunday = true;

	Color start = Color.web("#76C767");
	Color end = Color.web("#A80000");

	LocalDate startdate;
	LocalDate enddate;

	Day day;

	int duration = 30;
	int recurrnumber = 1;
	Appointment appointment;
	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainApp;
	boolean warningshown = false;

	boolean meeting = false;

	boolean edit = false;
	int editrange = 0;
	ObservableList<Appointment> recurringapps = FXCollections.observableArrayList();

	ObservableList<Patient> patients = FXCollections.observableArrayList();
	ObservableList<Account> staff = FXCollections.observableArrayList();

	/**
	 *
	 */
	public void checkAvaliable() { // Checks avaliability of selected staff member(s) <- not sure if modified for multiple staff members yet
		System.out.println("Checking Avaliablity");	
		for (Account account : staff) {
			
			System.out.println("Holidays Adding");			
			holidays.addAll(account.getHolidayList());
			for(LocalDate date:holidays) {
				System.out.println("Holiday: " + date.toString());			

			}

			if (!account.avaliableOnDay(0)) {
				monday = false;
			}
			if (!account.avaliableOnDay(1)) {
				tuesday = false;
			}
			if (!account.avaliableOnDay(2)) {
				wednesday = false;
			}
			if (!account.avaliableOnDay(3)) {
				thursday = false;
			}
			if (!account.avaliableOnDay(4)) {
				friday = false;
			}
			if (!account.avaliableOnDay(5)) {
				saturday = false;
			}
			if (!account.avaliableOnDay(6)) {
				sunday = false;
			}
		}
	}

	public void setedit(Appointment editapp) { // Sets the dialog to editing mode (as opposed to new appointment mode)
		appointment = editapp;
		edit = true;

		mainApp.getAppointmentTypes().stream().filter((type) -> (type.getId() == appointment.getAppType())).forEachOrdered((type) -> {
			typepicker.setValue(type);
		});

		for (Patient patient : mainApp.getPatients()) {
			if (appointment.getPatients().contains(patient.getPatientNumber())) {
				patients.add(patient);

				VBox patientpane = new VBox();
				patientpane.setAlignment(Pos.CENTER);
				HBox containerbox = new HBox();
				containerbox.prefWidthProperty().bind(patientpane.widthProperty());
				containerbox.setAlignment(Pos.CENTER);
				Glow glow = new Glow(0);

				patientpane.setEffect(glow);

				Insets insets = new Insets(0, 6, 0, 6);
				containerbox.setPadding(insets);
				HBox.setHgrow(containerbox, Priority.ALWAYS);
				Label patientlab = new Label();
				patientlab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

				patientpane.getStyleClass().add("appointment-delete");

				patientpane.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						patients.remove(patient);
						patientslist.getChildren().remove(patientpane);
					}

				});

				patientlab.setText(patient.getFirstName() + " " + patient.getLastName() + " | ID: " + String.valueOf(patient.getPatientNumber()));
				patientlab.getStyleClass().add("label-list");
				containerbox.getChildren().add(patientlab);

				patientpane.getChildren().add(containerbox);

				containerbox.prefWidthProperty().bind(patientpane.widthProperty());
				containerbox.maxHeightProperty().bind(patientpane.heightProperty());
				// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
				// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

				patientslist.getChildren().add(patientpane);
			}
		}

		for (Account account : mainApp.getAccounts()) {
			if (appointment.getStaffMembers().contains(account.getStaffNumber())) {
				staff.add(account);

				VBox staffpane = new VBox();
				staffpane.setAlignment(Pos.CENTER);
				HBox containerbox = new HBox();
				containerbox.prefWidthProperty().bind(staffpane.widthProperty());
				containerbox.setAlignment(Pos.CENTER);
				Glow glow = new Glow(0);

				staffpane.setEffect(glow);

				Insets insets = new Insets(0, 6, 0, 6);
				containerbox.setPadding(insets);
				HBox.setHgrow(containerbox, Priority.ALWAYS);
				Label stafflab = new Label();
				stafflab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

				staffpane.getStyleClass().add("appointment-delete");

				staffpane.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						staff.remove(account);
						stafflist.getChildren().remove(staffpane);
					}

				});

				stafflab.setText(account.getTitle() + " " + account.getFirstName() + " " + account.getLastName() + " | ID: " + String.valueOf(account.getStaffNumber()));
				stafflab.getStyleClass().add("label-list");
				containerbox.getChildren().add(stafflab);

				staffpane.getChildren().add(containerbox);

				containerbox.prefWidthProperty().bind(staffpane.widthProperty());
				containerbox.maxHeightProperty().bind(staffpane.heightProperty());
				// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
				// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

				stafflist.getChildren().add(staffpane);
			}
		}

		roompicker.setValue(appointment.getRoom());

		durationfield.setText(appointment.getDuration().toString());
		hourfield.setText(String.valueOf(appointment.getTime().getHour()));
		minutefield.setText(String.valueOf(appointment.getTime().getMinute()));
		datepicker.setValue(appointment.getDate());

		currencyfield.setText(appointment.getCurrency());
		pricefield.setText(appointment.getPrice().toString());

		descriptionfield.setText(appointment.getDescription());

		duration = appointment.getDuration();
		day.updateduration();

		recurringcheckbox.setVisible(false);
		recurringcheckbox.setManaged(false);

		for (Appointment checkapp : mainApp.getAppointments()) {
			if (checkapp.getPatient().equals(appointment.getPatient()) && checkapp.getTime().equals(appointment.getType()) && checkapp.getPrice().equals(appointment.getType())) {
				recurringapps.add(checkapp);
			}
		}

		if (recurringapps.size() > 1) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Similar Appointments Detected");
			alert.setHeaderText(String.valueOf(recurringapps.size()) + " appointments have been detected with the same patient, time and type");
			alert.setContentText("Would you like to edit just this appointment, all similar appointments after the selected, or all recorded similar appointments?");

			ButtonType buttonTypeOne = new ButtonType("This Appointment");

			ButtonType buttonTypeTwo = new ButtonType("Future Appointments");

			ButtonType buttonTypeThree = new ButtonType("All Appointments");

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				editrange = 0;
			}
			if (result.get() == buttonTypeTwo) {
				editrange = 1;
			}
			if (result.get() == buttonTypeThree) {
				editrange = 2;
			}
		}

	}

	/**
	 *
	 * @param mainapp
	 * @param date
	 * @param patient
	 */
	public void initialise(MainApp mainapp, LocalDate date, Patient patient) { // Initialises appointment creation

		this.mainApp = mainapp;
		onRecurringCheck();

		periods.add("Daily");
		periods.add("Weekly");
		periods.add("Fortnightly");
		periods.add("Monthly");
		periods.add("Quarterly");
		periods.add("Annually");

		periodpicker.setItems(periods);
		periodpicker.setValue("Weekly");

		roompicker.setItems(mainapp.getRooms());
		datepicker.setValue(date);
		typepicker.requestFocus();

		StringConverter<Room> roomconverter = new StringConverter<Room>() {

			@Override
			public Room fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Room object) {
				// TODO Auto-generated method stub
				return object.getRoomName();
			}
		};

		roompicker.converterProperty().set(roomconverter);

		typepicker.setItems(mainapp.getAppointmentTypes());

		StringConverter<AppointmentType> appointmentconverter = new StringConverter<AppointmentType>() {

			@Override
			public AppointmentType fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(AppointmentType object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		};

		typepicker.converterProperty().set(appointmentconverter);

		typepicker.valueProperty().addListener(new ChangeListener<AppointmentType>() {

			@Override
			public void changed(ObservableValue observable, AppointmentType oldValue, AppointmentType newValue) {
				if (newValue != null) {
					title.setText(newValue.getName().toUpperCase());
					if (newValue.getId() == 9999) {

						meeting = true;
						patientscontainer.setVisible(false);
						patientslabel.setVisible(false);
						pricefield.setVisible(false);
						currencyfield.setVisible(false);
						pricelabel.setVisible(false);

					} else {

						patientscontainer.setVisible(true);
						patientslabel.setVisible(true);
						pricefield.setVisible(true);
						currencyfield.setVisible(true);
						pricelabel.setVisible(true);

						meeting = false;
						durationfield.setText(String.valueOf(newValue.getDuration()));
						duration = newValue.getDuration();
						day.updateduration();

						currencyfield.setText(newValue.getCurrency());
						pricefield.setText(newValue.getPrice().toString());
					}
				}
			}
		});

		currencyfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"$€¢£¥".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}
			}

		});

		pricefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"0123456789.".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}
			}
		});

		patientpicker.setItems(mainapp.getPatients());
		patientpicker.setValue(patient);

		StringConverter<Patient> patientconverter = new StringConverter<Patient>() {

			@Override
			public Patient fromString(String string) {
				return patientFromString(string);
			}

			@Override
			public String toString(Patient object) {
				// TODO Auto-generated method stub
				try {
					return object.getFirstName() + " " + object.getLastName() + " | " + String.valueOf(object.getPatientNumber());
				} catch (Exception e) {
					return "";
				}

			}
		};

		patientpicker.converterProperty().set(patientconverter);
		new ComparatorBox<Patient>(patientpicker);

		staffpicker.setItems(mainapp.getListAccounts());

		mainapp.getListAccounts().forEach((account) -> {
			//System.out.println("List Account: " + account.getFirstName());
		});

		if (mainapp.getCurrentAccount().getPermission() != 0) {
			staffpicker.setValue(mainapp.getCurrentAccount());
			starttime.setText(mainapp.getCurrentAccount().getStartTime().toString());
			endtime.setText(mainapp.getCurrentAccount().getEndTime().toString());
		}

		if (staffpicker.getValue() != null) {
			checkAvaliable();
		}

		ObservableList<Account> stafftopop = FXCollections.observableArrayList(); // Staff to populate days with
		stafftopop.addAll(staff); // all added staff
		stafftopop.add(staffpicker.getSelectionModel().getSelectedItem()); // selected staff
		populatedays(stafftopop, null);

		StringConverter<Account> accountconverter = new StringConverter<Account>() {

			@Override
			public Account fromString(String string) {
				return accountFromString(string);
			}

			@Override
			public String toString(Account object) {
				// TODO Auto-generated method stub
				try {
					return object.getTitle() + "." + object.getFirstName() + " " + object.getLastName() + " | " + object.getRole();
				} catch (Exception e) {
					return "";
				}

			}
		};

		staffpicker.converterProperty().set(accountconverter);
		new ComparatorBoxStaff<Account>(staffpicker);

		populateAppointments(datepicker.getValue());
		if (appointments != null && appointments.size() > 0) {
			day.populateappointments(appointments);
		}
		appointmentDialogController controller = this;
		if (staffpicker.getValue() != null) {
			day = new Day(controller, datepicker.getValue(), staffpicker.getValue().getStartTime(), staffpicker.getValue().getEndTime(), dayprofile, appointments);
		} else {
			day = new Day(controller, datepicker.getValue(), LocalTime.of(9, 0), LocalTime.of(17, 0), dayprofile, appointments);
		}
		day.populateappointments(appointments);

		staffpicker.valueProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue ov, Account t, Account t1) {

				if (t1 != null) {					

					checkAvaliable();

					if (datepicker.getValue() != null) {

						if (holidays.contains(datepicker.getValue())) {

							Alert alert = new Alert(AlertType.WARNING);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.setAlwaysOnTop(true);

							DialogPane dialogPane = alert.getDialogPane();
							dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
							dialogPane.getStyleClass().add(".dialog-pane");

							alert.initOwner(dialogStage);
							alert.setTitle("Warning");
							alert.setHeaderText("Chosen Appointment Date is an 'Unavaliable' day for " + t1.getFirstName() + " " + t1.getLastName());
							alert.showAndWait();

						}
					}

					LocalTime staffstart;
					LocalTime staffend;

					if (staff.size() > 0) {
						staffstart = staff.get(0).getStartTime();
						staffend = staff.get(0).getEndTime();
						for (Account account : staff) {
							if (account.getStartTime().isAfter(staffstart)) {
								staffstart = account.getStartTime();
							}
							if (account.getEndTime().isBefore(staffend)) {
								staffend = account.getEndTime();
							}
						}
					} else {
						staffstart = staffpicker.getValue().getStartTime();
						staffend = staffpicker.getValue().getEndTime();
					}

					starttime.setText(staffstart.toString());
					endtime.setText(staffend.toString());
					day = new Day(controller, datepicker.getValue(), staffstart, staffend, dayprofile, appointments);
					if (day.getSize() > 675) {
						dialogStage.setWidth(200 + day.getSize());
					}
				}
				populateAppointments(datepicker.getValue());
				if (appointments != null && appointments.size() > 0) {
					day.populateappointments(appointments);
				}
			}
		});
		datepicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue ov, LocalDate t, LocalDate t1) {
				if (recurringcheckbox.selectedProperty().get() && !recurringnumber.getText().trim().equals("")) {
					setToDate(Integer.valueOf(recurringnumber.getText()));
				}
				if (recurringcheckbox.selectedProperty().get() && todatepicker.getValue() != null) {
					setToNumber(todatepicker.getValue());
				}
				if (datepicker.getValue() != null) {

					if (!checkDate(t1)) {

						Alert alert = new Alert(AlertType.WARNING);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.setAlwaysOnTop(true);

						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
						dialogPane.getStyleClass().add(".dialog-pane");

						alert.initOwner(dialogStage);
						alert.setTitle("Warning");
						alert.setHeaderText("Chosen Appointment Date is an 'Unavaliable' day for " + staffpicker.getValue().getFirstName() + " " + staffpicker.getValue().getLastName());
						alert.showAndWait();

					}

				}
				populateAppointments(datepicker.getValue());
				if (appointments != null && appointments.size() > 0) {
					day.populateappointments(appointments);
				}
				if (t == null || !t1.getMonth().equals(t.getMonth())) {
					ObservableList<Account> stafftopop = FXCollections.observableArrayList(); // Staff to populate days with
					stafftopop.addAll(staff); // all added staff
					stafftopop.add(staffpicker.getSelectionModel().getSelectedItem()); // selected staff
					populatedays(stafftopop, null);
				}
			}
		});

		todatepicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue ov, LocalDate t, LocalDate t1) {
				//System.out.println("To date changed");
				if (datepicker.getValue() == null) {
					datepicker.setValue(LocalDate.now());
				}
				if (t1 != null) {
					if (t1.isBefore(datepicker.getValue())) {
						//System.out.println("To date before datepicker date");

						todatepicker.setValue(LocalDate.now().plusDays(1));

						Alert alert = new Alert(AlertType.ERROR);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.setAlwaysOnTop(true);

						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
						dialogPane.getStyleClass().add(".dialog-pane");

						alert.initOwner(dialogStage);
						alert.setTitle("Error");
						alert.setHeaderText("Chosen 'to' date is before Chosen starting date");
						alert.showAndWait();

					} else {
						if (!disablelistener) {
							checkdates();
							disablelistener = true;
							todatepicker.setValue(t1);
							disablelistener = false;
							setToNumber(t1);
						}

					}
				}
			}
		});

		periodpicker.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				if (todatepicker.getValue() != null) {
					setToNumber(todatepicker.getValue());
				}
			}
		});

		patientpicker.valueProperty().addListener(new ChangeListener<Patient>() {
			@Override
			public void changed(ObservableValue ov, Patient t, Patient t1) {
				populateAppointments(datepicker.getValue());
				if (appointments != null && appointments.size() > 0) {
					day.populateappointments(appointments);
				}
			}
		});
		roompicker.valueProperty().addListener(new ChangeListener<Room>() {
			@Override
			public void changed(ObservableValue ov, Room t, Room t1) {
				populateAppointments(datepicker.getValue());
				if (appointments != null && appointments.size() > 0) {
					day.populateappointments(appointments);
				}
			}
		});

		hourfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {

				if (!hourfield.getSelectedText().isEmpty()) {
					hourfield.replaceSelection("");
				}
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (hourfield.getText().equals("") && Integer.valueOf(keyEvent.getCharacter()) > 2) {
					minutefield.requestFocus();
					hourfield.setText("0" + keyEvent.getCharacter());
					keyEvent.consume();
				} else if (Integer.valueOf(hourfield.getText() + keyEvent.getCharacter()) > 23) {
					minutefield.requestFocus();
					keyEvent.consume();
				} else {
					int minuteval;
					if (minutefield.getText().trim().length() > 0) {
						minuteval = Integer.valueOf(minutefield.getText());
					} else {
						minuteval = 0;
					}
					int hourval;
					hourval = Integer.valueOf(hourfield.getText() + keyEvent.getCharacter());

					LocalTime time = LocalTime.of(hourval, minuteval);
					day.set(time);
				}
			}
		});
		minutefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {

				if (!minutefield.getSelectedText().isEmpty()) {
					minutefield.replaceSelection("");
				}
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (Integer.valueOf(minutefield.getText() + keyEvent.getCharacter()) > 59) {
					keyEvent.consume();
				} else {
					int hourval;
					if (hourfield.getText().trim().length() > 0) {
						hourval = Integer.valueOf(hourfield.getText());
					} else {
						hourval = 0;
					}
					int minuteval;
					minuteval = Integer.valueOf(minutefield.getText() + keyEvent.getCharacter());
					LocalTime time = LocalTime.of(hourval, minuteval);
					day.set(time);
				}
			}
		});
		durationfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!durationfield.getSelectedText().isEmpty()) {
					durationfield.replaceSelection("");
				}

				if ("0123456789".contains(keyEvent.getCharacter())) {
					int num = Integer.valueOf(durationfield.getText() + keyEvent.getCharacter());
					if (num < 1200) {
						duration = num;
					} else {
						keyEvent.consume();
					}

				} else {
					keyEvent.consume();
				}
				day.updateduration();
			}

		});

		recurringnumber.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if ("0123456789".contains(keyEvent.getCharacter())) {

				} else {
					keyEvent.consume();
				}
				day.updateduration();
			}
		});

		recurringnumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				recurrnumber = Integer.valueOf(recurringnumber.getText());
				//System.out.println(String.valueOf(recurrnumber));
				setToDate(recurrnumber);
			}
		});

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (holidays.contains(item)) {
							setStyle("-fx-background-color: #2e82a9");
						}

						switch (item.getDayOfWeek()) {
						case MONDAY:
							if (!monday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case TUESDAY:
							if (!tuesday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case WEDNESDAY:
							if (!wednesday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case THURSDAY:
							if (!thursday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case FRIDAY:
							if (!friday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case SATURDAY:
							if (!saturday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case SUNDAY:
							if (!sunday) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;

						default:
							break;
						}

						if (item.isBefore(LocalDate.now().minusDays(7))) {
							setDisable(true);
						}

						if (item.isBefore(startdate) || item.isAfter(enddate)) {
							ObservableList<Account> stafftopop = FXCollections.observableArrayList(); // Staff to populate days with
							stafftopop.addAll(staff); // all added staff
							stafftopop.add(staffpicker.getSelectionModel().getSelectedItem()); // selected staff
							populatedays(stafftopop, item);
						}

						days.stream().filter((currentday) -> (item.equals(currentday.date))).forEachOrdered((currentday) -> {
							setStyle("-fx-background-color:" + fade(currentday.busyfraction, start, end));
						});
					}
				};
			}
		};

		datepicker.setDayCellFactory(dayCellFactory);

	}

	/**
	 *
	 * @param currdate
	 */
	public void populateAppointments(LocalDate currdate) { // Populates appointments in avaliability bar for current day
		appointments.clear();
		boolean staffpicked = false;
		boolean patientpicked = false;
		boolean roompicked = false;

		if (!staffpicker.getSelectionModel().isEmpty() || staff.size() > 0) {
			staffpicked = true;
		}
		if (!patientpicker.getSelectionModel().isEmpty() || patients.size() > 0) {
			patientpicked = true;
		}
		if (!roompicker.getSelectionModel().isEmpty()) {
			roompicked = true;
		}

		for (Appointment appointment : mainApp.getAppointments()) {
			if (appointment.getDate().equals(currdate)) {
				if (staffpicked) {

					ObservableList<Account> checkstaff = FXCollections.observableArrayList(); // Creates and fills array with all picked staff members + whoever is in the staff selection bar
					checkstaff.addAll(staff);
					checkstaff.add(staffpicker.getSelectionModel().getSelectedItem());

					for (Account acc : checkstaff) { // Iterates through staff members checking appointments on this day for them.
						try {
							if (appointment.getStaffMembers().contains(acc.getStaffNumber())) {
								appointments.add(appointment);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

				}
				if (patientpicked) {

					ObservableList<Patient> checkpatients = FXCollections.observableArrayList(); // Creates and fills array with all picked patients + whoever is in the patient selection bar
					checkpatients.addAll(patients);
					checkpatients.add(patientpicker.getSelectionModel().getSelectedItem());

					for (Patient patient : checkpatients) { // Iterates through patients checking appointments on this day for them.
						try {
							if (appointment.getPatients().contains(patient.getPatientNumber())) {
								appointments.add(appointment);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

				}
				if (roompicked) {
					if (appointment.getRoom().getId() == roompicker.getValue().getId()) {
						appointments.add(appointment);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public Patient patientFromString(String string) { // Returns a patient from a string
		boolean searching = true;
		Patient patient = null;
		for (int i = 0; i < mainApp.getPatients().size(); i++) {
			String tempname = mainApp.getPatients().get(i).getFirstName() + " " + mainApp.getPatients().get(i).getLastName() + " | " + String.valueOf(mainApp.getPatients().get(i).getPatientNumber());
			if (tempname.equals(string)) {
				patient = mainApp.getPatients().get(i);
				searching = false;
				break;
			}
		}
		return patient;
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public Account accountFromString(String string) { // Returns an account from a string
		boolean searching = true;
		Account account = null;
		for (int i = 0; i < mainApp.getAccounts().size(); i++) {
			String tempname = mainApp.getAccounts().get(i).getTitle() + "." + mainApp.getAccounts().get(i).getFirstName() + " " + mainApp.getAccounts().get(i).getLastName() + " | "
					+ mainApp.getAccounts().get(i).getRole();
			if (tempname.trim().equals(string.trim())) {
				account = mainApp.getAccounts().get(i);
				searching = false;
				break;
			}
		}
		return account;
	}

	/**
	 *
	 */
	public void hourfinished() { // hourfield is full
		if (hourfield.getText().length() == 2) {
			minutefield.requestFocus();
		}
	}

	/**
	 *
	 */
	public void minutefinished() { // minutefield is full
		if (minutefield.getText().length() == 2) {
		}
	}

	/**
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) { // sets the dialog stage
		this.dialogStage = dialogStage;
		coverpane.prefWidthProperty().bind(dialogStage.widthProperty());
		coverpane.prefHeightProperty().bind(dialogStage.heightProperty());
		coverpane.setTranslateX(3000);
		coverpane.setCache(true);
		coverpane.setCacheShape(true);
		coverpane.setCacheHint(CacheHint.SPEED);
	}

	/**
	 *
	 * @param appointment
	 */
	public void setAppointment(Appointment appointment) { // Sets appointment to modify
		this.appointment = appointment;
	}

	public boolean checkDate(LocalDate t1) { // checks if a date is 'ok' for avaliability
		
		checkAvaliable();

		boolean okay = true;

		switch (t1.getDayOfWeek()) {
		case MONDAY:
			if (!monday) {
				okay = false;
			}
			break;
		case TUESDAY:
			if (!tuesday) {
				okay = false;
			}
			break;
		case WEDNESDAY:
			if (!wednesday) {
				okay = false;
			}
			break;
		case THURSDAY:
			if (!thursday) {
				okay = false;
			}
			break;
		case FRIDAY:
			if (!friday) {
				okay = false;
			}
			break;
		case SATURDAY:
			if (!saturday) {
				okay = false;
			}
			break;
		case SUNDAY:
			if (!sunday) {
				okay = false;
			}
			break;

		default:
			break;
		}

		if (holidays.contains(datepicker.getValue())) {
			okay = false;
		}

		return okay;
	}

	private void checkdates() { // Checks recurring dates
		boolean looping = true;
		LocalDate date = datepicker.getValue();
		boolean okay = true;
		int loop = 0;
		while (looping) {
			loop += 1;
			okay = checkDate(date);
			switch (periodpicker.getValue()) {
			case "Daily":
				date = date.plusDays(1);
				break;
			case "Weekly":
				date = date.plusWeeks(1);
				break;
			case "Fortnightly":
				date = date.plusWeeks(2);
				break;
			case "Monthly":
				date = date.plusMonths(1);
				break;
			case "Quarterly":
				date = date.plusMonths(3);
				break;
			case "Annually":
				date = date.plusYears(1);
				break;
			}

			if (date.isAfter(todatepicker.getValue())) {
				looping = false;
			}

		}
		if (loop > 50) {
			Alert alert = new Alert(AlertType.WARNING);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(dialogStage);
			alert.setTitle("Warning");
			alert.setHeaderText("This will create " + String.valueOf(loop) + " Appointments");

			if (loop > 500) {
				alert.setContentText("This is likely to cause CRITICAL system lag");
			} else if (loop > 250) {
				alert.setContentText("This is likely to cause EXTREME system lag");
			} else if (loop > 100) {
				alert.setContentText("This is likely to cause SEVERE system lag");
			} else if (loop > 50) {
				alert.setContentText("This is likely to cause noticable system lag");
			}
			alert.showAndWait();
		}
		if (!okay) {
			Alert alert = new Alert(AlertType.WARNING);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(dialogStage);
			alert.setTitle("Warning");
			if (staffpicker.getSelectionModel().getSelectedItem() != null) {
				alert.setHeaderText("One of recurring appointment dates is an 'Unavaliable' day for " + staffpicker.getValue().getFirstName() + " " + staffpicker.getValue().getLastName());
			} else {
				alert.setHeaderText("One of recurring appointment dates is an 'Unavaliable' day for staff member");
			}
			alert.showAndWait();
		}

	}

	/**
	 *
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 *
	 */
	public void handleOk() { // Handles OK being clicked
		if (isInputValid()) {
			coverpane.setTranslateX(dialogStage.getWidth());
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(0);

			closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!saved) {
						LocalTime time = LocalTime.of(Integer.valueOf(hourfield.getText()), Integer.valueOf(minutefield.getText()));

						// mainApp.LoadAppointmentDataFromFile(mainApp.getFilePath());

						if (recurringcheckbox.selectedProperty().get()) {
							if (todatepicker.getValue() != null) {
								if (datepicker.getValue().isBefore(todatepicker.getValue())) {
									boolean looping = true;
									LocalDate date = datepicker.getValue();
									while (looping) {
										Appointment newappointment = new Appointment();
										newappointment.setTime(time);
										newappointment.setDate(date);
										newappointment.setType(0);
										newappointment.setDescription(descriptionfield.getText());
										newappointment.setRoom(roompicker.getValue());
										newappointment.setDuration(Integer.valueOf(durationfield.getText()));

										if (patientpicker.getValue() != null && patients.size() == 0) {
											addPatient();
										}
										String patientstring = "";
										for (Patient patient : patients) {
											patientstring += patient.getPatientNumber().toString() + ";";
										}
										newappointment.setPatient(patientstring);

										if (staffpicker.getValue() != null && staff.size() == 0) {
											addStaff();
										}
										String staffstring = "";
										for (Account account : staff) {
											staffstring += account.getStaffNumber().toString() + ";";
										}
										newappointment.setStaffMember(staffstring);

										newappointment.setAppType(typepicker.getValue().getId());
										newappointment.setCurrency(currencyfield.getText());
										newappointment.setPrice(Double.valueOf(pricefield.getText()));
										if (Double.valueOf(pricefield.getText()) == 0) {
											newappointment.setPaid(true);
										}

										int appointmentid = mainApp.appPrefs.getAppointmentID() + 1;
										newappointment.setID(appointmentid);

										mainApp.appPrefs.setAppointmentID(mainApp.appPrefs.getAppointmentID() + 1);

										switch (periodpicker.getValue()) {
										case "Daily":
											date = date.plusDays(1);
											break;
										case "Weekly":
											date = date.plusWeeks(1);
											break;
										case "Fortnightly":
											date = date.plusWeeks(2);
											break;
										case "Monthly":
											date = date.plusMonths(1);
											break;
										case "Quarterly":
											date = date.plusMonths(3);
											break;
										case "Annually":
											date = date.plusYears(1);
											break;
										}

										if (date.isAfter(todatepicker.getValue())) {
											looping = false;
											mainApp.actuallyAddAppointment(newappointment, false);
										} else {
											mainApp.actuallyAddAppointment2(newappointment, false);
										}

									}
								} else {
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											Alert alert = new Alert(AlertType.WARNING);
											Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
											stage.setAlwaysOnTop(true);

											DialogPane dialogPane = alert.getDialogPane();
											dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
											dialogPane.getStyleClass().add(".dialog-pane");

											alert.initOwner(dialogStage);
											alert.setTitle("Warning");
											alert.setHeaderText("'Until' Date cannot be before Initial Date");
											alert.showAndWait();
										}
									});

								}
							} else {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										Alert alert = new Alert(AlertType.WARNING);
										Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
										stage.setAlwaysOnTop(true);

										DialogPane dialogPane = alert.getDialogPane();
										dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
										dialogPane.getStyleClass().add(".dialog-pane");

										alert.initOwner(dialogStage);
										alert.setTitle("Warning");
										alert.setHeaderText("'Until' Date must be chosen");
										alert.showAndWait();
									}
								});
							}
						} else {
							long dayshift = 0;
							if (edit) {
								dayshift = appointment.getDate().until(datepicker.getValue(), ChronoUnit.DAYS);
							}

							appointment.setTime(time);
							appointment.setDate(datepicker.getValue());
							appointment.setType(0);
							appointment.setDescription(descriptionfield.getText());
							appointment.setRoom(roompicker.getValue());
							appointment.setDuration(Integer.valueOf(durationfield.getText()));

							if (patientpicker.getValue() != null) {
								addPatient();
							}
							String patientstring = "";
							for (Patient patient : patients) {
								patientstring += patient.getPatientNumber().toString() + ";";
							}
							appointment.setPatient(patientstring);

							if (staffpicker.getValue() != null) {
								addStaff();
							}
							String staffstring = "";
							for (Account account : staff) {
								staffstring += account.getStaffNumber().toString() + ";";
							}
							appointment.setStaffMember(staffstring);

							appointment.setAppType(typepicker.getValue().getId());

							if (!meeting) {
								appointment.setCurrency(currencyfield.getText());
								appointment.setPrice(Double.valueOf(pricefield.getText()));
								if (Double.valueOf(pricefield.getText()) == 0) {
									appointment.setPaid(true);
								}
							} else {
								appointment.setCurrency("");
								appointment.setPrice(0.00);
							}

							if (!edit) {
								int appointmentid = mainApp.appPrefs.getAppointmentID() + 1;
								appointment.setID(appointmentid);
								mainApp.appPrefs.setAppointmentID(mainApp.appPrefs.getAppointmentID() + 1);
							}

							if (edit) {
								switch (editrange) {
								case 1:
									for (Appointment editapp : recurringapps) {
										if (editapp.getDate().isAfter(appointment.getDate())) {
											editapp.setTime(time);
											editapp.setDate(editapp.getDate().plusDays(dayshift));
											editapp.setType(0);
											editapp.setDescription(descriptionfield.getText());
											editapp.setRoom(roompicker.getValue());
											editapp.setDuration(Integer.valueOf(durationfield.getText()));

											if (patientpicker.getValue() != null) {
												addPatient();
											}
											String patientstring2 = "";
											for (Patient patient : patients) {
												patientstring2 += patient.getPatientNumber().toString() + ";";
											}
											editapp.setPatient(patientstring2);

											if (staffpicker.getValue() != null) {
												addStaff();
											}
											String staffstring2 = "";
											for (Account account : staff) {
												staffstring2 += account.getStaffNumber().toString() + ";";
											}
											editapp.setStaffMember(staffstring2);

											editapp.setAppType(typepicker.getValue().getId());
											editapp.setCurrency(currencyfield.getText());
											editapp.setPrice(Double.valueOf(pricefield.getText()));
											if (Double.valueOf(pricefield.getText()) == 0) {
												editapp.setPaid(true);
											}
										}
									}
									break;

								case 2:
									for (Appointment editapp : recurringapps) {
										editapp.setTime(time);
										editapp.setDate(editapp.getDate().plusDays(dayshift));
										editapp.setType(0);
										editapp.setDescription(descriptionfield.getText());
										editapp.setRoom(roompicker.getValue());
										editapp.setDuration(Integer.valueOf(durationfield.getText()));

										if (patientpicker.getValue() != null) {
											addPatient();
										}
										String patientstring3 = "";
										for (Patient patient : patients) {
											patientstring += patient.getPatientNumber().toString() + ";";
										}
										editapp.setPatient(patientstring3);

										if (staffpicker.getValue() != null) {
											addStaff();
										}
										String staffstring3 = "";
										for (Account account : staff) {
											staffstring += account.getStaffNumber().toString() + ";";
										}
										editapp.setStaffMember(staffstring3);

										editapp.setAppType(typepicker.getValue().getId());
										editapp.setCurrency(currencyfield.getText());
										editapp.setPrice(Double.valueOf(pricefield.getText()));
										if (Double.valueOf(pricefield.getText()) == 0) {
											editapp.setPaid(true);
										}

									}
									break;
								}
							}

							mainApp.actuallyAddAppointment(appointment, edit);
						}
						mainApp.setVersion();
						saved = true;
						okClicked = true;
						dialogStage.close();
					}
				}
			});
			closeNav.play();
		}
	}

	private boolean isInputValid() { // checks if all field inputs are valid
		String errorMessage = "";
		String warningMessage = "";
		boolean warnMessage = false;

		if (hourfield.getText() == null || hourfield.getText().length() == 0) {
			errorMessage += "No valid Hour\n";
		}

		try {
			int hour = Integer.parseInt(hourfield.getText());
			if (hour < 7) {
				warningMessage += "Hours should be entered in 24 hour format\n";
			}

		} catch (Exception e) {
			errorMessage += "Hour field contains non numerical characters\n";
		}

		if (minutefield.getText().length() == 0) {
			minutefield.setText("00");
		}

		try {
			Integer.parseInt(minutefield.getText());
		} catch (Exception e) {
			errorMessage += "Minute field contains non numerical characters\n";
		}

		if (durationfield.getText().length() == 0) {
			durationfield.setText("30");
		}

		try {
			Integer.parseInt(durationfield.getText());
		} catch (Exception e) {
			errorMessage += "Duration field contains non numerical characters\n";
		}

		if (minutefield.getText() == null || minutefield.getText().length() == 0) {
			errorMessage += "No valid Minute\n";
		}

		if (durationfield.getText() == null || durationfield.getText().length() == 0) {
			errorMessage += "No valid Duration\n";
		}

		if (datepicker.valueProperty() == null) {
			errorMessage += "No valid Date\n";
		}

		if (!meeting) {
			if (patientpicker.getSelectionModel().getSelectedItem() == null && patients.isEmpty()) {
				errorMessage += "No Patient Selected\n";
			}
		}

		if (staffpicker.getSelectionModel().getSelectedItem() == null && staff.isEmpty()) {
			errorMessage += "No Staff Selected\n";
		}

		if (typepicker.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "No Appointment Type Selected\n";
		}

		if (roompicker.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "No Room Selected\n";
		}

		if (!meeting) {
			if (pricefield.getText() == null || pricefield.getText().length() == 0) {
				errorMessage += "No valid Price\n";
			}

			try {
				Double.valueOf(pricefield.getText());
			} catch (Exception e) {
				errorMessage += "No valid Price\n";
			}
		}

		if (!meeting) {
			if (currencyfield.getText() == null || currencyfield.getText().length() == 0) {
				errorMessage += "No valid Currency\n";
			}
		}

		if (recurringcheckbox.selectedProperty().get()) {
			if (todatepicker.valueProperty() == null) {
				errorMessage += "No valid end recurring Appointment Date\n";
			}
		}

		if (errorMessage.length() == 0) {
			//System.out.println("Error Message Length is Zero");

			LocalTime time = LocalTime.of(Integer.valueOf(hourfield.getText()), Integer.valueOf(minutefield.getText()));
			LocalTime endtime = LocalTime.of(Integer.valueOf(hourfield.getText()), Integer.valueOf(minutefield.getText())).plusMinutes(Integer.valueOf(durationfield.getText()));

			LocalTime staffstart;
			LocalTime staffend;

			if (staff.size() > 0) {
				staffstart = staff.get(0).getStartTime();
				staffend = staff.get(0).getEndTime();
				for (Account account : staff) {
					if (account.getStartTime().isAfter(staffstart)) {
						staffstart = account.getStartTime();
					}
					if (account.getEndTime().isBefore(staffend)) {
						staffend = account.getEndTime();
					}
				}
			} else {
				staffstart = staffpicker.getValue().getStartTime();
				staffend = staffpicker.getValue().getEndTime();
			}

			if (time.isBefore(staffstart) || endtime.isAfter(staffend)) {
				warningMessage += "Appointment falls outside of staff working hours.";
			}
			if (warningMessage.length() == 0) {
				//System.out.println("No Warning Message");
				return true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
				dialogPane.getStyleClass().add(".dialog-pane");

				alert.initOwner(dialogStage);
				alert.setTitle("Warning");
				alert.setHeaderText("Please consult the following warnings and decide to continue");
				alert.setContentText(warningMessage);

				ButtonType buttonTypeOne = new ButtonType("Continue Saving");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	@FXML
	private void onRecurringCheck() { // method run on recurring box checked
		if (recurringcheckbox.selectedProperty().get() == true) {
			recurringbox.setVisible(true);
			recurringbox.setManaged(true);
			recurringbox2.setVisible(true);
			recurringbox2.setManaged(true);
			recurringbox3.setVisible(true);
			recurringbox3.setManaged(true);
		} else {
			recurringbox.setVisible(false);
			recurringbox.setManaged(false);
			recurringbox2.setVisible(false);
			recurringbox2.setManaged(false);
			recurringbox3.setVisible(false);
			recurringbox3.setManaged(false);
		}
	}

	@FXML
	private void handleCancel() { // Handles the dialog being cancelled
		dialogStage.close();
	}

	/**
	 *
	 * @return
	 */
	public int getDuration() { // returns the appointment duration
		return duration;
	}

	/**
	 *
	 * @param time
	 */
	public void setTime(LocalTime time) { // Sets appointment time
		hourfield.setText(String.valueOf(time.getHour()));
		String minute = String.valueOf(time.getMinute());
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		minutefield.setText(minute);
	}

	/**
	 *
	 * @param t1
	 * @param next
	 * @param prev
	 */
	public void setHoveredTime(LocalTime t1, Appointment next, Appointment prev) { // Sets current hovered time on avaliability bar
		boolean red = false;

		currenttimelabel.setText(t1.toString() + " - " + t1.plusMinutes(duration).toString());
		if (next.getTime() != null) {
			nextappointment.setText(mainApp.getAppointmentTypes().get(next.getType()).getName() + ": " + next.getTime().toString() + " - " + next.getTime().plusMinutes(next.getDuration()).toString());
			if (t1.plusMinutes(duration).isAfter(next.getTime())) {
				red = true;
			}
		} else {
			nextappointment.setText("");
		}
		if (prev.getTime() != null) {
			prevappointment.setText(mainApp.getAppointmentTypes().get(prev.getType()).getName() + ": " + prev.getTime().toString() + " - " + prev.getTime().plusMinutes(prev.getDuration()).toString());
			if (t1.isBefore(prev.getTime().plusMinutes(prev.getDuration()))) {
				red = true;
			}
		} else {
			prevappointment.setText("");
		}

		if (red) {
			currenttimelabel.setStyle("-fx-text-fill: red;");
		} else {
			currenttimelabel.setStyle("-fx-text-fill: black;");
		}

	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Patient patient) { // sets appointment patient
		if (patient != null) {
			patientpicker.getSelectionModel().select(patient);
			populateAppointments(datepicker.getValue());
			if (appointments != null && appointments.size() > 0) {
				day.populateappointments(appointments);
			}
		}
	}

	/**
	 *
	 * @param account
	 */
	public void setAccount(Account account) { // sets appointment account < hmmmmmm
		if (account != null) {
			staffpicker.getSelectionModel().select(account);
			populateAppointments(datepicker.getValue());
			if (appointments != null && appointments.size() > 0) {
				day.populateappointments(appointments);
			}
		}
	}

	/**
	 *
	 * @param room
	 */
	public void setRoom(Room room) { // sets appointment room
		if (room != null) {
			roompicker.getSelectionModel().select(room);
			populateAppointments(datepicker.getValue());
			if (appointments != null && appointments.size() > 0) {
				day.populateappointments(appointments);
			}
		}
	}

	void setToDate(int number) { // sets 'to' date for recurring appointment stream
		disablelistener = true;
		if (datepicker.getValue() == null) {
			datepicker.setValue(LocalDate.now());
		}
		switch (periodpicker.getValue()) {
		case "Daily":
			todatepicker.setValue(datepicker.getValue().plusDays(number));
			break;
		case "Weekly":
			todatepicker.setValue(datepicker.getValue().plusWeeks(number));
			break;
		case "Fortnightly":
			todatepicker.setValue(datepicker.getValue().plusWeeks(number * 2));
			break;
		case "Monthly":
			todatepicker.setValue(datepicker.getValue().plusMonths(number));
			break;
		case "Quarterly":
			todatepicker.setValue(datepicker.getValue().plusMonths(number * 3));
			break;
		case "Annually":
			todatepicker.setValue(datepicker.getValue().plusYears(number));
			break;
		}
		disablelistener = false;
	}

	void setToNumber(LocalDate to) { // sets 'to' number for recurring appointment stream
		if (datepicker.getValue() == null) {
			datepicker.setValue(LocalDate.now());
		}
		boolean looping = true;
		LocalDate date = datepicker.getValue();
		int loop = 0;
		while (looping) {
			loop += 1;
			switch (periodpicker.getValue()) {
			case "Daily":
				date = date.plusDays(1);
				break;
			case "Weekly":
				date = date.plusWeeks(1);
				break;
			case "Fortnightly":
				date = date.plusWeeks(2);
				break;
			case "Monthly":
				date = date.plusMonths(1);
				break;
			case "Quarterly":
				date = date.plusMonths(3);
				break;
			case "Annually":
				date = date.plusYears(1);
				break;
			}

			if (date.isAfter(todatepicker.getValue())) {
				looping = false;
			}
		}
		recurringnumber.setText(String.valueOf(loop));
	}

	private void populatedays(ObservableList<Account> accounts, LocalDate basedate) { // Populates days and gives their 'business fraction' based on staff avaliability
		days.clear();

		boolean allstaff = true;
		if (accounts != null) {
			allstaff = false;
		}

		if (basedate == null) {
			if (datepicker.getValue() != null) {
				basedate = datepicker.getValue();
			} else {
				basedate = LocalDate.now();
			}
		}
		startdate = basedate.withDayOfMonth(1);
		enddate = basedate.withDayOfMonth(basedate.lengthOfMonth());

		for (Appointment appointment : mainApp.getAppointments()) {
			boolean staffcontained = false;

			if (!allstaff) { // Skips this function if allstaff
				for (Account acc : accounts) {
					if (acc != null && appointment != null) {
						if (appointment.getStaffMembers().contains(acc.getStaffNumber())) { // checks if any appointments on this day contain appointments with staffmembers
							staffcontained = true;
						}
					}
				}
			}
			if (allstaff || staffcontained && appointment.getDate().isAfter(startdate.minusDays(1)) && appointment.getDate().isBefore(enddate.plusDays(1))) {
				boolean found = false;
				for (day currentday : days) {

					if (appointment.getDate().equals(currentday.date)) {
						found = true;
						int minmins = 24 * 60; // Minimum minutes
						for (Account acc : accounts) {
							if (acc != null) {
								if (acc.getMinutes() < minmins) {
									minmins = (int) acc.getMinutes();
								}
							}
						}
						currentday.totalmins = minmins;
						//System.out.println("total Mins: " + String.valueOf(currentday.totalmins));
						currentday.busymins += appointment.getDuration();
						//System.out.println("Busy Mins: " + String.valueOf(currentday.busymins));
						currentday.busyfraction = currentday.busymins / currentday.totalmins;
					}
				}
				if (!found) {
					if (accounts != null) {
						day newday = new day();
						newday.date = appointment.getDate();
						int minmins = 24 * 60; // Minimum minutes
						for (Account acc : accounts) {
							if (acc != null) {
								if (acc.getMinutes() < minmins) {
									minmins = (int) acc.getMinutes();
								}
							}
						}
						newday.totalmins = minmins;
						//System.out.println("total Mins: " + String.valueOf(newday.totalmins));
						newday.busymins = appointment.getDuration();
						//System.out.println("Busy Mins: " + String.valueOf(newday.busymins));
						newday.busyfraction = newday.busymins / newday.totalmins;
						//System.out.println("Busy fraction: " + String.valueOf(newday.busyfraction));
						days.add(newday);
					}
				}
			}
		}
	}

	private String fade(double busyfraction, Color CLOSE, Color FAR) { // Fade between colours based on fraction
		//System.out.println("Performing Fade");

		//System.out.println("Ratio: " + String.valueOf(busyfraction));

		int red = (int) Math.round(Math.abs((busyfraction * FAR.getRed()) + ((1 - busyfraction) * CLOSE.getRed())) * 255);
		int green = (int) Math.round(Math.abs((busyfraction * FAR.getGreen()) + ((1 - busyfraction) * CLOSE.getGreen())) * 255);
		int blue = (int) Math.round(Math.abs((busyfraction * FAR.getBlue()) + ((1 - busyfraction) * CLOSE.getBlue())) * 255);

		//System.out.println("rgb(" + String.valueOf(red) + "," + String.valueOf(green) + "," + String.valueOf(blue) + ")");
		return "rgb(" + String.valueOf(red) + "," + String.valueOf(green) + "," + String.valueOf(blue) + ")";

	}

	private class day { // Class used for custom day colouring
		LocalDate date;
		double totalmins = 12 * 60;
		double busymins = 0;
		double busyfraction = 0;
	}

	public void addPatient() { // Adds patient to appointment
		if (patientpicker.getValue() != null) {
			Patient patient = patientpicker.getValue();
			patientpicker.getSelectionModel().clearSelection();
			patients.add(patient);

			VBox patientpane = new VBox();
			patientpane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			containerbox.prefWidthProperty().bind(patientpane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);

			patientpane.setEffect(glow);

			Insets insets = new Insets(0, 6, 0, 6);
			containerbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			Label patientlab = new Label();
			patientlab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

			patientpane.getStyleClass().add("appointment-delete");

			patientpane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					patients.remove(patient);
					patientslist.getChildren().remove(patientpane);
					populateAppointments(datepicker.getValue());
					if (appointments != null && appointments.size() > 0) {
						day.populateappointments(appointments);
					}
				}

			});

			patientlab.setText(patient.getFirstName() + " " + patient.getLastName() + " | ID: " + String.valueOf(patient.getPatientNumber()));
			patientlab.getStyleClass().add("label-list");
			containerbox.getChildren().add(patientlab);

			patientpane.getChildren().add(containerbox);

			containerbox.prefWidthProperty().bind(patientpane.widthProperty());
			containerbox.maxHeightProperty().bind(patientpane.heightProperty());
			// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
			// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

			patientslist.getChildren().add(patientpane);
			populateAppointments(datepicker.getValue());
			if (appointments != null && appointments.size() > 0) {
				day.populateappointments(appointments);
			}
		}
	}

	public void addStaff() { // Adds account to appointment
		if (staffpicker.getValue() != null) {
			
			Account account = staffpicker.getValue();
			staffpicker.getSelectionModel().clearSelection();
			staff.add(account);
			checkAvaliable();

			VBox staffpane = new VBox();
			staffpane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			containerbox.prefWidthProperty().bind(staffpane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);

			staffpane.setEffect(glow);

			Insets insets = new Insets(0, 6, 0, 6);
			containerbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			Label stafflab = new Label();
			stafflab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

			staffpane.getStyleClass().add("appointment-delete");

			staffpane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					staff.remove(account);
					stafflist.getChildren().remove(staffpane);
					populateAppointments(datepicker.getValue());
					if (appointments != null && appointments.size() > 0) {
						day.populateappointments(appointments);
					}
				}

			});

			stafflab.setText(account.getTitle() + " " + account.getFirstName() + " " + account.getLastName() + " | ID: " + String.valueOf(account.getStaffNumber()));
			stafflab.getStyleClass().add("label-list");
			containerbox.getChildren().add(stafflab);

			staffpane.getChildren().add(containerbox);

			containerbox.prefWidthProperty().bind(staffpane.widthProperty());
			containerbox.maxHeightProperty().bind(staffpane.heightProperty());
			// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
			// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

			stafflist.getChildren().add(staffpane);
			populateAppointments(datepicker.getValue());
			if (appointments != null && appointments.size() > 0) {
				day.populateappointments(appointments);
			}

		}
	}

	public void clearStaff() { // clears staff picker
		staffpicker.setValue(null);
		populateAppointments(datepicker.getValue());
		if (appointments != null && appointments.size() > 0) {
			day.populateappointments(appointments);
		}
	}

	public void clearPatient() { // clears patient picker
		patientpicker.setValue(null);
		populateAppointments(datepicker.getValue());
		if (appointments != null && appointments.size() > 0) {
			day.populateappointments(appointments);
		}
	}

}
