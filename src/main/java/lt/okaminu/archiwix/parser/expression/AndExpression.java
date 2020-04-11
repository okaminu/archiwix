package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class AndExpression extends Expression {

    private final QueryParser queryParser;

    public AndExpression(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        Predicate<Record> firstPredicate = queryParser.parse(matcher.group(1));
        Predicate<Record> secondPredicate = queryParser.parse(matcher.group(2));
        return firstPredicate.and(secondPredicate);
    }

    @NotNull
    @Override
    protected String getPattern() {
        String regexp = "\\(([A-Z_]+\\([\"\\s,_\\(\\)a-zA-Z0-9-]+\\)),([A-Z_]+\\([\"\\s,_\\(\\)a-zA-Z0-9-]+\\))\\)";
        return getOperator() + regexp;
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "AND";
    }
}
