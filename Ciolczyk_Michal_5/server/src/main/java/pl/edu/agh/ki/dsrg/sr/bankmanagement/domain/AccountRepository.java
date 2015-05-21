package pl.edu.agh.ki.dsrg.sr.bankmanagement.domain;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Michał Ciołczyk
 */
public class AccountRepository {
    private static final String NOT_USED_DELIMITER = "\\Z";
    private static AccountRepository instance = new AccountRepository();
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepository.class);
    private static final String DATA_DIR = "data/";
    private static final Gson gson = new Gson();
    private static final Map<String, Account.Type> registeredAccounts = Maps.newConcurrentMap();
    private static final Map<String, Account> cache = Maps.newConcurrentMap();

    public static AccountRepository getInstance() {
        return instance;
    }

    public void saveToFile(@NonNull Account account) {
        try (PrintWriter printWriter = new PrintWriter(DATA_DIR + account.getAccountNumber())) {
            printWriter.print(gson.toJson(account));
        } catch (FileNotFoundException e) {
            LOGGER.error("Save to file failed", e);
            cache.remove(account.getAccountNumber());
        }
    }

    public Optional<Account> loadToMemory(String accountNumber) {
        Optional<Account> account = loadToMemoryWithoutCaching(accountNumber);
        account.ifPresent(a -> cache.put(accountNumber, a));
        return account;
    }

    private Optional<Account> loadToMemoryWithoutCaching(String accountNumber) {
        if (!registeredAccounts.containsKey(accountNumber)) {
            return Optional.empty();
        }

        if (cache.containsKey(accountNumber)) {
            return Optional.of(cache.get(accountNumber));
        }

        Optional<Account> loadedAccount = tryToLoadFromFile(accountNumber);

        if (loadedAccount.isPresent()) {
            return loadedAccount;
        } else {
            return Optional.of(registeredAccounts.get(accountNumber).createAccount(accountNumber));
        }
    }

    private Optional<Account> tryToLoadFromFile(String accountNumber) {
        try {
            SilverAccount account = gson.fromJson(new Scanner(new File(DATA_DIR + accountNumber))
                    .useDelimiter(NOT_USED_DELIMITER).next(), SilverAccount.class);
            return Optional.of(account);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Account> get(String accountNumber) {
        if (!registeredAccounts.containsKey(accountNumber)) {
            return Optional.empty();
        }

        if (cache.containsKey(accountNumber)) {
            return Optional.of(cache.get(accountNumber));
        }

        return loadToMemoryWithoutCaching(accountNumber).map((account) -> {
            if (account instanceof SilverAccount) {
                return new AccountInFile((SilverAccount) account);
            }
            return account;
        });
    }

    public void register(@NonNull String accountNumber, Account.Type type) {
        registeredAccounts.put(accountNumber, type);
    }

    public boolean contains(@NonNull String accountNumber) {
        return registeredAccounts.containsKey(accountNumber);
    }

    public void unregister(@NonNull String accountNumber) throws NoSuchAccountException {
        if (!contains(accountNumber)) {
            throw new NoSuchAccountException();
        }

        registeredAccounts.remove(accountNumber);
    }

    public Account.Type getAccountType(String accountID) throws NoSuchAccountException {
        if (!contains(accountID)) {
            throw new NoSuchAccountException();
        }
        return registeredAccounts.get(accountID);
    }

    /**
     * This class is not intended to normal use. It's only purpose is be a receiver of transfer!
     */
    @RequiredArgsConstructor
    private static class AccountInFile implements Account {
        private final SilverAccount account;

        private boolean used = false;

        @Override
        public int getBalance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAccountNumber() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MoneyTransferBuilder transfer(int amount) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void increase(int amount) {
            if (used) {
                throw new UnsupportedOperationException("Object has been already used");
            }
            account.increase(amount);
            used = true;
            saveToFile();
        }

        @Override
        public void decrease(int amount) {
            if (used) {
                throw new UnsupportedOperationException("Object has been already used");
            }
            account.decrease(amount);
            used = true;
            saveToFile();
        }

        private void saveToFile() {
            try (PrintWriter pw = new PrintWriter(DATA_DIR + account.getAccountNumber())) {
                Gson gson = new Gson();
                pw.print(gson.toJson(account));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
