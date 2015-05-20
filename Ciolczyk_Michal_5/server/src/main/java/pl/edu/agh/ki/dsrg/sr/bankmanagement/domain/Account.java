package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import Bank.Currency;

/**
 * @author Michał Ciołczyk
 */
public interface Account {
    String getAccountNumber();

    void increase(int amount);

    void decrease(int amount);

    int getBalance();

    void makeLoan(int amount, Currency currency, int period);

    MoneyTransferBuilder transfer(int amount);

    public enum Type {
        SILVER {
            @Override
            public Account createAccount(String accountNumber) {
                return SilverAccount.create(accountNumber);
            }
        }, PREMIUM {
            @Override
            public Account createAccount(String accountNumber) {
                return null;
            }
        };

        public abstract Account createAccount(String accountNumber);
    }
}
