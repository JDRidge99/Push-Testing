package canopy.app.util;

import canopy.app.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Andy
 * @param <Patient>
 */
public class ComparatorBox<Patient> implements EventHandler<KeyEvent> { // Comparator box for patients is a modified combobox allowing typed patients to be searched and selected via dropdown list

	private final ComboBox<Patient> comboBox;
	private final StringBuilder sb;
	private final ObservableList<Patient> data;
	private boolean moveCaretToPos = false;
	private int caretPos;

	/**
	 *
	 * @param comboBox
	 */
	public ComparatorBox(ComboBox<Patient> comboBox) {
		this.comboBox = comboBox;
		sb = new StringBuilder();
		data = comboBox.getItems();

		this.comboBox.setEditable(true);
		this.comboBox.setStyle("-fx-font-size: 9pt;");
		this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				// comboBox.hide();
			}
		});
		this.comboBox.setOnKeyReleased(ComparatorBox.this);
	}

	/**
	 *
	 * @param pos
	 * @return
	 */
	public boolean checkContainment(int pos) {

		Patient patient = data.get(pos);
		String firstname = ((canopy.app.model.Patient) patient).getFirstName().toLowerCase();
		String lastname = ((canopy.app.model.Patient) patient).getLastName().toLowerCase();

		if (firstname.startsWith(comboBox.getEditor().getText().toLowerCase())) {
			return true;
		} else
			return lastname.startsWith(comboBox.getEditor().getText().toLowerCase());
	}

	@Override
	public void handle(KeyEvent event) {
		if (null != event.getCode())
			switch (event.getCode()) {
			case UP:
				caretPos = -1;
				moveCaret(comboBox.getEditor().getText().length());
				return;
			case DOWN:
				if (!comboBox.isShowing()) {
					comboBox.show();
				}
				caretPos = -1;
				moveCaret(comboBox.getEditor().getText().length());
				return;
			case BACK_SPACE:
				moveCaretToPos = true;
				caretPos = comboBox.getEditor().getCaretPosition();
				break;
			case DELETE:
				moveCaretToPos = true;
				caretPos = comboBox.getEditor().getCaretPosition();
				break;
			default:
				break;
			}

		if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.isControlDown() || event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END
				|| event.getCode() == KeyCode.TAB) {

			return;
		}

		if (event.getCode() == KeyCode.ENTER) {
			return;
		}

		ObservableList<Patient> list = FXCollections.observableArrayList();
		for (int i = 0; i < data.size(); i++) {
			if (checkContainment(i)) {
				list.add(data.get(i));
			}

		}
		String t = comboBox.getEditor().getText();

		comboBox.setItems(list);
		comboBox.getEditor().setText(t);
		if (!moveCaretToPos) {
			caretPos = -1;
		}
		moveCaret(t.length());
		if (!list.isEmpty()) {
			comboBox.show();
		}
	}

	private void moveCaret(int textLength) {
		if (caretPos == -1) {
			comboBox.getEditor().positionCaret(textLength);
		} else {
			comboBox.getEditor().positionCaret(caretPos);
		}
		moveCaretToPos = false;
	}

}
