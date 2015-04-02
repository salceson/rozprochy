package pl.edu.agh.dsrg.sr.chat.service;

import pl.edu.agh.dsrg.sr.chat.gui.GUI;

/**
 * @author Michał Ciołczyk
 */
public interface ChatService {
    void setNick(String nick);

    void setGUI(GUI gui);

    void joinManagementChnanel() throws Exception;

    void leaveManagementChannel();

    void joinChannel(String name) throws Exception;

    void leaveChannel(String name) throws Exception;

    void sendMessage(String channelName, String message) throws Exception;
}
