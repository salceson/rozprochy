package pl.edu.agh.ki.dsrg.rmi.tictactoe.player;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Board;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Move;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.MoveImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * @author Michał Ciołczyk
 */
public class HumanPlayer extends UnicastRemoteObject implements Player {
    private static final int FIELDS_NUM = 9;
    private static final int FIELDS_ROW = 3;
    private static final String SEPARATOR_H = "|";
    private static final String SEPARATOR_V = "---";
    private static final String SEPARATOR_VH = "+";
    private static final String SEPARATOR_PLACEHOLDER = "   ";
    private final String nick;
    private final Object synchronizer = new Object();
    private final Scanner scanner = new Scanner(System.in);
    private boolean isFinished = false;
    private String[] fields = new String[FIELDS_NUM];
    private String symbol = "   ";
    private String enemySymbol = "   ";

    public HumanPlayer(String nick) throws RemoteException {
        super();
        this.nick = nick;
        for (int i = 0; i < FIELDS_NUM; i++) {
            fields[i] = " " + i + " ";
        }
    }

    @Override
    public String getNick() throws RemoteException {
        return nick;
    }

    @Override
    public void onGameStarted(Board board, boolean hasFirstMove) throws RemoteException {
        System.out.println("Game started!");
        symbol = hasFirstMove ? " O " : " X ";
        enemySymbol = hasFirstMove ? " X " : " O ";
        if (hasFirstMove) {
            Move move;
            do {
                move = getNextMove();
            } while (!board.isMovePossible(move));
            fields[move.getField()] = symbol;
            printBoard();
            board.makeMove(this, move);
        }
    }

    private Move getNextMove() throws RemoteException {
        printBoard();
        System.out.println("You're" + symbol);
        System.out.print("Enter your choice: ");
        try {
            int f = scanner.nextInt();
            return new MoveImpl(f);
        } catch (Exception e) {
            return new MoveImpl(-1);
        }
    }

    @Override
    public void onEnemyMove(Board board, Move move) throws RemoteException {
        fields[move.getField()] = enemySymbol;
        printBoard();
        Move myMove;
        do {
            myMove = getNextMove();
        } while (!board.isMovePossible(myMove));
        fields[myMove.getField()] = symbol;
        printBoard();
        board.makeMove(this, myMove);
    }

    @Override
    public void onWin() throws RemoteException {
        System.out.println("You won!");
        synchronized (synchronizer) {
            isFinished = true;
            synchronizer.notifyAll();
        }
    }

    @Override
    public void onLose(Move move) throws RemoteException {
        printBoard();
        System.out.println("You lost!");
        synchronized (synchronizer) {
            isFinished = true;
            synchronizer.notifyAll();
        }
    }

    private void printBoard() {
        for (int i = 0; i < FIELDS_ROW; i++) {
            if (i != 0) {
                for (int j = 0; j < FIELDS_ROW; j++) {
                    System.out.print(SEPARATOR_V);
                    if (j != FIELDS_ROW - 1) {
                        System.out.print(SEPARATOR_VH);
                    }
                }
            }
            System.out.println();
            for (int j = 0; j < FIELDS_ROW; j++) {
                System.out.print(SEPARATOR_PLACEHOLDER);
                if (j != FIELDS_ROW - 1) {
                    System.out.print(SEPARATOR_H);
                }
            }
            System.out.println();
            for (int j = 0; j < FIELDS_ROW; j++) {
                System.out.print(fields[i * FIELDS_ROW + j]);
                if (j != FIELDS_ROW - 1) {
                    System.out.print(SEPARATOR_H);
                }
            }
            System.out.println();
            for (int j = 0; j < FIELDS_ROW; j++) {
                System.out.print(SEPARATOR_PLACEHOLDER);
                if (j != FIELDS_ROW - 1) {
                    System.out.print(SEPARATOR_H);
                }
            }
            System.out.println();
        }
    }

    public void waitForFinish() throws InterruptedException {
        synchronized (synchronizer) {
            while (!isFinished) {
                synchronizer.wait();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HumanPlayer player = (HumanPlayer) o;

        return nick.equals(player.nick);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + nick.hashCode();
        return result;
    }
}
