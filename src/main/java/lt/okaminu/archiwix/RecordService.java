package lt.okaminu.archiwix;

import java.util.Collection;
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
        Collection<Record> recordCollection = of(records);
        this.records.removeAll(recordCollection);
        this.records.addAll(recordCollection);
    }

    public Set<Record> findAll() {
        return records;
    }

    public Set<Record> findBy(String query) {
        return records.stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
