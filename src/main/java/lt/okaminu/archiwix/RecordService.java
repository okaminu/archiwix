package lt.okaminu.archiwix;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.List.of;

public final class RecordService {

    private final Set<Record> records = new HashSet<>();

    public void save(Record ...records) {
        Collection<Record> recordCollection = of(records);
        this.records.removeAll(recordCollection);
        this.records.addAll(recordCollection);
    }

    public Set<Record> findAll() {
        return records;
    }

    public Set<Record> findBy(String query) {
        String operation = "";
        String attributeName = "";
        String attributeValue = "";
        Matcher matcher = Pattern.compile(
                "(EQUAL)\\((id|title|content|views|timestamp),([\"\\sa-zA-Z1-9-]+)\\)"
        ).matcher(query);

        if (matcher.find()) {
            operation = matcher.group(1);
            attributeName = matcher.group(2);
            attributeValue = matcher.group(3);

            if (attributeName.equals("id")) {
                return records.stream()
                        .filter(record -> record.getId().equals(matcher.group(3).replaceAll("\"", "")))
                        .collect(Collectors.toSet());
            }

            if (attributeName.equals("title")) {
                return records.stream()
                        .filter(record -> record.getTitle().equals(matcher.group(3).replaceAll("\"", "")))
                        .collect(Collectors.toSet());
            }

            if (attributeName.equals("content")) {
                return records.stream()
                        .filter(record -> record.getContent().equals(matcher.group(3).replaceAll("\"", "")))
                        .collect(Collectors.toSet());
            }

            if (attributeName.equals("views")) {
                return records.stream()
                        .filter(record -> record.getViews() == Integer.parseInt(matcher.group(3)))
                        .collect(Collectors.toSet());
            }

            if (attributeName.equals("timestamp")) {
                return records.stream()
                        .filter(record -> record.getTimestamp() == Integer.parseInt(matcher.group(3)))
                        .collect(Collectors.toSet());
            }
        }
        throw new InvalidQueryException();
    }
}
