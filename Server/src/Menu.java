import java.io.IOException;
import java.util.*;

public class Menu {
    public static void help() {
        System.out.println("help: вывести справку по доступным командам");
        System.out.println("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        System.out.println("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("insert null {element} : добавить новый элемент с заданным ключом");
        System.out.println("update id {element} : обновить значение элемента коллекции, id которого равен заданному");
        System.out.println("remove_key null : удалить элемент из коллекции по его ключу");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        System.out.println("exit : завершить программу (без сохранения в файл)");
        System.out.println("history : вывести последние 12 команд (без их аргументов)");
        System.out.println("replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого");
        System.out.println("remove_lower_key null : удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        System.out.println("sum_of_minimal_point : вывести сумму значений поля minimalPoint для всех элементов коллекции");
        System.out.println("max_by_name : вывести любой объект из коллекции, значение поля name которого является максимальным");
        System.out.println("count_by_minimal_point minimalPoint : вывести количество элементов, значение поля minimalPoint которых равно заданному");
    }

    public static void info(LinkedHashMap<String, LabWork> collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста");
        } else {
            System.out.println("Размер коллекции: " + collection.size());
        }

        System.out.println("Тип коллекции: LinkedHashMap<String, LabWork>");
        System.out.println("LabWork: \n" +
                "int id" + "\n" +
                "String name" + "\n" +
                "Coordinates coordinates" + "\n" +
                "java.time.ZonedDateTime creationDate" + "\n" +
                "Integer minimalPoint" + "\n" +
                "String description" + "\n" +
                "Difficulty difficulty" + "\n" +
                "Person author");
        System.out.println("Coordinates:\n" +
                "Float x\n" +
                "Integer y");
        System.out.println("Person\n" +
                "String name\n" +
                "java.time.ZonedDateTime birthday\n" +
                "Integer height\n" +
                "long weight");
    }

    public static void show(LinkedHashMap<String, LabWork> collection) {
        int i = 0;

        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }

        for (Map.Entry<String, LabWork> entry : collection.entrySet()) {
            String str = entry.getKey();
            LabWork labWork = entry.getValue();
            System.out.println(i + ") " + str + " - " + labWork);
            i++;
        }
    }

    public LinkedHashMap<String, LabWork> insert(String lhmKey) {
        LinkedHashMap<String, LabWork> answer = new LinkedHashMap<>();
        Scanner in = new Scanner(System.in);
        String input;

        input = lhmKey;
        while(input.isEmpty() || input.contains(";") || Menu.isOnlyTab(input)) {
            System.out.print("Введите ключ: ");
            if (in.hasNextLine())
                input = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }
        lhmKey = input;

        answer.put(lhmKey, LabWork.insert());

        return answer;
    }

    public LinkedHashMap<String, LabWork> removeKey(LinkedHashMap<String, LabWork> collection, String lhmKey) {
        Scanner in = new Scanner(System.in);

        while(lhmKey.isEmpty() || !collection.containsKey(lhmKey)) {
            System.out.print("Введите ключ по которому надо удалить: ");
            if (in.hasNextLine())
                lhmKey = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }
        collection.remove(lhmKey);

        return collection;
    }

    public static void history(ArrayDeque<String> history) {
        if (history.isEmpty())
            System.out.println("История пуста");
        else {
            System.out.println("Последние 12 команд: ");
            for (String s : history) {
                System.out.println(s);
            }
        }
    }

    public LinkedHashMap<String, LabWork> update(LinkedHashMap<String, LabWork> collection, String id) {
        Scanner in = new Scanner(System.in);
        String key;

        while(!Menu.isInteger(id)) {
            System.out.print("Введите id: ");
            if (in.hasNextLine())
                id = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }

        Iterator iterator = collection.values().iterator();
        int i = 0;
        LabWork tmp = null;

        while (iterator.hasNext()) {
            tmp = (LabWork) iterator.next();
            if (tmp.getId() == Integer.parseInt(id)) {
                break;
            }
            i++;
        }

        if (i != collection.size()) {
            iterator = collection.keySet().iterator();
            while (i-- > 0) {
                iterator.next();
            }
            key = (String) iterator.next();

            collection.replace(key, tmp, LabWork.insert());
        }
        else
            System.out.println("Такого id нет");

        return collection;
    }

