// Canopy Main Application
// Authored by Andrew Graham, 2017

package canopy.app;

import canopy.app.model.Account;
import canopy.app.model.AccountListWrapper;
import canopy.app.model.Appointment;
import canopy.app.model.AppointmentType;
import canopy.app.model.AppointmentTypeWrapper;
import canopy.app.model.AppointmentV1Wrapper;
import canopy.app.model.AppointmentWrapper;
import canopy.app.model.Appointment_v1;
import canopy.app.model.Building;
import canopy.app.model.BuildingWrapper;
import canopy.app.model.Licence;
import canopy.app.model.Licence_v1;
import canopy.app.model.MailingList;
import canopy.app.model.MailingListWrapper;
import canopy.app.model.Note;
import canopy.app.model.NoteListWrapper;
import canopy.app.model.NoteListWrapperv1;
import canopy.app.model.NoteType;
import canopy.app.model.NoteTypeWrapper;
import canopy.app.model.Note_v1;
import canopy.app.model.Patient;
import canopy.app.model.PatientListWrapper;
import canopy.app.model.PatientV1ListWrapper;
import canopy.app.model.PatientV2ListWrapper;
import canopy.app.model.Patient_v1;
import canopy.app.model.Patient_v2;
import canopy.app.model.Person;
import canopy.app.model.PersonListWrapper;
import canopy.app.model.PersonType;
import canopy.app.model.PersonTypeWrapper;
import canopy.app.model.Prefs;
import canopy.app.model.Room;
import canopy.app.model.RoomWrapper;
import canopy.app.model.Version;
import canopy.app.util.CalendarMethod;
import canopy.app.util.Toast;
import canopy.app.view.NoteTypesDialogController;
import canopy.app.view.appointmentDialogController;
import canopy.app.view.appointmentListController;
import canopy.app.view.appointmentTypeDialogController;
import canopy.app.view.appointmentTypeListController;
import canopy.app.view.appointmentTypeSideBarController;
import canopy.app.view.avaliabilityDialogController;
import canopy.app.view.borderPaneController;
import canopy.app.view.calendarController;
import canopy.app.view.contactsController;
import canopy.app.view.forwardDialogController;
import canopy.app.view.loginController;
import canopy.app.view.mailListsDialogController;
import canopy.app.view.mailSettingsController;
import canopy.app.view.mailSideBarController;
import canopy.app.view.mailingListController;
import canopy.app.view.noteDialogController;
import canopy.app.view.notesListController;
import canopy.app.view.passwordDialogController;
import canopy.app.view.patientDialogController;
import canopy.app.view.personDialogController;
import canopy.app.view.personListController;
import canopy.app.view.personSideBarController;
import canopy.app.view.personTypeDialogController;
import canopy.app.view.reminderDialogController;
import canopy.app.view.roomDialogController;
import canopy.app.view.roomsListController;
import canopy.app.view.roomsSideBarController;
import canopy.app.view.sideBarController;
import canopy.app.view.staffDialogController;

import java.awt.Desktop;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.RandomStringUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.email.Recipient;
import org.simplejavamail.mailer.MailerBuilder;

//MAIL ATTEMPT

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;

import com.sun.mail.smtp.*;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Attendee;
import biweekly.property.Organizer;
import biweekly.property.RequestStatus;
import biweekly.property.Summary;
import biweekly.util.Duration;
import biweekly.util.Frequency;
import biweekly.util.Gobble;
import biweekly.util.Recurrence;

//MAIL ATTEMPT

/**
 *
 * @author Andy
 */
