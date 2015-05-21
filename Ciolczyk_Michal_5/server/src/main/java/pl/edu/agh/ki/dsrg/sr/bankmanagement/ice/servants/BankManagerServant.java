package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants;

import Bank.*;
import Ice.Current;
import Ice.StringHolder;

/**
 * @author Michał Ciołczyk
 */
public class BankManagerServant extends _BankManagerDisp {
    @Override
    public void createAccount(PersonalData data, accountType type, StringHolder accountID, Current __current) throws IncorrectData, RequestRejected {

    }

    @Override
    public void removeAccount(String accountID, Current __current) throws IncorrectData, NoSuchAccount {

    }
}
