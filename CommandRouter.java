
import java.util.HashMap;
import java.util.Map;

public class CommandRouter {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandRouter() {

        commandMap.put("USER", new UserCommand());
    }

    public void route(ClientSession session, String message) {

        String[] parts = message.split(" ", 2);

        String commandName = parts[0].toUpperCase();
        String args = (parts.length > 1) ? parts[1] : "";

        if (commandMap.containsKey(commandName)) {
            // Found it
            Command cmd = commandMap.get(commandName);
            cmd.execute(session, args);
        } else {
            // Unknown command
            session.sendMessage("502 Command not implemented.");
        }
    }
}