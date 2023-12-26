package aoc2023;

import aoc2023.Utils.Tuple2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static aoc2023.Utils.assertEquals;

/**
 * Day 13: Point of Incidence
 *
 * Find the line of reflection in each of the patterns in your notes.
 * What number do you get after summarizing all of your notes?
 */
public class Day13 {
    public static void main(String[] args) {
        var program = new Day13();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day13-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var gridList = getGridList(fileName);
        return gridList.stream().map(this::summarizeGrid).reduce(0, Integer::sum);
    }

    private List<char[][]> getGridList(String fileName) {
        var gridList = new ArrayList<char[][]>();
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var gridLines = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    var grid = gridLines.stream().map(String::toCharArray).toArray(char[][]::new);
                    gridList.add(grid);
                    gridLines = new ArrayList<String>();
                } else {
                    gridLines.add(line);
                }
            }
            // Last grid
            var grid = gridLines.stream().map(String::toCharArray).toArray(char[][]::new);
            gridList.add(grid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gridList;
    }

    private int summarizeGrid(char[][] grid) {
        Utils.printGrid(grid);
        var h = findHorizontalMidPoint(grid);
        var v = findVerticalMidPoint(grid);
        if (h.a()) {
            var val = 100 * h.b();
            System.out.println("  h=" + val);
            return val;
        } else if (v.a()) {
            var val = v.b();
            System.out.println("  v=" + val);
            return val;
        }
        return 0;
    }

    private Tuple2<Boolean, Integer> findHorizontalMidPoint(char[][] grid) {
        var firstLine = String.valueOf(grid[0]);
        var lastLine = String.valueOf(grid[grid.length - 1]);
        var startIndex = firstLine.equals(lastLine) ? 0 : 1;
        int mid = grid.length / 2;
        for (int r = startIndex; r < mid; r++) {
            var rowLine1 = String.valueOf(grid[r]);
            var rowLine2 = String.valueOf(grid[grid.length - r]);
            if (!rowLine1.equals(rowLine2)) {
                return new Tuple2<>(false, -1);
            }
        }
        return new Tuple2<>(true, mid + 1);
    }

    private Tuple2<Boolean, Integer> findVerticalMidPoint(char[][] grid) {
        var firstLine = String.join("", Arrays.stream(grid).map(chars -> String.valueOf(chars[0])).toList());
        var lastLine = String.join("", Arrays.stream(grid).map(chars -> String.valueOf(chars[grid[0].length - 1])).toList());
        var startIndex = firstLine.equals(lastLine) ? 0 : 1;
        int mid = grid[0].length / 2;
        for (int c = startIndex; c < mid; c++) {
            int finalC = c;
            var colLine1 = String.join("", Arrays.stream(grid).map(chars -> String.valueOf(chars[finalC])).toList());
            var colLine2 = String.join("", Arrays.stream(grid).map(chars -> String.valueOf(chars[grid[0].length - finalC])).toList());
            if (!colLine1.equals(colLine2)) {
                return new Tuple2<>(false, -1);
            }
        }
        return new Tuple2<>(true, mid + 1);
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day13-input1.txt");
        assertEquals(sum, 405);

        sum = runMain("src/aoc2023/Day13-input2.txt");
        assertEquals(sum, 1459);

    }
}
