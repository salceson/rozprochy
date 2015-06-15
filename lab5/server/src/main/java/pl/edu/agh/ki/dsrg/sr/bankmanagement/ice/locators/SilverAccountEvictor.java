package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.locators;

import Ice.*;
import Ice.Object;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.Account;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.AccountRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants.AccountServant;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class SilverAccountEvictor implements ServantLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SilverAccountEvictor.class);

    private final int limit;

    private final Map<String, AccountServant> servants = Maps.newConcurrentMap();
    private final Queue<String> lastAccessedAccounts = Queues.newConcurrentLinkedQueue();

    private final AccountRepository accountRepository = AccountRepository.getInstance();

    @Override
    public Ice.Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        LOGGER.info("Locating servant for id: " + current.id.name);

        if (servants.containsKey(current.id.name)) {
            return servants.get(current.id.name);
        }

        if (lastAccessedAccounts.size() >= limit) {
            evictLastServant();
        }

        return loadServant(current.id.name);
    }

    @Override
    public void deactivate(String s) {
        LOGGER.info("Deactivating: " + s);
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {
        LOGGER.info("Finishing: " + current.id.name);
    }

    private void evictLastServant() {
        String lastAccessedId = lastAccessedAccounts.poll();

        LOGGER.info("Evicting: " + lastAccessedId);

        AccountServant lastAccessedServant = servants.get(lastAccessedId);
        accountRepository.saveToFile(lastAccessedServant.getAccount());

        servants.remove(lastAccessedId);
    }

    private Object loadServant(String accountNumber) {
        Optional<Account> accountOptional = loadAccount(accountNumber);

        Optional<Object> servantOptional = accountOptional.map(account -> {
            AccountServant servant = AccountServant.create(account);
            servants.put(accountNumber, servant);
            lastAccessedAccounts.add(accountNumber);
            return servant;
        });

        if (servantOptional.isPresent()) {
            return servantOptional.get();
        }

        return null;
    }

    private Optional<Account> loadAccount(String accountNumber) {
        LOGGER.info("Loading: " + accountNumber);
        return accountRepository.loadToMemory(accountNumber);
    }
}
