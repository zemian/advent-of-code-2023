package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static aoc2023.TestUtils.assertEquals;

public class Day8b {
    public static void main(String[] args) throws Exception {
        var program = new Day8b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day8b-input2.txt");
        }
    }

    private Long runMain(String inputFilename) throws Exception {
        var turnCount = 0L;
        var startTime = Instant.now();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String[] steps = reader.readLine().split("");
            reader.readLine();

            var turns = new HashMap<String, String[]>();
            var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                var m = pattern.matcher(line);
                m.matches();
                var turn = m.group(1);
                var left = m.group(2);
                var right = m.group(3);
                turns.put(turn, new String[]{left, right});
            }

            boolean done = false;
            var currentTurnList = turns.keySet().stream().filter(e -> e.endsWith("A")).toList();
            while (!done) {
                for (String step : steps) {
                    currentTurnList = currentTurnList.stream().map(currentTurn -> {
                        var nextTurnTupple = turns.get(currentTurn);
                        String nextTurn;
                        if (step.equals("L")) {
                            nextTurn = nextTurnTupple[0];
                        } else {
                            nextTurn = nextTurnTupple[1];
                        }
                        return nextTurn;
                    }).toList();
                    turnCount++;

                    var allEndsZ = currentTurnList.stream().allMatch(e -> e.endsWith("Z"));
                    if (allEndsZ) {
                        System.out.println("Match found: " + currentTurnList);
                        done = true;
                        break;
                    }/* else {
                        System.out.println("No match: " + currentTurnList);
                    }*/
                }
                if (turnCount % 1_000_000 == 0) {
                    System.out.println("Repeating all steps. turnCount=" + turnCount);
                }
            }
        }

        System.out.println("Steps: " + turnCount);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        return turnCount;
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Long sum = runMain("aoc2023/Day8b-input1.txt");
        assertEquals(sum, 6L);

        sum = runMain("aoc2023/Day8b-input2.txt");
        assertEquals(sum, 0L);
    }
}