    public static int sumOfMinimalPoint(LinkedHashMap<String, LabWork> collection) {
        int res = 0;

        for (LabWork labWork : collection.values()) {
            res += labWork.getMinimalPoint();
        }

        return res;
    }

    public static LabWork maxByName(LinkedHashMap<String, LabWork> collection) {
        LabWork ans = null;
        String res = "";

        for (LabWork labWork : collection.values()) {
            if (labWork.getName().length() >= res.length()) {
                res = labWork.getName();
                ans = labWork;
            }
        }

        return ans;
    }

    public static int countByMinimalPoint(LinkedHashMap<String, LabWork> collection, String minimalPoint) {
        Scanner in = new Scanner(System.in);
        int res = 0;
        int mp;

        while(!Menu.isInteger(minimalPoint) || Integer.parseInt(minimalPoint) <= 0) {
            System.out.print("Введите значение minimal point: ");
            if (in.hasNextLine())
                minimalPoint = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }
        mp = Integer.parseInt(minimalPoint);

        for (LabWork labWork : collection.values()) {
            if (mp == labWork.getMinimalPoint())
                res += labWork.getMinimalPoint();
        }

        return res;
    }

    public static LinkedHashMap<String, LabWork> removeLowerKey(LinkedHashMap<String, LabWork> collection, String lhmKey) {
        Scanner in = new Scanner(System.in);

        while(lhmKey.isEmpty() || lhmKey.contains(";") || Menu.isOnlyTab(lhmKey)) {
            System.out.print("Введите ключ по которому надо удалить: ");
            if (in.hasNextLine())
                lhmKey = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }

        for (String s : collection.keySet()) {
            if (s.length() < lhmKey.length()) {
                collection.remove(s);
            }
        }

        return collection;
    }

    public LinkedHashMap<String, LabWork> replaceIfGreater(LinkedHashMap<String, LabWork> collection, String lhmKey) {
        Scanner in = new Scanner(System.in);

        while(lhmKey.isEmpty() || !collection.containsKey(lhmKey)) {
            System.out.print("Введите ключ по которому надо заменить: ");
            if (in.hasNextLine())
                lhmKey = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }

        LabWork labWork = LabWork.insert();

        if (collection.get(lhmKey).compareTo(labWork) < 0) {
            collection.replace(lhmKey, collection.get(lhmKey), labWork);
            System.out.println("Заменено");
        } else {
            System.out.println("Замены не было");
        }

        return collection;
    }

    public static void savetoFile(LinkedHashMap<String, LabWork> collection, String name, char del) {
        try {
            IO.Write(collection, name, del);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<String, LabWork> executeScript(LinkedHashMap<String, LabWork> collection, String script, String saveFile) throws IOException {
        Scanner in = new Scanner(System.in);

        while(script.isEmpty()) {
            System.out.print("Введите путь к скрипту: ");
            if (in.hasNextLine())
                script = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        }

        collection = Script.makeScript(collection, script, saveFile);
        return collection;
    }
    public static LinkedHashMap<String, LabWork> lhmSort(LinkedHashMap<String, LabWork> collection) {
        if (collection.isEmpty()) {
            return collection;
        }

        List<Map.Entry<String, LabWork>> entries =
                new ArrayList<>(collection.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<String, LabWork>>() {
            public int compare(Map.Entry<String, LabWork> a, Map.Entry<String, LabWork> b){
                return a.getValue().compareTo(b.getValue());
            }
        });

        LinkedHashMap<String, LabWork> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, LabWork> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isOnlyTab(String str) {
        return str.matches("[\\s]+");
    }
}
