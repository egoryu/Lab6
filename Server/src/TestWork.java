import java.time.ZonedDateTime;

public class TestWork extends LabWork {
    Result<Person, Integer> result;

    public TestWork(String name, Coordinates coordinates, Integer minimalPoint, String description, Difficulty difficulty, Person author) {
        super(name, coordinates, minimalPoint, description, difficulty, author);
    }

    public TestWork(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer minimalPoint, String description, Difficulty difficulty, Person author) {
        super(id, name, coordinates, creationDate, minimalPoint, description, difficulty, author);
    }

}
