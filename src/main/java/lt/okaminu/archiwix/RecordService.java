package lt.okaminu.archiwix;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
}
