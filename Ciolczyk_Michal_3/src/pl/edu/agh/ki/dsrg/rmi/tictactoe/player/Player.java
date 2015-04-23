package pl.edu.agh.ki.dsrg.rmi.tictactoe.player;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Board;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Move;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Michał Ciołczyk
 */
public interface Player extends Remote, Serializable {
    String getNick() throws RemoteException;

    void onGameStarted(Board board, boolean hasFirstMove) throws RemoteException;

    void onEnemyMove(Board board, Move move) throws RemoteException;

    void onWin() throws RemoteException;

    void onLose(Move move) throws RemoteException;
}
