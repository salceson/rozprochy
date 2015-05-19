package pl.edu.agh.ki.dsrg.sr.bankmanagement.bank;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank._AccountDisp;
import Ice.Current;

/**
 * @author Michał Ciołczyk
 */
public class AccountImpl extends _AccountDisp {
    private int balance;

    public AccountImpl(int balance) {
        super();
        this.balance = balance;
    }

    @Override
    public int getBalance(Current __current) {
        return balance;
    }

    @Override
    public String getAccountNumber(Current __current) {
        return null;
    }

    @Override
    public void transferMoney(String accountNumber, int amount, Current __current) throws IncorrectAccountNumber, IncorrectAmount {

    }
}
