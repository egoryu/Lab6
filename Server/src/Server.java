import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final int PORT = 6978;

        Analise analyst = new Analise();
        if (args.length == 0) {
            analyst.loadFile("");
        }
        else {
            analyst.loadFile(args[0]);
        }

        DatagramSocket s = new DatagramSocket(PORT);

        while (!analyst.exit) {
            analyst.startAnalise(s);
        }
    }
}
