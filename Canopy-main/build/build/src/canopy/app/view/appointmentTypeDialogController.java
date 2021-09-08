package canopy.app.view;

import java.time.LocalTime;
import java.util.prefs.Preferences;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.AppointmentType;
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
import javafx.scene.control.TextArea;
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
public class appointmentTypeDialogController {

	@FXML
	private TextField namefield;

	@FXML
	private TextField pricefield;
	
	@FXML
	private TextField durationfield;
	
	@FXML
	private TextField currencyfield;

	@FXML
	private TextArea descriptionfield;
	
	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	private boolean okClicked = false;	
	AppointmentType appointmentType;
	boolean edit = false;
	private Stage dialogStage;
	MainApp mainApp;

    /**
     *
     * @param mainapp
     */
    public void setMainApp(MainApp mainapp) {
		this.mainApp = mainapp;
		
		currencyfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!currencyfield.getSelectedText().isEmpty()) {
					currencyfield.replaceSelection("");
		        }
				if (!"$€¢£¥".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}
				if(currencyfield.getText().length()>0) {
					keyEvent.consume();
				}
			}

		});
		
		pricefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!pricefield.getSelectedText().isEmpty()) {
					pricefield.replaceSelection("");
		        }
				if (!"0123456789.".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}
				if(pricefield.getText().contains(".") && keyEvent.getCharacter().equals(".")) {
					keyEvent.consume();
				}
			}
		});
		
		durationfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!durationfield.getSelectedText().isEmpty()) {
		        	durationfield.replaceSelection("");
		        }
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				}

			}

		});

		
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		currencyfield.setText(prefs.get("currency", "£"));
		
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
     * @param type
     */
    public void setType(AppointmentType type) {
		this.appointmentType = type;
	}

    /**
     *
     */
    public void setedit() {
		edit = true;
		namefield.setText(appointmentType.getName());
		pricefield.setText(String.valueOf(appointmentType.getPrice()));
		descriptionfield.setText(appointmentType.getDescription());
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
					if(!saved) {
						
						Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

					prefs.put("currency", currencyfield.getText());		

					appointmentType.setName(namefield.getText());
					appointmentType.setPrice(Double.valueOf(pricefield.getText()));
					appointmentType.setDescription(descriptionfield.getText());
					appointmentType.setCurrency(currencyfield.getText());
					appointmentType.setDuration(Integer.valueOf(durationfield.getText()));
					
					Integer appointmenttypeId = mainApp.appPrefs.getAppTypeID() + 1;
					appointmentType.setId(appointmenttypeId);
					mainApp.appPrefs.setAppTypeID(mainApp.appPrefs.getAppTypeID() + 1);

					mainApp.actuallyAddAppointmentType(appointmentType, edit);
					mainApp.setVersion();

					okClicked = true;
					saved = true;
					dialogStage.close();
					}
				}
			});
			
			closeNav.play();
			

		}
	}

	private boolean isInputValid() {
		String errorMessage = "";
		if (namefield.getText() == null || namefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}
		if (pricefield.getText() == null || pricefield.getText().length() == 0) {
			errorMessage += "No valid Price\n";
		}
		try {
			Double.valueOf(pricefield.getText());
		}
		catch (Exception e) {
			errorMessage += "No valid Price\n";
		}
		if (currencyfield.getText() == null || currencyfield.getText().length() == 0) {
			errorMessage += "No valid Currency\n";
		}
		if (descriptionfield.getText() == null || descriptionfield.getText().length() == 0) {
			errorMessage += "No valid Description\n";
		}
		if (durationfield.getText() == null || durationfield.getText().length() == 0) {
			durationfield.setText("30");
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

