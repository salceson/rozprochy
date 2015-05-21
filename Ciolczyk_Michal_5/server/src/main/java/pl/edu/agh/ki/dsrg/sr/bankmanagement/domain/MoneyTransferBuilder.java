package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

/**
 * @author Michał Ciołczyk
 */
public interface MoneyTransferBuilder {
    void transferMoneyTo(String accountNumber) throws NoSuchAccountException, IllegalStateException;
}
