package canopy.app.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.Validate;

import canopy.app.MainApp;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class borderPaneController {

	// Reference to the main application
	private MainApp mainapp;
	
	private double screenwidth;
	private double screenheight;
	private boolean coverup = true;

	@FXML
	private AnchorPane coverpane;

	@FXML
	private VBox plannerbox;
	
	@FXML
	private HBox centercontainer;

	@FXML
	private VBox patientbox;

	@FXML
	private VBox mailbox;

	@FXML
	private VBox roomsbox;
	
	@FXML
	private VBox typesbox;

	@FXML
	private VBox logoutbox;

	@FXML
	private BorderPane borderpane;

	@FXML
	private AnchorPane rightpane;
	
	@FXML
	private AnchorPane tabpane;

	@FXML
	private GridPane sidepane;

	@FXML
	private AnchorPane centerpane;
	
	@FXML
	private AnchorPane centersecondpane;
	
	@FXML
	private AnchorPane centerthirdpane;
	
	@FXML
	private ImageView planner;
	
	@FXML
	private ImageView patients;
	
	@FXML
	private ImageView mail;
	
	@FXML
	private ImageView rooms;
	
	@FXML
	private ImageView types;
	
	@FXML
	private ImageView logout;
	
	@FXML
	private ImageView backgroundimage;
	
	@FXML
	private ImageView coverpiece;
	
	@FXML
	private Label plannerlab;
	
	@FXML
	private Label patientslab;
	
	@FXML
	private Label maillab;
	
	@FXML
	private Label roomslab;
	
	@FXML
	private Label typeslab;
	
	@FXML	
	private Label covertime;
	
	@FXML
	private Label coverdate;

	int hovered = 8;
	int selected = 8;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param main
	 */
	public void setMainApp(MainApp main) {
		this.mainapp = main;
	}

    /**
     *
     * @param node
     */
    public void setCenterContent(Node node) {
		centerpane.setManaged(true);
		centerpane.getChildren().add(node);	
	}
	
    /**
     *
     * @param node
     */
    public void setCenterSecondContent(Node node) {		
		centersecondpane.setManaged(true);
		centersecondpane.getChildren().add(node);
	}
	
    /**
     *
     * @param node
     */
    public void setCenterThirdContent(Node node) {		
		centerthirdpane.setManaged(true);
		centerthirdpane.getChildren().add(node);
	}

    /**
     *
     * @param node
     */
    public void bindCenterSize(AnchorPane node) {	
		node.maxWidthProperty().bind(centerpane.widthProperty());
		node.maxHeightProperty().bind(centerpane.heightProperty());
		
		centerpane.setPrefWidth(centercontainer.getWidth());
		
		centerpane.autosize();
	}
	
    /**
     *
     * @param node
     * @param initialnode
     */
    public void bindCenterSecondSize(AnchorPane node, AnchorPane initialnode) {
		node.maxWidthProperty().bind(centersecondpane.widthProperty());
		node.maxHeightProperty().bind(centersecondpane.heightProperty());
		initialnode.maxWidthProperty().bind(centerpane.widthProperty());
		initialnode.maxHeightProperty().bind(centerpane.heightProperty());
		centerpane.setPrefWidth(centercontainer.getWidth()/2);
		centersecondpane.setPrefWidth(centercontainer.getWidth()/2);
		
		centerpane.autosize();
		centersecondpane.autosize();

		
	}
	
    /**
     *
     * @param node
     * @param initialnode
     */
    public void bindCenterThirdSize(AnchorPane node, AnchorPane initialnode) {
    	System.out.println("Binding Third Pane Size");
		node.maxHeightProperty().bind(centerthirdpane.heightProperty());
		node.maxWidthProperty().bind(centerthirdpane.widthProperty());
		initialnode.maxWidthProperty().bind(centerpane.widthProperty());
		initialnode.maxHeightProperty().bind(centerpane.heightProperty());
		
		centerpane.setPrefWidth(centercontainer.getWidth()*32/100);
		centersecondpane.setPrefWidth(centercontainer.getWidth()*30/100);
		centerthirdpane.setPrefWidth(centercontainer.getWidth()*38/100);
		
		centerpane.autosize();
		centersecondpane.autosize();
		centerthirdpane.autosize();
		
//		node.setPrefWidth(centerthirdpane.getWidth());
//		node.setPrefHeight(centerthirdpane.getHeight());

	}

    /**
     *
     * @param node
     * @param inset
     */
    public void bindRightsize(AnchorPane node, double inset) {
		 node.maxHeightProperty().bind(mainapp.getPrimaryStage().heightProperty());
	}

    /**
     *
     * @param width
     * @param height
     * @param topinset
     * @param bottominset
     */
    public void initialise(double width, double height, double topinset, double bottominset) {
    	
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("EEEE dd MMMM");
		covertime.setText(LocalTime.now().format(timeformatter));
		coverdate.setText(LocalDate.now().format(dateformatter));
		
		screenwidth = width;
		screenheight = height;// - bottominset;
		borderpane.setPrefWidth(screenwidth);
		borderpane.setPrefHeight(screenheight);
		centercontainer.maxHeightProperty().bind(borderpane.heightProperty());
		centerpane.prefHeightProperty().bind(borderpane.heightProperty());
		
		coverpane.setEffect(new Glow(0.1));
		coverpane.setCache(true);
		coverpane.setCacheShape(true);
		coverpane.setCacheHint(CacheHint.SPEED);
		
		coverpane.setTranslateY(0);
		removecoverpane();
		planhovered();
		planclicked();
		centerpane.visibleProperty().bind(centerpane.managedProperty());
		centerpane.setManaged(true);
		centerpane.setCache(true);
		centerpane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		centersecondpane.visibleProperty().bind(centersecondpane.managedProperty());
		centersecondpane.setManaged(false);
		centersecondpane.setCache(true);
		centersecondpane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		centerthirdpane.visibleProperty().bind(centerthirdpane.managedProperty());
		centerthirdpane.setManaged(false);
		centerthirdpane.setCache(true);
		centerthirdpane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		rightpane.visibleProperty().bind(rightpane.managedProperty());
		rightpane.setManaged(true);		
		rightpane.setCache(true);
		rightpane.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		DropShadow covershadow = new DropShadow();		
		covershadow.setRadius(20);
		covershadow.setOffsetX(40);
		covershadow.setOffsetY(40);
		covershadow.setColor(Color.rgb(0, 0, 0, 0.1));
		coverpiece.setEffect(covershadow);
		
		coverpiece.setCache(true);
		coverpiece.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		centerpane.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
			if(newValue.getHeight() < 600 || newValue.getWidth() < 600) {
				if(backgroundimage.visibleProperty().get()) {
					backgroundimage.setVisible(false);
					backgroundimage.setManaged(false);
				}
			}
			
			if(newValue.getHeight() > 600 && newValue.getWidth() > 600) {
				if(!backgroundimage.visibleProperty().get()) {
					backgroundimage.setVisible(true);
					backgroundimage.setManaged(true);
				}
			}
		});    
		
		syncThread();
		
		Double heightval = plannerbox.getHeight();
		
		if(heightval > 100) {
		planner.setFitWidth(heightval/2);	
		patients.setFitWidth(heightval/2);
		mail.setFitWidth(heightval/2);	
		rooms.setFitWidth(heightval/2);
		types.setFitWidth(heightval/2);
		logout.setFitWidth(heightval/2);
		if(!planner.visibleProperty().get()) {
			planner.setVisible(true);
			patients.setVisible(true);
			mail.setVisible(true);
			rooms.setVisible(true);
			types.setVisible(true);
			logout.setVisible(true);
			planner.setManaged(true);
			patients.setManaged(true);
			mail.setManaged(true);
			rooms.setManaged(true);
			types.setManaged(true);
			logout.setManaged(true);
		}
		}
		else {
			planner.setVisible(false);
			patients.setVisible(false);
			mail.setVisible(false);
			rooms.setVisible(false);
			types.setVisible(false);
			logout.setVisible(false);
			planner.setManaged(false);
			patients.setManaged(false);
			mail.setManaged(false);
			rooms.setManaged(false);
			types.setManaged(false);
			logout.setManaged(false);
		}		
		
		plannerbox.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Double value = (Double) newValue;
				
				if(value > 100) {
				planner.setFitWidth(value/2);	
				patients.setFitWidth(value/2);
				mail.setFitWidth(value/2);	
				rooms.setFitWidth(value/2);
				types.setFitWidth(value/2);
				logout.setFitWidth(value/2);
				if(!planner.visibleProperty().get()) {
					planner.setVisible(true);
					patients.setVisible(true);
					mail.setVisible(true);
					rooms.setVisible(true);
					types.setVisible(true);
					logout.setVisible(true);
					planner.setManaged(true);
					patients.setManaged(true);
					mail.setManaged(true);
					rooms.setManaged(true);
					types.setManaged(true);
					logout.setManaged(true);
				}
				}
				else {
					planner.setVisible(false);
					patients.setVisible(false);
					mail.setVisible(false);
					rooms.setVisible(false);
					types.setVisible(false);
					logout.setVisible(false);
					planner.setManaged(false);
					patients.setManaged(false);
					mail.setManaged(false);
					rooms.setManaged(false);
					types.setManaged(false);
					logout.setManaged(false);
				}
				

			}
		});
		
		plannerbox.setCache(true);
		plannerbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		patientbox.setCache(true);
		patientbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		mailbox.setCache(true);
		mailbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		roomsbox.setCache(true);
		roomsbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		typesbox.setCache(true);
		typesbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		logoutbox.setCache(true);
		logoutbox.setCacheHint(CacheHint.SPEED); // Sets to prioritising animation
		
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			public void run() {
				
	            Platform.runLater(() -> {
					covertime.setText(LocalTime.now().format(timeformatter));
					coverdate.setText(LocalDate.now().format(dateformatter));
	            });

			}
		}, 50, 60 * 1000);
		
	}

    /**
     *
     * @param node
     */
    public void addRight(AnchorPane node) {
		rightpane.setManaged(true);
		rightpane.getChildren().add(node);
		node.prefWidthProperty().bind(rightpane.widthProperty());
	}
	
    /**
     *
     */
    public void clear() {
		centerpane.getChildren().clear();
		centersecondpane.getChildren().clear();
		centerthirdpane.getChildren().clear();
		rightpane.getChildren().clear();
		rightpane.setManaged(false);
		centerpane.setManaged(false);
		centersecondpane.setManaged(false);
		centerthirdpane.setManaged(false);
	}

    /**
     *
     */
    public void removecoverpane() {
		if (coverup) {
			coverpane.setVisible(true);
			coverpane.setManaged(true);			
			
			coverup = false;
			
			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv1 = new KeyValue(coverpane.translateXProperty(), 0, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(300), kv1);
			
			timeline.getKeyFrames().addAll(kf);
			
			final Timeline timeline2 = new Timeline();
			timeline2.setAutoReverse(true);
			
			final KeyValue kv2 = new KeyValue(coverpane.translateXProperty(), -screenwidth-10, Interpolator.EASE_BOTH);
			final KeyFrame kf2 = new KeyFrame(Duration.millis(1600),kv2);
			
			timeline2.getKeyFrames().addAll(kf2);
			
			SequentialTransition sequence = new SequentialTransition(timeline,timeline2);
			
			sequence.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(coverup) {
					coverpane.setVisible(false);
					coverpane.setManaged(false);
					}				
				}
			});
			
			sequence.play();
		}
	}

    /**
     *
     * @return
     */
    public BorderPane getBorderPane() {
		return this.borderpane;
	}

    /**
     *
     */
    public void planclicked() {
		if (selected != 0) {
			mainapp.setToPlannerScreen();
			colourYellow(planner,plannerlab);
			colourWhite(patients,patientslab);
			colourWhite(mail,maillab);
			colourWhite(rooms,roomslab);
			colourWhite(types, typeslab);
		}
		selected = 0;
		pullPlannerTab(0, 1);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}

    /**
     *
     */
    public void patientsclicked() {
		if (selected != 1) {
			mainapp.setToPatientScreen();
			colourWhite(planner,plannerlab);
			colourYellow(patients,patientslab);
			colourWhite(mail,maillab);
			colourWhite(rooms,roomslab);
			colourWhite(types, typeslab);
		}
		selected = 1;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 1);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}

    /**
     *
     */
    public void mailclicked() {
		if(selected!=2) {
			mainapp.setToMailScreen();
			colourWhite(planner,plannerlab);
			colourWhite(patients,patientslab);
			colourYellow(mail,maillab);
			colourWhite(rooms,roomslab);
			colourWhite(types, typeslab);
		}
		selected = 2;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 1);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}

    /**
     *
     */
    public void roomsclicked() {
		if (selected != 3) {
			mainapp.setToRoomScreen();			
			colourWhite(planner,plannerlab);
			colourWhite(patients,patientslab);
			colourWhite(mail,maillab);
			colourYellow(rooms,roomslab);
			colourWhite(types, typeslab);
		}
		selected = 3;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 1);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}
	
    /**
     *
     */
    public void typesclicked() {
		if (selected != 4) {
			mainapp.setToTypeScreen();			
			colourWhite(planner,plannerlab);
			colourWhite(patients,patientslab);
			colourWhite(mail,maillab);
			colourWhite(rooms,roomslab);
			colourYellow(types, typeslab);
		}
		selected = 4;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullTypeTab(1, 1);
		pullLogOutTab(1, 0);

	}

    /**
     *
     */
    public void logoutclicked() {
		if(selected != 5) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");			
		
	        alert.initOwner(mainapp.getPrimaryStage());
	        alert.setTitle("Log out?");
	        alert.setHeaderText("Do you wish to log out?");

	        ButtonType buttonTypeOne = new ButtonType("Log Out");
	        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

	        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == buttonTypeOne){
	        	
	    		
	        	
	        	coverpane.setTranslateX(screenwidth);
	        	
				final Timeline timeline3 = new Timeline();
				timeline3.setAutoReverse(true);
				
				final KeyValue kv7c = new KeyValue(coverpane.translateXProperty(), 10+screenwidth, Interpolator.EASE_BOTH);
				final KeyFrame kf4 = new KeyFrame(Duration.millis(300), kv7c);
				
				timeline3.getKeyFrames().addAll(kf4);
				
				final Timeline timeline4 = new Timeline();
				timeline4.setAutoReverse(true);
				
				final KeyValue kv11c = new KeyValue(coverpane.translateXProperty(), 0, Interpolator.EASE_BOTH);
				final KeyFrame kf5 = new KeyFrame(Duration.millis(1600), kv11c);
				
				timeline4.getKeyFrames().addAll(kf5);
				
				SequentialTransition sequence = new SequentialTransition(timeline3,timeline4);
				
				sequence.play();
				
				sequence.setOnFinished(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						mainapp.initLogin();						
					}
				});
	        } else {
	            
	        }
		}		

	}

    /**
     *
     */
    public void planhovered() {
		hovered = 0;
		pullPlannerTab(0, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}

    /**
     *
     */
    public void patientshovered() {
		hovered = 1;
		pullPlannerTab(1, 0);
		pullPatientTab(0, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);
	}

    /**
     *
     */
    public void mailhovered() {
		hovered = 2;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(0, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}

    /**
     *
     */
    public void roomhovered() {
		hovered = 3;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(0, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(1, 0);

	}
	
    /**
     *
     */
    public void typehovered() {
		hovered = 4;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullLogOutTab(1, 0);
		pullTypeTab(0, 0);


	}

    /**
     *
     */
    public void loghovered() {
		hovered = 5;
		pullPlannerTab(1, 0);
		pullPatientTab(1, 0);
		pullMailTab(1, 0);
		pullRoomTab(1, 0);
		pullTypeTab(1, 0);
		pullLogOutTab(0, 0);
		

	}

    /**
     *
     * @param direction
     * @param type
     */
    public void pullPlannerTab(int direction, int type) {
		// direction: 0 = up, 1 = down

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;

		if (type == 1 || selected != 0) {

			if (plannerbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				plannerbox.setEffect(dropShadow);
				plannerbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) plannerbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						plannerbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						plannerbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					plannerbox.setStyle("-fx-background-color: #e1e1e1;");
				}
			}
			if (plannerbox.getEffect() != null) {		
				
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}

    /**
     *
     * @param direction
     * @param type
     */
    public void pullPatientTab(int direction, int type) {
		// direction: 0 = up, 1 = down

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;
		if (type == 1 || selected != 1) {

			if (patientbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				patientbox.setEffect(dropShadow);
				patientbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) patientbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						patientbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						patientbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					patientbox.setStyle("-fx-background-color: #e1e1e1;");
				}
			}

			if (patientbox.getEffect() != null) {
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}

    /**
     *
     * @param direction
     * @param type
     */
    public void pullMailTab(int direction, int type) {
		// direction: 0 = up, 1 = down

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;
		if (type == 1 || selected != 2) {

			if (mailbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				mailbox.setEffect(dropShadow);
				mailbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) mailbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						mailbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						mailbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					mailbox.setStyle("-fx-background-color: #e1e1e1;");
				}
			}
			if (mailbox.getEffect() != null) {
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}

    /**
     *
     * @param direction
     * @param type
     */
    public void pullRoomTab(int direction, int type) {
		// direction: 0 = up, 1 = down

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;
		if (type == 1 || selected != 3) {

			if (roomsbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				roomsbox.setEffect(dropShadow);
				roomsbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) roomsbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						roomsbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						roomsbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					roomsbox.setStyle("-fx-background-color: #e1e1e1;");
				}

			}

			if (roomsbox.getEffect() != null) {
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}
	
    /**
     *
     * @param direction
     * @param type
     */
    public void pullTypeTab(int direction, int type) {
		// direction: 0 = up, 1 = down

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;
		if (type == 1 || selected != 4) {

			if (typesbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				typesbox.setEffect(dropShadow);
				typesbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) typesbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						typesbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						typesbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					typesbox.setStyle("-fx-background-color: #e1e1e1;");
				}

			}

			if (typesbox.getEffect() != null) {
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}

    /**
     *
     * @param direction
     * @param type
     */
    public void pullLogOutTab(int direction, int type) {
		// direction: 0 = up, 1 = down
		// type: 0 = hovered, 1 = clicked

		double height = 1;
		double offset = 1;
		DropShadow dropShadow;
		if (type == 1 || selected != 5) {

			if (logoutbox.getEffect() == null && direction == 0) {
				dropShadow = new DropShadow();
				dropShadow.setRadius(1);
				dropShadow.setOffsetX(1);
				dropShadow.setOffsetY(1);
				dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
				logoutbox.setEffect(dropShadow);
				logoutbox.setStyle("-fx-background-color: #8dc63f;");
				height = 40;
			} else {
				dropShadow = (DropShadow) logoutbox.getEffect();
				if (type == 0) {
					if (direction == 1) {
						height = 1;
						offset = 1;
						logoutbox.setStyle("-fx-background-color: #6eb62e;");
					} else {
						height = 40;
						offset = 3;
						logoutbox.setStyle("-fx-background-color: #8dc63f;");
					}
				} else {
					height = 1;
					offset = 1;
					logoutbox.setStyle("-fx-background-color: #e1e1e1;");
				}
			}

			if (logoutbox.getEffect() != null) {
				final Timeline timeline = new Timeline();
				timeline.setAutoReverse(true);
				final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), height, Interpolator.EASE_BOTH);
				final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), offset, Interpolator.EASE_BOTH);
				final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), offset, Interpolator.EASE_BOTH);
				final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		}
	}
	
    /**
     *
     * @param node
     * @param textnode
     */
    public void colourYellow(Node node, Node textnode) {	
		
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(90, 90, Color.web("#ffba08")));
        
        textnode.setStyle("-fx-text-fill: #ffba08;");
		
		node.setEffect(lighting);
	}

    /**
     *
     * @param node
     * @param textnode
     */
    public void colourWhite(Node node, Node textnode) {
		node.setEffect(null);
		
		textnode.setStyle("-fx-text-fill: white;");
	}
	
    /**
     *
     */
    public void syncThread() {	

		
		new Timer().schedule(
			    new TimerTask() {

			        @Override
			        public void run() {
		                if(mainapp.selectedtab != 5) {
		                    Platform.runLater(new Runnable() {
		                        @Override public void run() {
		    		                mainapp.syncThread();
		                        }
		                    });}
			        }
			    }, 0, 10000);
	}

}
