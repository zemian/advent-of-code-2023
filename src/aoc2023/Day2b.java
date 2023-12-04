package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Day2b {
    public static void main(String[] args) throws Exception {
        run();
        //testParseLine();
    }
    public static void run() throws Exception {
        var powerNums = new ArrayList<Integer>();

        var inputFilename = "aoc2023/Day2-input.txt";
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
        System.out.println(sum);
        // 3035
    }
    public static void testParseLine() throws Exception {
        // Test regex to get first and last digit.
        var s = "Game 1: 1 green, 1 blue, 1 red; 1 green, 8 red, 7 blue; 6 blue, 10 red; 4 red, 9 blue, 2 green; 1 green, 3 blue; 4 red, 1 green, 10 blue";
        var gameParts = s.split(": ");
        var setParts = gameParts[1].split("; ");
        var cubesParts = setParts[0].split(", ");
        System.out.println(gameParts[0]);
        System.out.println(setParts[0]);
        System.out.println(Arrays.asList(cubesParts));
    }
}
