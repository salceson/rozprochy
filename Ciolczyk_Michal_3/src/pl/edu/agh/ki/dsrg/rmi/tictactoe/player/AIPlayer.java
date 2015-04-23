package pl.edu.agh.ki.dsrg.rmi.tictactoe.player;

import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Board;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.Move;
import pl.edu.agh.ki.dsrg.rmi.tictactoe.board.MoveImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * @author Michał Ciołczyk
 */
public class AIPlayer extends UnicastRemoteObject implements Player {
    public static final String COMPUTER_NAME = "Computer";
    private static final int FIELDS_NUM = 9;
    private final Random random = new Random();

    public AIPlayer() throws RemoteException {
        super();
    }

    @Override
    public String getNick() throws RemoteException {
        return COMPUTER_NAME;
    }

    @Override
    public void onGameStarted(Board board, boolean hasFirstMove) throws RemoteException {
        if (hasFirstMove) {
            board.makeMove(this, new MoveImpl(random.nextInt(FIELDS_NUM)));
        }
    }

    @Override
    public void onEnemyMove(Board board, Move move) throws RemoteException {
        Move myMove;
        try {
            do {
                myMove = new MoveImpl(random.nextInt(FIELDS_NUM));
            } while (!board.isMovePossible(myMove));
            board.makeMove(this, myMove);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWin() throws RemoteException {
        System.out.println("Computer wins. Hell yeah!");
    }

    @Override
    public void onLose(Move move) throws RemoteException {
        System.out.println("Computer lost. What a shame!");
    }
}
