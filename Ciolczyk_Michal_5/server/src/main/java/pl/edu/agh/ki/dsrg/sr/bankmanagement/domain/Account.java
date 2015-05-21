package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import Bank.Currency;

/**
 * @author Michał Ciołczyk
 */
public interface Account {
    String getAccountNumber();

    void increase(int amount);

    void decrease(int amount) throws IllegalStateException;

    int getBalance();

    void takeLoan(int amount, Currency currency, int period);

    MoneyTransferBuilder transfer(int amount);

    @SuppressWarnings("unused")
    enum Type {
        SILVER {
            @Override
            public Account createAccount(String accountNumber) {
                return SilverAccount.create(accountNumber);
            }
        }, PREMIUM {
            @Override
            public Account createAccount(String accountNumber) {
                return PremiumAccount.create(accountNumber);
            }
        };

        public abstract Account createAccount(String accountNumber);

        public static Type fromBankAccountType(Bank.accountType accountType) {
            return valueOf(accountType.toString());
        }
    }
}
