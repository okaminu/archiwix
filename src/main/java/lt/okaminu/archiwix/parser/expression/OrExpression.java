package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.QueryParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class OrExpression extends CompositeExpression {

    public OrExpression(QueryParser queryParser) {
        super(queryParser);
    }

    @Override
    protected Predicate<Record> joinPredicates(Predicate<Record> first, Predicate<Record> second) {
        return first.or(second);
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "OR";
    }
}
