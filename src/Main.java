public class Main {

    public static void main(String[] args) {

        try {

            // Déléguer la gestion des arguments à AppConfig (Seperation of Concerns)
            AppConfig config = new AppConfig(args);

            // Afficher le status de la configuration maintenue par AppConfig
            config.printStatus();

            // Lancer l'application principale
            runApplication(config);

        } catch (IllegalArgumentException e) {
            // Gérer les erreurs de configuration et afficher l'usage correct
            System.out.println("Error: " + e.getMessage());
            System.out.println("Usage: java Main <server> [port] [user] [pass]");
        }
    }

    // Une méthode pour exécuter l'application principale
    private static void runApplication(AppConfig config) {
        ClientFTP client = new ClientFTP();
        try {
            client.connect(config.getServer(), config.getPort());

            boolean loggedIn = client.login(config.getUser(), config.getPassword());

            if (loggedIn) {
                Explorer explorer = new Explorer(client, config.getMaxDepth());
                explorer.start(config.isJsonMode());
            } else {
                System.out.println("Login failed.");
            }
            client.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}