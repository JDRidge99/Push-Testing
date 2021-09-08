package canopy.app.view;

import canopy.app.MainApp;
import canopy.app.model.MailingList;
import canopy.app.model.PersonType;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class personTypeDialogController {

	@FXML
	private TextField namefield;

	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	PersonType personType;
	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainapp;
	boolean warningshown = false;

    /**
     *
     * @param mainapp
     */
    public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
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
	}

    /**
     *
     * @param type
     */
    public void setPersonType(PersonType type) {
		this.personType = type;
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

		coverpane.setTranslateX(dialogStage.getWidth());
		TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
		closeNav.setToX(0);

		closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!saved) {

					String errorMessage = "";
					if (namefield.getText() == null || namefield.getText().length() == 0) {
						errorMessage += "No Person Type Name Entered";
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
					} else {
						personType.setTypeName(namefield.getText());

						mainapp.actuallyAddPersonType(personType);
						mainapp.setVersion();

						saved=true;
						okClicked = true;
						dialogStage.close();
					}
				}
			}
		});
		closeNav.play();

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
