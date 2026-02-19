
import java.io.IOException;
import java.net.ServerSocket;

public class EpsvCommand implements Command {
    @Override
    public void execute(ClientSession session, String args) {
        if (!session.isLoggedIn()) {
            session.sendMessage("530 Not logged in.");
            return;
        }

        try {
            if (session.getDataConnection() != null && !session.getDataConnection().isClosed()) {
                session.getDataConnection().close();
            }

            ServerSocket dataServer = new ServerSocket(0);
            session.setDataConnection(dataServer);
            int port = dataServer.getLocalPort();

            session.sendMessage("229 Entering Extended Passive Mode (|||" + port + "|).");
            System.out.println("Mode EPSV (IPv6) prêt sur le port " + port);

        } catch (IOException e) {
            session.sendMessage("425 Can't open data connection.");
        }
    }
}