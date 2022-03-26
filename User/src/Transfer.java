import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Transfer {
    SocketAddress a;
    DatagramSocket s;

    File file;

    public void Start() throws IOException, ClassNotFoundException {
        a = new InetSocketAddress(InetAddress.getByName(MyConstant.HOST), MyConstant.PORT);
        s = new DatagramSocket();

        file = new File(".\\User\\src\\trans.ser");

        Scanner in = new Scanner(System.in);

        while(true) {
            System.out.print("Введите команду: ");
            String input = "";
            if (!in.hasNextLine()) {
                System.out.println("Плохой символ");
                System.exit(0);
            }
            input = in.nextLine();


            String[] current = input.split(" ");

            if (current.length == 0)
                current = new String[]{" "};

            Request answer;

            switch (current[0]) {
                case ("exit"):
                    System.exit(0);
                    break;
                case ("help"):
                case ("show"):
                case ("info"):
                case ("history"):
                case ("clear"):
                case ("sum_of_minimal_point"):
                case ("save"):
                case ("max_by_name"):
                    sendLetter(new Request(current[0]));
                    answer = getLetter(s);
                    output(answer.getAnswer());
                    break;
                case ("insert"):
                case ("replace_if_greater"):
                case ("update"):
                    if (current.length < 2) {
                        System.out.println("Не введен аргумент");
                        break;
                    }
                    sendLetter(new Request(current[0], current[1], LabWork.insert()));
                    answer = getLetter(s);
                    output(answer.getAnswer());
                    break;
                case ("remove_key"):
                case ("remove_lower_key"):
                case ("count_by_minimal_point"):
                case ("execute_script"):
                    if (current.length < 2) {
                        System.out.println("Не введен аргумент");
                        break;
                    }
                    sendLetter(new Request(current[0], current[1]));
                    answer = getLetter(s);
                    output(answer.getAnswer());
                    break;
                default:
                    System.out.println("Неправильно введена команда");
            }
        }
    }

    public void sendLetter(Request send) throws IOException {
        FileOutputStream fileOutput;
        FileInputStream fileInput;
        ObjectOutputStream objectOut;

        fileOutput = new FileOutputStream(file);
        objectOut = new ObjectOutputStream(fileOutput);

        objectOut.writeObject(send);


        fileInput = new FileInputStream(file);

        byte[] request = new byte[(int)file.length()];
        fileInput.read(request);

        byte[] letterSize = Useful.convertToByte(request.length);
        DatagramPacket i = new DatagramPacket(letterSize, letterSize.length, a);
        s.send(i);

        DatagramPacket o = new DatagramPacket(request, request.length, a);
        s.send(o);

        fileOutput.close();
        fileInput.close();
    }

    public Request getLetter(DatagramSocket datagramSocket) throws IOException, ClassNotFoundException {
        FileOutputStream fileOutput;
        FileInputStream fileInput;
        ObjectInputStream objectInput;

        byte[] length = new byte[MyConstant.SIZE];
        DatagramPacket letterSize = new DatagramPacket(length, length.length);
        datagramSocket.receive(letterSize);

        byte[] req = new byte[Useful.convertToInt(length)];
        DatagramPacket inputRequest = new DatagramPacket(req, req.length);
        datagramSocket.receive(inputRequest);

        fileOutput = new FileOutputStream(file);

        fileOutput.write(req);

        fileInput = new FileInputStream(file);
        objectInput = new ObjectInputStream(fileInput);

        Request request = (Request) objectInput.readObject();

        objectInput.close();
        fileOutput.close();
        fileInput.close();

        return request;
    }

    public void output(ArrayList<String> output) {
        for (String u: output)
            System.out.println(u);
    }
}
