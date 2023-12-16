package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import static aoc2023.TestUtils.assertEquals;

public class Day10 {
    public static void main(String[] args) throws Exception {
        var program = new Day10();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/DayTemplate-input2.txt");
        }
    }

    private Integer runMain(String inputFilename) throws Exception {
        var lines = new ArrayList<String>();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                // Process Line
                lines.add(line);
            }
        }

        // Convert lines into grid
        char[][] grid = new char[lines.size()][lines.getFirst().length()];
        int x = -1, y = -1;
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
            if (x < 0) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 'S') {
                        x = i;
                        y = j;
                    }
                }
            }
        }

        // Process grid
        var processedCells = new HashSet<Cell>();
        var stack = new Stack<Cell>();
        var prevCell = new Cell(x, y);
        stack.add(prevCell);

        var done = false;
        var stepCount = 0;
        while (!done && !stack.isEmpty()) {
            var cell = stack.pop();
            processedCells.add(cell);
            var adjCells = findAdjCells(grid, cell);
            Cell finalPrevCell = prevCell;
            adjCells = adjCells.stream().filter(e -> !e.equals(finalPrevCell)).toList();

            var end = adjCells.stream().filter(e -> grid[e.x][e.y] == 'S').findAny();
            if (end.isPresent()) {
                done = true;
                System.out.println("** Back to start 'S'");
                stepCount++;
            } else {
                adjCells = adjCells.stream().filter(e -> !processedCells.contains(e)).toList();
                if (!adjCells.isEmpty()) {
                    adjCells.forEach(stack::push);
                    stepCount++;
                } else {
                    stepCount--;
                }

                System.out.println("Grid: " + grid[cell.x][cell.y] + ", " + cell + ", adjCells=" + adjCells);
                prevCell = cell;
            }
        }

        var maxSteps = (stepCount) / 2;
        System.out.println("Step Count: " + maxSteps + ", stepCount=" + stepCount);
        return maxSteps;
    }

    private List<Cell> findAdjCells(char[][] grid, Cell cell) {
        int x = cell.x, y = cell.y;
        var cells = new ArrayList<Cell>();

        int cx, cy;
        // Check right, if from is a vertical pipe, you can't go right or left
        if (y < grid[0].length - 1 && grid[x][y] != '|') {
            cx = x;
            cy = y + 1;
            if (grid[cx][cy] == 'S'
                    || grid[cx][cy] == '-'
                    || grid[cx][cy] == '7'
                    || grid[cx][cy] == 'J') {
                cells.add(new Cell(cx, cy));
            }
        }

        // Check left
        if (y > 0 && grid[x][y] != '|') {
            cx = x;
            cy = y - 1;
            if (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '-'
                            || grid[cx][cy] == 'F'
                            || grid[cx][cy] == 'L') {
                cells.add(new Cell(cx, cy));
            }
        }

        // Check top, if from is a horizontal pipe, you can't go up or down
        if (x > 0 && grid[x][y] != '-') {
            cx = x - 1;
            cy = y;
            if (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '|'
                            || grid[cx][cy] == 'F'
                            || grid[cx][cy] == '7') {
                cells.add(new Cell(cx, cy));
            }
        }

        // Check bottom
        if (x < grid.length - 1 && grid[x][y] != '-') {
            cx = x + 1;
            cy = y;
            if (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '|'
                            || grid[cx][cy] == 'L'
                            || grid[cx][cy] == 'J') {
                cells.add(new Cell(cx, cy));
            }
        }

//        System.out.println("Cell[" + x +"][" +  y + "]: " + grid[x][y] + ", adjCells: " + cells);
        return cells;
    }

    public record Cell(int x, int y) {}

    public record LoopResult(boolean done, int maxStep) {}

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Integer sum = runMain("aoc2023/Day10-input1a.txt");
        assertEquals(sum, 4);
        sum = runMain("aoc2023/Day10-input1a2.txt");
        assertEquals(sum, 4);

        sum = runMain("aoc2023/Day10-input1b.txt");
        assertEquals(sum, 8);
//        sum = runMain("aoc2023/Day10-input1b2.txt");
//        assertEquals(sum, 8);

        sum = runMain("aoc2023/Day10-input2.txt");
        assertEquals(sum, 5);
    }
}
