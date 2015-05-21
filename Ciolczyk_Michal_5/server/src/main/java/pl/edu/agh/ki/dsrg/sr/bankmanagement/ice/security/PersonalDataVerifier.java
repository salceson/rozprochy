package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.security;

import Bank.PersonalData;
import IceSSL.NativeConnectionInfo;

/**
 * @author Michał Ciołczyk
 */
public interface PersonalDataVerifier {
    boolean verifyPersonalDataWithCertificate(PersonalData personalData,
                                              NativeConnectionInfo connectionInfo);
}
