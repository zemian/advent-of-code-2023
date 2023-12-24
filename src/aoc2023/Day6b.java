package aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static aoc2023.Utils.assertEquals;

public class Day6b {
    public static void main(String[] args) {
        var program = new Day6b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day6-input2.txt");
        }
    }

    private Long runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var lines = Utils.readLines(fileName);
        var time = lines.get(0);
        var dist = lines.get(1);
        var timeParts = time.split(":");
        var times = timeParts[1].trim().split("\\s+");
        var distParts = dist.split(":");
        var dists = distParts[1].trim().split("\\s+");

        var t = Long.parseLong(String.join("", times));
        var d = Long.parseLong(String.join("", dists));
        var c = LongStream.range(1, t).map(a -> calcDist(a, t)).filter(b -> b > d).count();
        System.out.println("Total number of ways: " + c);

        return c;
    }

    private long calcDist(long buttonTime, long maxTime) {
        var runTime = maxTime - buttonTime;
        return runTime * buttonTime;
    }

    private void testCalcDist() {
        var ways = new ArrayList<Integer>();
        for (int i = 0; i <= 7; i++) {
            var dist = calcDist(i, 7);
            ways.add((int)dist);
        }
        assertEquals(ways, List.of(0, 6, 10, 12, 12, 10, 6, 0));
    }

    private void runTests() {
        testCalcDist();
        testMain();
    }

    private void testMain() {
        Long sum = runMain("src/aoc2023/Day6-input1.txt");
        assertEquals(sum, 71503L);

        sum = runMain("src/aoc2023/Day6-input2.txt");
        assertEquals(sum, 34934171L);
    }
}
