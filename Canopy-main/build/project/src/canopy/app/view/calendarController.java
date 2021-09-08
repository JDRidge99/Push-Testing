package canopy.app.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Patient;
import canopy.app.model.Room;
import canopy.app.util.ComparatorBox;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author Andy
 */
public class calendarController {

	private MainApp mainapp;
	LocalDate today;
	private final ObservableList<day> days = FXCollections.observableArrayList();

	@FXML
	private ImageView back;

	@FXML
	private ImageView forward;

	@FXML
	private ImageView back2;

	@FXML
	private ImageView forward2;

	@FXML
	private AnchorPane superparent;

	@FXML
	private DatePicker datepicker;

	@FXML
	private ComboBox<AppointmentType> typepicker;

	@FXML
	private ComboBox<Patient> patientpicker;

	@FXML
	private ComboBox<Room> roompicker;

	@FXML
	private ComboBox<Account> staffpicker;

	@FXML
	private CheckBox paidcheck;

	@FXML
	private Button clearstaffbutton;

	@FXML
	private Label paidheader;

	@FXML
	private Label typeheader;

	@FXML
	private GridPane calendarPane;

	Patient chosenpatient;

	boolean clearpatient = false;

	boolean manual = false;
	int numberofdays = 6;
	int dayshift = 0;
	Glow glowb = new Glow(0);
	Glow glowf = new Glow(0);
	Glow glowb2 = new Glow(0);
	Glow glowf2 = new Glow(0);

	/**
	 *
	 * @param main
	 */
	public void justSetMainApp(MainApp main) {
		this.mainapp = main;
	}

