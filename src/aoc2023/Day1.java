package aoc2023;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static aoc2023.Utils.assertEquals;

public class Day1 {
    public static void main(String[] args) {
        var program = new Day1();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day1-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var numbers = new ArrayList<Integer>();
        var firstDigitPattern = Pattern.compile("(\\d)");
        var lastDigitPattern = Pattern.compile(".*(\\d)");
        var cl = Thread.currentThread().getContextClassLoader();
        var lines = Utils.readLines(fileName);
        for (var line : lines) {
            String lineNum = "";
            var matcher = firstDigitPattern.matcher(line);
            if (matcher.find()) {
                lineNum += matcher.group(1);
            }
            var matcher2 = lastDigitPattern.matcher(line);
            if (matcher2.find()) {
                lineNum += matcher2.group(1);
            }
            Integer num = Integer.parseInt(lineNum);
            numbers.add(num);
            System.out.println(num + " < " + line);
        }

        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        return sum;
    }

    private void runTests() {
        testRegexOnDigits();
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day1-input1.txt");
        assertEquals(sum, 142);

        sum = runMain("src/aoc2023/Day1-input2.txt");
        assertEquals(sum, 54632);
    }

    private void testRegexOnDigits() {
        // Test regex to get first digit.
        var s = "sdpgz3five4seven6fiveh";
        var p = Pattern.compile("(\\d)");
        var m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "3");

        // Test regex to get last digit.
        s = "sdpgz3five4seven6fiveh";
        p = Pattern.compile(".*(\\d)");
        m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "6");

        s = "sdpgz3";
        p = Pattern.compile(".*(\\d)");
        m = p.matcher(s);
        m.find();
        assertEquals(m.group(1), "3");
    }
}
