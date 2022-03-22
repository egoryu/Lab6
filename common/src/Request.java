import java.io.Serializable;

public class Request implements Serializable {
    private String command;
    private String argument;
    private LabWork target;

    Request(String command) {
        this.command = command;
    }

    Request(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    Request(String command, String argument, LabWork target) {
        this.command = command;
        this.argument = argument;
        this.target = target;
    }
}
