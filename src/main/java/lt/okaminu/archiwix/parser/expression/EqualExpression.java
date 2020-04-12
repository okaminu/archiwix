package lt.okaminu.archiwix.parser.expression;

import lt.okaminu.archiwix.Record;
import lt.okaminu.archiwix.parser.InvalidQueryException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class EqualExpression extends Expression {

    private final Map<String, BiPredicate<String, Record>> actionMap = new HashMap<>();

    public EqualExpression() {
        actionMap.put("id", (String value, Record record) -> record.getId().equals(value.replaceAll("\"", "")));
        actionMap.put("title", (String value, Record record) -> record.getTitle().equals(value.replaceAll("\"", "")));
        actionMap.put("views", (String value, Record record) -> record.getViews() == Integer.parseInt(value));
        actionMap.put("timestamp", (String value, Record record) -> record.getTimestamp() == Integer.parseInt(value));
        actionMap.put(
                "content",
                (String value, Record record) -> record.getContent().equals(value.replaceAll("\"", ""))
        );
    }

    @Override
    protected Predicate<Record> interpret(Matcher matcher) {
        String attributeName = matcher.group(1);
        String attributeValue = matcher.group(2);

        if (actionMap.containsKey(attributeName))
            return record -> actionMap.get(attributeName).test(attributeValue, record);

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
}
