package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.QueryParser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Record> findBy(String query) {
        return records.stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
