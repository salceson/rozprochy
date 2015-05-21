package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants;

import Bank.*;
import Ice.ConnectionInfo;
import Ice.Current;
import Ice.Identity;
import Ice.StringHolder;
import IceSSL.NativeConnectionInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.Main;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.Account.Type;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.AccountRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.NoSuchAccountException;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.security.PersonalDataVerifier;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.security.PersonalDataVerifierImpl;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class BankManagerServant extends _BankManagerDisp {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankManagerServant.class);
    private final pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.BankManager bankManager;
    private final AccountRepository accountRepository = AccountRepository.getInstance();
    private final PersonalDataVerifier verifier = PersonalDataVerifierImpl.getInstance();

    @Override
    public void createAccount(PersonalData data, accountType type, StringHolder accountID, Current current) throws
            IncorrectData, RequestRejected {
        if (!isPersonalDataValid(data)) {
            throw new IncorrectData();
        }

        if (!isCertificateValid(data, current)) {
            throw new RequestRejected();
        }

        Type accountType = Type.fromBankAccountType(type);

        String createdAccount = bankManager.createAccount(accountType);

        if (accountType == Type.PREMIUM) {
            current.adapter.add(
                    PremiumAccountServant.create(accountRepository.loadToMemory(createdAccount).get()),
                    new Identity(createdAccount, Main.ACCOUNT_CATEGORY));
        }
        accountID.value = createdAccount;

        LOGGER.info("Created account with number: " + createdAccount);
    }

    @Override
    public void removeAccount(String accountID, Current current) throws IncorrectData, NoSuchAccount {
        if (StringUtils.isBlank(accountID)) {
            throw new IncorrectData();
        }
        try {
            if (bankManager.getAccountType(accountID) == Type.PREMIUM) {
                current.adapter.remove(new Identity(accountID, Main.ACCOUNT_CATEGORY));
            }
            bankManager.removeAccount(accountID);
        } catch (NoSuchAccountException e) {
            throw new NoSuchAccount(e);
        }

        LOGGER.info("Removed account: " + accountID);
    }

    private boolean isCertificateValid(PersonalData data, Current current) {
        final ConnectionInfo info = current.con.getInfo();

        if (!(info instanceof NativeConnectionInfo)) {
            return false;
        }

        final NativeConnectionInfo nativeConnectionInfo = (NativeConnectionInfo) info;

        return verifier.verifyPersonalDataWithCertificate(data, nativeConnectionInfo);
    }

    private boolean isPersonalDataValid(PersonalData data) {
        return !StringUtils.isAnyBlank(
                data.firstName,
                data.lastName,
                data.nationalIDNumber,
                data.nationality
        );
    }
}
