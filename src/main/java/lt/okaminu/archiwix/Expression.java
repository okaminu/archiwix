package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Expression {
    public final void interpret(Context context) {
        if (context.getInput().startsWith(getOperator())) {
            Matcher matcher = Pattern.compile(getPattern()).matcher(context.getInput());

            if (matcher.find()) {
                interpret(context, matcher.group(2), matcher.group(3));
                context.setInput(context.getInput().replaceFirst(getPattern(), ""));
            } else {
                throw new InvalidQueryException();
            }
        }
    }

    protected abstract void interpret(Context context, String attributeName, String attributeValue);

    @NotNull
    protected abstract String getPattern();

    @NotNull
    protected abstract String getOperator();
}
