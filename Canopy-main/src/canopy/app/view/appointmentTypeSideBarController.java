package canopy.app.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.MailingList;
import canopy.app.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Andy
 */
public class appointmentTypeSideBarController {

	@FXML
	private Label namelabel;

	@FXML
	private Label pricelabel;

	@FXML
	private Label description;

	@FXML
	private Button editbutton;

	@FXML
	private Button removebutton;

	@FXML
	private AnchorPane parent;

	@FXML
	private ScrollPane scrollpane;

	AppointmentType currenttype;

	private MainApp mainapp;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3 || mainapp.getCurrentAccount().getPermission() == 6) {
			editbutton.setVisible(false);
			removebutton.setManaged(false);
		}

		scrollpane.setFitToWidth(true);
	}

	/**
	 *
	 * @param type
	 */
	public void setType(AppointmentType type) {
		if (type.getId() == 9999) {
			removebutton.setVisible(false);
		} else {
			removebutton.setVisible(true);
		}
		this.currenttype = type;
		namelabel.setText(type.getName());
		pricelabel.setText(type.getCurrency() + " " + String.valueOf(type.getPrice()));
		description.setText(type.getDescription());
	}

	@FXML
	void remove() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Appointment Type?");
		alert.setHeaderText("Do you wish to permanently delete this Appointment Type and all associated appointments?");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedAppointmentTypes() + ";" + String.valueOf(currenttype.getId());
			mainapp.appPrefs.setDeletedAppointmentTypes(deletedstring);
			mainapp.removeAppointmentType(currenttype);

			ObservableList<Appointment> atoremove = FXCollections.observableArrayList();
			mainapp.getAppointments().stream().filter((appointment) -> (appointment.getAppType() == currenttype.getId())).forEachOrdered((appointment) -> {
				atoremove.add(appointment);
			});
			mainapp.getAppointments().removeAll(atoremove);

			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
			mainapp.setVersion();

		} else {

		}
	}

	@FXML
	void raiseall() {
	}

	@FXML
	void lowerall() {
	}

}
