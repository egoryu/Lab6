import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Analise {
    LinkedHashMap<String, LabWork> collection = new LinkedHashMap<>();
    Scanner in = new Scanner(System.in);
    Menu menu = new Menu();
    String nameOfSaveFile = "";
    ArrayDeque<String> history = new ArrayDeque<>();
    public boolean exit = false;
    final int SIZE = 1;

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
        Request request = null;
        byte[] len = new byte[SIZE];
        DatagramPacket inputlen = new DatagramPacket(len, len.length);
        System.out.println(2);
        datagramSocket.receive(inputlen);
        System.out.println(Arrays.toString(inputlen.getData()));
        DatagramPacket o = new DatagramPacket(len, len.length, inputlen.getAddress(), inputlen.getPort());
        datagramSocket.send(o);

        System.out.println(1);
        System.out.println(inputlen);
        byte[] req = new byte[convertToInt(len)];
        DatagramPacket inputRequest = new DatagramPacket(req, req.length);

        try (FileOutputStream outputStream = new FileOutputStream("/tmp/tmp.ser")) {
            outputStream.write(req);
        }
        FileInputStream fileIn = new FileInputStream("/tmp/tmp.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        request = (Request) in.readObject();
        in.close();
        
        history.addLast(request.getCommand());
        switch (request.getCommand()) {
            case ("help"):
                Menu.help();
                break;
            case ("exit"):
                System.exit(0);
                break;
            case ("show"):
                Menu.show(collection);
                break;
            case ("info"):
                Menu.info(collection);
                break;
            case ("insert"):
                collection.putAll(menu.insert(request.getArgument()));

                collection = Useful.lhmSort(collection);
                break;
            case ("remove_key"):
                collection = menu.removeKey(collection, request.getArgument());
                break;
            case ("clear"):
                collection.clear();
                System.out.println("Коллекция очищена");
                break;
            case ("history"):
                Menu.history(history);
                break;
            case ("update"):
                collection = menu.update(collection, request.getArgument());
                break;
            case ("sum_of_minimal_point"):
                System.out.println(Menu.sumOfMinimalPoint(collection));
                break;
            case ("max_by_name"):
                System.out.println(Menu.maxByName(collection));
                break;
            case ("count_by_minimal_point"):
                System.out.println(Menu.countByMinimalPoint(collection, request.getArgument()));
                break;
            case ("remove_lower_key"):
                collection = Menu.removeLowerKey(collection, request.getArgument());
                break;
            case ("replace_if_greater"):
                collection = menu.replaceIfGreater(collection, request.getArgument());
                break;
            case ("save"):
                Menu.savetoFile(collection, nameOfSaveFile, ';');
                break;
            case ("execute_script"):
                collection = Menu.executeScript(collection, request.getArgument(), nameOfSaveFile);
                break;
            default:
                System.out.println("Неправильно введена команда");
                history.pollLast();
        }
        if (history.size() > 12)
            history.poll();

    }

    public int convertToInt(byte[] in) {
        int res = 0;

        for (byte u: in) {
            res = res + 10 * (u - '0');
        }
        return res;
    }
}
