package canopy.app.view;

import java.util.Collections;
import java.util.Comparator;

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
import javafx.scene.control.CheckBox;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Andy
 */
public class contactsController {

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

	private Patient patient;

	ObservableList<String> sorttypes = FXCollections.observableArrayList();

	private MainApp mainapp;

	ObservableList<String> persontypes = FXCollections.observableArrayList();

	private Stage dialogStage;

	int pos = 0;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 *
	 */
	public void initialise() {

		AnchorPane.setBottomAnchor(parent, 0.0);

		scrllparent.maxWidthProperty().bind(parent.widthProperty());
		list.prefWidthProperty().bind(scrllparent.widthProperty());

		sorttypes.add("ID");
		sorttypes.add("First Name");
		sorttypes.add("Last Name");
		sorterselection.setItems(sorttypes);
		sorterselection.setValue("ID");

		sorterselection.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				refreshlist();
			}
		});

		searchbar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				refreshlist();
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

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
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

	ObservableList<Account> sortStaff(ObservableList<Account> input) {
		System.out.println("Sorting Staff");
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

	void refreshlist() {
		list.getChildren().clear();
		pos = 0;

		ObservableList<Account> data = checkStaffContainment(mainapp.getListAccounts());
		data = sortStaff(data);
		ObservableList<Person> data2 = checkPersonContainment(mainapp.getPeople());
		data2 = sortPeople(data2);

		refreshpeople(data2);
		refreshstaff(data);

	}

	/**
	 *
	 * @param staff
	 */
	public void refreshstaff(ObservableList<Account> staff) {
		HBox headerpane = new HBox();
		Label headerlabel = new Label();
		headerlabel.setText("Staff");
		headerpane.getStyleClass().add("background");
		headerlabel.getStyleClass().add("label-bright");
		headerpane.getChildren().add(headerlabel);
		list.add(headerpane, 0, pos);
		pos += 1;

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

			CheckBox includebox = new CheckBox();

			if (patient.getContacts() != null) {
				String[] ids = patient.getContacts().split(";");

				for (String cont : ids) {
					if (!cont.trim().equals("")) {
						if (account.getStaffNumber() == Integer.valueOf(cont)) {
							includebox.selectedProperty().set(true);
						}
					}
				}
			}

			includebox.selectedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						patient.addContact(account.getStaffNumber().toString());
					} else {
						patient.removeContact(account.getStaffNumber().toString());
					}
				}
			});

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
			containerbox.getChildren().addAll(namelab, importantbox, includebox);
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

		for (PersonType type : mainapp.getPersonTypes()) {
			HBox headerpane = new HBox();
			Label headerlabel = new Label();
			headerlabel.setText(type.getTypeName());
			headerpane.getStyleClass().add("background");
			headerlabel.getStyleClass().add("label-bright");
			headerpane.getChildren().add(headerlabel);
			list.add(headerpane, 0, pos);
			pos += 1;

			for (Person person : persondata) {

				if (person.getPersonType() == type.getId()) {

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
					CheckBox includebox = new CheckBox();

					if (patient.getContacts() != null) {

						String[] ids = patient.getContacts().split(";");

						for (String cont : ids) {
							if (!cont.trim().equals("")) {
								if (person.getId() == Integer.valueOf(cont)) {
									includebox.selectedProperty().set(true);
								}
							}
						}
					}

					includebox.selectedProperty().addListener(new ChangeListener<Boolean>() {

						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
							if (newValue) {
								patient.addContact(person.getId().toString());
							} else {
								patient.removeContact(person.getId().toString());
							}
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
					containerbox.getChildren().addAll(namelab, importantbox, includebox);
					importantbox.getChildren().addAll(doblab);
					containerbox.prefWidthProperty().bind(personpane.widthProperty());
					containerbox.maxHeightProperty().bind(personpane.heightProperty());

					list.add(personpane, 0, pos);
					pos += 1;
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
			String email = person.getEmail();
			String number = person.getNumber();
			String address = person.getStreet();
			String city = person.getCity();
			String postcode = person.getPostalCode();

			if (firstname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (lastname.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if ((firstname + " " + lastname).startsWith(searchbar.getText().toLowerCase())) {
				people.add(person);
			} else if (email.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (number.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (address.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (city.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			} else if (postcode.trim().toLowerCase().startsWith(searchbar.getText().trim().toLowerCase())) {
				people.add(person);
			}
		});
		return people;
	}

	public void handleOk() {

		mainapp.savePatientDataToFile(mainapp.getFilePath(), false);

		mainapp.refreshcontacts(patient);

		dialogStage.close();
	}

	public void handleCancel() {
		dialogStage.close();
	}

}
