package lt.okaminu.archiwix.core.parser;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.expression.*;

import java.util.Set;
import java.util.function.Predicate;

public class QueryParser {

    private final Set<Expression> expressions = Set.of(
            new OrExpression(this),
            new AndExpression(this),
            new NotExpression(this),
            new LessThanExpression(),
            new GreaterThanExpression(),
            new EqualExpression()
    );

    public Predicate<Record> parse(String query) {
        for (Expression expression : expressions)
            if (expression.hasOperator(query))
                return expression.interpret(query);

        throw new InvalidQueryException();
    }
}
