package lt.okaminu.archiwix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LibraryTest {

    @Test
    public void testAppHasAGreeting() {
        Library classUnderTest = new Library();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
