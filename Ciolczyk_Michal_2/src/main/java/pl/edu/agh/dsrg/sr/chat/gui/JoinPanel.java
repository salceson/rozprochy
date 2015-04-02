package pl.edu.agh.dsrg.sr.chat.gui;

import pl.edu.agh.dsrg.sr.chat.service.ChatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Michał Ciołczyk
 */
public class JoinPanel extends JPanel {
    private final ChatService chatService;
    private JComboBox<String> channelInput;
    private DefaultComboBoxModel<String> channelsModel = new DefaultComboBoxModel<>();

    public JoinPanel(ChatService service) {
        super(new GridLayout(1, 2));
        chatService = service;

        createGUI();
    }

    private void createGUI() {
        channelInput = new JComboBox<>(channelsModel);
        channelInput.setEditable(true);
        channelInput.setSelectedItem("");

        JButton joinButton = new JButton("Join");

        add(channelInput);
        add(joinButton);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if (!"Join".equals(actionCommand)) {
                    return;
                }

                final String channelName = (String) channelInput.getSelectedItem();

                if (channelName == null || channelName.isEmpty()) {
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatService.joinChannel(channelName);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(JoinPanel.this,
                                    String.format("%s: %s", e1.getClass().getName(),
                                            e1.getMessage()),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                        channelInput.setSelectedItem("");
                    }
                }).start();
            }
        };

        channelInput.addActionListener(actionListener);
        joinButton.addActionListener(actionListener);
    }

    public void addChannel(String channelName) {
        channelsModel.addElement(channelName);
    }

    public void removeChannel(String channelName) {
        channelsModel.removeElement(channelName);
    }
}