	/**
	 *
	 * @param main
	 */
	public void setMainApp(MainApp main) {
		this.mainapp = main;

		switch (mainapp.getCurrentAccount().getPermission()) {
		case 0: // Master Admin

			break;
		case 1: // Admin

			break;
		case 2: // Self Timetabling Staff
			clearstaffbutton.setVisible(false);
			paidheader.setVisible(false);
			paidcheck.setVisible(false);
			break;
		case 3: // Observing Staff
			staffpicker.setEditable(false);
			clearstaffbutton.setVisible(false);
			paidheader.setVisible(false);
			paidcheck.setVisible(false);
			break;
		case 5: // Non-Clinial Master Staff // Jumps straight to 5 as current account will never be 4.

			break;
		case 6: // Non-Clinical Staff

			break;

		}

		back.setEffect(glowb);
		forward.setEffect(glowf);

		back2.setEffect(glowb2);
		forward2.setEffect(glowf2);

		roompicker.setItems(mainapp.getRooms());
		datepicker.setValue(LocalDate.now());
		typepicker.requestFocus();

		StringConverter<Account> accountconverter = new StringConverter<Account>() {

			@Override
			public Account fromString(String string) {
				return accountFromString(string);
			}

			@Override
			public String toString(Account object) {
				// TODO Auto-generated method stub
				try {
					return object.getFirstName() + " " + object.getLastName();
				} catch (Exception e) {
					return "";
				}

			}
		};

		staffpicker.converterProperty().set(accountconverter);

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
		for (AppointmentType apptype : mainapp.getAppointmentTypes()) {
			System.out.println("Mainapp Apptype: " + apptype.getName());
		}
		typepicker.setItems(mainapp.getAppointmentTypes());

		for (AppointmentType apptype : typepicker.getItems()) {
			System.out.println("Typepicker Apptype: " + apptype.getName());
		}

		if (mainapp.getCurrentAccount().getPermission() != 5 && mainapp.getCurrentAccount().getPermission() != 6) { // if current account has access to patients
			patientpicker.setItems(mainapp.getPatients());
		}

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

		if (mainapp.getCurrentAccount().getPermission() != 2 && mainapp.getCurrentAccount().getPermission() != 3) { // If current account allows changing staff, populate staff picker
			staffpicker.setItems(mainapp.getClinicalAccounts());
		}

		if (mainapp.getCurrentAccount().getPermission() != 5 && mainapp.getCurrentAccount().getPermission() != 6 && mainapp.getCurrentAccount().getPermission() != 1
				&& mainapp.getCurrentAccount().getPermission() != 0) { // if current account could not
			// have appointments,
			staffpicker.setValue(mainapp.getCurrentAccount());
		}

		typepicker.valueProperty().addListener(new ChangeListener<AppointmentType>() {
			@Override
			public void changed(ObservableValue ov, AppointmentType t, AppointmentType t1) {
				try {
					initialise();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		patientpicker.valueProperty().addListener(new ChangeListener<Patient>() {
			@Override
			public void changed(ObservableValue ov, Patient t, Patient t1) {
				if (t1 != null) {
					chosenpatient = t1;
				}
				if (t1 == null && !clearpatient) {
					patientpicker.setValue(chosenpatient);
				}
				try {
					initialise();
				} catch (IOException e) {
					mainapp.writeError(e);
					e.printStackTrace();
				}
			}
		});
		roompicker.valueProperty().addListener(new ChangeListener<Room>() {
			@Override
			public void changed(ObservableValue ov, Room t, Room t1) {
				try {
					initialise();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		staffpicker.valueProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue ov, Account t, Account t1) {
				try {
					initialise();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		datepicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue ov, LocalDate t, LocalDate t1) {

				if (!manual) { // Checks if change is result of arrows changed
					try {
						dayshift = -LocalDate.now().compareTo(t1);
						initialise();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now().minusMonths(6))) {
							setDisable(true);
						}
					}
				};
			}
		};

		datepicker.setDayCellFactory(dayCellFactory);

		paidcheck.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					initialise();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		checkwidth();

		mainapp.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				checkwidth();
			}
		});

	}

	/**
	 *
	 * @throws IOException
	 */
	public void initialise() throws IOException {
		today = LocalDate.now();
		calendarPane.getChildren().clear();

		for (int daynumber = 0; daynumber < numberofdays; daynumber++) {
			addDay(daynumber);
		}
	}

	void addDay(int daynumber) throws IOException {
		day currentday = new day();
		currentday.day = today.plusDays(daynumber + dayshift);

		for (Appointment currapp : mainapp.getAppointments()) {
			if (currapp.getDate().equals(currentday.day)) {
				boolean include = true;
				System.out.println("Adding Day: " + currentday.day.toString());

				if (!typepicker.getSelectionModel().isEmpty() && !currentday.appointments.contains(currapp)) {
					System.out.println("Typepicker is not empty, and current appointment has not yet been added to the day.");
					System.out.println("Typepicker value name: " + typepicker.getValue().getName() + ", Typepicker value ID: " + String.valueOf(typepicker.getValue().getId()));
					System.out.println("Current Appointment type ID: " + String.valueOf(currapp.getAppType()));

					if (!typepicker.getValue().getId().equals(currapp.getAppType())) {
						System.out.println("Include equals false");
						include = false;
					}
				}
				if (!currentday.appointments.contains(currapp)) {
					if (paidcheck.selectedProperty().get() == true) {
						if (currapp.getPaid()) {
							include = false;
						}
					}
				}
				if (!patientpicker.getSelectionModel().isEmpty() && !currentday.appointments.contains(currapp)) {
					if (!currapp.getPatients().contains(patientpicker.getValue().getPatientNumber())) {
						include = false;
					}
				}
				if (!roompicker.getSelectionModel().isEmpty() && !currentday.appointments.contains(currapp)) {
					if (roompicker.getValue().getId() != currapp.getRoom().getId()) {
						include = false;
					}
				}
				if (!staffpicker.getSelectionModel().isEmpty() && !currentday.appointments.contains(currapp)) {
					if (!currapp.getStaffMembers().contains(staffpicker.getValue().getStaffNumber())) {
						include = false;
					}
				}
				if (include) {
					currentday.appointments.add(currapp);
				}
			}
		}

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/day.fxml"));
		AnchorPane daypane = (AnchorPane) loader.load();

		calendarPane.add(daypane, daynumber, 0);

		// daypane.prefHeightProperty().bind(calendarPane.heightProperty());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(1);
		dropShadow.setOffsetX(1);
		dropShadow.setOffsetY(1);
		dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));

		daypane.setEffect(dropShadow);

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(700), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		playAfterPause(800 + 150 * daynumber, timeline);

		// Give the controller access to the main app.
		dayController daycontroller = loader.getController();
		daycontroller.setParentController(this);
		daycontroller.setMainApp(mainapp);
		daycontroller.setDayName(currentday.day.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, mainapp.getLocale()));
		if (!staffpicker.getSelectionModel().isEmpty()) {
			if (staffpicker.getValue().getHolidayList().contains(currentday.day)) {
				daycontroller.setavaliable(false);
			}
		}
		LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
		daycontroller.setDate(dateStringConverter.toString(currentday.day), currentday.day);

