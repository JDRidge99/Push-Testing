package canopy.app.view;

import canopy.app.MainApp;
import canopy.app.model.MailingList;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
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
public class mailListsDialogController {

	@FXML
	private TextField namefield;

	@FXML
	private TextArea description;
	
	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	MailingList list;
	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainapp;
	boolean warningshown = false;

    /**
     *
     * @param mainapp
     */
    public void initialise(MainApp mainapp) {
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
		coverpane.setCache(true);
		coverpane.setCacheShape(true);
		coverpane.setCacheHint(CacheHint.SPEED);
	}

    /**
     *
     * @param list
     */
    public void setMailList(MailingList list) {
		this.list = list;
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
				if(!saved) {

		String errorMessage = "";
		if (namefield.getText() == null || namefield.getText().length() == 0) {
			errorMessage += "No Mailing List Name Entered";
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
			MailingList mailingList = new MailingList(namefield.getText(), "", description.getText());

			int maillistnum = mainapp.appPrefs.getMailID() + 1;
			mailingList.setId(maillistnum);
			mainapp.appPrefs.setMailID(mainapp.appPrefs.getMailID() + 1);

			mainapp.actuallyAddMailList(mailingList);
			mainapp.setVersion();
			
			saved=true;
			okClicked = true;
			dialogStage.close();
		}}}});closeNav.play();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
