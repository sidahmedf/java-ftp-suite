
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class StorCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        // 1. Le Vigile
        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        // 2. Vérifier la porte de données (PASV/EPSV)
        if (session.getDataConnection() == null) {
            session.sendMessage("425 Use PASV or EPSV first.");
            return;
        }

        if (args == null || args.isEmpty()) {
            session.sendMessage("501 Syntax error in parameters.");
            return;
        }

        try {
            // 3. Calculer où on va sauvegarder le fichier
            File fileToSave;
            if (args.startsWith("/")) {
                // S'il donne un chemin absolu, on part de la racine
                fileToSave = new File(session.getRootDirectory(), args.substring(1));
            } else {
                // Sinon, on le pose dans le dossier actuel
                fileToSave = new File(session.getCurrentDirectory(), args);
            }

            // Simplifier le chemin et vérifier la prison (Chroot)
            fileToSave = fileToSave.getCanonicalFile();
            if (!fileToSave.getPath().startsWith(session.getRootDirectory().getCanonicalPath())) {
                session.sendMessage("550 Access denied.");
                return;
            }

            // 4. Message : OK, je suis prêt à recevoir
            session.sendMessage("150 Ok to send data.");

            // 5. Réception des données
            try (Socket dataSocket = session.getDataConnection().accept();
                 InputStream in = dataSocket.getInputStream();
                 FileOutputStream fos = new FileOutputStream(fileToSave)) {

                // On lit ce qui arrive du réseau et on l'écrit sur le disque
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                session.sendMessage("426 Connection closed; transfer aborted.");
                return;
            } finally {
                // 6. On ferme la porte de données
                try { session.getDataConnection().close(); } catch (IOException e) {}
                session.setDataConnection(null);
            }

            // 7. Message de succès final
            session.sendMessage("226 Transfer complete.");

        } catch (IOException e) {
            session.sendMessage("550 File error.");
        }
    }
}