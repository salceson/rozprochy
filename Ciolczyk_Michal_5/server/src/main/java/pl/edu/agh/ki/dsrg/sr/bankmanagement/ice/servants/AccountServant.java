package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank._AccountDisp;
import Ice.Current;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.Account;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.AccountRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.NoSuchAccountException;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor(staticName = "create")
public class AccountServant extends _AccountDisp {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServant.class);
    private final AccountRepository accountRepository = AccountRepository.getInstance();

    @Getter
    private final Account account;

    @Override
    public int getBalance(Current __current) {
        LOGGER.info("Get balance for: " + account);
        return account.getBalance();
    }

    @Override
    public String getAccountNumber(Current __current) {
        LOGGER.info("Get account number for: " + account);
        return account.getAccountNumber();
    }

    @Override
    public void transferMoney(String accountNumber, int amount, Current __current) throws IncorrectAccountNumber, IncorrectAmount {
        LOGGER.info("Transfer money (" + amount + ") to: " + accountNumber);
        try {
            account.transfer(amount).transferMoneyTo(accountNumber);
        } catch (IllegalStateException e) {
            throw new IncorrectAmount(e);
        } catch (NoSuchAccountException e) {
            throw new IncorrectAccountNumber(e);
        }
    }
}
