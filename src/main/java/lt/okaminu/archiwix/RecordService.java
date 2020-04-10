package lt.okaminu.archiwix;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RecordService {

    private final Set<Record> records = new HashSet<>();

    public void save(Record ...records) {
        this.records.addAll(List.of(records));
    }

    public Set<Record> findAll() {
        return records;
    }
}
