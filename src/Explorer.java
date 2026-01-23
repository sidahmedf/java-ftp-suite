public class Explorer {

    private  final ClientFTP client;

    public Explorer(ClientFTP client) {
        this.client = client;
    }

    public void start() {
        System.out.println("Starting tree exploration...");
        // TODO : Appeler méthode qui implémnte LIST et explorer récursivement
        try {
            client.listFiles();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void explore(String currentPath) {
        // TODO: Logic métier pour explorerles répertoires
        // TODO : Logic doit etre récursive
    }

}