public final class MainApp extends Application {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) { // Launches main method
		launch(args);

	}

	private Stage primaryStage; // Main Application Stage
	private AnchorPane rootLayout;
	private Account currentaccount; // Current Logged On Account
	private ObservableList<Account> accountdata = FXCollections.observableArrayList(); // All Accounts in system
																						// (clinic/ company specific)
	private ObservableList<Patient> patientdata = FXCollections.observableArrayList(); // All Patients in system
	private ObservableList<Person> persondata = FXCollections.observableArrayList(); // All People in system
	private ObservableList<Building> buildingsdata = FXCollections.observableArrayList(); // All Buildings in system
	private ObservableList<Room> roomdata = FXCollections.observableArrayList(); // All Rooms in system
	private ObservableList<Appointment> appointments = FXCollections.observableArrayList(); // All Appointments
	private ObservableList<Note> notedata = FXCollections.observableArrayList(); // All Appointments in system
	private ObservableList<AppointmentType> appointmenttypes = FXCollections.observableArrayList(); // All Appointment
																									// Types
	private ObservableList<MailingList> mailinglists = FXCollections.observableArrayList(); // All Mailing Lists
	private ObservableList<PersonType> persontypes = FXCollections.observableArrayList(); // All person types
	private ObservableList<NoteType> notetypes = FXCollections.observableArrayList(); // All note types
	private SimpleBooleanProperty smalltext = new SimpleBooleanProperty(false); // Boolean controlling text size
	private boolean layout_started = false;

	/**
	 *
	 */
	public Prefs appPrefs;

	/**
	 *
	 */
	public Licence licence;

	/**
	 *
	 */
	public Version version;

	public String AppVersion = "v0.996"; // Application Version, used to check for updates ||| Actual version v0.990

	/**
	 *
	 */
	public int selectedtab = 0;
	// 0 = Appointments
	// 1 = People
	// 2 = Mail
	// 3 = Rooms
	// 4 = ?
	// 5 = Logout

	loginController loginController; // Log in screen controller

	calendarController calendarController; // Controller for gridpane containing days

	personListController personListController; // Controller for list of patients

	borderPaneController borderPaneController; // Controller for parent border pane

	sideBarController appointmentsidebarcontroller; // Controller for appointment sidebar

	appointmentListController appointmentListController; // Controller for patient specific appointment list

	personSideBarController personSideBarController; // Controller for patent side bar

	notesListController notesListController; // Controller for patient specific notes list

	roomsListController roomsListController; // FXML Controller

	roomsSideBarController roomsSideBarController; // FXML Controller

	mailingListController mailingListController; // FXML Controller

	mailSettingsController mailsettingscontroller; // FXML Controller

	mailSideBarController mailSideBarController; // FXML Controller

	patientDialogController patientDialogController; // FXML Controller

	staffDialogController staffDialogController; // FXML Controller

	personDialogController personDialogController; // FXML Controller

	personTypeDialogController personTypeDialogController; // FXML Controller

	appointmentTypeListController appointmentTypeListController; // FXML Controller

	appointmentTypeSideBarController appointmentTypeSideBarController; // FXML Controller

	Locale locale = Locale.getDefault(); // Returns system locale.

	MainApp mainApp; // Initialises class instance

	/**
	 *
	 */
	public MainApp() {
		// System.out.println("MainApp");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		prefs.put("AppVersion", AppVersion); // Holds App version for later
		mainApp = this;

		try {

			Task<Void> launchtasks = new Task<Void>() {

				@Override
				protected Void call() {

					while (!layout_started) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							OnLaunch(); // Runs Launch method on new thread
						}
					});

					return null;
				}
			};

			new Thread(launchtasks).start();

		} catch (Exception e) { // Catches the launch method failing
			writeError(e);
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Error");
			alert.setHeaderText("Launch Has Failed");
			alert.setContentText("The Cause is likely Corrupted Data: Click 'OK' to resolve then relaunch. If this has failed, please contact support with the following error message: " + e);

			ButtonType buttonTypeOne = new ButtonType("Resolve");

			alert.getButtonTypes().setAll(buttonTypeOne);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {

				try {
					prefs.clear();
					getPrimaryStage().close();
				} catch (Exception er) {
					writeError(e);
					er.printStackTrace();
				}

			} else {
				Platform.exit();
			}
		}

	}

	// SCREEN CONTROL
	@Override
	public void start(Stage primaryStage) { // Starts application
		// System.out.println("Start");

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// set Stage boundaries to visible bounds of the main screen
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Canopy");
		// System.out.println("Icon setting");
		Image image = new Image(getClass().getResourceAsStream("view/iconlarge.png"));
		this.primaryStage.getIcons().add(image);

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		initLogin(); // Initialises login screen
		layout_started = true;
	}

	/**
	 *
	 */
	public void initLogin() { // Initialises root login layout

		try {

			// Load layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/login.fxml"));
			AnchorPane login = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(login);
			primaryStage.setScene(scene);

			// Initialise and define screen size
			primaryStage.show();
			primaryStage.setMaximized(true);

			// primaryStage.setWidth(1024); // For testing Small Screens
			// primaryStage.setHeight(576);

			// primaryStage.setWidth(1920); // For testing Midscale Screens
			// primaryStage.setHeight(1080);

			// Give the controller access to the main app.
			loginController = loader.getController();
			loginController.setMainApp(this);

			if (primaryStage.getWidth() < 1500) {
				smalltext.set(true);
				// System.out.println("Small Text Is True");
				login.getStylesheets().clear();
				login.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}
			primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

					if (primaryStage.getWidth() > 1500) {
						smalltext.set(false);
						login.getStylesheets().clear();
						login.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
					}
					if (primaryStage.getWidth() < 1500) {
						smalltext.set(true);
						login.getStylesheets().clear();
						login.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
					}

				}
			});

			loginController.initialise(scene.getWidth(), scene.getHeight());
			selectedtab = 5;

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param number
	 */
	public void populateAccounts(int number) { // Populates accounts with dummy data
		for (int i = 0; i < number; i++) {
			int personnum = appPrefs.getPersonID() + 1;
			Account account = new Account("test", "test", "test", "test", "test", "test", personnum, LocalTime.now(), LocalTime.now().plusHours(1), "test", "test", 2);
			appPrefs.setPersonID(appPrefs.getPersonID() + 1);
			accountdata.add(account);
		}
	}

	public void checkUpdate() {
		try {

			URL url = new URL("https://canopyadminsoftware.com/checkupdate");
			File checkfile = new File(getFilePath().getAbsolutePath() + "/AppVersion.txt");
			if (checkfile.exists()) {
				checkfile.delete();
			}

			System.out.println("opening version check connection");
			InputStream in = url.openStream();
			FileOutputStream fos = new FileOutputStream(checkfile);

			System.out.println("reading version check file...");
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from
			// connection
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			fos.close();
			in.close();
			System.out.println("version check file was downloaded");

			String checkver = "";
			BufferedReader br = new BufferedReader(new FileReader(checkfile));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				checkver = sb.toString();
				if (!AppVersion.trim().equals(checkver.trim())) {

					Alert alert = new Alert(AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.setAlwaysOnTop(true);

					DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
					dialogPane.getStyleClass().add(".dialog-pane");
					dialogPane.setPrefWidth(550);

					alert.setTitle("Update Avaliable");
					alert.setHeaderText("Update Avaliable");
					alert.setContentText("This system is running Canopy " + AppVersion + ", Version " + checkver + " is currently avaliable: Update? ");

					ButtonType buttonTypeOne = new ButtonType("Update");
					ButtonType buttonTypeCancel = new ButtonType("Not Now");
					ButtonType buttonTypeNever = new ButtonType("Never");

					alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel, buttonTypeNever);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == buttonTypeOne) {
						downloadUpdate();
						confirmupdate();
					} else if (result.get() == buttonTypeNever) {
						Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
						prefs.putBoolean("UpdateVersion", false);

						alert.close();
					} else {
						alert.close();
					}
				}
			} finally {
				br.close();
			}

		} catch (Exception e) {

		}

	}

	public sideBarController getSideBarController() {
		return appointmentsidebarcontroller;
	}

	public void confirmupdate() { // Lets the user know that canopy will close when download is installed
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");
		dialogPane.setPrefWidth(550);

		alert.setTitle("Downloading");
		alert.setHeaderText("Update Downloading");
		alert.setContentText("The update is now downloading. Canopy will close and the installer will run when finished.");

		ButtonType buttonTypeOne = new ButtonType("Ok");

		alert.getButtonTypes().setAll(buttonTypeOne);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			alert.close();

		} else {
			alert.close();
		}
	}

	public void downloadUpdate() {
		try {

			String toastMsg = "Downloading Update";
			int toastMsgTime = 600000; // 600 seconds
			int fadeInTime = 500; // 0.5 seconds
			int fadeOutTime = 500; // 0.5 seconds
			Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 2);

			Task<Void> downloader = new Task<Void>() {

				@Override
				protected Void call() throws Exception {

					URL url = new URL("https://canopyadminsoftware.com/download");
					String home = System.getProperty("user.home");
					File instfile = new File(home + "/Downloads/" + "/installer.exe");

					if (instfile.exists()) {
						instfile.delete();
					}

					System.out.println("opening update download connection");
					InputStream in = url.openStream();
					FileOutputStream fos = new FileOutputStream(instfile);

					System.out.println("reading updated installer file...");
					int length = -1;
					byte[] buffer = new byte[1024];// buffer for portion of data from
					// connection
					int kbytecounter = 0; // kbytes read counter
					int mbytecounter = 0; // mbytes read counter
					while ((length = in.read(buffer)) > -1) {
						fos.write(buffer, 0, length);

						kbytecounter += 1;
						// System.out.println(String.valueOf(kbytecounter)+"Kb downloaded");
						if (kbytecounter == 1024) {
							kbytecounter = 0;
							mbytecounter += 1;
							System.out.println(String.valueOf(mbytecounter) + "Mb downloaded");
						}
					}
					fos.close();
					in.close();
					System.out.println("update installer downloaded");

					Desktop desktop = Desktop.getDesktop();

					desktop.open(instfile);

					Platform.exit();

					return null;
				}
			};

			new Thread(downloader).start();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 *
	 * @param number
	 */
	public void populatePatients(int number) { // Populates patients with dummy data
		for (int i = 0; i < number; i++) {
			int personnum = appPrefs.getPersonID() + 1;
			ObservableList<Integer> staffmembercodes = FXCollections.observableArrayList();
			staffmembercodes.add(0);
			Patient patient = new Patient("test", "test", "test", "test", personnum, "test", "test", "test", LocalDate.now(), "test", "test", "test|test", staffmembercodes);
			appPrefs.setPersonID(appPrefs.getPersonID() + 1);
			patientdata.add(patient);
		}
	}

	/**
	 *
	 * @param number
	 */
	public void populateAppointments(int number) { // Populates appointments with dummy data
		for (int i = 0; i < number; i++) {
			// Room room = new Room("blah", 0, 0);
			// Appointment appointment = new Appointment("0", "0",
			// LocalDate.now().plusDays(i), LocalTime.now(), room, 0, "test", 60, 0,0,0);
			// appointments.add(appointment);
		}
	}

	/**
	 *
	 * @param file
	 * @param licence
	 */
	public void saveLicenceToFile(File file, Licence licence) { // Saves licence to specified file

		File prefsfile = new File(file.getAbsolutePath() + "/Licence.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(Licence.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(licence, prefsfile);

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param file
	 */
	public void LoadLicenceFromFile(File file) { // Loads licence from specified file
		File prefsfile = new File(file.getAbsolutePath() + "/Licence.xml");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		prefs.put("AdminKey", accountdata.get(0).getKey());

		if (!prefsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + prefsfile.getPath() + " does not exist: filepath is incorrect. - Is filesharing network avaliable? Close Application or choose new filepath.");

			ButtonType buttonTypeOne = new ButtonType("Choose Different FilePath");
			ButtonType buttonTypeTwo = new ButtonType("Retry");

			ButtonType buttonTypeCancel = new ButtonType("Close Application");

			alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				ReChooseFilePath();
			} else if (result.get() == buttonTypeTwo) {
				LoadLicenceFromFile(getFilePath());
			} else {
				Platform.exit();
			}

		} else {
			try {
				JAXBContext context = JAXBContext.newInstance(Licence.class);
				Unmarshaller um = context.createUnmarshaller();

				// System.out.println("Unmarshalling Licence");

				// Reading XML from the file and unmarshalling.
				licence = (Licence) um.unmarshal(prefsfile);

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				for (File checkfile : listOfFiles) {
					// System.out.println("Checking File: " + checkfile.getName());
					if (checkfile.getName().contains("Licence") && !checkfile.getName().equals("Licence.xml")) {
						checkfile.delete();
					}
				}

			} catch (Exception e) { // catches ANY exception
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						LoadLicencev1FromFile(file);
					}
				});
			}
		}
	}

	public void LoadLicencev1FromFile(File file) { // Loads licence from specified file
		File prefsfile = new File(file.getAbsolutePath() + "/Licence.xml");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		prefs.put("AdminKey", accountdata.get(0).getKey());

		try {
			JAXBContext context = JAXBContext.newInstance(Licence.class);
			Unmarshaller um = context.createUnmarshaller();

			// System.out.println("Unmarshalling Licence");

			// Reading XML from the file and unmarshalling.
			Licence_v1 templicence = (Licence_v1) um.unmarshal(prefsfile);

			licence = new Licence(templicence.getEndDate(), templicence.getID(), 3);

			File folder = getFilePath();
			File[] listOfFiles = folder.listFiles();

			for (File checkfile : listOfFiles) {
				// System.out.println("Checking File: " + checkfile.getName()); // Prints all
				// files being checked for a clash
				if (checkfile.getName().contains("Licence") && !checkfile.getName().equals("Licence.xml")) {
					checkfile.delete();
				}
			}

		} catch (Exception e) { // catches ANY exception
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	/**
	 *
	 * @return
	 */
	public boolean checklicence() {
		LoadLicenceFromFile(getFilePath());

		if (licence.getDaysUntilExpiry() > 100) {
			Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

			prefs.putBoolean("reminder", true);
		}

		System.out.println("Licence ID: " + licence.getID());
		System.out.println("Licence End Date: " + licence.getEndDate().toString());
		System.out.println("Days Until Expiry: " + licence.getDaysUntilExpiry().toString());
		System.out.println("Type : " + licence.getType().toString());

		if (licence.getEndDate().isBefore(LocalDate.now())) {
			return false;
		} else {
			int expirydays = licence.getDaysUntilExpiry();
			licence.calculateDaysUntilExpiry();
			saveLicenceToFile(getFilePath(), licence);
			return expirydays >= licence.getDaysUntilExpiry() - 1; // If days until expiry has increased, then system
																	// date has been fiddled and licence is invalid. -1
																	// is in case same is accessed from
			// the other side of the world for whatever reason.

		}

	}

	/**
	 *
	 */
	public void OnLaunch() {

		// Make Sure FilePath Is Chosen
		ChooseFilePath();

		// System.out.println("Filepath Chosen");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		// System.out.println("Prefs Chosen");

		new Thread() {

			public void run() {

				File f = new File(getFilePath().getAbsolutePath() + "/Version.xml");
				if (!f.exists() && !f.isDirectory()) {
					// System.out.println("Version Does not Exist");

					try {
						version = new Version();
						firstSaveVersion(getFilePath());
					} catch (Exception e) {
						// System.out.println("catching new version error");

						// TODO: handle exception
					}
				}

				// System.out.println("Version about to load");

				appPrefs = new Prefs();
				try {
					LoadVersion(getFilePath());
				} catch (Exception e) {
					// TODO: handle exception
				}

				try {

					LoadPreferenceDataFromFile(getFilePath());

					if (!appPrefs.getKeysScrambled()) {
						// updateKeyEncryption();
						prefs.putBoolean("keysUpdated", true);
					} else {
						prefs.putBoolean("keysUpdated", true);
					}

					prefs.put("KeyOne", appPrefs.getKeyOne());
					prefs.put("KeyTwo", appPrefs.getKeyTwo());
					prefs.put("KeyThree", appPrefs.getKeyThree());
					prefs.put("KeyFour", appPrefs.getKeyFour());
					prefs.put("KeyFive", appPrefs.getKeyFive());

				} catch (Exception e) {
					e.printStackTrace();

					if (!LoadPreferenceDataFromBackup(getFilePath())) {

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// System.out.println("Prefs failed to load");
								Alert alert = new Alert(AlertType.ERROR);
								Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
								stage.setAlwaysOnTop(true);

								alert.setTitle("Error");
								e.printStackTrace();
								alert.setHeaderText("Could not load data");
								alert.setContentText("Could not load data from file:\n" + getFilePath() + "\\Preferences.xml" + ". Error Message: " + e);

								alert.showAndWait();

							}
						});

					}

				}

				LoadAccountDataFromFile(getFilePath());

				// System.out.println("Checking License");

				if (!checklicence()) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Alert alert = new Alert(AlertType.ERROR);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.setAlwaysOnTop(true);

							DialogPane dialogPane = alert.getDialogPane();
							dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
							dialogPane.getStyleClass().add(".dialog-pane");

							alert.setTitle("Error");
							alert.setHeaderText("LICENCE IS INVALID OR HAS EXPIRED");
							alert.setContentText("Please Replace Licence Key. Contact info@canopyadminsoftware.com");

							ButtonType buttonTypeOne = new ButtonType("Close Application");

							alert.getButtonTypes().setAll(buttonTypeOne);

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == buttonTypeOne) {
								getPrimaryStage().close();
							} else {
								getPrimaryStage().close();
							}
						}
					});

				}

				accountdata.clear();

				if (!appPrefs.getMailingListsSaved()) {
					MailingList allpatients = new MailingList();
					allpatients.setListName("All Patients");
					allpatients.setId(0);
					allpatients.setDescription("Mailing List containing all Patients.");
					allpatients.setList("");

					MailingList allstaff = new MailingList();
					allstaff.setListName("All Staff");
					allstaff.setId(1);
					allstaff.setDescription("Mailing List containing all Staff.");
					allstaff.setList("");

					appPrefs.setMailID(1);
					mailinglists.add(allpatients);
					mailinglists.add(allstaff);

					saveMailingListDataToFile(getFilePath(), false);
				} else {
					LoadMailingListDataFromFile(getFilePath());

					// mailinglists.forEach((list) -> {
					// System.out.println("Mailing Lists: " + list.getListName());
					// });
				}

				if (prefs.getBoolean("UpdateVersion", true)) {
					Task<Void> update = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							checkUpdate();
							return null;
						}
					};
					update.run();
				}

				try {
					LoadNoteTypeDataFromFile(getFilePath());
				} catch (Exception e) {
					NoteType defaulttype = new NoteType("Default", 0, "225,225,225");
					notetypes.add(defaulttype);

					saveNoteTypeDataToFile(getFilePath(), false);
				}
			}
		}.start();
	}

	// APPOINTMENTS

	/**
	 *
	 * @param date
	 * @param patient
	 * @param account
	 * @param room
	 */

	public void addNewAppointment(LocalDate date, Patient patient, Account account, Room room) { // Creates empty
																									// appointment, and
																									// initialises 'new
		// appointment'
		// dialog, with reference to empty appointment

		Appointment newappointment = new Appointment();

		showAppointmentEditWindow(newappointment, date, patient, account, room, false);
	}

	/**
	 * This method adds an appointment to the list of appointments, then refreshes the appointment list or calendar. It also sends the automated event invites, or prompts the user to do so.
	 * 
	 * @param appointment
	 */
	public void actuallyAddAppointment(Appointment appointment, boolean edit) { // Adds specified appointment to list of
																				// appointments,
		// then refreshes calendar view

		if (edit) {

			if (appPrefs.getAutoEmailPatients()) { // If the preferences say to auto email patients, try to do so.
				sendAppointmentInvitePatients(appointment, 1);
			}
			if (appPrefs.getAutoEmailStaff()) { // If the preferences say to auto email staff, try to do so.
				sendAppointmentInviteStaff(appointment, 1);
			}

		} else {// If the appointment is a new appointment, and not an old one being edited

			appointments.add(appointment); // Add appointment to appointments observablelist

			if (appPrefs.getAutoEmailPatients()) { // If the preferences say to auto email patients, try to do so.
				sendAppointmentInvitePatients(appointment, 0);
			}
			if (appPrefs.getAutoEmailStaff()) { // If the preferences say to auto email staff, try to do so.
				sendAppointmentInviteStaff(appointment, 0);
			}
		}

		saveAppointmentsDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());

		if (appointmentListController != null) {
			appointmentListController.refreshlist();
		}

		try {

			calendarController.initialise(); // Refreshes calendar view

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
		// System.out.println("Appointments Size post all: " +
		// String.valueOf(appointments.size()));

	}

	/**
	 * Adds specified appointment to list of appointments, without saving, refreshing calendar view or preferences. Used for recurring appointments.
	 * 
	 * @param appointment
	 */
	public void actuallyAddAppointment2(Appointment appointment, boolean edit) {

		if (edit) {

			if (appPrefs.getAutoEmailPatients()) { // If the preferences say to auto email patients, try to do so.
				sendAppointmentInvitePatients(appointment, 1);
			}
			if (appPrefs.getAutoEmailStaff()) { // If the preferences say to auto email staff, try to do so.
				sendAppointmentInviteStaff(appointment, 1);
			}

		} else {// If the appointment is a new appointment, and not an old one being edited

			appointments.add(appointment); // Add appointment to appointments observablelist

			if (appPrefs.getAutoEmailPatients()) { // If the preferences say to auto email patients, try to do so.
				sendAppointmentInvitePatients(appointment, 0);
			}
			if (appPrefs.getAutoEmailStaff()) { // If the preferences say to auto email staff, try to do so.
				sendAppointmentInviteStaff(appointment, 0);
			}
		}
	}

	/**
	 *
	 * @param appointment
	 * @param date
	 * @param patient
	 * @param account
	 * @param room
	 * @return
	 */
	public boolean showAppointmentEditWindow(Appointment appointment, LocalDate date, Patient patient, Account account, Room room, boolean edit) { // Shows edit
		// window
		// for a
		// specific appointment.
		try {

			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/appointmentdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Appointment");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			appointmentDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAppointment(appointment);
			controller.initialise(this, date, patient);
			if (edit) {
				controller.setedit(appointment);
			} else {
				controller.setPatient(patient);
				controller.setAccount(account);
				controller.setRoom(room);
				controller.setDialogStage(dialogStage);
			}

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 */
	public void addNewAppointmentType() {

		AppointmentType newtype = new AppointmentType();

		showAppointmentTypeEditWindow(newtype);
	}

	/**
	 *
	 * @param type
	 * @param edit
	 */
	public void actuallyAddAppointmentType(AppointmentType type, Boolean edit) { // Adds specified appointment to list
																					// of appointments,
		// then refreshes calendar view

		if (!edit) {
			appointmenttypes.add(type);
			if (appointmentTypeListController != null) {
				appointmentTypeListController.refreshlists(appointmenttypes);
			}
		} else {
		}

		saveAppointmentTypeDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());

	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public boolean showAppointmentTypeEditWindow(AppointmentType type) { // Shows edit window
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/appointmenttypedialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Appointment Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			appointmentTypeDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setType(type);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	// PEOPLE
	// --------Patients

	/**
	 *
	 */
	public void addNewPatient() { // Creates empty patient object, and initialises 'new patient' dialog, with
		// reference to empty patient

		Patient newpatient = new Patient();

		showPatientEditWindow(newpatient, false);
	}

	/**
	 *
	 * @param patient
	 * @param edit
	 */
	public void actuallyAddPatient(Patient patient, Boolean edit) { // Adds specified patient to patients list

		if (!edit) {
			// System.out.println("Adding Patient: " + patient.getFirstName());
			patientdata.add(patient);
			mailinglists.get(0).addEmail(patient.getEmails());
			saveMailingListDataToFile(getFilePath(), false);
		} else {
		}
		savePatientDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());
		personSideBarController.setPatient(patient);
		// personListController.refreshpatients(patientdata);
		personListController.refreshlist();

	}

	/**
	 *
	 * @param patient
	 */
	public void addNewNote(Patient patient) { // I guess this does nothing...

	}

	/**
	 *
	 * @param patient
	 * @param edit
	 * @return
	 */
	public boolean showPatientEditWindow(Patient patient, Boolean edit) { // Shows edit window for specified patient

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/patientdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Patient");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			patientDialogController = loader.getController();
			patientDialogController.setMainApp(this);
			patientDialogController.setDialogStage(dialogStage);
			patientDialogController.setPatient(patient);

			if (edit) {
				patientDialogController.setedit();
			}

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

			return patientDialogController.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	// --------Staff

	/**
	 *
	 */

	public void addNewAccount() { // Creates empty account object, and initialises 'new staff' dialog

		Account newaccount = new Account();

		showAccountEditWindow(newaccount, false);
	}

	/**
	 *
	 * @param account
	 * @param edit
	 */
	public void actuallyAddAccount(Account account, Boolean edit) { // Add account or update account into accounts list
		if (!edit) {
			accountdata.add(account);
			mailinglists.get(1).addEmail(account.getEmail());
			saveMailingListDataToFile(getFilePath(), false);
			savePreferenceDataToFile(getFilePath());
		} else {
		}
		saveAccountDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());
		personSideBarController.setAccount(account);
		personListController.refreshstaff(getListAccounts());

	}

	/**
	 *
	 * @param account
	 * @param edit
	 * @return
	 */
	public boolean showAccountEditWindow(Account account, Boolean edit) { // Shows edit window for specified staff

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/staffdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Staff Member");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			staffDialogController = loader.getController();
			staffDialogController.setMainApp(this);
			staffDialogController.setDialogStage(dialogStage);

			staffDialogController.setStaff(account);

			if (edit) {
				staffDialogController.setedit();
			}

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

			return staffDialogController.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	// --------Misc People

	/**
	 *
	 * @param persontype
	 */

	public void addNewPerson(Integer persontype) { // Creates empty person object, and initialises 'new person' dialog

		Person newperson = new Person();
		showPersonEditWindow(newperson, false, persontype);
	}

	/**
	 *
	 * @param person
	 * @param edit
	 */
	public void actuallyAddPerson(Person person, Boolean edit) { // Adds specified person to people list

		if (!edit) {
			persondata.add(person);
		} else {
		}
		savePersonDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());
		personSideBarController.setPerson(person);
		personListController.refreshlist();

	}

	/**
	 *
	 * @param person
	 * @param edit
	 * @param persontype
	 * @return
	 */
	public boolean showPersonEditWindow(Person person, Boolean edit, Integer persontype) { // Shows edit window for
																							// specified staff

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/persondialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			personDialogController = loader.getController();
			personDialogController.setMainApp(this);
			personDialogController.setDialogStage(dialogStage);
			personDialogController.setPerson(person);
			personDialogController.setPersonType(persontype);

			if (edit) {
				personDialogController.setedit();
			}

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

			return personDialogController.isOkClicked();

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	// --------Person Types

	/**
	 *
	 */

	public void addNewPersonType() { // Creates empty person object, and initialises 'new person' dialog

		PersonType newpersontype = new PersonType();
		newpersontype.setId(persontypes.size());
		showPersonTypeEditWindow(newpersontype);
	}

	/**
	 *
	 * @param persontype
	 */
	public void actuallyAddPersonType(PersonType persontype) { // Adds specified person to people list
		persontypes.add(persontype);
		savePersonTypeDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());
		personListController.actuallyAddPersonType(persontype.getTypeName());
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public boolean showPersonTypeEditWindow(PersonType type) { // Shows edit window for specified staff

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/persontypedialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create Person Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			personTypeDialogController = loader.getController();
			personTypeDialogController.setMainApp(this);
			personTypeDialogController.setDialogStage(dialogStage);
			personTypeDialogController.setPersonType(type);

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

			return personTypeDialogController.isOkClicked();

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	// MAIL

	/**
	 *
	 * @return
	 */
	public boolean showMailListEditWindow() { // Shows edit window for mailing list
		try {

			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/mailinglistdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Mailing List");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			mailListsDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.initialise(this);
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<MailingList> getMailingLists() {
		return mailinglists;
	}

	/**
	 *
	 * @param mlist
	 */
	public void setSelectedMailingList(MailingList mlist) { // Set selected mailing list in the mailing list sidebar
		mailSideBarController.setList(mlist);
	}

	/**
	 *
	 * @param type
	 */
	public void setSelectedAppointmentType(AppointmentType type) { // Set selected appointment type in appointment type
																	// list sidebar
		appointmentTypeSideBarController.setType(type);
	}

	/**
	 *
	 */
	public void addNewMailingList() {
		showMailListEditWindow();
	}

	/**
	 *
	 * @param mailingList
	 */
	public void actuallyAddMailList(MailingList mailingList) { // Add mailing list to system
		mailinglists.add(mailingList);
		mailingListController.refreshlists(mailinglists);
		saveMailingListDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());
	}

	// ROOMS

	/**
	 *
	 */

	public void addNewRoom() { // Creates empty appointment, and initialises 'new appointment'
		// dialog, with reference to empty appointment

		Room newroom = new Room();

		showNewRoomWindow(newroom);
	}

	/**
	 *
	 * @param room
	 */
	public void actuallyAddRoom(Room room) { // Actually adds room to room list

		room.setId(roomdata.size());
		roomdata.add(room);
		saveRoomDataToFile(getFilePath(), false);
		savePreferenceDataToFile(getFilePath());

		roomsListController.initialise();
	}

	/**
	 *
	 * @param building
	 */
	public void actuallyAddBuilding(Building building) { // Actually adds building

		buildingsdata.add(building);
		saveBuildingDataToFile(getFilePath(), false);
	}

	/**
	 *
	 * @param room
	 * @return
	 */
	public boolean showNewRoomWindow(Room room) { // Shows creation window for new room
		try {

			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/roomdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create Room");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			roomDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setRoom(room);
			controller.initialise(this);
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	public Locale getLocale() { // Returns default locale
		return locale;
	}

	void setSizeListener(AnchorPane node) {
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				if (primaryStage.getWidth() > 1500) {
					smalltext.set(false);
					node.getStylesheets().clear();
					node.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
				}
				if (primaryStage.getWidth() < 1500) {
					smalltext.set(true);
					node.getStylesheets().clear();
					node.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
				}

			}
		});
	}

	/**
	 *
	 * @param usernamestr
	 * @param passwordstr
	 */
	public void clickLogin(String usernamestr, String passwordstr) { // Checks credentials on login request

		// System.out.println("Attempting Logon: " + usernamestr + ", " + passwordstr);

		Task<Void> loader = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// STUFF TO DO ON THREAD GOES HERE
				if (AccountsSaved()) {
					// Check Username Exists
					File path = getFilePath();
					if (path != null) {
						LoadAccountDataFromFile(path);
					} else {

					}
				}
				return null;
			}
		};
		loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE
				boolean found = false; // Defaults account found to false

				for (int i = 0; i < accountdata.size(); i++) { // Iterate through accounts

					if (accountdata.get(i).getPermission() != 4) { // If staff is an interacting staff type

						if (accountdata.get(i).getUserName().equals(usernamestr) && accountdata.get(i).getPassword().equals(passwordstr)) { // If username and account
																																			// data are
																																			// equal, log account in.
							found = true;
							currentaccount = accountdata.get(i);
							break;
						}
					}
				}

				if (found) { // If a match has been found
					loginController.correctnamepass();

					System.out.println("Login Succesfull");

					Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

					boolean reminder = prefs.getBoolean("reminder", true);

					// System.out.println("License: ");
					//
					// System.out.println("Expires on : " + licence.getEndDate().toString());
					//
					// System.out.println("Expires in : " + licence.getDaysUntilExpiry().toString());
					//
					// System.out.println("Type : " + licence.getType().toString());										

					if (licence.getDaysUntilExpiry() < 8 && (currentaccount.getPermission() == 1 || currentaccount.getPermission() == 0) && reminder) {

						Alert alert = new Alert(AlertType.INFORMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.setAlwaysOnTop(true);

						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
						dialogPane.getStyleClass().add(".dialog-pane");

						alert.setTitle("Licence Expiring Soon");
						alert.setHeaderText("This Licence Expires in " + licence.getDaysUntilExpiry().toString() + " days. To renew licence, contact info@canopyadminsoftware.com");

						ButtonType buttonTypeOne = new ButtonType("Do Not Remind Me Again");

						ButtonType buttonTypeTwo = new ButtonType("Dismiss");

						alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == buttonTypeOne) {
							prefs.putBoolean("reminder", false);
						}
						if (result.get() == buttonTypeTwo) {
							alert.close();
						}
					}

					Task<Void> loader = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							System.out.println("loader running");

							LocalDate checkdate = LocalDate.parse(prefs.get("BackupDate", LocalDate.now().minusDays(1).toString()));
							if (checkdate.isBefore(LocalDate.now())) {
								// System.out.println("saving to backup");
								loginController.setLoginText("Saving data to backup...");
								saveToBackup();
								// System.out.println("saved to backup");
							}

							if (AppointmentsSaved()) {
								System.out.println("Appointment loader running");
								try {
									loginController.setLoginText("Loading Appointments...");
									LoadAppointmentDataFromFile(getFilePath());
								} catch (Exception e) {
									e.printStackTrace();
								}
								System.out.println("Appointment loader succeeded");

							}
							if (PatientsSaved()) {

								System.out.println("Note loader running");
								loginController.setLoginText("Loading Notes...");
								LoadNoteDataFromFile(getFilePath());
								System.out.println("Note loader succeeded");

								System.out.println("Note Type loader running");
								LoadNoteTypeDataFromFile(getFilePath());
								System.out.println("Note Type loader succeeded");

								System.out.println("Patient loader running");
								loginController.setLoginText("Loading Patients...");

								LoadPatientDataFromFile(getFilePath());
								CleanPatientsList();
								System.out.println("Patient loader succeeded");

							}
							if (BuildingsSaved()) {
								System.out.println("Building loader running");
								loginController.setLoginText("Loading Rooms...");
								LoadBuildingDataFromFile(getFilePath());
								System.out.println("Building loader succeeded");

							}
							if (RoomsSaved()) {
								System.out.println("Room loader running");
								LoadRoomDataFromFile(getFilePath());
								System.out.println("Room loader succeeded");

							}
							if (MailingListsSaved()) {
								System.out.println("Mail loader running");
								loginController.setLoginText("Loading Mail...");
								LoadMailingListDataFromFile(getFilePath());
								System.out.println("Mail loader succeeded");

							}
							if (PersonTypesSaved()) {
								System.out.println("People loader running");
								loginController.setLoginText("Loading Contacts...");
								LoadPeopleDataFromFile(getFilePath());
								System.out.println("People loader succeeded");

							}
							if (PeopleSaved()) {
								System.out.println("PersonType loader running");
								LoadPersonTypeDataFromFile(getFilePath());
								System.out.println("persontype loader succeeded");

							}
							if (AppointmentTypesSaved()) {
								System.out.println("AppointmentType loader running");
								LoadAppointmentTypeDataFromFile(getFilePath());
								System.out.println("Appointmenttype loader succeeded");

							}
							loginController.setLoginText("Loading Complete");
							return null;

						}
					};
					loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							// STUFF TO DO AFTER THREAD GOES HERE
							System.out.println("loader succeeded");

							loginController.loggingIn();

						}
					});
					new Thread(loader).start();

				} else {
					loginController.incorrectnamepass();
				}
			}
		});
		new Thread(loader).start();

	}

	/**
	 *
	 */
	public void changescreen() { // Changes from logon screen to parent borderpane

		try {
			// Load layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/mainborderpane.fxml"));
			rootLayout = (AnchorPane) loader.load();

			if (primaryStage.getWidth() < 1500) {
				smalltext.set(true);
				// System.out.println("Small Text Is True");
				rootLayout.getStylesheets().clear();
				// System.out.println("Rootlayout small stylesheets");
				rootLayout.getStylesheets().remove(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
				rootLayout.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}
			primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

					if (primaryStage.getWidth() > 1500) {
						smalltext.set(false);
						rootLayout.getStylesheets().clear();
						// System.out.println("Rootlayout small stylesheets");
						rootLayout.getStylesheets().remove(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
						rootLayout.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
					}
					if (primaryStage.getWidth() < 1500) {
						smalltext.set(true);
						rootLayout.getStylesheets().clear();
						// System.out.println("Rootlayout small stylesheets");
						rootLayout.getStylesheets().remove(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
						rootLayout.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
					}

				}
			});

			if (getSmallText()) {

			}

			setSizeListener(rootLayout);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);

			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			borderPaneController controller = loader.getController();
			controller.setMainApp(this);
			borderPaneController = controller;
			controller.getBorderPane();
			setToPlannerScreen();

			primaryStage.setMaximized(false);
			primaryStage.setMaximized(true);

			controller.initialise(scene.getWidth(), scene.getHeight(), scene.getY(), scene.getWindow().getHeight() - scene.getHeight() - scene.getY());

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	public void setToPlannerScreen() { // Set the screen to the planner screen

		try {

			selectedtab = 0;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/calendar.fxml"));
			AnchorPane calendarPane = (AnchorPane) loader.load();

			if (getSmallText()) {
				calendarPane.getStylesheets().clear();
				calendarPane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			} else {
				calendarPane.getStylesheets().clear();
				calendarPane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
			}

			setSizeListener(calendarPane);

			borderPaneController.clear();
			borderPaneController.setCenterContent(calendarPane);
			borderPaneController.bindCenterSize(calendarPane);

			calendarController = loader.getController();

			FXMLLoader sideloader = new FXMLLoader();
			sideloader.setLocation(MainApp.class.getResource("view/sidebar.fxml"));
			AnchorPane sidePane = (AnchorPane) sideloader.load();

			if (getSmallText()) {

				sidePane.getStylesheets().clear();
				sidePane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
				sidePane.setPrefWidth(200);
			} else {
				sidePane.setPrefWidth(300);
			}

			borderPaneController.addRight(sidePane);
			borderPaneController.bindRightsize(sidePane, primaryStage.getScene().getWindow().getHeight() - primaryStage.getScene().getHeight() - primaryStage.getScene().getY());

			appointmentsidebarcontroller = sideloader.getController();
			appointmentsidebarcontroller.setMainApp(mainApp);

			// primaryStage.setMaximized(true);
			System.out.println("primarystage maximised");
			calendarController.checkwidth();
			System.out.println("calendar controller width checked");

			// primaryStage.setWidth(1024); // For testing Small Screens
			// primaryStage.setHeight(576);

			// primaryStage.setWidth(1920); // For testing Medium Screens
			// primaryStage.setHeight(1080);

		} catch (Exception e) {
			writeError(e);
		}

		Task<Void> loadertask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("Planner screen open loader task running");
				if (!checkVersion()) {
					if (AppointmentTypesSaved()) {
						LoadAppointmentTypeDataFromFile(getFilePath());
					}
					if (AppointmentsSaved()) {
						LoadAppointmentDataFromFile(getFilePath());
					}
					if (PatientsSaved()) {
						LoadPatientDataFromFile(getFilePath());
						LoadNoteDataFromFile(getFilePath());
						CleanPatientsList();
					}
					if (BuildingsSaved()) {
						LoadBuildingDataFromFile(getFilePath());
					}
					if (RoomsSaved()) {
						LoadRoomDataFromFile(getFilePath());
					}
				}
				return null;
			}
		};
		loadertask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE

				calendarController.setMainApp(mainApp);
				try {
					calendarController.initialise();
				} catch (IOException e) {
					writeError(e);
					e.printStackTrace();
				}

			}
		});
		System.out.println("About to start loadertask");
		new Thread(loadertask).start();
		System.out.println("loadertask started");

	}

	/**
	 *
	 */
	public void setToPatientScreen() { // Set the screen to the patient screen
		selectedtab = 0;

		try {

			selectedtab = 1;

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/personlist.fxml"));
			AnchorPane patientpane = (AnchorPane) loader.load();

			if (getSmallText()) {

				patientpane.getStylesheets().clear();
				patientpane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			setSizeListener(patientpane);

			borderPaneController.clear();
			borderPaneController.setCenterContent(patientpane);

			personListController = loader.getController();

			FXMLLoader listloader = new FXMLLoader();
			listloader.setLocation(MainApp.class.getResource("view/appointmentlist.fxml"));
			AnchorPane listPane = (AnchorPane) listloader.load();

			if (getSmallText()) {

				listPane.getStylesheets().clear();
				listPane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			appointmentListController = listloader.getController();

			FXMLLoader notesloader = new FXMLLoader();
			notesloader.setLocation(MainApp.class.getResource("view/notesbar.fxml"));
			AnchorPane notespane = (AnchorPane) notesloader.load();

			if (getSmallText()) {

				notespane.getStylesheets().clear();
				notespane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			notesListController = notesloader.getController();

			FXMLLoader detailsloader = new FXMLLoader();
			detailsloader.setLocation(MainApp.class.getResource("view/personsidebar.fxml"));
			AnchorPane detailspane = (AnchorPane) detailsloader.load();

			if (getSmallText()) {

				detailspane.getStylesheets().clear();
				detailspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
				detailspane.setPrefWidth(200);
			} else {
				detailspane.setPrefWidth(300);
			}

			personSideBarController = detailsloader.getController();
			personSideBarController.setMainApp(mainApp);

			borderPaneController.setCenterSecondContent(listPane);
			borderPaneController.setCenterThirdContent(notespane);

			// borderPaneController.bindCenterSize(patientpane);
			borderPaneController.bindCenterSecondSize(listPane, patientpane);
			borderPaneController.bindCenterThirdSize(notespane, patientpane);

			borderPaneController.addRight(detailspane);
			borderPaneController.bindRightsize(detailspane, primaryStage.getScene().getWindow().getHeight() - primaryStage.getScene().getHeight() - primaryStage.getScene().getY());

			// if (patientdata.size() != 0) {
			// setSelectedPatient(patientdata.get(0));
			// notesListController.setPatient(patientdata.get(0));
			// }

		} catch (Exception e) {
			writeError(e);
			e.printStackTrace();
		}
		Task<Void> loadertask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (!checkVersion()) {
					if (AppointmentTypesSaved()) {
						LoadAppointmentTypeDataFromFile(getFilePath());
					}
					if (AppointmentsSaved()) {
						LoadAppointmentDataFromFile(getFilePath());
					}
					if (AccountsSaved()) {
						LoadAccountDataFromFile(getFilePath());
					}
					if (PatientsSaved()) {
						// System.out.println("Patients returning saved");
						LoadPatientDataFromFile(getFilePath());
						LoadNoteDataFromFile(getFilePath());
						CleanPatientsList();
					}
					if (MailingListsSaved()) {
						LoadMailingListDataFromFile(getFilePath());
					}
					if (BuildingsSaved()) {
						LoadBuildingDataFromFile(getFilePath());
					}
					if (RoomsSaved()) {
						LoadRoomDataFromFile(getFilePath());
					}
					if (PersonTypesSaved()) {
						LoadPeopleDataFromFile(getFilePath());
					}
					if (PeopleSaved()) {
						LoadPersonTypeDataFromFile(getFilePath());
					}
				}
				return null;
			}
		};
		loadertask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				personListController.setMainApp(mainApp);
				personListController.initialise();

				appointmentListController.setMainApp(mainApp);
				appointmentListController.initialise();

				notesListController.setMainApp(mainApp);
				notesListController.initialise();
			}
		});

		new Thread(loadertask).start();

	}

	/**
	 *
	 */
	public void setToMailScreen() { // Set the screen to the mail screen

		Task<Void> loader = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (!checkVersion()) {
					if (PatientsSaved()) {
						LoadPatientDataFromFile(getFilePath());
						LoadNoteDataFromFile(getFilePath());
						CleanPatientsList();
					}
					if (MailingListsSaved()) {
						LoadMailingListDataFromFile(getFilePath());
					}
				}
				return null;
			}
		};
		loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE

				selectedtab = 0;

				try {

					selectedtab = 2;

					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("view/mailinglists.fxml"));
					AnchorPane mailpane = (AnchorPane) loader.load();

					borderPaneController.clear();
					borderPaneController.setCenterContent(mailpane);
					borderPaneController.bindCenterSize(mailpane);

					mailingListController = loader.getController();
					mailingListController.setMainApp(mainApp);
					mailingListController.initialise();

					FXMLLoader settingsloader = new FXMLLoader();
					settingsloader.setLocation(MainApp.class.getResource("view/mailsettings.fxml"));
					AnchorPane settingspane = (AnchorPane) settingsloader.load();

					mailsettingscontroller = settingsloader.getController();
					mailsettingscontroller.setMainApp(mainApp);
					mailsettingscontroller.initialise();

					FXMLLoader detailsloader = new FXMLLoader();
					detailsloader.setLocation(MainApp.class.getResource("view/mailinglistsidebar.fxml"));
					AnchorPane detailspane = (AnchorPane) detailsloader.load();

					if (getSmallText()) {

						mailpane.getStylesheets().clear();
						mailpane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
						settingspane.getStylesheets().clear();
						settingspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());

						detailspane.getStylesheets().clear();
						detailspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
						detailspane.setPrefWidth(200);
					} else {
						detailspane.setPrefWidth(300);
						settingspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
					}

					mailSideBarController = detailsloader.getController();
					mailSideBarController.setMainApp(mainApp);
					borderPaneController.setCenterSecondContent(settingspane);
					borderPaneController.bindCenterSecondSize(settingspane, mailpane);
					borderPaneController.addRight(detailspane);
					borderPaneController.bindRightsize(detailspane, primaryStage.getScene().getWindow().getHeight() - primaryStage.getScene().getHeight() - primaryStage.getScene().getY());

				} catch (IOException e) {
					writeError(e);
					e.printStackTrace();
				}
			}
		});
		new Thread(loader).start();

	}

	public void setToRoomScreen() { // Set the screen to the room screen

		Task<Void> loader = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (!checkVersion()) {
					if (AppointmentTypesSaved()) {
						LoadAppointmentTypeDataFromFile(getFilePath());
					}
					if (AppointmentsSaved()) {
						LoadAppointmentDataFromFile(getFilePath());
					}
					if (BuildingsSaved()) {
						LoadBuildingDataFromFile(getFilePath());
					}
					if (RoomsSaved()) {
						LoadRoomDataFromFile(getFilePath());
					}
				}
				return null;
			}
		};
		loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE

				selectedtab = 0;

				try {

					selectedtab = 3;

					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("view/roomslist.fxml"));
					AnchorPane roompane = (AnchorPane) loader.load();

					if (getSmallText()) {

						roompane.getStylesheets().clear();
						roompane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
					}

					borderPaneController.clear();
					borderPaneController.setCenterContent(roompane);

					roomsListController = loader.getController();
					roomsListController.setMainApp(mainApp);
					roomsListController.initialise();

					FXMLLoader listloader = new FXMLLoader();
					listloader.setLocation(MainApp.class.getResource("view/appointmentlist.fxml"));
					AnchorPane listPane = (AnchorPane) listloader.load();

					appointmentListController = listloader.getController();
					appointmentListController.setMainApp(mainApp);
					appointmentListController.initialise();

					FXMLLoader detailsloader = new FXMLLoader();
					detailsloader.setLocation(MainApp.class.getResource("view/roomssidebar.fxml"));
					AnchorPane detailspane = (AnchorPane) detailsloader.load();

					if (getSmallText()) {

						detailspane.getStylesheets().clear();
						detailspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
						detailspane.setPrefWidth(200);
					} else {
						detailspane.setPrefWidth(300);
					}

					roomsSideBarController = detailsloader.getController();
					roomsSideBarController.setMainApp(mainApp);

					borderPaneController.setCenterSecondContent(listPane);
					borderPaneController.bindCenterSecondSize(listPane, roompane);
					borderPaneController.addRight(detailspane);
					borderPaneController.bindRightsize(detailspane, primaryStage.getScene().getWindow().getHeight() - primaryStage.getScene().getHeight() - primaryStage.getScene().getY());

				} catch (IOException e) {
					writeError(e);
					e.printStackTrace();
				}
			}
		});
		new Thread(loader).start();

	}

	/**
	 *
	 */
	public void setToTypeScreen() { // Set the screen to the appointment type screen
		Task<Void> loader = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (!checkVersion()) {
					if (AppointmentTypesSaved()) {
						LoadAppointmentTypeDataFromFile(getFilePath());
					}
				}
				return null;
			}
		};
		loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE

				selectedtab = 0;

				try {

					selectedtab = 4;

					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("view/appointmenttypelist.fxml"));
					AnchorPane typepane = (AnchorPane) loader.load();
					if (getSmallText()) {

						typepane.getStylesheets().clear();
						typepane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
					}

					borderPaneController.clear();
					borderPaneController.setCenterContent(typepane);
					borderPaneController.bindCenterSize(typepane);

					appointmentTypeListController = loader.getController();
					appointmentTypeListController.setMainApp(mainApp);
					appointmentTypeListController.initialise();

					FXMLLoader detailsloader = new FXMLLoader();
					detailsloader.setLocation(MainApp.class.getResource("view/appointmenttypesidebar.fxml"));
					AnchorPane detailspane = (AnchorPane) detailsloader.load();

					if (getSmallText()) {

						detailspane.getStylesheets().clear();
						detailspane.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
						detailspane.setPrefWidth(200);
					} else {
						detailspane.setPrefWidth(300);
					}

					appointmentTypeSideBarController = detailsloader.getController();
					appointmentTypeSideBarController.setMainApp(mainApp);
					borderPaneController.addRight(detailspane);
					borderPaneController.bindRightsize(detailspane, primaryStage.getScene().getWindow().getHeight() - primaryStage.getScene().getHeight() - primaryStage.getScene().getY());

				} catch (IOException e) {
					writeError(e);
					e.printStackTrace();
				}
			}
		});
		new Thread(loader).start();

	}

	/**
	 *
	 * @param room
	 */
	public void setSelectedRoom(Room room) { // Sets selected patient with respect to 'patient' screen
		appointmentListController.setMainApp(this);
		appointmentListController.setRoom(room);
		roomsSideBarController.setMainApp(this);
		roomsSideBarController.setRoom(room);
	}

	/**
	 *
	 * @param appointment
	 */
	public void setRightPlannerPaneContents(Appointment appointment) { // Sets side bar contents appropriately
		appointmentsidebarcontroller.setAppointment(appointment);
	}

	/**
	 *
	 * @return
	 */
	public Stage getPrimaryStage() { // Returns main stage
		return primaryStage;
	}

	/**
	 *
	 * @return
	 */
	public boolean AccountsSaved() { // Returns true if accounts have been saved
		return appPrefs.getAccountsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean AppointmentTypesSaved() { // Returns true if appointment types have been saved
		return appPrefs.getAppointmentTypesSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean AppointmentsSaved() { // Returns true if appointments have been saved
		return appPrefs.getAppointmentsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean PatientsSaved() { // Returns true if patients have been saved
		return appPrefs.getPatientsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean BuildingsSaved() { // Returns true if buildings have been saved
		return appPrefs.getBuildingsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean RoomsSaved() { // Returns true if rooms have been saved
		return appPrefs.getRoomsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean MailingListsSaved() { // Returns true if mailing lists have been saved
		return appPrefs.getMailingListsSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean PersonTypesSaved() { // Returns true if person types have been saved
		return appPrefs.getTypesSaved();
	}

	/**
	 *
	 * @return
	 */
	public boolean PeopleSaved() { // Returns true if people have been saved
		return appPrefs.getPeopleSaved();
	}

	/**
	 *
	 */
	public void ChooseFilePath() { // Chooses master file path
		try {
			Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

			String filePath = prefs.get("filePath", null);
			if (filePath == null) {

				Alert alert = new Alert(AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
				dialogPane.getStyleClass().add(".dialog-pane");

				alert.setTitle("Choose Master Directory");
				alert.setHeaderText("Choose Master File Directory");
				alert.setContentText("It looks like this is the first time this system is using Canopy: Please navigate to the (shared) master directory.");

				ButtonType buttonTypeOne = new ButtonType("Choose");
				ButtonType buttonTypeCancel = new ButtonType("Close");

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					File file = chooseDirectory(primaryStage);
					if (file != null) {
						prefs.put("filePath", file.getAbsolutePath());

						try {

							checkFilePath(file);

						} catch (Exception e) {

						}
					}
				} else {
					getPrimaryStage().close();
				}

			}
		} catch (Exception e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void ReChooseFilePath() { // Choose new master file path
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		File file = chooseDirectory(primaryStage);
		prefs.put("filePath", file.getAbsolutePath());

		checkFilePath(file);

	}

	public void checkFilePath(File file) {
		File accountsfile = new File(file.getAbsolutePath() + "/Accounts.xml");

		if (!accountsfile.exists()) {
			Alert alert2 = new Alert(AlertType.ERROR);
			Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
			stage2.setAlwaysOnTop(true);

			DialogPane dialogPane2 = alert2.getDialogPane();
			dialogPane2.getStylesheets().add(this.getClass().getResource("view/dialog_Stylesheet.css").toExternalForm());
			dialogPane2.getStyleClass().add(".dialog-pane");

			alert2.setTitle("Error");
			alert2.setHeaderText("Invalid Master Directory");
			alert2.setContentText("It appears the selected Directory is invalid; select another?");

			ButtonType buttonTypeOne2 = new ButtonType("Choose");
			ButtonType buttonTypeCancel2 = new ButtonType("Close Canopy");

			alert2.getButtonTypes().setAll(buttonTypeOne2, buttonTypeCancel2);

			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == buttonTypeOne2) {
				ReChooseFilePath();
			} else {
				getPrimaryStage().close();
			}
		} else {

			Task<Void> loader = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					LoadPreferenceDataFromFile(file);
					LoadAccountDataFromFile(file);
					LoadLicenceFromFile(file); // Loads new Licence
					return null;
				}
			};
			loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					System.out.println("Licence Loaded, end date " + String.valueOf(licence.getEndDate().toString()));

					Alert alert3 = new Alert(AlertType.INFORMATION);
					Stage stage3 = (Stage) alert3.getDialogPane().getScene().getWindow();
					stage3.setAlwaysOnTop(true);

					DialogPane dialogPane3 = alert3.getDialogPane();
					dialogPane3.getStylesheets().add(this.getClass().getResource("\\view\\dialog_Stylesheet.css").toExternalForm());
					dialogPane3.getStyleClass().add(".dialog-pane");

					alert3.setTitle("Master Directory Linked");
					alert3.setHeaderText("The master directory has been linked! Licence end date: " + String.valueOf(licence.getEndDate()));

					ButtonType buttonTypeOne3 = new ButtonType("Ok");

					alert3.getButtonTypes().setAll(buttonTypeOne3);

					Optional<ButtonType> result3 = alert3.showAndWait();
					if (result3.get() == buttonTypeOne3) {
						alert3.close();
					}

				}
			});
			new Thread(loader).start();

		}
	}

	/**
	 *
	 * @return
	 */
	public File getFilePath() { // Returns the chosen file path

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);

		if (filePath == null) {
			File file = chooseDirectory(primaryStage);

			return file;
		} else {
			return new File(filePath);
		}
	}

	/**
	 *
	 * @param file
	 */
	public void setFilePath(File file) { // Sets the file path of the currently loaded file. The path is persisted in
											// the OS specific registry.

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			System.out.println("Setting File Path: " + file.getAbsolutePath());
			prefs.put("filePath", file.getPath());
		} else {
			prefs.remove("filePath");
		}
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadAccountDataFromFile(File file) { // Loads account data from specified filepath

		boolean same = true; // initialises change variable

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File accountsfile = new File(file.getAbsolutePath() + "/Accounts.xml");

		if (!accountsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + accountsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {

			try {
				JAXBContext context = JAXBContext.newInstance(AccountListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				AccountListWrapper wrapper = (AccountListWrapper) um.unmarshal(accountsfile);

				accountdata.clear();
				accountdata.addAll(wrapper.getAccounts());

				// System.out.println(accountdata.get(0).getFirstName());
				// System.out.println(accountdata.get(0).getUserName());
				// System.out.println(accountdata.get(0).getStaffNumber());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					// System.out.println("Checking File: " + checkfile.getName());
					if (checkfile.getName().contains("Accounts") && !checkfile.getName().equals("Accounts.xml")) {

						String toastMsg = "Resolving Clash: Accounts";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						// System.out.println("File Found");
						JAXBContext context2 = JAXBContext.newInstance(AccountListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						AccountListWrapper wrapper2 = (AccountListWrapper) um2.unmarshal(checkfile);

						ObservableList<Account> tempaccounts = FXCollections.observableArrayList();
						tempaccounts.addAll(wrapper2.getAccounts());

						// System.out.println("Clash Loaded");

						String[] deletedstr = appPrefs.getDeletedPeople().split(";"); // Returns string array of deleted

						for (Account ac : tempaccounts) { // For all Clash Accounts
							// System.out.println("Temp Acc Loaded: " + ac.getFirstName());

							boolean match = false;

							for (Account comparison : accountdata) { // For all Loaded Accounts
								// System.out.println("Comparison Account: " + comparison.getFirstName());

								if (comparison.getFirstName().equals(ac.getFirstName()) && comparison.getLastName().equals(ac.getLastName())) { // Checks if clashed
																																				// account is present in
																																				// loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// System.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == ac.getStaffNumber()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer personnum = mainApp.appPrefs.getPersonID() + 1;
									ac.setStaffNumber(personnum);
									mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

									accountdata.add(ac); // ADD THEM BACK IN
									// System.out.println("Temp Acc Getting Added to list");
								}
							}
						}

						// System.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveAccountDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				e.printStackTrace();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// LoadAccountDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;

	}

	public void CleanNotesList() {
		System.out.println("Clean Note List Function Run");
		String filename = "notes_decrypted_2";
		for (Note n : notedata) {
			writeFile("_________________________", filename);
			writeFile("Date: " + n.getDate().toString(), filename);
			writeFile("Staffcode: " + n.getStaffCode().toString(), filename);
			writeFile("Patient No.: " + n.getPatient().toString(), filename);
			writeFile("Note ID: " + n.getId().toString(), filename);
			writeFile("Text: " + n.getText(), filename);
			writeFile("Type: " + n.getType().toString(), filename);
		}
	}

	public void CleanPatientsList() {
		System.out.println("Clean Patient List Function Run");
		for (Patient p : patientdata) {
			System.out.println("Patient check: " + p.getFirstName() + " " + p.getLastName() + ". Patient ID: " + String.valueOf(p.getPatientNumber()));

			for (Patient comparison : patientdata) {
				if (comparison.getPatientNumber().equals(p.getPatientNumber())) {
					System.out.println("Matching Patient ID Found: " + p.getFirstName() + " " + p.getLastName());

					if (!(comparison.getFirstName().equals(p.getFirstName()) && comparison.getLastName().equals(p.getLastName()) && comparison.getStreet().equals(p.getStreet()))) {

						System.out.println("Non matching patient info found");

						Integer personnum = mainApp.appPrefs.getPersonID() + 1;
						comparison.setPatientNumber(personnum);
						mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

						ObservableList<Note> tempnotedata = FXCollections.observableArrayList();

						System.out.println("Patient ID changed");
						for (Note note : notedata) {

							if (note.getId() != 0 && note.getStaffCode() != null && note.getText() != null && !note.getText().trim().equals("") && note.getPatient() != null) {
								System.out.println("Checking note: ID " + String.valueOf(note.getId()));
								if (note.getPatient().equals(p.getPatientNumber())) {
									System.out.println("Note ID match. Creating new note");
									Note newnote = new Note(comparison, note.getStaffCode(), note.getDate(), note.getTime(), note.getText(), 0);
									System.out.println("Note duplicated");
									tempnotedata.add(newnote);
									System.out.println("Note added");
								}
							}

						}
						if (!tempnotedata.isEmpty()) {
							notedata.addAll(tempnotedata);
						}
					}

				}
			}
			System.out.println("Patient checked");

		}
		System.out.println("Function end");
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadPatientDataFromFile(File file) { // Loads account data from specified filepath
		System.out.println("Loading Patient Data From File 1");

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		System.out.println("Loading Patient Data From File 1.2");

		boolean same = true; // initialises change variable

		System.out.println("Loading Patient Data From File 1.3");

		File patientsfile = new File(file.getAbsolutePath() + "/Patients.xml");

		System.out.println("Loading Patient Data From File 2");

		if (!patientsfile.exists()) {
			System.out.println("Loading Patient Data From File Error 1");

			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + patientsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {

			System.out.println("Loading Patient Data From File 3");

			try {
				JAXBContext context = JAXBContext.newInstance(PatientListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				PatientListWrapper wrapper = (PatientListWrapper) um.unmarshal(patientsfile);

				patientdata.clear();
				patientdata.addAll(wrapper.getPatients());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;
				System.out.println("Loading Patient Data From File 4");

				for (File checkfile : listOfFiles) {
					// System.out.println("Checking File: " + checkfile.getName());
					if (checkfile.getName().contains("Patients") && !checkfile.getName().equals("Patients.xml")) {

						String toastMsg = "Resolving Clash: Patients";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						// System.out.println("File Found");
						JAXBContext context2 = JAXBContext.newInstance(PatientListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						PatientListWrapper wrapper2 = (PatientListWrapper) um2.unmarshal(checkfile);

						ObservableList<Patient> temppatients = FXCollections.observableArrayList();
						temppatients.addAll(wrapper2.getPatients());

						System.out.println("Loading Patient Data From File Clash");

						// System.out.println("Clash Loaded");

						String[] deletedstr = appPrefs.getDeletedPeople().split(";"); // Returns string array of deleted

						for (Patient p : temppatients) { // For all Clash Accounts
							boolean match = false;

							for (Patient comparison : patientdata) { // For all Loaded Accounts

								if (comparison.getFirstName().equals(p.getFirstName()) && comparison.getLastName().equals(p.getLastName()) && comparison.getStreet().equals(p.getStreet())) { // Checks
									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// System.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getPatientNumber()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer personnum = mainApp.appPrefs.getPersonID() + 1;
									p.setPatientNumber(personnum);
									mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

									patientdata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// System.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePatientDataToFile(getFilePath(), false);
						System.out.println("Loading Patient Data From File 5");
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				System.out.println("Loading Patient Data From File caught error");
				System.out.print(e.getMessage());
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadPatientV2DataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadPatientV2DataFromFile(File file) { // Loads old patient version data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File patientsfile = new File(file.getAbsolutePath() + "/Patients.xml");

		if (!patientsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + patientsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {

			try {
				JAXBContext context = JAXBContext.newInstance(PatientV1ListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				PatientV2ListWrapper wrapper = (PatientV2ListWrapper) um.unmarshal(patientsfile);

				patientdata.clear();
				ObservableList<Patient_v2> oldpatients = FXCollections.observableArrayList();
				oldpatients.addAll(wrapper.getPatients());

				for (Patient_v2 patient : oldpatients) {
					Patient newpatient = new Patient();

					if (patient.getBirthday() != null) {
						newpatient.setBirthday(patient.getBirthday());
					}

					if (patient.getCarerFirstName() != null) {
						newpatient.setCarerFirstName(patient.getCarerFirstName());
					}

					if (patient.getCarerLastName() != null) {
						newpatient.setCarerLastName(patient.getCarerLastName());
					}

					if (patient.getCity() != null) {
						newpatient.setCity(patient.getCity());
					}

					if (patient.getDiagnosis() != null) {
						newpatient.setDiagnosis(patient.getDiagnosis());
					}

					if (patient.getEmails() != null) {
						newpatient.setEmails(patient.getEmails());
					}

					if (patient.getNumbers() != null) {
						newpatient.setNumbers(patient.getNumbers());
					}

					if (patient.getPatientNumber() != null) {
						newpatient.setPatientNumber(patient.getPatientNumber());
					}

					if (patient.getPostalCode() != null) {
						newpatient.setPostalCode(patient.getPostalCode());
					}

					if (patient.getStreet() != null) {
						newpatient.setStreet(patient.getStreet());
					}

					if (patient.getContacts() != null) {
						newpatient.setContacts(patient.getContacts());
					}
				}

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					// system.out.println("Checking File: " + checkfile.getName());
					if (checkfile.getName().contains("Patients") && !checkfile.getName().equals("Patients.xml")) {

						String toastMsg = "Resolving Clash: Patients";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						// system.out.println("File Found");
						JAXBContext context2 = JAXBContext.newInstance(PatientListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						PatientListWrapper wrapper2 = (PatientListWrapper) um2.unmarshal(checkfile);

						ObservableList<Patient> temppatients = FXCollections.observableArrayList();
						temppatients.addAll(wrapper2.getPatients());

						// system.out.println("Clash Loaded");

						String[] deletedstr = appPrefs.getDeletedPeople().split(";"); // Returns string array of deleted

						for (Patient p : temppatients) { // For all Clash Accounts
							boolean match = false;

							for (Patient comparison : patientdata) { // For all Loaded Accounts

								if (comparison.getFirstName().equals(p.getFirstName()) && comparison.getLastName().equals(p.getLastName()) && comparison.getStreet().equals(p.getStreet())) { // Checks
									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getPatientNumber()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer personnum = mainApp.appPrefs.getPersonID() + 1;
									p.setPatientNumber(personnum);
									mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

									patientdata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePatientDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadPatientV1DataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadPatientV1DataFromFile(File file) { // Loads old patient version data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File patientsfile = new File(file.getAbsolutePath() + "/Patients.xml");

		if (!patientsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + patientsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {

			try {
				JAXBContext context = JAXBContext.newInstance(PatientV1ListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				PatientV1ListWrapper wrapper = (PatientV1ListWrapper) um.unmarshal(patientsfile);

				patientdata.clear();
				ObservableList<Patient_v1> oldpatients = FXCollections.observableArrayList();
				oldpatients.addAll(wrapper.getPatients());

				for (Patient_v1 patient : oldpatients) {
					Patient newpatient = new Patient();

					if (patient.getBirthday() != null) {
						newpatient.setBirthday(patient.getBirthday());
					}

					if (patient.getCarerFirstName() != null) {
						newpatient.setCarerFirstName(patient.getCarerFirstName());
					}

					if (patient.getCarerLastName() != null) {
						newpatient.setCarerLastName(patient.getCarerLastName());
					}

					if (patient.getCity() != null) {
						newpatient.setCity(patient.getCity());
					}

					if (patient.getDiagnosis() != null) {
						newpatient.setDiagnosis(patient.getDiagnosis());
					}

					if (patient.getEmails() != null) {
						newpatient.setEmails(patient.getEmails());
					}

					if (patient.getNumbers() != null) {
						newpatient.setNumbers(patient.getNumbers());
					}

					if (patient.getPatientNumber() != null) {
						newpatient.setPatientNumber(patient.getPatientNumber());
					}

					if (patient.getPostalCode() != null) {
						newpatient.setPostalCode(patient.getPostalCode());
					}

					if (patient.getStreet() != null) {
						newpatient.setStreet(patient.getStreet());
					}
				}

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					// system.out.println("Checking File: " + checkfile.getName());
					if (checkfile.getName().contains("Patients") && !checkfile.getName().equals("Patients.xml")) {

						String toastMsg = "Resolving Clash: Patients";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						// system.out.println("File Found");
						JAXBContext context2 = JAXBContext.newInstance(PatientListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						PatientListWrapper wrapper2 = (PatientListWrapper) um2.unmarshal(checkfile);

						ObservableList<Patient> temppatients = FXCollections.observableArrayList();
						temppatients.addAll(wrapper2.getPatients());

						// system.out.println("Clash Loaded");

						String[] deletedstr = appPrefs.getDeletedPeople().split(";"); // Returns string array of deleted

						for (Patient p : temppatients) { // For all Clash Accounts
							boolean match = false;

							for (Patient comparison : patientdata) { // For all Loaded Accounts

								if (comparison.getFirstName().equals(p.getFirstName()) && comparison.getLastName().equals(p.getLastName()) && comparison.getStreet().equals(p.getStreet())) { // Checks
									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getPatientNumber()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer personnum = mainApp.appPrefs.getPersonID() + 1;
									p.setPatientNumber(personnum);
									mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

									patientdata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePatientDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadPatientDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadPeopleDataFromFile(File file) { // Loads person data from specified filepath
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File peoplefile = new File(file.getAbsolutePath() + "/People.xml");

		if (!peoplefile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + peoplefile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(peoplefile);

				persondata.clear();
				persondata.addAll(wrapper.getPeople());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("People") && !checkfile.getName().equals("People.xml")) {

						String toastMsg = "Resolving Clash: People";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(PersonListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						PersonListWrapper wrapper2 = (PersonListWrapper) um2.unmarshal(checkfile);

						ObservableList<Person> temppeople = FXCollections.observableArrayList();
						temppeople.addAll(wrapper2.getPeople());

						String[] deletedstr = appPrefs.getDeletedPeople().split(";"); // Returns string array of deleted

						for (Person p : temppeople) { // For all Clash Accounts
							boolean match = false;

							for (Person comparison : persondata) { // For all Loaded Accounts

								if (comparison.getFirstName().equals(p.getFirstName()) && comparison.getLastName().equals(p.getLastName())) { // Checks if clashed
																																				// account is present in
																																				// loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer personnum = mainApp.appPrefs.getPersonID() + 1;
									p.setId(personnum);
									mainApp.appPrefs.setPersonID(mainApp.appPrefs.getPersonID() + 1);

									persondata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePersonDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadPeopleDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadPersonTypeDataFromFile(File file) { // Loads person type data from specified filepath
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		ObservableList<PersonType> persontypelist = FXCollections.observableArrayList(persontypes); // Creates duplicate
																									// of pre-load data
		File peoplefile = new File(file.getAbsolutePath() + "/PersonTypes.xml");

		if (!peoplefile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + peoplefile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(PersonTypeWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				PersonTypeWrapper wrapper = (PersonTypeWrapper) um.unmarshal(peoplefile);

				persontypes.clear();
				persontypes.addAll(wrapper.getPersonTypes());

				if (persontypelist.size() > 0) {

					// CLASH CHECK ------------------------------------------

					File folder = getFilePath();
					File[] listOfFiles = folder.listFiles();

					boolean clash = false;

					for (File checkfile : listOfFiles) {
						if (checkfile.getName().contains("PersonTypes") && !checkfile.getName().equals("PersonTypes.xml")) {

							String toastMsg = "Resolving Clash: Person Types";
							int toastMsgTime = 3500; // 3.5 seconds
							int fadeInTime = 500; // 0.5 seconds
							int fadeOutTime = 500; // 0.5 seconds
							Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

							clash = true;
							JAXBContext context2 = JAXBContext.newInstance(PersonTypeWrapper.class);
							Unmarshaller um2 = context2.createUnmarshaller();

							// Reading XML from the file and unmarshalling.
							PersonTypeWrapper wrapper2 = (PersonTypeWrapper) um2.unmarshal(checkfile);

							ObservableList<PersonType> temptypes = FXCollections.observableArrayList();
							temptypes.addAll(wrapper2.getPersonTypes());

							for (PersonType p : temptypes) { // For all Clash Accounts
								boolean match = false;

								for (PersonType comparison : persontypes) { // For all Loaded Accounts

									if (comparison.getTypeName().equals(p.getTypeName())) { // Checks if clashed account
																							// is present in loaded

										match = true;
										break;
									}
								}

								if (!match) { // If not, check if that's because they've been deleted
									// system.out.println("Match Not Found");

									persontypes.add(p); // ADD THEM BACK IN

								}
							}
							// system.out.println("Clash File Being Deleted");
							checkfile.delete();
						}
						if (clash) {
							savePersonTypeDataToFile(getFilePath(), false);
						}
					}

					// CLASH CHECK ------------------------------------------

				}

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadPersonTypeDataFromFile(getFilePath());
					}
				});
			}
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	public boolean LoadNoteTypeDataFromFile(File file) { // Loads person type data from specified filepath
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		ObservableList<NoteType> notetypelist = FXCollections.observableArrayList(notetypes); // Creates duplicate of
																								// pre-load data
		File notetypesfile = new File(file.getAbsolutePath() + "/NoteTypes.xml");

		if (!notetypesfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + notetypesfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(NoteTypeWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				NoteTypeWrapper wrapper = (NoteTypeWrapper) um.unmarshal(notetypesfile);

				notetypes.clear();
				notetypes.addAll(wrapper.getNoteTypes());

				if (notetypelist.size() > 0) {

					// CLASH CHECK ------------------------------------------

					File folder = getFilePath();
					File[] listOfFiles = folder.listFiles();

					boolean clash = false;

					for (File checkfile : listOfFiles) {
						if (checkfile.getName().contains("NoteTypes") && !checkfile.getName().equals("NoteTypes.xml")) {

							String toastMsg = "Resolving Clash: Note Types";
							int toastMsgTime = 3500; // 3.5 seconds
							int fadeInTime = 500; // 0.5 seconds
							int fadeOutTime = 500; // 0.5 seconds
							Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

							clash = true;
							JAXBContext context2 = JAXBContext.newInstance(NoteTypeWrapper.class);
							Unmarshaller um2 = context2.createUnmarshaller();

							// Reading XML from the file and unmarshalling.
							NoteTypeWrapper wrapper2 = (NoteTypeWrapper) um2.unmarshal(checkfile);

							ObservableList<NoteType> temptypes = FXCollections.observableArrayList();
							temptypes.addAll(wrapper2.getNoteTypes());

							for (NoteType p : temptypes) { // For all Clash Accounts
								boolean match = false;

								for (NoteType comparison : notetypes) { // For all Loaded Accounts

									if (comparison.getTypeName().equals(p.getTypeName())) { // Checks if clashed
																							// notetype is present in
																							// loaded

										match = true;
										break;
									}
								}

								if (!match) { // If not present

									notetypes.add(p); // ADD THEM BACK IN

								}
							}
							// system.out.println("Clash File Being Deleted");
							checkfile.delete();
						}
						if (clash) {
							saveNoteTypeDataToFile(getFilePath(), false);
						}
					}

					// CLASH CHECK ------------------------------------------

				}

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadNoteTypeDataFromFile(getFilePath());
					}
				});
			}
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadAppointmentDataFromFile(File file) { // Loads appointment data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File appointmentsfile = new File(file.getAbsolutePath() + "/Appointments.xml");
		if (!appointmentsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + appointmentsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(AppointmentWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				AppointmentWrapper wrapper = (AppointmentWrapper) um.unmarshal(appointmentsfile);

				appointments.clear();
				appointments.addAll(wrapper.getAppointments());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Appointments") && !checkfile.getName().equals("Appointments.xml")) {

						String toastMsg = "Resolving Clash: Appointments";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(AppointmentWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						AppointmentWrapper wrapper2 = (AppointmentWrapper) um2.unmarshal(checkfile);

						ObservableList<Appointment> tempappointments = FXCollections.observableArrayList();
						tempappointments.addAll(wrapper2.getAppointments());

						String[] deletedstr = appPrefs.getDeletedAppointments().split(";"); // Returns string array of
																							// deleted

						for (Appointment p : tempappointments) { // For all Clash Accounts
							boolean match = false;

							for (Appointment comparison : appointments) { // For all Loaded Accounts

								if (comparison.getPatient().equals(p.getPatient()) && comparison.getDate().equals(p.getDate()) && comparison.getTime().equals(p.getTime())) { // Checks if clashed
																																												// account is present in
																																												// loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getID()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer appnum = mainApp.appPrefs.getAppointmentID() + 1;
									p.setID(appnum);
									mainApp.appPrefs.setAppointmentID(appnum + 1);

									appointments.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveAppointmentsDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				e.printStackTrace();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadAppointmentV1DataFromFile(getFilePath());
					}
				});
			}
		}
		ObservableList<Appointment> archiveapps = FXCollections.observableArrayList();
		appointments.stream().filter((appointment) -> (appointment.getDate().isBefore(LocalDate.now().minusMonths(6)))).forEachOrdered((appointment) -> { // If appointment is over 6 months gone, it
																																							// will archive.
			archiveapps.add(appointment);
		});
		for (Appointment appointment : archiveapps) {
			ArchiveAppointment(appointment);

		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	public boolean LoadAppointmentV1DataFromFile(File file) { // Loads old appointment version data from specified
																// filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File appointmentsfile = new File(file.getAbsolutePath() + "/Appointments.xml");
		if (!appointmentsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + appointmentsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(AppointmentV1Wrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				AppointmentV1Wrapper wrapper = (AppointmentV1Wrapper) um.unmarshal(appointmentsfile);

				ObservableList<Appointment_v1> oldappointments = FXCollections.observableArrayList();

				oldappointments.addAll(wrapper.getAppointments());

				appointments.clear();

				for (Appointment_v1 appointment : oldappointments) {
					Appointment newapp = new Appointment(appointment.getPatient().toString(), appointment.getStaffMember().toString(), appointment.getDate(), appointment.getTime(),
							appointment.getRoom(), appointment.getType(), appointment.getDescription(), appointment.getDuration(), appointment.getAppType(), appointment.getID(),
							appointment.getPrice(), appointment.getCurrency());

					newapp.setPaid(appointment.getPaid());
					newapp.setReminded(appointment.getReminded());

					appointments.add(newapp);
				}

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Appointments") && !checkfile.getName().equals("Appointments.xml")) {

						String toastMsg = "Resolving Clash: Appointments";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(AppointmentWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						AppointmentWrapper wrapper2 = (AppointmentWrapper) um2.unmarshal(checkfile);

						ObservableList<Appointment> tempappointments = FXCollections.observableArrayList();
						tempappointments.addAll(wrapper2.getAppointments());

						String[] deletedstr = appPrefs.getDeletedAppointments().split(";"); // Returns string array of
																							// deleted

						for (Appointment p : tempappointments) { // For all Clash Accounts
							boolean match = false;

							for (Appointment comparison : appointments) { // For all Loaded Accounts

								if (comparison.getPatient().equals(p.getPatient()) && comparison.getDate().equals(p.getDate()) && comparison.getTime().equals(p.getTime())) { // Checks if clashed
																																												// account is present in
																																												// loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getID()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer appnum = mainApp.appPrefs.getAppointmentID() + 1;
									p.setID(appnum);
									mainApp.appPrefs.setPersonID(appnum + 1);

									appointments.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveAppointmentsDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				e.printStackTrace();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
					}
				});
			}
		}
		ObservableList<Appointment> archiveapps = FXCollections.observableArrayList();
		appointments.stream().filter((appointment) -> (appointment.getDate().isBefore(LocalDate.now().minusDays(14)))).forEachOrdered((appointment) -> {
			archiveapps.add(appointment);
		});
		for (Appointment appointment : archiveapps) {
			ArchiveAppointment(appointment);

		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadAppointmentTypeDataFromFile(File file) { // Loads appointment type data from specified filepath
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File appointmenttypesfile = new File(file.getAbsolutePath() + "/AppointmentTypes.xml");
		if (!appointmenttypesfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + appointmenttypesfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(AppointmentTypeWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				AppointmentTypeWrapper wrapper = (AppointmentTypeWrapper) um.unmarshal(appointmenttypesfile);

				appointmenttypes.clear();
				appointmenttypes.addAll(wrapper.getAppointments());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("AppointmentTypes") && !checkfile.getName().equals("AppointmentTypes.xml")) {

						String toastMsg = "Resolving Clash: Appointment Types";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(AppointmentTypeWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						AppointmentTypeWrapper wrapper2 = (AppointmentTypeWrapper) um2.unmarshal(checkfile);

						ObservableList<AppointmentType> tempappointments = FXCollections.observableArrayList();
						tempappointments.addAll(wrapper2.getAppointments());

						String[] deletedstr = appPrefs.getDeletedAppointmentTypes().split(";"); // Returns string array
																								// of deleted

						for (AppointmentType p : tempappointments) { // For all Clash Accounts
							boolean match = false;

							for (AppointmentType comparison : appointmenttypes) { // For all Loaded Accounts

								if (comparison.getName().equals(p.getName()) && comparison.getPrice().equals(p.getPrice())) { // Checks if clashed account is
																																// present in loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer appointmenttypeId = mainApp.appPrefs.getAppTypeID() + 1;
									p.setId(appointmenttypeId);
									mainApp.appPrefs.setAppTypeID(mainApp.appPrefs.getAppTypeID() + 1);

									appointmenttypes.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveAppointmentTypeDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadAppointmentTypeDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		// The below code checks if the 'meetings' appointment type has been added, and
		// if not, adds it. It also checks if more than one meetings type has
		// accidentally been created, and deletes the
		// latter.
		boolean meeting = false;
		int meetingcounter = 0;
		ObservableList<AppointmentType> todelete = FXCollections.observableArrayList();
		for (AppointmentType apptype : appointmenttypes) {
			// system.out.println("App type name: " + apptype.getName() + " ,App type ID: "
			// + String.valueOf(apptype.getId()));
			if (apptype.getId().equals(9999)) {
				meetingcounter += 1;
				if (meetingcounter > 1) {
					todelete.add(apptype);
				}
				meeting = true;
				break;
			}
		}
		appointmenttypes.removeAll(todelete);

		if (!prefs.getBoolean("meetingtypeadded", false) && !meeting) {
			// system.out.println("Meeting Type Not Added");

			AppointmentType meetingtype = new AppointmentType();

			meetingtype.setName("Meeting");
			meetingtype.setPrice(0.00);
			meetingtype.setDescription("Staff Meeting");
			meetingtype.setCurrency("");
			meetingtype.setDuration(60);

			meetingtype.setId(9999);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					mainApp.actuallyAddAppointmentType(meetingtype, false);
					prefs.putBoolean("meetingtypeadded", true);
				}

			});

		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadBuildingDataFromFile(File file) { // Loads building data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		ObservableList<Building> buildinglist = FXCollections.observableArrayList(buildingsdata); // Creates
																									// duplicate
																									// of pre-load
																									// data
		File buildingsfile = new File(file.getAbsolutePath() + "/Buildings.xml");

		if (!buildingsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + buildingsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();

		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(BuildingWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				BuildingWrapper wrapper = (BuildingWrapper) um.unmarshal(buildingsfile);

				buildingsdata.clear();
				buildingsdata.addAll(wrapper.getBuildings());

				if (buildinglist.size() > 0) {

					// CLASH CHECK ------------------------------------------

					File folder = getFilePath();
					File[] listOfFiles = folder.listFiles();

					boolean clash = false;

					for (File checkfile : listOfFiles) {
						if (checkfile.getName().contains("Buildings") && !checkfile.getName().equals("Buildings.xml")) {

							String toastMsg = "Resolving Clash: Buildings";
							int toastMsgTime = 3500; // 3.5 seconds
							int fadeInTime = 500; // 0.5 seconds
							int fadeOutTime = 500; // 0.5 seconds
							Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

							clash = true;
							JAXBContext context2 = JAXBContext.newInstance(BuildingWrapper.class);
							Unmarshaller um2 = context2.createUnmarshaller();

							// Reading XML from the file and unmarshalling.
							BuildingWrapper wrapper2 = (BuildingWrapper) um2.unmarshal(checkfile);

							ObservableList<Building> tempbuildings = FXCollections.observableArrayList();
							tempbuildings.addAll(wrapper2.getBuildings());

							for (Building p : tempbuildings) { // For all Clash Accounts
								boolean match = false;

								for (Building comparison : buildingsdata) { // For all Loaded Accounts

									if (comparison.getName().equals(p.getName()) && comparison.getAddress().equals(p.getAddress())) { // Checks if clashed
																																		// account is
																																		// present in loaded

										match = true;
										break;
									}
								}

								if (!match) {
									buildingsdata.add(p); // ADD THEM BACK IN
								}
							}
							// system.out.println("Clash File Being Deleted");
							checkfile.delete();
						}
						if (clash) {
							saveBuildingDataToFile(getFilePath(), false);
						}
					}

					// CLASH CHECK ------------------------------------------

				}

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadBuildingDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadRoomDataFromFile(File file) { // Loads room data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File roomsfile = new File(file.getAbsolutePath() + "/Rooms.xml");

		if (!roomsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + roomsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(RoomWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				RoomWrapper wrapper = (RoomWrapper) um.unmarshal(roomsfile);

				roomdata.clear();
				roomdata.addAll(wrapper.getRooms());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Rooms") && !checkfile.getName().equals("Rooms.xml")) {

						String toastMsg = "Resolving Clash: Rooms";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(RoomWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						RoomWrapper wrapper2 = (RoomWrapper) um2.unmarshal(checkfile);

						ObservableList<Room> temprooms = FXCollections.observableArrayList();
						temprooms.addAll(wrapper2.getRooms());

						String[] deletedstr = appPrefs.getDeletedRooms().split(";"); // Returns string array of deleted

						for (Room p : temprooms) { // For all Clash Accounts
							boolean match = false;

							for (Room comparison : roomdata) { // For all Loaded Accounts

								if (comparison.getRoomName().equals(p.getRoomName()) && comparison.getBuilding().equals(p.getBuilding())) { // Checks if clashed room
																																			// is present in loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer roomid = mainApp.appPrefs.getRoomID() + 1;
									p.setId(roomid);
									mainApp.appPrefs.setRoomID(roomid + 1);

									roomdata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveRoomDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadRoomDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadMailingListDataFromFile(File file) { // Loads mailing list data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File mailinglistsfile = new File(file.getAbsolutePath() + "/MailingLists.xml");
		if (!mailinglistsfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + mailinglistsfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(MailingListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				MailingListWrapper wrapper = (MailingListWrapper) um.unmarshal(mailinglistsfile);

				mailinglists.clear();
				mailinglists.addAll(wrapper.getMailingList());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("MailingLists") && !checkfile.getName().equals("MailingLists.xml")) {

						String toastMsg = "Resolving Clash: Mailing Lists";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(MailingListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						MailingListWrapper wrapper2 = (MailingListWrapper) um2.unmarshal(checkfile);

						ObservableList<MailingList> tempmail = FXCollections.observableArrayList();
						tempmail.addAll(wrapper2.getMailingList());

						String[] deletedstr = appPrefs.getDeletedMail().split(";"); // Returns string array of deleted

						for (MailingList p : tempmail) { // For all Clash Accounts
							boolean match = false;

							for (MailingList comparison : mailinglists) { // For all Loaded Accounts

								if (comparison.getListName().equals(p.getListName()) && comparison.getDescription().equals(p.getDescription())) { // Checks if
																																					// clashed room
																																					// is present in
																																					// loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer mid = mainApp.appPrefs.getMailID() + 1;
									p.setId(mid);
									mainApp.appPrefs.setMailID(mid + 1);

									mailinglists.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveMailingListDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadMailingListDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadNoteDataFromFile(File file) { // Loads note data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File notesfile = new File(file.getAbsolutePath() + "/Notes.xml");
		if (!notesfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + notesfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(NoteListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				NoteListWrapper wrapper = (NoteListWrapper) um.unmarshal(notesfile);

				notedata.clear();
				notedata.addAll(wrapper.getNotes());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Notes") && !checkfile.getName().equals("Notes.xml")) {

						String toastMsg = "Resolving Clash: Notes";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(NoteListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						NoteListWrapper wrapper2 = (NoteListWrapper) um2.unmarshal(checkfile);

						ObservableList<Note> tempnotes = FXCollections.observableArrayList();
						tempnotes.addAll(wrapper2.getNotes());

						String[] deletedstr = appPrefs.getDeletedNotes().split(";"); // Returns string array of deleted

						for (Note p : tempnotes) { // For all Notes in loading file
							boolean match = false;

							for (Note comparison : notedata) { // For all Loaded Notes

								if (comparison.getPatient().equals(p.getPatient()) && comparison.getText().equals(p.getText())) { // Checks if clashed room is
																																	// present in loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer nid = mainApp.appPrefs.getNoteID() + 1;
									p.setId(nid);
									mainApp.appPrefs.setNoteID(nid + 1);

									notedata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveNotesDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadNoteV1DataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public boolean LoadNoteV1DataFromFile(File file) { // Loads note data from specified filepath

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		boolean same = true; // initialises change variable

		File notesfile = new File(file.getAbsolutePath() + "/Notes.xml");
		if (!notesfile.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + notesfile.getPath() + " does not exist: filepath is incorrect.");
			alert.showAndWait();
		}

		else {
			try {
				JAXBContext context = JAXBContext.newInstance(NoteListWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				NoteListWrapperv1 wrapper = (NoteListWrapperv1) um.unmarshal(notesfile);

				notedata.clear();

				for (Note_v1 note : wrapper.getNotes()) {
					Patient notepatient = null;
					for (Patient checkpatient : patientdata) {
						if (checkpatient.getPatientNumber().equals(note.getPatient())) {
							notepatient = checkpatient;
						}
					}
					if (!notepatient.equals(null)) {
						Note newnote = new Note(notepatient, note.getStaffCode(), note.getDate(), note.getTime(), note.getText(), 0);
						notedata.add(newnote);
					}
				}

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Notes") && !checkfile.getName().equals("Notes.xml")) {

						String toastMsg = "Resolving Clash: Notes";
						int toastMsgTime = 3500; // 3.5 seconds
						int fadeInTime = 500; // 0.5 seconds
						int fadeOutTime = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(NoteListWrapper.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						NoteListWrapper wrapper2 = (NoteListWrapper) um2.unmarshal(checkfile);

						ObservableList<Note> tempnotes = FXCollections.observableArrayList();
						tempnotes.addAll(wrapper2.getNotes());

						String[] deletedstr = appPrefs.getDeletedNotes().split(";"); // Returns string array of deleted

						for (Note p : tempnotes) { // For all Clash Accounts
							boolean match = false;

							for (Note comparison : notedata) { // For all Loaded Accounts

								if (comparison.getPatient().equals(p.getPatient()) && comparison.getText().equals(p.getText())) { // Checks if clashed room is
																																	// present in loaded

									match = true;
									break;
								}
							}

							if (!match) { // If not, check if that's because they've been deleted
								// system.out.println("Match Not Found");
								boolean matchdel = false;
								for (String str : deletedstr) {
									if (!str.isEmpty()) {
										if (Integer.valueOf(str) == p.getId()) {
											matchdel = true;
										}
									}
								}
								if (!matchdel) { // If not, adjust their personID, and add them in

									Integer nid = mainApp.appPrefs.getNoteID() + 1;
									p.setId(nid);
									mainApp.appPrefs.setNoteID(nid + 1);

									notedata.add(p); // ADD THEM BACK IN
								}
							}
						}
						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						saveNotesDataToFile(getFilePath(), false);
					}
				}

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LoadNoteDataFromFile(getFilePath());
					}
				});
			}
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

		return same;
	}

	public boolean LoadPreferenceDataFromBackup(File file) {
		boolean loaded = true;

		File prefsfile = new File(file.getAbsolutePath() + "/Backup/Preferences.xml");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		if (!prefsfile.exists()) {

			System.out.println("Prefsfile doesnt exist");

			Alert alert = new Alert(AlertType.ERROR);
			System.out.println("Alert initialised");

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			System.out.println("Stage initialised");

			stage.setAlwaysOnTop(true);
			System.out.println("Stage moved to top");

			DialogPane dialogPane = alert.getDialogPane();

			dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());

			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + prefsfile.getPath() + " does not exist: filepath is incorrect. - Is filesharing network avaliable? - Retry, Close Application or choose new filepath.");

			ButtonType buttonTypeTwo = new ButtonType("Retry");
			ButtonType buttonTypeOne = new ButtonType("Choose Different FilePath");
			ButtonType buttonTypeCancel = new ButtonType("Close Application");

			alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				File newfile = chooseDirectory(primaryStage);
				if (newfile != null) {
					prefs.put("filePath", newfile.getAbsolutePath());
				}
			} else if (result.get() == buttonTypeTwo) {
				LoadPreferenceDataFromFile(getFilePath());
			} else {
				Platform.exit();
			}

			loaded = false;

		} else {
			try {

				JAXBContext context = JAXBContext.newInstance(Prefs.class);
				Unmarshaller um = context.createUnmarshaller();

				System.out.println("Unmarshalling");

				// Reading XML from the file and unmarshalling.
				appPrefs = (Prefs) um.unmarshal(prefsfile);

				System.out.println("Unmarshalled");
				System.out.println("Key One: " + appPrefs.getKeyOne());
				System.out.println("File Path: " + appPrefs.getFilePath());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				String delapps[] = appPrefs.getDeletedAppointments().split(";");
				String delapptypes[] = appPrefs.getDeletedAppointmentTypes().split(";");
				String delmail[] = appPrefs.getDeletedMail().split(";");
				String delnotes[] = appPrefs.getDeletedNotes().split(";");
				String delpeople[] = appPrefs.getDeletedPeople().split(";");
				String delrooms[] = appPrefs.getDeletedRooms().split(";");

				for (File checkfile : listOfFiles) {

					if ((checkfile.getName().contains("Preferences")) && (!checkfile.getName().trim().equals("Preferences.xml"))) {

						String toastMsg2 = "Resolving Clash: Preferences";
						int toastMsgTime2 = 3500; // 3.5 seconds
						int fadeInTime2 = 500; // 0.5 seconds
						int fadeOutTime2 = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg2, toastMsgTime2, fadeInTime2, fadeOutTime2, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(Prefs.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						Prefs tempprefs = (Prefs) um2.unmarshal(checkfile);

						if (tempprefs.getAccountsSaved()) {
							appPrefs.setAccountsSaved(true);
						}
						if (tempprefs.getAppointmentsSaved()) {
							appPrefs.setAppointmentsSaved(true);
						}
						if (tempprefs.getAppointmentTypesSaved()) {
							appPrefs.setAppointmentTypesSaved(true);
						}
						if (tempprefs.getPatientsSaved()) {
							appPrefs.setPatientsSaved(true);
						}
						if (tempprefs.getRoomsSaved()) {
							appPrefs.setRoomsSaved(true);
						}
						if (tempprefs.getBuildingsSaved()) {
							appPrefs.setBuildingsSaved(true);
						}
						if (tempprefs.getMailingListsSaved()) {
							appPrefs.setMailingListsSaved(true);
						}
						if (tempprefs.getPeopleSaved()) {
							appPrefs.setPeopleSaved(true);
						}
						if (tempprefs.getTypesSaved()) {
							appPrefs.setTypesSaved(true);
						}
						if (tempprefs.getAppointmentID() > appPrefs.getAppointmentID()) {
							appPrefs.setAppointmentID(tempprefs.getAppointmentID());
						}
						if (tempprefs.getPersonID() > appPrefs.getPersonID()) {
							appPrefs.setPersonID(tempprefs.getPersonID());
						}
						if (tempprefs.getRoomID() > appPrefs.getRoomID()) {
							appPrefs.setRoomID(tempprefs.getRoomID());
						}
						if (tempprefs.getMailID() > appPrefs.getMailID()) {
							appPrefs.setMailID(tempprefs.getMailID());
						}
						if (tempprefs.getNoteID() > appPrefs.getNoteID()) {
							appPrefs.setNoteID(tempprefs.getNoteID());
						}
						if (tempprefs.getAppTypeID() > appPrefs.getAppTypeID()) {
							appPrefs.setAppTypeID(tempprefs.getAppTypeID());
						}

						// Lists deleted item ID's of clashing preferences file in string arrays
						String tempdelapps[] = tempprefs.getDeletedAppointments().split(";");
						String tempdelapptypes[] = tempprefs.getDeletedAppointmentTypes().split(";");
						String tempdelmail[] = tempprefs.getDeletedMail().split(";");
						String tempdelnotes[] = tempprefs.getDeletedNotes().split(";");
						String tempdelpeople[] = tempprefs.getDeletedPeople().split(";");
						String tempdelrooms[] = tempprefs.getDeletedRooms().split(";");

						// Deleted Appointments Merging
						String todelete = ""; // String containing deleted item ID's that are not in the original Prefs file.
						for (String app : tempdelapps) {
							if (!Arrays.asList(delapps).contains(app)) {
								todelete += ";" + app;
							}
						}
						appPrefs.setDeletedAppointments(appPrefs.getDeletedAppointments() + todelete);

						// Deleted Appointment Types Merging
						todelete = "";
						for (String apptype : tempdelapptypes) {
							if (!Arrays.asList(delapptypes).contains(apptype)) {
								todelete += ";" + apptype;
							}
						}
						appPrefs.setDeletedAppointmentTypes(appPrefs.getDeletedAppointmentTypes() + todelete);

						// Deleted Mail Types Merging
						todelete = "";
						for (String mail : tempdelmail) {
							if (!Arrays.asList(delmail).contains(mail)) {
								todelete += ";" + mail;
							}
						}
						appPrefs.setDeletedMail(appPrefs.getDeletedMail() + todelete);

						// Deleted Notes Merging
						todelete = "";
						for (String note : tempdelnotes) {
							if (!Arrays.asList(delnotes).contains(note)) {
								todelete += ";" + note;
							}
						}
						appPrefs.setDeletedNotes(appPrefs.getDeletedNotes() + todelete);

						// Deleted People Merging
						todelete = "";
						for (String person : tempdelpeople) {
							if (!Arrays.asList(delpeople).contains(person)) {
								todelete += ";" + person;
							}
						}
						appPrefs.setDeletedPeople(appPrefs.getDeletedPeople() + todelete);

						// Deleted Rooms Merging
						todelete = "";
						for (String room : tempdelrooms) {
							if (!Arrays.asList(delrooms).contains(room)) {
								todelete += ";" + room;
							}
						}
						appPrefs.setDeletedRooms(appPrefs.getDeletedRooms() + todelete);

						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePreferenceDataToFile(getFilePath());
					}
				}

				prefs.put("KeyOne", appPrefs.getKeyOne());
				prefs.put("KeyTwo", appPrefs.getKeyTwo());
				prefs.put("KeyThree", appPrefs.getKeyThree());
				prefs.put("KeyFour", appPrefs.getKeyFour());
				prefs.put("KeyFive", appPrefs.getKeyFive());

				loaded = true;

				savePreferenceDataToFile(getFilePath());

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				loaded = false;
			}
		}

		return loaded;
	}

	/**
	 *
	 * @param file
	 */
	public void LoadPreferenceDataFromFile(File file) { // Loads preference file

		File prefsfile = new File(file.getAbsolutePath() + "/Preferences.xml");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

		if (!prefsfile.exists()) {

			System.out.println("Prefsfile doesnt exist");

			Alert alert = new Alert(AlertType.ERROR);
			System.out.println("Alert initialised");

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			System.out.println("Stage initialised");

			stage.setAlwaysOnTop(true);
			System.out.println("Stage moved to top");

			DialogPane dialogPane = alert.getDialogPane();

			dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());

			dialogPane.getStyleClass().add(".dialog-pane");

			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("File:\n" + prefsfile.getPath() + " does not exist: filepath is incorrect. - Is filesharing network avaliable? - Retry, Close Application or choose new filepath.");

			ButtonType buttonTypeTwo = new ButtonType("Retry");
			ButtonType buttonTypeOne = new ButtonType("Choose Different FilePath");
			ButtonType buttonTypeCancel = new ButtonType("Close Application");

			alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				File newfile = chooseDirectory(primaryStage);
				if (newfile != null) {
					prefs.put("filePath", newfile.getAbsolutePath());
				}
			} else if (result.get() == buttonTypeTwo) {
				LoadPreferenceDataFromFile(getFilePath());
			} else {
				Platform.exit();
			}

		}

		else {
			try {

				JAXBContext context = JAXBContext.newInstance(Prefs.class);
				Unmarshaller um = context.createUnmarshaller();

				System.out.println("Unmarshalling");

				// Reading XML from the file and unmarshalling.
				appPrefs = (Prefs) um.unmarshal(prefsfile);

				if (appPrefs.getKeyOne().equals(null)) {
					System.out.println("Key one corrupted. Loading from backup.");
					writeError(new Exception("preferences file corrupted: key one is null."));
				} else if (appPrefs.getKeyOne().trim().equals("")) {
					System.out.println("Key one corrupted. Loading from backup.");
					writeError(new Exception("preferences file corrupted: key one is null."));
				}

				System.out.println("Unmarshalled");
				System.out.println("Key One: " + appPrefs.getKeyOne());
				System.out.println("File Path: " + appPrefs.getFilePath());

				// CLASH CHECK ------------------------------------------

				File folder = getFilePath();
				File[] listOfFiles = folder.listFiles();

				boolean clash = false;

				for (File checkfile : listOfFiles) {
					if (checkfile.getName().contains("Preferences") && !checkfile.getName().equals("Preferences.xml")) {

						String toastMsg2 = "Resolving Clash: Preferences";
						int toastMsgTime2 = 3500; // 3.5 seconds
						int fadeInTime2 = 500; // 0.5 seconds
						int fadeOutTime2 = 500; // 0.5 seconds
						Toast.makeText(primaryStage, toastMsg2, toastMsgTime2, fadeInTime2, fadeOutTime2, 0);

						clash = true;
						JAXBContext context2 = JAXBContext.newInstance(Prefs.class);
						Unmarshaller um2 = context2.createUnmarshaller();

						// Reading XML from the file and unmarshalling.
						Prefs tempprefs = (Prefs) um2.unmarshal(checkfile);

						if (tempprefs.getAccountsSaved()) {
							appPrefs.setAccountsSaved(true);
						}
						if (tempprefs.getAppointmentsSaved()) {
							appPrefs.setAppointmentsSaved(true);
						}
						if (tempprefs.getAppointmentTypesSaved()) {
							appPrefs.setAppointmentTypesSaved(true);
						}
						if (tempprefs.getPatientsSaved()) {
							appPrefs.setPatientsSaved(true);
						}
						if (tempprefs.getRoomsSaved()) {
							appPrefs.setRoomsSaved(true);
						}
						if (tempprefs.getBuildingsSaved()) {
							appPrefs.setBuildingsSaved(true);
						}
						if (tempprefs.getMailingListsSaved()) {
							appPrefs.setMailingListsSaved(true);
						}
						if (tempprefs.getPeopleSaved()) {
							appPrefs.setPeopleSaved(true);
						}
						if (tempprefs.getTypesSaved()) {
							appPrefs.setTypesSaved(true);
						}

						if (tempprefs.getAppointmentID() > appPrefs.getAppointmentID()) {
							appPrefs.setAppointmentID(tempprefs.getAppointmentID());
						}
						if (tempprefs.getPersonID() > appPrefs.getPersonID()) {
							appPrefs.setPersonID(tempprefs.getPersonID());
						}
						if (tempprefs.getRoomID() > appPrefs.getRoomID()) {
							appPrefs.setRoomID(tempprefs.getRoomID());
						}
						if (tempprefs.getMailID() > appPrefs.getMailID()) {
							appPrefs.setMailID(tempprefs.getMailID());
						}
						if (tempprefs.getNoteID() > appPrefs.getNoteID()) {
							appPrefs.setNoteID(tempprefs.getNoteID());
						}
						if (tempprefs.getAppTypeID() > appPrefs.getAppTypeID()) {
							appPrefs.setAppTypeID(tempprefs.getAppTypeID());
						}

						// system.out.println("Clash File Being Deleted");
						checkfile.delete();
					}
					if (clash) {
						savePreferenceDataToFile(getFilePath());
					}
				}

				prefs.put("KeyOne", appPrefs.getKeyOne());
				prefs.put("KeyTwo", appPrefs.getKeyTwo());
				prefs.put("KeyThree", appPrefs.getKeyThree());
				prefs.put("KeyFour", appPrefs.getKeyFour());
				prefs.put("KeyFive", appPrefs.getKeyFive());
				savePreferenceDataToFile(getFilePath());

				// CLASH CHECK ------------------------------------------

			} catch (Exception e) { // catches ANY exception
				writeError(e);
				Alert alert = new Alert(AlertType.ERROR);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);

				alert.setTitle("Error");
				e.printStackTrace();
				alert.setHeaderText("Could not load data");
				alert.setContentText("Could not load data from file:\n" + prefsfile.getPath() + ". Error Message: " + e);

				alert.showAndWait();
			}
		}

	}

	/**
	 *
	 */
	public void setVersion() { // Sets Save Version (DIFFERENT FROM APPLICATION VERSION), which is changed each
								// time data changes
		int currver = version.getVersion();
		int newver = 0;
		boolean searching = true;
		while (searching) {
			newver = ThreadLocalRandom.current().nextInt(0, 100);
			if (newver != currver) {
				searching = false;
			}
		}
		version.setVersion(newver);
		saveVersion(getFilePath());
	}

	/**
	 *
	 * @return
	 */
	public boolean checkVersion() { // Checks if files have changed. If not, returns true.
		int currver = version.getVersion();
		LoadVersion(getFilePath());

		return version.getVersion() == currver;
	}

	/**
	 *
	 * @param file
	 */
	public void LoadVersion(File file) { // Loads current save version (DIFFERENT FROM APPLICATION VERSION)

		File verfile = new File(file.getAbsolutePath() + "/Version.xml");
		try {
			JAXBContext context = JAXBContext.newInstance(Version.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			version = (Version) um.unmarshal(verfile);

			// CLASH CHECK ------------------------------------------

			// Version clash check can lead to a loop because it changes the version each
			// save, creating a clash each time it resolves the clash.

			// File folder = getFilePath();
			// File[] listOfFiles = folder.listFiles();
			//
			// boolean clash = false;
			//
			// for (File checkfile : listOfFiles) {
			// if (checkfile.getName().contains("Version") &&
			// !checkfile.getName().equals("Version.xml")) {
			//
			// String toastMsg = "Resolving Clash: Version";
			// int toastMsgTime = 3500; // 3.5 seconds
			// int fadeInTime = 500; // 0.5 seconds
			// int fadeOutTime = 500; // 0.5 seconds
			// Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime,
			// fadeOutTime);
			//
			// clash = true;
			// JAXBContext context2 = JAXBContext.newInstance(Version.class);
			// Unmarshaller um2 = context2.createUnmarshaller();
			//
			// // Reading XML from the file and unmarshalling.
			// Version tempver = (Version) um2.unmarshal(checkfile);
			//
			// if (tempver.getVersion() > version.getVersion()) {
			// version.setVersion(tempver.getVersion());
			// }
			//
			// System.out.println("Clash File Being Deleted");
			// checkfile.delete();
			// }
			// if (clash) {
			// saveVersion(getFilePath());
			// }
			// }

			// CLASH CHECK ------------------------------------------

		} catch (Exception e) { // catches ANY exception
			writeError(e);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveVersion(File file) { // Saves save version

		LoadPreferenceDataFromFile(getFilePath());

		File verfile = new File(file.getAbsolutePath() + "/Version.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(Version.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(version, verfile);

		} catch (Exception e) { // catches ANY exception
			writeError(e);
		}
	}

	/**
	 *
	 * @param file
	 */
	public void firstSaveVersion(File file) { // Creates the first version if there isn't one yet

		File verfile = new File(file.getAbsolutePath() + "/Version.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(Version.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(version, verfile);

		} catch (Exception e) { // catches ANY exception
			writeError(e);
		}
	}

	/**
	 *
	 * @param file
	 */
	public void savePreferenceDataToFile(File file) { // Saves preferences

		File prefsfile = new File(file.getAbsolutePath() + "/Preferences.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(Prefs.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(appPrefs, prefsfile);

		} catch (Exception e) { // catches ANY exception
			Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText(getFilePath().getAbsolutePath() + " does not exist: filepath is incorrect. - Is filesharing network avaliable? Close Application or choose new filepath.");

			ButtonType buttonTypeOne = new ButtonType("Choose Different FilePath");
			ButtonType buttonTypeCancel = new ButtonType("Close Application");

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne) {
				File newfile = chooseDirectory(primaryStage);
				if (newfile != null) {
					prefs.put("filePath", file.getAbsolutePath());
				}
			} else {
				getPrimaryStage().close();
			}

		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveAccountDataToFile(File file, boolean backup) { // Saves account data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File accountsfile = new File(file.getAbsolutePath() + "/Accounts.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(AccountListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			AccountListWrapper wrapper = new AccountListWrapper();
			wrapper.setAccounts(accountdata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, accountsfile);

			if (!backup) {

				// system.out.println("Set File Path Accounts");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setAccountsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + accountsfile.getPath());

			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void savePatientDataToFile(File file, boolean backup) { // Saves patient data to file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File patientsfile = new File(file.getAbsolutePath() + "/Patients.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.PatientListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			PatientListWrapper wrapper = new PatientListWrapper();
			wrapper.setPatients(patientdata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, patientsfile);

			if (!backup) {

				// system.out.println("Set File Path Patients");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setPatientsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + patientsfile.getPath());

			alert.showAndWait();
		}
		if (!backup) {
			saveNotesDataToFile(file, false);
			saveNoteTypeDataToFile(file, false);
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void savePersonDataToFile(File file, boolean backup) { // Saves person data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File peoplefile = new File(file.getAbsolutePath() + "/People.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPeople(persondata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, peoplefile);

			if (!backup) {

				// system.out.println("Set File Path Person");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setPeopleSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + peoplefile.getPath());

			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void savePersonTypeDataToFile(File file, boolean backup) { // Saves person type data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File typesfile = new File(file.getAbsolutePath() + "/PersonTypes.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(PersonTypeWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			PersonTypeWrapper wrapper = new PersonTypeWrapper();
			wrapper.setPersonTypes(persontypes);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, typesfile);

			if (!backup) {

				// system.out.println("Set File Path Person Type");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setTypesSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + typesfile.getPath());

			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveNoteTypeDataToFile(File file, boolean backup) { // Saves person type data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File typesfile = new File(file.getAbsolutePath() + "/NoteTypes.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(NoteTypeWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			NoteTypeWrapper wrapper = new NoteTypeWrapper();
			wrapper.setNoteTypes(notetypes);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, typesfile);

			if (!backup) {

				// Save the file path to the registry.
				setFilePath(file);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + typesfile.getPath());

			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveBuildingDataToFile(File file, boolean backup) { // Saves building data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File buildingsfile = new File(file.getPath() + "/Buildings.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.BuildingWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping data.
			BuildingWrapper wrapper = new BuildingWrapper();
			wrapper.setBuildings(buildingsdata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, buildingsfile);

			if (!backup) {

				// system.out.println("Set File Path Building");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setBuildingsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveRoomDataToFile(File file, boolean backup) { // Saves room data to specified file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File roomsfile = new File(file.getAbsolutePath() + "/Rooms.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.RoomWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			RoomWrapper wrapper = new RoomWrapper();
			wrapper.setRooms(roomdata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, roomsfile);

			if (!backup) {

				// system.out.println("Set File Path Room");

				// Save the file path to the registry.
				setFilePath(file);

				// system.out.println("Saved Rooms");
				appPrefs.setRoomsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + roomsfile.getPath());

			alert.showAndWait();
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveMailingListDataToFile(File file, boolean backup) { // Saves mailing lists to file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File mailinglistsfile = new File(file.getAbsolutePath() + "/MailingLists.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.MailingListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			MailingListWrapper wrapper = new MailingListWrapper();
			wrapper.setMailingList(mailinglists);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, mailinglistsfile);

			if (!backup) {

				// system.out.println("Set File Path Mail");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setMailingListsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + mailinglistsfile.getPath());

			alert.showAndWait();
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveAppointmentsDataToFile(File file, boolean backup) { // Saves appointments to file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File appointmentsfile = new File(file.getAbsolutePath() + "/Appointments.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.AppointmentWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			AppointmentWrapper wrapper = new AppointmentWrapper();
			wrapper.setAppointments(appointments);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, appointmentsfile);

			if (!backup) {

				// system.out.println("Set File Path Appointments");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setAppointmentsSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + appointmentsfile.getPath());

			alert.showAndWait();
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveAppointmentTypeDataToFile(File file, boolean backup) { // Saves appointment types to file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File appointmenttypesfile = new File(file.getAbsolutePath() + "/AppointmentTypes.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.AppointmentTypeWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			AppointmentTypeWrapper wrapper = new AppointmentTypeWrapper();
			wrapper.setAppointments(appointmenttypes);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, appointmenttypesfile);

			if (!backup) {

				// system.out.println("Set File Path AppointmentType");

				// Save the file path to the registry.
				setFilePath(file);

				appPrefs.setAppointmentTypesSaved(true);
				savePreferenceDataToFile(getFilePath());
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + appointmenttypesfile.getPath());

			alert.showAndWait();
		}

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	/**
	 *
	 * @param file
	 */
	public void saveNotesDataToFile(File file, boolean backup) { // Saves notes data to file

		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		}

		File notesfile = new File(file.getAbsolutePath() + "/Notes.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(canopy.app.model.NoteListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			NoteListWrapper wrapper = new NoteListWrapper();
			wrapper.setNotes(notedata);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, notesfile);

			if (!backup) {
				// system.out.println("Set File Path Notes");

				// Save the file path to the registry.
				setFilePath(file);
			}

		} catch (Exception e) { // catches ANY exception
			writeError(e);
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);

			alert.setTitle("Error");
			e.printStackTrace();
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + notesfile.getPath());

			alert.showAndWait();
		}
		if (getPrimaryStage() != null) {
			getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}

	}

	private void saveToBackup() { // Saves all data to backup folder

		Task<Void> loader = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// system.out.println("Saving To Backup Started");

				String path = getFilePath() + "/Backup/";
				File check = new File(path);
				if (!check.exists()) {
					new File(path).mkdirs();
				}

				if (AppointmentTypesSaved()) {
					saveAppointmentTypeDataToFile(check, true);
				}
				if (AppointmentsSaved()) {
					saveAppointmentsDataToFile(check, true);
				}
				if (PatientsSaved()) {
					savePatientDataToFile(check, true);
				}
				if (BuildingsSaved()) {
					saveBuildingDataToFile(check, true);
				}
				if (RoomsSaved()) {
					saveRoomDataToFile(check, true);
				}
				if (PersonTypesSaved()) {
					savePersonDataToFile(check, true);
				}
				if (PeopleSaved()) {
					savePersonTypeDataToFile(check, true);
				}
				if (AccountsSaved()) {
					saveAccountDataToFile(check, true);
				}
				if (MailingListsSaved()) {
					saveMailingListDataToFile(check, true);
				}
				savePreferenceDataToFile(check);
				saveNoteTypeDataToFile(check, true);

				return null;
			}
		};
		loader.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// STUFF TO DO AFTER THREAD GOES HERE
				Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
				prefs.put("BackupDate", LocalDate.now().toString());
				System.out.println("Saving To Backup Finished");

			}
		});
		System.out.println("Saving To Backup");
		new Thread(loader).start();

	}

	private File chooseDirectory(Stage stage) { // Allows user to choose master directory
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select Master Data Directory");
		File selectedDirectory = chooser.showDialog(stage);
		return selectedDirectory;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Appointment> getAppointments() { // Returns all appointments in system
		return appointments;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Room> getRooms() { // Returns all rooms in system
		return roomdata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Account> getAccounts() { // Returns all accounts in system
		return accountdata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Account> getListAccounts() { // Returns only listed accounts
		ObservableList<Account> data = FXCollections.observableArrayList();
		data.addAll(accountdata);
		data.remove(0);
		return data;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Account> getClinicalAccounts() { // Returns only accounts with clinical permissions
		ObservableList<Account> clinicalaccounts = FXCollections.observableArrayList();

		accountdata.stream().filter((account) -> (account.getPermission() == 1 || account.getPermission() == 2 || account.getPermission() == 3)).forEachOrdered((account) -> {
			clinicalaccounts.add(account);
		});
		return clinicalaccounts;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Patient> getPatients() { // Returns all patients in system
		return patientdata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Person> getPeople() { // Returns all people in system
		return persondata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<PersonType> getPersonTypes() { // Returns all person types
		return persontypes;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Note> getNotes() { // Returns all notes in system
		return notedata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<NoteType> getNoteTypes() { // Returns all notes in system
		return notetypes;
	}

	/**
	 *
	 * @param note
	 */
	public void addNote(Note note) { // Adds a note to the system

		notedata.add(note);

	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Building> getBuildings() { // Returns all buildings in system
		return buildingsdata;
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<AppointmentType> getAppointmentTypes() { // Returns all appointment types in system

		int meetingcounter = 0;
		ObservableList<AppointmentType> todelete = FXCollections.observableArrayList();
		for (AppointmentType apptype : appointmenttypes) {
			// System.out.println("App type name: " + apptype.getName() + " ,App type ID: "
			// + String.valueOf(apptype.getId()));
			if (apptype.getId().equals(9999)) {
				meetingcounter += 1;
				if (meetingcounter > 1) {
					System.out.println("---------------EXTRA MEETING FOUND, DELETING-----------------");
					todelete.add(apptype);
				}
				;
				break;
			}
		}
		appointmenttypes.removeAll(todelete);

		return appointmenttypes;
	}

	/**
	 *
	 * @return
	 */
	public Account getCurrentAccount() { // Returns the currently logged in account
		return currentaccount;
	}

	/**
	 *
	 * @param building
	 */
	public void setSelectedBuildingList(int building) { // Sets the currently selected building with regard to it's
														// contained rooms
		roomsListController.setSelectedBuildingList(building);
	}

	/**
	 *
	 */
	public void patientScreenClick() { // Patients clicked
		borderPaneController.patientsclicked();
	}

	/**
	 *
	 * @param account
	 */
	public void setSelectedAccount(Account account) { // Sets selected account in accounts list

		appointmentListController.show();
		// BREAK POINT
		appointmentListController.setMainApp(this);
		appointmentListController.setAccount(account);
		personSideBarController.clear();
		personSideBarController.setMainApp(this);
		personSideBarController.setAccount(account);
		notesListController.setAccount(account);

	}

	/**
	 *
	 * @param patient
	 */
	public void setSelectedPatient(Patient patient) { // Sets selected patient with respect to 'patient' screen
		appointmentListController.show();
		appointmentListController.setMainApp(this);
		appointmentListController.setPatient(patient);
		personSideBarController.clear();
		personSideBarController.setMainApp(this);
		personSideBarController.setPatient(patient);
		notesListController.setPatient(patient);
	}

	/**
	 *
	 * @param person
	 */
	public void setSelectedPerson(Person person) { // Sets selected person in person list
		appointmentListController.hide();
		personSideBarController.clear();
		personSideBarController.setMainApp(this);
		personSideBarController.setPerson(person);
		notesListController.hide();
	}

	/**
	 *
	 */
	public void refreshcalendar() { // Refreshes the calendar view
		try {
			calendarController.initialise();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void raisecalendar() { // Raises calendar with 3d effects
		calendarController.raiseall();
	}

	/**
	 *
	 */
	public void lowercalendar() { // lowers calendar with 3d effects
		calendarController.lowerall();
	}

	/**
	 *
	 * @param patient
	 */
	public void editPatient(Patient patient) { // Edit selected patient
		showPatientEditWindow(patient, true);
	}

	/**
	 *
	 * @param app
	 */
	public void editAppointment(Appointment app) { // Edit selected appointment
		showAppointmentEditWindow(app, null, null, null, null, true);
	}

	/**
	 *
	 * @param staff
	 */
	public void editStaff(Account staff) { // Edit selected staff account
		showAccountEditWindow(staff, true);
	}

	/**
	 *
	 * @param person
	 * @param persontype
	 */
	public void editPerson(Person person, Integer persontype) { // Edit selected person
		showPersonEditWindow(person, true, persontype);
	}

	/**
	 *
	 * @param appointment
	 */

	public void removeAppointment(Appointment appointment) { // remove selected appointment
		appointments.remove(appointment);

		Task<Void> savetask = new Task<Void>() {

			@Override
			protected Void call() {
				saveAppointmentsDataToFile(getFilePath(), false);
				return null;
			}
		};

		new Thread(savetask).start();

		appointmentsidebarcontroller.setMainApp(this);
		if (!appointments.isEmpty()) {
			appointmentsidebarcontroller.setAppointment(appointments.get(0));
		}
		try {
			calendarController.initialise();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param appointmentType
	 */
	public void removeAppointmentType(AppointmentType appointmentType) { // remove selected appointment type
		appointmenttypes.remove(appointmentType);
		saveAppointmentTypeDataToFile(getFilePath(), false);
		appointmentTypeSideBarController.setMainApp(this);
		if (!appointmenttypes.isEmpty()) {
			appointmentTypeSideBarController.setType(appointmenttypes.get(0));
		}
		appointmentTypeListController.refreshlists(appointmenttypes);
	}

	/**
	 *
	 * @param account
	 */
	public void removeAccount(Account account) { // remove selected account
		accountdata.remove(account);
		savePatientDataToFile(getFilePath(), false);
		personSideBarController.setMainApp(this);
		if (accountdata.size() > 0) {
			personSideBarController.setAccount(accountdata.get(1));
		}
		personListController.refreshstaff(getListAccounts());
	}

	/**
	 *
	 * @param person
	 */
	public void removePerson(Person person) { // remove selected person
		persondata.remove(person);
		savePatientDataToFile(getFilePath(), false);
		personSideBarController.setMainApp(this);
		if (!persondata.isEmpty()) {
			personSideBarController.setPerson(persondata.get(0));
		}
		personListController.refreshlist();
	}

	/**
	 *
	 * @param list
	 */
	public void removeMailingList(MailingList list) { // remove selected mailing list
		mailinglists.remove(list);
		saveMailingListDataToFile(getFilePath(), false);
		mailSideBarController.setMainApp(this);
		if (!mailinglists.isEmpty()) {
			mailSideBarController.setList(mailinglists.get(0));
		}
		mailingListController.initialise();
	}

	/**
	 *
	 * @param room
	 */
	public void removeRoom(Room room) { // remove selected room
		roomdata.remove(room);
		saveRoomDataToFile(getFilePath(), false);
		roomsSideBarController.setMainApp(this);
		if (!roomdata.isEmpty()) {
			roomsSideBarController.setRoom(roomdata.get(0));
		}
		roomsListController.refreshrooms();
	}

	/**
	 *
	 * @return
	 */
	public Prefs getPrefs() { // return preferences
		return appPrefs;
	}

	/**
	 *
	 */
	public void syncThread() { // Synchronises data with file system
		if (!checkVersion()) {

			LoadPreferenceDataFromFile(getFilePath());

			Boolean same = true;

			switch (selectedtab) {
			case 0:

				if (AppointmentTypesSaved()) {
					LoadAppointmentTypeDataFromFile(getFilePath());
				}
				if (AppointmentsSaved()) {
					same = LoadAppointmentDataFromFile(getFilePath());
				}
				if (PatientsSaved()) {
					LoadNoteDataFromFile(getFilePath());
					LoadPatientDataFromFile(getFilePath());
					CleanPatientsList();
				}
				if (BuildingsSaved()) {
					LoadBuildingDataFromFile(getFilePath());
				}
				if (RoomsSaved()) {
					LoadRoomDataFromFile(getFilePath());
				}
				if (PersonTypesSaved()) {
					LoadPeopleDataFromFile(getFilePath());
				}
				if (PeopleSaved()) {
					LoadPersonTypeDataFromFile(getFilePath());
				}

				try {
					calendarController.initialise();
				} catch (IOException e) {
					writeError(e);
					e.printStackTrace();
				}

				break;
			case 1:

				if (AppointmentsSaved()) {
					LoadAppointmentDataFromFile(getFilePath());
				}
				if (AppointmentTypesSaved()) {
					LoadAppointmentTypeDataFromFile(getFilePath());
				}
				if (AccountsSaved()) {
					LoadAccountDataFromFile(getFilePath());
				}
				if (PatientsSaved()) {
					LoadNoteDataFromFile(getFilePath());
					same = LoadPatientDataFromFile(getFilePath());
					CleanPatientsList();
				}
				if (BuildingsSaved()) {
					LoadBuildingDataFromFile(getFilePath());
				}
				if (RoomsSaved()) {
					LoadRoomDataFromFile(getFilePath());
				}

				if (MailingListsSaved()) {
					LoadMailingListDataFromFile(getFilePath());
				}
				try {
					personListController.initialise();
				} catch (Exception e) {
					writeError(e);
				}

				break;

			case 2:

				if (MailingListsSaved()) {
					same = LoadMailingListDataFromFile(getFilePath());
				}

				try {
					mailingListController.initialise();
				} catch (Exception e) {
					writeError(e);
				}

				break;

			case 3:

				if (AppointmentsSaved()) {
					LoadAppointmentDataFromFile(getFilePath());
				}
				if (AppointmentTypesSaved()) {
					LoadAppointmentTypeDataFromFile(getFilePath());
				}
				if (PatientsSaved()) {
					LoadNoteDataFromFile(getFilePath());
					LoadPatientDataFromFile(getFilePath());
					CleanPatientsList();
				}
				if (BuildingsSaved()) {
					LoadBuildingDataFromFile(getFilePath());
				}
				if (RoomsSaved()) {
					same = LoadRoomDataFromFile(getFilePath());
				}

				try {
					roomsListController.refreshrooms();
				} catch (Exception e) {
					writeError(e);
				}

				break;

			case 4:

				if (AppointmentTypesSaved()) {
					same = LoadAppointmentTypeDataFromFile(getFilePath());
				}

				try {

					appointmentTypeListController.initialise();
				} catch (Exception e) {
					writeError(e);
				}

				break;
			}

		}
		// REMOVE
		// CleanNotesList();
		// TODO REMOVE
	}

	/**
	 *
	 * @return
	 */
	public Boolean getSmallText() { // returns true if small text size is active
		return smalltext.get();
	}

	public SimpleBooleanProperty SmallTextProperty() { // returns smalltext property
		return smalltext;
	}

	/**
	 *
	 * @param bool
	 */
	public void setSmallText(boolean bool) { // Sets smalltext property
		smalltext.set(bool);
	}

	/**
	 *
	 * @param appointment
	 */
	public void ArchiveAppointment(Appointment appointment) { // Archives selected appointment
		appointments.remove(appointment);

		String deletedstring = appPrefs.getDeletedAppointments() + ";" + String.valueOf(appointment.getID());
		appPrefs.setDeletedAppointments(deletedstring);

		BufferedWriter writer = null;
		try {

			String path = getFilePath() + "/Archive/";
			File check = new File(path);
			if (!check.exists()) {
				new File(path).mkdirs();
			}
			File toFile = new File(check.getAbsolutePath() + "/AppointmentsArchive.txt");

			writer = new BufferedWriter(new FileWriter(toFile, true));
			writer.write("\r\nAppointment: \r\n" + appointment.getDate().toString() + " at " + appointment.getTime().toString() + " for " + appointment.getDuration().toString()
					+ " minutes \r\nStaff Member ID: " + appointment.getStaffMember().toString() + " Patient ID: " + appointment.getPatient().toString() + "\r\nRoom ID: "
					+ appointment.getRoom().getId());
		} catch (Exception e) {
			writeError(e);
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens
				writer.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 *
	 * @param patient
	 */
	public void ArchivePatient(Patient patient) { // Archives selected patient
		patientdata.remove(patient);
		String deletedstring = appPrefs.getDeletedPeople() + ";" + String.valueOf(patient.getPatientNumber());
		appPrefs.setDeletedPeople(deletedstring);

		BufferedWriter writer = null;
		try {

			String path = getFilePath() + "/Archive/";
			File check = new File(path);
			if (!check.exists()) {
				new File(path).mkdirs();
			}
			File toFile = new File(check.getAbsolutePath() + "/PatientsArchive.txt");

			String birthday = "";
			String street = "";
			String city = "";
			String postcode = "";
			int patientnumber = 9999;
			String emails = "";
			String numbers = "";

			if (patient.getBirthday() != null) {
				birthday = patient.getBirthday().toString();
			}
			if (patient.getStreet() != null) {
				street = patient.getStreet();
			}
			if (patient.getCity() != null) {
				city = patient.getCity();
			}
			if (patient.getPostalCode() != null) {
				postcode = patient.getPostalCode();
			}
			if (patient.getPatientNumber() != null) {
				patientnumber = patient.getPatientNumber();
			}
			if (patient.getEmails() != null) {
				emails = patient.getEmails();
			}
			if (patient.getNumbers() != null) {
				numbers = patient.getNumbers();
			}

			writer = new BufferedWriter(new FileWriter(toFile, true));
			writer.write("\r\n\r\n -----PATIENT----- \r\n" + patient.getFirstName() + " " + patient.getLastName() + " \r\nDOB: " + birthday + " \r\nAddress: " + street + " \r\n" + city + " \r\n"
					+ postcode + " \r\nID: " + patientnumber + " \r\nEmails: " + emails + " \r\nNumbers: " + numbers);

			ObservableList<Note> removenoteslist = FXCollections.observableArrayList();
			for (Note note : notedata) {
				if (note.getPatient() == patient.getPatientNumber()) {
					String date = "";
					String Time = "";
					String File = "";
					String Staffcode = "";
					String Text = "";

					if (note.getDate() != null) {
						date = note.getDate().toString();
					}
					if (note.getTime() != null) {
						Time = note.getTime().toString();
					}
					if (note.getFile() != null) {
						File = note.getFile();
					}
					if (note.getStaffCode() != null) {
						Staffcode = note.getStaffCode().toString();
					}
					if (note.getText() != null) {
						Text = note.getText();
					}

					writer.write("\r\n\r\n --NOTE-- \r\n Date: " + date + "\r\n Time: " + Time + "\r\n File: " + File + "\r\n Staffcode: " + Staffcode + "\r\n Text: " + Text);

					removenoteslist.add(note);
				}
			}
			notedata.removeAll(removenoteslist);

		} catch (Exception e) {
			writeError(e);
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens
				writer.close();
			} catch (Exception e) {
			}
		}

		savePatientDataToFile(getFilePath(), false);
		personSideBarController.setMainApp(this);
		if (!patientdata.isEmpty()) {
			personSideBarController.setPatient(patientdata.get(0));
			notesListController.setPatient(patientdata.get(0));
		} else {
			notesListController.hide();
		}

		personListController.refreshpatients(patientdata);

	}

	public void writeFile(String text, String filename) { // Writes error based on exception

		text = text + "\r\n";

		BufferedWriter writer = null;
		try {
			String path = getFilePath() + "/Reports/";
			File check = new File(path);
			if (!check.exists()) {
				new File(path).mkdirs();
			}
			File toFile = new File(check.getAbsolutePath() + "/" + filename + ".txt");

			writer = new BufferedWriter(new FileWriter(toFile, true));
			writer.write(text);
		} catch (Exception r) {
		} finally {
			try {
				// Close the writer regardless of what happens
				writer.close();
			} catch (Exception r) {
			}
		}
	}

	public void writeError(Exception e) { // Writes error based on exception

		System.out.println("Writing Error");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String string = sw.toString(); // stack trace as a string

		BufferedWriter writer = null;
		try {
			String path = getFilePath() + "/Error/";
			File check = new File(path);
			if (!check.exists()) {
				new File(path).mkdirs();
			}
			File toFile = new File(check.getAbsolutePath() + "/Error.txt");

			writer = new BufferedWriter(new FileWriter(toFile, true));
			writer.write(string);
		} catch (Exception r) {
		} finally {
			try {
				// Close the writer regardless of what happens
				writer.close();
			} catch (Exception r) {
			}
		}
	}

	/**
	 *
	 * @param selectedstaff
	 * @return
	 */
	public boolean viewHoliday(Account selectedstaff) { // Shows avaliability dialog for selected staff member
		try {

			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/avaliabilitydialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit " + selectedstaff.getFirstName() + " " + selectedstaff.getLastName() + " Avaliability");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			avaliabilityDialogController controller = loader.getController();
			controller.initialise(this, selectedstaff);
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param selectedstaff
	 * @return
	 */
	public boolean viewPassword(Account selectedstaff) { // Shows view / change password popup for selected staff member
		try {

			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/passwordDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Password");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			passwordDialogController controller = loader.getController();
			controller.Initialize(this, selectedstaff);
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}
	}

	public void seeAppointment(Appointment appointment) {
		setToPlannerScreen();
		borderPaneController.planclicked();
		calendarController.justSetMainApp(this);
		long dayshift = -1 * LocalDate.now().until(appointment.getDate(), ChronoUnit.DAYS);
		calendarController.setDayShift((int) dayshift);
		appointmentsidebarcontroller.setAppointment(appointment);
	}

	public void seePerson(Person person) {

		setToPatientScreen();
		borderPaneController.patientsclicked();
		personSideBarController.setPerson(person);
		personListController.setPersonType(persontypes.get(person.getPersonType())); // This doesn't work yet but
																						// leaving it

	}

	public void seeStaff(Account account) {

		setToPatientScreen();
		borderPaneController.patientsclicked();
		personSideBarController.setAccount(account);
		personListController.setPersonType(persontypes.get(1)); // This doesn't work yet but leaving it

	}

	public void seePatient(Patient patient) {
		setToPatientScreen();
		borderPaneController.patientsclicked();
		personSideBarController.setPatient(patient);
		personListController.setPersonType(persontypes.get(0)); // This doesn't work yet but leaving it

	}

	public void createReminder(Patient selectedpatient) { // Creates a reminder for the selected patient
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/reminderdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create Reminder");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			reminderDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setPatient(selectedpatient);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}
	}

	public static String htmlEscape(String s) { // Escapes the input string to a html friendly string

		// try {
		// s = URLEncoder.encode(s, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// System.out.println(s);

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

	public void addContact(Patient selectedpatient) { // Opens the contact selection window for the selected patient

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/contactsdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Contacts");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			contactsController contactsController;

			// Set the person into the controller.
			contactsController = loader.getController();
			contactsController.setMainApp(this);
			contactsController.setDialogStage(dialogStage);
			contactsController.setPatient(selectedpatient);
			contactsController.initialise();

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}

	}

	public void refreshcontacts(Patient patient) { // Refreshes the contacts list for the selected patient
		if (personSideBarController != null) {
			personSideBarController.setPatient(patient);
		}

	}

	public void forwardNote(Note note, Patient patient) { // Opens the contact selection dialog to forward notes to
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/forwardtodialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Forward To");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			forwardDialogController forwardDialogController;

			// Set the person into the controller.
			forwardDialogController = loader.getController();
			forwardDialogController.setDialogStage(dialogStage);
			forwardDialogController.initialise(this, note, patient);

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}

	}

	public void editNoteTypes() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/notetypesdialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Note Types");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NoteTypesDialogController noteTypesDialogController;

			// Set the person into the controller.
			noteTypesDialogController = loader.getController();
			noteTypesDialogController.setDialogStage(dialogStage);
			noteTypesDialogController.initialise(this);

			// Show the dialog and wait until the user closes it

			dialogStage.showAndWait();

		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
		}

	}

	public void deleteNote(Note note) { // Deletes the selected note
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(this.getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.setTitle("Delete Note?");
		alert.setHeaderText("Are you sure you want to delete this note?");
		alert.setContentText("This note should only be deleted if it was created in error");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			
			new Thread() {
				public void run() {
					notedata.remove(note);
					Task<Void> save = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							saveNotesDataToFile(getFilePath(), false);
							return null;
						}
					};
					save.run();
				}
			}.start();

			notesListController.initialise();

		} else {

		}

	}

	public void updateKeyEncryption() { // Encryption passwords are further scrambled when the appropriate preference is
										// set to true. This ensures that all files are read and then re-written with
										// the
										// new scrambling.

		// ATTENTION CHECK THIS IF CLIENT LOADS FROM BACKUP AND CANT READ DATA!

		System.out.println("Running update key encryption");

		Preferences prefs = Preferences.userNodeForPackage(MainApp.class); // Retrieves preferences

		Task<Void> loadertask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("Running update key encryption loading");
				LoadAccountDataFromFile(getFilePath()); // Begins to load all avaliable data from files
				LoadLicenceFromFile(getFilePath());
				System.out.println("Running update key encryption loading 2");

				if (AppointmentsSaved()) {
					LoadAppointmentDataFromFile(getFilePath());
				}
				if (AppointmentTypesSaved()) {
					LoadAppointmentTypeDataFromFile(getFilePath());
				}
				System.out.println("Running update key encryption loading 3");
				if (BuildingsSaved()) {
					LoadBuildingDataFromFile(getFilePath());
				}
				if (MailingListsSaved()) {
					LoadMailingListDataFromFile(getFilePath());
				}
				System.out.println("Running update key encryption loading 4");
				if (PatientsSaved()) {
					LoadNoteDataFromFile(getFilePath());
					LoadPatientDataFromFile(getFilePath());
					CleanPatientsList();
				}
				if (PeopleSaved()) {
					LoadPeopleDataFromFile(getFilePath());
				}
				System.out.println("Running update key encryption loading 5");
				if (PersonTypesSaved()) {
					LoadPersonTypeDataFromFile(getFilePath());
				}
				if (RoomsSaved()) {
					LoadRoomDataFromFile(getFilePath());
				}
				System.out.println("Running update key encryption loading 6");
				return null;
			}
		};
		loadertask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				appPrefs.setKeysScrambled(true);
				savePreferenceDataToFile(getFilePath());
				prefs.putBoolean("keysUpdated", true);

				System.out.println("Running update key encryption saving");

				saveAccountDataToFile(getFilePath(), false);

				if (AppointmentsSaved()) {
					saveAppointmentsDataToFile(getFilePath(), false);
				}

				if (AppointmentTypesSaved()) {
					saveAppointmentTypeDataToFile(getFilePath(), false);
				}
				System.out.println("Running update key encryption loading 3");
				if (BuildingsSaved()) {
					saveBuildingDataToFile(getFilePath(), false);
				}
				if (MailingListsSaved()) {
					saveMailingListDataToFile(getFilePath(), false);
				}
				System.out.println("Running update key encryption loading 4");
				if (PatientsSaved()) {
					savePatientDataToFile(getFilePath(), false);
					saveNotesDataToFile(getFilePath(), false);

				}
				if (PeopleSaved()) {
					savePersonDataToFile(getFilePath(), false);
				}
				System.out.println("Running update key encryption loading 5");
				if (PersonTypesSaved()) {
					savePersonTypeDataToFile(getFilePath(), false);
				}
				if (RoomsSaved()) {
					saveRoomDataToFile(getFilePath(), false);
				}

				saveLicenceToFile(getFilePath(), licence);
				savePreferenceDataToFile(getFilePath());
			}
		});
		new Thread(loadertask).start();
	}

	public void refreshnotes() {
		notesListController.refresh();
		notesListController.refreshtypepickers();

	}

	/**
	 * This method adds sends an appointment invite, edit, or cancellation .ics to the patient contacts that are in the appointment. If any patients do not have email contacts, this invite will not be
	 * sent.
	 * 
	 * @param appointment
	 *            - The appointment the invite is being created for
	 * @param format
	 *            - 0: initial invite, 1: edit, 2: cancel
	 */
	public void sendAppointmentInvitePatients(Appointment appointment, int format) {
		try {

			System.out.println("Email Sending to Patient");

			ICalendar ical = new ICalendar();
			VEvent event = new VEvent();

			String appointmenttype = "";
			for (AppointmentType type : getAppointmentTypes()) {
				if (appointment.getAppType() == type.getId()) {
					appointmenttype = type.getName();
				}
			}

			Summary summary = event.setSummary(appointmenttype);
			summary.setLanguage("en-us");

			LocalDateTime ldt = LocalDateTime.of(appointment.getDate(), appointment.getTime());
			Date start = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			event.setDateStart(start);

			Duration duration = new Duration.Builder().minutes(appointment.getDuration()).build();
			event.setDuration(duration);

			Collection<Recipient> recipients = FXCollections.observableArrayList(); // In advance for email sending

			for (int apppatient : appointment.getPatients()) { // Cycles through appointment Patient ID's
				for (Patient patient : getPatients()) { // Cycles through all patients
					if (patient.getPatientNumber() == apppatient) { // Finds matches

						if (patient.getEmails() != null) {
							Attendee newatendee = new Attendee(patient.getFirstName() + " " + patient.getLastName(), patient.getEmails()); // Adds Attendees for each patient
							event.addAttendee(newatendee);

							String[] contacts = patient.getEmails().split(";");
							for (String contact : contacts) {
								Recipient recipient = new Recipient(patient.getFirstName() + " " + patient.getLastName(), contact, RecipientType.BCC);
								recipients.add(recipient); // Creates and adds a recipient for each patient.
							}
						}
					}
				}
			}

			event.setOrganizer(new Organizer(currentaccount.getTitle() + " " + currentaccount.getFirstName() + " " + currentaccount.getLastName(), currentaccount.getEmail()));

			event.setUid("CanopyAppointment" + String.valueOf(appointment.getID()));

			event.setLocation(appointment.getRoom().getRoomName() + ", " + getBuildings().get(appointment.getRoom().getBuilding()).getName());

			String description = "";

			description += "This is an automated appointment invite: \nDate: " + appointment.getDate().toString() + "\n" + "Time: " + appointment.getTime().toString() + "\nRoom: "
					+ appointment.getRoom().getRoomName() + "\n" + getBuildings().get(appointment.getRoom().getBuilding()).getAddress() + "\n"
					+ getBuildings().get(appointment.getRoom().getBuilding()).getCity() + "\n" + getBuildings().get(appointment.getRoom().getBuilding()).getPostcode() + "\n\n"
					+ "Patient invite generated by Canopy Admin System. Do not reply to this email.";

			event.setDescription(description);

			if (format == 1) { // Editing
				ical.setMethod("EDIT");
				// event.setSequence(2); // increment the sequence number of the existing event
				event.addComment(" Appointment has been edited ");
			} else if (format == 2) { // Cancelling
				ical.setMethod("CANCEL");
				// event.setSequence(2); // increment the sequence number of the existing event
				event.addComment(" Appointment has been cancelled ");
			}

			ical.addEvent(event);

			File file = new File("meeting.ics");
			String eventstring = Biweekly.write(ical).go();

			try {
				Biweekly.write(ical).go(file);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Let Simple Java Mail handle the rest

			String sendername;
			String senderemail;

			if (appPrefs.getAutomatedSenderName() != null) { // Checks if sender name is null, if so gives one.
				sendername = appPrefs.getAutomatedSenderName();
			} else {
				sendername = "Auto Invite";
			}
			if (appPrefs.getAutomatedSenderEmail() != null) { // Checks if sender email is null, if so gives one.
				senderemail = appPrefs.getAutomatedSenderEmail();
			} else {
				senderemail = "AutoInvite";
			}

			Email email = EmailBuilder.startingBlank().from(sendername, "NoReply_" + senderemail + "@canopyadminsoftware.com").to().bcc(recipients).withHTMLText(description)
					.withSubject(appointmenttype).withAttachment("Event", new FileDataSource(file)).buildEmail();

			MailerBuilder.withSMTPServer("smtp.eu.mailgun.org", 25, "postmaster@mg.canopyadminsoftware.com", "4002b21ecd9649fdc507cecdf3098c86-41a2adb4-5da6e48d").buildMailer().sendMail(email);

		} catch (Exception e) {
			writeError(e);
		}
	}

	/**
	 * This method adds sends an appointment invite, edit, or cancellation .ics to the staff contacts that are in the appointment. If any staff member does not have email contacts, this invite will
	 * not be sent.
	 * 
	 * @param appointment
	 *            - The appointment the invite is being created for
	 * @param format
	 *            - 0: initial invite, 1: edit, 2: cancel
	 */
	public void sendAppointmentInviteStaff(Appointment appointment, int format) {
		try {

			System.out.println("Email Sending to Staff");

			ICalendar ical = new ICalendar();
			VEvent event = new VEvent();

			String appointmenttype = "";
			for (AppointmentType type : getAppointmentTypes()) {
				if (appointment.getAppType() == type.getId()) {
					appointmenttype = type.getName();
				}
			}

			Summary summary = event.setSummary(appointmenttype);
			summary.setLanguage("en-us");

			LocalDateTime ldt = LocalDateTime.of(appointment.getDate(), appointment.getTime());
			Date start = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			event.setDateStart(start);

			Duration duration = new Duration.Builder().minutes(appointment.getDuration()).build();
			event.setDuration(duration);

			Collection<Recipient> recipients = FXCollections.observableArrayList(); // In advance for email sending

			for (int appstaff : appointment.getStaffMembers()) { // Cycles through appointment Patient ID's
				for (Account account : getAccounts()) { // Cycles through all patients
					if (account.getStaffNumber() == appstaff) { // Finds matches

						if (account.getEmail() != null) {
							Attendee newatendee = new Attendee(account.getTitle() + " " + account.getFirstName() + " " + account.getLastName(), account.getEmail()); // Adds Attendees for each patient
							event.addAttendee(newatendee);

							Recipient recipient = new Recipient(account.getTitle() + " " + account.getFirstName() + " " + account.getLastName(), account.getEmail(), RecipientType.BCC);
							recipients.add(recipient); // Creates and adds a recipient for each patient.
						}
					}
				}
			}

			event.setOrganizer(new Organizer(currentaccount.getTitle() + " " + currentaccount.getFirstName() + " " + currentaccount.getLastName(), currentaccount.getEmail()));

			event.setUid("CanopyAppointment" + String.valueOf(appointment.getID()));

			event.setLocation(appointment.getRoom().getRoomName() + ", " + getBuildings().get(appointment.getRoom().getBuilding()).getName());

			String description = "";

			description += "This is an automated appointment invite: \nDate: " + appointment.getDate().toString() + "\n" + "Time: " + appointment.getTime().toString() + "\nRoom: "
					+ appointment.getRoom().getRoomName() + "\n" + getBuildings().get(appointment.getRoom().getBuilding()).getAddress() + "\n"
					+ getBuildings().get(appointment.getRoom().getBuilding()).getCity() + "\n" + getBuildings().get(appointment.getRoom().getBuilding()).getPostcode() + "\n\n"
					+ "Staff invite generated by Canopy Admin System. Do not reply to this email.";

			event.setDescription(description);

			if (format == 1) { // Editing
				ical.setMethod("EDIT");
				// event.setSequence(2); // increment the sequence number of the existing event
				event.addComment(" Appointment has been edited ");
			} else if (format == 2) { // Cancelling
				ical.setMethod("CANCEL");
				// event.setSequence(2); // increment the sequence number of the existing event
				event.addComment(" Appointment has been cancelled ");
			}

			ical.addEvent(event);

			File file = new File("meeting.ics");
			String eventstring = Biweekly.write(ical).go();

			try {
				Biweekly.write(ical).go(file);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Let Simple Java Mail handle the rest

			Email email = EmailBuilder.startingBlank().from(appPrefs.getAutomatedSenderName(), "NoReply_" + appPrefs.getAutomatedSenderEmail() + "@canopyadminsoftware.com").to().bcc(recipients)
					.withHTMLText(description).withSubject(appointmenttype).withAttachment("Event", new FileDataSource(file)).buildEmail();

			MailerBuilder.withSMTPServer("smtp.eu.mailgun.org", 25, "postmaster@mg.canopyadminsoftware.com", "4002b21ecd9649fdc507cecdf3098c86-41a2adb4-5da6e48d").buildMailer().sendMail(email);

		} catch (Exception e) {
			writeError(e);
		}
	}

	public void sendEmail(String fromname, String fromemail, String toname, String toemail, String subject, String text) throws AddressException, MessagingException {
		try {

			ICalendar ical = new ICalendar();
			VEvent event = new VEvent();
			Summary summary = event.setSummary("Test Event");
			summary.setLanguage("en-us");

			LocalDateTime ldt = LocalDateTime.now();
			Date start = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			event.setDateStart(start);

			Duration duration = new Duration.Builder().hours(1).build();
			event.setDuration(duration);

			Attendee newatendee = new Attendee(toname, toemail);
			event.addAttendee(newatendee);

			event.setOrganizer(new Organizer(fromname, fromemail));

			event.setUid(fromemail + fromname + LocalDateTime.now().toString());

			Recurrence recur = new Recurrence.Builder(Frequency.WEEKLY).interval(2).build();
			event.setRecurrenceRule(recur);

			ical.addEvent(event);

			File file = new File("meeting.ics");
			String eventstring = Biweekly.write(ical).go();

			try {
				Biweekly.write(ical).go(file);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Let Simple Java Mail handle the rest

			Email email = EmailBuilder.startingBlank().from(fromname, "NoReply@canopyadminsoftware.com").to(toname, toemail).withSubject(subject).withPlainText(text)
					.withAttachment("Event", new FileDataSource(file)).withHTMLText(eventstring).buildEmail();

			MailerBuilder.withSMTPServer("smtp.eu.mailgun.org", 25, "postmaster@mg.canopyadminsoftware.com", "4002b21ecd9649fdc507cecdf3098c86-41a2adb4-5da6e48d").buildMailer().sendMail(email);

		} catch (Exception e) {
			writeError(e);
		}
	}

	public boolean openNoteWindow(Patient patient) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/notedialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			if (getSmallText()) {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet_Small.css").toExternalForm());
			} else {
				page.getStylesheets().clear();
				page.getStylesheets().add(this.getClass().getResource("view/login_Stylesheet.css").toExternalForm());
			}

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Note");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			noteDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.initialise(mainApp, patient);

			// Show the dialog and wait until the user closes it
			dialogStage.show();

			return controller.isOkClicked();
		} catch (IOException e) {
			writeError(e);
			e.printStackTrace();
			return false;
		}

	}

	public void setPersonID() {
		int maxID = 0;

		for (Patient patient : patientdata) {
			if (patient.getPatientNumber() > maxID) {
				maxID = patient.getPatientNumber();
			}
		}
		for (Account acc : accountdata) {
			if (acc.getStaffNumber() > maxID) {
				maxID = acc.getStaffNumber();
			}
		}
		for (Person person : persondata) {
			if (person.getId() > maxID) {
				maxID = person.getId();
			}
		}
		appPrefs.setPersonID(maxID);
	}

}