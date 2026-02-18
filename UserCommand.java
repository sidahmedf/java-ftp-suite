
public class UserCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        System.out.println("Processing USER command with argument: " + args);
        session.setUsername(args);
        session.sendMessage("331 User name okay, need password.");
    }
}