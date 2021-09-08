package canopy.app.view;

import java.awt.Button;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.Day;
import canopy.app.model.Patient;
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
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
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
public class avaliabilityDialogController {

	@FXML
	private DatePicker fromdatepicker;

	@FXML
	private CheckBox mondaybox;

	@FXML
	private CheckBox tuesdaybox;

	@FXML
	private CheckBox wednesdaybox;

	@FXML
	private CheckBox thursdaybox;

	@FXML
	private CheckBox fridaybox;

	@FXML
	private CheckBox saturdaybox;

	@FXML
	private CheckBox sundaybox;

	@FXML
	private DatePicker todatepicker;

	@FXML
	private AnchorPane coverpane;

	@FXML
	private GridPane list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;

	private boolean saved = false;

	ObservableList<LocalDate> dates = FXCollections.observableArrayList();

	private boolean okClicked = false;
	private Stage dialogStage;
	MainApp mainApp;
	Account account;

    /**
     *
     * @param mainapp
     * @param account
     */
    public void initialise(MainApp mainapp, Account account) {
		this.account = account;
		this.mainApp = mainapp;
		
		dates = account.getHolidayList();

		if (account.avaliableOnDay(0)) {
			mondaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(1)) {
			tuesdaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(2)) {
			wednesdaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(3)) {
			thursdaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(4)) {
			fridaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(5)) {
			saturdaybox.selectedProperty().set(true);
		}
		if (account.avaliableOnDay(6)) {
			sundaybox.selectedProperty().set(true);
		}

		mondaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(0, mondaybox.selectedProperty().getValue());
			}
		});
		tuesdaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(1, tuesdaybox.selectedProperty().getValue());
			}
		});
		wednesdaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(2, wednesdaybox.selectedProperty().getValue());
			}
		});
		thursdaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(3, thursdaybox.selectedProperty().getValue());
			}
		});
		fridaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(4, fridaybox.selectedProperty().getValue());
			}
		});
		saturdaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(5, saturdaybox.selectedProperty().getValue());
			}
		});
		sundaybox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				account.setAvaliableOnDay(6, sundaybox.selectedProperty().getValue());
			}
		});

		refreshdates();
		
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						
						if(dates.contains(item)) {
							setStyle("-fx-background-color: #2e82a9");
						}
						
						switch (item.getDayOfWeek()) {
						case MONDAY:
							if(!mondaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case TUESDAY:
							if(!tuesdaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case WEDNESDAY:
							if(!wednesdaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case THURSDAY:
							if(!thursdaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case FRIDAY:
							if(!fridaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case SATURDAY:
							if(!saturdaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;
						case SUNDAY:
							if(!sundaybox.selectedProperty().get()) {
								setStyle("-fx-background-color: #2e82a9");
							}
							break;

						default:
							break;
						}
					}
				};
			}
		};

		todatepicker.setDayCellFactory(dayCellFactory);
		fromdatepicker.setDayCellFactory(dayCellFactory);
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
     * @return
     */
    public boolean isOkClicked() {
		return okClicked;
	}

    /**
     *
     */
    public void handleOk() {

		coverpane.setTranslateX(dialogStage.getWidth());
		TranslateTransition closeNav = new TranslateTransition(new Duration(500), coverpane);
		closeNav.setToX(0);

		closeNav.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!saved) {
					mainApp.saveAccountDataToFile(mainApp.getFilePath(),false);
				}
				mainApp.setVersion();
				saved = true;
				okClicked = true;
				dialogStage.close();
			}

		});
		closeNav.play();
	}

    /**
     *
     */
    public void refreshdates() {
		System.out.println("Refreshing Dates");

		list.getChildren().clear();

		ObservableList<LocalDate> dates = account.getHolidayList();

		int pos = 0;

		for (LocalDate date : dates) {

			System.out.println("Creating Date: " + date.toString());

			VBox datepane = new VBox();
			datepane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			containerbox.prefWidthProperty().bind(datepane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);

			datepane.setEffect(glow);

			Insets insets = new Insets(0, 6, 0, 6);
			containerbox.setPadding(insets);
			HBox.setHgrow(containerbox, Priority.ALWAYS);
			Label datelab = new Label();
			datelab.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

			datepane.getStyleClass().add("appointment-delete");

			datepane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					ObservableList<LocalDate> tempdates = account.getHolidayList();
					tempdates.remove(date);
					account.setHolidayList(tempdates);
					avaliabilityDialogController.this.dates = tempdates;
					refreshdates();
				}

			});

			datelab.setText(date.toString());
			datelab.getStyleClass().add("label-list");
			containerbox.getChildren().add(datelab);

			datepane.getChildren().add(containerbox);

			containerbox.prefWidthProperty().bind(datepane.widthProperty());
			containerbox.maxHeightProperty().bind(datepane.heightProperty());

			datepane.prefWidthProperty().bind(scrllparent.widthProperty());
			datepane.minWidthProperty().bind(scrllparent.widthProperty());

			list.add(datepane, 0, pos);
			pos += 1;

		}
	}

	@FXML
	void addDates() {
		ObservableList<LocalDate> holidaylist = account.getHolidayList();
		System.out.println("adding Dates");
		if (fromdatepicker.getValue() != null) {
			if (todatepicker.getValue() != null) {
				int shift = 0;
				while (fromdatepicker.getValue().plusDays(shift).isBefore(todatepicker.getValue().plusDays(1))) {
					if (!holidaylist.contains(fromdatepicker.getValue().plusDays(shift))) {
						holidaylist.add(fromdatepicker.getValue().plusDays(shift));
						System.out.println("adding Date: " + fromdatepicker.getValue().plusDays(shift).toString());
					}

					shift += 1;
				}
			} else {
				if (!holidaylist.contains(fromdatepicker.getValue())) {
					holidaylist.add(fromdatepicker.getValue());
				}
				System.out.println("adding Date: " + fromdatepicker.getValue().toString());

			}
		} else {
			if (todatepicker.getValue() != null) {
				if (!holidaylist.contains(todatepicker.getValue())) {
					holidaylist.add(todatepicker.getValue());
				}
				System.out.println("adding Date: " + todatepicker.getValue().toString());
			} else {

			}
		}
		account.setHolidayList(holidaylist);
		dates = holidaylist;
		refreshdates();
	}
}
