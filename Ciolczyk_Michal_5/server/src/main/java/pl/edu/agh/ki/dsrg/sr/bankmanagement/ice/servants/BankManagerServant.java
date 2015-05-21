package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants;

import Bank.*;
import Ice.Current;
import Ice.StringHolder;
import lombok.RequiredArgsConstructor;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class BankManagerServant extends _BankManagerDisp {
    private final pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.BankManager bankManager;

    @Override
    public void createAccount(PersonalData data, accountType type, StringHolder accountID, Current __current) throws IncorrectData, RequestRejected {

    }

    @Override
    public void removeAccount(String accountID, Current __current) throws IncorrectData, NoSuchAccount {

    }
}
