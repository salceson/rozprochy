import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.CRC32;

public class Zad3 {
    public static final int PORT = 6666;
    public static final int CHECKSUM_SIZE = 8;
    public static final int USERNAME_SIZE = 6;
    public static final int MESSAGE_SIZE = 20;
    public static final int DATE_SIZE = 8;
    public static final int PACKET_SIZE_WITHOUT_CHECKSUM = USERNAME_SIZE + MESSAGE_SIZE + DATE_SIZE;
    public static final int PACKET_SIZE = PACKET_SIZE_WITHOUT_CHECKSUM + CHECKSUM_SIZE;
    public static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static final String MULTICAST_ADDRESS = "224.224.224.224";

    public static void main(String[] args) {
        System.out.println("Zad3\n(C) 2015 Michał Ciołczyk\n\n");
        if (args.length != 1) {
            System.out.println("Usage: java Zad3 <NICK>");
            return;
        }

        String name = args[0];
        byte[] nameBytes = name.getBytes(StandardCharsets.US_ASCII);

        MulticastSocket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            InetAddress addr = InetAddress.getByName(MULTICAST_ADDRESS);

            socket = new MulticastSocket(PORT);
            socket.joinGroup(addr);

            new Receiver(socket, name).start();

            while (true) {
                byte[] packetBuffer = new byte[PACKET_SIZE_WITHOUT_CHECKSUM];

                System.out.print(">>> ");

                String line = scanner.nextLine();
                byte[] lineBytes = line.getBytes(StandardCharsets.US_ASCII);

                //Send name
                System.arraycopy(nameBytes, 0, packetBuffer, 0, Math.min(USERNAME_SIZE, nameBytes.length));
                //Send message
                System.arraycopy(lineBytes, 0, packetBuffer, USERNAME_SIZE, Math.min(MESSAGE_SIZE, lineBytes.length));
                //Send date
                System.arraycopy(dateFormat.format(new Date()).getBytes(StandardCharsets.US_ASCII),
                        0, packetBuffer, USERNAME_SIZE + MESSAGE_SIZE, DATE_SIZE);

                //Get packet bytes
                byte[] toSend = ByteBuffer.allocate(PACKET_SIZE).put(packetBuffer)
                        .putLong(calculateChecksum(packetBuffer)).array();

                //Send all data + checksum
                socket.send(new DatagramPacket(toSend,PACKET_SIZE, addr, PORT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long calculateChecksum(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    public static boolean validateChecksum(byte[] packet) {
        byte[] checksumBytes = new byte[CHECKSUM_SIZE];
        byte[] packetWithoutChecksum = new byte[PACKET_SIZE_WITHOUT_CHECKSUM];

        System.arraycopy(packet, 0, packetWithoutChecksum, 0, PACKET_SIZE_WITHOUT_CHECKSUM);
        System.arraycopy(packet, PACKET_SIZE_WITHOUT_CHECKSUM, checksumBytes, 0, CHECKSUM_SIZE);

        long checksum = ByteBuffer.wrap(checksumBytes).getLong();

        return checksum == calculateChecksum(packetWithoutChecksum);
    }

    public static class Receiver extends Thread {
        private MulticastSocket socket;
        private String name;

        public Receiver(MulticastSocket socket, String name) {
            this.socket = socket;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[PACKET_SIZE];
                while (true) {
                    byte[] data = new byte[PACKET_SIZE];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, PACKET_SIZE);
                    socket.receive(datagramPacket);

                    System.arraycopy(datagramPacket.getData(), 0, data, 0, PACKET_SIZE);

                    if (validateChecksum(data)) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                        //Receive name
                        String userName = getStringWithSpecifiedLength(byteBuffer, USERNAME_SIZE);
                        //If it was send by the current user, skip it
                        if (name.equals(userName)) {
                            continue;
                        }
                        //Receive message
                        String message = getStringWithSpecifiedLength(byteBuffer, MESSAGE_SIZE);
                        //Receive date
                        String date = getStringWithSpecifiedLength(byteBuffer, DATE_SIZE);

                        //Display message
                        System.out.println(String.format("\r%s[%s]: %s", userName, date, message));
                    } else {
                        System.out.println("\r[ERROR] Got message with invalid checksum!");
                    }

                    System.out.print(">>> ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getStringWithSpecifiedLength(ByteBuffer byteBuffer, int length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                byte b = byteBuffer.get();
                if (b != 0) {
                    sb.append((char) b);
                }
            }
            return sb.toString();
        }
    }
}