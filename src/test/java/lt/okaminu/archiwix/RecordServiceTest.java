package lt.okaminu.archiwix;

import lt.okaminu.archiwix.parser.QueryParser;
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
public class RecordServiceTest {

    @Mock(lenient = true)
    private QueryParser queryParserStub;

    private RecordService recordService;

    @BeforeEach
    public void setUp() {
        recordService = new RecordService(queryParserStub);
        when(queryParserStub.parse("QUERY")).thenReturn(record -> true);
    }

    @Test
    public void retrievesEmptyRecord() {
        Set<Record> actualRecords = recordService.findBy("QUERY");

        assertEquals(of(), actualRecords);
    }

    @Test
    public void appendsRecordsToExistingOnes() {
        Record record1 = new Record("xbox", "Title");
        Record record2 = new Record("playstation", "Title");

        recordService.save(record1);
        recordService.save(record2);
        Set<Record> actualRecords = recordService.findBy("QUERY");

        assertEquals(of(record1, record2), actualRecords);
    }

    @Test
    public void storesUniqueRecordsByInstance() {
        Record record = new Record("someId");

        recordService.save(record, record);
        Set<Record> actualRecords = recordService.findBy("QUERY");

        assertEquals(of(record), actualRecords);
    }

    @Test
    public void storesUniqueRecordsById() {
        Record record = new Record("someId");
        Record duplicateRecord = new Record("someId");

        recordService.save(record, duplicateRecord);
        Set<Record> actualRecords = recordService.findBy("QUERY");

        assertEquals(of(record), actualRecords);
    }

    @Test
    public void overwritesRecordWhenNewRecordIsWithSameId() {
        Record record = new Record("someId");
        Record updatedRecord = new Record("someId");

        recordService.save(record);
        recordService.save(updatedRecord);
        Set<Record> actualRecords = recordService.findBy("QUERY");

        assertEquals(1, actualRecords.size());
        assertSame(updatedRecord, actualRecords.iterator().next());
    }

    @Test
    public void retrievesRecordsByQuery() {
        RecordService service = new RecordService(new QueryParser());
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        service.save(greenRecord, blueRecord);
        Set<Record> actualRecords = service.findBy("EQUAL(id,\"green-id123\")");

        assertEquals(of(greenRecord), actualRecords);
    }
}
