package lt.okaminu.archiwix.core.parser.expression;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Expression {
    public final Predicate<Record> interpret(String query) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(query);
        if (matcher.find()) return interpret(matcher);

        throw new InvalidQueryException();
    }

    public final boolean hasOperator(String query) {
        return query.startsWith(getOperator());
    }

    protected abstract Predicate<Record> interpret(Matcher matcher);

    @NotNull
    protected abstract String getPattern();

    @NotNull
    protected abstract String getOperator();
}
