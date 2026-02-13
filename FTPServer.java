
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    private final int port;

    public FTPServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("FTP Server is listening on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientSession session = new ClientSession(clientSocket);
                new Thread(session).start();
            }

        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}