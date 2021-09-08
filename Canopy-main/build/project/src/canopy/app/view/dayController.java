package canopy.app.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Patient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 *
 * @author Andy
 */
public class dayController {

	@FXML
	private Label dayname;
	@FXML
	private Label date;
	@FXML
	private GridPane schedule;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane superparent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private AnchorPane daynamepane;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private MainApp mainapp;

	calendarController parentcontroller;
	LocalDate date2;

	/**
	 *
	 * @param parentcontroller
	 */
	public void setParentController(calendarController parentcontroller) {

		this.parentcontroller = parentcontroller;

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.prefWidthProperty().bind(parent.widthProperty());
		schedule.prefWidthProperty().bind(scrllparent.widthProperty());
	}
	
	public void setavaliable(boolean avaliable) {
		if(!avaliable) {
			daynamepane.setStyle(" -fx-background-color: d7d7d7; -fx-background-radius: 0; -fx-padding: 20;");
			addbutton.setVisible(false);
			addbutton.setManaged(false);
		}
		else {
			daynamepane.setStyle(" -fx-background-color: #b7d373; -fx-background-radius: 0; -fx-padding: 20;");

			if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 2) {
				addbutton.setVisible(true);
				addbutton.setManaged(true);
			}
		}
		

	}

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		if (mainapp.getCurrentAccount().getPermission() != 0 && mainapp.getCurrentAccount().getPermission() != 1 && mainapp.getCurrentAccount().getPermission() != 2) {
			addbutton.setVisible(false);
			addbutton.setManaged(false);
		}
		if (mainapp.getSmallText()) {
			superparent.setMinWidth(80);
			superparent.setPrefWidth(130);
			superparent.setMaxWidth(130);
		} else {
			superparent.setMinWidth(130);
			superparent.setPrefWidth(180);
			superparent.setMaxWidth(180);
		}

		mainapp.SmallTextProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					superparent.setMinWidth(80);
					superparent.setPrefWidth(130);
					superparent.setMaxWidth(130);
				} else {
					superparent.setMinWidth(130);
					superparent.setPrefWidth(180);
					superparent.setMaxWidth(180);
				}

			}
		});

	}

	/**
	 *
	 * @param name
	 */
	public void setDayName(String name) {
		dayname.setText(name);
	}

	/**
	 *
	 * @param name
	 * @param givendate
	 */
	public void setDate(String name, LocalDate givendate) {
		date.setText(name);
		date2 = givendate;
		if (date2.equals(LocalDate.now())) {
			if (mainapp.getSmallText()) {
				daynamepane.setStyle(" -fx-background-color: #FFBA08; -fx-background-radius: 0; -fx-padding: 10;");
			} else {
				daynamepane.setStyle(" -fx-background-color: #FFBA08; -fx-background-radius: 0; -fx-padding: 20;");
			}
		}

	}

	/**
	 *
	 * @param pos
	 */
	public void addRow(int pos) {
		Pane fillpane = new Pane();
		schedule.addRow(pos, fillpane);
	}

	/**
	 *
	 * @param appointment
	 * @param pos
	 */
	public void addAppointment(Appointment appointment, int pos) {

		AnchorPane appointmentpane = new AnchorPane();
		HBox containerbox = new HBox();
		VBox rightbox = new VBox();
		VBox mainbox = new VBox();

		appointmentpane.setMinWidth(100);
		appointmentpane.setPrefWidth(150);
		containerbox.prefWidthProperty().bind(appointmentpane.widthProperty());
		containerbox.setAlignment(Pos.CENTER);
		Glow glow = new Glow(0);

		if (appointment.getType() != 2) {
			appointmentpane.setEffect(glow);
			appointmentpane.setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0.2);

				}
			});
		}

		Insets insets = new Insets(4, 4, 4, 4);
		containerbox.setPadding(insets);
		HBox.setHgrow(containerbox, Priority.ALWAYS);
		rightbox.setAlignment(Pos.CENTER_LEFT);
		Label timelab = new Label();
		timelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		Label roomlab = new Label();
		Label descriptionlab = new Label();
		Label durationlab = new Label();
		// Label stafflab = new Label();
		// Label patientlab = new Label();
		switch (appointment.getType()) {
		case 0:
			appointmentpane.getStyleClass().add("appointment");

			appointmentpane.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0);
				}
			});
			appointmentpane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.setRightPlannerPaneContents(appointment);
				}
			});

			timelab.setText(appointment.getTime().toString() + " | ");
			roomlab.setText(appointment.getRoom().getRoomName());
			for (AppointmentType type : mainapp.getAppointmentTypes()) {
				if (type.getId() == appointment.getAppType()) {
					descriptionlab.setText(type.getName());
					break;
				}
			}

			// for(Patient patient:mainapp.getPatients()) {
			// if(patient.getPatientNumber() == appointment.getPatient()) {
			// patientlab.setText(patient.getFirstName() + " " + patient.getLastName());
			// break;
			// }
			// }
			//
			// for(Account account:mainapp.getAccounts()) {
			// if(account.getStaffNumber() == appointment.getStaffMember()) {
			// stafflab.setText(account.getFirstName() + " " + account.getLastName());
			// break;
			// }
			// }

			durationlab.setText(String.valueOf(appointment.getDuration()) + " MINS");
			timelab.getStyleClass().add("label-list-big");
			roomlab.getStyleClass().add("label-list");
			durationlab.getStyleClass().add("label-list");
			descriptionlab.getStyleClass().add("label-list-body");
			// stafflab.getStyleClass().add("label-list");
			// patientlab.getStyleClass().add("label-list");

			descriptionlab.setWrapText(true);
			durationlab.setWrapText(true);
			roomlab.setWrapText(true);

			rightbox.getChildren().addAll(roomlab, durationlab);
			containerbox.getChildren().addAll(timelab, rightbox);
			// mainbox.getChildren().addAll(containerbox);
			appointmentpane.getChildren().add(containerbox);

			if ((appointment.getDate().equals(LocalDate.now()) && appointment.getTime().plusMinutes(appointment.getDuration()).isBefore(LocalTime.now()))
					|| appointment.getDate().isBefore(LocalDate.now())) {
				appointmentpane.getStyleClass().add("appointment-past");
			}

			else if (appointment.getDate().equals(LocalDate.now()) && appointment.getTime().plusMinutes(appointment.getDuration()).isAfter(LocalTime.now())) {// TIMER TO MODIFY PROPERTIES BASED ON
																																								// APPOINTMENT BEING PAST OR CURRENT

				Timer timer = new Timer();

				timer.schedule(new TimerTask() {
					public void run() {
						if (LocalTime.now().isAfter(appointment.getTime().plusMinutes(appointment.getDuration()))) {
							appointmentpane.getStyleClass().add("appointment-past");
						}
						if (LocalTime.now().isAfter(appointment.getTime()) && LocalTime.now().isBefore(appointment.getTime().plusMinutes(appointment.getDuration()))) {
							appointmentpane.getStyleClass().add("appointment-current");
							timelab.getStyleClass().add("label-list-big-white");
							roomlab.getStyleClass().add("label-list-white");
							durationlab.getStyleClass().add("label-list-white");
							descriptionlab.getStyleClass().add("label-list-body-white");
							// stafflab.getStyleClass().add("label-list-white");
							// patientlab.getStyleClass().add("label-list-white");
						}
					}
				}, 0, 60 * 1000);
			}

			break;

		case 2:
			appointmentpane.setStyle("-fx-background-color: transparent; -fx-background-insets: 10;-fx-padding: 3;");

			descriptionlab.setText(appointment.getDescription());
			
			String style;
			
			Image clock = new Image("/canopy/app/view/miniclock.png");

			ImageView clockview = new ImageView(clock);
			
			if (mainapp.getSmallText()) {
				style = "    -fx-font-size: 10pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: #c4c4c4;\r\n" + "    -fx-opacity: 1;\r\n" + "    -fx-padding: 1;";
				clockview.setScaleX(0.3);
				clockview.setScaleY(0.3);
			} else {
				style = "    -fx-font-size: 15pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: #c4c4c4;\r\n" + "    -fx-opacity: 1;\r\n" + "    -fx-padding: 1;";
				clockview.setScaleX(0.5);
				clockview.setScaleY(0.5);
			}


			descriptionlab.setStyle(style);



			containerbox.getChildren().addAll(clockview, descriptionlab);
			mainbox.getChildren().addAll(containerbox);
			appointmentpane.getChildren().add(mainbox);

			break;

		}

		containerbox.prefWidthProperty().bind(appointmentpane.widthProperty());
		containerbox.prefHeightProperty().bind(appointmentpane.heightProperty());
		// mainbox.prefWidthProperty().bind(appointmentpane.widthProperty());
		// mainbox.prefHeightProperty().bind(appointmentpane.heightProperty());

		schedule.add(appointmentpane, 0, pos);

	}

	/**
	 *
	 */
	public void addnewAppointment() {
		parentcontroller.addNewAppointment(date2, null);
	}

	/**
	 *
	 */
	@FXML
	public void ground() {
		parentcontroller.groundDay(superparent);
	}

	/**
	 *
	 */
	@FXML
	public void lift() {
		parentcontroller.liftDay(superparent);
	}

}
