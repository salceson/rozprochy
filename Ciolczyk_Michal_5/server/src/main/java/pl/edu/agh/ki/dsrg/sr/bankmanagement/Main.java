package pl.edu.agh.ki.dsrg.sr.bankmanagement;

import Ice.Communicator;

import static Ice.Util.initialize;

/**
 * @author Michał Ciołczyk
 */
public class Main {
    public static void main(String[] args) {
        Communicator communicator;

        communicator = initialize();

        communicator.waitForShutdown();
    }
}
