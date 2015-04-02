package pl.edu.agh.dsrg.sr.chat.gui;

import pl.edu.agh.dsrg.sr.chat.service.ChatService;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * @author Michał Ciołczyk
 */
public class ChannelPanel extends JPanel {
    private final String channelName;
    private final ChatService chatService;
    private JTextArea chatTranscript = new JTextArea();
    private DefaultListModel<String> usersModel = new DefaultListModel<>();

    public ChannelPanel(ChatService chatService, String channelName) {
        super(new GridLayout(3, 1));

        this.chatService = chatService;
        this.channelName = channelName;

        createGUI();
    }

    private void createGUI() {
        JPanel chatPanel = new JPanel(new GridLayout(1, 2));
        JPanel sendPanel = new JPanel(new GridLayout(1, 2));

        JScrollPane scrollTranscript = new JScrollPane();
        JScrollPane scrollUsers = new JScrollPane();

        chatPanel.add(scrollTranscript);
        chatPanel.add(scrollUsers);
        add(chatPanel);

        JList<String> users = new JList<>(usersModel);

        chatTranscript.setWrapStyleWord(true);
        chatTranscript.setLineWrap(true);
        chatTranscript.setEditable(false);
        chatTranscript.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        DefaultCaret caret = (DefaultCaret) chatTranscript.getCaret();
        caret.setUpdatePolicy(Rectangle2D.OUT_BOTTOM);

        scrollTranscript.setViewportView(chatTranscript);
        scrollUsers.setViewportView(users);

        JButton sendButton = new JButton("Send");
        final JTextField sendText = new JTextField();

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = sendText.getText();

                if(message == null || message.isEmpty()) {
                    return;
                }

                try {
                    chatService.sendMessage(channelName, message);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(ChannelPanel.this,
                            String.format("%s: %s", e1.getClass().getName(),
                                    e1.getMessage()),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        sendButton.addActionListener(actionListener);
        sendText.addActionListener(actionListener);

        sendPanel.add(sendText);
        sendPanel.add(sendButton);

        add(sendPanel);

        JButton leaveButton = new JButton("Leave channel");
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chatService.leaveChannel(channelName);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(ChannelPanel.this,
                            String.format("%s: %s", e1.getClass().getName(),
                                    e1.getMessage()),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        add(leaveButton);
    }

    public void clear() {
        chatTranscript.setText("");
    }

    public void append(String line) {
        chatTranscript.append(line);
    }

    public void addUser(String nick) {
        usersModel.addElement(nick);
    }

    public void removeUser(String nick) {
        usersModel.removeElement(nick);
    }
}
