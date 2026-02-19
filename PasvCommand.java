import java.io.IOException;
import java.net.ServerSocket;

public class PasvCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {

        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        try {
            // Fermer l'ancienne connexion de données si elle existait déjà
            if (session.getDataConnection() != null && !session.getDataConnection().isClosed()) {
                session.getDataConnection().close();
            }

            // Créer un nouveau ServerSocket sur le port 0
            ServerSocket dataServer = new ServerSocket(0);
            session.setDataConnection(dataServer);

            // Récupérer le port réel qui a été généré
            int port = dataServer.getLocalPort();

            // Calculer p1 et p2 selon la norme FTP (Port = p1 * 256 + p2)
            int p1 = port / 256;
            int p2 = port % 256;

            // Pour mes tests en local, on force l'IP "127.0.0.1" (avec des virgules !)
            String ip = "127,0,0,1";

            // Envoyer la réponse 227
            session.sendMessage("227 Entering Passive Mode (" + ip + "," + p1 + "," + p2 + ").");

            System.out.println("Mode passif prêt sur le port " + port);

        } catch (IOException e) {
            session.sendMessage("425 Can't open data connection.");
        }
    }
}