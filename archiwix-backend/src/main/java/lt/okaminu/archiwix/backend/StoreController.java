package lt.okaminu.archiwix.backend;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import lt.okaminu.archiwix.core.Record;
import lt.okaminu.archiwix.core.RecordService;
import lt.okaminu.archiwix.core.parser.InvalidQueryException;
import lt.okaminu.archiwix.core.parser.QueryParser;

import java.util.Set;

@Controller("/store")
public class StoreController {

    private final RecordService recordService = new RecordService(new QueryParser());

    @Post
    public void store(@Body Record record) {
        recordService.save(record);
    }

    @Get("{?query}")
    public HttpResponse<Set<Record>> retrieve(@QueryValue("query") String val) {
        try {
            return HttpResponse.ok(recordService.find(val));
        } catch (InvalidQueryException ex) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST);
        }
    }
}