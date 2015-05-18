package pl.edu.agh.ki.dsrg.sr.bankmanagement.bank;

import Bank.*;
import Ice.Current;
import Ice.StringHolder;

/**
 * @author Michał Ciołczyk
 */
public class BankManagerImpl extends _BankManagerDisp {
    @Override
    public void createAccount(PersonalData data, accountType type, StringHolder accountID, Current __current)
            throws IncorrectData, RequestRejected {


        switch (type) {
            case SILVER:

        }
    }

    @Override
    public void removeAccount(String accountID, Current __current) throws IncorrectData, NoSuchAccount {

    }

    private void verifyPersonalData(PersonalData personalData, Current __current) throws IncorrectData {

    }
}
