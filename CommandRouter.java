
import java.util.HashMap;
import java.util.Map;

public class CommandRouter {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandRouter() {

        commandMap.put("USER", new UserCommand());
        commandMap.put("PASS", new PassCommand());
        commandMap.put("QUIT", new QuitCommand());
        commandMap.put("PWD", new PwdCommand());
        commandMap.put("CWD", new CwdCommand());
        commandMap.put("CDUP", new CwdCommand());

        commandMap.put("OPTS", (session, args) -> session.sendMessage("200 OK"));

        commandMap.put("SYST", (session, args) -> session.sendMessage("215 UNIX Type: L8"));
        commandMap.put("XPWD", new PwdCommand()); // Pour résoudre problème vieux client Windows qui envoie XPWD au lieu de PWD
    }

    public void route(ClientSession session, String message) {

        String[] parts = message.split(" ", 2);

        String commandName = parts[0].toUpperCase().trim();
        String args = (parts.length > 1) ? parts[1].trim() : "";

        if (commandMap.containsKey(commandName)) {

            Command cmd = commandMap.get(commandName);
            cmd.execute(session, args);
        } else {

            session.sendMessage("502 Command not implemented.");
        }
    }
}