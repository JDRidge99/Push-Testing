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
public class mailingListController {

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
		
		if(mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3) {
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
		
		if (sorttypes.size() == 0) {
			sorttypes.add("Name");
			sorttypes.add("Number of Emails");
		}
		

		sorterselection.setItems(sorttypes);
		sorterselection.setValue("Name");

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
	
	ObservableList<MailingList> sortLists(ObservableList<MailingList> input) {
		ObservableList<MailingList> output = FXCollections.observableArrayList();
		output.addAll(input);		

		switch (sorterselection.getValue()) {		
		case "Name":
			Collections.sort(output,
	                 new Comparator<MailingList>()
	                 {
	                     public int compare(MailingList p1, MailingList p2)
	                     {	                    	 
	                         return p1.getListName().toLowerCase().compareTo(p2.getListName().toLowerCase());
	                     }        
	                 });
			break;
			
		case "Number of Emails":
			Collections.sort(output,
	                 new Comparator<MailingList>()
	                 {
	                     public int compare(MailingList p1, MailingList p2)
	                     {
	                    	 String[] emails1 = p1.getList().split(";");
	                    	 String[] emails2 = p2.getList().split(";");
	                    	 int e1 = emails1.length;
	                    	 int e2 = emails2.length;
	                    	 return e2-e1;
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
		
		if(dropShadow !=null) {

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();}

	}
	
	@FXML
	void lift() {

		DropShadow dropShadow = (DropShadow) parent.getEffect();
		
		if(dropShadow !=null) {

		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
		final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
		final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
		timeline.getKeyFrames().add(kf);

		timeline.playFromStart();}

	}

	void refreshlist2() {
		ObservableList<MailingList> data = checkMailListContainment(mainapp.getMailingLists());
		refreshlists(sortLists(data));
	}

	void refreshlist() {
		refreshlists(sortLists(mainapp.getMailingLists()));
	}

    /**
     *
     * @param lists
     */
    public void refreshlists(ObservableList<MailingList> lists) {
		list.getChildren().clear();
                lists.stream().map((mlist) -> {
                    VBox maillistpane = new VBox();
                    maillistpane.setAlignment(Pos.CENTER);
                    HBox containerbox = new HBox();
                    VBox importantbox = new VBox();
                    containerbox.prefWidthProperty().bind(maillistpane.widthProperty());
                    containerbox.setAlignment(Pos.CENTER);
                    Glow glow = new Glow(0);
                    maillistpane.setEffect(glow);
                    maillistpane.setOnMouseEntered(new EventHandler<Event>() {
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
                    maillistpane.getStyleClass().add("appointment");
                    maillistpane.setOnMouseExited(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            glow.setLevel(0);
                        }
                    });
                    maillistpane.setOnMouseClicked(new EventHandler<Event>() {
                        
                        @Override
                        public void handle(Event event) {
                            mainapp.setSelectedMailingList(mlist);
                        }
                        
                    });
                    namelab.setText(mlist.getListName());
                    if(mainapp.getSmallText()) {
                        namelab.getStyleClass().add("label-list");}
                    else {
                        namelab.getStyleClass().add("label-list-mid");
                    }
                    maillistpane.getChildren().add(containerbox);
                    containerbox.getChildren().addAll(namelab, importantbox);
                    containerbox.prefWidthProperty().bind(maillistpane.widthProperty());
                    containerbox.maxHeightProperty().bind(maillistpane.heightProperty());
                return maillistpane;
            }).map((maillistpane) -> {
                list.add(maillistpane, 0, pos);
                return maillistpane;
            }).forEachOrdered((_item) -> {
                pos += 1;
            });
	}

    /**
     *
     */
    @FXML
	public void addList() {
		mainapp.addNewMailingList();
	}

    /**
     *
     * @param data
     * @return
     */
    public ObservableList<MailingList> checkMailListContainment(ObservableList<MailingList> data) {
		ObservableList<MailingList> lists = FXCollections.observableArrayList();

                data.forEach((mailinglist) -> {
                    String name = mailinglist.getListName().toLowerCase();
                    String description = mailinglist.getDescription().toLowerCase();
                    String list = mailinglist.getList().toLowerCase();
                    
                    if (name.startsWith(searchbar.getText().toLowerCase())) {
                        lists.add(mailinglist);
                    } else if (description.contains(searchbar.getText().toLowerCase())) {
                        lists.add(mailinglist);
                    } else if (list.contains(searchbar.getText().toLowerCase())) {
                        lists.add(mailinglist);
                    }
            });
		return lists;
	}
}
