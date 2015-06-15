package pl.edu.agh.ki.dsrg.rmi.tictactoe.exceptions;

/**
 * @author Michał Ciołczyk
 */
public class PlayerRejectedException extends RuntimeException {
    public PlayerRejectedException() {
        super("Player has been rejected by the server.");
    }
}
