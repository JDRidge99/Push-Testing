package canopy.app.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.MailingList;
import canopy.app.model.Note;
import canopy.app.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author Andy
 */
public class sideBarController {

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String htmlEscape(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("<br>");
				break;
			case ' ':
				builder.append("%20");
				break;
			// We need Tab support here, because we print StackTraces as HTML
			case '\t':
				builder.append("&nbsp; &nbsp; &nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}

	@FXML
	private Label timelabel;

	@FXML
	private Label datelabel;

	@FXML
	private Label typelabel;

	@FXML
	private Label durationlabel;

	@FXML
	private Label descriptionlabel;

	@FXML
	private Label pricelabel;

	@FXML
	private Label paidlabel;

	@FXML
	private Label roomnamelabel;

	@FXML
	private Label addresslabel;

	@FXML
	private Label citylabel;

	@FXML
	private Label postalcodelabel;

	@FXML
	private Label paidheading;

	@FXML
	private Button paybutton;

	@FXML
	private Button removebutton;

	@FXML
	private ScrollPane scrollpane;

	@FXML
	private AnchorPane parent;

	@FXML
	private Label patientheader;

	@FXML
	private VBox patientbox;

	@FXML
	private VBox staffbox;

	@FXML
	private ScrollPane patientlist;

	Appointment appointment;
	String currdeletedstring = "";
	MainApp mainapp;

	ObservableList<Patient> patients = FXCollections.observableArrayList();
	ObservableList<Account> staff = FXCollections.observableArrayList();

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;

		parent.getChildren().stream().map((node) -> {
			node.setVisible(false);
			return node;
		}).forEachOrdered((node) -> {
			node.setManaged(false);
		});

		scrollpane.setFitToWidth(true);
	}

	/**
	 *
	 * @param appointment
	 */
	public void setAppointment(Appointment appointment) {

		parent.getChildren().stream().map((node) -> {
			node.setVisible(true);
			return node;
		}).forEachOrdered((node) -> {
			node.setManaged(true);
		});
		this.appointment = appointment;

		if (appointment.getAppType() != 9999) {

			patientbox.setVisible(true); // Sets all patient related items visible (invisible from meetings type)
			patientheader.setVisible(true);

			switch (mainapp.getCurrentAccount().getPermission()) {
			case 0: // Master Admin
				paidlabel.setVisible(true);
				paybutton.setVisible(true);
				paidheading.setVisible(true);
				removebutton.setVisible(true);
				patientbox.setVisible(true);
				patientheader.setVisible(true);
				break;
			case 1: // Admin
				paidlabel.setVisible(true);
				paybutton.setVisible(true);
				paidheading.setVisible(true);
				removebutton.setVisible(true);
				patientbox.setVisible(true);
				patientheader.setVisible(true);
				break;
			case 2: // Self Timetabling Staff
				paidlabel.setVisible(false);
				paybutton.setVisible(false);
				paidheading.setVisible(false);
				break;
			case 3: // Observing Staff
				paidlabel.setVisible(false);
				paybutton.setVisible(false);
				paidheading.setVisible(false);
				removebutton.setVisible(false);
				break;
			case 5: // Non-Clinial Master Staff // Jumps straight to 5 as current account will never be 4.
				patientbox.setVisible(false);
				patientheader.setVisible(false);
				break;
			case 6: // Non-Clinical Staff
				patientbox.setVisible(false);
				patientheader.setVisible(false);
				break;

			}

			timelabel.setText(appointment.getTime().toString());
			LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
			datelabel.setText(dateStringConverter.toString(appointment.getDate()));
			durationlabel.setText(String.valueOf((int) appointment.getDuration() / 60) + "H " + String.valueOf(appointment.getDuration() % 60) + "M");
			descriptionlabel.setText(appointment.getDescription());
			mainapp.getAppointmentTypes().stream().filter((type) -> (type.getId() == appointment.getAppType())).map((type) -> {
				typelabel.setText(type.getName());
				return type;
			}).forEachOrdered((type) -> {
				if (appointment.getPatients().size() > 1) {
					pricelabel.setText(appointment.getCurrency() + " " + String.valueOf(appointment.getPrice()) + " Total,  " + appointment.getCurrency() + " "
							+ String.valueOf(appointment.getPrice() / appointment.getPatients().size()) + " per Patient ");
				} else {
					pricelabel.setText(appointment.getCurrency() + " " + String.valueOf(appointment.getPrice()));
				}
			});

			patientbox.getChildren().clear();
			staffbox.getChildren().clear();

			for (Patient patient : mainapp.getPatients()) {
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

					patientpane.getStyleClass().add("appointment");

					Button info = new Button();
					info.setText("i");
					info.getStyleClass().add("buttons");
					info.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							patientdetails(patient);
						}
					});

					patientlab.setText(patient.getFirstName().substring(0, 1) + "." + patient.getLastName());
					patientlab.getStyleClass().add("label-list");
					containerbox.getChildren().addAll(patientlab, info);

					patientpane.getChildren().add(containerbox);

					containerbox.prefWidthProperty().bind(patientpane.widthProperty());
					containerbox.maxHeightProperty().bind(patientpane.heightProperty());
					// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
					// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

					patientbox.getChildren().add(patientpane);
				}
			}

			for (Account account : mainapp.getAccounts()) {
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
					Label patientlab = new Label();
					patientlab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

					staffpane.getStyleClass().add("appointment");

					Button info = new Button();
					info.setText("i");
					info.getStyleClass().add("buttons");
					info.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							staffdetails(account);
						}
					});

					patientlab.setText(account.getTitle() + "." + account.getLastName());
					patientlab.getStyleClass().add("label-list");
					containerbox.getChildren().addAll(patientlab, info);

					staffpane.getChildren().addAll(containerbox);

					containerbox.prefWidthProperty().bind(staffpane.widthProperty());
					containerbox.maxHeightProperty().bind(staffpane.heightProperty());
					// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
					// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

					staffbox.getChildren().add(staffpane);
				}
			}

			if (appointment.getPaid()) {
				paybutton.setText("UNDO");
				paidlabel.setText("Y");
			} else {
				paidlabel.setText("N");
				paybutton.setText("PAY");
			}
			roomnamelabel.setText(appointment.getRoom().getRoomName());
			addresslabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getAddress());
			citylabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getCity());
			postalcodelabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getPostcode());

		} else { // Appointment is Meeting Type

			patientbox.setVisible(false); // Sets all patient related items invisible
			patientheader.setVisible(false);

			switch (mainapp.getCurrentAccount().getPermission()) {
			case 0: // Master Admin
				removebutton.setVisible(true);
				break;
			case 1: // Admin
				removebutton.setVisible(true);
				break;
			case 2: // Self Timetabling Staff
				break;
			case 3: // Observing Staff
				paidlabel.setVisible(false);
				paybutton.setVisible(false);
				paidheading.setVisible(false);
				removebutton.setVisible(false);
				break;
			case 5: // Non-Clinial Master Staff // Jumps straight to 5 as current account will never be 4.
				patientbox.setVisible(false);
				patientheader.setVisible(false);
				break;
			case 6: // Non-Clinical Staff
				patientbox.setVisible(false);
				patientheader.setVisible(false);
				break;

			}

			timelabel.setText(appointment.getTime().toString());
			LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
			datelabel.setText(dateStringConverter.toString(appointment.getDate()));
			durationlabel.setText(String.valueOf((int) appointment.getDuration() / 60) + "H " + String.valueOf(appointment.getDuration() % 60) + "M");
			descriptionlabel.setText(appointment.getDescription());
			typelabel.setText("Meeting");

			staffbox.getChildren().clear();

			for (Account account : mainapp.getAccounts()) {
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
					Label patientlab = new Label();
					patientlab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

					staffpane.getStyleClass().add("appointment");

					Button info = new Button();
					info.setText("i");
					info.getStyleClass().add("buttons");
					info.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							staffdetails(account);
						}
					});

					patientlab.setText(account.getTitle() + "." + account.getLastName());
					patientlab.getStyleClass().add("label-list");
					containerbox.getChildren().addAll(patientlab, info);

					staffpane.getChildren().add(containerbox);

					containerbox.prefWidthProperty().bind(staffpane.widthProperty());
					containerbox.maxHeightProperty().bind(staffpane.heightProperty());
					// patientpane.prefWidthProperty().bind(scrllparent.widthProperty());
					// patientpane.minWidthProperty().bind(scrllparent.widthProperty());

					staffbox.getChildren().add(staffpane);
				}
			}

			roomnamelabel.setText(appointment.getRoom().getRoomName());
			addresslabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getAddress());
			citylabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getCity());
			postalcodelabel.setText(mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getPostcode());
		}

	}

	/**
	 *
	 */
	@FXML
	public void pay() {
		if (!appointment.getPaid()) {
			paybutton.setText("UNDO");
			appointment.setPaid(true);
			paidlabel.setText("Y");
		} else {
			paybutton.setText("PAY");
			appointment.setPaid(false);
			paidlabel.setText("N");
		}
		mainapp.refreshcalendar();
		mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
	}

	void emailstaff(Account account) {

		String staffemail = account.getEmail();

		String subject = typelabel.getText() + " " + datelabel.getText() + " " + timelabel.getText(); // Set subject

		subject = htmlEscape(subject); // Escape subject for html

		if (Desktop.isDesktopSupported()) { // If desktop is supported, open the created email with the default mail app
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				try {
					mailto = new URI("mailto:" + staffemail + "?subject=" + subject);
					desktop.mail(mailto);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	void emailpatient(Patient epatient) {
		String patientemail = epatient.getEmails();

		String subject = epatient.getFirstName() + " " + epatient.getLastName() + " " + typelabel.getText() + " " + datelabel.getText() + " " + timelabel.getText(); // generate subject for email

		subject = htmlEscape(subject); // html escape subject for email

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				try {
					mailto = new URI("mailto:" + patientemail + "?subject=" + subject);
					desktop.mail(mailto);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	void staffdetails(Account account) {
		mainapp.patientScreenClick();
		mainapp.setSelectedAccount(account);
	}

	void patientdetails(Patient patient) {
		mainapp.patientScreenClick();
		mainapp.setSelectedPatient(patient);

	}
		
	void removefromlist(Appointment app) {
		appointment = app;
		remove();
	}

	@FXML
	void remove() {

		ObservableList<Appointment> recurringapps = FXCollections.observableArrayList();

		for (Appointment checkapp : mainapp.getAppointments()) {
			if (checkapp.getPatient().equals(appointment.getPatient()) && checkapp.getTime().equals(appointment.getTime()) && checkapp.getType().equals(appointment.getType())) {
				System.out.println("Time match: " + checkapp.getTime().toString() + " and " + appointment.getTime().toString());
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
			alert.setContentText("Would you like to delete just this appointment, this appointment and all similar appointments after the selected, or all recorded similar appointments?");

			ButtonType buttonTypeOne = new ButtonType("This Appointment");

			ButtonType buttonTypeTwo = new ButtonType("Future Appointments");

			ButtonType buttonTypeThree = new ButtonType("All Appointments");

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				justDelete(appointment);
				currdeletedstring = mainapp.appPrefs.getDeletedAppointments();
				save();
			}
			if (result.get() == buttonTypeTwo) {
				currdeletedstring = mainapp.appPrefs.getDeletedAppointments();
				for (Appointment editapp : recurringapps) {
					if (editapp.getDate().isAfter(appointment.getDate())) {
						justDelete(editapp);
					}
				}
				save();
			}
			if (result.get() == buttonTypeThree) {
				currdeletedstring = mainapp.appPrefs.getDeletedAppointments();
				for (Appointment editapp : recurringapps) {
					justDelete(editapp);
				}
				save();
			}

		}

		else {
			delete(appointment);
		}

	}

	void justDelete(Appointment app) {
		currdeletedstring += ";" + String.valueOf(app.getID());
		mainapp.removeAppointment(app);
	}

	void save() {
		mainapp.appPrefs.setDeletedAppointments(currdeletedstring);
		mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
		mainapp.savePreferenceDataToFile(mainapp.getFilePath());
		mainapp.setVersion();
	}

	void delete(Appointment app) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Appointment?");
		alert.setHeaderText("Do you wish to permanently delete this appointment?");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedAppointments() + ";" + String.valueOf(app.getID());
			mainapp.appPrefs.setDeletedAppointments(deletedstring);
			mainapp.removeAppointment(app);
			
			if (mainapp.appPrefs.getAutoEmailPatients()) { // If the preferences say to auto email patients, try to do so.
				mainapp.sendAppointmentInvitePatients(appointment, 2);
			}
			if (mainapp.appPrefs.getAutoEmailStaff()) { // If the preferences say to auto email staff, try to do so.
				mainapp.sendAppointmentInviteStaff(appointment, 2);
			}
			
			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.setVersion();

		} else {

		}
	}

	@FXML
	void edit() {
		mainapp.editAppointment(appointment);
	}

	@FXML
	void raiseall() {
		mainapp.raisecalendar();
	}

	@FXML
	void lowerall() {
		mainapp.lowercalendar();
	}

}
