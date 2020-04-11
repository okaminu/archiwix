package lt.okaminu.archiwix;

import lt.okaminu.archiwix.parser.InvalidQueryException;
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
    public void doesNotFindWhenCriteriaDoesNotMatch() {
        Record greenRecord = new Record("green-id123");

        recordService.save(greenRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(id,\"yellow\")");

        assertEquals(of(), actualRecords);
    }

    @Test
    public void findsById() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(id,\"green-id123\")");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void findsByTitle() {
        Record greenRecord = new Record("greenId", "Green Title");
        Record blueRecord = new Record("blueId", "Blue Title");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(title,\"Blue Title\")");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByContent() {
        Record greenRecord = new Record("greenId", "", "Green Content");
        Record blueRecord = new Record("blueId", "", "Blue Content");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(content,\"Blue Content\")");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByViews() {
        Record greenRecord = new Record("greenId", "", "", 123);
        Record blueRecord = new Record("blueId", "", "", 321);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(views,321)");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByTimestamp() {
        Record greenRecord = new Record("greenId", "", "", 0, 1333333333);
        Record blueRecord = new Record("blueId", "", "", 0, 1222222222);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("EQUAL(timestamp,1222222222)");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByGreaterThanButNotEqualsViews() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("GREATER_THAN(views,5)");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void findsByGreaterThanButNotEqualsTimestamp() {
        Record greenRecord = new Record("greenId", "", "", 0, 200);
        Record blueRecord = new Record("blueId", "", "", 0, 300);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("GREATER_THAN(timestamp,200)");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenGreaterThanIsUsedForNonNumericFields() {
        Record record = new Record("green-id123", "Title", "Content", 5, 100);

        recordService.save(record);

        assertThrowsInvalidQueryException("GREATER_THAN(id,\"green-id123\")");
        assertThrowsInvalidQueryException("GREATER_THAN(title\"Title\")");
        assertThrowsInvalidQueryException("GREATER_THAN(content,\"Content\")");
        assertThrowsInvalidQueryException("GREATER_THAN(content,123)");
    }

    @Test
    public void findsByLessThanButNotEqualsViews() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("LESS_THAN(views,10)");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByLessThanButNotEqualsTimestamp() {
        Record greenRecord = new Record("greenId", "", "", 0, 200);
        Record blueRecord = new Record("blueId", "", "", 0, 300);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("LESS_THAN(timestamp,300)");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenLessThanIsUsedForNonNumericFields() {
        Record record = new Record("green-id123", "Title", "Content", 5, 100);

        recordService.save(record);

        assertThrowsInvalidQueryException("LESS_THAN(id,\"green-id123\")");
        assertThrowsInvalidQueryException("LESS_THAN(title\"Title\")");
        assertThrowsInvalidQueryException("LESS_THAN(content,\"Content\")");
        assertThrowsInvalidQueryException("LESS_THAN(content,123)");
    }

    @Test
    public void throwsExceptionWhenQueryIsInvalid() {
        Record greenRecord = new Record("green-id123");

        recordService.save(greenRecord);

        assertThrowsInvalidQueryException("");
        assertThrowsInvalidQueryException("WHEN(id,\"green-id123\")");
        assertThrowsInvalidQueryException("EQUAL(,\"green-id123\")");
        assertThrowsInvalidQueryException("EQUAL(id,)");
        assertThrowsInvalidQueryException("EQUAL(id,\"green-id123\"");
        assertThrowsInvalidQueryException("EQUAL(space,\"green-id123\")");
        assertThrowsInvalidQueryException("EQUAL(id\"green-id123\")");
        assertThrowsInvalidQueryException("EQUALid,\"green-id123\")");
        assertThrowsInvalidQueryException("GREATER_THAN(views,\"green-id123\")");
        assertThrowsInvalidQueryException("LESS_THAN(views,\"green-id123\")");
        assertThrowsInvalidQueryException("NOT(views,123)");
    }

    @Test
    public void findsByNotEqualId() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("NOT(EQUAL(id,\"green-id123\"))");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void findsByDoubleNegation() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("NOT(NOT(EQUAL(id,\"green-id123\")))");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void findsByNotLessThan() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("NOT(LESS_THAN(views,6))");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void findsByNotGreaterThan() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        recordService.save(greenRecord, blueRecord);
        Set<Record> actualRecords = recordService.findBy("NOT(GREATER_THAN(views,6))");

        assertEquals(of(blueRecord), actualRecords);
    }

    private void assertThrowsInvalidQueryException(String query) {
        assertThrows(InvalidQueryException.class, () -> recordService.findBy(query));
    }
}
