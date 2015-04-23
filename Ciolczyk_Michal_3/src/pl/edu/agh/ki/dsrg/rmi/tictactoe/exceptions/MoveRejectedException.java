package pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions;

/**
 * @author Michał Ciołczyk
 */
public class MoveRejectedException extends RuntimeException {
    public MoveRejectedException() {
        super("Move has been rejected by the server.");
    }
}
