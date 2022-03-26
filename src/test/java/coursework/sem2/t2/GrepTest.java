package coursework.sem2.t2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Grep tests class
 */
class GrepTest {
    /**
     * Test Grep with no console options
     */
    @Test
    void testWithNoOptions() {
        String[] args = {"Как", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Как называют православного программиста?");
        assertEquals(expected, new Grep(args).getResult());
    }

    /**
     * Test Grep with reverse filter option (-v)
     */
    @Test
    void testReversedFlag() {
        String[] args = {"-v", ".", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Как называют православного программиста?");
        expected.add("Кодило");
        assertEquals(expected, new Grep(args).getResult());
    }

    /**
     * Test Grep with ignored case filter option (-i)
     */
    @Test
    void testIgnoredCaseFlag() {
        String[] args = {"-i", "А", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Как называют православного программиста?");
        expected.add(". Точка 1");
        expected.add(". Точка 2.");
        expected.add(". А А А");
        assertEquals(expected, new Grep(args).getResult());
    }

    /**
     * Test Grep with RegEx filter option (-r)
     */
    @Test
    void testRegexFlag() {
        String[] args = {"-r", "^(?:[^\\ ]+\\ ){3}", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Как называют православного программиста?");
        expected.add(". А А А");
        assertEquals(expected, new Grep(args).getResult());
    }

    @Test
    void testRegexAndIgnoreFlagsIncompatibility() {
        String[] args = {"-r", "-i", "^(?:[^\\ ]+\\ ){3}", "./src/test/resources/input_test.txt"};
        assertEquals(new ArrayList<String>(), new Grep(args).getResult());
    }
}