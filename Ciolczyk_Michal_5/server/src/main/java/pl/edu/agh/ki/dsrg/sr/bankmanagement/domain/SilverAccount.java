package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import Bank.Currency;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews.FinancialData;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews.FinancialDataRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.CurrencyPackageConverter;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor(staticName = "create")
@ToString
@EqualsAndHashCode(of = "accountNumber")
public class SilverAccount implements Account {
    private final String accountNumber;
    private int balance = 0;
    private final FinancialData financialData = FinancialDataRepository.getInstance();
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
    public synchronized void decrease(int amount) {
        balance -= amount;
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }

    @Override
    public void makeLoan(int amount, Currency currency, int period) {
        if (loan != 0) {
            throw new IllegalStateException("You have an active loan!");
        }

        float interestRate = financialData.getInterestRate(
                CurrencyPackageConverter.convertBankCurrencyToFinancialCurrency(currency)
        );

        float exchangeRate = financialData.getExchangeRate(new Pair<>(
                CurrencyPackageConverter.convertBankCurrencyToFinancialCurrency(currency),
                FinancialNews.Currency.PLN
        ));

        increase(amount);
        loan = (int) Math.round(amount * exchangeRate * (1 + Math.pow(interestRate, period)));
    }

    @Override
    public MoneyTransferBuilder transfer(final int amount) {
        return toAccountNumber -> {
            final AccountRepository accountRepository = AccountRepository.getInstance();
            Account account = accountRepository.get(toAccountNumber).orElseThrow(NoSuchAccountException::new);
            synchronized (SilverAccount.this) {
                if (amount < getBalance()) {
                    throw new IllegalArgumentException("Not enough money!");
                }
                decrease(amount);
            }
            account.increase(amount);
        };
    }
}
