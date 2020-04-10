package lt.okaminu.archiwix;

import java.util.function.Predicate;

public final class Context {
    private String input;
    private Predicate<Record> output = record -> false;

    public Context(String input) {
        this.input = input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(Predicate<Record> output) {
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public Predicate<Record> getOutput() {
        return output;
    }
}
