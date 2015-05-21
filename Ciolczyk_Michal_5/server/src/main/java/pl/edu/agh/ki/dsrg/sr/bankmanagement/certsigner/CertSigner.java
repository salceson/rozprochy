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
        if(args.length < 3) {
            System.err.println("Usage: java CertSigner [ice config] [cert input file] [cert output file]");
            System.exit(666);
        }
        Communicator communicator;
        try (FileInputStream fis = new FileInputStream(args[1]); FileOutputStream fos = new FileOutputStream(args[2])) {
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
