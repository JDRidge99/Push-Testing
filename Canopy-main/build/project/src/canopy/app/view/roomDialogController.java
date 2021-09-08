package canopy.app.view;

import canopy.app.MainApp;
import canopy.app.model.Building;
import canopy.app.model.Room;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author Andy
 */
public class roomDialogController {

	@FXML
	private TextField roomnamefield;

	@FXML
	private TextField addressfield;

	@FXML
	private TextField cityfield;
	
	@FXML
	private Button buildingbutton;
	
	@FXML
	private Label buildingname;
	
	@FXML
	private Label buildingaddress;

	@FXML
	private TextField postcodefield;

	@FXML
	private TextField buildingnamefield;

	@FXML
	private ComboBox<Building> buildingpicker;

	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	Room room;
	Building addbuilding;
	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainApp;
	boolean warningshown = false;
	boolean addnew = false;

    /**
     *
     * @param mainapp
     */
    public void initialise(MainApp mainapp) {
		this.mainApp = mainapp;

		buildingnamefield.setVisible(false);
		addressfield.setVisible(false);
		cityfield.setVisible(false);
		postcodefield.setVisible(false);

		buildingpicker.getItems().clear();
		buildingpicker.setItems(mainapp.getBuildings());
		if (!mainapp.getBuildings().isEmpty()) {
			buildingpicker.setValue(buildingpicker.getItems().get(0));
		}

		addbuilding = new Building();
		addbuilding.setName("Add Building");

		if (buildingpicker.getItems().isEmpty()) {
			addNew();
			buildingbutton.setVisible(false);
		}

		StringConverter<Building> buildingconverter = new StringConverter<Building>() {

			@Override
			public Building fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Building object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		};

		buildingpicker.converterProperty().set(buildingconverter);

	}

    /**
     *
     */
    @FXML
	public void addNew() {
		if (!addnew) {
			addnew = true;
			
			buildingpicker.setVisible(false);
			buildingname.setVisible(true);
			buildingaddress.setVisible(true);
			buildingnamefield.setVisible(true);
			addressfield.setVisible(true);
			cityfield.setVisible(true);
			postcodefield.setVisible(true);
			
			buildingbutton.setText("Existing Building");
		}
		else {
			addnew = false;
			
			buildingpicker.setVisible(true);
			buildingname.setVisible(false);
			buildingaddress.setVisible(false);
			buildingnamefield.setVisible(false);
			addressfield.setVisible(false);
			cityfield.setVisible(false);
			postcodefield.setVisible(false);
			

			buildingbutton.setText("+");
		}
	}

    /**
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
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
     * @param room
     */
    public void setRoom(Room room) {
		this.room = room;
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
    public void handleOk() {
		if (isInputValid()) {
			coverpane.setTranslateX(dialogStage.getWidth());
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(0);

			closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!saved) {
//						if (!mainApp.checkVersion()) { // If Checking version returns a changed file version, i.e saved data has changed
//							mainApp.LoadBuildingDataFromFile(mainApp.getFilePath());
//							mainApp.LoadRoomDataFromFile(mainApp.getFilePath());
//						}

						Room room = new Room("null", 1, 1); // Name, Building, ID
						
						room.setRoomName(roomnamefield.getText());

						if (!addnew) { // If a new building ISNT being added
							room.setBuilding(mainApp.getBuildings().indexOf(buildingpicker.getValue()));// Set the new room's building as the selected one

							System.out.println("Building being added is not new");

						} else {

							System.out.println("Building being added is new");

							// Check for Errors in Building fields
							String errorMessage = "";
							if (buildingnamefield.getText() == null || roomnamefield.getText().length() == 0) {
								errorMessage += "No Building Name Entered";
							}

							if (buildingnamefield.getText().contains("Add Building")) {
								errorMessage += "Building Name Cannot be 'Add Building'";
							}

							for (int i = 0; i < mainApp.getBuildings().size(); i++) {
								if (buildingnamefield.getText().equals(mainApp.getBuildings().get(i).getName())) {
									errorMessage += "A Building with the Name '" + buildingnamefield.getText() + "' Already Exists.";
								}
							}

							if (addressfield.getText() == null || roomnamefield.getText().length() == 0) {
								errorMessage += "No Address Entered";
							}

							if (cityfield.getText() == null || roomnamefield.getText().length() == 0) {
								errorMessage += "No City Entered";
							}

							if (postcodefield.getText() == null || roomnamefield.getText().length() == 0) {
								errorMessage += "No Post Code Entered";
							}

							if (errorMessage.length() != 0) { // If there IS an error, show it.
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
							} else { // If there are no errors
								Building newbuilding; // Create and initialise New building object
								newbuilding = new Building();
								newbuilding.setName(buildingnamefield.getText());
								newbuilding.setAddress(addressfield.getText());
								newbuilding.setCity(cityfield.getText());
								newbuilding.setPostcode(postcodefield.getText());
								mainApp.actuallyAddBuilding(newbuilding); // Add new building to base building list
								room.setBuilding(mainApp.getBuildings().indexOf(newbuilding)); // set room's building index correctly
							}
						}

						System.out.println("Room Building: " + mainApp.getBuildings().get(room.getBuilding()).getName());

						Integer roomnum = mainApp.appPrefs.getRoomID() + 1;
						room.setId(roomnum); // set room ID correctly
						mainApp.appPrefs.setRoomID(mainApp.appPrefs.getRoomID() + 1);

						mainApp.actuallyAddRoom(room); // Actually add room to base room list
						mainApp.setSelectedBuildingList(room.getBuilding()); // Set the selected building in the rooms list to the correct building
						mainApp.setVersion(); // Notify that save changes have been made

						saved = true;
						okClicked = true;
						dialogStage.close();
					}
				}
			});
			closeNav.play();
		}
	}

	private boolean isInputValid() {
		String errorMessage = "";
		boolean warnMessage = false;

		if (roomnamefield.getText() == null || roomnamefield.getText().length() == 0) {
			errorMessage += "No Room Name Given";
		}

		if (errorMessage.length() == 0) {

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

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
