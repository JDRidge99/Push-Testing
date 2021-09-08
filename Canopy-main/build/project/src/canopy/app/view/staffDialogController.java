package canopy.app.view;

import java.time.LocalTime;
import canopy.app.MainApp;
import canopy.app.model.Account;
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
public class staffDialogController {

	@FXML
	private TextField titlefield;

	@FXML
	private TextField firstnamefield;

	@FXML
	private TextField lastnamefield;

	@FXML
	private TextField usernamefield;

	@FXML
	private TextField passwordfield;

	@FXML
	private TextField rolefield;

	@FXML
	private TextField starthourfield;

	@FXML
	private TextField startminutefield;

	@FXML
	private TextField endhourfield;

	@FXML
	private TextField endminutefield;

	@FXML
	private TextField streetfield;

	@FXML
	private TextField cityfield;

	@FXML
	private TextField countyfield;

	@FXML
	private TextField postcodefield;

	@FXML
	private TextField emailfield;

	@FXML
	private TextField numberfield;

	@FXML
	private Label passwordlab;

	@FXML
	private ComboBox<String> permissionpicker;

	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	Integer permissionlevel = 3;
	private boolean okClicked = false;
	boolean edit = false;
	private Stage dialogStage;
	private Account account;
	MainApp mainApp;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainApp = mainapp;
		firstnamefield.requestFocus();

		// IS THIS NEEDED?
		if (mainApp.getCurrentAccount().getPermission() == 0) {
			passwordlab.setManaged(true);
			passwordfield.setManaged(true);
			passwordlab.setVisible(true);
			passwordfield.setVisible(true);
		} else {
			passwordlab.setManaged(false);
			passwordfield.setManaged(false);
			passwordlab.setVisible(false);
			passwordfield.setVisible(false);
		} // IS THIS NEEDED?

		// 0 - Master Admin: Everything, password unblocked
		// 1 - Admin: Everything except new staff & edit staff, password blocked
		// 2 - Self Timetabling Staff: can see everything except some staff data, and can only add new appointments for themselves
		// 3 - Observing Staff: can see but not touch
		// 4 - Non interacting Staff: no log in details, simply a placeholder
		// 5 - Non clinical Master Staff: can observe and modify everything non patient related
		// 6 - Non clinical Staff: can observer everything not patient related

		ObservableList<String> permissionlevels = FXCollections.observableArrayList();

		// permissionlevels.add("Master Admin"); // Not Creatable
		int admins = 0;
		for (Account acc : mainapp.getAccounts()) {
			if (acc.getPermission() == 1) {
				admins += 1;
			}
		}

		String admintext = "Admin";

		int adminlimit = 50000;

		switch (mainapp.licence.getType()) {
		case 0:
			adminlimit = 1;
			System.out.println("AdminLimit = 1");	

			break;
		case 1:
			adminlimit = 2;
			break;
		case 2:
			adminlimit = 3;
			break;
		}	
		
		System.out.println("Running");		

		if (adminlimit != 50000) {
			System.out.println("AdminLimit != 50000");	

			if (adminlimit - admins > 0) {
				admintext = "Admin (" + String.valueOf(adminlimit - admins) + " left)";
				permissionlevels.add(admintext);
			}
		}
		else {
			admintext = "Admin";
			permissionlevels.add(admintext);
		}

		permissionlevels.add("Self Timetabling Staff");
		permissionlevels.add("Observing Staff");
		permissionlevels.add("Non interacting Staff");
		permissionlevels.add("Non Clinical Master Staff");
		permissionlevels.add("Non Clinical Staff");

		permissionpicker.setItems(permissionlevels);
		permissionpicker.setValue("Observing Staff");

		final String admint = admintext;

		permissionpicker.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				switch (t1) {

				case "Master Admin":
					permissionlevel = 0;
					break;
				case "Self Timetabling Staff":
					permissionlevel = 2;
					break;
				case "Observing Staff":
					permissionlevel = 3;
					break;
				case "Non interacting Staff":
					permissionlevel = 4;					
					break;
				case "Non Clinical Master Staff":
					permissionlevel = 5;
					break;
				case "Non Clinical Staff":
					permissionlevel = 6;
					break;
				}
				
				if(permissionlevel == 4) { // If chosen permission is non interacting staff, remove username and password fields
					passwordfield.setVisible(false);
					passwordfield.setManaged(false);
					usernamefield.setVisible(false);
					usernamefield.setManaged(false);
				}
				else {
					passwordfield.setVisible(true);
					passwordfield.setManaged(true);
					usernamefield.setVisible(true);
					usernamefield.setManaged(true);
				}
				
