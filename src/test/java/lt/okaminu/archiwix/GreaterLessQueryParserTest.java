package lt.okaminu.archiwix;

import lt.okaminu.archiwix.parser.InvalidQueryException;
import lt.okaminu.archiwix.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GreaterLessQueryParserTest {

    private final QueryParser queryParser = new QueryParser();

    @Test
    public void parsesGREATERWithViewsField() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        Set<Record> actualRecords = filterRecords("GREATER_THAN(views,5)", greenRecord, blueRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesGREATERWithTimestampField() {
        Record greenRecord = new Record("greenId", "", "", 0, 200);
        Record blueRecord = new Record("blueId", "", "", 0, 300);

        Set<Record> actualRecords = filterRecords("GREATER_THAN(timestamp,200)", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenGreaterThanIsUsedForNonNumericFields() {
        Record record = new Record("green-id123");

        assertThrowsInvalidQueryException("GREATER_THAN(id,\"green-id123\")", record);
        assertThrowsInvalidQueryException("GREATER_THAN(title\"Title\")", record);
        assertThrowsInvalidQueryException("GREATER_THAN(content,\"Content\")", record);
        assertThrowsInvalidQueryException("GREATER_THAN(content,123)", record);
    }

    @Test
    public void parsesLESSWithViewsField() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        Set<Record> actualRecords = filterRecords("LESS_THAN(views,10)", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesLESSWithTimestampField() {
        Record greenRecord = new Record("greenId", "", "", 0, 200);
        Record blueRecord = new Record("blueId", "", "", 0, 300);

        Set<Record> actualRecords = filterRecords("LESS_THAN(timestamp,300)", greenRecord, blueRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenLessThanIsUsedForNonNumericFields() {
        Record record = new Record("green-id123");

        assertThrowsInvalidQueryException("LESS_THAN(id,\"green-id123\")", record);
        assertThrowsInvalidQueryException("LESS_THAN(title\"Title\")", record);
        assertThrowsInvalidQueryException("LESS_THAN(content,\"Content\")", record);
        assertThrowsInvalidQueryException("LESS_THAN(content,123)", record);
    }

    private void assertThrowsInvalidQueryException(String query, Record ...records) {
        assertThrows(InvalidQueryException.class, () -> filterRecords(query, records));
    }

    private Set<Record> filterRecords(String query, Record ...records) {
        return of(records).stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
