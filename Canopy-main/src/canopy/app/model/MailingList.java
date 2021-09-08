package canopy.app.model;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.ObjectUtils.Null;

import canopy.app.util.EncryptedIntegerXmlAdapter;
import canopy.app.util.EncryptedStringXmlAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andy
 */
public class MailingList {
	private final StringProperty listname;
	private final StringProperty list;
	private final StringProperty description;
	private final IntegerProperty id;

	/**
	 *
	 */
	public MailingList() {
		this.listname = new SimpleStringProperty();
		this.list = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.id = new SimpleIntegerProperty();
	}

	/**
	 *
	 * @param listname
	 * @param list
	 * @param description
	 */
	public MailingList(String listname, String list, String description) {
		this.listname = new SimpleStringProperty(listname);
		this.list = new SimpleStringProperty(list);
		this.description = new SimpleStringProperty(description);
		this.id = new SimpleIntegerProperty();
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "listName")
	@XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getListName() {
		return listname.get();
	}

	/**
	 *
	 * @param listname
	 */
	public void setListName(String listname) {
		this.listname.set(listname);
	}

	/**
	 *
	 * @return
	 */
	public StringProperty listNameProperty() {
		return listname;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "list")
	@XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getList() {
		cleanList();
		return list.get();
	}

	/**
	 *
	 * @param list
	 */
	public void setList(String list) {
		this.list.set(list);
	}

	/**
	 *
	 * @return
	 */
	public StringProperty listProperty() {
		return list;
	}

	/**
	 *
	 * @param email
	 */
	public void addEmail(String email) {
		String list = getList();
		list = list + ";" + email;
		setList(list);
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "description")
	@XmlJavaTypeAdapter(value = EncryptedStringXmlAdapter.class)
	public String getDescription() {
		return description.get();
	}

	/**
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description.set(description);
	}

	/**
	 *
	 * @return
	 */
	public StringProperty descriptionProperty() {
		return description;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "id")
	@XmlJavaTypeAdapter(value = EncryptedIntegerXmlAdapter.class)
	public Integer getId() {
		return id.get();
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id.set(id);
	}

	public void cleanList() {
		String[] emails = this.list.get().split(";");
		String newemails = "";

		for (String email : emails) {
//			System.out.print("Email = ");
//			System.out.println(email);
			if (email == null || email.trim().equals("")) {
			} else {
				if (newemails.length() > 0) {
					String[] newemailssplit = newemails.split(";");
					if (!Arrays.asList(newemailssplit).contains(email)) {
//						System.out.print("Emails list does not contain ");
//						System.out.println(email);
						newemails += email;
						newemails += ";";
					}
				} else {
					newemails += email;
					newemails += "";
				}
			}
		}
		if (newemails.length() > 0) {
			newemails = newemails.substring(0, newemails.length() - 1);
		} else {
			newemails = "";
		}
		this.list.set(newemails);
	}

	/**
	 *
	 * @return
	 */
	public IntegerProperty idProperty() {
		return id;
	}

}
