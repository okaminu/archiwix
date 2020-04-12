package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.InvalidQueryException;
import lt.okaminu.archiwix.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public abstract class CompositeExpression extends Expression {

    private final QueryParser queryParser;

    protected CompositeExpression(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    @Override
    protected final Predicate<Record> interpret(Matcher matcher) {
        String subQuery = matcher.group(1);
        int index = findExpressionSeparatorIndex(subQuery);

        return joinPredicates(
                queryParser.parse(subQuery.substring(0, index)),
                queryParser.parse(subQuery.substring(index + 1))
        );
    }

    @NotNull
    @Override
    protected final String getPattern() {
        return getOperator() + "\\(([\"\\s,_\\(\\)a-zA-Z0-9-]+)\\)";
    }

    protected abstract Predicate<Record> joinPredicates(Predicate<Record> first, Predicate<Record> second);

    private int findExpressionSeparatorIndex(String query) {
        int openedParentheses = 0;
        for (int i = 0; i < query.length(); i++) {
            String character = String.valueOf(query.charAt(i));
            openedParentheses = character.equals("(") ? openedParentheses + 1 : openedParentheses;
            openedParentheses = character.equals(")") ? openedParentheses - 1 : openedParentheses;

            if (character.equals(",") && openedParentheses == 0) return i;
        }
        throw new InvalidQueryException("Missing comma that separates expressions");
    }
}
