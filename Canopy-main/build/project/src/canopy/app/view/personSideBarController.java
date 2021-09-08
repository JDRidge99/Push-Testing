package canopy.app.view;

import java.awt.Desktop;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.MailingList;
import canopy.app.model.Note;
import canopy.app.model.Patient;
import canopy.app.model.Person;
import canopy.app.util.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.Glow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author Andy
 */
public class personSideBarController {

	@FXML
	private Label firstnamelab;

	@FXML
	private Label secondnamelab;

	@FXML
	private Label cflab;

	@FXML
	private Label cllab;

	@FXML
	private Label cfheader;

	@FXML
	private Label clheader;

	@FXML
	private Label doblab;

	@FXML
	private Label dobtitle;

	@FXML
	private Label agelabel;

	@FXML
	private Label ageheaderlabel;

	@FXML
	private Label addresslabel;

	@FXML
	private Label citylabel;

	@FXML
	private Label diagnoseslabel;

	@FXML
	private Label footnotesheader;

	@FXML
	private Label postalcodelabel;

	@FXML
	private Label starttimelab;

	@FXML
	private Label starttimeheader;

	@FXML
	private Label endtimelab;

	@FXML
	private Label endtimeheader;

	@FXML
	private Label holidayheader;

	@FXML
	private Label usernameheader;

	@FXML
	private Label passwordheader;

	@FXML
	private Label username;

	@FXML
	private Button password;

	@FXML
	private Button holidaybutton;

	@FXML
	private VBox emailsbox;

	@FXML
	private VBox numbersbox;

	@FXML
	private VBox contactsbox;

	@FXML
	private VBox addressbox;

	@FXML
	private VBox owedheader;

	@FXML
	private VBox owedbox;

	@FXML
	private Label owedlabel;

	@FXML
	private VBox maillistbox;

	@FXML
	private Label addresstitle;

	@FXML
	private Button editbutton;

	@FXML
	private Button removebutton;

	@FXML
	private AnchorPane generatebutton;

	@FXML
	private VBox generateheader;

	@FXML
	private VBox contactsheader;

	@FXML
	private Button contactsbutton;

	@FXML
	private AnchorPane parent;

	@FXML
	private ScrollPane scrollpane;

	@FXML
	private ScrollPane contactsscrollpane;

	@FXML
	private Label permissionheader;

	@FXML
	private Button permissionbutton;

	Patient selectedpatient;
	Account selectedstaff;
	Person selectedperson;

	int selected = 0;
	// 0 = patient
	// 1 = staff
	// 2 = person

	private MainApp mainapp;

	/**
	 *
	 * @param mainapp
	 */
	public void setMainApp(MainApp mainapp) {
		this.mainapp = mainapp;
		scrollpane.setFitToWidth(true);
	}

	/**
	 *
	 */
	public void clear() {
		emailsbox.getChildren().clear();
		numbersbox.getChildren().clear();
	}

	/**
	 *
	 * @param patient
	 */
	public void setPatient(Patient patient) {

		selected = 0;
		selectedpatient = patient;

		firstnamelab.setText(patient.getFirstName());
		secondnamelab.setText(patient.getLastName());
		LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
		doblab.setText(dateStringConverter.toString(patient.getBirthday()));

		if (mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3) {
			editbutton.setVisible(false);
			editbutton.setManaged(false);
			removebutton.setVisible(false);
			removebutton.setManaged(false);
		}

		if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getPermission() == 1) { // Admin or Master Admin
			owedbox.setVisible(true);
			owedbox.setManaged(true);
			owedheader.setVisible(true);
			owedheader.setManaged(true);

			editbutton.setVisible(true);
			editbutton.setManaged(true);
			removebutton.setVisible(true);
			removebutton.setManaged(true);

			calculateOwed();

			generateheader.setVisible(true);
			generateheader.setManaged(true);
			generatebutton.setVisible(true);
			generatebutton.setManaged(true);

			if (patient.getPermissionFile() != null) {
				permissionheader.setVisible(true);
				permissionheader.setManaged(true);
				permissionbutton.setVisible(true);
				permissionbutton.setManaged(true);
			} else {
				permissionheader.setVisible(false);
				permissionheader.setManaged(false);
				permissionbutton.setVisible(false);
				permissionbutton.setManaged(false);
			}
		} else {
			owedbox.setVisible(false);
			owedbox.setManaged(false);
			owedheader.setVisible(false);
			owedheader.setManaged(false);

			generateheader.setVisible(false);
			generateheader.setManaged(false);
			generatebutton.setVisible(false);
			generatebutton.setManaged(false);

			permissionheader.setVisible(false);
			permissionheader.setManaged(false);
			permissionbutton.setVisible(false);
			permissionbutton.setManaged(false);
		}

