
public interface Command {
    /**
     * @param session La session du client qui a envoyé la commande.
     * @param args L'argument (e.g., "sid" dans "USER sid").
     */
    void execute(ClientSession session, String args);
}