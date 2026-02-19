
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        // Sécurité : Être connecté
        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        // Vérifier que le client a bien fait PASV avant
        if (session.getDataConnection() == null) {
            session.sendMessage("425 Use PASV or PORT first.");
            return;
        }

        // Message de début (Sur le canal de Contrôle)
        session.sendMessage("150 Here comes the directory listing.");

        // Utilisation du Canal de Données
        // accept() met le thread en pause jusqu'à ce que le client se connecte au nouveau port
        try (Socket dataSocket = session.getDataConnection().accept();
             PrintWriter dataWriter = new PrintWriter(dataSocket.getOutputStream(), true)) {

            File currentDir = session.getCurrentDirectory();
            File[] files = currentDir.listFiles();

            if (files != null) {
                // Formateur de date style UNIX (ex: Jan 15 14:30)
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);

                for (File f : files) {
                    // Je donne le droit 'x' (exécution) aux dossiers pour que FileZilla puisse y entrer..
                    String perms = f.isDirectory() ? "drwxr-xr-x" : "-rw-r--r--";

                    String size = String.valueOf(f.length());
                    String date = dateFormat.format(new Date(f.lastModified()));
                    String name = f.getName();

                    // Format : perms 1 owner group size date name
                    String line = String.format("%s 1 ftp ftp %8s %s %s", perms, size, date, name);

                    dataWriter.println(line);
                }
            }

        } catch (IOException e) {
            session.sendMessage("426 Connection closed; transfer aborted.");
            return;
        } finally {
            // Nettoyage : On détruit la porte de données.
            // En FTP, une connexion de données = UN SEUL transfert.
            try {
                session.getDataConnection().close();
            } catch (IOException e) { /**/ }
            session.setDataConnection(null);
        }

        // Message de fin (Sur le canal de Contrôle)
        session.sendMessage("226 Directory send OK.");
    }
}