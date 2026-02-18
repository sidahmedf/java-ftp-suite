
public interface Command {
    /**
     * @param session The client session (so we can reply).
     * @param args The argument (e.g., "sid" in "USER sid").
     */
    void execute(ClientSession session, String args);
}