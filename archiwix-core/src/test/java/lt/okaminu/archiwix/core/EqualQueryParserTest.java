package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EqualQueryParserTest {

    private final QueryParser queryParser = new QueryParser();

    @Test
    public void parsesEQUALWithIdField() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        Set<Record> actualRecords = filterRecords("EQUAL(id,\"green-id123\")", greenRecord, blueRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesEQUALWithTitleField() {
        Record greenRecord = new Record("greenId", "Green Title");
        Record blueRecord = new Record("blueId", "Blue Title");

        Set<Record> actualRecords = filterRecords("EQUAL(title,\"Blue Title\")", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesEQUALWithContentField() {
        Record greenRecord = new Record("greenId", "", "Green Content");
        Record blueRecord = new Record("blueId", "", "Blue Content");

        Set<Record> actualRecords = filterRecords("EQUAL(content,\"Blue Content\")", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesEQUALWithViewsField() {
        Record greenRecord = new Record("greenId", "", "", 123);
        Record blueRecord = new Record("blueId", "", "", 321);

        Set<Record> actualRecords = filterRecords("EQUAL(views,321)", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesEQUALWithTimestampField() {
        Record greenRecord = new Record("greenId", "", "", 0, 1333333333);
        Record blueRecord = new Record("blueId", "", "", 0, 1222222222);

        Set<Record> actualRecords = filterRecords("EQUAL(timestamp,1222222222)", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenQueryIsInvalid() {
        Record record = new Record("green-id123");

        assertThrowsInvalidQueryException("", record);
        assertThrowsInvalidQueryException("WHEN(id,\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUAL(,\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUAL(id,)", record);
        assertThrowsInvalidQueryException("EQUAL(id,\"green-id123\"", record);
        assertThrowsInvalidQueryException("EQUAL(space,\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUAL(id\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUAL(views,\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUAL(timestamp,\"green-id123\")", record);
        assertThrowsInvalidQueryException("EQUALid,\"green-id123\")", record);
    }

    private void assertThrowsInvalidQueryException(String query, Record ...records) {
        assertThrows(InvalidQueryException.class, () -> filterRecords(query, records));
    }

    private Set<Record> filterRecords(String query, Record ...records) {
        return of(records).stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
