package canopy.app.view;

import java.awt.Button;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Day;
import canopy.app.model.Note;
import canopy.app.model.Patient;
import canopy.app.model.Person;
import canopy.app.model.PersonType;
import canopy.app.model.Room;
import canopy.app.util.ComparatorBox;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Glow;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author Andy
 */
public class forwardDialogController {

	@FXML
	private VBox list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;

	private boolean saved = false;

	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainApp;
	Note note;
	Patient patient;

	ObservableList<Account> tickedstaff = FXCollections.observableArrayList();
	ObservableList<Person> tickedpeople = FXCollections.observableArrayList();
	boolean patientemail;

	/**
	 *
	 * @param mainapp
	 * @param account
	 */
	public void initialise(MainApp mainapp, Note note, Patient patient) {
		this.mainApp = mainapp;
		this.note = note;
		this.patient = patient;

		ObservableList<Account> staff = FXCollections.observableArrayList();
		ObservableList<Person> people = FXCollections.observableArrayList();
		
		addpatientemail(); // Adds patient's contact emails as a forwarding option

		if (patient.getContacts() != null) {
			String[] contacts = patient.getContacts().split(";");

			for (String contact : contacts) {
				if (!contact.trim().equals("")) {
					int id = Integer.valueOf(contact.trim());
					for (Account checkstaff : mainapp.getAccounts()) {
						if (checkstaff.getStaffNumber() == id) {
							staff.add(checkstaff);
						}
					}
					for (Person checkperson : mainapp.getPeople()) {
						if (checkperson.getId() == id) {
							people.add(checkperson);
						}
					}
				}
			}					
			refreshpeople(people);
			refreshstaff(staff);
		}
	}

