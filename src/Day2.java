import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day2 {
    public static void main(String[] args) throws Exception {
        run();
        //testParseLine();
    }
    public static void run() throws Exception {
        var availableCubes = Map.of("red", 12, "green", 13, "blue", 14);
        var possibleGameIds = new ArrayList<Integer>();

        var inputFilename = "Day2-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    var passed = true;
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

                        if (cubesCounts.getOrDefault("red", 0) > availableCubes.get("red")
                                || cubesCounts.getOrDefault("green", 0) > availableCubes.get("green")
                                || cubesCounts.getOrDefault("blue", 0) > availableCubes.get("blue")) {
                            //System.out.println("Rejected> " + line);
                            passed = false;
                            break;
                        }
                    }
                    if (passed) {
                        possibleGameIds.add(Integer.parseInt(gameId));
                    }
                }
            }
        }

        var sum = possibleGameIds.stream().reduce(0, Integer::sum);
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
