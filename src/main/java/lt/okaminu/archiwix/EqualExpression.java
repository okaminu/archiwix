package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class EqualExpression extends Expression {

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        String attributeName = matcher.group(2);
        String attributeValue = matcher.group(3);
        if (attributeName.equals("id")) {
            return record -> record.getId().equals(attributeValue.replaceAll("\"", ""));
        }

        if (attributeName.equals("title")) {
            return record -> record.getTitle().equals(attributeValue.replaceAll("\"", ""));
        }

        if (attributeName.equals("content")) {
            return record -> record.getContent().equals(attributeValue.replaceAll("\"", ""));
        }

        if (attributeName.equals("views")) {
            return record -> record.getViews() == Integer.parseInt(attributeValue);
        }

        if (attributeName.equals("timestamp")) {
            return record -> record.getTimestamp() == Integer.parseInt(attributeValue);
        }
        throw new InvalidQueryException("Attribute "+ attributeName +" cannot be used for this operation");
    }

    @NotNull
    @Override
    protected String getPattern() {
        return "("+ getOperator() +")\\(([a-z]+),([\"\\sa-zA-Z0-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "EQUAL";
    }
}
