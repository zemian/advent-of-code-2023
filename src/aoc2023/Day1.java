package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day1 {
    public static void main(String[] args) throws Exception {
        run();
        //testParseLine();
    }
    public static void run() throws Exception {
        var numbers = new ArrayList<Integer>();
        var firstDigitPattern = Pattern.compile("(\\d)");
        var lastDigitPattern = Pattern.compile(".*(\\d)");

        var inputFilename = "aoc2023/Day1-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
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
            }
            int sum = numbers.stream().reduce(0, Integer::sum);
            System.out.println(sum);
            // 54632
        }
    }

    public static void testParseLine() throws Exception {
        // Test regex to get first and last digit.
        var s = "sdpgz3five4seven6fiveh";
        var p = Pattern.compile(".*(\\d)");
        var m = p.matcher(s);
        m.find();
        System.out.println(m.group(1));

        s = "sdpgz3";
        p = Pattern.compile(".*(\\d)");
        m = p.matcher(s);
        m.find();
        System.out.println(m.group(1));
    }
}
