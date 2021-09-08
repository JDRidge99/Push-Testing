package canopy.app.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.Appointment;
import canopy.app.model.MailingList;
import canopy.app.model.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Andy
 */
public class mailSideBarController {

	@FXML
	private Label listname;

	@FXML
	private Label description;

	@FXML
	private VBox emailsbox;

	MailingList currentlist;

	private MainApp mainapp;

	@FXML
	private AnchorPane parent;

	@FXML
	private ScrollPane scrollpane;

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
	 * @param list
	 */
	public void setList(MailingList list) {
		this.currentlist = list;
		if (list.getListName().equals("All Patients")) {
			refreshpatientlist(list);
		} else if (list.getListName().equals("All Staff")) {
			refreshstafflist(list);
		}
		listname.setText(list.getListName());
		description.setText(list.getDescription());

		String style = "     -fx-font-size: 9pt;\r\n" + "    -fx-font-family: \"Segoe UI Semibold\";\r\n" + "    -fx-text-fill: black;\r\n" + "    -fx-opacity: 0.6;";

		String[] emails = list.getList().split(";");

		emailsbox.getChildren().clear();
		String removenull = "";
		for (String email : emails) {
			if (email != null && !email.trim().equals("null")) {
				removenull += email + ";";
				Label emaillabel = new Label();
				emaillabel.setText(email);
				emaillabel.setStyle(style);
				emailsbox.getChildren().add(emaillabel);
			}
			currentlist.setList(removenull);

		}
	}

	public void refreshpatientlist(MailingList list) {
		String listcontent = "";
		for (Patient patient : mainapp.getPatients()) {
			if (patient.getEmails() != null && !patient.getEmails().trim().equals("")) {
				listcontent += patient.getEmails() + ";";
			}
		}
		list.setList(listcontent);
	}

	public void refreshstafflist(MailingList list) {
		String listcontent = "";
		for (Account account : mainapp.getAccounts()) {
			if (account.getEmail() != null && !account.getEmail().trim().equals("")) {
				listcontent += account.getEmail() + ";";
			}
		}
		list.setList(listcontent);
	}

	@FXML
	void email() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				try {
					mailto = new URI("mailto:?Bcc=" + currentlist.getList());
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
	void remove() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("dialog_Stylesheet.css").toExternalForm());
		dialogPane.getStyleClass().add(".dialog-pane");

		alert.initOwner(mainapp.getPrimaryStage());
		alert.setTitle("Delete Mailing List?");
		alert.setHeaderText("Do you wish to permanently delete this Mailing List?");

		ButtonType buttonTypeOne = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			String deletedstring = mainapp.appPrefs.getDeletedMail() + ";" + String.valueOf(currentlist.getId());
			mainapp.appPrefs.setDeletedMail(deletedstring);
			mainapp.removeMailingList(currentlist);

			mainapp.savePreferenceDataToFile(mainapp.getFilePath());
			mainapp.setVersion();

		} else {

		}
	}

	@FXML
	void raiseall() {
	}

	@FXML
	void lowerall() {
	}

}
