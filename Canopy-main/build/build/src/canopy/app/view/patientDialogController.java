package canopy.app.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.prefs.Preferences;

import canopy.app.MainApp;
import canopy.app.model.Note;
import canopy.app.model.Patient;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class patientDialogController {

	@FXML
	private Label date;

	@FXML
	private DatePicker datepicker;

	@FXML
	private TextField firstnamefield;

	@FXML
	private TextField lastnamefield;
	
	@FXML 
	private CheckBox carercheckbox;
	
	@FXML
	private TextField cftextbox;

	@FXML
	private TextField cltextbox;
	
	@FXML
	private Label cfheader;

	@FXML
	private Label clheader;

	@FXML
	private TextField diagnosesfield;

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
	private AnchorPane addemailbutton;

	@FXML
	private Label dayname1;	

	@FXML
	private Label agelabel;

	@FXML
	private TextField numberfield;

	@FXML
	private AnchorPane addnumberbutton;

	@FXML
	private Label dayname11;

	@FXML
	private VBox emailslist;

	@FXML
	private VBox numberslist;

	@FXML
	private AnchorPane coverpane;
	
	@FXML
	private Label filename;
	
	@FXML
	private Button filebutton;
	
	private boolean saved = false;

	private boolean okClicked = false;
	private boolean permissiongiven = false;
	private boolean permissiongiven2 = false;
	
	String filepath;
	String filenamestr;
	
	boolean edit = false;
	private Stage dialogStage;
	private Patient patient;
	MainApp mainApp;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainApp = mainapp;
		firstnamefield.requestFocus();
		
		cfheader.setVisible(false);
		cftextbox.setVisible(false);
		clheader.setVisible(false);
		cltextbox.setVisible(false);
		
		datepicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue ov, LocalDate t, LocalDate t1) {
				long curryears = t1.until(LocalDate.now(),ChronoUnit.YEARS);
				long currmonths = t1.until(LocalDate.now(),ChronoUnit.MONTHS) - curryears*12;
				
				agelabel.setText(String.valueOf(curryears) + " Years " + String.valueOf(currmonths) + " Months ");
				if(curryears < 18) {
					carercheckbox.selectedProperty().set(true);
					onRecurringCheck();
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
	
	@FXML
	private void onRecurringCheck() {
		if (carercheckbox.selectedProperty().get() == true) {
			cfheader.setVisible(true);
			cftextbox.setVisible(true);
			clheader.setVisible(true);
			cltextbox.setVisible(true);
		} else {
			cfheader.setVisible(false);
			cftextbox.setVisible(false);
			clheader.setVisible(false);
			cltextbox.setVisible(false);
		}
	}

	/**
	 *
	 */
	public void setedit() {
		edit = true;
		firstnamefield.setText(patient.getFirstName());
		lastnamefield.setText(patient.getLastName());
		
		if(patient.getCarerFirstName()!=null) {
			carercheckbox.selectedProperty().set(true);
			cftextbox.setText(patient.getCarerFirstName());			
		}
		if(patient.getCarerLastName()!=null) {
			cltextbox.setText(patient.getCarerLastName());			
		}
		
		datepicker.setValue(patient.getBirthday());
		streetfield.setText(patient.getStreet());
		cityfield.setText(patient.getCity());
		postcodefield.setText(patient.getPostalCode());
		diagnosesfield.setText(patient.getDiagnosis());

		if (patient.getEmails() != null) {
			String[] emails = patient.getEmails().split(";");

			emailslist.getChildren().clear();
			for (String email : emails) {
				if (email.trim().length() != 0 && email != null) {
					Label emaillabel = new Label();
					emaillabel.setText(email);
					emaillabel.getStyleClass().add("label-grey-highlight");
					System.out.println("Set email label style");
					emaillabel.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							System.out.println("Email Label Click");
							emailslist.getChildren().remove(emaillabel);
							patient.removeEmail(emaillabel.getText());
						}
					});
					System.out.println("Set email label Onclick");
					
					emaillabel.setWrapText(true);
					emailslist.getChildren().add(emaillabel);
				}
			}
		}

		if (patient.getNumbers() != null) {

			String[] numbers = patient.getNumbers().split(";");

			numberslist.getChildren().clear();
			for (String number : numbers) {
				if (number.trim().length() != 0 && number != null) {
					Label numberlabel = new Label();
					numberlabel.setText(number);
					numberlabel.getStyleClass().add("label-grey-highlight");
					
					numberlabel.setOnMouseClicked(new EventHandler<Event>() {

						@Override
						public void handle(Event event) {
							System.out.println("Number Label Click");
							numberslist.getChildren().remove(numberlabel);
							patient.removeNumber(numberlabel.getText());							
						}
					});
					
					
					numberlabel.setWrapText(true);
					numberslist.getChildren().add(numberlabel);
				}
			}
		}
	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Patient patient) {

		this.patient = patient;
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
		try {
		if (isInputValid()) {
			coverpane.setTranslateX(dialogStage.getWidth());
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(0);
			
			if(!emailfield.getText().trim().isEmpty()) {							
				addEmail();
			}
			
			if(!numberfield.getText().trim().isEmpty()) {							
				addNumber();
			}

			closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!saved) {

						// if(mainApp.PatientsSaved()) {
						// mainApp.LoadPatientDataFromFile(mainApp.getFilePath());}

						patient.setFirstName(firstnamefield.getText());
						patient.setLastName(lastnamefield.getText());
						patient.setDiagnosis(diagnosesfield.getText());
						patient.setBirthday(datepicker.getValue());
						patient.setCity(cityfield.getText());
						
						if(carercheckbox.selectedProperty().get() == true) {
							patient.setCarerFirstName(cftextbox.getText());
							patient.setCarerLastName(cltextbox.getText());
						}

						Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
						if(!edit) {
						mainApp.setPersonID();
						int patientnumber = mainApp.appPrefs.getPersonID() + 1;
						patient.setPatientNumber(patientnumber);
						mainApp.appPrefs.setPersonID(patientnumber+1);
						}
						patient.setStreet(streetfield.getText());
						patient.setPostalCode(postcodefield.getText());
						
						if(filenamestr!=null && filepath!=null) {
							patient.setPermissionFile(filepath);
							patient.setPermissionFileName(filenamestr);
						}

						Note creatednote = new Note(patient, mainApp.getCurrentAccount().getStaffNumber(), LocalDate.now(), LocalTime.now(), "Patient Created", 0);
						mainApp.addNote(creatednote);

						mainApp.actuallyAddPatient(patient, edit);
						mainApp.setVersion();

						saved = true;
						okClicked = true;
						dialogStage.close();
					}
				}
			});
			closeNav.play();
		}}catch (Exception e) {
			mainApp.writeError(e);
		}
	}

	private boolean isInputValid() {
		String errorMessage = "";
		boolean warnMessage = false;

		if (firstnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid First Name\n";
		}
		if (lastnamefield.getText() == null || firstnamefield.getText().length() == 0) {
			errorMessage += "No valid Last Name\n";
		}
		
		if(carercheckbox.selectedProperty().get() == true) {
			if ((cftextbox.getText() == null || cftextbox.getText().length() == 0)&& (cltextbox.getText() == null || cltextbox.getText().length() == 0)) {
				errorMessage += "No valid Carer Name\n";
			}
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
	void addEmail() {
		if (!permissiongiven) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(dialogStage);
			alert.setTitle("Check Permission");
			alert.setHeaderText("Has permission been given to use this email for contact purposes?");

			ButtonType buttonTypeOne = new ButtonType("Yes");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				permissiongiven = true;
			} else {

			}

		}

		if (permissiongiven) {
			if (emailfield.getText().length() > 0) {
				patient.addEmail(emailfield.getText());
				Label emaillabel = new Label();
				//emaillabel.setStyle("    -fx-font-size: 9pt;\r\n" + "    -fx-text-fill: black;");
				emaillabel.getStyleClass().add("label-grey-highlight");
				emaillabel.setText(emailfield.getText());
				emaillabel.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						emailslist.getChildren().remove(emaillabel);
						patient.removeEmail(emaillabel.getText());
					}
				});
				emailfield.clear();
				emailslist.getChildren().add(emaillabel);
			}
		}
	}

	@FXML
	void addNumber() {
		if (!permissiongiven2) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.initOwner(dialogStage);
			alert.setTitle("Check Permission");
			alert.setHeaderText("Has permission been given to use this telephone number for contact purposes?");

			ButtonType buttonTypeOne = new ButtonType("Yes");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				permissiongiven2 = true;
			} else {
				alert.close();
			}
		}

		if (permissiongiven2) {
			if (numberfield.getText().length() > 0) {
				patient.addNumber(numberfield.getText());
				Label numberlabel = new Label();
				//numberlabel.setStyle("    -fx-font-size: 9pt;\r\n" + "    -fx-text-fill: black;");
				numberlabel.getStyleClass().add("label-grey-highlight");
				numberlabel.setText(numberfield.getText());
				
				numberlabel.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						emailslist.getChildren().remove(numberlabel);
						patient.removeNumber(numberlabel.getText());
					}
				});
				
				numberfield.clear();
				numberslist.getChildren().add(numberlabel);
			}
		}
	}

	@FXML
	void hourfinished() {

	}
	
	/**
	 *
	 */
	@FXML
	public void addfile() {
		
		if (filebutton.getText().equals("REMOVE FILE")) {
			File delete = new File(filepath);
			delete.delete();
			filepath = null;
			filebutton.setText("ADD FILE");
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose File to Attach");
			File addfile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
			if (addfile != null) {
				filebutton.setText("UPLOADING FILE");

				Thread filethread = new Thread() {
					// runnable for thread
					public void run() {
						String path = mainApp.getFilePath() + "/Resources/";
						File check = new File(path);
						if (!check.exists()) {
							new File(path).mkdir();
						}
						File toFile = new File(check.getAbsolutePath() + "/" + addfile.getName());

						if (toFile.exists()) {
							filebutton.setText("REMOVE FILE");
							filename.setText(addfile.getName());
							filepath = toFile.getAbsolutePath();
							filenamestr = addfile.getName();
						}

						else {

							Path from = Paths.get(addfile.toURI());
							Path to = Paths.get(toFile.toURI());

							try {
								Files.copy(from, to);

								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										filebutton.setText("REMOVE FILE");
										filename.setText(addfile.getName());
										filepath = toFile.getAbsolutePath();
										filenamestr = addfile.getName();
									}
								});

							} catch (IOException e) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										filebutton.setText("UPLOAD FAILED");
									}
								});
								e.printStackTrace();

							}
						}

					}
				};

				filethread.start();
			}

		}
	}

}
