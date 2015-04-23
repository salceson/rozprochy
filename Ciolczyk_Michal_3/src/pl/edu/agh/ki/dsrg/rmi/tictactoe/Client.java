package pl.edu.agh.ki.dsrg.rmi.tictactoe;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.EnemyType;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.broker.BoardBroker;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.HumanPlayer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            System.out.println("Connecting to server...");
            HumanPlayer player = new HumanPlayer(nick);
            Registry registry = LocateRegistry.getRegistry(host, Integer.parseInt(port));
            BoardBroker boardBroker = (BoardBroker) registry.lookup("broker");
            System.out.println("Connected. Please wait for your game...");
            boardBroker.registerPlayer(player, enemyType);
            player.waitForFinish();
            Thread.sleep(500);
            boardBroker.unregisterPlayer(player);
            System.exit(0);
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("Usage: java Client <HOST> <PORT> <NICK> <ENEMY TYPE: human|computer>");
    }
}
