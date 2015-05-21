package pl.edu.agh.ki.dsrg.sr.bankmanagement;

import Ice.Communicator;
import Ice.ObjectAdapter;
import Ice.ServantLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.BankManager;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.locators.SilverAccountEvictor;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants.BankManagerServant;

import static Ice.Util.initialize;

/**
 * @author Michał Ciołczyk
 */
public class Main {
    private static final String ACCOUNT_CATEGORY = "acc";
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Communicator communicator;

        communicator = initialize(args);
        LOGGER.info("Communicator initialized");

        ObjectAdapter objectAdapter = communicator.createObjectAdapter("BankManager");

        int evictorLimit = communicator.getProperties().getPropertyAsIntWithDefault("Evictor.Limit", 2);
        ServantLocator silverAccountEvictor = new SilverAccountEvictor(evictorLimit);
        objectAdapter.addServantLocator(silverAccountEvictor, ACCOUNT_CATEGORY);
        LOGGER.info("Evictor added: " + silverAccountEvictor);

        BankManager bankManager = new BankManager();
        Bank.BankManager bankManagerServant = new BankManagerServant(bankManager);

        objectAdapter.addDefaultServant(bankManagerServant, "common");

        objectAdapter.activate();

        LOGGER.info("Activated object adapter");
        LOGGER.info("Started server");

        communicator.waitForShutdown();
    }
}
