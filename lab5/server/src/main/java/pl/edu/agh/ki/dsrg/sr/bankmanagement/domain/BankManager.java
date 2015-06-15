package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import java.util.UUID;

/**
 * @author Michał Ciołczyk
 */
public class BankManager {
    private final AccountRepository accountRepository = AccountRepository.getInstance();

    public String createAccount(Account.Type accountType) {
        String accountNumber = UUID.randomUUID().toString();
        accountRepository.register(accountNumber, accountType);
        return accountNumber;
    }

    public void removeAccount(String accountNumber) throws NoSuchAccountException {
        accountRepository.unregister(accountNumber);
    }

    public Account.Type getAccountType(String accountID) throws NoSuchAccountException {
        return accountRepository.getAccountType(accountID);
    }
}
