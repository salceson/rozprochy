package pl.edu.agh.ki.dsrg.rmi.tictactoe;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBroker;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBrokerImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Michał Ciołczyk
 */
public class Server {
    static BoardBroker boardBroker;

    public static void main(String[] args) {
        try {
            boardBroker = new BoardBrokerImpl();
            UnicastRemoteObject.exportObject(boardBroker, 0);
            Naming.rebind("broker", boardBroker);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
