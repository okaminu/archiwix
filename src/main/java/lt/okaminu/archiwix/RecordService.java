package lt.okaminu.archiwix;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
        Context context = new Context(query);
        new Expression().interpret(context);

        if(!context.getInput().isEmpty()) {
            throw new InvalidQueryException();
        }

        return records.stream().filter(context.getOutput()).collect(Collectors.toSet());
    }
}
