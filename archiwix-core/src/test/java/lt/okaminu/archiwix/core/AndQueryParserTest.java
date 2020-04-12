package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AndQueryParserTest {

    private final QueryParser queryParser = new QueryParser();

    @Test
    public void parsesAND() {
        Record greenRecord = new Record("greenId", "RandomTitle");
        Record blueRecord = new Record("blueId", "RandomTitle");

        String query = "AND(EQUAL(id,\"blueId\"),EQUAL(title,\"RandomTitle\"))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfNOT() {
        Record redRecord = new Record("redId", "RandomTitle");
        Record greenRecord = new Record("greenId", "RandomTitle");
        Record blueRecord = new Record("blueId", "SomeTitle");

        String query = "AND(EQUAL(title,\"RandomTitle\"),NOT(EQUAL(id,\"redId\")))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfDoubleNOT() {
        Record redRecord = new Record("redId");
        Record greenRecord = new Record("greenId");
        Record blueRecord = new Record("blueId");

        String query = "AND(NOT(EQUAL(id,\"blueId\")),NOT(EQUAL(id,\"redId\")))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfLESSWithGREATER() {
        Record redRecord = new Record("redId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record blueRecord = new Record("blueId", "", "", 10);

        String query = "AND(LESS_THAN(views,8),GREATER_THAN(views,3))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfSingleAND() {
        Record greenRecord = new Record("greenId", "RandomTitle", "BoringContent", 5);
        Set<Record> records = of(
                new Record("yellowId", "RandomTitle", "InterestingContent"),
                new Record("redId", "RandomTitle", "BoringContent", 10),
                new Record("blueId", "SomeTitle"),
                greenRecord
        );

        String subExpression = "AND(EQUAL(title,\"RandomTitle\"),EQUAL(content,\"BoringContent\")";
        String query = "AND(" + subExpression + "),LESS_THAN(views,8))";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfDoubleAND() {
        Record greenRecord = new Record("greenId", "RandomTitle", "BoringContent", 5);
        Set<Record> records = of(
                new Record("yellowId", "RandomTitle", "InterestingContent"),
                new Record("whiteId", "RandomTitle", "BoringContent", 2),
                new Record("redId", "RandomTitle", "BoringContent", 10),
                new Record("blueId", "SomeTitle"),
                greenRecord
        );

        String subExpression1 = "AND(EQUAL(title,\"RandomTitle\"),EQUAL(content,\"BoringContent\"))";
        String subExpression2 = "AND(LESS_THAN(views,8),GREATER_THAN(views,3))";
        String query = "AND(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfOR() {
        Record blueRecord = new Record("blueId", "", "", 1);
        Record whiteRecord = new Record("whiteId", "", "", 10);
        Set<Record> records = of(
                new Record("yellowId", "", "", 2),
                new Record("greenId", "", "", 5),
                new Record("redId", "", "", 7),
                blueRecord,
                whiteRecord
        );

        String subExpression1 = "OR(EQUAL(id,\"blueId\"),EQUAL(id,\"whiteId\"))";
        String subExpression2 = "OR(LESS_THAN(views,3),GREATER_THAN(views,6))";
        String query = "AND(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(blueRecord, whiteRecord), actualRecords);
    }

    @Test
    public void parsesANDCompositeOfORWithNOT() {
        Record blueRecord = new Record("blueId", "", "", 1);
        Record whiteRecord = new Record("whiteId", "", "", 10);
        Set<Record> records = of(
                new Record("yellowId", "", "", 2),
                new Record("greenId", "", "", 5),
                new Record("redId", "", "", 7),
                blueRecord,
                whiteRecord
        );

        String subExpression1 = "OR(EQUAL(id,\"blueId\"),EQUAL(id,\"whiteId\"))";
        String subExpression2 = "OR(NOT(LESS_THAN(views,6)),NOT(GREATER_THAN(views,3)))";
        String query = "AND(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(blueRecord, whiteRecord), actualRecords);
    }

    @Test
    public void throwsExceptionWhenQueryIsInvalid() {
        Record record = new Record("green-id123");

        assertThrowsInvalidQueryException("AND(views,123)", record);
        assertThrowsInvalidQueryException("AND(EQUAL(id,\"green-id123\"))", record);
    }

    private void assertThrowsInvalidQueryException(String query, Record ...records) {
        assertThrows(InvalidQueryException.class, () -> filterRecords(query, records));
    }

    private Set<Record> filterRecords(String query, Record ...records) {
        return filterRecords(query, of(records));
    }

    private Set<Record> filterRecords(String query, Set<Record> records) {
        return records.stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
