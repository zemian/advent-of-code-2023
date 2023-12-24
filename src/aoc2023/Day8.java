package aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import static aoc2023.Utils.assertEquals;

public class Day8 {
    public static void main(String[] args) {
        var program = new Day8();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day8-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        try {
            try (var reader = new BufferedReader(new FileReader(fileName))) {
                var turnCount = 0;
                String[] steps = reader.readLine().split("");
                reader.readLine();

                var turns = new HashMap<String, String[]>();
                var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
                String line;
                while ((line = reader.readLine()) != null) {
                    var m = pattern.matcher(line);
                    m.matches();
                    var turn = m.group(1);
                    var left = m.group(2);
                    var right = m.group(3);
                    turns.put(turn, new String[]{left, right});
                }
                boolean done = false;
                String currentTurn = "AAA";
                while (!done) {
                    for (String step : steps) {
                        var nextTurnTupple = turns.get(currentTurn);
                        if (step.equals("L")) {
                            currentTurn = nextTurnTupple[0];
                        } else {
                            currentTurn = nextTurnTupple[1];
                        }
                        turnCount++;

                        if (currentTurn.equals("ZZZ")) {
                            done = true;
                            break;
                        }
                    }
                }
                System.out.println("Steps: " + turnCount);
                return turnCount;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file " + fileName, e);
        }
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day8-input1.txt");
        assertEquals(sum, 2);

        sum = runMain("src/aoc2023/Day8-input1b.txt");
        assertEquals(sum, 6);

        sum = runMain("src/aoc2023/Day8-input2.txt");
        assertEquals(sum, 18113);
    }
}
