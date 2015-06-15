package pl.edu.agh.ki.dsrg.rmi.tictactoe.board;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.MoveRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.PlayerRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Michał Ciołczyk
 */
public interface Board extends Remote, Serializable {
    void makeMove(Player player, Move move) throws RemoteException, PlayerRejectedException, MoveRejectedException;

    boolean isMovePossible(Move move) throws RemoteException;
}
