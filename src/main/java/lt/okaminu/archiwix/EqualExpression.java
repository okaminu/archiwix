package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

public final class EqualExpression extends Expression {

    @Override
    protected void interpret(Context context, String attributeName, String attributeValue) {
        if (attributeName.equals("id")) {
            context.setOutput(record -> record.getId().equals(attributeValue.replaceAll("\"", "")));
        }

        if (attributeName.equals("title")) {
            context.setOutput(record -> record.getTitle().equals(attributeValue.replaceAll("\"", "")));
        }

        if (attributeName.equals("content")) {
            context.setOutput(record -> record.getContent().equals(attributeValue.replaceAll("\"", "")));
        }

        if (attributeName.equals("views")) {
            context.setOutput(record -> record.getViews() == Integer.parseInt(attributeValue));
        }

        if (attributeName.equals("timestamp")) {
            context.setOutput(record -> record.getTimestamp() == Integer.parseInt(attributeValue));
        }
    }

    @NotNull
    @Override
    protected String getPattern() {
        return "(EQUAL)\\((id|title|content|views|timestamp),([\"\\sa-zA-Z1-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "EQUAL";
    }
}