	/**
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
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
		String addresses = "";
		
		if(patientemail) {
			addresses += patient.getEmails() + ";";
		}


		for (Person person : tickedpeople) {
			addresses += person.getEmail() + ";";
		}
		for (Account acc : tickedstaff) {
			addresses += acc.getEmail() + ";";
		}
		

		String subject = patient.getFirstName() + " " + patient.getLastName() + " note copy: " + note.getDate().toString() + " " + note.getTime().toString();
		subject = MainApp.htmlEscape(subject);
		addresses = MainApp.htmlEscape(addresses);
		String body = note.getText() + "\n + generated with CANOPY system";
		//body = body.substring(0, 200);
		System.out.println(body);
		body = MainApp.htmlEscape(body);
		System.out.println(body);
		
		


		if (note.getFile() != null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Note file will not automatically attach");
			alert.setHeaderText("Note file will not automatically attach");
			alert.setContentText("The note file must be manually attached if it is intended to be sent. The file can be found at " + note.getFile() + ", or at it's original location if not deleted.");
			ButtonType buttonTypeOne = new ButtonType("Ok");

			alert.getButtonTypes().setAll(buttonTypeOne);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					if (desktop.isSupported(Desktop.Action.MAIL)) {
						URI mailto;
						try {
							mailto = new URI("mailto:?Bcc=" + addresses + "&subject=" + subject + "&body=" + body);
							desktop.mail(mailto);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} else {

			}
		} else {

			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.MAIL)) {
					URI mailto;
					try {
						mailto = new URI("mailto:?Bcc=" + addresses + "&subject=" + subject + "&body=" + body);
						desktop.mail(mailto);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		dialogStage.close();
	}
	
	public void addpatientemail() {
		HBox headerpane = new HBox();
		Label headerlabel = new Label();
		headerlabel.setText("Patient Contact Emails");
		headerpane.getStyleClass().add("background");
		headerlabel.getStyleClass().add("label-bright");
		headerpane.getChildren().add(headerlabel);
		list.getChildren().add(headerpane);
		
		VBox accountpane = new VBox();
		accountpane.setAlignment(Pos.CENTER);
		HBox containerbox = new HBox();
		VBox importantbox = new VBox();
		accountpane.prefWidthProperty().bind(list.widthProperty());
		containerbox.prefWidthProperty().bind(accountpane.widthProperty());
		containerbox.setAlignment(Pos.CENTER);
		Glow glow = new Glow(0);
		accountpane.setEffect(glow);
		accountpane.setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				glow.setLevel(0.2);

			}
		});
		Insets insets = new Insets(0, 6, 0, 6);
		containerbox.setPadding(insets);
		HBox.setHgrow(containerbox, Priority.ALWAYS);
		importantbox.setAlignment(Pos.CENTER_LEFT);
		Label namelab = new Label();
		namelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		Label doblab = new Label();
		doblab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		namelab.setWrapText(true);
		doblab.setWrapText(true);

		CheckBox includebox = new CheckBox();

		includebox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					patientemail = true;
				} else {
					patientemail = false;
				}
			}
		});

		String style;
		accountpane.getStyleClass().add("appointment");
		accountpane.setOnMouseExited(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				glow.setLevel(0);
			}
		});

		namelab.setText("Patient Emails");
		doblab.setText("");
		namelab.getStyleClass().add("label-list");
		doblab.getStyleClass().add("label-list");
		accountpane.getChildren().add(containerbox);
		containerbox.getChildren().addAll(namelab, importantbox, includebox);
		importantbox.getChildren().addAll(doblab);
		containerbox.maxHeightProperty().bind(accountpane.heightProperty());
		list.getChildren().add(containerbox);			
	}

	/**
	 *
	 */
	public void refreshstaff(ObservableList<Account> staff) {
		HBox headerpane = new HBox();
		Label headerlabel = new Label();
		headerlabel.setText("Staff");
		headerpane.getStyleClass().add("background");
		headerlabel.getStyleClass().add("label-bright");
		headerpane.getChildren().add(headerlabel);
		list.getChildren().add(headerpane);

		for(Account account:staff) {
			VBox accountpane = new VBox();
			accountpane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			VBox importantbox = new VBox();
			accountpane.prefWidthProperty().bind(list.widthProperty());
			containerbox.prefWidthProperty().bind(accountpane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);
			accountpane.setEffect(glow);
			accountpane.setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0.2);

				}
			});
			Insets insets = new Insets(0, 6, 0, 6);
			containerbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			importantbox.setAlignment(Pos.CENTER_LEFT);
			Label namelab = new Label();
			namelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			Label doblab = new Label();
			doblab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			namelab.setWrapText(true);
			doblab.setWrapText(true);

			CheckBox includebox = new CheckBox();

			String[] ids = patient.getContacts().split(";");

			includebox.selectedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						tickedstaff.add(account);
					} else {
						tickedstaff.remove(account);
					}
				}
			});

			String style;
			accountpane.getStyleClass().add("appointment");
			accountpane.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0);
				}
			});

			namelab.setText(account.getTitle() + "." + account.getFirstName() + " " + account.getLastName() + " | ");
			doblab.setText(account.getRole());
			namelab.getStyleClass().add("label-list");
			doblab.getStyleClass().add("label-list");
			accountpane.getChildren().add(containerbox);
			containerbox.getChildren().addAll(namelab, importantbox, includebox);
			importantbox.getChildren().addAll(doblab);
			containerbox.maxHeightProperty().bind(accountpane.heightProperty());
			list.getChildren().add(containerbox);			
		}
	}

	/**
	 *
	 * @param persondata
	 */
	public void refreshpeople(ObservableList<Person> persondata) {
		list.prefWidthProperty().bind(scrllparent.widthProperty());

		for (PersonType type : mainApp.getPersonTypes()) {
			HBox headerpane = new HBox();
			Label headerlabel = new Label();
			headerlabel.setText(type.getTypeName());
			headerpane.getStyleClass().add("background");
			headerlabel.getStyleClass().add("label-bright");
			headerpane.getChildren().add(headerlabel);
			list.getChildren().add(headerpane);

			for (Person person : persondata) {

				if (person.getPersonType() == type.getId()) {

					VBox personpane = new VBox();
					personpane.setAlignment(Pos.CENTER);
					HBox containerbox = new HBox();
					VBox importantbox = new VBox();
					personpane.prefWidthProperty().bind(list.widthProperty());
					containerbox.prefWidthProperty().bind(personpane.widthProperty());
					containerbox.setAlignment(Pos.CENTER);
					Glow glow = new Glow(0);
					personpane.setEffect(glow);
					personpane.setOnMouseEntered(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							glow.setLevel(0.2);

						}
					});
					CheckBox includebox = new CheckBox();

					includebox.selectedProperty().addListener(new ChangeListener<Boolean>() {

						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
							if (newValue) {
								tickedpeople.add(person);
							} else {
								tickedpeople.remove(person);
							}
						}
					});
					Insets insets = new Insets(0, 6, 0, 6);
					containerbox.setPadding(insets);
					HBox.setHgrow(containerbox, Priority.ALWAYS);
					importantbox.setAlignment(Pos.CENTER_LEFT);
					Label namelab = new Label();
					namelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
					Label doblab = new Label();
					doblab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
					namelab.setWrapText(true);
					doblab.setWrapText(true);
					personpane.getStyleClass().add("appointment");
					personpane.setOnMouseExited(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							glow.setLevel(0);
						}
					});
					namelab.setText(person.getFirstName() + " " + person.getLastName() + " | ");
					doblab.setText(person.getDescription());
					namelab.getStyleClass().add("label-list");
					doblab.getStyleClass().add("label-list");
					personpane.getChildren().add(containerbox);
					containerbox.getChildren().addAll(namelab, importantbox, includebox);
					importantbox.getChildren().addAll(doblab);
					containerbox.maxHeightProperty().bind(personpane.heightProperty());

					list.getChildren().add(containerbox);
				}
			}

		}

	}

	@FXML

	void handleCancel() {
		dialogStage.close();
	}

}
