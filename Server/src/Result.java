import java.util.Map;

public class Result<T extends Person, U extends Integer> {
    private Map<T, U> result;

    public Map<? extends T, ? extends U> getResult() {
        return result;
    }

    public void setResult(Result<? extends T, ? extends U> from) {
        this.result.putAll(from.result);
    }

    public Result<? super T, ? super U> addResult(Result<? super T, ? super U> to) {
        to.result.putAll(this.result);
        return to;
    }

    public Result(Map<T, U> result) {
        this.result = result;
    }

    public int sum() {
        Integer sum = 0;
        for (U x : result.values()) {
            sum += x.intValue();
        }
        return sum;
    }

    public <X extends Person> void findPerson(X x) {
        if (result.containsKey(x))
            System.out.println("true!");
        else
            System.out.println("false");
    }
}
