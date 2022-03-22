import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class LabWork implements Comparable<LabWork>{
    static int lastid = 0;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private String description; //Длина строки не должна быть больше 5207, Поле не может быть null
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле не может быть null

    public LabWork(String name, Coordinates coordinates, Integer minimalPoint, String description,
                   Difficulty difficulty, Person author) {
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;
        this.creationDate = ZonedDateTime.now();
        this.id = lastid++;
    }

    public LabWork(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer minimalPoint,
                   String description, Difficulty difficulty, Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;
        lastid = Math.max(lastid, id + 1);
    }

    public int getId() {
        return id;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public String getName() {
        return name;
    }

    public static LabWork insert() {
        Scanner in = new Scanner(System.in);
        String input = "";
        String name = ""; //Поле не может быть null, Строка не может быть пустой
        Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
        String description = ""; //Длина строки не должна быть больше 5207, Поле не может быть null
        Difficulty difficulty; //Поле не может быть null

        do {
            System.out.print("Введите название работы: ");
            if (in.hasNextLine())
                name = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        } while(name.isEmpty() || name.contains(";") || Menu.isOnlyTab(name));

        do {
            System.out.print("Введите минимальный бал: ");
            if (in.hasNextLine())
                input = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        } while(!Menu.isInteger(input) || Integer.parseInt(input) <= 0);
        minimalPoint = Integer.parseInt(input);

        do {
            System.out.print("Введите описание работы: ");
            if (in.hasNextLine())
                description = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        } while(description.isEmpty() || description.length() >= 5207 || input.contains(";") || Menu.isOnlyTab(name));

        String a = Arrays.toString(Difficulty.values());
        do {
            System.out.println(a);
            System.out.print("Выберите уровень сложности: ");
            if (in.hasNextLine())
                input = in.nextLine();
            else {
                System.out.println("Плохой символ");
                System.exit(0);
            }
        } while(!a.contains(input));
        difficulty = Difficulty.valueOf(input);

        return new LabWork(name, Coordinates.insert(), minimalPoint, description, difficulty, Person.insert());
    }

    public String parse(char del) {
        return Integer.toString(id) +
                del + name +
                del + coordinates.parse(del) +
                del + creationDate.getYear() +
                del + creationDate.getMonthValue() +
                del + creationDate.getDayOfMonth() +
                del + creationDate.getHour() +
                del + creationDate.getMinute() +
                del + creationDate.getSecond() +
                del + minimalPoint +
                del + description +
                del + difficulty +
                del + author.parse(del);
    }

    @Override
    public int compareTo(LabWork o) {
        return name.compareTo(o.name) == 0 ? creationDate.compareTo(o.creationDate) : name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", \n\tcoordinates=" + coordinates +
                ", \n\tcreationDate=" + creationDate +
                ", \n\tminimalPoint=" + minimalPoint +
                ", \n\tdescription='" + description + '\'' +
                ", \n\tdifficulty=" + difficulty +
                ", \n\tauthor=" + author +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, description, difficulty, author);
    }

}
