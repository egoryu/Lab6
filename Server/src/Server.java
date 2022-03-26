import java.io.IOException;
import java.net.DatagramSocket;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Analise analyst = new Analise();
        if (args.length == 0) {
            analyst.loadFile("");
        }
        else {
            analyst.loadFile(args[0]);
        }

        DatagramSocket s = new DatagramSocket(MyConstant.PORT);

        while (!analyst.exit) {
            analyst.startAnalise(s);
        }
    }
}
