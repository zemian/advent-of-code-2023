package aoc2023;

import java.util.Arrays;

import static aoc2023.Utils.assertEquals;

public class Day15 {
    public static void main(String[] args) {
        var program = new Day15();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day15-input1.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var lines = Utils.readLines(fileName);
        var sum = lines.stream().map(this::hashLineSum).reduce(0, Integer::sum);
        System.out.println("Hash sum: " + sum);
        return sum;
    }

    private int hash(String input) {
        return input.chars().reduce(0, (h, c) -> ((h + c) * 17) % 256);
    }

    private int hashLineSum(String line) {
        return Arrays.stream(line.split(",")).map(this::hash).reduce(0, Integer::sum);
    }

    private void runTests() {
        testHASH();
        testMain();
    }

    private void testHASH() {
        assertEquals(hash("HASH"), 52);

        var s = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
        assertEquals(hashLineSum(s), 1320);
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day15-input1.txt");
        assertEquals(sum, 508552);
    }
}
