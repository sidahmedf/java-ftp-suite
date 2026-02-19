
import java.io.File;
import java.io.IOException;

public class CwdCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {

        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        File newDir;

        // Gérer les chemins absolus (commençant par /) ou relatifs
        if (args.startsWith("/")) {

            String cleanArg = args.substring(1);
            newDir = new File(session.getRootDirectory(), cleanArg);
        } else {
            // Sinon c'est relatif au dossier courant
            newDir = new File(session.getCurrentDirectory(), args);
        }

        try {
            // Simplifier le chemin (enlever les ".." et les ".")
            File canonicalDir = newDir.getCanonicalFile();

            // Vérifier la "Prison" (Chroot)
            if (!canonicalDir.getPath().startsWith(session.getRootDirectory().getCanonicalPath())) {
                session.sendMessage("550 Access denied.");
                return;
            }

            // Vérifier que c'est bien un dossier qui existe
            if (canonicalDir.exists() && canonicalDir.isDirectory()) {
                session.setCurrentDirectory(canonicalDir);
                session.sendMessage("250 Directory changed to " + session.getRelativePath());
            } else {
                session.sendMessage("550 Directory not found.");
            }

        } catch (IOException e) {
            session.sendMessage("550 Error resolving path.");
        }
    }
}