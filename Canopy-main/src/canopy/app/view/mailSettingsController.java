package canopy.app.view;

import java.util.Collections;
import java.util.Comparator;

import canopy.app.MainApp;
import canopy.app.model.AppointmentType;
import canopy.app.model.MailingList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class mailSettingsController {

	@FXML
	private GridPane parentgrid;
	@FXML
	private AnchorPane parent;
	@FXML
	private TextField sendernamefield;
	@FXML
	private TextField senderemailfield;
	@FXML
	private CheckBox sendtopatientsbox;
	@FXML
	private CheckBox sendtostaffbox;
	
	private MainApp mainapp;

	/**
	 * Sets the parent class as mainapp
	 * 
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;

		if (mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 0) {
			sendtopatientsbox.disableProperty().set(false);
			sendtostaffbox.disableProperty().set(false);
			senderemailfield.disableProperty().set(false);
			sendernamefield.disableProperty().set(false);
		} else {
			sendtopatientsbox.disableProperty().set(true);
			sendtostaffbox.disableProperty().set(true);
			senderemailfield.disableProperty().set(true);
			sendernamefield.disableProperty().set(true);
		}

		senderemailfield.setText(mainapp.appPrefs.getAutomatedSenderEmail());
		sendernamefield.setText(mainapp.appPrefs.getAutomatedSenderName());
		sendtopatientsbox.selectedProperty().set(mainapp.appPrefs.getAutoEmailPatients());
		sendtostaffbox.selectedProperty().set(mainapp.appPrefs.getAutoEmailStaff());

	}

	/**
	 * Checks validity of fields and saves settings to preferences file
	 * 
	 */
	@FXML
	void save() {
		String errorMessage = "";
		if (senderemailfield.getText() == null || senderemailfield.getText().length() == 0) {
			errorMessage += "No valid Email\n";
		}
		if (sendernamefield.getText() == null || sendernamefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}

		if (errorMessage.length() != 0) {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(mainapp.getPrimaryStage());
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();
		} else {
			mainapp.appPrefs.setAutomatedSenderEmail(senderemailfield.getText().trim());
			mainapp.appPrefs.setAutomatedSenderName(sendernamefield.getText().trim());
			mainapp.appPrefs.setAutoEmailStaff(sendtostaffbox.selectedProperty().getValue());
			mainapp.appPrefs.setAutoEmailPatients(sendtopatientsbox.selectedProperty().getValue());

			Task<Void> loader = new Task<Void>() {
				@Override
				protected Void call() {
					mainapp.savePreferenceDataToFile(mainapp.getFilePath());
					return null;
				}
			};
			new Thread(loader).start();

		}
	}

	/**
	 *
	 */
	public void initialise() {

		AnchorPane.setBottomAnchor(parent, 0.0);
		
		//parent.prefWidthProperty().bind(parentgrid.widthProperty());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(0);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);
		dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));

		parent.setEffect(dropShadow);

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(700), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		playAfterPause(800, timeline);

	}

	/**
	 *
	 * @param millis
	 * @param timeline
	 */
	public void playAfterPause(int millis, Timeline timeline) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				timeline.play();
			}
		});
		new Thread(sleeper).start();
	}

	@FXML
	void ground() {

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		if (dropShadow != null) {

			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
			final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
			timeline.getKeyFrames().add(kf);

			timeline.playFromStart();
		}

	}

	@FXML
	void lift() {

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		if (dropShadow != null) {

			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
			final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
			timeline.getKeyFrames().add(kf);

			timeline.playFromStart();
		}

	}

}
