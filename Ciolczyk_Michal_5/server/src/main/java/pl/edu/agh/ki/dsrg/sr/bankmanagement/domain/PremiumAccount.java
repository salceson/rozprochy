package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import Bank.Currency;

/**
 * @author Michał Ciołczyk
 */
public class PremiumAccount implements Account {
    @Override
    public String getAccountNumber() {
        return null;
    }

    @Override
    public void increase(int amount) {

    }

    @Override
    public void decrease(int amount) {

    }

    @Override
    public int getBalance() {
        return 0;
    }

    @Override
    public void makeLoan(int amount, Currency currency, int period) {

    }

    @Override
    public MoneyTransferBuilder transfer(int amount) {
        return null;
    }
}
