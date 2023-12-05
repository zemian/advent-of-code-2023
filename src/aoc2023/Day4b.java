package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc2023.TestUtils.assertEquals;

public class Day4b {
    public static void main(String[] args) throws Exception {
        var program = new Day4b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day4-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        var cardMathces = new HashMap<Integer, Set<Integer>>();
        var cardInstances = new HashMap<Integer, Integer>();

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
                String[] cards = line.split(": ");
                var cardParts = cards[0].split("\\s+");
                var cardNum = Integer.parseInt(cardParts[1]);
                var matches = parseAndGetMatches(cards[1]);
                cardMathces.put(cardNum, matches);
                cardInstances.put(cardNum, cardInstances.getOrDefault(cardNum, 0) + 1);
                System.out.println(line + "> matches=" + matches);

                var currentIntanceCount = cardInstances.get(cardNum);
                while (currentIntanceCount-- > 0) {
                    var instanceCardMatches = cardMathces.get(cardNum);
                    // Per puzzle rules, we increment the num of instances of card starting with current cardNum
                    for (int i = 1; i <= instanceCardMatches.size(); i++) {
                        var newCardNum = cardNum + i;
                        cardInstances.put(newCardNum, cardInstances.getOrDefault(newCardNum, 0) + 1);
                    }
                }
            }
        }

        var sum = cardInstances.values().stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        return sum;
    }

    private Set<Integer> parseAndGetMatches(String _line) {
        String[] nums = _line.split(" \\| ");
        var winingNums = Arrays.stream(nums[0].trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet());
        var elfNums = Arrays.stream(nums[1].trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet());
        winingNums.retainAll(elfNums);
        return winingNums;
    }

    private void runTests() throws Exception {
        testSplitEmptyString();
        testMain();
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
        assertEquals(sum, 30);

        sum = runMain("aoc2023/Day4-input2.txt");
        assertEquals(sum, 13080971);
    }
}
