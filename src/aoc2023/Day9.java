package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static aoc2023.Utils.assertEquals;

public class Day9 {
    public static void main(String[] args) {
        var program = new Day9();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day9-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) {
        var lastNums = new ArrayList<Integer>();
        var lines = Utils.readLines(inputFilename);
        for (String line : lines) {
            // Process Line
            var nums = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            var diffList = new ArrayList<List<Integer>>();
            var diffs = nums;
            diffList.add(new ArrayList<>(diffs));
            var done = false;
            while (!done) {
                var diffsFinal = diffs;
                diffs = IntStream.range(0, diffs.size() - 1)
                        .map(i -> diffsFinal.get(i + 1) - diffsFinal.get(i))
                        .boxed().toList();
                diffList.add(new ArrayList<>(diffs));
                var isAllZeros = diffs.stream().allMatch(e -> e == 0);
                if (isAllZeros) {
                    done = true;
                }
            }
            for (int i = diffList.size() - 2; i >= 0 ; i--) {
                var prevDiffs = diffList.get(i + 1);
                var curDiffs = diffList.get(i);
                curDiffs.add(curDiffs.getLast() + prevDiffs.getLast());
            }
            lastNums.add(diffList.getFirst().getLast());
        }

        var sum = lastNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum " + sum);
        return sum;
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day9-input1.txt");
        assertEquals(sum, 114);

        sum = runMain("src/aoc2023/Day9-input2.txt");
        assertEquals(sum, 1762065988);
    }
}
