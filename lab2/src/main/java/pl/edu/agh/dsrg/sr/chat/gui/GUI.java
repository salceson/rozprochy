package pl.edu.agh.dsrg.sr.chat.gui;

/**
 * @author Michał Ciołczyk
 */
public interface GUI {
    void addChannel(String channelName);

    void joinChannel(String channelName);

    void leaveChannel(String channelName);

    void putMessage(String channelName, String nick, String text);

    void addUser(String channelName, String nick);

    void removeUser(String channelName, String nick);

    void removeChannel(String channelName);
}
