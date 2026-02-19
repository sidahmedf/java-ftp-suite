

public class PwdCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {

        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        String path = session.getRelativePath();
        session.sendMessage("257 \"" + path + "\" is current directory.");
    }
}