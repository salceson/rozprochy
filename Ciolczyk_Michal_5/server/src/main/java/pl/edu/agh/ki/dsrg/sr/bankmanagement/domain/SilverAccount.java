package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor(staticName = "create")
@ToString
@EqualsAndHashCode(of = "accountNumber")
public class SilverAccount implements Account {
    private final String accountNumber;
    private int balance = 0;
    private int loan = 0;

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
        balance -= amount;
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }

    @Override
    public MoneyTransferBuilder transfer(final int amount) {
        return toAccountNumber -> {
            final AccountRepository accountRepository = AccountRepository.getInstance();
            Account toAccount = accountRepository.get(toAccountNumber).orElseThrow(NoSuchAccountException::new);
            decrease(amount);
            toAccount.increase(amount);
        };
    }
}
