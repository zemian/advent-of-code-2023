package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc2023.Utils.assertEquals;

public class Day4 {
    public static void main(String[] args) throws Exception {
        var program = new Day4();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day4-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        var scores = new ArrayList<Integer>();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                //System.out.println(line);
                var matches = parseAndGetMatches(line);
                var score = calcMatchScore(matches);
                System.out.println(line + "> matches=" + matches + ", score=" + score);
                scores.add(score);
            }
        }

        var sum = scores.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        return sum;
    }

    private Integer calcMatchScore(Set<Integer> matches) {
        if (matches.isEmpty()) {
            return 0;
        }

        var score = 1;
        for (int i = 0; i < matches.size() - 1; i++) {
            score *= 2;
        }
        return score;
    }

    private Set<Integer> parseAndGetMatches(String line) {
        String[] cards = line.split(": ");
        String[] nums = cards[1].split(" \\| ");
        var winingNums = Arrays.stream(nums[0].trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet());
        var elfNums = Arrays.stream(nums[1].trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet());
        winingNums.retainAll(elfNums);
        return winingNums;
    }

    private void runTests() throws Exception {
        testSplitEmptyString();
        testCalcMatchScore();
        testMain();
    }

    private void testCalcMatchScore() {
        assertEquals(calcMatchScore(Set.of()), 0);
        assertEquals(calcMatchScore(Set.of(99)), 1);
        assertEquals(calcMatchScore(Set.of(99, 98)), 2);
        assertEquals(calcMatchScore(Set.of(99, 98, 97)), 4);
        assertEquals(calcMatchScore(Set.of(99, 98, 97, 96, 95, 94)), 32);
    }

    private void testSplitEmptyString() {
        var s = "69 82 63 72 16 21 14  1";
        var ls = Arrays.asList(s.split("\\s+"));
        assertEquals(ls, List.of("69", "82", "63", "72", "16", "21", "14", "1"));

        s = " 1 21 53 59 44";
        ls = Arrays.asList(s.split("\\s+"));
        // Java give you one extra element, unless you trim line first!
        assertEquals(ls, List.of("", "1", "21", "53", "59", "44"));
        ls = Arrays.asList(s.trim().split("\\s+"));
        assertEquals(ls, List.of("1", "21", "53", "59", "44"));
    }

    private void testMain() throws Exception {
        Integer sum = runMain("aoc2023/Day4-input1.txt");
        assertEquals(sum, 13);

        sum = runMain("aoc2023/Day4-input2.txt");
        assertEquals(sum, 26914);
    }
}
