package canopy.app.util;

import canopy.app.MainApp;
import canopy.app.model.Account;
import canopy.app.model.NoteType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

public class ComparatorBoxNoteType<NoteType> implements EventHandler<KeyEvent> { // Comparator box for Accounts is a modified combobox allowing typed Accounts to be searched and selected via dropdown
																					// list

	private final ComboBox<NoteType> comboBox;
	private final StringBuilder sb;
	private final ObservableList<NoteType> data;
	private boolean moveCaretToPos = false;
	private int caretPos;
	


	/**
	 *
	 * @param comboBox
	 */
	public ComparatorBoxNoteType(ComboBox<NoteType> comboBox) {
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
		this.comboBox.setOnKeyReleased(ComparatorBoxNoteType.this);	
		

	}
	

	/**
	 *
	 * @param pos
	 * @return
	 */
	public boolean checkContainment(int pos) {

		NoteType type = data.get(pos);
		String name = ((canopy.app.model.NoteType) type).getTypeName().toLowerCase();

		if (name.startsWith(comboBox.getEditor().getText().toLowerCase())) {
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

		ObservableList<NoteType> list = FXCollections.observableArrayList();
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
