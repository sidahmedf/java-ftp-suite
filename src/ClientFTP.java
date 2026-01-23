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
    public boolean login(String user, String pass) throws IOException  {
        System.out.println("Attempting login as " + user);

        // Envoyer USER
        writer.println("USER " + user);
        // Lire la réponse du serveur  (doit etre 331)
        String response = readResponse();
        if (!response.startsWith("331") && !response.startsWith("230")) {
            System.out.println("Login failed at USER step: " + response);
            return false;
        }

        // Envoyer PASS
        writer.println("PASS " + pass);
        // Lire la réponse du serveur (doit etre 230)
        response = readResponse();
        if (response.startsWith("230")) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Login failed at PASS step: " + response);
            return false ;
        }

    }

    // Pour lister les fichiers dans le répertoire courant
    public void listFiles() throws IOException {

        // Ouvrir une connexion de données en mode passif (Obligatoire pour LIST)
        Socket dataSocket = openDataConnection();

        // Envoyer la commande LIST et lire la réponse (Sockets writer et reader)
        writer.println("LIST");
        readResponse();

        // Lire les données de la connexion de données (Socket dataSocket !)
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        // Boucle de lecture des lignes reçues
        String line;
        while((line = dataReader.readLine()) != null) {
            System.out.println(line);
        }

        // Fermer la connexion de données
        dataSocket.close();

        readResponse();
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

    private String readResponse() throws IOException {
        String line = reader.readLine();
        System.out.println("SERVER: " + line);
        return line;
    }

    private Socket openDataConnection() throws IOException {

        // Envoyer la commande PASV et lire la réponse
        writer.println("PASV");
        String response = readResponse();

        // Analyser la réponse pour obtenir l'adresse IP et le port
        if (!response.startsWith("227")) {
            throw new IOException("Error entering passive mod: " + response);
        }

        int openParen = response.indexOf('(');
        int closeParen = response.indexOf(')');

        if (openParen == -1 || closeParen == -1) {
            throw new IOException("Error when parse PASV response: " + response);
        }

        String content  = response.substring(openParen+1, closeParen);

        // Ex: 192,168,1,2,7,138
        String[] parts = content.split(",");

        String ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];

        String p1_str = parts[4];
        String p2_str = parts[5];

        int p1 = Integer.parseInt(p1_str);
        int p2 = Integer.parseInt(p2_str);

        // Calcul du port
        int dataPort = (p1* 256) + p2;

        // Etablir la connexion de données et la retourner
        Socket dataSocket = new Socket(ip, dataPort);
        return dataSocket;


    }






}