		for (int i = 0; i < currentday.appointments.size(); i++) {

			for (int n = 0; n < currentday.appointments.size(); n++) {

				if (n < i) {
					if (currentday.appointments.get(i).getTime().isBefore(currentday.appointments.get(n).getTime())) {
						Appointment holdappointment = currentday.appointments.get(n);
						currentday.appointments.set(n, currentday.appointments.get(i));
						currentday.appointments.set(i, holdappointment);
					}
				}
				if (n > i) {
					if (currentday.appointments.get(i).getTime().isAfter(currentday.appointments.get(n).getTime())) {
						Appointment holdappointment = currentday.appointments.get(n);
						currentday.appointments.set(n, currentday.appointments.get(i));
						currentday.appointments.set(i, holdappointment);
					}
				}
			}

		}

		if (currentday.appointments.size() != 0) { // ADDS STARTING APPOINTMENT GAP
			LocalTime starttime = currentday.appointments.get(0).getTime();

			LocalTime stafftime = LocalTime.of(9, 0);

			if (staffpicker.getValue() != null) {
				stafftime = staffpicker.getValue().getStartTime(); // If a staff member is selected, use their starting time
			} else {
				for (Account staff : mainapp.getAccounts()) {
					if (staff.getStartTime() != null) {
						if (staff.getStartTime().isBefore(stafftime)) {
							stafftime = staff.getStartTime(); // Otherwise, use the earliest starting time of any staff
						}
					}
				}
			}

			if (stafftime.isBefore(currentday.appointments.get(0).getTime())) {

				long minutestill = stafftime.until(currentday.appointments.get(0).getTime(), ChronoUnit.MINUTES);

				long hourstill = stafftime.until(currentday.appointments.get(0).getTime(), ChronoUnit.HOURS);

				minutestill -= hourstill * 60;

				Appointment gapAppointment = new Appointment("0", "0", currentday.day, starttime, null, 2, String.valueOf(hourstill) + " HR " + String.valueOf(minutestill), (int) minutestill, 0,
						99999, 999.00, "x");
				currentday.appointments.add(0, gapAppointment);
			}
		}

		for (int i = 0; i < currentday.appointments.size() - 1; i++) {

			LocalTime appointmentendtime = currentday.appointments.get(i).getTime().plus(currentday.appointments.get(i).getDuration(), ChronoUnit.MINUTES);

			boolean addgap = false;

			long minutestill = appointmentendtime.until(currentday.appointments.get(i + 1).getTime(), ChronoUnit.MINUTES);

			if (minutestill > 0) {
				addgap = true;
			}

			long hourstill = appointmentendtime.until(currentday.appointments.get(i + 1).getTime(), ChronoUnit.HOURS);
			minutestill -= hourstill * 60;

			// String patient, String staffmember, LocalDate date, LocalTime time, Room room, int type, String description, int duration, int appointmentType, int id, double price, String currency

			if (addgap) {
				Appointment gapAppointment = new Appointment("0", "0", currentday.day, appointmentendtime, null, 2, String.valueOf(hourstill) + " HR " + String.valueOf(minutestill), (int) minutestill,
						0, 99999, 999.00, "x");
				currentday.appointments.add(i + 1, gapAppointment);
				i += 1;
			}

		}

