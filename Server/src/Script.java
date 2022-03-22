import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Script {
    public static ArrayList<String> scriptHistory = new ArrayList<>();

    public static LinkedHashMap<String, LabWork> makeScript(LinkedHashMap<String, LabWork> collection, String script, String saveFile) throws IOException {
        if (scriptHistory.contains(script)) {
            System.out.println("Образовался цикл из команд");
            scriptHistory.remove(script);
            return collection;
        } else {
            scriptHistory.add(script);
        }

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(
                    new FileInputStream(script));
        } catch (FileNotFoundException e) {
            System.out.println("Не получилось открыть файл");
            scriptHistory.remove(script);
            return collection;
        }

        int c;
        ArrayList<String> strScript = new ArrayList<>();
        String tmp = "";

        while ((c = reader.read()) != -1) {
            if (c == '\n') {
                strScript.add(tmp);
                tmp = "";
            } else
                tmp += (char) c;
        }

        ArrayDeque<String> history = new ArrayDeque<>();
        for (int i = 0, strScriptSize = strScript.size(); i < strScriptSize; i++) {
            String[] current = strScript.get(i).split(" ");

            if (current[0].isEmpty())
                continue;

            history.addLast(current[0]);
            switch (current[0]) {
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
                    if (current.length < 2 || current[1].isEmpty()) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }
                    int x = checkScript(strScript, i);

                    if (x == 0) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }

                    collection.put(current[1], Script.insertScript(strScript, i));
                    collection = Menu.lhmSort(collection);
                    i += x;
                    break;
                case ("remove_key"):
                    if (current.length < 2 || current[1].isEmpty()) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }
                    collection.remove(current[1]);
                    break;
                case ("clear"):
                    collection.clear();
                    System.out.println("Коллекция очищена");
                    break;
                case ("history"):
                    if (history.isEmpty())
                        System.out.println("История пуста");
                    else {
                        System.out.println("Последние 12 команд: ");
                        for (String s : history) {
                            System.out.println(s);
                        }
                    }
                    break;
                case ("update"):
                    if (current.length < 2 || !Menu.isInteger(current[1])) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }

                    int x1 = checkScript(strScript, i);

                    if (x1 == 0) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }

                    Iterator iterator = collection.values().iterator();
                    int cur = 0;
                    LabWork labWork1 = null;

                    while (iterator.hasNext()) {
                        labWork1 = (LabWork) iterator.next();
                        if (labWork1.getId() == Integer.parseInt(current[1])) {
                            break;
                        }
                        cur++;
                    }

                    if (cur != collection.size()) {
                        iterator = collection.keySet().iterator();
                        while (cur-- > 0) {
                            iterator.next();
                        }
                        String key = (String) iterator.next();

                        collection.replace(key, labWork1, insertScript(strScript, i));
                    } else
                        System.out.println("Такого id нет");

                    i += x1;
                    break;
                case ("sum_of_minimal_point"):
                    System.out.println(Menu.sumOfMinimalPoint(collection));
                    break;
                case ("max_by_name"):
                    System.out.println(Menu.maxByName(collection));
                    break;
                case ("count_by_minimal_point"):
                    if (current.length < 2 || !Menu.isInteger(current[1])) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }
                    System.out.println(Menu.countByMinimalPoint(collection, current[1]));
                    break;
                case ("remove_lower_key"):
                    if (current.length < 2 || current[1].isEmpty()) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }
                    collection = Menu.removeLowerKey(collection, current[1]);
                    break;
                case ("replace_if_greater"):
                    int x2 = checkScript(strScript, i);

                    if (x2 == 0) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }

                    LabWork labWork = Script.insertScript(strScript, i);

                    if (current.length < 2 || current[1].isEmpty()) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }

                    if (collection.get(current[1]).compareTo(labWork) < 0) {
                        collection.replace(current[1], collection.get(current[1]), labWork);
                        System.out.println("Заменено");
                    } else {
                        System.out.println("Замены не было");
                    }
                    i += x2;
                    break;
                case ("save"):
                    Menu.savetoFile(collection, saveFile, ',');
                    break;
                case ("execute_script"):
                    if (current.length < 2 || current[1].isEmpty()) {
                        System.out.println("Неправильно введена команда в скрипте");
                        scriptHistory.remove(script);
                        return collection;
                    }
                    collection = makeScript(collection, current[1], saveFile);
                    break;
                default:
                    System.out.println("Неправильно введена команда в скрипте");
                    scriptHistory.remove(script);
                    return collection;
            }
            if (history.size() > 12)
                history.poll();
        }
        scriptHistory.remove(script);
        return collection;
    }

    public static LabWork insertScript(ArrayList<String> strScript, int position) {
        Coordinates coordinates = new Coordinates(Float.parseFloat(strScript.get(position + 2)), Integer.parseInt(strScript.get(position + 3)));
        Person person;

        if (strScript.get(position + 8).isEmpty())
            person = new Person(strScript.get(position + 7), null, Integer.parseInt(strScript.get(position + 9)), Integer.parseInt(strScript.get(position + 10)));
        else {
            Month[] months = Month.values();
            ZoneId zone = ZoneId.of("Europe/Moscow");
            LocalDateTime time = LocalDateTime.of(Integer.parseInt(strScript.get(position + 8)),
                    months[Integer.parseInt(strScript.get(position + 9)) - 1],
                    Integer.parseInt(strScript.get(position + 10)),
                    Integer.parseInt(strScript.get(position + 11)),
                    Integer.parseInt(strScript.get(position + 12)));
            java.time.ZonedDateTime birthday = ZonedDateTime.of(time, zone);

            person = new Person(strScript.get(position + 7), birthday, Integer.parseInt(strScript.get(position + 13)), Integer.parseInt(strScript.get(position + 14)));
        }

        LabWork labWork = new LabWork(strScript.get(position + 1), coordinates, Integer.parseInt(strScript.get(position + 4)), strScript.get(position + 5), Difficulty.valueOf(strScript.get(position + 6)), person);

        return labWork;
    }

    public static int checkScript(ArrayList<String> strScript, int position) {
        if (strScript.size() - position - 1 < 10)
            return 0;
        else {
            String a = Arrays.toString(Difficulty.values());
            if (strScript.get(position + 8).isEmpty()) {
                if (strScript.get(position + 1).isEmpty()
                        || !Menu.isFloat(strScript.get(position + 2))
                        || !Menu.isInteger(strScript.get(position + 3))
                        || !Menu.isInteger(strScript.get(position + 4)) || Integer.parseInt(strScript.get(position + 4)) <= 0
                        || strScript.get(position + 5).isEmpty() || strScript.get(position + 5).length() >= 5207 || strScript.get(position + 5).contains(";")
                        || !a.contains(strScript.get(position + 6))
                        || strScript.get(position + 7).isEmpty()
                        || !strScript.get(position + 8).isEmpty()
                        || !Menu.isInteger(strScript.get(position + 9)) || Integer.parseInt(strScript.get(position + 9)) <= 0
                        || !Menu.isInteger(strScript.get(position + 10)) || Integer.parseInt(strScript.get(position + 10)) <= 0) {
                    return 0;
                }
                else {
                    return 10;
                }
            } else if (strScript.size() - position - 1 >= 14) {
                if (strScript.get(position + 1).isEmpty()
                        || !Menu.isFloat(strScript.get(position + 2))
                        || !Menu.isInteger(strScript.get(position + 3))
                        || !Menu.isInteger(strScript.get(position + 4)) || Integer.parseInt(strScript.get(position + 4)) <= 0
                        || strScript.get(position + 5).isEmpty() || strScript.get(position + 5).length() >= 5207 || strScript.get(position + 5).contains(";")
                        || !a.contains(strScript.get(position + 6))
                        || strScript.get(position + 7).isEmpty()
                        || !Menu.isInteger(strScript.get(position + 8)) || Integer.parseInt(strScript.get(position + 8)) <= 1970 || Integer.parseInt(strScript.get(position + 8)) >= 2023
                        || !Menu.isInteger(strScript.get(position + 9)) || Integer.parseInt(strScript.get(position + 9)) <= 0 || Integer.parseInt(strScript.get(position + 9)) >= 13
                        || !Menu.isInteger(strScript.get(position + 10)) || Integer.parseInt(strScript.get(position + 10)) <= 0 || Integer.parseInt(strScript.get(position + 10)) >= 32
                        || !Menu.isInteger(strScript.get(position + 11)) || Integer.parseInt(strScript.get(position + 11)) < 0 || Integer.parseInt(strScript.get(position + 11)) >= 24
                        || !Menu.isInteger(strScript.get(position + 12)) || Integer.parseInt(strScript.get(position + 12)) < 0 || Integer.parseInt(strScript.get(position + 12)) >= 60
                        || !Menu.isInteger(strScript.get(position + 13)) || Integer.parseInt(strScript.get(position + 13)) <= 0
                        || !Menu.isInteger(strScript.get(position + 14)) || Integer.parseInt(strScript.get(position + 14)) <= 0) {
                    return 0;
                } else {
                    return 14;
                }
            } else {
                return 0;
            }
        }
    }
}
//"C:\Users\egorn\Documents\ИТМО\1_курс\Програмировани\Lab5\pot.txt"