import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        LinkedHashMap<String, LabWork> collection = new LinkedHashMap<>();
        Scanner in = new Scanner(System.in);
        Menu menu = new Menu();
        String nameOfSaveFile = "";
        ArrayDeque<String> history = new ArrayDeque<>();

        if (args.length == 0) {
            do {
                System.out.print("Введите путь к файлу для загрузки и сохранения: ");
                if (in.hasNextLine())
                    nameOfSaveFile = in.nextLine();
                else {
                    System.out.println("Плохой символ");
                    System.exit(0);
                }
            } while(!nameOfSaveFile.isEmpty());
        } else {
            nameOfSaveFile = args[0];
        }
        collection = IO.Read(nameOfSaveFile, ';');

        if (collection == null) {
            collection = new LinkedHashMap<>();
        }

        IO.CheckedWrite(nameOfSaveFile);
        collection = Menu.lhmSort(collection);
        Menu.help();

        while(true) {
            System.out.print("Введите команду: ");
            String input = "";
            if (in.hasNextLine())
                input = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }

            String[] current = input.split(" ");

            if (current.length == 0)
                current = new String[] {" "};
            
            history.addLast(current[0]);
            switch(current[0]) {
                case("help"):
                    Menu.help();
                    break;
                case("exit"):
                    System.exit(0);
                    break;
                case("show"):
                    Menu.show(collection);
                    break;
                case("info"):
                    Menu.info(collection);
                    break;
                case("insert"):
                    if (current.length < 2)
                        collection.putAll(menu.insert(""));
                    else
                        collection.putAll(menu.insert(current[1]));

                    collection = Menu.lhmSort(collection);
                    break;
                case("remove_key"):
                    if (current.length < 2)
                        collection = menu.removeKey(collection, "");
                    else
                        collection = menu.removeKey(collection, current[1]);
                    break;
                case("clear"):
                    collection.clear();
                    System.out.println("Коллекция очищена");
                    break;
                case("history"):
                    Menu.history(history);
                    break;
                case("update"):
                    if (current.length < 2)
                        collection = menu.update(collection, "");
                    else
                        collection = menu.update(collection, current[1]);
                    break;
                case("sum_of_minimal_point"):
                    System.out.println(Menu.sumOfMinimalPoint(collection));
                    break;
                case("max_by_name"):
                    System.out.println(Menu.maxByName(collection));
                    break;
                case("count_by_minimal_point"):
                    if (current.length < 2)
                        System.out.println(Menu.countByMinimalPoint(collection, ""));
                    else
                        System.out.println(Menu.countByMinimalPoint(collection, current[1]));
                    break;
                case("remove_lower_key"):
                    if (current.length < 2)
                        collection = Menu.removeLowerKey(collection,  "");
                    else
                        collection = Menu.removeLowerKey(collection, current[1]);
                    break;
                case("replace_if_greater"):
                    if (current.length < 2)
                        collection = menu.replaceIfGreater(collection, "");
                    else
                        collection = menu.replaceIfGreater(collection, current[1]);
                    break;
                case("save"):
                    Menu.savetoFile(collection, nameOfSaveFile, ';');
                    break;
                case("execute_script"):
                    if (current.length < 2)
                        collection = Menu.executeScript(collection, "", nameOfSaveFile);
                    else
                        collection = Menu.executeScript(collection, current[1], nameOfSaveFile);
                    break;
                default:
                    System.out.println("Неправильно введена команда");
                    history.pollLast();
                    continue;
            }
            if (history.size() > 12)
                history.poll();
        }
    }
}
