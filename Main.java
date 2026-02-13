
public class Main {
    public static void main(String[] args) {

        int port = 2121;
        System.out.println("Starting FTP Server on port " + port + ".");

        FTPServer server = new FTPServer(port);
        server.start();
    }
}