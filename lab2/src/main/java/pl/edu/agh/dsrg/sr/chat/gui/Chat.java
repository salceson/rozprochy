package pl.edu.agh.dsrg.sr.chat.gui;

import pl.edu.agh.dsrg.sr.chat.service.ChatService;
import pl.edu.agh.dsrg.sr.chat.service.ChatServiceImpl;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michał Ciołczyk
 */
public class Chat extends JFrame implements GUI {
    private JTabbedPane tabsPane;
    private Map<String, ChannelPanel> tabs;
    private JoinPanel joinPanel;
    private ChatService chatService;

    public Chat() throws Exception {
        super("Chat");

        chatService = new ChatServiceImpl();
        chatService.setGUI(this);
        chatService.setNick(promptForNick());

        tabs = new HashMap<>();

        createGUI();

        chatService.joinManagementChnanel();
    }

    private String promptForNick() throws Exception {
        String nick = "";

        while (nick.length() <= 0) {
            nick = JOptionPane.showInputDialog(
                    this, "Enter nick", "Nick", JOptionPane.QUESTION_MESSAGE
            );

            if (nick == null) {
                System.exit(1);
            }
        }

        return nick;
    }

    private void createGUI() {
        tabsPane = new JTabbedPane();
        add(tabsPane);

        joinPanel = new JoinPanel(chatService);
        tabsPane.add("Join", joinPanel);

        pack();
        setSize(300, 400);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                chatService.leaveManagementChannel();
                try {
                    Thread.sleep(5000);
                    System.exit(0);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    @Override
    public void addChannel(String channelName) {
        ChannelPanel panel = new ChannelPanel(chatService, channelName);
        tabs.put(channelName, panel);
        joinPanel.addChannel(channelName);
    }

    @Override
    public void joinChannel(String channelName) {
        ChannelPanel panel = tabs.get(channelName);
        tabsPane.add(panel, channelName);
        tabsPane.setSelectedComponent(panel);
    }

    @Override
    public void leaveChannel(String channelName) {
        ChannelPanel panel = tabs.get(channelName);
        panel.clear();
        tabsPane.setSelectedIndex(0);
        tabsPane.remove(panel);
    }

    @Override
    public void putMessage(String channelName, String nick, String text) {
        ChannelPanel panel = tabs.get(channelName);

        if (panel.isDisplayable()) {
            panel.append(String.format("<%s> %s\n", nick, text));
        }
    }

    @Override
    public void addUser(String channelName, String nick) {
        ChannelPanel panel = tabs.get(channelName);
        panel.addUser(nick);

        if (panel.isDisplayable()) {
            panel.append(String.format("* %s joined channel.\n", nick));
        }
    }

    @Override
    public void removeUser(String channelName, String nick) {
        ChannelPanel panel = tabs.get(channelName);
        panel.removeUser(nick);

        if (panel.isDisplayable()) {
            panel.append(String.format("* %s left channel.\n", nick));
        }
    }

    @Override
    public void removeChannel(String channelName) {
        tabs.remove(channelName);
        joinPanel.removeChannel(channelName);
    }
}
