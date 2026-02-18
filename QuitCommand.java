

public class QuitCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        session.sendMessage("221 Goodbye.");
        session.disconnect();
    }
}