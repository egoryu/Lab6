import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Analise {
    LinkedHashMap<String, LabWork> collection = new LinkedHashMap<>();
    Scanner in = new Scanner(System.in);
    String nameOfSaveFile = "";
    ArrayDeque<String> history = new ArrayDeque<>();
    public boolean exit = false;

    File file;

    InetAddress client;
    int port;

    public void loadFile(String nameOfFile) {
        while (nameOfFile.isEmpty() || !IO.CheckedWrite(nameOfFile) || !IO.CheckedRead(nameOfFile)) {
            System.out.print("Введите путь к файлу для загрузки и сохранения: ");
            if (!in.hasNextLine()) {
                System.out.println("Принудительный выход");
                System.exit(0);
            }
            nameOfFile = in.nextLine();
        }
        nameOfSaveFile = nameOfFile;
        collection = IO.Read(nameOfSaveFile, ';');


        if (collection == null) {
            collection = new LinkedHashMap<>();
        }

        collection = Useful.lhmSort(collection);
    }

    public void startAnalise(DatagramSocket datagramSocket) throws IOException, ClassNotFoundException {
        Menu menu = new Menu();
        file = new File(".\\Server\\src\\analise.ser");

        Request request = getLetter(datagramSocket);

        history.addLast(request.getCommand());
        switch (request.getCommand()) {
            case ("help"):
                menu.help();
                break;
            case ("exit"):
                System.exit(0);
                break;
            case ("show"):
                menu.show(collection);
                break;
            case ("info"):
                menu.info(collection);
                break;
            case ("insert"):
                collection = menu.insert(collection, request.getArgument(), request.getTarget());

                collection = Useful.lhmSort(collection);
                break;
            case ("remove_key"):
                collection = menu.removeKey(collection, request.getArgument());
                break;
            case ("clear"):
                collection.clear();
                menu.answer.add("Коллекция очищена");
                break;
            case ("history"):
                menu.history(history);
                break;
            case ("update"):
                collection = menu.update(collection, request.getArgument(), request.getTarget());
                break;
            case ("sum_of_minimal_point"):
                menu.answer.add(String.valueOf(menu.sumOfMinimalPoint(collection)));
                break;
            case ("max_by_name"):
                menu.answer.add(String.valueOf(menu.maxByName(collection)));
                break;
            case ("count_by_minimal_point"):
                menu.answer.add(String.valueOf(menu.countByMinimalPoint(collection, request.getArgument())));
                break;
            case ("remove_lower_key"):
                collection = menu.removeLowerKey(collection, request.getArgument());
                break;
            case ("replace_if_greater"):
                collection = menu.replaceIfGreater(collection, request.getArgument(), request.getTarget());
                break;
            case ("save"):
                menu.savetoFile(collection, nameOfSaveFile, ';');
                break;
            case ("execute_script"):
                collection = menu.executeScript(collection, request.getArgument(), nameOfSaveFile);
                break;
            default:
                menu.answer.add("Неправильно введена команда");
                history.pollLast();
        }
        if (history.size() >= 12)
            history.poll();

        sendLetter(new Request(menu.answer), datagramSocket);
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

        client = inputRequest.getAddress();
        port = inputRequest.getPort();

        fileInput = new FileInputStream(file);
        objectInput = new ObjectInputStream(fileInput);

        Request request = (Request) objectInput.readObject();

        objectInput.close();
        fileInput.close();
        fileOutput.close();

        return request;
    }

    public void sendLetter(Request send, DatagramSocket datagramSocket) throws IOException {
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
        DatagramPacket i = new DatagramPacket(letterSize, letterSize.length, client, port);
        datagramSocket.send(i);

        DatagramPacket o = new DatagramPacket(request, request.length, client, port);
        datagramSocket.send(o);

        objectOut.close();
        fileInput.close();
        fileOutput.close();
    }
}
