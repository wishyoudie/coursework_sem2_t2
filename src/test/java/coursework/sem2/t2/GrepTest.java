package coursework.sem2.t2;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Grep tests class
 */
class GrepTest {
    String testFilePath = getClass().getClassLoader().getResource("input_test.txt").getPath();

    /**
     * Test Grep with no console options.
     */
    @Test
    void testWithNoOptions() {
        String[] args = {"maria", testFilePath};
        List<String> expected = new ArrayList<>();
        expected.add("maria is the kindest and the purest person I know");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test Grep with reverse filter option (-v).
     */
    @Test
    void testReversedFlag() {
        String[] args = {"-v", ".", testFilePath};
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
        String[] args = {"-i", "G", testFilePath};
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
        String[] args = {"-r", "[1-9]", testFilePath};
        List<String> expected = new ArrayList<>();
        expected.add("Maria number 1.");
        assertEquals(expected, new Grep(args).calculate());
    }

    /**
     * Test incompatibility of ignore letters case and RegEx flags.
     */
    @Test
    void testRegexAndIgnoreFlagsIncompatibility() {
        String[] args = {"-r", "-i", "[1-9]", testFilePath};
        List<String> expected = new ArrayList<>();
        expected.add("Maria number 1.");
        assertThrows(IllegalArgumentException.class, () -> new Grep(args).calculate());
    }

    /**
     * Test result of running with empty string as key argument.
     */
    @Test
    void testEmptyStringArgument() {
        String[] args = {"", testFilePath};
        Grep gr = new Grep(args);
        List<String> expected = new ArrayList<>();
        expected.add("I do really love Maria.");
        expected.add("She is very beautiful and loving.");
        expected.add("maria is the kindest and the purest person I know");
        expected.add("Maria number 1.");
        expected.add("Maria");
        expected.add("String three words.");
        assertEquals(expected, gr.calculate());
        gr.changeArgs(new String[]{"-i", "", testFilePath});
        assertEquals(expected, gr.calculate());
        gr.changeArgs(new String[]{"-v", "", testFilePath});
        assertEquals(new ArrayList<>(), gr.calculate());
        gr.changeArgs(new String[]{"-i", "-v", "", testFilePath});
        assertEquals(new ArrayList<>(), gr.calculate());
    }

    /**
     * Test the feature to change program arguments on the same Grep instance.
     */
    @Test
    void testMultipleGrepInstanceCalls() {
        String[] argsFirst = {"is", testFilePath};
        List<String> expected = new ArrayList<>();
        expected.add("She is very beautiful and loving.");
        expected.add("maria is the kindest and the purest person I know");
        Grep gr = new Grep(argsFirst);
        assertEquals(expected, gr.calculate());

        String[] argsSecond = {"really", testFilePath};
        gr.changeArgs(argsSecond);
        expected.clear();
        expected.add("I do really love Maria.");
        assertEquals(expected, gr.calculate());
    }

    /**
     * Test empty result when trying to search in non-existent file.
     */
    @Test
    void testWithNonExistentFile() {
        String[] args = {"Maria", "VI:/coursework_sem2_t2/this_doesnt_exis.txt"};
        assertThrows(IllegalArgumentException.class, () -> new Grep(args).calculate());
    }
}
