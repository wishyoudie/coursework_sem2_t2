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
     * Test Grep with no console options.
     */
    @Test
    void testWithNoOptions() {
        String[] args = {"maria", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("maria is the kindest and the purest person I know");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test Grep with reverse filter option (-v).
     */
    @Test
    void testReversedFlag() {
        String[] args = {"-v", ".", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("maria is the kindest and the purest person I know");
        expected.add("Maria");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test Grep with ignored case filter option (-i).
     */
    @Test
    void testIgnoredCaseFlag() {
        String[] args = {"-i", "G", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("She is very beautiful and loving.");
        expected.add("String three words.");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test Grep with RegEx filter option (-r).
     */
    @Test
    void testRegexFlag() {
        String[] args = {"-r", "[1-9]", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Maria number 1.");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test incompatibility of ignore letters case and RegEx flags.
     */
    @Test
    void testRegexAndIgnoreFlagsIncompatibility() {
        String[] args = {"-r", "-i", "[1-9]", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("Maria number 1.");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test result of running with empty string as key argument.
     */
    @Test
    void testEmptyStringArgument() {
        String[] args = {"", "./src/test/resources/input_test.txt"};
        Grep gr = new Grep(args);
        List<String> expected = new ArrayList<>();
        expected.add("I do really love Maria.");
        expected.add("She is very beautiful and loving.");
        expected.add("maria is the kindest and the purest person I know");
        expected.add("Maria number 1.");
        expected.add("Maria");
        expected.add("String three words.");
        assertEquals(expected, gr.calculate());
        gr.changeArgs(new String[]{"-i", "", "./src/test/resources/input_test.txt"});
        assertEquals(expected, gr.calculate());
        gr.changeArgs(new String[]{"-v", "", "./src/test/resources/input_test.txt"});
        assertEquals(new ArrayList<>(), gr.calculate());
        gr.changeArgs(new String[]{"-i", "-v", "", "./src/test/resources/input_test.txt"});
        assertEquals(new ArrayList<>(), gr.calculate());
    }

    /**
     * Test the feature to change program arguments on the same Grep instance.
     */
    @Test
    void testMultipleGrepInstanceCalls() {
        String[] args_first = {"is", "./src/test/resources/input_test.txt"};
        List<String> expected = new ArrayList<>();
        expected.add("She is very beautiful and loving.");
        expected.add("maria is the kindest and the purest person I know");
        Grep gr = new Grep(args_first);
        assertEquals(expected, gr.calculate());

        String[] args_second = {"really", "./src/test/resources/input_test.txt"};
        gr.changeArgs(args_second);
        expected.clear();
        expected.add("I do really love Maria.");
        assertEquals(expected, gr.calculate());
    }

    /**
     * Test empty result when trying to search in non-existent file.
     */
    @Test
    void testWithNonExistentFile() {
        String[] args = {"Maria", "./src/test/resources/non_existent_input.txt"};
        assertEquals(new ArrayList<>(), new Grep(args).calculate());
    }
}
