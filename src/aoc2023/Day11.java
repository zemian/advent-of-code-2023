package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static aoc2023.Utils.assertEquals;
import static java.util.function.Predicate.not;

public class Day11 {
    public static void main(String[] args) {
        var program = new Day11();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day11-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        var lines = Utils.readLines(fileName);
        // Convert read-only lines to mutable list
        List<List<String>> gridList = lines.stream()
                .map(e -> Arrays.stream(e.split("")).collect(Collectors.toList()))
                .collect(Collectors.toList());
        char[][] grid = expand(gridList);
        //TestUtils.printGrid(grid);
        var distList = findAllGalaxyDistPairs(grid);
        int sum = distList.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum + ", pairs count: " + distList.size());
        return sum;
    }

    private List<Integer> findAllGalaxyDistPairs(char[][] grid) {
        var distList = new ArrayList<Integer>();
        var processedXY = new HashSet<String>();
        for (int i = 0, max = grid.length; i < max; i++) {
            for (int j = 0, max2 = grid[i].length; j < max2; j++) {
                var ch = grid[i][j];
                if (ch == '#') {
                    var key = i + "," + j;
                    processedXY.add(key);
                    var galaxiesDistList = findGalaxyDistCombination(grid, processedXY, i, j);
                    distList.addAll(galaxiesDistList);
                }
            }
        }
        return distList;
    }

    private List<Integer> findGalaxyDistCombination(char[][] grid, Set<String> processedXY, int x, int y) {
        var galaxyDistList = new ArrayList<Integer>();
        for (int i = 0, max = grid.length; i < max; i++) {
            for (int j = 0, max2 = grid[i].length; j < max2; j++) {
                var key = i + "," + j;
                if (processedXY.contains(key))
                    continue;
                var ch = grid[i][j];
                if (ch == '#') {
                    var dist = Math.abs(x - i) + Math.abs(y - j);
                    galaxyDistList.add(dist);
                    //System.out.println("Dist from (" + x + "," + y + ") to (" + i + "," + j + "): " + dist);
                }
            }
        }
        return galaxyDistList;
    }

    private char[][] expand(List<List<String>> gridList) {
        // Check horizontal lines
        var listIter = gridList.listIterator();
        while (listIter.hasNext()) {
            var gridLine = listIter.next();
            if (gridLine.stream().allMatch(e -> e.equals("."))) {
                listIter.add(new ArrayList<>(gridLine)); // Double it
            }
        }

        // Check vertical lines
        var addedLinesCount = 0;
        var firstGridLineMax = gridList.getFirst().size();
        for (int i = 0; i < firstGridLineMax; i++) {
            int finalI = i + addedLinesCount;
            if (!(gridList.stream().map(row -> row.get(finalI)).allMatch(ch -> ch.equals("."))))
                continue;
            for (var gridLine : gridList) {
                gridLine.add(i + addedLinesCount, ".");
            }
            addedLinesCount++;
        }

        // Convert List<List<String>> to char[][]
        return gridList.stream()
                .map(charList -> String.join("", charList).toCharArray())
                .toArray(char[][]::new);
    }

    private void runTests() {
        testExpand();
        testMain();
    }

    private void testExpand() {
        var s = """
                #.#
                ...
                #..
                """;
        List<List<String>> gridList = Arrays.stream(s.split("\n")).filter(not(String::isEmpty))
                .map(e -> Arrays.stream(e.split("")).collect(Collectors.toList()))
                .collect(Collectors.toList());
        var g = expand(gridList);
        assertEquals(String.valueOf(g[0]), "#..#");
        assertEquals(String.valueOf(g[1]), "....");
        assertEquals(String.valueOf(g[2]), "....");
        assertEquals(String.valueOf(g[3]), "#...");

        s = """
            .##
            ...
            #..
            """;
        gridList = Arrays.stream(s.split("\n")).filter(not(String::isEmpty))
                .map(e -> Arrays.stream(e.split("")).collect(Collectors.toList()))
                .collect(Collectors.toList());
        g = expand(gridList);
        assertEquals(String.valueOf(g[0]), ".##");
        assertEquals(String.valueOf(g[1]), "...");
        assertEquals(String.valueOf(g[2]), "...");
        assertEquals(String.valueOf(g[3]), "#..");
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day11-input1.txt");
        assertEquals(sum, 374);

        sum = runMain("src/aoc2023/Day11-input2.txt");
        assertEquals(sum, 9329143);
    }
}
