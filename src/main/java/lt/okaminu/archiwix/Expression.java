package lt.okaminu.archiwix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    public void interpret(Context context) {
        if (context.getInput().startsWith("EQUAL")) {
            Matcher matcher = Pattern.compile(
                    "(EQUAL)\\((id|title|content|views|timestamp),([\"\\sa-zA-Z1-9-]+)\\)"
            ).matcher(context.getInput());

            if (matcher.find()) {
                String attributeName = matcher.group(2);
                String attributeValue = matcher.group(3);
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

                context.setInput(context.getInput().replaceFirst("(EQUAL)\\((id|title|content|views|timestamp),([\"\\sa-zA-Z1-9-]+)\\)", ""));
            } else {
                throw new InvalidQueryException();
            }
        }
    }
}
