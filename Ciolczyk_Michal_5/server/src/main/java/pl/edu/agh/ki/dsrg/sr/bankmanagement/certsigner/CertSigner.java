package pl.edu.agh.ki.dsrg.sr.bankmanagement.certsigner;

import Ice.Current;
import SR.DataTooLong;
import SR.IncorrectCSRFile;

/**
 * @author Michał Ciołczyk
 */
public class CertSigner extends SR._CertSignerDisp {
    @Override
    public byte[] signCSR(String name, String surname, byte[] csrFile, Current __current)
            throws DataTooLong, IncorrectCSRFile {
        return new byte[0];
    }
}
