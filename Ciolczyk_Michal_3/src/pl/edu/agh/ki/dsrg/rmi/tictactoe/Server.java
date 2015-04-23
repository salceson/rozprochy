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
        if (args.length < 2) {
            System.out.println("Usage: java Server <HOST> <PORT>");
            return;
        }
        String host = args[0];
        String port = args[1];
        try {
            BoardBroker boardBrokerImpl = new BoardBrokerImpl();
            boardBroker = (BoardBroker) UnicastRemoteObject.exportObject(boardBrokerImpl, 0);
            Naming.rebind("rmi://" + host + ":" + port + "/broker", boardBroker);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