				if (t1.equals(admint)) {
					permissionlevel = 1;
				}
			}
		});

		starthourfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (Integer.valueOf(starthourfield.getText() + keyEvent.getCharacter()) > 23) {
					keyEvent.consume();
				} else {
					if (startminutefield.getText().trim().length() > 0) {
						Integer.valueOf(startminutefield.getText());
					} else {
					}
				}
			}
		});
		startminutefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (Integer.valueOf(startminutefield.getText() + keyEvent.getCharacter()) > 59) {
					keyEvent.consume();
				} else {
					if (starthourfield.getText().trim().length() > 0) {
						Integer.valueOf(starthourfield.getText());
					} else {
					}
				}
			}
		});

		endhourfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (Integer.valueOf(endhourfield.getText() + keyEvent.getCharacter()) > 23) {
					keyEvent.consume();
				} else {
					if (endminutefield.getText().trim().length() > 0) {
						Integer.valueOf(endminutefield.getText());
					} else {
					}
				}
			}
		});
		endminutefield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (!"0123456789".contains(keyEvent.getCharacter())) {
					keyEvent.consume();
				} else if (Integer.valueOf(endminutefield.getText() + keyEvent.getCharacter()) > 59) {
					keyEvent.consume();
				} else {
					if (endhourfield.getText().trim().length() > 0) {
						Integer.valueOf(endhourfield.getText());
					} else {
					}
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
	 */
	public void setedit() {
		edit = true;
		titlefield.setText(account.getTitle());
		firstnamefield.setText(account.getFirstName());
		lastnamefield.setText(account.getLastName());
		usernamefield.setText(account.getUserName());
		passwordfield.setText(account.getPassword());
		rolefield.setText(account.getRole());	

		starthourfield.setText(String.valueOf(account.getStartTime().getHour()));
		endhourfield.setText(String.valueOf(account.getEndTime().getHour()));
		startminutefield.setText(String.valueOf(account.getStartTime().getMinute()));
		endminutefield.setText(String.valueOf(account.getEndTime().getMinute()));

		emailfield.setText(account.getEmail());
		numberfield.setText(account.getPhoneNumber());
		
		
		//--------------------------
		
		if(account.getPermission() != 0) {		
		permissionpicker.getSelectionModel().select(account.getPermission()-1);}
	
		
		//--------------------------

	}

	/**
	 *
	 * @param account
	 */
	public void setStaff(Account account) {
		this.account = account;
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

						// mainApp.LoadAccountDataFromFile(mainApp.getFilePath());

						account.setTitle(titlefield.getText());
						account.setFirstName(firstnamefield.getText());
						account.setLastName(lastnamefield.getText());
						account.setUserName(usernamefield.getText());
						account.setPassword(passwordfield.getText());
						account.setRole(rolefield.getText());
						account.setPermission(permissionlevel);

						if (!edit) {
							mainApp.setPersonID();
							int personnum = mainApp.appPrefs.getPersonID() + 1;
							account.setStaffNumber(personnum);
							mainApp.appPrefs.setPersonID(personnum + 1);
						}

						account.setStartTime(LocalTime.of(Integer.valueOf(starthourfield.getText()), Integer.valueOf(startminutefield.getText())));
						System.out.println(account.getStartTime().toString());
						account.setEndTime(LocalTime.of(Integer.valueOf(endhourfield.getText()), Integer.valueOf(endminutefield.getText())));

						account.setEmail(emailfield.getText());
						account.setPhoneNumber(numberfield.getText());

						mainApp.actuallyAddAccount(account, edit);
						mainApp.setVersion();
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
		if (firstnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}
		if (lastnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid Name\n";
		}
		if (usernamefield.getText() == null || usernamefield.getText().length() == 0) {
			errorMessage += "No valid Username\n";
		}
		if (passwordfield.getText() == null || passwordfield.getText().length() == 0) {
			errorMessage += "No valid Password\n";
		}
		if (starthourfield.getText() == null || starthourfield.getText().length() == 0) {
			errorMessage += "No valid Starting Hour\n";
		}
		if (startminutefield.getText() == null || startminutefield.getText().length() == 0) {
			errorMessage += "No valid Starting Minute\n";
		}		
		if (endhourfield.getText() == null || endhourfield.getText().length() == 0) {
			errorMessage += "No valid Ending Hour\n";
		}
		if (endminutefield.getText() == null || endminutefield.getText().length() == 0) {
			errorMessage += "No valid Ending Minute\n";
		}
		
	

		if (errorMessage.length() != 0) {
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
