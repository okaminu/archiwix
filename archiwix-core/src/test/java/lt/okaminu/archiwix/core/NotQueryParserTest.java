package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotQueryParserTest {

    private final QueryParser queryParser = new QueryParser();

    @Test
    public void parsesNOT() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        Set<Record> actualRecords = filterRecords("NOT(EQUAL(id,\"green-id123\"))", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfNOT() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        Set<Record> actualRecords = filterRecords("NOT(NOT(EQUAL(id,\"green-id123\")))", greenRecord, blueRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfMultipleNOTs() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        Set<Record> actualRecords = filterRecords("NOT(NOT(NOT(EQUAL(id,\"green-id123\"))))", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfLESS() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        Set<Record> actualRecords = filterRecords("NOT(LESS_THAN(views,6))", greenRecord, blueRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfGREATER() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);

        Set<Record> actualRecords = filterRecords("NOT(GREATER_THAN(views,6))", greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfAND() {
        Record redRecord = new Record("redId", "RandomTitle");
        Record greenRecord = new Record("greenId", "SomeTitle");

        String query = "NOT(AND(EQUAL(title,\"RandomTitle\"),EQUAL(id,\"redId\")))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, redRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesNOTOfOR() {
        Record redRecord = new Record("redId");
        Record greenRecord = new Record("greenId");
        Record blueRecord = new Record("blueId");

        String query = "NOT(OR(EQUAL(id,\"redId\"),EQUAL(id,\"greenId\")))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenQueryIsInvalid() {
        assertThrows(InvalidQueryException.class, () -> filterRecords("NOT(views,123)"));
    }

    private Set<Record> filterRecords(String query, Record ...records) {
        return of(records).stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
