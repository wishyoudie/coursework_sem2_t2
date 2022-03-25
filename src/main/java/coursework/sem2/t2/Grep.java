package coursework.sem2.t2;

import org.kohsuke.args4j.Argument;
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
    @Option(name="-r",usage="Regex flag")
    private boolean useRegexFlag;

    /**
     * Keyword or RegEx argument.
     */
    @Argument(required = true)
    private String filter;

    /**
     * Input file name argument.
     */
    @Argument(required = true, index = 1)
    private String fileName;

    /**
     * Read lines from file, then filter them using provided command line arguments.
     * @param inputName Input file name
     * @throws IOException Throws if errors occurred while reading file
     */
    private void grep(String inputName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            List<String> lines = new ArrayList<>();
            String currentLine = br.readLine();

            while (currentLine != null) {
                lines.add(currentLine);
                currentLine = br.readLine();
            }

            if (useRegexFlag) {
                Pattern pattern = Pattern.compile(filter);
                for (String line : lines) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find() ^ useReversedFlag) {
                        System.out.println(line);
                    }
                }
            } else {
                if (useIgnoredCaseFlag) {
                    String wordLowered = filter.toLowerCase();
                    for (String line : lines) {
                        String lineLowered = line.toLowerCase();
                        if (lineLowered.contains(wordLowered) ^ useReversedFlag) {
                            System.out.println(line);
                        }
                    }
                } else {
                    for (String line : lines) {
                        if (line.contains(filter) ^ useReversedFlag) {
                            System.out.println(line);
                        }
                    }
                }
            }
        }
    }

    /**
     * Command line arguments parser.
     * @param args Command line arguments
     */
    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            grep(String.format("./files/%s", fileName));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Entry point.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new Grep().launch(args);
    }
}