		int pos = -1;
		for (Appointment appointment : currentday.appointments) {
			pos += 1;
			daycontroller.addRow(pos);
			daycontroller.addAppointment(appointment, pos);

		}
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public Patient patientFromString(String string) {
		boolean searching = true;
		Patient patient = null;
		for (int i = 0; i < mainapp.getPatients().size(); i++) {
			String tempname = mainapp.getPatients().get(i).getFirstName() + " " + mainapp.getPatients().get(i).getLastName();
			if (tempname.equals(string)) {
				patient = mainapp.getPatients().get(i);
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
	public Account accountFromString(String string) {
		boolean searching = true;
		Account account = null;
		for (int i = 0; i < mainapp.getAccounts().size(); i++) {
			String tempname = mainapp.getAccounts().get(i).getFirstName() + " " + mainapp.getAccounts().get(i).getLastName();
			if (tempname.trim().equals(string.trim())) {
				account = mainapp.getAccounts().get(i);
				searching = false;
				break;
			}
		}
		return account;
	}

	/**
	 *
	 * @param millis
	 * @param timeline
	 */
	public void playAfterPause(int millis, Timeline timeline) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				timeline.play();
			}
		});
		new Thread(sleeper).start();
	}

	/**
	 *
	 * @param date
	 * @param patient
	 */
	public void addNewAppointment(LocalDate date, Patient patient) {
		mainapp.addNewAppointment(date, patient, null, null);
	}

	@FXML
	void clickforward() {
		dayshift += 1;
		manual = true;
		datepicker.setValue(LocalDate.now().plusDays(dayshift));
		manual = false;
		calendarPane.getChildren().clear();
		try {
			initialise();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void clickback() {
		if (dayshift > -184) {
			dayshift -= 1;
			manual = true;
			datepicker.setValue(LocalDate.now().plusDays(dayshift));
			manual = false;
			calendarPane.getChildren().clear();
			try {
				initialise();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@FXML
	void clickforward2() {
		dayshift += numberofdays;
		manual = true;
		datepicker.setValue(LocalDate.now().plusDays(dayshift));
		manual = false;
		calendarPane.getChildren().clear();
		try {
			initialise();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void clickback2() {
		if (dayshift > -184) {
			dayshift -= numberofdays;
			manual = true;
			datepicker.setValue(LocalDate.now().plusDays(dayshift));
			manual = false;
			calendarPane.getChildren().clear();
			try {
				initialise();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setDayShift(int shift) {
		dayshift = shift;
		manual = true;
		datepicker.setValue(LocalDate.now().plusDays(dayshift));
		manual = false;
		calendarPane.getChildren().clear();
		try {
			initialise();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void groundDay(AnchorPane pane) {

		DropShadow dropShadow = (DropShadow) pane.getEffect();
		
		pane.setCache(true);
		pane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);
		
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				pane.setCacheHint(CacheHint.QUALITY); // Sets to prioritising quality				
			}
		});
		
		timeline.playFromStart();

	}

	void liftDay(AnchorPane pane) {

		DropShadow dropShadow = (DropShadow) pane.getEffect();
		
		pane.setCache(true);
		pane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);
		
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				pane.setCacheHint(CacheHint.QUALITY); // Sets to prioritising quality				
			}
		});
		
		timeline.playFromStart();
		

	}

	/**
	 *
	 */
	public void checkwidth() {
		double divwidth;
		if (mainapp.getSmallText()) {
			divwidth = 130;
		} else {
			divwidth = 200;
		}
		double width = mainapp.getPrimaryStage().getWidth() - 600;
		numberofdays = (int) Math.round(width / divwidth);
		try {
			initialise();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void hoverb() {
		glowb.setLevel(0.6);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			groundDay(pane);
		});

	}

	@FXML
	void hoverf() {
		glowf.setLevel(0.6);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			groundDay(pane);
		});

	}

	@FXML
	void hoverlb() {
		glowb.setLevel(0);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			liftDay(pane);
		});
	}

	@FXML
	void hoverlf() {
		glowf.setLevel(0);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			liftDay(pane);
		});
	}

	@FXML
	void hoverb2() {
		glowb2.setLevel(0.6);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			groundDay(pane);
		});

	}

	@FXML
	void hoverf2() {
		glowf2.setLevel(0.6);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			groundDay(pane);
		});

	}

	@FXML
	void hoverlb2() {
		glowb2.setLevel(0);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			liftDay(pane);
		});
	}

	@FXML
	void hoverlf2() {
		glowf2.setLevel(0);

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			liftDay(pane);
		});
	}

	/**
	 *
	 */
	@FXML
	public void raiseall() {

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			liftDay(pane);
		});
	}

	/**
	 *
	 */
	@FXML
	public void lowerall() {

		calendarPane.getChildren().stream().map((node) -> (AnchorPane) node).forEachOrdered((pane) -> {
			groundDay(pane);
		});
	}

	@FXML
	void cleartype() {
		typepicker.getSelectionModel().clearSelection();
	}

	@FXML
	void clearpatient() {
		clearpatient = true;
		patientpicker.getSelectionModel().clearSelection();
		clearpatient = false;
	}

	@FXML
	void clearroom() {
		roompicker.getSelectionModel().clearSelection();
	}

	@FXML
	void clearstaff() {
		staffpicker.getSelectionModel().clearSelection();
	}

	class day {
		LocalDate day;
		private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
	}

}
