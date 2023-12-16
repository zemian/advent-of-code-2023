package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static aoc2023.TestUtils.assertEquals;

public class DayTemplate {
    public static void main(String[] args) throws Exception {
        var program = new DayTemplate();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/DayTemplate-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        var result = new ArrayList<Integer>();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                // Process Line
                result.add(Integer.parseInt(line));
            }
        }

        var sum = result.stream().reduce(0, Integer::sum);
        System.out.println("Sum " + sum);
        return sum;
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Integer sum = runMain("aoc2023/DayTemplate-input1.txt");
        assertEquals(sum, 0);

        sum = runMain("aoc2023/DayTemplate-input2.txt");
        assertEquals(sum, 0);
    }
}
