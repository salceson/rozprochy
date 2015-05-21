package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.security;

import IceSSL.CertificateVerifier;
import IceSSL.NativeConnectionInfo;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author Michał Ciołczyk
 */
public class SimpleCertificateVerifier implements CertificateVerifier {
    @Override
    public boolean verify(NativeConnectionInfo nativeConnectionInfo) {
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

        X509Certificate x509Certificate = (X509Certificate) cert;
        Principal issuerDN = x509Certificate.getIssuerDN();
        Principal subjectDN = x509Certificate.getSubjectDN();

        System.out.println("Certificate:");
        System.out.println("\t" + issuerDN.getName());
        System.out.println("\t" + subjectDN.getName());
        System.out.println();

        return issuerDN.getName().contains("CN=Laboratorium Systemów Rozproszonych");
    }
}
