package canopy.app.view;

import java.util.Collections;
import java.util.Comparator;

import canopy.app.MainApp;
import canopy.app.model.AppointmentType;
import canopy.app.model.MailingList;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class appointmentTypeListController {

	@FXML
	private GridPane list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private TextField searchbar;
	@FXML
	private ComboBox<String> sorterselection;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();

	private MainApp mainapp;

	int pos = 0;

    /**
     *
     * @param mainapp
     */
    public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;

		if(mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3 || mainapp.getCurrentAccount().getPermission() == 6) {
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

		list.maxHeightProperty().bind(scrllparent.heightProperty());
		list.prefWidthProperty().bind(scrllparent.widthProperty());

		searchbar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				refreshlist2();
			}
		});
		
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
		
		sorttypes.add("ID");
		sorttypes.add("Name");
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("ID");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				
				if(searchbar.getText().trim().isEmpty()) {
					refreshlist();
				}
				else {
					refreshlist2();
				}
			}
		});
		
		refreshlist();

	}
	
	ObservableList<AppointmentType> sortTypes(ObservableList<AppointmentType> input) {
		ObservableList<AppointmentType> output = FXCollections.observableArrayList();
		output.addAll(input);

		switch (sorterselection.getValue()) {
		case "ID":
			Comparator<AppointmentType> comparator = Comparator.comparingInt(AppointmentType::getId);
			FXCollections.sort(output, comparator);
			break;
		case "Name":
			Collections.sort(output,
	                 new Comparator<AppointmentType>()
	                 {
	                     public int compare(AppointmentType p1, AppointmentType p2)
	                     {	                    	 
	                         return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
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

	void refreshlist2() {
		ObservableList<AppointmentType> data = checkAppointmentTypeContainment(mainapp.getAppointmentTypes());
		refreshlists(sortTypes(data));
	}

	void refreshlist() {
		refreshlists(sortTypes(mainapp.getAppointmentTypes()));
	}

    /**
     *
     * @param lists
     */
    public void refreshlists(ObservableList<AppointmentType> lists) {
		list.getChildren().clear();
                lists.stream().map((type) -> {
                    VBox typepane = new VBox();
                    typepane.setAlignment(Pos.CENTER);
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
                    Label doblab = new Label();
                    doblab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
                    typepane.getStyleClass().add("appointment");
                    typepane.setOnMouseExited(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            glow.setLevel(0);
                        }
                    });
                    typepane.setOnMouseClicked(new EventHandler<Event>() {
                        
                        @Override
                        public void handle(Event event) {
                            mainapp.setSelectedAppointmentType(type);
                        }
                        
                    });
                    namelab.setText(type.getName());
                    if(mainapp.getSmallText()) {
                        namelab.getStyleClass().add("label-list");
                    }
                    else {
                        namelab.getStyleClass().add("label-list-mid");}
                    typepane.getChildren().add(containerbox);
                    containerbox.getChildren().addAll(namelab, importantbox);
                    containerbox.prefWidthProperty().bind(typepane.widthProperty());
                    containerbox.maxHeightProperty().bind(typepane.heightProperty());
                    namelab.setWrapText(true);
                    doblab.setWrapText(true);
                return typepane;
            }).map((typepane) -> {
                list.add(typepane, 0, pos);
                return typepane;
            }).forEachOrdered((_item) -> {
                pos += 1;
            });
	}

    /**
     *
     */
    @FXML
	public void addList() {
		mainapp.addNewAppointmentType();
	}

    /**
     *
     * @param data
     * @return
     */
    public ObservableList<AppointmentType> checkAppointmentTypeContainment(ObservableList<AppointmentType> data) {
		ObservableList<AppointmentType> lists = FXCollections.observableArrayList();

                data.forEach((type) -> {
                    String name = type.getName().toLowerCase();
                    String description = type.getDescription().toLowerCase();
                    
                    if (name.startsWith(searchbar.getText().toLowerCase())) {
                        lists.add(type);
                    } else if (description.contains(searchbar.getText().toLowerCase())) {
                        lists.add(type);
                    }
            });
		return lists;
	}
}
