package pl.edu.agh.ki.dsrg.sr.bankmanagement.locators;

import Bank.Account;
import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.bank.AccountImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Michał Ciołczyk
 */
public class SilverAccountEvictor extends EvictorBase {
    public SilverAccountEvictor() {
        super(2);
    }

    @Override
    protected Ice.Object add(Current current, LocalObjectHolder cookieHolder) {
        int balance;
        try {
            FileInputStream fis = new FileInputStream("accounts/" + current.id.category + "_" + current.id.name + ".dat");
            byte[] buffer = new byte[1024];
            if (fis.read(buffer) != Integer.BYTES) {
                return null;
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            balance = byteBuffer.getInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new AccountImpl(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        cookieHolder.value = current.id.category + "_" + current.id.name;
        return new AccountImpl(balance);
    }

    @Override
    protected void evict(Object servant, java.lang.Object cookie) {
        if (!(servant instanceof Account)) {
            return;
        }
        Account account = (Account) servant;
        int balance = account.getBalance();
        try {
            FileOutputStream fos = new FileOutputStream("accounts/" + cookie + ".dat");
            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
            byteBuffer.putInt(balance);
            fos.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
