package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static aoc2023.TestUtils.assertEquals;

public class Day1b {
    public static void main(String[] args) throws Exception {
        var program = new Day1b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day1-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        System.out.println("Processing input: " + inputFilename);
        var numbers = new ArrayList<Integer>();
        var firstDigitPattern = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        var lastDigitPattern = Pattern.compile(".*(\\d|one|two|three|four|five|six|seven|eight|nine)");

        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String lineNum = "";
                    var matcher = firstDigitPattern.matcher(line);
                    if (matcher.find()) {
                        lineNum += convertNum(matcher.group(1));
                    }
                    var matcher2 = lastDigitPattern.matcher(line);
                    if (matcher2.find()) {
                        lineNum += convertNum(matcher2.group(1));
                    }
                    Integer num = Integer.parseInt(lineNum);
                    numbers.add(num);
                    System.out.println(num + " < " + line);
                }
            }
            //System.out.println(numbers);
            int sum = numbers.stream().reduce(0, Integer::sum);
            System.out.println("Sum: " + sum);

            return sum;
        }
    }

    static Map<String, String> numToValMap = new HashMap<>();
    static {
        numToValMap.putAll(Map.of("one", "1", "two", "2", "three", "3"));
        numToValMap.putAll(Map.of("four", "4", "five", "5", "six", "6"));
        numToValMap.putAll(Map.of("seven", "7", "eight", "8", "nine", "9"));
    }
    private String convertNum(String num) {
        return (num.length() == 1) ? num : numToValMap.get(num);
    }

    private void runTests() throws Exception {
        testRegexOnDigitOrWord();
        testConvertNumWordToVal();
        testMain();
    }

    private void testConvertNumWordToVal() {
        assertEquals(convertNum("one"), "1");
        var words = "one|two|three|four|five|six|seven|eight|nine".split("\\|");
        for (int i = 0; i < words.length; i++) {
            assertEquals(convertNum(words[i]), "" + (i + 1));
        }
    }

    private void testMain() throws Exception {
        Integer sum = runMain("aoc2023/Day1-input1b.txt");
        assertEquals(sum, 281);

        sum = runMain("aoc2023/Day1-input2.txt");
        assertEquals(sum, 54019);
    }

    private void testRegexOnDigitOrWord() {
        // Test regex to get first digit.
        var s = "sdpgz3five4seven6fiveh";
        var p = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        var m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "3");

        s = "sdpgzfive4seven6fiveh";
        p = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "five");

        // Test regex to get last digit
        s = "sdpgzfive4seven6fiveh";
        p = Pattern.compile(".*(\\d|one|two|three|four|five|six|seven|eight|nine)");
        m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "five");

        s = "sdpgzfive4seven6h";
        p = Pattern.compile(".*(\\d|one|two|three|four|five|six|seven|eight|nine)");
        m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "6");
    }
}
