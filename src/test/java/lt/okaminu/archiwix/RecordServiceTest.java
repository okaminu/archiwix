package lt.okaminu.archiwix;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.*;

public class RecordServiceTest {

    private final RecordService recordService = new RecordService();

    @Test
    public void retrievesEmptyRecord() {
        Set<Record> actualRecords = recordService.findAll();

        assertTrue(actualRecords.isEmpty());
    }

    @Test
    public void retrievesStoredRecords() {
        Record record1 = new Record("xbox");
        Record record2 = new Record("playstation");

        recordService.save(record1, record2);
        Set<Record> actualRecords = recordService.findAll();

        assertEquals(of(record1, record2), actualRecords);
    }

    @Test
    public void appendsRecordsToExistingOnes() {
        Record record1 = new Record("xbox");
        Record record2 = new Record("playstation");

        recordService.save(record1);
        recordService.save(record2);
        Set<Record> actualRecords = recordService.findAll();

        assertEquals(of(record1, record2), actualRecords);
    }

    @Test
    public void storesUniqueRecordsByInstance() {
        Record record = new Record("someId");

        recordService.save(record, record);
        Set<Record> actualRecords = recordService.findAll();

        assertEquals(of(record), actualRecords);
    }

    @Test
    public void storesUniqueRecordsById() {
        Record record = new Record("someId");
        Record duplicateRecord = new Record("someId");

        recordService.save(record, duplicateRecord);
        Set<Record> actualRecords = recordService.findAll();

        assertEquals(of(record), actualRecords);
    }

    @Test
    public void overwritesRecordWhenNewRecordIsWithSameId() {
        Record record = new Record("someId");
        Record updatedRecord = new Record("someId");

        recordService.save(record);
        recordService.save(updatedRecord);
        Set<Record> actualRecords = recordService.findAll();

        assertEquals(1, actualRecords.size());
        assertSame(updatedRecord, actualRecords.iterator().next());
    }
}
