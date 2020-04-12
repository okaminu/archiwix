package lt.okaminu.archiwix;

import lt.okaminu.archiwix.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.*;

public class RecordServiceTest {

    private final RecordService recordService = new RecordService(new QueryParser());

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

    @Test
    public void retrievesRecordsByQuery() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(id,\"green-id123\")");

        assertEquals(of(greenRecord), actualRecords);
    }
}
