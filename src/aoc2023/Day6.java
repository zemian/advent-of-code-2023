package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static aoc2023.Utils.assertEquals;

public class Day6 {
    public static void main(String[] args) throws Exception {
        var program = new Day6();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day6-input2.txt");
        }
    }

    private Long runMain(String inputFilename) throws Exception {
        var countList = new ArrayList<Long>();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            var time = reader.readLine();
            var dist = reader.readLine();
            var timeParts = time.split(":");
            var times = timeParts[1].trim().split("\\s+");
            var distParts = dist.split(":");
            var dists = distParts[1].trim().split("\\s+");

            for (int i = 0; i < times.length; i++) {
                var t = Integer.parseInt(times[i]);
                var d = Integer.parseInt(dists[i]);
                var c = IntStream.range(1, t).map(a -> calcDist(a, t)).filter(b -> b > d).count();
                countList.add(c);
            }
        }

        var product = countList.stream().reduce(1L, (a, b) -> a * b);
        System.out.println("Total number of ways: " + product);

        return product;
    }

    private int calcDist(int buttonTime, int maxTime) {
        var runTime = maxTime - buttonTime;
        return runTime * buttonTime;
    }

    private void testCalcDist() {
        var ways = new ArrayList<Integer>();
        for (int i = 0; i <= 7; i++) {
            var dist = calcDist(i, 7);
            ways.add(dist);
        }
        assertEquals(ways, List.of(0, 6, 10, 12, 12, 10, 6, 0));
    }

    private void runTests() throws Exception {
        testCalcDist();
        testMain();
    }

    private void testMain() throws Exception {
        Long sum = runMain("aoc2023/Day6-input1.txt");
        assertEquals(sum, 288L);

        sum = runMain("aoc2023/Day6-input2.txt");
        assertEquals(sum, 2065338L);
    }
}
