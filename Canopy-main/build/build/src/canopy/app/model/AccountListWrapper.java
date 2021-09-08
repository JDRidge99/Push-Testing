package canopy.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andy
 */
@XmlRootElement(name = "accounts")
public class AccountListWrapper {

    private List<Account> accounts;

    /**
     *
     * @return
     */
    @XmlElement(name = "account")
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     *
     * @param accounts
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}