package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class NotExpression extends Expression {

    private final QueryParser queryParser;

    protected NotExpression(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        return queryParser.parse(matcher.group(2)).negate();
    }

    @NotNull
    @Override
    protected String getPattern() {
        return "("+ getOperator() +")\\(([\"\\s,_\\(\\)a-zA-Z0-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "NOT";
    }
}
