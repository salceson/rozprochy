package pl.edu.agh.dsrg.sr.chat.service;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;
import pl.edu.agh.dsrg.sr.chat.gui.GUI;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatMessage;
import pl.edu.agh.dsrg.sr.chat.service.adapters.ChannelReceiver;
import pl.edu.agh.dsrg.sr.chat.service.adapters.ManagementReceiverAdapter;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;

/**
 * @author Michał Ciołczyk
 */
public class ChatServiceImpl implements ChatService {
    private String nick;
    private GUI gui;
    private JChannel managementChannel;
    private Map<String, List<String>> channelUsers;
    private Map<String, JChannel> channels;

    private static final String MANAGEMENT_CHANNEL_NAME = "ChatManagement768624";

    public ChatServiceImpl() {
        System.out.println("Chat Service started");
        channelUsers = new HashMap<>();
        channels = new HashMap<>();
    }

    @Override
    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void joinManagementChnanel() throws Exception {
        managementChannel = new JChannel(false);

        managementChannel.setName(nick);

        ProtocolStack protocolStack = new ProtocolStack();
        managementChannel.setProtocolStack(protocolStack);
        populateProtocolStack(protocolStack);

        managementChannel.setReceiver(new ManagementReceiverAdapter(
                channelUsers, gui, managementChannel
        ));

        managementChannel.connect(MANAGEMENT_CHANNEL_NAME);
        managementChannel.getState(null, 10000);
    }

    private void populateProtocolStack(ProtocolStack protocolStack) throws Exception {
        populateProtocolStack(protocolStack, null);
    }

    private void populateProtocolStack(ProtocolStack protocolStack, InetAddress address)
            throws Exception {
        Protocol udp = new UDP();
        if (address != null) {
            udp.setValue("mcast_group_addr", address);
        }

        protocolStack.addProtocol(udp)
                .addProtocol(new PING())
                .addProtocol(new MERGE2())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK())
                .addProtocol(new UNICAST2())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FLUSH())
                .init();
    }

    @Override
    public void leaveManagementChannel() {
        managementChannel.close();
    }

    @Override
    public void joinChannel(String name) throws Exception {
        if (channels.containsKey(name)) {
            channels.get(name);
            System.out.println("Switched channel: " + name);
            return;
        }

        InetAddress address = InetAddress.getByName(name);

        if (!address.isMulticastAddress()) {
            throw new Exception(address + "is not a multicast address!");
        }
        JChannel channel = new JChannel(false);
        channel.setName(nick);

        ProtocolStack protocolStack = new ProtocolStack();
        channel.setProtocolStack(protocolStack);

        populateProtocolStack(protocolStack, address);

        channel.setReceiver(new ChannelReceiver(channel, gui));
        channel.connect(name);

        channels.put(name, channel);

        ChatAction chatAction = ChatAction.newBuilder()
                .setAction(ChatAction.ActionType.JOIN)
                .setNickname(nick)
                .setChannel(name)
                .build();

        Message msg = new Message(null, null, chatAction.toByteArray());

        if (!channelUsers.containsKey(name)) {
            channelUsers.put(name, new LinkedList<String>());
            gui.addChannel(name);
        }

        gui.joinChannel(name);
        managementChannel.send(msg);
    }

    @Override
    public void leaveChannel(String name) throws Exception {
        if (!channels.containsKey(name)) {
            return;
        }

        JChannel channel = channels.get(name);
        channels.remove(name);

        gui.leaveChannel(name);

        ChatAction action = ChatAction.newBuilder()
                .setAction(ChatAction.ActionType.LEAVE)
                .setNickname(nick)
                .setChannel(name)
                .build();

        managementChannel.send(new Message(null, null, action.toByteArray()));

        channel.close();
    }

    @Override
    public void sendMessage(String channelName, String message) throws Exception {
        if (!channels.containsKey(channelName)) {
            return;
        }

        JChannel channel = channels.get(channelName);

        ChatMessage msg = ChatMessage.newBuilder()
                .setMessage(message)
                .build();

        channel.send(new Message(null, null, msg.toByteArray()));
    }
}
