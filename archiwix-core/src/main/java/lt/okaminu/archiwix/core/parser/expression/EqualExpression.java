package lt.okaminu.archiwix.core.parser.expression;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class EqualExpression extends Expression {

    private final Map<String, BiPredicate<String, Record>> matcherMap = new HashMap<>();

    public EqualExpression() {
        matcherMap.put("id", (String value, Record record) -> record.getId().equals(value.replaceAll("\"", "")));
        matcherMap.put("title", (String value, Record record) -> record.getTitle().equals(value.replaceAll("\"", "")));
        matcherMap.put("views", (String value, Record record) -> equals(value, record.getViews()));
        matcherMap.put("timestamp", (String value, Record record) -> equals(value, record.getTimestamp()));
        matcherMap.put(
                "content",
                (String value, Record record) -> record.getContent().equals(value.replaceAll("\"", ""))
        );
    }

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        String attributeName = matcher.group(1);
        String attributeValue = matcher.group(2);

        if (matcherMap.containsKey(attributeName))
            return record -> matcherMap.get(attributeName).test(attributeValue, record);

        throw new InvalidQueryException("Attribute "+ attributeName +" cannot be used for this operation");
    }

    @NotNull
    @Override
    protected String getPattern() {
        return getOperator() + "\\(([a-z]+),([\"\\sa-zA-Z0-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "EQUAL";
    }

    private static boolean equals(String textNumber, int number) {
        try {
            return number == Integer.parseInt(textNumber);
        } catch (NumberFormatException ex) {
            throw new InvalidQueryException("This field accepts only numeric values");
        }
    }
}
