package aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static aoc2023.TestUtils.assertEquals;
import static java.util.function.Predicate.not;

public class Day11b {
    public static void main(String[] args) throws Exception {
        var program = new Day11b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day11-input2.txt", 1_000_000);
        }
    }

    private Long runMain(String fileName, int expandTimes) throws Exception {
        System.out.println("Processing input: " + fileName);
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            List<List<String>> gridList = reader.lines().filter(not(String::isEmpty))
                    .map(e -> Arrays.stream(e.split("")).toList())
                    .toList();
            char[][] grid = gridList.stream()
                    .map(charList -> String.join("", charList).toCharArray())
                    .toArray(char[][]::new);
            var distList = findAllGalaxyDistPairs(grid, expandTimes);
            Long sum = distList.stream().reduce(0L, Long::sum);
            System.out.println("Sum: " + sum + ", pairs count: " + distList.size());
            return sum;
        }
    }

    private List<Long> findAllGalaxyDistPairs(char[][] grid, int expandTimes) {
        var distList = new ArrayList<Long>();
        var processedXY = new HashSet<String>();
        for (int i = 0, maxI = grid.length; i < maxI; i++) {
            for (int j = 0, maxJ = grid[i].length; j < maxJ; j++) {
                var ch = grid[i][j];
                if (ch == '#') {
                    var key = i + "," + j;
                    processedXY.add(key);
                    var galaxiesDistList = findGalaxyDistCombination(grid, processedXY, i, j, expandTimes);
                    distList.addAll(galaxiesDistList);
                }
            }
        }
        return distList;
    }

    private List<Long> findGalaxyDistCombination(char[][] grid, Set<String> processedXY, int x, int y, int expandTimes) {
        var galaxyDistList = new ArrayList<Long>();
        for (int i = 0, maxI = grid.length; i < maxI; i++) {
            for (int j = 0, maxJ = grid[i].length; j < maxJ; j++) {
                var key = i + "," + j;
                if (processedXY.contains(key))
                    continue;
                var ch = grid[i][j];
                if (ch == '#') {
                    //System.out.println("Dist from (" + x + "," + y + ") to (" + i + "," + j + ")");
                    var xDist = Math.abs(x - i);
                    // Expand rows
                    for (int k = Math.min(x, i), maxK = k + xDist; k < maxK; k++) {
                        if (isExpandRow(grid, k)) {
                            xDist += (expandTimes > 1 ? expandTimes - 1 : expandTimes);
                            //System.out.printf("  Expanding row(%d) by %d, xDist=%d\n", k, expandTimes, xDist);
                        }
                    }

                    var yDist = Math.abs(y - j);
                    // Expand columns
                    for (int k = Math.min(y, j), maxK = k + yDist; k < maxK; k++) {
                        if (isExpandCol(grid, k)) {
                            yDist += (expandTimes > 1 ? expandTimes - 1 : expandTimes);
                            //System.out.printf("  Expanding col(%d) by %d, yDist=%d\n", k, expandTimes, yDist);
                        }
                    }

                    Long dist = (long)(xDist) + yDist;
                    galaxyDistList.add(dist);
                    //System.out.printf("  Dist=%d, xDist=%d, yDist=%d\n", dist, xDist, yDist);
                }
            }
        }
        return galaxyDistList;
    }

    private Map<Integer, Boolean> expandRowCache = new HashMap<>();
    private boolean isExpandRow(char[][] grid, int x) {
        if (expandRowCache.containsKey(x)) {
            return expandRowCache.get(x);
        }
        var expand = new String(grid[x]).chars().allMatch(e -> e == '.');
        expandRowCache.put(x, expand);
        return expand;
    }

    private Map<Integer, Boolean> expandColCache = new HashMap<>();
    private boolean isExpandCol(char[][] grid, int y) {
        if (expandColCache.containsKey(y)) {
            return expandColCache.get(y);
        }

        var expand = Arrays.stream(grid).map(row -> row[y]).allMatch(ch -> ch == '.');
        expandColCache.put(y, expand);
        return expand;
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Long sum = runMain("src/aoc2023/Day11-input1.txt", 1);
        assertEquals(sum, 374L);

        sum = runMain("src/aoc2023/Day11-input1.txt", 10);
        assertEquals(sum, 1030L);

        sum = runMain("src/aoc2023/Day11-input1.txt", 100);
        assertEquals(sum, 8410L);

        sum = runMain("src/aoc2023/Day11-input2.txt", 1_000_000);
        assertEquals(sum, 751044867439L);
    }
}
