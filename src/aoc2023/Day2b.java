package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static aoc2023.TestUtils.assertEquals;

public class Day2b {
    public static void main(String[] args) throws Exception {
        var program = new Day2b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day2-input2.txt");
        }
    }

    private Object runMain(String inputFilename) throws Exception {
        System.out.println("Processing input: " + inputFilename);
        var powerNums = new ArrayList<Integer>();
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    var largestCubesCounts = new HashMap<String, Integer>();
                    var gameParts = line.split(": ");
                    var gameId = gameParts[0].split(" ")[1];
                    var setParts = gameParts[1].split("; ");
                    for (var setPart : setParts) {
                        // NOTE: We assume Elf will return cubes to bag per each set
                        var cubesCounts = new HashMap<String, Integer>();
                        var cubesParts = setPart.split(", ");
                        for (var cubesPart : cubesParts) {
                            var cubeParts = cubesPart.split(" ");
                            var count = cubeParts[0];
                            var color = cubeParts[1];
                            var currentCount = cubesCounts.getOrDefault(color, 0);
                            cubesCounts.put(color, currentCount + Integer.parseInt(count));
                        }

                        var redCount = largestCubesCounts.getOrDefault("red", 0);
                        largestCubesCounts.put("red", Integer.max(redCount, cubesCounts.getOrDefault("red", 0)));
                        var greenCount = largestCubesCounts.getOrDefault("green", 0);
                        largestCubesCounts.put("green", Integer.max(greenCount, cubesCounts.getOrDefault("green", 0)));
                        var blueCount = largestCubesCounts.getOrDefault("blue", 0);
                        largestCubesCounts.put("blue", Integer.max(blueCount, cubesCounts.getOrDefault("blue", 0)));
                    }
                    System.out.println("Game " + gameId + " > " + largestCubesCounts);
                    var redCount = largestCubesCounts.getOrDefault("red", 0);
                    var greenCount = largestCubesCounts.getOrDefault("green", 0);
                    var blueCount = largestCubesCounts.getOrDefault("blue", 0);
                    var power = redCount * greenCount * blueCount;
                    powerNums.add(power);
                }
            }
        }

        var sum = powerNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        return sum;
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Integer sum = (Integer)runMain("aoc2023/Day2-input1.txt");
        assertEquals(sum, 2286);

        sum = (Integer)runMain("aoc2023/Day2-input2.txt");
        assertEquals(sum, 66027);
    }
}
