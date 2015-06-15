package pl.edu.agh.ki.dsrg.rmi.tictactoe.board;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Michał Ciołczyk
 */
public class MoveImpl extends UnicastRemoteObject implements Move {
    private final int move;

    public MoveImpl(int move) throws RemoteException {
        super();
        this.move = move;
    }

    @Override
    public int getField() throws RemoteException {
        return move;
    }
}
