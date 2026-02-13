
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession implements Runnable {
    private final Socket socket;

    public ClientSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {

            writer.println("220 Welcome to SR1 FTP Server");

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Client " + socket.getInetAddress() + " " + clientMessage);

                writer.println("not yet implemented.");
            }

        } catch (IOException e) {
            System.err.println("Client session error: " + e.getMessage());
        } finally {
            System.out.println("Client disconnected.");
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}