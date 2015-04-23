package pl.edu.agh.ki.dsrg.rmi.tictactoe;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBroker;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBrokerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Michał Ciołczyk
 */
public class Server {
    static BoardBroker boardBroker;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Server <VISIBLE-HOST>");
            return;
        }
        System.setProperty("java.rmi.server.hostname", args[0]);
        try {
            boardBroker = new BoardBrokerImpl();
            UnicastRemoteObject.exportObject(boardBroker, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("broker", boardBroker);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
