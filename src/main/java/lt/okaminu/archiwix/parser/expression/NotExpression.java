package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class NotExpression extends Expression {

    //Acyclic dependency here, not proud of it, but tradeoffs were made...
    private final QueryParser queryParser;

    public NotExpression(QueryParser queryParser) {
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
