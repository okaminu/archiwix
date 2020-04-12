package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.InvalidQueryException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class GreaterThanExpression extends Expression {

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        String attributeName = matcher.group(1);
        String attributeValue = matcher.group(2);
        if (attributeName.equals("views"))
            return record -> record.getViews() > Integer.parseInt(attributeValue);

        if (attributeName.equals("timestamp"))
            return record -> record.getTimestamp() > Integer.parseInt(attributeValue);

        throw new InvalidQueryException("Attribute "+ attributeName +" cannot be used for this operation");
    }

    @NotNull
    @Override
    protected String getPattern() {
        return getOperator() + "\\(([a-z]+),([\"0-9]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "GREATER_THAN";
    }
}
