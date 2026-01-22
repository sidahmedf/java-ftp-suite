import java.io.*;
import java.net.*;

public class ClientFTP {

    // Sockets et flux de communication
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    // Etat de la connexion
    private boolean isConnected = false;


    // Pour se connecter au serveur FTP
    public void connect (String server, int port) throws IOException {
        System.out.println("Connecting to " + server + " on port " + port);

        socket = new Socket(server, port);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        String response = reader.readLine();
        System.out.println("Server response: " + response);

        if (response.startsWith("220")) {
            isConnected = true;
            System.out.println("Connected to the FTP server.");
        } else {
            throw new IOException("Failed to connect to the FTP server : " + response);
        }



    }


    // Pour se logger avec un nom d'utilisateur et un mot de passe
    public boolean login(String user, String pass)  {
        System.out.println("Attempting login as " + user);

        // TODO: Implémenter USER et PASS
        return false;
    }

    // Pour lister les fichiers dans le répertoire courant
    public void listFiles() throws IOException {

        // TODO: Implémenter LIST
    }

    // Pour changer de répertoire
    public boolean changeDirectory(String dir) {

        // TODO: Implémenter CWD
        return false;
    }

    // Pour remonter d'un répertoire
    public boolean changeDirectoryUp() throws IOException {

        // TODO: Implémenter CDUP
        return false;
    }


    // Pour se déconnecter du serveur FTP
    public void disconnect() throws IOException {

        // TODO : déconnecter proprement
        System.out.println("Disconnected.");
    }



}
