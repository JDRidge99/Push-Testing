package canopy.app.view;

import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Note;
import canopy.app.model.NoteType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;

/**
 *
 * @author Andy
 */
public class NoteTypesDialogController {

	@FXML
	private VBox list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private ColorPicker colourpicker;
	@FXML
	private TextField namefield;

	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainApp;

	/**
	 *
	 * @param mainapp
	 * @param account
	 */
	public void initialise(MainApp mainapp) {
		this.mainApp = mainapp;
		list.getChildren().clear();

		for (NoteType notetype : mainapp.getNoteTypes()) { // Adds note types to list
			addNoteType(notetype);
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

		Task<Void> save = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				mainApp.saveNoteTypeDataToFile(mainApp.getFilePath(), false);
				dialogStage.close();
				return null;
			}
		};
		save.run();

	}

	public void addNoteType(NoteType notetype) {

		VBox typepane = new VBox();
		typepane.setAlignment(Pos.CENTER);
		typepane.setMinWidth(580);

		HBox containerbox = new HBox();
		VBox importantbox = new VBox();
		containerbox.prefWidthProperty().bind(typepane.widthProperty());
		containerbox.setAlignment(Pos.CENTER);
		Glow glow = new Glow(0);
		typepane.setEffect(glow);
		typepane.setOnMouseEntered(new EventHandler<Event>() {
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

		Label infolabel = new Label();
		infolabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		namelab.setWrapText(true);
		infolabel.setWrapText(true);

		ColorPicker colourbox = new ColorPicker();

		String[] rgb = notetype.getColour().split(","); // Splits string into r , g , b

		colourbox.setValue(Color.rgb(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2]))); // Sets colour to rgb version using strings. Yes it's janky.

		colourbox.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {

				String colour = String.valueOf(Math.round(newValue.getRed() * 255)) + "," + String.valueOf(Math.round(newValue.getGreen() * 255)) + ","
						+ String.valueOf(Math.round(newValue.getBlue() * 255)); // Gets string from colour.
				notetype.setColour(colour);

			}
		});

		Button deletebutton = new Button();
		deletebutton.setText("DELETE");

		deletebutton.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
				dialogPane.getStyleClass().add(".dialog-pane");

				alert.setTitle("Delete Note Type?");
				alert.setHeaderText("Are you sure you want to delete this note type?");
				alert.setContentText("All notes with this type will return to the default note type.");

				ButtonType buttonTypeOne = new ButtonType("Delete");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {

					for (Note note : mainApp.getNotes()) {
						if (note.getType().equals(notetype.getId())) {
							note.setType(0);
						}
					}

					mainApp.getNoteTypes().remove(notetype);
					initialise(mainApp);

				} else {

				}

			}
		});

		typepane.getStyleClass().add("appointment");
		typepane.setOnMouseExited(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				glow.setLevel(0);
			}
		});

		namelab.setText(notetype.getTypeName());
		infolabel.setText(notetype.getId().toString());
		namelab.getStyleClass().add("label-list-mid");
		infolabel.getStyleClass().add("label-list");
		colourbox.getStyleClass().add("table-view-notext");		
			
		colourbox.setPadding(insets);
		namelab.setPadding(insets);
		containerbox.setPadding(insets);
		
		typepane.getChildren().add(containerbox);
		containerbox.getChildren().addAll(importantbox, colourbox, deletebutton);
		importantbox.getChildren().addAll(namelab, infolabel);
		containerbox.maxHeightProperty().bind(typepane.heightProperty());
		list.getChildren().add(typepane);
		list.fillWidthProperty().set(true);
		list.setPrefWidth(dialogStage.getWidth());
	}

	@FXML
	void addtype() {
		String error = "";
		if (namefield.getText().trim().equals("")) {
			error += "No valid name";
		}
		
		for(NoteType type:mainApp.getNoteTypes()) {
			if(namefield.getText().trim().equals(type.getTypeName().trim())) {
				error += "A type with this name already exists";
			}
		}

		if (error.length() > 0) {
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
			alert.setContentText(error);

			alert.showAndWait();
		} else {

			String colour = String.valueOf(Math.round(colourpicker.getValue().getRed() * 255)) + "," + String.valueOf(Math.round(colourpicker.getValue().getGreen() * 255)) + ","
					+ String.valueOf(Math.round(colourpicker.getValue().getBlue() * 255)); // Gets string from colour.

			NoteType newtype = new NoteType(namefield.getText(), mainApp.getNoteTypes().size(), colour);

			mainApp.getNoteTypes().add(newtype);
			initialise(mainApp); // Refresh List
			mainApp.refreshnotes();
		}

	}

	@FXML
	void handleCancel() {
		dialogStage.close();
	}

}
