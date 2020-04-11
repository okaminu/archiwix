package lt.okaminu.archiwix;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class QueryParser {

    private final Set<Expression> expressions = new HashSet<>();

    protected QueryParser() {
        expressions.add(new NotExpression(this));
        expressions.add(new LessThanExpression());
        expressions.add(new GreaterThanExpression());
        expressions.add(new EqualExpression());
    }

    protected Predicate<Record> parse(String query) {
        for (Expression expression : expressions)
            if (expression.hasOperator(query))
                return expression.interpret(query);

        throw new InvalidQueryException();
    }
}
