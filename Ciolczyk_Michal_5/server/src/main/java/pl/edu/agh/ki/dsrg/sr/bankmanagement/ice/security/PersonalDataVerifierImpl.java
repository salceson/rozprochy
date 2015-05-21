package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.security;

import Bank.PersonalData;
import IceSSL.NativeConnectionInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author Michał Ciołczyk
 */
public class PersonalDataVerifierImpl implements PersonalDataVerifier {
    private final Logger LOGGER = LoggerFactory.getLogger(PersonalDataVerifierImpl.class);
    private static final String CERTIFICATE_FORMAT = "Certificate:\n\tData: %s, %s\n\tUser data: %s, %s\n";
    private static final PersonalDataVerifierImpl instance = new PersonalDataVerifierImpl();

    public static PersonalDataVerifierImpl getInstance() {
        return instance;
    }

    @Override
    public boolean verifyPersonalDataWithCertificate(PersonalData personalData,
                                                     NativeConnectionInfo nativeConnectionInfo) {
        if (nativeConnectionInfo.nativeCerts.length < 1) {
            return false;
        }

        if (nativeConnectionInfo.nativeCerts[0] == null) {
            return false;
        }

        Certificate cert = nativeConnectionInfo.nativeCerts[0];

        if (!(cert instanceof X509Certificate)) {
            return false;
        }

        X509Certificate certificate = (X509Certificate) cert;

        X500Name x500name = new X500Name(certificate.getSubjectX500Principal().getName());

        String cn = x500name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
        String c = x500name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();

        String ownerCn = personalData.firstName + " " + personalData.lastName;
        String ownerC = personalData.nationality;

        LOGGER.info(String.format(CERTIFICATE_FORMAT, cn, c, ownerCn, ownerC));

        return (ownerCn.equals(cn) && ownerC.equals(c));
    }
}
