package canopy.app.util;

import javax.xml.stream.events.StartDocument;

import canopy.app.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class Toast { // Creates a toast

	// Sample use:

	// String toastMsg = "some text...";
	// int toastMsgTime = 3500; //3.5 seconds
	// int fadeInTime = 500; //0.5 seconds
	// int fadeOutTime= 500; //0.5 seconds
	// Toast.makeText(primarystage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);

	/**
	 *
	 * @param ownerStage
	 * @param toastMsg
	 * @param toastDelay
	 * @param fadeInDelay
	 * @param fadeOutDelay
	 */

	public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay, int Position) {

		try {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					Stage toastStage = new Stage();
					toastStage.initOwner(ownerStage);
					// toastStage.setResizable(true);
					toastStage.initStyle(StageStyle.TRANSPARENT);

					HBox hBox = new HBox();
					hBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-padding: 20px;");
					VBox vBox = new VBox();
					vBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);-fx-padding: 20px;");

					hBox.fillHeightProperty().set(true);
					hBox.setPrefWidth(ownerStage.getWidth());
					vBox.fillWidthProperty().set(true);
					vBox.setPrefHeight(ownerStage.getHeight());

					switch (Position) {
					case 0: // Bottom left
						hBox.setAlignment(Pos.BOTTOM_LEFT);
						vBox.setAlignment(Pos.BOTTOM_LEFT);
						break;
					case 1: // Bottom Right
						hBox.setAlignment(Pos.BOTTOM_RIGHT);
						vBox.setAlignment(Pos.BOTTOM_RIGHT);
						break;
					case 2: // Center
						hBox.setAlignment(Pos.CENTER);
						vBox.setAlignment(Pos.CENTER);
						break;

					default:
						break;
					}

					Text text = new Text(toastMsg);
					text.setFont(Font.font("Verdana", 15));
					text.setFill(Color.WHITE);

					StackPane root = new StackPane(text);
					root.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(141, 198, 63, 0.8); -fx-padding: 20px;");
					root.setAlignment(Pos.BOTTOM_CENTER);

					hBox.getChildren().add(root);
					vBox.getChildren().add(hBox);

					Scene scene = new Scene(vBox);
					scene.setFill(Color.TRANSPARENT);
					toastStage.setScene(scene);
					toastStage.show();

					Timeline fadeInTimeline = new Timeline();
					KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
					fadeInTimeline.getKeyFrames().add(fadeInKey1);
					fadeInTimeline.setOnFinished((ae) -> {
						new Thread(() -> {
							try {
								Thread.sleep(toastDelay);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Timeline fadeOutTimeline = new Timeline();
							KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
							fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
							fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
							fadeOutTimeline.play();
						}).start();
					});
					fadeInTimeline.play();

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Same as above, but runs task AFTER toast initiated
	public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay, int Position, Runnable task) {

		try {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					Stage toastStage = new Stage();
					toastStage.initOwner(ownerStage);
					// toastStage.setResizable(true);
					toastStage.initStyle(StageStyle.TRANSPARENT);

					HBox hBox = new HBox();
					hBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-padding: 20px;");
					VBox vBox = new VBox();
					vBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);-fx-padding: 20px;");

					hBox.fillHeightProperty().set(true);
					hBox.setPrefWidth(ownerStage.getWidth());
					vBox.fillWidthProperty().set(true);
					vBox.setPrefHeight(ownerStage.getHeight());

					switch (Position) {
					case 0: // Bottom left
						hBox.setAlignment(Pos.BOTTOM_LEFT);
						vBox.setAlignment(Pos.BOTTOM_LEFT);
						break;
					case 1: // Bottom Right
						hBox.setAlignment(Pos.BOTTOM_RIGHT);
						vBox.setAlignment(Pos.BOTTOM_RIGHT);
						break;
					case 2: // Center
						hBox.setAlignment(Pos.CENTER);
						vBox.setAlignment(Pos.CENTER);
						break;

					default:
						break;
					}

					Text text = new Text(toastMsg);
					text.setFont(Font.font("Verdana", 15));
					text.setFill(Color.WHITE);

					StackPane root = new StackPane(text);
					root.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(141, 198, 63, 0.8); -fx-padding: 20px;");
					root.setAlignment(Pos.BOTTOM_CENTER);

					hBox.getChildren().add(root);
					vBox.getChildren().add(hBox);

					Scene scene = new Scene(vBox);
					scene.setFill(Color.TRANSPARENT);
					toastStage.setScene(scene);
					toastStage.show();

					Timeline fadeInTimeline = new Timeline();
					KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
					fadeInTimeline.getKeyFrames().add(fadeInKey1);
					fadeInTimeline.setOnFinished((ae) -> {
						new Thread(() -> {
							try {
								Thread.sleep(toastDelay);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Timeline fadeOutTimeline = new Timeline();
							KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
							fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
							fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
							fadeOutTimeline.play();
						}).start();
					});
					// fadeInKey1.getOnFinished().handle(new ActionEventt);
					fadeInTimeline.play();

					new Thread() {
						public void run() {
							task.run();
						}
					}.start();

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
