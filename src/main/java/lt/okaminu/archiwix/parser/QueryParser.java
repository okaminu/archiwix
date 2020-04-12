package lt.okaminu.archiwix.parser;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.expression.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class QueryParser {

    private final Set<Expression> expressions = new HashSet<>();

    public QueryParser() {
        expressions.add(new OrExpression(this));
        expressions.add(new AndExpression(this));
        expressions.add(new NotExpression(this));
        expressions.add(new LessThanExpression());
        expressions.add(new GreaterThanExpression());
        expressions.add(new EqualExpression());
    }

    public Predicate<Record> parse(String query) {
        for (Expression expression : expressions)
            if (expression.hasOperator(query))
                return expression.interpret(query);

        throw new InvalidQueryException();
    }
}
