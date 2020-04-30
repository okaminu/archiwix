package lt.okaminu.archiwix.core;

import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import lt.okaminu.archiwix.core.parser.QueryParser;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrQueryParserTest {

    private final QueryParser queryParser = new QueryParser();

    @Test
    void parsesOR() {
        Record greenRecord = new Record("greenId");
        Record blueRecord = new Record("blueId");
        Record redRecord = new Record("redId");

        String query = "OR(EQUAL(id,\"blueId\"),EQUAL(id,\"greenId\"))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(blueRecord, greenRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfNOT() {
        Record redRecord = new Record("redId");
        Record greenRecord = new Record("greenId");
        Record blueRecord = new Record("blueId");

        String query = "OR(EQUAL(id,\"redId\"),NOT(EQUAL(id,\"greenId\")))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(redRecord, blueRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfDoubleNOT() {
        Record redRecord = new Record("redId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record blueRecord = new Record("blueId", "", "", 10);

        String query = "OR(NOT(LESS_THAN(views,8)),NOT(GREATER_THAN(views,4)))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(redRecord, blueRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfLESSWithGREATER() {
        Record redRecord = new Record("redId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record blueRecord = new Record("blueId", "", "", 10);

        String query = "OR(LESS_THAN(views,4),GREATER_THAN(views,7))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, blueRecord, redRecord);

        assertEquals(of(redRecord, blueRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfSingleOR() {
        Record blueRecord = new Record("blueId", "", "", 1);
        Record yellowRecord = new Record("yellowId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record redRecord = new Record("redId", "", "", 10);

        String query = "OR(OR(EQUAL(id,\"yellowId\"),EQUAL(id,\"greenId\")),GREATER_THAN(views,8))";
        Set<Record> actualRecords = filterRecords(query, greenRecord, yellowRecord, redRecord, blueRecord);

        assertEquals(of(yellowRecord, greenRecord, redRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfDoubleOR() {
        Record blueRecord = new Record("blueId", "", "", 1);
        Record yellowRecord = new Record("yellowId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record redRecord = new Record("redId", "", "", 7);
        Record whiteRecord = new Record("whiteId", "", "", 10);

        String subExpression1 = "OR(EQUAL(id,\"yellowId\"),EQUAL(id,\"greenId\"))";
        String subExpression2 = "OR(LESS_THAN(views,2),GREATER_THAN(views,8))";
        String query = "OR(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(
                query,
                greenRecord,
                blueRecord,
                yellowRecord,
                redRecord,
                whiteRecord
        );

        assertEquals(of(greenRecord, yellowRecord, whiteRecord, blueRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfAND() {
        Record blueRecord = new Record("blueId", "RandomTitle", "RandomContent", 1);
        Record yellowRecord = new Record("yellowId", "RandomTitle", "RandomContent", 2);
        Record redRecord = new Record("redId", "SomeTitle", "SomeContent", 7);
        Set<Record> records = of(
                new Record("greenId", "RandomTitle", "SomeContent", 5),
                new Record("whiteId", "SomeTitle", "", 10),
                blueRecord,
                yellowRecord,
                redRecord
        );

        String subExpression1 = "AND(EQUAL(title,\"RandomTitle\"),EQUAL(content,\"RandomContent\"))";
        String subExpression2 = "AND(LESS_THAN(views,8),GREATER_THAN(views,6))";
        String query = "OR(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(blueRecord, yellowRecord, redRecord), actualRecords);
    }

    @Test
    void parsesORCompositeOfANDWithNOT() {
        Record blueRecord = new Record("blueId", "RandomTitle", "RandomContent", 1);
        Record yellowRecord = new Record("yellowId", "RandomTitle", "RandomContent", 2);
        Record redRecord = new Record("redId", "SomeTitle", "SomeContent", 7);
        Set<Record> records = of(
                new Record("greenId", "RandomTitle", "SomeContent", 5),
                new Record("whiteId", "SomeTitle", "", 10),
                blueRecord,
                yellowRecord,
                redRecord
        );

        String subExpression1 = "AND(EQUAL(title,\"RandomTitle\"),EQUAL(content,\"RandomContent\"))";
        String subExpression2 = "AND(NOT(GREATER_THAN(views,8)),NOT(LESS_THAN(views,6)))";
        String query = "OR(" + subExpression1 + "," + subExpression2 + ")";
        Set<Record> actualRecords = filterRecords(query, records);

        assertEquals(of(blueRecord, yellowRecord, redRecord), actualRecords);
    }

    @Test
    void throwsExceptionWhenQueryIsInvalid() {
        Record record = new Record("green-id123");

        assertThrows(InvalidQueryException.class, () -> filterRecords("OR(views,123)", record));
        assertThrows(InvalidQueryException.class, () -> filterRecords("OR(EQUAL(id,\"green-id123\"))", record));
    }

    private Set<Record> filterRecords(String query, Record ...records) {
        return filterRecords(query, of(records));
    }

    private Set<Record> filterRecords(String query, Set<Record> records) {
        return records.stream().filter(queryParser.parse(query)).collect(Collectors.toSet());
    }
}
