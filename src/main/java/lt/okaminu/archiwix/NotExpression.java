package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class NotExpression extends Expression {

    @Override
    protected Predicate<Record> interpret(Matcher matcher, Set<Expression> expressions) {
        String subQuery = matcher.group(2);
        for (Expression expression : expressions)
            if (expression.hasOperator(subQuery))
                return expression.interpret(subQuery, expressions).negate();

            throw new InvalidQueryException();
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
