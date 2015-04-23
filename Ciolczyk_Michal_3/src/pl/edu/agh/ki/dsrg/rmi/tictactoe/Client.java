package pl.edu.agh.ki.dsrg.rmi.tictactoe;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.EnemyType;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBroker;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.HumanPlayer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Michał Ciołczyk
 */
public class Client {
    public static void main(String[] args) {
        if (args.length < 4) {
            usage();
            return;
        }
        String host = args[0];
        String port = args[1];
        String nick = args[2];
        String enemyTypeString = args[3];
        EnemyType enemyType;

        switch (enemyTypeString) {
            case "human":
                enemyType = EnemyType.HUMAN;
                break;
            case "computer":
                enemyType = EnemyType.COMPUTER;
                break;
            default:
                usage();
                return;
        }

        try {
            HumanPlayer player = new HumanPlayer(nick);
            BoardBroker boardBroker = (BoardBroker) Naming.lookup("rmi://" + host + ":" + port + "/broker");
            System.out.println("Connecting to server...");
            boardBroker.registerPlayer(player, enemyType);
            System.out.println("Connected. Please wait for your game...");
            player.waitForFinish();
            boardBroker.unregisterPlayer(player);
        } catch (RemoteException | MalformedURLException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("Usage: java Client <HOST> <PORT> <NICK> <ENEMY TYPE: human|computer>");
    }
}
