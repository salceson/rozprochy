import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

public class Zad1Server {
    private ServerSocket serverSocket;

    public Zad1Server() {
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Random random = new Random();
        try {
            while(true) {
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                byte[] receive = new byte[8];
                for(int i=0; i<8; i++)
                    receive[i] = 0;

                int receivedBytes = in.read(receive);

                System.out.print("Received bytes: ");
                System.out.println(receivedBytes);
                System.out.print("Received data: ");

                ByteBuffer byteBuffer = ByteBuffer.wrap(receive);

                switch (receivedBytes) {
                    case 1:
                        System.out.println(byteBuffer.get(0));
                        break;
                    case 2:
                        System.out.println(byteBuffer.getShort());
                        break;
                    case 4:
                        System.out.println(byteBuffer.getInt());
                        break;
                    case 8:
                        System.out.println(byteBuffer.getLong());
                        break;
                }

                byte[] send = ByteBuffer.allocate(1).put(Integer.valueOf(random.nextInt(10)).byteValue()).array();

                out.write(send);
                out.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public static void main(String[] args) {
        Zad1Server zad1Server = new Zad1Server();
        zad1Server.run();
    }
}