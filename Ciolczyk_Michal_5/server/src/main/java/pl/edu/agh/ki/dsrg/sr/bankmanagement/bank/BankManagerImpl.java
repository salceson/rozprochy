package pl.edu.agh.ki.dsrg.sr.bankmanagement.bank;

import Bank.*;
import Ice.Current;
import Ice.StringHolder;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michał Ciołczyk
 */
public class BankManagerImpl extends _BankManagerDisp {
    private final Map<String, AccountData> currentAccounts = new HashMap<>();

    @Override
    public void createAccount(PersonalData data, accountType type, StringHolder accountID, Current __current)
            throws IncorrectData, RequestRejected {
        verifyPersonalData(data, __current);

        switch (type) {
            case SILVER:

        }
    }

    @Override
    public void removeAccount(String accountID, Current __current) throws IncorrectData, NoSuchAccount {

    }

    private void verifyPersonalData(PersonalData personalData, Current __current) throws IncorrectData {
        if (!isDataValid(personalData)) {
            throw new IncorrectData();
        }
    }

    private boolean isDataValid(PersonalData personalData) {
        return !Strings.isNullOrEmpty(personalData.firstName) &&
                !Strings.isNullOrEmpty(personalData.lastName) &&
                !Strings.isNullOrEmpty(personalData.nationality) &&
                !Strings.isNullOrEmpty(personalData.nationalIDNumber);
    }
}
