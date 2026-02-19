import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.io.File;
import java.nio.file.Path;


public class ClientSession implements Runnable {
    private final Socket socket;
    private final CommandRouter router;
    private PrintWriter writer;
    private String username = null;
    private boolean isLoggedIn =false;

    private final File rootDirectory = new File(System.getProperty("user.dir"), "ftp_root");
    private File currentDirectory = rootDirectory;

    public ClientSession(Socket socket) {
        this.socket = socket;
        this.router = new CommandRouter();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File newDirectory) {
        this.currentDirectory = newDirectory;
    }

    public String getRelativePath() {
        Path rootPath = rootDirectory.toPath();
        Path currentPath = currentDirectory.toPath();

        // Calcule le chemin relatif
        String relative = rootPath.relativize(currentPath).toString();

        // Pour les systèmes Windows, remplacer les backslashes par des slashes (sinon ca crashe sur les chemins)
        relative = relative.replace("\\", "/");

        // Si c'est vide, c'est la racine "/"
        if (relative.isEmpty()) return "/";

        return "/" + relative;
    }

    @Override
    public void run() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer = new PrintWriter(socket.getOutputStream(), true);


            sendMessage("220 Welcome to SR1 FTP Server");

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Client " + socket.getInetAddress() + " " + clientMessage);

                router.route(this, clientMessage);
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