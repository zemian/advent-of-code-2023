package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static aoc2023.Utils.assertEquals;
import static aoc2023.Utils.readLines;

public class Day9b {
    public static void main(String[] args) {
        var program = new Day9b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day9-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var firstNums = new ArrayList<Integer>();
        var lines = readLines(fileName);
        for (var line : lines) {
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
            for (int i = diffList.size() - 2; i >= 0; i--) {
                var prevDiffs = diffList.get(i + 1);
                var curDiffs = diffList.get(i);
                curDiffs.add(0, curDiffs.getFirst() - prevDiffs.getFirst());
            }
            firstNums.add(diffList.getFirst().getFirst());
        }

        var sum = firstNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum " + sum);
        return sum;
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day9-input1.txt");
        assertEquals(sum, 2);

        sum = runMain("src/aoc2023/Day9-input2.txt");
        assertEquals(sum, 1066);
    }
}
