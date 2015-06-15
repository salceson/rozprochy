package pl.edu.agh.dsrg.sr.chat;

import pl.edu.agh.dsrg.sr.chat.gui.Chat;

/**
 * @author Michał Ciołczyk
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Chat chat = new Chat();
    }
}
