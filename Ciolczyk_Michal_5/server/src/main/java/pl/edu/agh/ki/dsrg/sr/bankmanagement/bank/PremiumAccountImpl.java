package pl.edu.agh.ki.dsrg.sr.bankmanagement.bank;

import Bank.*;
import Ice.Current;
import Ice.FloatHolder;
import Ice.IntHolder;

/**
 * @author Michał Ciołczyk
 */
public class PremiumAccountImpl extends _PremiumAccountDisp {
    AccountImpl account;

    public PremiumAccountImpl(int balance) {
        super();
        account = new AccountImpl(balance);
    }

    @Override
    public void calculateLoan(int amount, Currency curr, int period, FloatHolder interestRate, IntHolder totalCost, Current __current) throws IncorrectData {

    }

    @Override
    public int getBalance(Current __current) {
        return account.getBalance(__current);
    }

    @Override
    public String getAccountNumber(Current __current) {
        return account.getAccountNumber(__current);
    }

    @Override
    public void transferMoney(String accountNumber, int amount, Current __current) throws IncorrectAccountNumber, IncorrectAmount {
        account.transferMoney(accountNumber, amount, __current);
    }

}
