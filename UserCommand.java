
public class UserCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        // Just print to console to prove it works
        System.out.println("Processing USER command with argument: " + args);

        // FTP Code 331: User name okay, need password.
        session.sendMessage("331 User name okay, need password.");
    }
}