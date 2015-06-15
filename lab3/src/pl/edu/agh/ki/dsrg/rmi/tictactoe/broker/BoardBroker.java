package pl.edu.agh.ki.dsrg.rmi.tictactoe.broker;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.EnemyType;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.PlayerRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Michał Ciołczyk
 */
public interface BoardBroker extends Remote, Serializable {
    void registerPlayer(Player player, EnemyType enemyType) throws RemoteException, PlayerRejectedException, InterruptedException;

    void unregisterPlayer(Player player) throws RemoteException, PlayerRejectedException;
}
