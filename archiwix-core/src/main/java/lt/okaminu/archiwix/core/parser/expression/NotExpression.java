package lt.okaminu.archiwix.core.parser.expression;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class NotExpression extends Expression {

    private final QueryParser queryParser;

    public NotExpression(QueryParser parser) {
        this.queryParser = parser;
    }

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        return queryParser.parse(matcher.group(1)).negate();
    }

    @NotNull
    @Override
    protected String getPattern() {
        return getOperator() + "\\(([\"\\s,_\\(\\)a-zA-Z0-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "NOT";
    }
}
