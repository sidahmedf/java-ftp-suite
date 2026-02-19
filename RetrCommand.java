
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class RetrCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        // 1. Le Vigile
        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        // 2. Vérifier que la porte de données est prête
        if (session.getDataConnection() == null) {
            session.sendMessage("425 Use PASV or EPSV first.");
            return;
        }

        if (args == null || args.isEmpty()) {
            session.sendMessage("501 Syntax error in parameters.");
            return;
        }

        try {
            // 3. Trouver le fichier demandé et le sécuriser (Chroot Jail)
            File fileToDownload;
            if (args.startsWith("/")) {
                fileToDownload = new File(session.getRootDirectory(), args.substring(1)).getCanonicalFile();
            } else {
                fileToDownload = new File(session.getCurrentDirectory(), args).getCanonicalFile();
            }

            // Vérifier qu'on ne sort pas de la prison
            if (!fileToDownload.getPath().startsWith(session.getRootDirectory().getCanonicalPath())) {
                session.sendMessage("550 Access denied.");
                return;
            }

            // Vérifier que le fichier existe et que ce n'est pas un dossier
            if (!fileToDownload.exists() || !fileToDownload.isFile()) {
                session.sendMessage("550 File not found.");
                return;
            }

            // 4. Message de départ
            session.sendMessage("150 Opening data connection for " + fileToDownload.getName());

            // 5. Lecture et Envoi des données brutes
            try (Socket dataSocket = session.getDataConnection().accept();
                 FileInputStream fis = new FileInputStream(fileToDownload);
                 OutputStream out = dataSocket.getOutputStream()) {

                // On lit et on envoie par blocs de 4096 octets
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                session.sendMessage("426 Connection closed; transfer aborted.");
                return;
            } finally {
                // 6. Fermeture de la porte
                try { session.getDataConnection().close(); } catch (IOException e) {}
                session.setDataConnection(null);
            }

            // 7. Message de succès
            session.sendMessage("226 Transfer complete.");

        } catch (IOException e) {
            session.sendMessage("550 File error.");
        }
    }
}