package coursework.sem2.t2;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Grep class
 */
public class Grep {

    /**
     * Inverts filter condition: if set, will return lines NOT containing the keyword.
     */
    @Option(name="-v",usage="Reverse flag")
    private boolean useReversedFlag;

    /**
     * Ignores letters case.
     */
    @Option(name="-i",usage="Ignore letters case flag")
    private boolean useIgnoredCaseFlag;

    /**
     * Changes logic from keyword to regular expression.
     */
    @Option(name="-r",usage="Regex flag", forbids={"-i"})
    private boolean useRegexFlag;

    /**
     * Keyword or RegEx argument.
     */
    @Argument(required = true)
    private String filter;

    /**
     * Input file argument.
     */
    @Argument(required = true, index = 1)
    private File file;

    /**
     * Add a string into a list while minding useReversedFlag helper method.
     * @param condition A condition to be checked.
     * @param list Result list.
     * @param line Line to be added.
     */
    private void addConsideringReversedFlag(boolean condition, List<String> list, String line) {
        if (condition ^ this.useReversedFlag)
            list.add(line);
    }

    /**
     * Read lines from file, then filter them using provided command line arguments.
     * @throws IllegalArgumentException Throws if errors occurred while reaching selected file.
     * @return Resulting list of filtered lines.
     */
    public List<String> calculate() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            List<String> result = new ArrayList<>();
            String currentLine = br.readLine();

            while (currentLine != null) {
                lines.add(currentLine);
                currentLine = br.readLine();
            }

            if (useRegexFlag) {
                Pattern pattern = Pattern.compile(filter);
                for (String line : lines) {
                    Matcher matcher = pattern.matcher(line);
                    addConsideringReversedFlag(matcher.find(), result, line);
                }
            } else {
                if (useIgnoredCaseFlag) {
                    String filterLowered = filter.toLowerCase();
                    for (String line : lines) {
                        String lineLowered = line.toLowerCase();
                        addConsideringReversedFlag(lineLowered.contains(filterLowered), result, line);
                    }
                } else {
                    for (String line : lines) {
                        addConsideringReversedFlag(line.contains(filter), result, line);
                    }
                }
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    /**
     * Constructor from command line arguments.
     * @param args Command line arguments.
     */
    public Grep(String[] args) {
        changeArgs(args);
    }

    /**
     * Parses command line arguments.
     * @throws IllegalArgumentException Throws if errors occurred while parsing command line arguments.
     * @param args Command line arguments.
     */
    public void changeArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Entry point.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        List<String> list = new Grep(args).calculate();
        for (String s : list) {
            System.out.println(s);
        }
    }
}
