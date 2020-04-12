package lt.okaminu.archiwix.core.parser.expression;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class AndExpression extends CompositeExpression {

    public AndExpression(QueryParser queryParser) {
        super(queryParser);
    }

    @Override
    protected Predicate<Record> joinPredicates(Predicate<Record> first, Predicate<Record> second) {
        return first.and(second);
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "AND";
    }
}
