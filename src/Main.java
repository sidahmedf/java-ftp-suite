import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String server = "ftp.ubuntu.com";

        ClientFTP client = new ClientFTP();

        try {

            // Connexion au serveur FTP
            client.connect(server, 21);

            // Tentative de login (hardcodée pour l'instant)
            boolean loggedIn = client.login("anonymous", "guest");

            if (loggedIn) {

                Explorer explorer = new Explorer(client);
                explorer.start();


            } else {
                System.out.println("Login failed.");
            }
            // Déconnexion du serveur FTP
            client.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
