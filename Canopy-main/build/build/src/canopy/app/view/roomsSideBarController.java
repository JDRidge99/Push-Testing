package canopy.app.view;

import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Appointment;
import canopy.app.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Andy
 */
public class roomsSideBarController {

	@FXML
	private Label roomname;

	@FXML
	private Label buildingname;

	@FXML
	private Label addresslabel;

	@FXML
	private Label citylabel;

	@FXML
	private Label postalcodelabel;

	private Room currentroom;

	private MainApp mainapp;
	
	@FXML
	private AnchorPane parent;
	
	@FXML 
	private ScrollPane scrollpane;

    /**
     *
     * @param mainapp
     */
    public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		scrollpane.setFitToWidth(true);
	}

    /**
     *
     * @param room
     */
    public void setRoom(Room room) {
		this.currentroom = room;

		roomname.setText(room.getRoomName());
		buildingname.setText(mainapp.getBuildings().get(room.getBuilding()).getName());
		addresslabel.setText(mainapp.getBuildings().get(room.getBuilding()).getAddress());
		citylabel.setText(mainapp.getBuildings().get(room.getBuilding()).getCity());
		postalcodelabel.setText(mainapp.getBuildings().get(room.getBuilding()).getPostcode());
		// EMAILS

		String style = "     -fx-font-size: 11pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n"
				+ "    -fx-text-fill: black;\r\n" + "    -fx-opacity: 0.6;";

	}
	
	@FXML	
	void remove() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");			
	
		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Room?");
		alert.setHeaderText("Do you wish to permanently delete this Room and all associated appointments?");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedRooms() + ";"
					+ String.valueOf(currentroom.getId());
			mainapp.appPrefs.setDeletedRooms(deletedstring);
			mainapp.removeRoom(currentroom);
			
			ObservableList<Appointment> atoremove = FXCollections.observableArrayList();
                        mainapp.getAppointments().stream().filter((appointment) -> (appointment.getRoom().getId() == currentroom.getId())).forEachOrdered((appointment) -> {
                            atoremove.add(appointment);
                    });
			mainapp.getAppointments().removeAll(atoremove);		

			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(),false);
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
