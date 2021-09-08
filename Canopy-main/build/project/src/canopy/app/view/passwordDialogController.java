package canopy.app.view;

import java.time.LocalTime;
import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.AppointmentType;
import canopy.app.model.Person;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class passwordDialogController {

	@FXML
	private PasswordField changefield;

	@FXML
	private PasswordField confirmfield;

	@FXML
	private Label password;
	
	@FXML
	private Button change;
	
	@FXML
	private Button cancel;
	
	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	private boolean okClicked = false;	
	Account account;
	boolean edit = false;
	private Stage dialogStage;
	MainApp mainApp;

    /**
     *
     * @param mainapp
     * @param account
     */
    public void Initialize(MainApp mainapp, Account account) {
		this.mainApp = mainapp;		
		this.account = account;
		change.setDisable(true);
		password.setText(account.getPassword());
		
		confirmfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!changefield.getText().trim().isEmpty() && !confirmfield.getText().trim().isEmpty()) {
					change.setDisable(false);
				}
			}

		});
		changefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!changefield.getText().trim().isEmpty() && !confirmfield.getText().trim().isEmpty()) {
					change.setDisable(false);
				}
			}

		});
		
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
     * @return
     */
    public boolean isOkClicked() {
		return okClicked;
	}

    /**
     *
     */
    public void handleChange() {
			coverpane.setTranslateX(dialogStage.getWidth());			
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(0);
			
			closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(!saved) {

					if(changefield.getText().equals(confirmfield.getText())) {
					
					account.setPassword(changefield.getText());

					mainApp.saveAccountDataToFile(mainApp.getFilePath(),false);
					mainApp.setVersion();

					okClicked = true;
					saved = true;
					dialogStage.close();}
					else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Alert alert = new Alert(AlertType.ERROR);
								Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
								stage.setAlwaysOnTop(true);
								
								alert.setTitle("Error");
								alert.setHeaderText("Changed Password and Confirmation do not Match");
								alert.showAndWait();
								dialogStage.close();
							}
						});

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