		if (patient.getCarerFirstName() != null) {
			cflab.setVisible(true);
			cflab.setManaged(true);
			cfheader.setVisible(true);
			cfheader.setManaged(true);
			cflab.setText(patient.getCarerFirstName());
		} else {
			cflab.setVisible(false);
			cflab.setManaged(false);
			cfheader.setVisible(false);
			cfheader.setManaged(false);
		}
		if (patient.getCarerLastName() != null) {
			cllab.setVisible(true);
			cllab.setManaged(true);
			clheader.setVisible(true);
			clheader.setManaged(true);
			cllab.setText(patient.getCarerLastName());
		} else {
			cllab.setVisible(false);
			cllab.setManaged(false);
			clheader.setVisible(false);
			clheader.setManaged(false);
		}

		doblab.setVisible(true);
		dobtitle.setVisible(true);

		ageheaderlabel.setVisible(true);
		ageheaderlabel.setManaged(true);
		agelabel.setVisible(true);
		agelabel.setManaged(true);
		try {
			long curryears = patient.getBirthday().until(LocalDate.now(), ChronoUnit.YEARS);
			long currmonths = patient.getBirthday().until(LocalDate.now(), ChronoUnit.MONTHS) - curryears * 12;
			agelabel.setText(String.valueOf(curryears) + " Y " + String.valueOf(currmonths) + " M ");
		} catch (Exception e) {
			// TODO: handle exception
		}

		diagnoseslabel.setVisible(true);
		footnotesheader.setVisible(true);
		diagnoseslabel.setManaged(true);
		footnotesheader.setManaged(true);

		if (mainapp.getCurrentAccount().getPermission() != 3) { // Observing staff can't see patient address
			addressbox.setVisible(true);
			addresstitle.setVisible(true);
		}

		doblab.setManaged(true);
		dobtitle.setManaged(true);
		addressbox.setManaged(true);
		addresstitle.setManaged(true);

		starttimelab.setVisible(false);
		starttimeheader.setVisible(false);
		endtimelab.setVisible(false);
		endtimeheader.setVisible(false);
		holidayheader.setVisible(false);
		holidaybutton.setVisible(false);

		starttimelab.setManaged(false);
		starttimeheader.setManaged(false);
		endtimelab.setManaged(false);
		endtimeheader.setManaged(false);
		holidayheader.setManaged(false);
		holidaybutton.setManaged(false);

		contactsbox.setVisible(true);
		contactsbox.setManaged(true);
		contactsscrollpane.setVisible(true);
		contactsscrollpane.setManaged(true);
		contactsheader.setVisible(true);
		contactsheader.setManaged(true);
		contactsbutton.setVisible(true);
		contactsbutton.setManaged(true);

		usernameheader.setVisible(false);
		username.setVisible(false);
		passwordheader.setVisible(false);
		password.setVisible(false);
		usernameheader.setManaged(false);
		username.setManaged(false);
		passwordheader.setManaged(false);
		password.setManaged(false);

		removebutton.setText("ARCHIVE");

		addresslabel.setText(patient.getStreet());
		citylabel.setText(patient.getCity());
		postalcodelabel.setText(patient.getPostalCode());
		diagnoseslabel.setText(patient.getDiagnosis());

		// EMAILS

		String style = "     -fx-font-size: 9pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: black;\r\n" + "    -fx-opacity: 0.6;";

		clear();

		emailsbox.getChildren().clear();

		if (patient.getEmails() != null) {
			String[] emails = patient.getEmails().split(";");
			for (String email : emails) {
				if (email.trim().length() != 0 && email != null) {
					Label emaillabel = new Label();
					emaillabel.setText(email);
					emaillabel.setStyle(style);
					emaillabel.setWrapText(true);
					emailsbox.getChildren().add(emaillabel);
				}
			}

		}

		numbersbox.getChildren().clear();

		if (patient.getNumbers() != null) {
			String[] numbers = patient.getNumbers().split(";");
			for (String number : numbers) {
				if (number.trim().length() != 0 && number != null) {
					Label numberlabel = new Label();
					numberlabel.setText(number);
					numberlabel.setStyle(style);
					numberlabel.setWrapText(true);
					numbersbox.getChildren().add(numberlabel);
				}
			}
		}

		maillistbox.getChildren().clear();

