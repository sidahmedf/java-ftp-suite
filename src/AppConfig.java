

public class AppConfig {
    // Valeurs par défaut (en cas d'arguments manquants)
    private static final int DEFAULT_PORT = 21;
    private static final String DEFAULT_USER = "anonymous";
    private static final String DEFAULT_PASS = "guest";
    private static final int DEFAULT_DEPTH = Integer.MAX_VALUE;

    // Champs pour stocker la configuration
    private String server;
    private int port;
    private String user;
    private String password;
    private int maxDepth;

    private boolean jsonMode = false;

    // Parse et valider les arguments de la ligne de commande
    public AppConfig(String[] args) throws IllegalArgumentException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Server address is missing.");
        }

        // Obligatoire: Server
        this.server = args[0];

        // Optionnel: Port
        try {
            this.port = (args.length >= 2) ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port must be a valid number.");
        }

        // Optionnel: User et Password
        this.user = (args.length >= 3) ? args[2] : DEFAULT_USER;
        this.password = (args.length >= 4) ? args[3] : DEFAULT_PASS;

        try {
            if (args.length >= 5) {
                this.maxDepth = Integer.parseInt(args[4]);
            } else {
                this.maxDepth = DEFAULT_DEPTH;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Depth must be a valid number.");
        }

        if (args.length >= 6 && args[5].equalsIgnoreCase("json")) {
            this.jsonMode = true;
        }
    }


    // Afficher le status de la configuration
    public void printStatus() {
        System.out.println("FTP Client Starting...");
        System.out.println("--------------------------------");
        System.out.println(" Server : " + server + ":" + port);
        System.out.println(" Login   : " + user);

        String passDisplay = (password.isEmpty()) ? "(none)" : password;
        System.out.println(" Password   : " + passDisplay);

        String depthDisplay = (maxDepth == Integer.MAX_VALUE) ? "Unlimited" : String.valueOf(maxDepth);
        System.out.println(" Max Depth  : " + depthDisplay);

        System.out.println(" Output Fmt : " + (jsonMode ? "JSON" : "Text Tree"));
        System.out.println("--------------------------------");
    }

    // En gros des getters
    public String getServer() { return server; }
    public int getPort() { return port; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
    public int getMaxDepth() { return maxDepth; }
    public boolean isJsonMode() { return jsonMode; }
}