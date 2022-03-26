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
     * Result of grep filtration.
     */
    private List<String> result = new ArrayList<>();

    /**
     * Take account of reverse flag helper method.
     * @param condition Condition to 'change' result of.
     * @return New state of condition based on reverse flag value.
     */
    private boolean useReversedFlag(boolean condition) {
        return condition ^ this.useReversedFlag;
    }

    /**
     * Read lines from file, then filter them using provided command line arguments.
     * @param input Input file.
     * @return Resulting list of filtered lines.
     * @throws IOException Throws if errors occurred while reading file.
     */
    private List<String> run(File input) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
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
                    if (useReversedFlag(matcher.find())) {
                        result.add(line);
                    }
                }
            } else {
                if (useIgnoredCaseFlag) {
                    String filterLowered = filter.toLowerCase();
                    for (String line : lines) {
                        String lineLowered = line.toLowerCase();
                        if (useReversedFlag(lineLowered.contains(filterLowered))) {
                            result.add(line);
                        }
                    }
                } else {
                    for (String line : lines) {
                        if (useReversedFlag(line.contains(filter))) {
                            result.add(line);
                        }
                    }
                }
            }
            return result;
        }
    }

    /**
     * Constructor from command line arguments.
     * @param args Command line arguments.
     */
    public Grep(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
            this.result = run(file);
        } catch (CmdLineException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Grep result getter.
     * @return Result of grep filtration.
     */
    public List<String> getResult() {
        return this.result;
    }

    /**
     * Entry point.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Grep gr = new Grep(args);
        List<String> list = gr.getResult();
        for (String s : list) {
            System.out.println(s);
        }
    }
}
