package canopy.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import canopy.app.view.appointmentDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 *
 * @author Andy
 */
public final class Day {
	appointmentDialogController controller;
	LocalDate date;
	LocalTime start;
	LocalTime end;
	minute selectedminute;
	double blockwidth = 1;
	ObservableList<minute> allmins = FXCollections.observableArrayList();
	ObservableList<Appointment> appointments = FXCollections.observableArrayList();
	HBox daybox1;
	int hovered;

	/**
	 *
	 * @param controller
	 * @param date
	 * @param start
	 * @param end
	 * @param daybox1
	 * @param appointments
	 */
	public Day(appointmentDialogController controller, LocalDate date, LocalTime start, LocalTime end, HBox daybox1, ObservableList<Appointment> appointments) {
		System.out.println("Creating Day");
		this.date = date;
		this.start = start;
		this.end = end;
		this.controller = controller;
		this.appointments = appointments;
		this.daybox1 = daybox1;
		populateDay();
		populateappointments(appointments);
	}
	
	public int getSize() {
		return allmins.size();
	}

	/**
	 *
	 * @param time
	 */
	public void set(LocalTime time) {
		int startref = (int) MINUTES.between(start, time)/5;
		if (startref >= 0 && startref < allmins.size()) {
			selectedminute = allmins.get(startref);
			allmins.stream().filter((tempmin) -> (tempmin.minstate == 2)).map((tempmin) -> {
				tempmin.minstate = 0;
				return tempmin;
			}).forEachOrdered((tempmin) -> {
				tempmin.setStyle();
			});
			for (int i = 0; i < controller.getDuration()/5; i++) {
				if (startref + i < allmins.size()) {
					if (allmins.get(startref + i).minstate != 3 && allmins.get(startref + i).minstate != 4) {
						allmins.get(startref + i).minstate = 2;
					}
				}
			}
			refresh();
		}
	}

	void clicked(minute min) {
		selectedminute = min;
		int startref = allmins.indexOf(min);
		allmins.stream().filter((tempmin) -> (tempmin.minstate == 2)).map((tempmin) -> {
			tempmin.minstate = 0;
			return tempmin;
		}).forEachOrdered((tempmin) -> {
			tempmin.setStyle();
		});
		for (int i = 0; i < controller.getDuration()/5; i++) {
			if (startref + i < allmins.size()) {
				if (allmins.get(startref + i).minstate != 3 && allmins.get(startref + i).minstate != 4) {
					allmins.get(startref + i).minstate = 2;
				}
			}
		}
		controller.setTime(min.time);
		refresh();
	}

	void hoveredon(minute min) {
		int startref = allmins.indexOf(min);
		for (int i = 0; i < controller.getDuration()/5; i++) {
			if (startref + i < allmins.size()) {
				if ((allmins.get(startref + i).minstate != 2) && (allmins.get(startref + i).minstate != 3) && allmins.get(startref + i).minstate != 4) {
					allmins.get(startref + i).minstate = 1;
				}
			}
		}
		refresh();
		Appointment before = new Appointment();
		Appointment after = new Appointment();
		for (Appointment app : appointments) {
			if (app.getTime().isBefore(min.time)) {
				if (before.getTime() != null) {
					if (before.getTime().isBefore(app.getTime())) {
						before = app;
					}
				} else {
					before = app;
				}

			}

			if (app.getTime().isAfter(min.time)) {
				if (after.getTime() != null) {
					if (after.getTime().isAfter(app.getTime())) {
						after = app;
					}
				} else {
					after = app;
				}

			}
		}

		controller.setHoveredTime(min.time, after, before);
		hovered = allmins.indexOf(min);
	}

	void hoveredoff(minute min) {
		int startref = allmins.indexOf(min);
		for (int i = 0; i < controller.getDuration()/5; i++) {
			if (startref + i < allmins.size()) {
				if ((allmins.get(startref + i).minstate != 2) && (allmins.get(startref + i).minstate != 3) && allmins.get(startref + i).minstate != 4) {
					allmins.get(startref + i).minstate = 0;
				}
			}
		}
		refresh();
	}

