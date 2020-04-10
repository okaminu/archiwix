package lt.okaminu.archiwix;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordServiceTest {

    @Test
    public void retrievesEmptyRecord() {
        RecordService service = new RecordService();

        Set<Record> actualRecords = service.findAll();

        assertEquals(of(), actualRecords);
    }

    @Test
    public void retrievesStoredRecords() {
        RecordService service = new RecordService();
        Record record1 = new Record();
        Record record2 = new Record();

        service.save(record1, record2);
        Set<Record> actualRecords = service.findAll();

        assertEquals(of(record1, record2), actualRecords);
    }
}
