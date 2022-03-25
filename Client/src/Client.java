import java.io.IOException;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        byte b[] = {1};
        SocketAddress a = new InetSocketAddress(InetAddress.getByName("localhost"), 6978);
        DatagramSocket s = new DatagramSocket();
        DatagramPacket o = new DatagramPacket(b, b.length, a);
        //s.connect(InetAddress.getByName("localhost"), 6978);
        s.send(o);
        //s.disconnect();
        DatagramPacket i = new DatagramPacket(b, b.length);
        System.out.println(1);
        //s.connect(InetAddress.getByName("localhost"), 6978);
        s.receive(i);
        System.out.println(i);
    }
}
