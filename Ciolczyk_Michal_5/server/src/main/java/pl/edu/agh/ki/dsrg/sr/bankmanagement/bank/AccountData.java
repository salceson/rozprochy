package pl.edu.agh.ki.dsrg.sr.bankmanagement.bank;

import Bank.PersonalData;
import Bank.accountType;
import lombok.Value;

/**
 * @author Michał Ciołczyk
 */
@Value
public class AccountData {
    private final PersonalData personalData;
    private final accountType type;
}
