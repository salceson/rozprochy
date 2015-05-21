package pl.edu.agh.ki.dsrg.sr.bankmanagement;

import FinancialNews.*;
import Ice.Communicator;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
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
    public static final String ACCOUNT_CATEGORY = "acc";
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        Communicator communicator;

        communicator = initialize(args);
        LOGGER.info("Communicator initialized");

        LOGGER.info("Creating server object adapter");

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

        Ice.ObjectPrx proxy = communicator.propertyToProxy("FinancialNews");
        FinancialNewsServerPrx newsServer = FinancialNewsServerPrxHelper
                .checkedCast(proxy);

        Ice.ObjectAdapter clientAdapter = communicator.createObjectAdapter("");
        FinancialNewsReceiver receiver =
                new pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews.FinancialNewsReceiver();
        ObjectPrx objectProxy = clientAdapter.addWithUUID(receiver);
        FinancialNewsReceiverPrx receiverProxy = FinancialNewsReceiverPrxHelper
                .uncheckedCast(objectProxy);

        clientAdapter.activate();

        boolean tryToRegister = true;

        while (true) {
            try {
                if (tryToRegister) {
                    Ice.Connection connection = newsServer.ice_getConnection();
                    connection.setAdapter(clientAdapter);
                    newsServer.registerForNews(receiverProxy);
                    LOGGER.info("Registered for financial notifications");
                    tryToRegister = false;
                } else {
                    newsServer.ice_ping();
                    Thread.sleep(1000);
                }
            } catch (Ice.SocketException | Ice.TimeoutException e) {
                LOGGER.error("Financial notifications: connection error, trying to reconnect...");
                tryToRegister = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LOGGER.error("Thread interrupted: ", e1);
                }
            } catch (InterruptedException e) {
                LOGGER.error("Thread interrupted: ", e);
            }
        }
    }
}
