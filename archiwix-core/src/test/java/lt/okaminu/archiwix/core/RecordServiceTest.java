package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.QueryParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock(lenient = true)
    private QueryParser queryParserStub;

    private RecordService recordService;

    @BeforeEach
    void setUp() {
        recordService = new RecordService(queryParserStub);
        when(queryParserStub.parse("QUERY")).thenReturn(record -> true);
    }

    @Test
    void retrievesEmptyRecord() {
        Set<Record> actualRecords = recordService.find("QUERY");

        assertEquals(of(), actualRecords);
    }

    @Test
    void appendsRecordsToExistingOnes() {
        Record record1 = new Record("xbox", "Title");
        Record record2 = new Record("playstation", "Title");
        recordService.save(record1);
        recordService.save(record2);

        Set<Record> actualRecords = recordService.find("QUERY");

        assertEquals(of(record1, record2), actualRecords);
    }

    @Test
    void storesUniqueRecordsByInstance() {
        Record record = new Record();
        recordService.save(record, record);

        Set<Record> actualRecords = recordService.find("QUERY");

        assertEquals(of(record), actualRecords);
    }

    @Test
    void storesUniqueRecordsById() {
        Record record = new Record("someId");
        Record duplicateRecord = new Record("someId");
        recordService.save(record, duplicateRecord);

        Set<Record> actualRecords = recordService.find("QUERY");

        assertEquals(of(record), actualRecords);
    }

    @Test
    void overwritesRecordWhenNewRecordIsWithSameId() {
        Record record = new Record();
        Record updatedRecord = new Record();
        recordService.save(record);
        recordService.save(updatedRecord);

        Set<Record> actualRecords = recordService.find("QUERY");

        assertEquals(1, actualRecords.size());
        assertSame(updatedRecord, actualRecords.iterator().next());
    }

    @Test
    void retrievesRecordsByQuery() {
        RecordService service = new RecordService(new QueryParser());
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");
        service.save(greenRecord, blueRecord);

        Set<Record> actualRecords = service.find("EQUAL(id,\"green-id123\")");

        assertEquals(of(greenRecord), actualRecords);
    }
}
