package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

public final class GreaterThanExpression extends Expression {

    @Override
    protected void interpret(Context context, String attributeName, String attributeValue) {
        if (attributeName.equals("views")) {
            context.setOutput(record -> record.getViews() > Integer.parseInt(attributeValue));
        }

        if (attributeName.equals("timestamp")) {
            context.setOutput(record -> record.getTimestamp() > Integer.parseInt(attributeValue));
        }
    }

    @NotNull
    @Override
    protected String getPattern() {
        return "("+ getOperator() +")\\((views|timestamp),([\"0-9-]+)\\)";
    }

    @NotNull
    @Override
    protected String getOperator() {
        return "GREATER_THAN";
    }
}
