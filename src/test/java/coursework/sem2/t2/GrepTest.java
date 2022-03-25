package coursework.sem2.t2;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Grep tests class
 */
class GrepTest {

    /**
     * Get result of running Grep with specified argument list
     * @param args Console argument list
     * @return Console output
     */
    String getGrepConsoleOutput(String[] args) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
        Grep.main(args);
        System.out.flush();
        System.setOut(old);

        return byteArrayOutputStream.toString();
    }

    /**
     * Test Grep with no console options
     */
    @Test
    void testWithNoOptions() {
        String[] args = {"Как", "input_test.txt"};
        String actual = getGrepConsoleOutput(args);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(expected);
        System.setOut(ps);
        System.out.println("Как называют православного программиста?");

        System.out.flush();
        System.setOut(old);

        assertEquals(expected.toString(), actual);
    }

    /**
     * Test Grep with reverse filter option (-v)
     */
    @Test
    void testReversedFlag() {
        String[] args = {"-v", ".", "input_test.txt"};
        String actual = getGrepConsoleOutput(args);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(expected);
        System.setOut(ps);
        System.out.println("Как называют православного программиста?");
        System.out.println("Кодило");

        System.out.flush();
        System.setOut(old);

        assertEquals(expected.toString(), actual);
    }

    /**
     * Test Grep with ignored case filter option (-i)
     */
    @Test
    void testIgnoredCaseFlag() {
        String[] args = {"-i", "А", "input_test.txt"};
        String actual = getGrepConsoleOutput(args);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(expected);
        System.setOut(ps);
        System.out.println("Как называют православного программиста?");
        System.out.println(". Точка 1");
        System.out.println(". Точка 2.");
        System.out.println(". А А А");

        System.out.flush();
        System.setOut(old);

        assertEquals(expected.toString(), actual);
    }

    /**
     * Test Grep with RegEx filter option (-r)
     */
    @Test
    void testRegexFlag() {
        String[] args = {"-r", "^(?:[^\\ ]+\\ ){3}", "input_test.txt"};
        String actual = getGrepConsoleOutput(args);
        ByteArrayOutputStream expected = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(expected);
        System.setOut(ps);
        System.out.println("Как называют православного программиста?");
        System.out.println(". А А А");

        System.out.flush();
        System.setOut(old);

        assertEquals(expected.toString(), actual);
    }
}