package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.QueryParser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.function.Predicate;

import static java.util.List.of;

public final class RecordService {

    private final Set<Record> records = new HashSet<>();
    private final QueryParser queryParser;

    public RecordService(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    public void save(Record ...records) {
        this.records.removeAll(of(records));
        this.records.addAll(of(records));
    }

    public Set<Record> find(String query) {
        Predicate<Record> predicate = queryParser.parse(query);
        return records.stream().filter(predicate).collect(Collectors.toSet());
    }
}
