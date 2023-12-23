package aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;

import static aoc2023.TestUtils.assertEquals;
import static java.util.function.Predicate.not;

public class DayTemplate {
    public static void main(String[] args) throws Exception {
        var program = new DayTemplate();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/DayTemplate-input2.txt");
        }
    }

    private Integer runMain(String fileName) throws Exception {
        System.out.println("Processing input: " + fileName);
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var lines = reader.lines().filter(not(String::isEmpty)).toList();
            System.out.println(lines);
        }

        return 0;
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Integer sum = runMain("src/aoc2023/DayTemplate-input2.txt");
        assertEquals(sum, 0);
    }
}
