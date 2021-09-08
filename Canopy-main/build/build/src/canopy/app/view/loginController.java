package canopy.app.view;

import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import canopy.app.MainApp;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 *
 * @author Andy
 */
public class loginController {

	// Reference to the main application
	private MainApp mainapp;
	private double screenwidth;
	private double screenheight;
	private boolean coverup = true;

	@FXML
	private AnchorPane coverpane;
	@FXML
	private AnchorPane coverpane2;
	@FXML
	private AnchorPane mainpane;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Label incorrectnamepass;
	@FXML
	private Label correctnamepass;
	@FXML
	private Label logintext;
	@FXML
	private ImageView padlockbody;
	@FXML
	private ImageView padlocktop;
	@FXML
	private ImageView coverpiece;
	@FXML
	private ImageView coverpiece2;
	@FXML
	private GridPane loginPane;
	@FXML
	private VBox boxvbox;
	@FXML
	private Label covertime;
	@FXML
	private Label coverdate;
	@FXML
	private Label covertime1;
	@FXML
	private Label coverdate1;
	

	DropShadow dropShadow;
	DropShadow padlockShadow;
	DropShadow covershadow;

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
     * @param width
     * @param height
     */
    public void initialise(double width, double height) {
    	
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("EEEE dd MMMM");
		covertime.setText(LocalTime.now().format(timeformatter));
		coverdate.setText(LocalDate.now().format(dateformatter));
		
		covertime1.setText(LocalTime.now().format(timeformatter));
		coverdate1.setText(LocalDate.now().format(dateformatter));		
		
		incorrectnamepass.setVisible(false);
		correctnamepass.setVisible(false);

		username.setPromptText("Username");
		password.setPromptText("Password");
		username.requestFocus();

		screenwidth = width;
		screenheight = height;
		coverpane.setPrefWidth(screenwidth);
		coverpane.setPrefHeight(screenheight);
		coverpane.setEffect(new Glow(0.1));
		coverpane2.setEffect(new Glow(0.1));

		coverpane2.setPrefWidth(screenwidth);
		coverpane2.setPrefHeight(screenheight);
		coverpane2.setTranslateX(screenwidth);
		
		coverpane.setCache(true);
		coverpane.setCacheShape(true);
		coverpane.setCacheHint(CacheHint.SPEED);
		coverpane2.setCache(true);
		coverpane2.setCacheShape(true);
		coverpane2.setCacheHint(CacheHint.SPEED);
		
		if(height < 900) {
			padlocktop.setScaleX(height/1200);
			padlocktop.setScaleY(height/1200);
			padlockbody.setScaleX(height/1200);
			padlockbody.setScaleY(height/1200);
		}
		
		username.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (coverup) {
					coverpaneclicked();
				}
				else if (keyEvent.getCode() == KeyCode.ENTER) {
					password.requestFocus();
				}
			}
		});

		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (coverup) {
					coverpaneclicked();
				}
				if (keyEvent.getCode() == KeyCode.ENTER) {
					mainapp.clickLogin(username.getText(), password.getText());
				}
			}
		});
		
		covershadow = new DropShadow();		
		covershadow.setRadius(20);
		covershadow.setOffsetX(40);
		covershadow.setOffsetY(40);
		covershadow.setColor(Color.rgb(0, 0, 0, 0.1));
		coverpiece.setEffect(covershadow);
		coverpiece2.setEffect(covershadow);
		
		coverpiece.setCache(true);
		coverpiece.setCacheHint(CacheHint.SPEED);
		coverpiece2.setCache(true);
		coverpiece2.setCacheHint(CacheHint.SPEED);
		
		generateAnimation(coverpiece);
		
		Timer timer = new Timer();
		


		timer.schedule(new TimerTask() {
			public void run() {
				
	            Platform.runLater(() -> {
					covertime.setText(LocalTime.now().format(timeformatter));
					coverdate.setText(LocalDate.now().format(dateformatter));
					
					covertime1.setText(LocalTime.now().format(timeformatter));
					coverdate1.setText(LocalDate.now().format(dateformatter));
	            });

			}
		}, 0, 60 * 1000);

	}
	
	private void generateAnimation(ImageView node) {
		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv1 = new KeyValue(node.translateXProperty(), Math.round((0.6-Math.random())*150) , Interpolator.EASE_BOTH);		
		final KeyValue kv2 = new KeyValue(node.translateYProperty(), Math.round((0.5-Math.random())*100) , Interpolator.EASE_BOTH);	
		double rand = Math.random();
		final KeyValue kv2sx = new KeyValue(node.scaleXProperty(), 1 + (0.5-rand)*0.1 , Interpolator.EASE_BOTH);	
		final KeyValue kv2sy = new KeyValue(node.scaleYProperty(), 1 + (0.5-rand)*0.1 , Interpolator.EASE_BOTH);
		final KeyValue kv2d = new KeyValue(covershadow.radiusProperty(), 40*(1 + (0.5-rand)*0.2) , Interpolator.EASE_BOTH);	

		
		final KeyFrame kf2 = new KeyFrame(Duration.millis((1+Math.random())*9000), kv1, kv2, kv2sx, kv2sy, kv2d);
		
		final KeyValue kv3 = new KeyValue(node.translateXProperty(), Math.round((0.6-Math.random())*150) , Interpolator.EASE_BOTH);
		final KeyValue kv4 = new KeyValue(node.translateYProperty(), Math.round((0.5-Math.random())*100) , Interpolator.EASE_BOTH);
		rand = Math.random();
		final KeyValue kv4sx = new KeyValue(node.scaleXProperty(), 1 + (0.5-rand)*0.1 , Interpolator.EASE_BOTH);	
		final KeyValue kv4sy = new KeyValue(node.scaleYProperty(), 1 + (0.5-rand)*0.1 , Interpolator.EASE_BOTH);
		final KeyValue kv4d = new KeyValue(covershadow.radiusProperty(), 40*(1 + (0.5-rand)*0.2) , Interpolator.EASE_BOTH);	

		
		final KeyFrame kf4 = new KeyFrame(Duration.millis((2.5+Math.random())*9000),kv3, kv4, kv4sx, kv4sy, kv4d);

		timeline.getKeyFrames().addAll(kf2,kf4);

		SequentialTransition sequence = new SequentialTransition(timeline);

		sequence.play();

		sequence.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(coverup) {
				generateAnimation(node);}				
			}
		});
	}

    /**
     *
     */
    @FXML
	public void clicked() {
		mainpane.requestFocus();
		coverpaneclicked();
	}

    /**
     *
     */
    public void coverpaneclicked() {
		username.requestFocus();

		if (coverup) {
			coverup = false;
			TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
			closeNav.setToX(-screenwidth);
			closeNav.play();

			dropShadow = new DropShadow();
			dropShadow.setRadius(0);
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setColor(Color.rgb(0, 0, 0, 0.1));

			loginPane.setEffect(dropShadow);
			padlockbody.setEffect(dropShadow);
			padlocktop.setEffect(dropShadow);
			boxvbox.setEffect(dropShadow);
			logintext.setEffect(dropShadow);
			
			
			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 20, Interpolator.EASE_BOTH);
			final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(300), kv, kv1, kv2);
			timeline.getKeyFrames().add(kf);

			closeNav.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					timeline.play();
					coverpiece.setVisible(false);
					coverpiece.setManaged(false);
				}
			});
		}
	}

    /**
     *
     */
    public void loggingIn() {



		final Timeline timeline3 = new Timeline();
		timeline3.setAutoReverse(true);

		final KeyValue kv4 = new KeyValue(loginPane.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv5 = new KeyValue(padlockbody.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv6 = new KeyValue(padlocktop.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv7 = new KeyValue(correctnamepass.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv7a = new KeyValue(logintext.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv7b = new KeyValue(boxvbox.translateXProperty(), 10, Interpolator.EASE_BOTH);
		final KeyValue kv7c = new KeyValue(coverpane2.translateXProperty(), 10 + screenwidth, Interpolator.EASE_BOTH);
		final KeyFrame kf4 = new KeyFrame(Duration.millis(300), kv4, kv5, kv6, kv7,kv7a,kv7b, kv7c);

		timeline3.getKeyFrames().addAll(kf4);

		final Timeline timeline4 = new Timeline();
		timeline4.setAutoReverse(true);

		final KeyValue kv8 = new KeyValue(loginPane.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv9 = new KeyValue(padlockbody.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv10 = new KeyValue(padlocktop.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv11 = new KeyValue(correctnamepass.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv11a = new KeyValue(logintext.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv11b = new KeyValue(boxvbox.translateXProperty(), -screenwidth, Interpolator.EASE_BOTH);
		final KeyValue kv11c = new KeyValue(coverpane2.translateXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf5 = new KeyFrame(Duration.millis(1600), kv8, kv9, kv10, kv11, kv11a, kv11b, kv11c);

		timeline4.getKeyFrames().addAll(kf5);

		SequentialTransition sequence = new SequentialTransition(timeline3, timeline4);
		
		coverpiece2.setVisible(true);
		sequence.play();

		sequence.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainapp.changescreen();

			}
		});

	}

    /**
     *
     */
    public void logInPressed() {

		String usernamestr = username.getText();
		String passwordstr = password.getText();
		mainapp.clickLogin(usernamestr, passwordstr);
	}

    /**
     *
     */
    public void incorrectnamepass() {
		incorrectnamepass.setVisible(true);
		correctnamepass.setVisible(false);
	}

    /**
     *
     */
    public void correctnamepass() {
		correctnamepass.setVisible(true);
		incorrectnamepass.setVisible(false);
		
		final Timeline timeline = new Timeline();
		timeline.setAutoReverse(true);
		final KeyValue kv1 = new KeyValue(padlocktop.translateYProperty(), 2, Interpolator.EASE_BOTH);
		final KeyValue kv1b = new KeyValue(padlockbody.translateYProperty(), -2, Interpolator.EASE_BOTH);
		final KeyFrame kf1 = new KeyFrame(Duration.millis(200), kv1, kv1b);

		final KeyValue kv2 = new KeyValue(padlocktop.translateYProperty(), -15, Interpolator.EASE_BOTH);
		final KeyValue kv2b = new KeyValue(padlockbody.translateYProperty(), 15, Interpolator.EASE_BOTH);

		final KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2, kv2b);

		timeline.getKeyFrames().addAll(kf1, kf2);

		final Timeline timeline2 = new Timeline();
		timeline2.setAutoReverse(true);

		final KeyValue kv3 = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kvo1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
		final KeyValue kvo2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
		final KeyFrame kf3 = new KeyFrame(Duration.millis(300), kv3, kvo1, kvo2);

		timeline2.getKeyFrames().addAll(kf3);
		
		SequentialTransition sequence = new SequentialTransition(timeline, timeline2);

		sequence.play();		
	}
    
    public void setLoginText(String text) {
    	
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				correctnamepass.setText(text);  				
			}
		});    	  	
    }

    /**
     *
     */
    @FXML
	public void colourYellow() {
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(1.0);
		lighting.setSpecularConstant(0.0);
		lighting.setSpecularExponent(0.0);
		lighting.setSurfaceScale(0.0);
		lighting.setLight(new Light.Distant(90, 90, Color.web("#ffba08")));
		// dropShadow.setInput(lighting);
	}

    /**
     *
     */
    @FXML
	public void colourWhite() {
		// dropShadow.setInput(null);
	}

    /**
     *
     */
    @FXML
	public void changefilepath() {
		mainapp.ReChooseFilePath();
	}

}
