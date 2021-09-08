package canopy.app.util;

import canopy.app.model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class ComparatorBoxStaff<Account> implements EventHandler<KeyEvent> { // Comparator box for Accounts is a modified combobox allowing typed Accounts to be searched and selected via dropdown list

	private final ComboBox<Account> comboBox;
	private final StringBuilder sb;
	private final ObservableList<Account> data;
	private boolean moveCaretToPos = false;
	private int caretPos;

	/**
	 *
	 * @param comboBox
	 */
	public ComparatorBoxStaff(ComboBox<Account> comboBox) {
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
		this.comboBox.setOnKeyReleased(ComparatorBoxStaff.this);
	}

	/**
	 *
	 * @param pos
	 * @return
	 */
	public boolean checkContainment(int pos) {

		Account account = data.get(pos);
		String firstname = ((canopy.app.model.Account) account).getFirstName().toLowerCase();
		String lastname = ((canopy.app.model.Account) account).getLastName().toLowerCase();
		String title = ((canopy.app.model.Account) account).getTitle().toLowerCase();
		String role = ((canopy.app.model.Account) account).getRole().toLowerCase();

		if (firstname.startsWith(comboBox.getEditor().getText().toLowerCase())) {
			return true;
		}

		else if (lastname.startsWith(comboBox.getEditor().getText().toLowerCase())) {
			return true;
		}
		
		else if (title.startsWith(comboBox.getEditor().getText().toLowerCase())) {
			return true;
		}
		
		else if (role.startsWith(comboBox.getEditor().getText().toLowerCase())) {
			return true;
		}
		
		else
			return false;

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

		ObservableList<Account> list = FXCollections.observableArrayList();
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
