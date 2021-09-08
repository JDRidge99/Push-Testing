package canopy.app.view;

import java.time.LocalTime;
import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Person;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class personDialogController {

	@FXML
	private TextField firstnamefield;

	@FXML
	private TextField lastnamefield;

	@FXML
	private TextField streetfield;

	@FXML
	private TextField cityfield;

	@FXML
	private TextField postcodefield;

	@FXML
	private TextField emailfield;

	@FXML
	private TextField numberfield;

	@FXML
	private TextField descriptionfield;

	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	private boolean okClicked = false;
	Integer personType;
	boolean edit = false;
	private Stage dialogStage;
	private Person person;
	MainApp mainApp;

    /**
     *
     * @param mainapp
     */
    public void setMainApp(MainApp mainapp) {
		this.mainApp = mainapp;
		firstnamefield.requestFocus();
		// 0 - Master Admin: Everything, password unblocked
		// 1 - Admin: Everything except new staff & edit staff, password blocked
		// 2 - Self Timetabling Staff: can see everything except some staff data, and can only add new appointments for themselves
		// 3 - Observing Staff: can see but not touch
		// 4 - Non interacting Staff: no log in details, simply a placeholder
		// 5 - Non clinical Master Staff: can observe and modify everything non patient related
		// 6 - Non clinical Staff: can observer everything not patient related
		
		
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
     */
    public void setedit() {
		edit = true;
		firstnamefield.setText(person.getFirstName());
		lastnamefield.setText(person.getLastName());
		emailfield.setText(person.getEmail());
		numberfield.setText(person.getNumber());
		streetfield.setText(person.getStreet());
		cityfield.setText(person.getCity());
		postcodefield.setText(person.getPostalCode());
		descriptionfield.setText(person.getDescription());

	}

    /**
     *
     * @param type
     */
    public void setPersonType(Integer type) {
		personType = type;
	}

    /**
     *
     * @param person
     */
    public void setPerson(Person person) {
		this.person = person;
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
						
//						if(mainApp.PeopleSaved()) {
//						mainApp.LoadPeopleDataFromFile(mainApp.getFilePath());}

						person.setFirstName(firstnamefield.getText());
						person.setLastName(lastnamefield.getText());
						if(!emailfield.getText().trim().equals("")) {
						person.setEmail(emailfield.getText());}
						if(!numberfield.getText().trim().equals("")) {
						person.setNumber(numberfield.getText());}
						person.setStreet(streetfield.getText());
						person.setCity(cityfield.getText());
						person.setPostalCode(postcodefield.getText());
						person.setDescription(descriptionfield.getText());
						person.setPersonType(personType);
						
						if(!edit) {
						mainApp.setPersonID();
						Integer personnum = mainApp.appPrefs.getPersonID() + 1;
						person.setId(personnum);
						mainApp.appPrefs.setPersonID(personnum + 1);}

						mainApp.actuallyAddPerson(person, edit);
						mainApp.setVersion();
						
						saved=true;
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
		if (firstnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}
		if (lastnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}

		if (errorMessage.length() != 0) {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");			
		
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		} else {
			return true;
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	void hourfinished() {

	}

}
