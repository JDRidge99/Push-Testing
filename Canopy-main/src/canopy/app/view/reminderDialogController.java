package canopy.app.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.prefs.Preferences;

import javax.annotation.Generated;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Patient;
import canopy.app.model.Person;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class reminderDialogController {

	@FXML
	private CheckBox futurebox;

	@FXML
	private CheckBox pastbox;

	@FXML
	private CheckBox datesbox;

	@FXML
	private DatePicker fromdatepicker;

	@FXML
	private DatePicker todatepicker;

	private Patient patient;
	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainapp;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		futurebox.selectedProperty().set(true);

		futurebox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					pastbox.selectedProperty().set(false);
					datesbox.selectedProperty().set(false);
				}
			}
		});

		pastbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					futurebox.selectedProperty().set(false);
					datesbox.selectedProperty().set(false);
				}
			}
		});

		datesbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					pastbox.selectedProperty().set(false);
					futurebox.selectedProperty().set(false);
				}
			}
		});
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 *
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	boolean checkDate(LocalDate date) {
		boolean contained = false;
		if (futurebox.selectedProperty().get()) {
			if (date.isAfter(LocalDate.now())) {
				contained = true;
			}
		} else if (pastbox.selectedProperty().get()) {
			if (date.isBefore(LocalDate.now())) {
				contained = true;
			}
		} else if (datesbox.selectedProperty().get()) {
			if (date.isBefore(todatepicker.getValue()) && date.isAfter(fromdatepicker.getValue())) {
				contained = true;
			}
		}

		return contained;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {

			String patientemail = "";

			if (patient.getEmails() != null) {
				String[] emails = patient.getEmails().split(";");
				for (String email : emails) {
					patientemail += ";" + email;
				}
			}

			String subject = patient.getFirstName() + " " + patient.getLastName() + " Appointments: " + LocalDate.now().toString();
			subject = htmlEscape(subject);

			String body = patient.getFirstName() + " " + patient.getLastName() + " Appointments: " + LocalDate.now().toString() + "\n\n";

			// ---------------------------------- GENERATING BODY

			for (Appointment appointment : mainapp.getAppointments()) {
				if (appointment.getPatients() != null) { // catch all, but generaly checks that appointment isn't a meeting
					if (appointment.getPatients().contains(patient.getPatientNumber()) && checkDate(appointment.getDate())) {
						for (AppointmentType type : mainapp.getAppointmentTypes()) {
							if (type.getId() == appointment.getType()) {
								body += type.getName() + "\n";
								break;
							}
						}
						body += "APPOINTMENT: \nDate: " + appointment.getDate().toString() + "\n" + "Time: " + appointment.getTime().toString() + "\n Room: " + appointment.getRoom().getRoomName()
								+ "\n" + mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getAddress() + "\n" + mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getCity()
								+ "\n" + mainapp.getBuildings().get(appointment.getRoom().getBuilding()).getPostcode() + "\n\n";

					}
				}
			}

			body += "Summary generated using CANOPY system \n\n";

			// --------------------------------

			body = htmlEscape(body);

			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.MAIL)) {
					URI mailto;
					try {
						mailto = new URI("mailto:" + patientemail + "?subject=" + subject + "&body=" + body);
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

			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (datesbox.selectedProperty().get()) {
			if (todatepicker.valueProperty() == null) {
				errorMessage += "No valid end date\n";
			}
			if (fromdatepicker.valueProperty() == null) {
				errorMessage += "No valid start date\n";
			}
		}

		if (errorMessage.length() == 0) {
			System.out.println("Error Message Length is Zero");
			return true;
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

	public String htmlEscape(String s) {
		s = MainApp.htmlEscape(s);
		return s;
	}

}
