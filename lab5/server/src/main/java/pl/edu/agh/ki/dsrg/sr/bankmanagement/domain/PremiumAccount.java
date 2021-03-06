package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews.FinancialData;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews.FinancialDataRepository;

/**
 * @author Michał Ciołczyk
 */
@EqualsAndHashCode(of = "accountNumber")
@ToString
public class PremiumAccount implements Account {
    private final FinancialData financialData = FinancialDataRepository.getInstance();

    private final String accountNumber;

    private int balance;

    private PremiumAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static PremiumAccount create(String accountNumber) {
        PremiumAccount account = new PremiumAccount(accountNumber);
        account.balance = 500;
        return account;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public synchronized void increase(int amount) {
        balance += amount;
    }

    @Override
    public synchronized void decrease(int amount) throws IllegalStateException {
        if (balance < amount) {
            throw new IllegalStateException("Not enough money!");
        }
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }

    @Override
    public MoneyTransferBuilder transfer(int amount) {
        return toAccountNumber -> {
            if (amount <= 0) {
                throw new IllegalStateException("Amount <= 0!");
            }
            final AccountRepository accountRepository = AccountRepository.getInstance();
            Account toAccount = accountRepository.get(toAccountNumber).orElseThrow(NoSuchAccountException::new);
            decrease(amount);
            toAccount.increase(amount);
        };
    }
}
