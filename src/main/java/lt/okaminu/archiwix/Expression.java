package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Expression {
    public final Predicate<Record> interpret(String query, Set<Expression> expressions) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(query);

        if (matcher.find()) return interpret(matcher, expressions);

        throw new InvalidQueryException();
    }

    public final boolean hasOperator(String query) {
        return query.startsWith(getOperator());
    }

    protected abstract Predicate<Record> interpret(Matcher matcher, Set<Expression> expressions);

    @NotNull
    protected abstract String getPattern();

    @NotNull
    protected abstract String getOperator();
}
