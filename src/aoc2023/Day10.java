package aoc2023;

import static aoc2023.Utils.assertEquals;

public class Day10 {
    public static void main(String[] args) {
        var program = new Day10();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/DayTemplate-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        // Find the 'S' indexes
        char[][] grid = Utils.readGrid(fileName);
        int x = -1, y = -1;
        FIND_S: for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    x = i;
                    y = j;
                    break FIND_S;
                }
            }
        }
        //TestUtils.printGrid(grid);

        // Start with 'S' and find the loop back to 'S' in the grid
        var startCell = new Cell(x, y);
        var done = false;
        var stepCount = 0;
        var fromCell = new Cell(x, y);
        var toCell = new Cell(x, y);
        //System.out.println("Start " + startCell);
        while (!done) {
            var nextCell = findNextCell(grid, fromCell, toCell);
            if (nextCell == null) {
                throw new RuntimeException("Failed to find cell path! fromCell=" + fromCell + ", toCell=" + toCell);
            }
            stepCount++;
            fromCell = toCell;
            toCell = nextCell;
            if (nextCell.x == startCell.x && nextCell.y == startCell.y) {
                done = true;
            }
            //System.out.println("Next " + nextCell);
        }

        var halfSteps = (stepCount) / 2;
        System.out.println("Step Count: " + halfSteps);
        return halfSteps;
    }

    private Cell findNextCell(char[][] grid, Cell fromCell, Cell toCell) {
        int x = toCell.x, y = toCell.y;
        var val = grid[x][y];

        int cx, cy;
        // Check right
        if (y < grid[0].length - 1 && !(val == '7' || val == 'J' || val == '|') ) {
            cx = x;
            cy = y + 1;
            if (!(fromCell.x == cx && fromCell.y == cy)
                && (grid[cx][cy] == 'S'
                    || grid[cx][cy] == '-'
                    || grid[cx][cy] == '7'
                    || grid[cx][cy] == 'J')) {
                return new Cell(cx, cy);
            }
        }

        // Check left
        if (y > 0 && !(val == 'F' || val == 'L' || val == '|')) {
            cx = x;
            cy = y - 1;
            if (!(fromCell.x == cx && fromCell.y == cy)
                    && (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '-'
                            || grid[cx][cy] == 'F'
                            || grid[cx][cy] == 'L')) {
                return new Cell(cx, cy);
            }
        }

        // Check top, if from is a horizontal pipe, you can't go up or down
        if (x > 0 && !(val == 'F' || val == '7' || val == '-')) {
            cx = x - 1;
            cy = y;
            if (!(fromCell.x == cx && fromCell.y == cy)
                    && (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '|'
                            || grid[cx][cy] == 'F'
                            || grid[cx][cy] == '7')) {
                return new Cell(cx, cy);
            }
        }

        // Check bottom
        if (x < grid.length - 1 && !(val == 'L' || val == 'J' || val == '-')) {
            cx = x + 1;
            cy = y;
            if (!(fromCell.x == cx && fromCell.y == cy)
                    && (grid[cx][cy] == 'S'
                            || grid[cx][cy] == '|'
                            || grid[cx][cy] == 'L'
                            || grid[cx][cy] == 'J')) {
                return new Cell(cx, cy);
            }
        }

        return null;
    }

    public record Cell(int x, int y) {}

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day10-input1a.txt");
        assertEquals(sum, 4);

        sum = runMain("src/aoc2023/Day10-input1a2.txt");
        assertEquals(sum, 4);

        sum = runMain("src/aoc2023/Day10-input1b.txt");
        assertEquals(sum, 8);

        sum = runMain("src/aoc2023/Day10-input1b2.txt");
        assertEquals(sum, 8);

        sum = runMain("src/aoc2023/Day10-input2.txt");
        assertEquals(sum, 6778);
    }
}
