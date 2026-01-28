import java.io.IOException;
import java.util.List;

public class Explorer {

    private  final ClientFTP client;

    public Explorer(ClientFTP client) {
        this.client = client;
    }

    public void start() {
        System.out.println("Starting tree exploration...");


        try {
            this.explore(".");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void explore(String prefix) throws IOException {

        // récupérer la liste des fichiers dans le répertoire courant
        List<String> files = client.listFiles();
        // pour chaque fichier/répertoire
        for (String line  : files) {

            // vérifier que la ligne n'est pas vide
            if (line.length() < 1) {
                continue;
            }

            //vérifier si c'est un répertoire ou un fichier
            char type = line.charAt(0);

            // extraire le nom (dernier mot de la ligne)
            String[] parts = line.split("\\s+");
            String name = parts[parts.length - 1];

            // ignorer les entrées spéciales . et .. (lien symboliques)
            if (name.equals(".") || name.equals("..")) {
                continue;
            }


            System.out.println(prefix + "|-- " + name);

            // récursivement explorer les répertoires
            if (type == 'd') {

                // Tenter d'aller dans le répertoire (CWD)
                boolean moved = client.changeDirectory(name);

                if (moved) {
                    //appeler récursivement explore avec un préfixe mis à jour
                    explore(prefix + "|   ");

                    // Remonter d'un répertoire (CDUP) après l'exploration
                    client.changeDirectoryUp();
                }
            }
        }
    }
}
