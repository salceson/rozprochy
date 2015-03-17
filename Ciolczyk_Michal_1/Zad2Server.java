import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Zad2Server extends Thread {
    public static final int PORT = 6666;
    public static final int BUFFER_SIZE = 255;
    public static final int THREADS_NUM = 4;
    private static BlockingQueue<Socket> sockets = new ArrayBlockingQueue<Socket>(THREADS_NUM);

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = sockets.take();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                byte[] name = new byte[BUFFER_SIZE];
                byte[] buffer = new byte[BUFFER_SIZE];

                System.out.print("Name: ");

                int receivedBytes = in.read(name);
                StringBuilder nameBuilder = new StringBuilder();
                ByteBuffer nameBuffer = ByteBuffer.wrap(name);
                for (int i = 0; i < receivedBytes; i++) {
                    byte b = nameBuffer.get();
                    char ch = (char) b;
                    nameBuilder.append(ch);
                }

                String fileName = nameBuilder.toString();

                System.out.println(fileName);

                FileInputStream fis = new FileInputStream(fileName);

                int readBytes = fis.read(buffer);
                while (readBytes != -1) {
                    out.write(buffer);
                    out.flush();
                    readBytes = fis.read(buffer);
                }

                socket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < THREADS_NUM; i++) {
            new Zad2Server().start();
        }

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                sockets.put(socket);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}