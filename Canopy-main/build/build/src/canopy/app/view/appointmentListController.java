package canopy.app.view;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.Patient;
import canopy.app.model.Room;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author Andy Controller for list of all appointments visible to the logged in user.
 */

public class appointmentListController {

	@FXML
	private GridPane schedule;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private AnchorPane titlebox;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private MainApp mainapp;
	@FXML
	private ComboBox<String> sorterselection;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();

	private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

	int selected = 0;

	int displaylimit = 10;

	Patient currentpatient;

	Account currentaccount;

	Room currentroom;

	LocalDate date2;

	/**
	 *
	 */
	public void initialise() { // Initialises class, called when list is created.

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		schedule.prefWidthProperty().bind(scrllparent.widthProperty());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(0);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);
		dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));

		parent.setEffect(dropShadow);

		final Timeline timeline = new Timeline(); // Animates list raising
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(700), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		playAfterPause(1050, timeline);

		sorttypes.add("ID"); // Adds all sorting options to array
		sorttypes.add("Date");
		sorttypes.add("Time");
		sorttypes.add("Patient ID");
		sorttypes.add("Staff ID");
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("ID");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() { // Adds listener to sort appointments when sort criteria changes
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				appointments = sortAppointments(appointments);
				int n = 0; // Position Appointment will be placed
				for (int i = 0; i <= displaylimit; i++) {
					
					try {		// Try in case display limit is too high				
					addAppointment(appointments.get(i), n);}
					catch (Exception e) {
						// TODO: handle exception
					}
					
					n += 1;
				}
			}
		});

	}

	ObservableList<Appointment> sortAppointments(ObservableList<Appointment> input) { // Does what it says on the tin
		ObservableList<Appointment> output = FXCollections.observableArrayList();
		output.addAll(input);

		if (sorterselection.getValue() != null) {

			switch (sorterselection.getValue()) {
			case "ID":
				Comparator<Appointment> comparator = Comparator.comparingInt(Appointment::getID);
				FXCollections.sort(output, comparator);
				break;
			case "Date":
				Collections.sort(output, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment p2, Appointment p1) {
						return p1.getDate().compareTo(p2.getDate());
					}
				});
				break;
			case "Time":
				Collections.sort(output, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment p2, Appointment p1) {
						return p1.getTime().compareTo(p2.getTime());
					}
				});
				break;
			case "Patient ID":
				Comparator<Appointment> comparator1 = Comparator.comparingInt(Appointment::getPatientInt);
				FXCollections.sort(output, comparator1);
				break;
			case "Staff ID":
				Comparator<Appointment> comparator2 = Comparator.comparingInt(Appointment::getStaffInt);
				FXCollections.sort(output, comparator2);
				break;
			}

		}
		return output;
	}

	/**
	 *
	 * @param millis
	 * @param timeline
	 */
	public void playAfterPause(int millis, Timeline timeline) { // Function to run a timeline after a pause
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

	@FXML
	void ground() { // 'grounds' appointment list (smoothly gets rid of 3d effects, looking like it lowers it to the ground)

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		if (dropShadow == null) {
			dropShadow = new DropShadow();
			dropShadow.setRadius(0);
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));
		}

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();

	}

	@FXML
	void lift() { // Opposite of ground

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();

	}

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) { // Sets the mainapp instance to link back to for accessing variables and calling methods
		this.mainapp = mainapp;
		if (mainapp.getCurrentAccount().getPermission() != 0 && mainapp.getCurrentAccount().getPermission() != 1 && mainapp.getCurrentAccount().getPermission() != 2) {
			addbutton.setVisible(false);
			addbutton.setManaged(false);
		}
	}

	/**
	 *
	 * @param pos
	 */
	public void addRow(int pos) { // Adds a new row to the list
		Pane fillpane = new Pane();
		schedule.addRow(pos, fillpane);
	}

	/**
	 *
	 * @param appointment
	 * @param pos
	 */
	public void addAppointment(Appointment appointment, int pos) { // Adds a new appointment block in the list, populating it with info and adding onclick effects

		VBox appointmentpane = new VBox();
		appointmentpane.setAlignment(Pos.CENTER);
		HBox containerbox = new HBox();
		containerbox.prefWidthProperty().bind(appointmentpane.widthProperty());
		containerbox.setAlignment(Pos.CENTER);

		if (pos < displaylimit) {
			VBox importantbox = new VBox();
			VBox buttonbox = new VBox();
			Glow glow = new Glow(0);
			Button deletebutton = new Button();
			Button editbutton = new Button();

			buttonbox.getChildren().addAll(editbutton, deletebutton);
			buttonbox.setAlignment(Pos.CENTER);

			deletebutton.setText("DELETE");
			editbutton.setText("EDIT");

			deletebutton.getStyleClass().add("buttons");
			editbutton.getStyleClass().add("buttons");

			deletebutton.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.getSideBarController().removefromlist(appointment);
				}
			});

			editbutton.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.getSideBarController().setAppointment(appointment);
					mainapp.getSideBarController().edit();
				}
			});

			appointmentpane.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {

					Alert alert = new Alert(AlertType.CONFIRMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.setAlwaysOnTop(true);

					DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
					dialogPane.getStyleClass().add(".dialog-pane");

					alert.initOwner(mainapp.getPrimaryStage());
					alert.setTitle("Go to Planner?");
					alert.setHeaderText("See Appointment Details in Planner?");

					ButtonType buttonTypeOne = new ButtonType("Yes");
					ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == buttonTypeOne) {
						mainapp.seeAppointment(appointment);
					} else {
						alert.close();
					}

				}
			});

			Insets insets = new Insets(0, 6, 0, 6);
			containerbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			importantbox.setAlignment(Pos.CENTER_LEFT);
			Label timelab = new Label();
			timelab.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
			Label roomlab = new Label();
			Label descriptionlab = new Label();
			Label durationlab = new Label();
			Label datelab = new Label();
			datelab.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

			switch (appointment.getType()) {
			case 0:
				appointmentpane.getStyleClass().add("appointment");

				appointmentpane.setOnMouseExited(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						glow.setLevel(0);
					}
				});

				LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
				datelab.setText(dateStringConverter.toString(appointment.getDate()) + " | ");
				timelab.setText(appointment.getTime().toString() + " | ");
				roomlab.setText(appointment.getRoom().getRoomName());
				descriptionlab.setText(appointment.getDescription());
				durationlab.setText(String.valueOf(appointment.getDuration()) + " MINS");

				descriptionlab.setWrapText(true);
				durationlab.setWrapText(true);
				roomlab.setWrapText(true);

				if (mainapp.getSmallText()) {
					datelab.getStyleClass().add("label-list");
					timelab.getStyleClass().add("label-list");
					roomlab.getStyleClass().add("label-list");
					descriptionlab.getStyleClass().add("label-list");
					durationlab.getStyleClass().add("label-list");
				} else {
					datelab.getStyleClass().add("label-list-big");
					timelab.getStyleClass().add("label-list-big");
					roomlab.getStyleClass().add("label-list");
					descriptionlab.getStyleClass().add("label-list");
					durationlab.getStyleClass().add("label-list");
				}


				containerbox.getChildren().addAll(datelab, timelab, importantbox, buttonbox);
				importantbox.getChildren().addAll(roomlab, descriptionlab, durationlab);
				editbutton.setMinWidth(deletebutton.getWidth());

				break;

			}

		} else if(pos == displaylimit){
			Label loadmorelab = new Label();
			loadmorelab.setText("Load More");
			loadmorelab.getStyleClass().add("label-list-big");
			containerbox.getChildren().add(loadmorelab);
			appointmentpane.getStyleClass().add("appointment");

			appointmentpane.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {

					displaylimit += 20;
					refreshlist();

				}
			});

		}
		
		appointmentpane.getChildren().add(containerbox);
		containerbox.prefWidthProperty().bind(appointmentpane.widthProperty());
		containerbox.maxHeightProperty().bind(appointmentpane.heightProperty());

		schedule.add(appointmentpane, 0, pos);

	}

	/**
	 *
	 */
	public void addnewAppointment() { // Fires off the command in the mainapp class to add a new appointment, launching the appointment dialog
		mainapp.addNewAppointment(null, currentpatient, currentaccount, currentroom);
	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Patient patient) { // Sets a chosen patient to view appointments for
		selected = 0;
		currentpatient = patient;
		appointments.clear();
		schedule.getChildren().clear();

		mainapp.getAppointments().forEach((currappointment) -> {
			boolean permission = false;
			if (currappointment.getStaffMembers().contains(mainapp.getCurrentAccount().getStaffNumber())) { // if appointment is with current account, it can be shown
				permission = true;
			} else if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1) {// if current account is admin or master admin, appointments can be shown
				permission = true;
			}
			if (currappointment != null && patient != null && currappointment.getAppType() != 9999) { // If current appoinment is not null, the current patient is not null, and the current appointment
																										// is not a meeting
				if (currappointment.getPatients().contains(patient.getPatientNumber()) && permission) {
					// If patient equals current patient
					appointments.add(currappointment); // Add appointment to temporary list
				}
			}
		});

		appointments = sortAppointments(appointments);

		int n = 0; // Position Appointment will be placed
		for (int i = 0; i <= displaylimit; i++) {
			
			try {		// Try in case display limit is too high				
			addAppointment(appointments.get(i), n);}
			catch (Exception e) {
				// TODO: handle exception
			}
			
			n += 1;
		}

	}

	/**
	 *
	 * @param room
	 */
	public void setRoom(Room room) { // Sets a chosen room to view appointments for
		selected = 2;
		currentroom = room;
		appointments.clear();
		schedule.getChildren().clear();

		mainapp.getAppointments().forEach((currappointment) -> {
			boolean permission = false;
			if (currappointment.getStaffMembers().contains(mainapp.getCurrentAccount().getStaffNumber())) { // if appointment is with current account, it can be shown
				permission = true;
			} else if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 5
					|| mainapp.getCurrentAccount().getPermission() == 6) { // if account is allowed to see all appointments
				permission = true;
			}
			if (currappointment.getRoom().getId() == room.getId() && permission) {
				appointments.add(currappointment); // Add appointment to temporary list
			}
		});

		appointments = sortAppointments(appointments);

		int n = 0; // Position Appointment will be placed
		for (int i = 0; i <= displaylimit; i++) {
			addAppointment(appointments.get(i), n);
			n += 1;
		}

	}

	/**
	 *
	 */
	public void refreshlist() { // refreshes the list of appointments
		switch (selected) {
		case 0:
			setPatient(currentpatient);
			break;
		case 1:
			setAccount(currentaccount);
			break;
		case 2:
			setRoom(currentroom);
			break;
		}
	}

	/**
	 *
	 * @param account
	 */
	public void setAccount(Account account) { // Sets a chosen account to view appointments for
		currentaccount = account;
		selected = 1;
		int currentstaffnumber = account.getStaffNumber();
		appointments.clear();
		schedule.getChildren().clear();

		boolean permission = false;
		if (account.getStaffNumber() == mainapp.getCurrentAccount().getStaffNumber()) { // If account is looking at it's own appointments
			permission = true;
		} else if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 5
				|| mainapp.getCurrentAccount().getPermission() == 6) { // Otherwise must be of apropriate permission level
			permission = true;
		}

		if (permission) {

			mainapp.getAppointments().stream().filter((currappointment) -> (currappointment.getStaffMembers().contains(currentstaffnumber))).forEachOrdered((currappointment) -> {
				appointments.add(currappointment);
			});

		} else {
			hide();
		}

		appointments = sortAppointments(appointments);

		int n = 0; // Position Appointment will be placed
		for (int i = 0; i <= displaylimit; i++) {
			
			try {		// Try in case display limit is too high				
			addAppointment(appointments.get(i), n);}
			catch (Exception e) {
				// TODO: handle exception
			}
			
			n += 1;
		}
	}

	/**
	 *
	 */
	public void show() { // Allows adding of appointments
		addbutton.getStyleClass().clear();
		addbutton.getStyleClass().add("card-orange");
		addbutton.setDisable(false);

		titlebox.getStyleClass().clear();
		titlebox.getStyleClass().add("card");
	}

	/**
	 *
	 */
	public void hide() { // Disables adding of appointments
		schedule.getChildren().clear();
		addbutton.getStyleClass().clear();
		addbutton.getStyleClass().add("card2");
		addbutton.setDisable(true);

		titlebox.getStyleClass().clear();
		titlebox.getStyleClass().add("card2");
	}
}
