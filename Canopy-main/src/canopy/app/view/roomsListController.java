package canopy.app.view;

import java.util.Collections;
import java.util.Comparator;

import canopy.app.MainApp;
import canopy.app.model.Appointment;
import canopy.app.model.Building;
import canopy.app.model.Room;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author Andy
 */
public class roomsListController {

	@FXML
	private GridPane list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private MainApp mainapp;
	@FXML
	private ComboBox<Building> buildingbox;
	@FXML
	private ComboBox<String> sorterselection;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();

	ObservableList<Room> rooms;
	int pos = 0;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;

		if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3 || mainapp.getCurrentAccount().getPermission() == 6) {
			addbutton.setVisible(false);
			addbutton.setManaged(false);
		}
	}

	/**
	 *
	 */
	public void initialise() {

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		list.prefWidthProperty().bind(scrllparent.widthProperty());

		buildingbox.setItems(mainapp.getBuildings());
		if (!mainapp.getBuildings().isEmpty()) {
			buildingbox.setValue(mainapp.getBuildings().get(0));
		}

		StringConverter<Building> buildingconverter = new StringConverter<Building>() {

			@Override
			public Building fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Building object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		};

		buildingbox.converterProperty().set(buildingconverter);

		buildingbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Building>() {

			@Override
			public void changed(ObservableValue<? extends Building> observable, Building oldValue, Building newValue) {
				refreshrooms();
			}
		});

		rooms = mainapp.getRooms();

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

		if (sorttypes.size() == 0) {
			sorttypes.add("ID");
			sorttypes.add("Room Name");
		}
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("ID");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				refreshrooms();
			}
		});

		refreshrooms();

	}

	ObservableList<Room> sortRooms(ObservableList<Room> input) {
		ObservableList<Room> output = FXCollections.observableArrayList();
		output.addAll(input);

		switch (sorterselection.getValue()) {
		case "ID":
			Comparator<Room> comparator = Comparator.comparingInt(Room::getId);
			FXCollections.sort(output, comparator);
			break;
		case "Date":
			Collections.sort(output, new Comparator<Room>() {
				public int compare(Room p1, Room p2) {
					return p1.getRoomName().toLowerCase().compareTo(p2.getRoomName().toLowerCase());
				}
			});
			break;
		}

		return output;
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
	 */
	public void refreshrooms() {
		list.getChildren().clear();

		pos = 0;

		ObservableList<Room> rooms = mainapp.getRooms();
		rooms = sortRooms(rooms);

		for (Room room : rooms) {

			if (mainapp.getBuildings().get(room.getBuilding()).getName().equals(buildingbox.getSelectionModel().getSelectedItem().getName())) { // TODO NULL POINTERS

				VBox roompane = new VBox();
				roompane.setAlignment(Pos.CENTER);
				HBox containerbox = new HBox();
				VBox importantbox = new VBox();
				containerbox.prefWidthProperty().bind(roompane.widthProperty());

				containerbox.setAlignment(Pos.CENTER);
				Glow glow = new Glow(0);

				roompane.setEffect(glow);
				roompane.setOnMouseEntered(new EventHandler<Event>() {
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
				
				
				namelab.prefWidthProperty().bind(roompane.widthProperty().subtract(5));
				namelab.setAlignment(Pos.CENTER);
				namelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
				Label doblab = new Label();
				doblab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

				roompane.getStyleClass().add("appointment");

				roompane.setOnMouseExited(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						glow.setLevel(0);
					}
				});
				roompane.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						mainapp.setSelectedRoom(room);
					}

				});

				namelab.setText(room.getRoomName());
				doblab.setText(mainapp.getBuildings().get(room.getBuilding()).getName());

				namelab.getStyleClass().add("label-list-mid");
				doblab.getStyleClass().add("label-list-mid");
				namelab.setWrapText(false);
				//doblab.setWrapText(false);

				roompane.getChildren().add(containerbox);

				containerbox.getChildren().addAll(namelab, importantbox);
				//importantbox.getChildren().addAll(doblab);

				containerbox.prefWidthProperty().bind(roompane.widthProperty());
				containerbox.maxHeightProperty().bind(roompane.heightProperty());


				list.add(roompane, 0, pos);
				pos += 1;

			}
		}
	}

	/**
	 *
	 */
	public void addRoom() {
		mainapp.addNewRoom();
	}

	/**
	 *
	 * @param building
	 */
	public void setSelectedBuildingList(int building) {
		buildingbox.setValue(mainapp.getBuildings().get(building));
		refreshrooms();
	}
}
