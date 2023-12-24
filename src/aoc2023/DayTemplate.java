package aoc2023;

import static aoc2023.Utils.assertEquals;

public class DayTemplate {
    public static void main(String[] args) {
        var program = new DayTemplate();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/DayTemplate-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var lines = Utils.readLines(fileName);
        return lines.size();
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/DayTemplate-input1.txt");
        assertEquals(sum, 0);
    }
}
