package pl.edu.agh.ki.dsrg.rmi.tictactoe.broker;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.BoardImpl;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.EnemyType;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.PlayerRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.AIPlayer;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.Player;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Michał Ciołczyk
 */
public class BoardBrokerImpl implements BoardBroker {
    private final Set<Player> waitingPlayers = new HashSet<>();
    private final Set<String> players = new HashSet<>();

    public BoardBrokerImpl() {
        players.add(AIPlayer.COMPUTER_NAME);
    }

    @Override
    public void registerPlayer(Player player, EnemyType enemyType)
            throws RemoteException, PlayerRejectedException, InterruptedException {
        if (players.contains(player.getNick())) {
            throw new PlayerRejectedException();
        }

        System.out.println(player.getNick() + " joined the server.");

        players.add(player.getNick());

        GameThread gameThread = null;

        if (enemyType == EnemyType.COMPUTER) {
            gameThread = new GameThread(player, new AIPlayer());
        } else {
            synchronized (waitingPlayers) {
                if (waitingPlayers.isEmpty()) {
                    waitingPlayers.add(player);
                } else {
                    //noinspection LoopStatementThatDoesntLoop
                    for (Player waitingPlayer : waitingPlayers) {
                        waitingPlayers.remove(waitingPlayer);
                        gameThread = new GameThread(player, waitingPlayer);
                        break;
                    }
                }
            }
        }

        if (gameThread != null) {
            gameThread.start();
            gameThread.join();
        }
    }

    @Override
    public void unregisterPlayer(Player player) throws RemoteException, PlayerRejectedException {
        if (!players.contains(player.getNick())) {
            throw new PlayerRejectedException();
        }

        players.remove(player.getNick());

        System.out.println(player.getNick() + " left the server.");
    }

    private static class GameThread extends Thread {
        private final Player player1, player2;
        private final int firstPlayer;

        public GameThread(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
            firstPlayer = new Random().nextInt(2);
        }

        @Override
        public void run() {
            try {
                String player1Nick = player1.getNick();
                String player2Nick = player2.getNick();

                System.out.println("Starting game between:");
                System.out.println("\t0: " + player1Nick);
                System.out.println("\t1: " + player2Nick);
                System.out.println("\tPlayer " + firstPlayer + " starts!");

                BoardImpl board = new BoardImpl(player1, player2, firstPlayer);

                switch (firstPlayer) {
                    case 0:
                        player2.onGameStarted(board, false);
                        player1.onGameStarted(board, true);
                        break;
                    case 1:
                        player1.onGameStarted(board, false);
                        player2.onGameStarted(board, true);
                        break;
                }

                board.waitForFinish();

                System.out.println("Game between:");
                System.out.println("\t0: " + player1Nick);
                System.out.println("\t1: " + player2Nick);
                System.out.println("finished!");
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