		if (mainapp.getMailingLists() != null && patient.getEmails() != null) {
			mainapp.getMailingLists().stream().map((mailinglist) -> {
				HBox containerbox = new HBox();
				CheckBox cb = new CheckBox();
				cb.setText(mailinglist.getListName());
				String[] curremails = mailinglist.getList().split(";");
				ArrayList<String> curremailslist = new ArrayList<String>(Arrays.asList(curremails));
				String[] patientemails = patient.getEmails().split(";");
				for (String e : patientemails) {
					if (curremailslist.contains(e)) {
						cb.selectedProperty().set(true);
					}
				}
				cb.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (cb.selectedProperty().getValue()) {
							mailinglist.addEmail(patient.getEmails());
							mainapp.saveMailingListDataToFile(mainapp.getFilePath(), false);
						} else {
							String[] emails = mailinglist.getList().split(";");
							ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(emails));
							String[] patientemails = patient.getEmails().split(";");
							emailslist.removeAll(Arrays.asList(patientemails));
						}
					}
				});
				return cb;
			}).forEachOrdered((cb) -> {
				maillistbox.getChildren().add(cb);
			});
		}

		contactsbox.getChildren().clear();
		contactsbox.prefWidthProperty().bind(contactsscrollpane.widthProperty());

		if (patient.getContacts() != null && !patient.getContacts().trim().equals("")) {
			String[] contacts = patient.getContacts().split(";");

			for (String contact : contacts) {
				if (!contact.trim().equals("")) {
					int id = Integer.valueOf(contact);

					for (Person person : mainapp.getPeople()) {
						if (person.getId() == id) {

							Label namelabel = new Label();
							namelabel.setText(mainapp.getPersonTypes().get(person.getPersonType()).getTypeName() + " " + person.getFirstName() + " " + person.getLastName());
							HBox containerbox = new HBox();
							containerbox.prefWidthProperty().bind(contactsbox.widthProperty());
							containerbox.getChildren().add(namelabel);
							namelabel.getStyleClass().add("label-list");
							containerbox.getStyleClass().add("appointment");
							contactsbox.getChildren().add(containerbox);

							containerbox.setOnMouseClicked(new EventHandler<Event>() {
								@Override
								public void handle(Event event) {

									Alert alert = new Alert(AlertType.CONFIRMATION);
									Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
									stage.setAlwaysOnTop(true);

									DialogPane dialogPane = alert.getDialogPane();
									dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
									dialogPane.getStyleClass().add(".dialog-pane");

									alert.initOwner(mainapp.getPrimaryStage());
									alert.setTitle("Go to Person?");
									alert.setHeaderText("See Person Details in Planner: " + person.getFirstName() + " " + person.getLastName() + "?");

									ButtonType buttonTypeOne = new ButtonType("Yes");
									ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

									alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

									Optional<ButtonType> result = alert.showAndWait();
									if (result.get() == buttonTypeOne) {
										mainapp.seePerson(person);
									} else {
										alert.close();
									}

								}
							});

						}
					}
					for (Account acc : mainapp.getAccounts()) {
						if (acc.getStaffNumber() == id) {

							Label namelabel = new Label();
							namelabel.setText(acc.getTitle() + ". " + acc.getFirstName() + " " + acc.getLastName());
							HBox containerbox = new HBox();
							containerbox.prefWidthProperty().bind(contactsbox.widthProperty());
							containerbox.getChildren().add(namelabel);
							namelabel.getStyleClass().add("label-list");
							containerbox.getStyleClass().add("appointment");
							contactsbox.getChildren().add(containerbox);

							containerbox.setOnMouseClicked(new EventHandler<Event>() {
								@Override
								public void handle(Event event) {

									Alert alert = new Alert(AlertType.CONFIRMATION);
									Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
									stage.setAlwaysOnTop(true);

									DialogPane dialogPane = alert.getDialogPane();
									dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
									dialogPane.getStyleClass().add(".dialog-pane");

									alert.initOwner(mainapp.getPrimaryStage());
									alert.setTitle("Go to Staff Member?");
									alert.setHeaderText("See Staff Details in Planner: " + acc.getFirstName() + " " + acc.getLastName() + "?");

									ButtonType buttonTypeOne = new ButtonType("Yes");
									ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

									alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

									Optional<ButtonType> result = alert.showAndWait();
									if (result.get() == buttonTypeOne) {
										mainapp.seeStaff(acc);
									} else {
										alert.close();
									}

								}
							});

						}
					}
				}
			}

		}

	}

	/**
	 *
	 * @param account
	 */
	public void setAccount(Account account) {

		if (mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3
				|| mainapp.getCurrentAccount().getPermission() == 5 || mainapp.getCurrentAccount().getPermission() == 6) {
			editbutton.setVisible(false);
			removebutton.setManaged(false);
		}

		selected = 1;
		selectedstaff = account;

		firstnamelab.setText(account.getFirstName());
		secondnamelab.setText(account.getLastName());
		doblab.setVisible(false);
		dobtitle.setVisible(false);
		addressbox.setVisible(false);
		addresstitle.setVisible(false);
		doblab.setManaged(false);
		dobtitle.setManaged(false);
		ageheaderlabel.setVisible(false);
		ageheaderlabel.setManaged(false);
		agelabel.setVisible(false);
		agelabel.setManaged(false);
		addressbox.setManaged(false);
		addresstitle.setManaged(false);
		cflab.setVisible(false);
		cflab.setManaged(false);
		cfheader.setVisible(false);
		cfheader.setManaged(false);
		cllab.setVisible(false);
		cllab.setManaged(false);
		clheader.setVisible(false);
		clheader.setManaged(false);
		owedbox.setVisible(false);
		owedbox.setManaged(false);
		owedheader.setVisible(false);
		owedheader.setManaged(false);
		generateheader.setVisible(false);
		generateheader.setManaged(false);
		generatebutton.setVisible(false);
		generatebutton.setManaged(false);
		removebutton.setText("DELETE");

		contactsbox.setVisible(false);
		contactsbox.setManaged(false);
		contactsscrollpane.setVisible(false);
		contactsscrollpane.setManaged(false);
		contactsheader.setVisible(false);
		contactsheader.setManaged(false);
		contactsbutton.setVisible(false);
		contactsbutton.setManaged(false);

		permissionheader.setVisible(false);
		permissionheader.setManaged(false);
		permissionbutton.setVisible(false);
		permissionbutton.setManaged(false);

		if (mainapp.getCurrentAccount().getPermission() != 2 && mainapp.getCurrentAccount().getPermission() != 3) {
			starttimelab.setVisible(true);
			starttimeheader.setVisible(true);
			endtimelab.setVisible(true);
			endtimeheader.setVisible(true);
			holidayheader.setVisible(true);
			holidaybutton.setVisible(true);

			starttimelab.setManaged(true);
			starttimeheader.setManaged(true);
			endtimelab.setManaged(true);
			endtimeheader.setManaged(true);
			holidayheader.setManaged(true);
			holidaybutton.setManaged(true);

			starttimelab.setText(account.getStartTime().toString());
			endtimelab.setText(account.getEndTime().toString());

		} else {
			starttimelab.setVisible(false);
			starttimeheader.setVisible(false);
			endtimelab.setVisible(false);
			endtimeheader.setVisible(false);
			holidayheader.setVisible(false);
			holidaybutton.setVisible(false);

			starttimelab.setManaged(false);
			starttimeheader.setManaged(false);
			endtimelab.setManaged(false);
			endtimeheader.setManaged(false);
			holidayheader.setManaged(false);
			holidaybutton.setManaged(false);
		}

		if (mainapp.getCurrentAccount().getPermission() == 0 || mainapp.getCurrentAccount().getStaffNumber() == account.getStaffNumber()) {

			System.out.println("Password & username shown");
			usernameheader.setVisible(true);
			username.setVisible(true);
			username.setText(account.getUserName());
			passwordheader.setVisible(true);
			password.setVisible(true);
			usernameheader.setManaged(true);
			username.setManaged(true);
			passwordheader.setManaged(true);
			password.setManaged(true);
		}

		else {

			System.out.println("Password & username hidden ");

			usernameheader.setVisible(false);
			username.setVisible(false);
			passwordheader.setVisible(false);
			password.setVisible(false);
			usernameheader.setManaged(false);
			username.setManaged(false);
			passwordheader.setManaged(false);
			password.setManaged(false);
			diagnoseslabel.setVisible(false);
			footnotesheader.setVisible(false);
		}

		// EMAILS

		String style = "     -fx-font-size: 9pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: black;\r\n" + "    -fx-opacity: 0.6;";

		clear();
		Label emaillabel = new Label();
		emaillabel.setText(account.getEmail());
		emaillabel.setStyle(style);
		emailsbox.getChildren().add(emaillabel);

		Label numberlabel = new Label();
		numberlabel.setText(account.getPhoneNumber());
		numberlabel.setStyle(style);
		numbersbox.getChildren().add(numberlabel);

		maillistbox.getChildren().clear();

		if (mainapp.getMailingLists() != null) {
			mainapp.getMailingLists().stream().map((mailinglist) -> {
				HBox containerbox = new HBox();
				CheckBox cb = new CheckBox();
				cb.setText(mailinglist.getListName());
				String[] curremails = mailinglist.getList().split(";");
				ArrayList<String> curremailslist = new ArrayList<String>(Arrays.asList(curremails));
				if (curremailslist.contains(account.getEmail())) {
					cb.selectedProperty().set(true);
				}
				cb.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (cb.selectedProperty().getValue()) {
							mailinglist.addEmail(account.getEmail());
							mainapp.saveMailingListDataToFile(mainapp.getFilePath(), false);
						} else {
							String[] emails = mailinglist.getList().split(";");
							ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(emails));
							emailslist.remove(account.getEmail());
						}
					}
				});
				return cb;
			}).forEachOrdered((cb) -> {
				maillistbox.getChildren().add(cb);
			});
		}

	}

	/**
	 *
	 * @param person
	 */
	public void setPerson(Person person) {

		if (mainapp.getCurrentAccount().getPermission() == 1 || mainapp.getCurrentAccount().getPermission() == 2 || mainapp.getCurrentAccount().getPermission() == 3
				|| mainapp.getCurrentAccount().getPermission() == 6) {
			editbutton.setVisible(false);
			removebutton.setManaged(false);
		}

		selected = 2;
		selectedperson = person;

		firstnamelab.setText(person.getFirstName());
		secondnamelab.setText(person.getLastName());
		doblab.setVisible(false);
		dobtitle.setVisible(false);
		doblab.setManaged(false);
		dobtitle.setManaged(false);
		ageheaderlabel.setVisible(false);
		ageheaderlabel.setManaged(false);
		agelabel.setVisible(false);
		agelabel.setManaged(false);

		diagnoseslabel.setVisible(true);
		footnotesheader.setVisible(true);
		diagnoseslabel.setManaged(true);
		footnotesheader.setManaged(true);

		contactsbox.setVisible(false);
		contactsbox.setManaged(false);
		contactsscrollpane.setVisible(false);
		contactsscrollpane.setManaged(false);
		contactsheader.setVisible(false);
		contactsheader.setManaged(false);
		contactsbutton.setVisible(false);
		contactsbutton.setManaged(false);

		permissionheader.setVisible(false);
		permissionheader.setManaged(false);
		permissionbutton.setVisible(false);
		permissionbutton.setManaged(false);

		cflab.setVisible(false);
		cflab.setManaged(false);
		cfheader.setVisible(false);
		cfheader.setManaged(false);
		cllab.setVisible(false);
		cllab.setManaged(false);
		clheader.setVisible(false);
		clheader.setManaged(false);
		owedbox.setVisible(false);
		owedbox.setManaged(false);
		owedheader.setVisible(false);
		owedheader.setManaged(false);
		generateheader.setVisible(false);
		generateheader.setManaged(false);
		generatebutton.setVisible(false);
		generatebutton.setManaged(false);

		if (mainapp.getCurrentAccount().getPermission() != 3) { // Observing staff can't see patient address
			addressbox.setVisible(true);
			addresstitle.setVisible(true);
		}

		addressbox.setManaged(true);
		addresstitle.setManaged(true);

		removebutton.setText("DELETE");

		starttimelab.setVisible(false);
		starttimeheader.setVisible(false);
		endtimelab.setVisible(false);
		endtimeheader.setVisible(false);
		holidayheader.setVisible(false);
		holidaybutton.setVisible(false);

		starttimelab.setManaged(false);
		starttimeheader.setManaged(false);
		endtimelab.setManaged(false);
		endtimeheader.setManaged(false);
		holidayheader.setManaged(false);
		holidaybutton.setManaged(false);

		usernameheader.setVisible(false);
		username.setVisible(false);
		passwordheader.setVisible(false);
		password.setVisible(false);
		usernameheader.setManaged(false);
		username.setManaged(false);
		passwordheader.setManaged(false);
		password.setManaged(false);

		addresslabel.setText(person.getStreet());
		citylabel.setText(person.getCity());
		postalcodelabel.setText(person.getPostalCode());
		diagnoseslabel.setText(person.getDescription());

		// EMAILS

		String style = "     -fx-font-size: 9pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: black;\r\n" + "    -fx-opacity: 0.6;";

		clear();
		Label emaillabel = new Label();
		emaillabel.setText(person.getEmail());
		emaillabel.setStyle(style);
		emailsbox.getChildren().add(emaillabel);

		Label numberlabel = new Label();
		numberlabel.setText(person.getNumber());
		numberlabel.setStyle(style);
		numbersbox.getChildren().add(numberlabel);

		maillistbox.getChildren().clear();

		if (mainapp.getMailingLists() != null) {
			mainapp.getMailingLists().stream().map((mailinglist) -> {
				HBox containerbox = new HBox();
				CheckBox cb = new CheckBox();
				cb.setText(mailinglist.getListName());
				String[] curremails = mailinglist.getList().split(";");
				ArrayList<String> curremailslist = new ArrayList<String>(Arrays.asList(curremails));
				if (curremailslist.contains(person.getEmail())) {
					cb.selectedProperty().set(true);
				}
				cb.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (cb.selectedProperty().getValue()) {
							mailinglist.addEmail(person.getEmail());
							mainapp.saveMailingListDataToFile(mainapp.getFilePath(), false);
						} else {
							String[] emails = mailinglist.getList().split(";");
							ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(emails));
							emailslist.remove(person.getEmail());
						}
					}
				});
				return cb;
			}).forEachOrdered((cb) -> {
				maillistbox.getChildren().add(cb);
			});
		}

	}

	@FXML
	void emailpatient() {

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			String email = "";
			switch (selected) {
			case 0:
				email = selectedpatient.getEmails();
				break;
			case 1:
				email = selectedstaff.getEmail();
				break;
			case 2:
				email = selectedperson.getEmail();
				break;
			}

			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				try {
					mailto = new URI("mailto:" + email);
					desktop.mail(mailto);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(this.getClass().getResource("\\view\\dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Error");
			alert.setHeaderText("Desktop Linking Not Supported");
			alert.setContentText("Please manually copy email addresses");

			ButtonType buttonTypeOne = new ButtonType("Okay");

			alert.getButtonTypes().setAll(buttonTypeOne);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
			} else {
			}

		}
	}

	@FXML
	void copyEmail() {
		String email = "";
		String name = "";
		switch (selected) {
		case 0:
			email = selectedpatient.getEmails();
			name = selectedpatient.getFirstName() + " " + selectedpatient.getLastName();
			break;
		case 1:
			email = selectedstaff.getEmail();
			name = selectedstaff.getTitle() + "." + selectedstaff.getFirstName() + " " + selectedstaff.getLastName();
			break;
		case 2:
			email = selectedperson.getEmail();
			name = selectedperson.getFirstName() + " " + selectedperson.getLastName();
			break;
		}

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(email);
		clipboard.setContent(content);

		String toastMsg = name + " Email Adresses Copied";
		int toastMsgTime = 3000; // 3 seconds
		int fadeInTime = 500; // 0.5 seconds
		int fadeOutTime = 500; // 0.5 seconds
		int position = 2; // Position of Tost, puts it centre

		Toast.makeText(mainapp.getPrimaryStage(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime, position);

	}

	@FXML
	void edit() {
		switch (selected) {
		case 0:
			editpatient();
			break;
		case 1:
			editstaff();
			break;
		case 2:
			editperson();
			break;
		}
	}

	void editpatient() {
		mainapp.editPatient(selectedpatient);
	}

	void editstaff() {
		mainapp.editStaff(selectedstaff);
	}

	void editperson() {
		mainapp.editPerson(selectedperson, selectedperson.getPersonType());
	}

	@FXML
	void remove() {
		switch (selected) {
		case 0:
			removepatient();
			break;
		case 1:
			removestaff();
			break;
		case 2:
			removeperson();
			break;
		}
	}

	void removepatient() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Patient?");
		alert.setHeaderText("Do you wish to permanently archive this patient and all associated appointments? - Patient Notes will be Archived.");
		alert.setContentText("WARNING: archived patients and appointments will be unencrypted and must therefore be manually copied to a secure location and deleted if security requires.");
		ButtonType buttonTypeOne = new ButtonType("Archive");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedPeople() + ";" + String.valueOf(selectedpatient.getPatientNumber());
			mainapp.appPrefs.setDeletedPeople(deletedstring);
			mainapp.ArchivePatient(selectedpatient);

			ObservableList<Appointment> toremove = FXCollections.observableArrayList();

			mainapp.getAppointments().stream().filter((appointment) -> (appointment.getPatients().contains(selectedpatient.getPatientNumber()))).forEachOrdered((appointment) -> {
				toremove.add(appointment);
			});
			toremove.forEach((appointment) -> {
				mainapp.ArchiveAppointment(appointment);
			});

			String[] patientemails = selectedpatient.getEmails().split(";");

			mainapp.getMailingLists().forEach((mailinglist) -> {
				String[] listemails = mailinglist.getList().split(";");
				ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(listemails));
				ArrayList<String> removelist = new ArrayList<String>(Arrays.asList(listemails));
				emailslist.forEach((email) -> {
					for (String patientemail : patientemails) {
						if (patientemail.equals(email)) {
							removelist.add(email);
						}
					}
				});
				emailslist.removeAll(removelist);
				String liststring = "";
				for (String email : emailslist) {					
					liststring += email + ";";
				}
				mailinglist.setList(liststring);
			});

			mainapp.savePatientDataToFile(mainapp.getFilePath(), false);
			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.saveNotesDataToFile(mainapp.getFilePath(), false);
			mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
			mainapp.setVersion();

		} else {

		}
	}

	void removestaff() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Staff?");
		alert.setHeaderText("Do you wish to permanently delete this staff member and Archive all associated appointments?");
		alert.setContentText("WARNING: archived appointments will be unencrypted and must therefore be manually copied to a secure location and deleted if security requires.");
		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedPeople() + ";" + String.valueOf(selectedstaff.getStaffNumber());
			mainapp.appPrefs.setDeletedPeople(deletedstring);
			mainapp.removeAccount(selectedstaff);

			ObservableList<Note> ntoremove = FXCollections.observableArrayList();
			mainapp.getNotes().stream().filter((note) -> (note.getStaffCode() == selectedstaff.getStaffNumber())).forEachOrdered((note) -> {
				ntoremove.add(note);
			});
			mainapp.getNotes().removeAll(ntoremove);

			ObservableList<Appointment> toremove = FXCollections.observableArrayList();

			mainapp.getAppointments().stream().filter((appointment) -> (appointment.getStaffMembers().contains(selectedstaff.getStaffNumber()))).forEachOrdered((appointment) -> {
				toremove.add(appointment);
			});
			toremove.forEach((appointment) -> {
				mainapp.ArchiveAppointment(appointment);
			});
			mainapp.getMailingLists().forEach((mailinglist) -> {
				String[] listemails = mailinglist.getList().split(";");
				ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(listemails));
				ArrayList<String> removelist = new ArrayList<String>(Arrays.asList(listemails));
				emailslist.stream().filter((email) -> (selectedstaff.getEmail().equals(email))).forEachOrdered((email) -> {
					removelist.add(email);
				});
				emailslist.removeAll(removelist);
				String liststring = "";
				for (String email : emailslist) {
					liststring = liststring + ";" + email;
				}
				mailinglist.setList(liststring);
			});

			mainapp.saveAccountDataToFile(mainapp.getFilePath(), false);
			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.saveNotesDataToFile(mainapp.getFilePath(), false);
			mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
			mainapp.setVersion();

		} else {

		}
	}

	void removeperson() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Person?");
		alert.setHeaderText("Do you wish to permanently delete this person?");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedPeople() + ";" + String.valueOf(selectedperson.getId());
			mainapp.appPrefs.setDeletedPeople(deletedstring);
			mainapp.removePerson(selectedperson);

			mainapp.getMailingLists().forEach((mailinglist) -> {
				String[] listemails = mailinglist.getList().split(";");
				ArrayList<String> emailslist = new ArrayList<String>(Arrays.asList(listemails));
				ArrayList<String> removelist = new ArrayList<String>(Arrays.asList(listemails));
				emailslist.stream().filter((email) -> (selectedpatient.getEmails().equals(email))).forEachOrdered((email) -> {
					removelist.add(email);
				});
				emailslist.removeAll(removelist);
				String liststring = "";
				for (String email : emailslist) {
					liststring = liststring + ";" + email;
				}
				mailinglist.setList(liststring);
			});

			mainapp.savePersonDataToFile(mainapp.getFilePath(), false);
			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.saveNotesDataToFile(mainapp.getFilePath(), false);
			mainapp.saveAppointmentsDataToFile(mainapp.getFilePath(), false);
			mainapp.setVersion();

		} else {

		}
	}

	@FXML
	void viewHoliday() {
		mainapp.viewHoliday(selectedstaff);
	}

	@FXML
	void viewPassword() {
		mainapp.viewPassword(selectedstaff);
	}

	void calculateOwed() {
		ObservableList<String> currencies = FXCollections.observableArrayList(); // List of currencies in a single patient's appointments
		ObservableList<Double> values = FXCollections.observableArrayList(); // Outstanding price for each currency

		for (Appointment appointment : mainapp.getAppointments()) {
			if (appointment.getAppType() != 9999) { // If appointment is not a meeting
				if (appointment.getPatients().contains(selectedpatient.getPatientNumber())) {
					if (appointment.getPaid() == false) {
						if (!currencies.contains(appointment.getCurrency().trim())) {
							currencies.add(appointment.getCurrency().trim());
							System.out.println("Pre rounding value: " + String.valueOf(appointment.getPrice() / appointment.getPatients().size()));
							BigDecimal result = new BigDecimal(appointment.getPrice() / appointment.getPatients().size()).setScale(2, BigDecimal.ROUND_HALF_UP);
							System.out.println("Post rounding value: " + String.valueOf(result));
							System.out.println("Post double value: " + String.valueOf(Double.valueOf(result.toString())));

							values.add(Double.valueOf(result.toString())); // Yes this is super janky, I'll change all monetary values to bigdecimal at some point.
						} else {
							System.out.println("Pre rounding value: " + String.valueOf(appointment.getPrice() / appointment.getPatients().size()));
							BigDecimal result = new BigDecimal(values.get(currencies.indexOf(appointment.getCurrency().trim())) + appointment.getPrice() / appointment.getPatients().size()).setScale(2,
									BigDecimal.ROUND_HALF_UP);
							System.out.println("Post rounding value: " + String.valueOf(result));
							System.out.println("Post double value: " + String.valueOf(Double.valueOf(result.toString())));

							values.set(currencies.indexOf(appointment.getCurrency().trim()), Double.valueOf(result.toString()));// adds prices of all the same currencies P.S Yes this is super janky,
																																// I'll change all monetary values to bigdecimal at some point.
						}
					}
				}
			}
		}

		String text = "";
		for (int i = 0; i < currencies.size(); i++) {
			text += currencies.get(i) + " " + values.get(i) + " ";
		}
		owedlabel.setText(text);
	}

	@FXML
	void payAll() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Pay All Outstanding?");
		alert.setHeaderText("Do you wish to mark all patient appointments as paid?");

		ButtonType buttonTypeOne = new ButtonType("Yes");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			for (Appointment appointment : mainapp.getAppointments()) {
				if (appointment.getPatients().contains(selectedpatient.getPatientNumber())) {
					appointment.setPaid(true);
				}
			}
			calculateOwed();
		} else {
			alert.close();
		}
	}

	public static String htmlEscape(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("%0D%0A");
				break;
			case ' ':
				builder.append("%20");
				break;
			case '$':
				builder.append("DOLLARS");
				break;
			case '€':
				builder.append("EUR");
				break;
			case '¢':
				builder.append("CENT");
				break;
			case '£':
				builder.append("GBP");
				break;
			case '¥':
				builder.append("YEN");
				break;
			// We need Tab support here, because we print StackTraces as HTML
			case '\t':
				builder.append("&nbsp; &nbsp; &nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}

	@FXML
	void invoice() {
		String patientemail = "";

		if (selectedpatient.getEmails() != null) {
			String[] emails = selectedpatient.getEmails().split(";");
			for (String email : emails) {
				patientemail += ";" + email;
			}
		}

		String subject = selectedpatient.getFirstName() + " " + selectedpatient.getLastName() + " Invoice: " + LocalDate.now().toString();
		subject = htmlEscape(subject);

		String body = selectedpatient.getFirstName() + " " + selectedpatient.getLastName() + " Invoice: " + LocalDate.now().toString() + "\n\n";

		// ---------------------------------- GENERATING INVOICE BODY

		ObservableList<String> currencies = FXCollections.observableArrayList(); // List of currencies in a single patient's appointments
		ObservableList<Double> values = FXCollections.observableArrayList(); // Outstanding price for each currency
		ObservableList<String> paidcurrencies = FXCollections.observableArrayList(); // List of currencies in a single patient's appointments
		ObservableList<Double> paidvalues = FXCollections.observableArrayList(); // Outstanding price for each currency

		for (Appointment appointment : mainapp.getAppointments()) {
			System.out.println("Selected Patient Number: " + String.valueOf(selectedpatient.getPatientNumber()));
			System.out.println("Appointment Patients: " + String.valueOf(appointment.getPatients()));

			if (appointment.getPatients() != null) {

				if (appointment.getPatients().contains(selectedpatient.getPatientNumber())) {
					for (AppointmentType type : mainapp.getAppointmentTypes()) {
						if (type.getId() == appointment.getType()) {
							body += type.getName() + "\n";
							break;
						}
					}
					body += "APPOINTMENT: \nDate: " + appointment.getDate().toString() + "\n" + "Time: " + appointment.getTime().toString() + "\n";
					if (appointment.getPaid() == false) {
						body += "Paid: N\n";
						body += appointment.getCurrency() + " " + String.valueOf(appointment.getPrice() / appointment.getPatients().size()) + "\n\n";

						if (!currencies.contains(appointment.getCurrency().trim())) {
							currencies.add(appointment.getCurrency().trim());
							values.add(appointment.getPrice() / appointment.getPatients().size());

						} else {
							values.set(currencies.indexOf(appointment.getCurrency().trim()),
									values.get(currencies.indexOf(appointment.getCurrency().trim())) + appointment.getPrice() / appointment.getPatients().size()); // Phoar what a
							// line of
							// code;
							// adds
							// prices
							// of all
							// the
							// same
							// currencies
						}
					} else {
						body += "Paid: Y\n";
						body += appointment.getCurrency() + " " + String.valueOf(appointment.getPrice() / appointment.getPatients().size()) + "\n\n";

						if (!paidcurrencies.contains(appointment.getCurrency().trim())) {
							paidcurrencies.add(appointment.getCurrency().trim());
							paidvalues.add(appointment.getPrice() / appointment.getPatients().size());

						} else {
							paidvalues.set(paidcurrencies.indexOf(appointment.getCurrency().trim()),
									paidvalues.get(paidcurrencies.indexOf(appointment.getCurrency().trim())) + appointment.getPrice() / appointment.getPatients().size()); // Phoar
							// same
							// currencies
						}
					}
				}
			}
		}

		String paidtext = "";
		for (int i = 0; i < paidcurrencies.size(); i++) {
			paidtext += paidcurrencies.get(i) + " " + paidvalues.get(i) + " ";
		}
		String text = "";
		for (int i = 0; i < currencies.size(); i++) {
			text += currencies.get(i) + " " + values.get(i) + " ";
		}
		body += "Total Paid: " + paidtext + "\n" + "Total Outstanding: " + text + "\n\n Invoice generated using CANOPY system \n\n";

		// --------------------------------

		body = htmlEscape(body);

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				try {
					mailto = new URI("mailto:" + patientemail + "?subject=" + subject + "&body=" + body);
					desktop.mail(mailto);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	@FXML
	void createReminder() {
		mainapp.createReminder(selectedpatient);
	}

	@FXML
	void addContact() {
		mainapp.addContact(selectedpatient);
	}

	@FXML
	void viewFile() {
		File openfile = new File(selectedpatient.getPermissionFile());
		try {
			Desktop.getDesktop().open(openfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
