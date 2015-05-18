package pl.edu.agh.ki.dsrg.sr.bankmanagement.certsigner;

import Ice.Communicator;
import Ice.ObjectPrx;
import SR.CertSignerPrx;
import SR.CertSignerPrxHelper;
import SR.DataTooLong;
import SR.IncorrectCSRFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static Ice.Util.initialize;

/**
 * @author Michał Ciołczyk
 */
public class CertSigner {
    public static void main(String[] args) {
        Communicator communicator = null;
        try (FileInputStream fis = new FileInputStream("c.csr"); FileOutputStream fos = new FileOutputStream("c.crt")) {
            communicator = initialize(args);
            ObjectPrx certSignerProxy = communicator.propertyToProxy("CertSigner");
            CertSignerPrx certSigner = CertSignerPrxHelper.checkedCast(certSignerProxy);
            byte[] buffer = new byte[4096];
            int read = fis.read(buffer);
            byte[] cert = new byte[read];
            System.arraycopy(buffer, 0, cert, 0, read);
            byte[] signed = certSigner.signCSR("Michal", "Ciolczyk", cert);
            fos.write(signed);
        } catch (DataTooLong | IncorrectCSRFile | IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
