package pl.edu.agh.ki.dsrg.rmi.tictactoe.board;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.MoveRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions.PlayerRejectedException;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Michał Ciołczyk
 */
public class BoardImpl extends UnicastRemoteObject implements Board {
    private static final int FIELDS_NUM = 9;
    private static final int FIELDS_ROW = 3;
    private final Player player1;
    private final Player player2;
    private final Object synchronizer = new Object();
    private int currentPlayer;
    private boolean isGameFinished = false;
    private String[] fields = new String[FIELDS_NUM];

    public BoardImpl(Player player1, Player player2, int firstPlayer) throws RemoteException {
        super();
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = firstPlayer;
        for (int i = 0; i < FIELDS_NUM; i++) {
            fields[i] = "";
        }
    }

    @Override
    public void makeMove(Player player, Move move)
            throws RemoteException, PlayerRejectedException, MoveRejectedException {
        if (!player.equals(player1) && !player.equals(player2)) {
            throw new PlayerRejectedException();
        }

        switch (currentPlayer) {
            case 0:
                if (player.equals(player2)) {
                    throw new PlayerRejectedException();
                }
                break;
            case 1:
                if (player.equals(player1)) {
                    throw new PlayerRejectedException();
                }
                break;
        }

        if (!isMovePossible(move)) {
            throw new MoveRejectedException();
        }

        fields[move.getField()] = player.getNick();

        if (isFinished()) {
            switch (currentPlayer) {
                case 0:
                    player1.onWin();
                    player2.onLose(move);
                    break;
                case 1:
                    player2.onWin();
                    player1.onLose(move);
                    break;
            }

            synchronized (synchronizer) {
                isGameFinished = true;
                synchronizer.notifyAll();
            }

            return;
        }

        switch (currentPlayer) {
            case 0:
                currentPlayer = 1 - currentPlayer;
                player2.onEnemyMove(this, move);
                break;
            case 1:
                currentPlayer = 1 - currentPlayer;
                player1.onEnemyMove(this, move);
                break;
        }
    }

    private boolean isFinished() {
        //ROWS
        for (int i = 0; i < FIELDS_ROW; i++) {
            String player = fields[i * FIELDS_ROW];
            boolean finished = true;
            for (int j = 1; j < FIELDS_ROW; j++) {
                if (!player.equals(fields[i * FIELDS_ROW + j])) {
                    finished = false;
                    break;
                }
            }

            if (finished && !player.equals("")) {
                return true;
            }
        }

        //COLS
        for (int i = 0; i < FIELDS_ROW; i++) {
            String player = fields[i];
            boolean finished = true;
            for (int j = 1; j < FIELDS_ROW; j++) {
                if (!player.equals(fields[j * FIELDS_ROW + i])) {
                    finished = false;
                    break;
                }
            }

            if (finished && !player.equals("")) {
                return true;
            }
        }

        //DIAGONALS
        //TOP-LEFT - BOTTOM-RIGHT
        String player = fields[0];
        boolean finished = true;
        for (int i = 1; i < FIELDS_ROW; i++) {
            if (!player.equals(fields[i * FIELDS_ROW + i])) {
                finished = false;
                break;
            }
        }

        if (finished && !player.equals("")) {
            return true;
        }

        //TOP-RIGHT - BOTTOM-LEFT
        player = fields[FIELDS_ROW];
        finished = true;
        for (int i = 1; i < FIELDS_ROW; i++) {
            if (!player.equals(fields[(i + 1) * FIELDS_ROW - i])) {
                finished = false;
                break;
            }
        }

        //noinspection RedundantIfStatement
        if (finished && !player.equals("")) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isMovePossible(Move move) throws RemoteException {
        int field = move.getField();

        //noinspection SimplifiableIfStatement
        if (field < 0 || field >= FIELDS_NUM) {
            return false;
        }

        return "".equals(fields[field]);
    }

    public void waitForFinish() throws InterruptedException {
        synchronized (synchronizer) {
            while (!isGameFinished) {
                synchronizer.wait();
            }
        }
    }
}
