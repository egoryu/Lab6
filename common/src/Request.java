import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Request implements Serializable {
    private String command;
    private String argument;
    private LabWork target;

    Request() {
        this.command = null;
        this.argument = null;
        this.target = null;
    }

    Request(String command) {
        this.command = command;
        this.argument = null;
        this.target = null;
    }

    Request(String command, String argument) {
        this.command = command;
        this.argument = argument;
        this.target = null;
    }

    Request(String command, String argument, LabWork target) {
        this.command = command;
        this.argument = argument;
        this.target = target;
    }

    public String getCommand() {
        return command;
    }

    public String getArgument() {
        return argument;
    }

    public LabWork getTarget() {
        return target;
    }
/*private void  writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
    }*/
}
