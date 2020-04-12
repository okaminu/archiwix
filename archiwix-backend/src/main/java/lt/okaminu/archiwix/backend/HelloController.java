package lt.okaminu.archiwix.backend;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lt.okaminu.archiwix.core.Record;

@Controller("/hello")
public class HelloController {

    @Get
    public Record index() {
        return new Record("one", "two", "three", 1, 2);
    }
}