package canopy.app.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import javax.swing.ButtonModel;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.Note;
import canopy.app.model.NoteType;
import canopy.app.model.Patient;
import canopy.app.util.Toast;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import jdk.nashorn.internal.ir.BreakableNode;

/**
 *
 * @author Andy
 */
public class notesListController {

	@FXML
	private GridPane schedule;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private AnchorPane notewindowbutton;
	@FXML
	private AnchorPane titlebox;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private TextArea textfield;
	@FXML
	private MainApp mainapp;
	@FXML
	private Label filename;
	@FXML
	private Button filebutton;
	@FXML
	private HBox bottombox;
	@FXML
	private ComboBox<String> sorterselection;
	@FXML
	private ComboBox<NoteType> typepicker;
	@FXML
	private ComboBox<NoteType> typeselection;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();
	ObservableList<NoteType> typeselections = FXCollections.observableArrayList();

	int type = 2;
	Patient patient;
	Account account;
	String filepath;
	String filenamestr;
	Boolean permission = true;
	private final ObservableList<Note> notes = FXCollections.observableArrayList();
	boolean revertingnotetype = false;

	int displaylimit = 10;

	calendarController parentcontroller;
	LocalDate date2;

	/**
	 *
	 */
	public void initialise() {

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		schedule.maxWidthProperty().bind(scrllparent.widthProperty());

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		schedule.prefWidthProperty().bind(scrllparent.widthProperty());

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

		playAfterPause(1300, timeline);

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

		typeselection.setConverter(new StringConverter<NoteType>() {

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
		typeselections.clear();
		NoteType all = new NoteType("All", 9999, "255,255,255");
		typeselections.add(all);
		typeselections.addAll(mainapp.getNoteTypes());
		typeselection.setItems(typeselections);
		typeselection.setValue(all);

		typeselection.valueProperty().addListener(new ChangeListener<NoteType>() {

			@Override
			public void changed(ObservableValue<? extends NoteType> observable, NoteType oldValue, NoteType newValue) {
				refresh();

			}
		});

		typeselection.setCellFactory(new Callback<ListView<NoteType>, ListCell<NoteType>>() {
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

		typeselection.setButtonCell(new ListCell<NoteType>() {
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

		sorttypes.add("Created Descending");
		sorttypes.add("Created Ascending");
		sorttypes.add("Patient ID");
		sorttypes.add("Staff ID");
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("Created Descending");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {

				refresh();
			}
		});

		hide();

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

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();

	}

	@FXML
	void lift() {

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();

	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Patient patient) {
		type = 0;
		schedule.getChildren().clear();
		this.patient = patient;
		if (permission) {
			refreshnotes();
			show();
		}
	}

	/**
	 *
	 * @param setaccount
	 */
	public void setAccount(Account setaccount) {
		type = 1;
		schedule.getChildren().clear();
		account = setaccount;
		boolean permission2 = false;

		if (account.getStaffNumber() == mainapp.getCurrentAccount().getStaffNumber() && (account.getPermission() != 5 && account.getPermission() != 6)) { // If account is looking at it's own notes
			permission2 = true;
		} else if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1) { // Otherwise must be of apropriate permission level
			permission2 = true;
		}

		if (permission2) {
			System.out.println("refreshing based on account");
			refreshnotesStaff();
			show();
		} else {
			hide();
		}

		hidebutton();
	}

	ObservableList<Note> sortNotes(ObservableList<Note> input) {
		ObservableList<Note> output = FXCollections.observableArrayList();
		output.addAll(input);

		if (typeselection.getValue() != null) {

			if (typeselection.getValue().getId().equals(9999)) {

			} else {
				ObservableList<Note> included = FXCollections.observableArrayList();

				for (Note currentnote : output) {
					if (currentnote.getType().equals(typeselection.getValue().getId())) {
						included.add(currentnote);
					}
				}
				output.clear();
				output.addAll(included);
			}
		}

		switch (sorterselection.getValue()) {

		case "Created Descending":
			Collections.sort(output, new Comparator<Note>() {
				public int compare(Note p1, Note p2) {
					LocalDateTime dt1 = LocalDateTime.of(p1.getDate(), p1.getTime());
					LocalDateTime dt2 = LocalDateTime.of(p2.getDate(), p2.getTime());

					return dt2.compareTo(dt1);
				}
			});
			break;
		case "Created Ascending":
			Collections.sort(output, new Comparator<Note>() {
				public int compare(Note p1, Note p2) {
					LocalDateTime dt1 = LocalDateTime.of(p1.getDate(), p1.getTime());
					LocalDateTime dt2 = LocalDateTime.of(p2.getDate(), p2.getTime());

					return dt1.compareTo(dt2);
				}
			});
			break;
		case "Patient ID":
			Comparator<Note> comparator1 = Comparator.comparingInt(Note::getPatient).reversed();
			FXCollections.sort(output, comparator1);
			break;
		case "Staff ID":
			Comparator<Note> comparator2 = Comparator.comparingInt(Note::getStaffCode).reversed();
			FXCollections.sort(output, comparator2);
			break;
		}

		return output;
	}

	/**
	 *
	 */
	public void refreshnotesStaff() {
		schedule.getChildren().clear();

		ObservableList<Note> notes = FXCollections.observableArrayList();
		ObservableList<Note> sortnotes = FXCollections.observableArrayList();

		sortnotes = sortNotes(mainapp.getNotes());
		for (Note note : sortnotes) {
			System.out.println("Note StaffCode " + note.getStaffCode());
			System.out.println("Account StaffCode " + account.getStaffNumber());
			if (note.getStaffCode() == account.getStaffNumber()) {
				System.out.println("Match");
				notes.add(note);
				addNote(note, notes.size(), false);
			}
		}
	}

	/**
	 *
	 */
	public void refreshnotes() {
		schedule.getChildren().clear();
		ObservableList<Note> notes = FXCollections.observableArrayList();
		ObservableList<Note> sortnotes = FXCollections.observableArrayList();

		try {
			sortnotes = sortNotes(mainapp.getNotes());

			for (Note note : sortnotes) {
				if (note.getId() == mainapp.getPrefs().getNoteID()) {
					System.out.println("Most recent note loaded " + note.getId());
					System.out.println("Note Patient ID: " + note.getPatient());
					System.out.println("Current Patient ID: " + patient.getPatientNumber());

				}
				if (note.getPatient().equals(patient.getPatientNumber())) {
					notes.add(note);
					addNote(note, notes.size(), true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void refresh() {
		switch (type) {
		case 0:
			refreshnotes();
			break;
		case 1:
			refreshnotesStaff();
			break;
		}

	}

	public void refreshtypepickers() {
		typepicker.setItems(mainapp.getNoteTypes());
		typepicker.setValue(mainapp.getNoteTypes().get(0));

		typeselections.clear();
		NoteType all = new NoteType("All", 9999, "255,255,255");
		typeselections.add(all);
		typeselections.addAll(mainapp.getNoteTypes());
		typeselection.setItems(typeselections);
		typeselection.setValue(all);
	}

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		System.out.println("Setting MainApp");
		if (mainapp.getCurrentAccount().getPermission() == 5 || mainapp.getCurrentAccount().getPermission() == 6) {
			System.out.println("Account Permission is 5 or 6");
			hide();
			permission = false;
		}

	}

	/**
	 *
	 * @param pos
	 */
	public void addRow(int pos) {
		Pane fillpane = new Pane();
		schedule.addRow(pos, fillpane);
	}

	/**
	 *
	 * @param note
	 * @param pos
	 */
	public void addNote(Note note, int pos, boolean forward) {

		VBox notepane = new VBox();
		notepane.setAlignment(Pos.CENTER);
		HBox containerbox = new HBox();
		VBox importantbox = new VBox();

		if (pos < displaylimit) {

			HBox buttonbox = new HBox();
			buttonbox.prefWidthProperty().bind(notepane.widthProperty());
			buttonbox.setAlignment(Pos.BASELINE_RIGHT);
			containerbox.maxWidthProperty().bind(notepane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);

			ComboBox<NoteType> notetypeselector = new ComboBox<>();
			notetypeselector.setConverter(new StringConverter<NoteType>() {

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
			notetypeselector.setItems(mainapp.getNoteTypes());
			notetypeselector.setValue(mainapp.getNoteTypes().get(note.getType()));

			notetypeselector.setCellFactory(new Callback<ListView<NoteType>, ListCell<NoteType>>() {
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

			notetypeselector.setButtonCell(new ListCell<NoteType>() {
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

			ChangeListener<NoteType> typelistener = new ChangeListener<NoteType>() {
				@Override
				public void changed(ObservableValue<? extends NoteType> observable, NoteType oldValue, NoteType newValue) {

					if (!revertingnotetype) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.setAlwaysOnTop(true);

						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
						dialogPane.getStyleClass().add(".dialog-pane");

						alert.setTitle("Change Note Type?");
						alert.setHeaderText("Are you sure you want to change this note type from " + oldValue.getTypeName() + " to " + newValue.getTypeName() + "?");

						ButtonType buttonTypeOne = new ButtonType("Change");
						ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

						alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == buttonTypeOne) {

							note.setType(newValue.getId()); // changes note type

							Task<Void> save = new Task<Void>() { // Saves changed note type in a new thread
								@Override
								protected Void call() throws Exception {
									mainapp.saveNoteTypeDataToFile(mainapp.getFilePath(), false);
									return null;
								}
							};
							save.run();

							refresh();

							stage.close();

						} else {
							revertingnotetype = true;
							notetypeselector.setValue(oldValue);
							revertingnotetype = false;
							stage.close();
						}
					}

				}
			};

			notetypeselector.valueProperty().addListener(typelistener);

			Insets insets = new Insets(6, 10, 6, 10);
			importantbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			importantbox.setAlignment(Pos.CENTER_LEFT);
			Label timelab = new Label();
			timelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			Label stafflab = new Label();
			Label descriptionlab = new Label();
			Label datelab = new Label();
			datelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

			Button forwardbutton = new Button();

			forwardbutton.setText("FORWARD");
			forwardbutton.getStyleClass().add("buttonp");

			forwardbutton.onMousePressedProperty().set(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.forwardNote(note, patient);
					refreshnotes();
				}
			});

			Button deletebutton = new Button();

			deletebutton.setText("DELETE");
			deletebutton.getStyleClass().add("buttonp");

			deletebutton.onMousePressedProperty().set(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {

					
					mainapp.deleteNote(note);
					refresh();
				}
			});

			stafflab.setWrapText(true);
			descriptionlab.setWrapText(true);
			datelab.setWrapText(true);

			notepane.getStyleClass().add("appointment");

			LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
			datelab.setText(dateStringConverter.toString(note.getDate()) + " | ");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			timelab.setText(note.getTime().format(dtf));

			Account staffaccount = null;

			System.out.println("before loop");

			for (Account account : mainapp.getClinicalAccounts()) {
				// System.out.println("in loop");

				if (account.getStaffNumber() == note.getStaffCode()) {
					// System.out.println("in if");

					staffaccount = account;
				}
			}

			if (staffaccount != null) {
				stafflab.setText(staffaccount.getTitle() + " " + staffaccount.getFirstName() + " " + staffaccount.getLastName());
			}
			descriptionlab.setText(note.getText());

			if (mainapp.getSmallText()) {
				datelab.getStyleClass().add("label-list");
				timelab.getStyleClass().add("label-list");
				stafflab.getStyleClass().add("label-list");
				descriptionlab.getStyleClass().add("label-list-body");
			} else {
				datelab.getStyleClass().add("label-list-mid");
				timelab.getStyleClass().add("label-list-mid");
				stafflab.getStyleClass().add("label-list");
				descriptionlab.getStyleClass().add("label-list-body");
			}

			notetypeselector.getStyleClass().add("table-view");

			descriptionlab.setWrapText(true);
			AnchorPane descriptionpane = new AnchorPane();
			descriptionpane.getChildren().add(descriptionlab);
			descriptionlab.setMinWidth(0);
			descriptionlab.setMinHeight(0);
			descriptionlab.setMaxHeight(60);
			descriptionlab.setPrefHeight(60);
			descriptionlab.maxWidthProperty().bind(containerbox.widthProperty());

			buttonbox.getChildren().add(notetypeselector);

			descriptionlab.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if (descriptionlab.getMaxHeight() > 100) {
						descriptionlab.setMaxHeight(60);
						descriptionlab.setPrefHeight(60);
					} else {
						descriptionlab.setMaxHeight(10000);
						descriptionlab.setPrefHeight(javafx.scene.control.Control.USE_COMPUTED_SIZE);
					}
				}
			});

			containerbox.getChildren().addAll(datelab, timelab);
			if (forward) {
				buttonbox.getChildren().add(forwardbutton);
			}
			buttonbox.getChildren().add(deletebutton);
			importantbox.getChildren().addAll(containerbox, stafflab, descriptionpane, buttonbox);

			if (note.getFile() != null) {
				System.out.println("in get file if");

				Label filelabel = new Label();
				filelabel.setText(note.getFileName());
				filelabel.getStyleClass().add("label-file");
				filelabel.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						File openfile = new File(mainapp.getFilePath() + note.getFile());
						try {
							Desktop.getDesktop().open(openfile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				importantbox.getChildren().add(filelabel);
			}

		} else if (pos == displaylimit) {

			Label loadmorelab = new Label();
			loadmorelab.setText("Load More");
			loadmorelab.getStyleClass().add("label-list-big");
			importantbox.getChildren().add(loadmorelab);
			notepane.getStyleClass().add("appointment");

			notepane.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {

					displaylimit += 20;
					refresh();

				}
			});

		}

		notepane.getChildren().add(importantbox);
		importantbox.maxWidthProperty().bind(notepane.widthProperty());
		importantbox.maxHeightProperty().bind(notepane.heightProperty());

		notepane.setPrefSize(notepane.getWidth(), notepane.getHeight());

		System.out.println("about to add");

		schedule.add(notepane, 0, pos);
		scrllparent.setVvalue(0.0);

	}

	/**
	 *
	 */
	public void addNewNote() {

		String toastMsg = "Creating Note";
		int toastMsgTime = 3500; // 3.5 seconds
		int fadeInTime = 500; // 0.5 seconds
		int fadeOutTime = 500; // 0.5 seconds
		Toast.makeText(mainapp.getPrimaryStage(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 2, new Runnable() {

			@Override
			public void run() {
				Note newnote = new Note(patient, mainapp.getCurrentAccount().getStaffNumber(), LocalDate.now(), LocalTime.now(), textfield.getText(), typepicker.getValue().getId());

				int notenum = mainapp.appPrefs.getNoteID() + 1;

				newnote.setId(notenum);

				mainapp.appPrefs.setNoteID(mainapp.appPrefs.getNoteID() + 1);

				if (filepath != null) {
					newnote.setFile(filepath);
					newnote.setFileName(filenamestr);
				}

				mainapp.addNote(newnote);

				System.out.println("Patient: ID " + newnote.getPatient().toString() + " Staff Code: " + newnote.getStaffCode().toString() + " Note ID: " + newnote.getId().toString() + " type: "
						+ newnote.getType().toString() + " property 3: " + newnote.getProperty3().toString());

				mainapp.saveNotesDataToFile(mainapp.getFilePath(), false);
				mainapp.savePreferenceDataToFile(mainapp.getFilePath());
				mainapp.setVersion();

				System.out.println("Adding new note 9");

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						textfield.clear();
						refreshnotes();
						filebutton.setText("ADD FILE");
						filename.setText("");
						filepath = null;
						filenamestr = null;
					}
				});

			}
		});

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
							System.out.println("Creating folder: " + check.getAbsolutePath());
							System.out.println("Permission to create = " + mainapp.getFilePath().canWrite());
							File newDirectory = new File(check.getAbsolutePath());
							System.out.println("Creating folder success = " + newDirectory.mkdirs());
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
							System.out.println(toFile.getAbsolutePath());

							Path from = Paths.get(addfile.toURI());
							Path to = Paths.get(toFile.toURI());

							System.out.println(to.toString());

							new Thread() {
								@Override
								public void run() {

									try {
										toFile.createNewFile();
										Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
									} catch (IOException e) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												filebutton.setText("UPLOAD FAILED");
											}
										});
										mainapp.writeError(e);
										e.printStackTrace();
									}
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
							}.start();

						}

					}
				};

				filethread.start();
			}

		}
	}

	/**
	 *
	 */
	public void show() {
		addbutton.getStyleClass().clear();
		addbutton.getStyleClass().add("card-orange");
		addbutton.setDisable(false);

		notewindowbutton.getStyleClass().clear();
		notewindowbutton.getStyleClass().add("card-mid-lowpadding");
		notewindowbutton.setDisable(false);

		filebutton.getStyleClass().clear();
		filebutton.getStyleClass().add("button");
		filebutton.setDisable(false);

		textfield.getStyleClass().clear();
		textfield.getStyleClass().add("textfield");
		textfield.setDisable(false);

		titlebox.getStyleClass().clear();
		titlebox.getStyleClass().add("card");

		bottombox.setVisible(true);
		bottombox.setManaged(true);
	}

	/**
	 *
	 */
	public void hide() {
		schedule.getChildren().clear();
		addbutton.getStyleClass().clear();
		addbutton.getStyleClass().add("card2");
		addbutton.setDisable(true);

		notewindowbutton.getStyleClass().clear();
		notewindowbutton.getStyleClass().add("card2");
		notewindowbutton.setDisable(true);

		filebutton.setDisable(true);

		textfield.getStyleClass().clear();
		textfield.getStyleClass().add("textfield-disabled");
		textfield.setDisable(true);

		titlebox.getStyleClass().clear();
		titlebox.getStyleClass().add("card2");
	}

	/**
	 *
	 */
	public void hidebutton() {
		bottombox.setVisible(false);
		bottombox.setManaged(false);
	}

	@FXML
	public void editNoteTypes() {
		mainapp.editNoteTypes();
	}

	@FXML
	public void openNoteWindow() {
		mainapp.openNoteWindow(patient);
	}

}
