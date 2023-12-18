package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import static aoc2023.TestUtils.assertEquals;

public class Day15 {
    public static void main(String[] args) throws Exception {
        var program = new Day15();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day15-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        var sum = 0;
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
                sum += hashLineSum(line);
            }
        }

        System.out.println("Hash sum: " + sum);
        return sum;
    }

    private int hash(String input) {
        return input.chars().reduce(0, (h, c) -> ((h + c) * 17) % 256);
    }

    private int hashLineSum(String line) {
        return Arrays.stream(line.split(",")).map(this::hash).reduce(0, Integer::sum);
    }

    private void runTests() throws Exception {
        testHASH();
        testMain();
    }

    private void testHASH() {
        assertEquals(hash("HASH"), 52);

        var s = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
        assertEquals(hashLineSum(s), 1320);
    }

    private void testMain() throws Exception {
        Integer sum = runMain("aoc2023/Day15-input1.txt");
        assertEquals(sum, 508552);
    }
}
