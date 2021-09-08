package canopy.app.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Patient;
import canopy.app.model.Person;
import canopy.app.model.PersonType;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author Andy
 */
public class personListController {

	@FXML
	private GridPane list;
	@FXML
	private AnchorPane parent;
	@FXML
	private AnchorPane addbutton;
	@FXML
	private ScrollPane scrllparent;
	@FXML
	private ComboBox<String> persontypeselector;
	@FXML
	private TextField searchbar;
	@FXML
	private ComboBox<String> sorterselection;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();

	private MainApp mainapp;

	ObservableList<String> persontypes = FXCollections.observableArrayList();

	int displaylimit = 30;

	int pos = 0;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
	}

	/**
	 *
	 */
	public void initialise() {
		if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 2
				|| mainapp.getCurrentAccount().getPermission() == 3) {
			persontypes.add("Patients");
			persontypeselector.setValue("Patients");
			if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3) {
				addbutton.setVisible(false);
				addbutton.setManaged(false);
			}

		} else {
			persontypeselector.setValue("Staff");
			if (mainapp.getCurrentAccount().getPermission() != 0) {
				addbutton.setVisible(false);
				addbutton.setManaged(false);
			}
		}

		persontypes.add("Staff");

		if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 5
				|| mainapp.getCurrentAccount().getPermission() == 6) {

			mainapp.getPersonTypes().forEach((type) -> {
				persontypes.add(type.getTypeName());
			});
		}
		persontypeselector.setItems(persontypes);

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		list.prefWidthProperty().bind(scrllparent.widthProperty());

		persontypeselector.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				if ("Patients".equals(persontypeselector.getValue())) {
					if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3) {
						addbutton.setVisible(false);
						addbutton.setManaged(false);
					} else if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1) {
						addbutton.setVisible(true);
						addbutton.setManaged(true);
					}
				} else if ("Staff".equals(persontypeselector.getValue())) {
					if (mainapp.getCurrentAccount().getPermission() != 0) {
						addbutton.setVisible(false);
						addbutton.setManaged(false);
					} else {
						addbutton.setVisible(true);
						addbutton.setManaged(true);
					}
				} else {
					if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3) {
						addbutton.setVisible(false);
						addbutton.setManaged(false);
					} else {
						addbutton.setVisible(true);
						addbutton.setManaged(true);
					}
				}
				if (searchbar.getText().length() != 0) {
					refreshlist2();
				} else {
					refreshlist();
				}
			}
		});

		sorttypes.add("ID");
		sorttypes.add("First Name");
		sorttypes.add("Last Name");
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("ID");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				if (searchbar.getText().trim().length() != 0) {
					refreshlist2();
				} else {
					refreshlist();
				}
			}
		});

		searchbar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				refreshlist2();
			}
		});

		refreshlist();

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

		if (dropShadow != null) {

			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 0, Interpolator.EASE_BOTH);
			final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 0, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 0, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
			timeline.getKeyFrames().add(kf);

			timeline.playFromStart();
		}
	}

	@FXML
	void lift() {

		DropShadow dropShadow = (DropShadow) parent.getEffect();

		if (dropShadow != null) {

			final Timeline timeline = new Timeline();
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(dropShadow.radiusProperty(), 40, Interpolator.EASE_BOTH);
			final KeyValue kv1 = new KeyValue(dropShadow.offsetXProperty(), 5, Interpolator.EASE_BOTH);
			final KeyValue kv2 = new KeyValue(dropShadow.offsetYProperty(), 5, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(400), kv, kv1, kv2);
			timeline.getKeyFrames().add(kf);

			timeline.playFromStart();
		}

	}

	ObservableList<Patient> sortPatients(ObservableList<Patient> input) {
		ObservableList<Patient> output = FXCollections.observableArrayList();
		output.addAll(input);

		switch (sorterselection.getValue()) {
		case "ID":
			Comparator<Patient> comparator = Comparator.comparingInt(Patient::getPatientNumber).reversed();
			FXCollections.sort(output, comparator);
			break;
		case "First Name":
			Collections.sort(output, new Comparator<Patient>() {
				public int compare(Patient p1, Patient p2) {
					return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
				}
			});
			break;
		case "Last Name":
			Collections.sort(output, new Comparator<Patient>() {
				public int compare(Patient p1, Patient p2) {
					return p1.getLastName().toLowerCase().compareTo(p2.getLastName().toLowerCase());
				}
			});
			break;
		}

		return output;
	}

	ObservableList<Account> sortStaff(ObservableList<Account> input) {
		// System.out.println("Sorting Staff");
		ObservableList<Account> output = FXCollections.observableArrayList();
		output.addAll(input);

		switch (sorterselection.getValue()) {
		case "ID":
			Comparator<Account> comparator = Comparator.comparingInt(Account::getStaffNumber);
			FXCollections.sort(output, comparator);
			break;
		case "First Name":
			Collections.sort(output, new Comparator<Account>() {
				public int compare(Account p1, Account p2) {
					return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
				}
			});
			break;
		case "Last Name":
			Collections.sort(output, new Comparator<Account>() {
				public int compare(Account p1, Account p2) {
					return p1.getLastName().toLowerCase().compareTo(p2.getLastName().toLowerCase());
				}
			});
			break;
		}

		return output;
	}

	ObservableList<Person> sortPeople(ObservableList<Person> input) {
		ObservableList<Person> output = FXCollections.observableArrayList();
		output.addAll(input);

		switch (sorterselection.getValue()) {
		case "ID":
			Comparator<Person> comparator = Comparator.comparingInt(Person::getId);
			FXCollections.sort(output, comparator);
			break;
		case "First Name":
			Collections.sort(output, new Comparator<Person>() {
				public int compare(Person p1, Person p2) {
					return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
				}
			});
			break;
		case "Last Name":
			Collections.sort(output, new Comparator<Person>() {
				public int compare(Person p1, Person p2) {
					return p1.getLastName().toLowerCase().compareTo(p2.getLastName().toLowerCase());
				}
			});
			break;
		}

		return output;
	}

	void refreshlist2() {
		
		pos = 0;
		if (persontypeselector.getValue().contains("Patients")) {
			ObservableList<Patient> data = checkPatientContainment(mainapp.getPatients());
			data = sortPatients(data);
			refreshpatients(data);
		} else if (persontypeselector.getValue().contains("Staff")) {
			ObservableList<Account> data = checkStaffContainment(mainapp.getListAccounts());
			data = sortStaff(data);
			refreshstaff(data);
		} else {
			ObservableList<Person> people = FXCollections.observableArrayList();
			for (PersonType type : mainapp.getPersonTypes()) {
				if (type.getTypeName().trim().equals(persontypeselector.getValue().trim())) {

					mainapp.getPeople().stream().filter((person) -> (person.getPersonType() == type.getId())).forEachOrdered((person) -> {
						people.add(person);
					});

					break;
				}
			}
			ObservableList<Person> data = checkPersonContainment(people);
			data = sortPeople(data);
			refreshpeople(data);
		}
	}

	/**
	 *
	 */
	public void refreshlist() {
		pos = 0;
		if (persontypeselector.getValue().contains("Patients")) {
			// if (mainapp.PatientsSaved()) {
			// mainapp.LoadPatientDataFromFile(mainapp.getFilePath());
			// }
			ObservableList<Patient> data = FXCollections.observableArrayList();
			data = sortPatients(mainapp.getPatients());
			refreshpatients(data);
		} else if (persontypeselector.getValue().contains("Staff")) {
			// if (mainapp.AccountsSaved()) {
			// mainapp.LoadAccountDataFromFile(mainapp.getFilePath());
			// }
			ObservableList<Account> data = checkStaffContainment(mainapp.getListAccounts());
			data = sortStaff(data);
			refreshstaff(data);
		} else {
			for (PersonType type : mainapp.getPersonTypes()) {
				// System.out.println("Searching Types: " + type.getTypeName());
				if (type.getTypeName().trim().equals(persontypeselector.getValue().trim())) {
					// System.out.println("Setting Type int = " + String.valueOf(type.getId()));
					setPersonType(type);
					break;
				}
			}

		}
	}

	/**
	 *
	 * @param patients
	 */
	public void refreshpatients(ObservableList<Patient> patients) {
		list.getChildren().clear();
		patients.stream().map((patient) -> {

			VBox patientpane = new VBox();
			if (pos <= displaylimit) {

				patientpane.setAlignment(Pos.CENTER);
				patientpane.getStyleClass().add("appointment");
				HBox containerbox = new HBox();
				patientpane.getChildren().add(containerbox);
				containerbox.prefWidthProperty().bind(patientpane.widthProperty());
				containerbox.setFillHeight(true);
				containerbox.setAlignment(Pos.CENTER);
				Glow glow = new Glow(0);
				patientpane.setEffect(glow);
				patientpane.setOnMouseEntered(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						glow.setLevel(0.2);

					}
				});
				HBox.setHgrow(containerbox, Priority.ALWAYS);

				if (pos < displaylimit) {

					Label namelab = new Label();
					Label doblab = new Label();
					String style;
					patientpane.setOnMouseExited(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							glow.setLevel(0);
						}
					});
					patientpane.setOnMouseClicked(new EventHandler<Event>() {

						@Override
						public void handle(Event event) {
							mainapp.setSelectedPatient(patient);
						}

					});
					namelab.setText(patient.getFirstName() + " " + patient.getLastName() + " | " + "ID: " + String.valueOf(patient.getPatientNumber()) + " | ");
					if (patient.getBirthday() != null) {
						LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
						doblab.setText(dateStringConverter.toString(patient.getBirthday()));
					}
					namelab.getStyleClass().add("label-list");
					doblab.getStyleClass().add("label-list");
					namelab.setWrapText(true);
					doblab.setWrapText(true);
					containerbox.getChildren().addAll(namelab, doblab);

				} else if (pos == displaylimit) {
					Label loadmorelab = new Label();
					loadmorelab.setText("Load More");
					loadmorelab.getStyleClass().add("label-list-big");
					containerbox.getChildren().add(loadmorelab);
					patientpane.getStyleClass().add("appointment");

					patientpane.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {

							displaylimit += 20;
							refreshlist();

						}
					});
				}

				containerbox.prefWidthProperty().bind(patientpane.widthProperty());
				containerbox.maxHeightProperty().bind(patientpane.heightProperty());

			}
			return patientpane;

		}).map((patientpane) -> {
			list.add(patientpane, 0, pos);
			return patientpane;
		}).forEachOrdered((_item) -> {
			pos += 1;
		});
	}

	/**
	 *
	 * @param staff
	 */
	public void refreshstaff(ObservableList<Account> staff) {
		list.getChildren().clear();
		staff.stream().map((account) -> {
			VBox accountpane = new VBox();
			accountpane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			VBox importantbox = new VBox();
			containerbox.prefWidthProperty().bind(accountpane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);
			accountpane.setEffect(glow);
			accountpane.setOnMouseEntered(new EventHandler<Event>() {
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
			namelab.setWrapText(true);
			doblab.setWrapText(true);
			String style;
			accountpane.getStyleClass().add("appointment");
			accountpane.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0);
				}
			});
			accountpane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.setSelectedAccount(account);
				}

			});
			namelab.setText(account.getTitle() + "." + account.getFirstName() + " " + account.getLastName() + " | ");
			doblab.setText(account.getRole());
			namelab.getStyleClass().add("label-list");
			doblab.getStyleClass().add("label-list");
			accountpane.getChildren().add(containerbox);
			containerbox.getChildren().addAll(namelab, importantbox);
			importantbox.getChildren().addAll(doblab);
			containerbox.prefWidthProperty().bind(accountpane.widthProperty());
			containerbox.maxHeightProperty().bind(accountpane.heightProperty());
			return accountpane;
		}).map((accountpane) -> {
			list.add(accountpane, 0, pos);
			return accountpane;
		}).forEachOrdered((_item) -> {
			pos += 1;
		});
	}

	/**
	 *
	 * @param persondata
	 */
	public void refreshpeople(ObservableList<Person> persondata) {
		list.getChildren().clear();
		persondata.stream().map((person) -> {
			VBox personpane = new VBox();
			personpane.setAlignment(Pos.CENTER);
			HBox containerbox = new HBox();
			VBox importantbox = new VBox();
			containerbox.prefWidthProperty().bind(personpane.widthProperty());
			containerbox.setAlignment(Pos.CENTER);
			Glow glow = new Glow(0);
			personpane.setEffect(glow);
			personpane.setOnMouseEntered(new EventHandler<Event>() {
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
			namelab.setWrapText(true);
			doblab.setWrapText(true);
			personpane.getStyleClass().add("appointment");
			personpane.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					glow.setLevel(0);
				}
			});
			personpane.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					mainapp.setSelectedPerson(person);
				}

			});
			namelab.setText(person.getFirstName() + " " + person.getLastName() + " | ");
			doblab.setText(person.getDescription());
			namelab.getStyleClass().add("label-list");
			doblab.getStyleClass().add("label-list");
			personpane.getChildren().add(containerbox);
			containerbox.getChildren().addAll(namelab, importantbox);
			importantbox.getChildren().addAll(doblab);
			containerbox.prefWidthProperty().bind(personpane.widthProperty());
			containerbox.maxHeightProperty().bind(personpane.heightProperty());
			return personpane;
		}).map((personpane) -> {
			list.add(personpane, 0, pos);
			return personpane;
		}).forEachOrdered((_item) -> {
			pos += 1;
		});

	}

	/**
	 *
	 */
	public void addPerson() {

		if (persontypeselector.getValue().contains("Patients")) {
			int patientlimit = 50000;
			switch (mainapp.licence.getType()) {
			case 0:
				patientlimit = 250;
				break;
			case 1:
				patientlimit = 600;
				break;
			case 2:
				patientlimit = 1250;
				break;
			case 3:
				patientlimit = 50000;
				break;
			}
			if (mainapp.getPatients().size() >= patientlimit) {
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
				dialogPane.getStyleClass().add(".dialog-pane");

				alert.setTitle("Error");
				alert.setHeaderText("Patient Limit Reached");
				alert.setContentText("Please Archive Unused Patients or Upgrade Licence Key. Contact info@canopyadminsoftware.com");

				ButtonType buttonTypeOne = new ButtonType("Cancel");

				alert.getButtonTypes().setAll(buttonTypeOne);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					alert.close();
				} else {
					alert.close();
				}
			} else {
				mainapp.addNewPatient();
			}
		} else if (persontypeselector.getValue().contains("Staff")) {

			int adminlimit = 50000;
			int stafflimit = 50000;
			switch (mainapp.licence.getType()) {
			case 0:
				adminlimit = 1;
				stafflimit = 6;
				break;
			case 1:
				adminlimit = 2;
				stafflimit = 15;
				break;
			case 2:
				adminlimit = 3;
				stafflimit = 25;
				break;
			case 3:
				adminlimit = 50000;
				stafflimit = 50000;
				break;
			}
			if (mainapp.getAccounts().size() >= stafflimit) {
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
				dialogPane.getStyleClass().add(".dialog-pane");

				alert.setTitle("Error");
				alert.setHeaderText("Staff Limit Reached");
				alert.setContentText("Please Delete Unused Staff Accounts or Upgrade Licence Key. Contact info@canopyadminsoftware.com");

				ButtonType buttonTypeOne = new ButtonType("Cancel");

				alert.getButtonTypes().setAll(buttonTypeOne);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					alert.close();
				} else {
					alert.close();
				}
			} else {
				mainapp.addNewAccount();
			}

		} else {
			for (PersonType type : mainapp.getPersonTypes()) {
				if (type.getTypeName().trim().equals(persontypeselector.getValue().trim())) {
					mainapp.addNewPerson(type.getId());
					break;
				}
			}
		}

	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public ObservableList<Patient> checkPatientContainment(ObservableList<Patient> data) {
		ObservableList<Patient> patients = FXCollections.observableArrayList();

		data.forEach((patient) -> {
			String firstname = patient.getFirstName().toLowerCase();
			String lastname = patient.getLastName().toLowerCase();

			if (firstname.startsWith(searchbar.getText().toLowerCase())) {
				patients.add(patient);
			} else if (lastname.startsWith(searchbar.getText().toLowerCase())) {
				patients.add(patient);
			} else if ((firstname + " " + lastname).startsWith(searchbar.getText().toLowerCase())) {
				patients.add(patient);
			}
		});
		return patients;

	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public ObservableList<Account> checkStaffContainment(ObservableList<Account> data) {
		ObservableList<Account> accounts = FXCollections.observableArrayList();

		data.forEach((account) -> {
			String title = account.getTitle();
			String role = account.getRole();
			String firstname = account.getFirstName().toLowerCase();
			String lastname = account.getLastName().toLowerCase();

			if (firstname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				accounts.add(account);
			} else if (lastname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				accounts.add(account);
			} else if (title.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				accounts.add(account);
			} else if (role.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				accounts.add(account);
			} else if ((firstname + " " + lastname).startsWith(searchbar.getText().toLowerCase())) {
				accounts.add(account);
			} else if ((title + "." + firstname + " " + lastname).startsWith(searchbar.getText().toLowerCase())) {
				accounts.add(account);
			}
		});
		return accounts;
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public ObservableList<Person> checkPersonContainment(ObservableList<Person> data) {
		ObservableList<Person> people = FXCollections.observableArrayList();

		data.forEach((person) -> {
			String firstname = person.getFirstName().toLowerCase();
			String lastname = person.getLastName().toLowerCase();

			if (firstname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (lastname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if ((firstname + " " + lastname).startsWith(searchbar.getText().toLowerCase())) {
				people.add(person);
			}
		});
		return people;
	}

	/**
	 *
	 * @param persontype
	 */
	public void setPersonType(PersonType persontype) {
		// System.out.println("Setting Person Type");
		ObservableList<Person> people = FXCollections.observableArrayList();
		for (Person person : mainapp.getPeople()) {
			// System.out.println("Setting People: " + String.valueOf(person.getPersonType()));
			if (person.getPersonType() == persontype.getId()) {
				people.add(person);
			}
		}
		people = sortPeople(people);
		refreshpeople(people);

	}

	/**
	 *
	 */
	@FXML
	public void addPersonType() {
		mainapp.addNewPersonType();
	}

	/**
	 *
	 * @param name
	 */
	public void actuallyAddPersonType(String name) {
		persontypes.add(name);
		persontypeselector.setItems(persontypes);
		persontypeselector.setValue(name);
		refreshlist();
	}

}
