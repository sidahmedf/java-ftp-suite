

public class PassCommand implements Command{

    @Override
    public void execute(ClientSession session, String args) {

        if (session.getUsername() == null) {
            session.sendMessage("503 Login with USER first.");
            return;
        }

        session.setLoggedIn(true);
        System.out.println("User " + session.getUsername() + " is now logged in.");

        session.sendMessage("230 User logged in, proceed.");

    }
}
