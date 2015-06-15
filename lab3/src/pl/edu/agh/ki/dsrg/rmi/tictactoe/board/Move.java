package pl.edu.agh.ki.dsrg.rmi.tictactoe.board;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Michał Ciołczyk
 */
public interface Move extends Remote, Serializable {
    int getField() throws RemoteException;
}