	void populateDay() {
		daybox1.getChildren().clear();
		allmins.clear();
		int minutes = (int) start.until(end, MINUTES);

		AnchorPane startpane = new AnchorPane();
		AnchorPane endpane = new AnchorPane();

		startpane.setStyle("-fx-background-color: #e1e1e1");
		endpane.setStyle("-fx-background-color: #e1e1e1");

		daybox1.getChildren().add(startpane);

		for (int i = 0; i < minutes; i+=5) {
			minute min = new minute(start.plusMinutes(i));
			daybox1.getChildren().add(min.block);
		}
		daybox1.getChildren().add(endpane);

		HBox.setHgrow(startpane, Priority.ALWAYS);
		HBox.setHgrow(endpane, Priority.ALWAYS);
	}

	/**
	 *
	 */
	public void updateduration() {
		if (selectedminute != null) {
			int startref = allmins.indexOf(selectedminute);
			allmins.stream().filter((tempmin) -> (tempmin.minstate == 2)).map((tempmin) -> {
				tempmin.minstate = 0;
				return tempmin;
			}).forEachOrdered((tempmin) -> {
				tempmin.setStyle();
			});
			for (int i = 0; i < controller.getDuration()/5; i++) {
				if (startref + i < allmins.size() && allmins.get(startref + i).minstate != 3 && allmins.get(startref + i).minstate != 4) {
					allmins.get(startref + i).minstate = 2;
					allmins.get(startref + i).setStyle();
				}
			}

			controller.setTime(selectedminute.time);
		}
	}

	/**
	 *
	 */
	public void refresh() {
		allmins.forEach((refreshmin) -> {
			refreshmin.setStyle();
		});

	}

	/**
	 *
	 * @param appointments
	 */
	public void populateappointments(ObservableList<Appointment> appointments) {
		allmins.stream().filter((refreshmin) -> (refreshmin.minstate != 2)).map((refreshmin) -> {
			refreshmin.minstate = 0;
			return refreshmin;
		}).forEachOrdered((refreshmin) -> {
			refreshmin.setStyle();
		});
		appointments.stream().map((appointment) -> {
//			System.out.println(appointment.getDescription());
			return appointment;
		}).map((appointment) -> {
			LocalTime appointmenttime = appointment.getTime();
//			System.out.println(appointmenttime.toString());
			int appstart = (int) MINUTES.between(start, appointmenttime)/5;
//			System.out.println(String.valueOf(appstart));
//			System.out.println(String.valueOf(appointment.getDuration()));
			if (appstart >= 0 && (appstart) < allmins.size()) {
				allmins.get(appstart).minstate = 3; // Customise first minute slot
				for (int i = 1; i < (int) appointment.getDuration()/5; i++) {
					if ((appstart + i) < allmins.size()) {
						allmins.get(appstart + i).minstate = 3;
					}
				}
			}
			return appointment;
		}).forEachOrdered((_item) -> {
			refresh();
		});
	}

	final class minute {
		LocalTime time;
		int minstate; // 0 = resting, 1 = highlighted, 2 = selected, 3 = greyed, 4 = barrier;
		AnchorPane block;

		minute(LocalTime time) {
			minute ref = this; // Create reference minute;
			block = new AnchorPane();
			block.setPrefWidth(5);
			block.setMinWidth(5);
			block.setMaxWidth(5);
			this.time = time;
			minstate = 0;
			setStyle();

			block.setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if (minstate != 3 && minstate != 2 && minstate != 4) {
						hoveredon(ref);
					}
				}
			});
			block.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if (minstate != 3 && minstate != 2 && minstate != 4) {
						hoveredoff(ref);
					}
				}
			});
			block.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if (minstate != 3) {
						clicked(ref);
					}
				}
			});

			allmins.add(this);
		}

		void setStyle() {
			//if (allmins.indexOf(this) != hovered) {
				switch (minstate) {
				case 0:
					block.setStyle("-fx-background-color: white");
					break;
				case 1:
					block.setStyle("-fx-background-color: #8dc63f");
					break;
				case 2:
					block.setStyle("-fx-background-color: #FFBA08");
					break;
				case 3:
					block.setStyle("-fx-background-color: #f1675d");
					break;
				case 4:
					block.setStyle("-fx-background-color: #FFBA08");
					break;
				}
//			} else {
//			}
		}

	}

}
