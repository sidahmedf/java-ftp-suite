

public class TypeCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        // On dit simplement OK au client, peu importe s'il demande A ou I
        session.sendMessage("200 Type set to " + args);
    }
}