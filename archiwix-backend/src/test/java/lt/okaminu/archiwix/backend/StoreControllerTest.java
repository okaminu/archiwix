package lt.okaminu.archiwix.backend;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import lt.okaminu.archiwix.core.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;
import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StoreControllerTest {

    private EmbeddedServer server;
    private HttpClient client;

    @BeforeEach
    void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterEach
    void stopServer() {
        server.stop();
        client.stop();
    }

    @Test
    void providesBadRequestStatusOnInvalidQuery() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");
        store(greenRecord, blueRecord);

        HttpClientResponseException responseException = assertThrows(
                HttpClientResponseException.class, () -> retrieve("EQUAL(views)")
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseException.getResponse().getStatus());
    }

    @Test
    void providesEmptyResult() {
        Set<Record> actualRecords = retrieve("EQUAL(id,\"green-id123\")");

        assertEquals(of(), actualRecords);
    }

    @Test
    void findsByEQUAL() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");

        store(greenRecord, blueRecord);
        Set<Record> actualRecords = retrieve("EQUAL(id,\"green-id123\")");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    void findsByOR() {
        Record greenRecord = new Record("greenId");
        Record blueRecord = new Record("blueId");
        Record redRecord = new Record("redId");
        store(greenRecord, blueRecord, redRecord);

        String query = "OR(EQUAL(id,\"blueId\"),EQUAL(id,\"greenId\"))";
        Set<Record> actualRecords = retrieve(query);

        assertEquals(of(blueRecord, greenRecord), actualRecords);
    }

    @Test
    void findsByAND() {
        Record greenRecord = new Record("greenId", "RandomTitle");
        Record blueRecord = new Record("blueId", "RandomTitle");
        store(greenRecord, blueRecord);

        String query = "AND(EQUAL(id,\"blueId\"),EQUAL(title,\"RandomTitle\"))";
        Set<Record> actualRecords = retrieve(query);

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    void findsByNOT() {
        Record greenRecord = new Record("green-id123");
        Record blueRecord = new Record("blue-id123");
        store(greenRecord, blueRecord);

        Set<Record> actualRecords = retrieve("NOT(EQUAL(id,\"green-id123\"))");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    void findsByGREATER() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);
        store(greenRecord, blueRecord);

        Set<Record> actualRecords = retrieve("GREATER_THAN(views,5)");

        assertEquals(of(greenRecord), actualRecords);
    }

    @Test
    void findsByLESS() {
        Record greenRecord = new Record("greenId", "", "", 10);
        Record blueRecord = new Record("blueId", "", "", 5);
        store(greenRecord, blueRecord);

        Set<Record> actualRecords = retrieve("LESS_THAN(views,10)");

        assertEquals(of(blueRecord), actualRecords);
    }

    @Test
    void findsByORCompositeOfOR() {
        Record blueRecord = new Record("blueId", "", "", 1);
        Record yellowRecord = new Record("yellowId", "", "", 2);
        Record greenRecord = new Record("greenId", "", "", 5);
        Record redRecord = new Record("redId", "", "", 10);
        store(greenRecord, yellowRecord, redRecord, blueRecord);

        String query = "OR(OR(EQUAL(id,\"yellowId\"),EQUAL(id,\"greenId\")),GREATER_THAN(views,8))";
        Set<Record> actualRecords = retrieve(query);

        assertEquals(of(yellowRecord, greenRecord, redRecord), actualRecords);
    }

    private void store(Record ...records) {
        for (Record record: of(records)) {
            HttpResponse<Set<Record>> response = client.toBlocking().exchange(POST("/store", record));
            assertEquals(HttpStatus.OK, response.getStatus());
        }
    }

    private Set<Record> retrieve(String query) {
        String encodedQuery = query.replaceAll(",", "%2C").replaceAll("\"", "%22");
        HttpResponse<Set<Record>> response = client.toBlocking()
                .exchange(GET("/store?query=" + encodedQuery), Argument.setOf(Record.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        return response.body();
    }
}