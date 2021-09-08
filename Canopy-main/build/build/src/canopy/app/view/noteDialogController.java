package canopy.app.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.MailingList;
import canopy.app.model.Note;
import canopy.app.model.NoteType;
import canopy.app.model.Patient;
import canopy.app.util.Toast;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author Andy
 */
public class noteDialogController {

	@FXML
	private AnchorPane coverpane;
	private boolean saved = false;

	@FXML
	private TextArea textfield;

	private MainApp mainapp;

	@FXML
	private Label filename;
	@FXML
	private Label patientname;
	@FXML
	private Button filebutton;
	@FXML
	private HBox bottombox;
	@FXML
	private ComboBox<NoteType> typepicker;

	MailingList list;
	private boolean okClicked = false;
	private Stage dialogStage;
	boolean warningshown = false;

	private Patient patient;
	private Account account;
	private String filepath;
	private String filenamestr;
	private Boolean permission = true;

	/**
	 *
	 * @param mainapp
	 */
	public void initialise(MainApp mainapp, Patient patient) {
		this.mainapp = mainapp;
		this.patient = patient;

		patientname.setText(patient.getFirstName() + " " + patient.getLastName());

		typepicker.setConverter(new StringConverter<NoteType>() {

			@Override
			public String toString(NoteType object) {
				return object.getTypeName();
			}

			@Override
			public NoteType fromString(String string) {
				for (NoteType type : mainapp.getNoteTypes()) {
					if (type.getTypeName().equals(string)) {
						return type;
					}
				}
				return null;
			}
		});
		typepicker.setItems(mainapp.getNoteTypes());
		typepicker.setValue(mainapp.getNoteTypes().get(0));

		typepicker.setCellFactory(new Callback<ListView<NoteType>, ListCell<NoteType>>() {
			@Override
			public ListCell<NoteType> call(ListView<NoteType> p) {

				return new ListCell<NoteType>() {
					@Override
					protected void updateItem(NoteType item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Circle circle = new Circle(12, 12, 7);

							String[] rgb = item.getColour().split(","); // Splits string into r , g , b

							circle.setFill(Color.rgb(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
							// setTextFill((Color.rgb(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])))); // Sets colour to rgb version using strings. Yes it's janky.
							setText(item.getTypeName());
							setGraphic(circle);

						}
					}
				};
			}
		});

		typepicker.setButtonCell(new ListCell<NoteType>() {
			@Override
			protected void updateItem(NoteType item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					Circle circle = new Circle(12, 12, 7);

					String[] rgb = item.getColour().split(","); // Splits string into r , g , b

					circle.setFill(Color.rgb(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
					// setTextFill((Color.rgb(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])))); // Sets colour to rgb version using strings. Yes it's janky.
					setText(item.getTypeName());
					setGraphic(circle);

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

		String errorMessage = "";
		if (textfield.getText() == null || textfield.getText().length() == 0) {
			errorMessage += "No Note Text Entered";
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

			alert.show();
		} else {

			Note newnote = new Note(patient, mainapp.getCurrentAccount().getStaffNumber(), LocalDate.now(), LocalTime.now(), textfield.getText(), typepicker.getValue().getId());

			int notenum = mainapp.appPrefs.getNoteID() + 1;
			newnote.setId(notenum);
			mainapp.appPrefs.setNoteID(mainapp.appPrefs.getNoteID() + 1);

			if (filepath != null) {
				newnote.setFile(filepath);
				newnote.setFileName(filenamestr);
			}

			textfield.clear();

			mainapp.addNote(newnote);

			coverpane.setTranslateX(dialogStage.getWidth());
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(0);
			closeNav.play();
			
			String toastMsg = "Creating Note";
			int toastMsgTime = 3500; // 3.5 seconds
			int fadeInTime = 500; // 0.5 seconds
			int fadeOutTime = 500; // 0.5 seconds
			Toast.makeText(mainapp.getPrimaryStage(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 2);
			
			new Thread() {
				public void run() {
					mainapp.saveNotesDataToFile(mainapp.getFilePath(), false);
					mainapp.savePreferenceDataToFile(mainapp.getFilePath());
					mainapp.setVersion();
				}
			}.start();
			
			

			filebutton.setText("ADD FILE");
			filename.setText("");
			filepath = null;
			filenamestr = null;

			saved = true;
			okClicked = true;

			mainapp.refreshnotes();

			dialogStage.close();
		}

	}

	/**
	 *
	 */
	@FXML
	public void addfile() {
		if (filebutton.getText().equals("REMOVE FILE")) {
			File delete = new File(mainapp.getFilePath() + filepath);
			delete.delete();
			filename.setText("");
			filepath = null;
			filebutton.setText("ADD FILE");
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose File to Attach");
			File addfile = fileChooser.showOpenDialog(mainapp.getPrimaryStage());
			if (addfile != null) {
				filebutton.setText("UPLOADING FILE");

				Thread filethread = new Thread() {
					// runnable for thread
					public void run() {
						String path = "/Resources/";
						File check = new File(mainapp.getFilePath() + path);
						if (!check.exists()) {
							new File(path).mkdir();
						}
						File toFile = new File(mainapp.getFilePath() + path + "/" + addfile.getName());

						if (toFile.exists()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									filebutton.setText("REMOVE FILE");
									filename.setText(addfile.getName());
									filepath = path + "/" + addfile.getName();
									filenamestr = addfile.getName();
								}
							});

						}

						else {

							Path from = Paths.get(addfile.toURI());
							Path to = Paths.get(toFile.toURI());

							try {
								System.out.println("From file: " + addfile.getAbsolutePath());
								System.out.println("To file: " + toFile.getAbsolutePath());
								Files.copy(from, to);

								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												filebutton.setText("REMOVE FILE");
												filename.setText(addfile.getName());
												filepath = path + "/" + addfile.getName();
												filenamestr = addfile.getName();
											}
										});
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

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
