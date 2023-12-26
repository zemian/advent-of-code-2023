package aoc2023;

import static aoc2023.Utils.*;
import static aoc2023.Day16.Dir.*;
import java.util.*;

public class Day16 {
    public static void main(String[] args) {
        var program = new Day16();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day16-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var grid = Utils.readGrid(fileName);
        //Utils.printGrid(grid);

        var walkedCellKeys = new HashSet<String>();
        var counter = 0;
        var queue = new Stack<Cell>();
        queue.add(new Cell(0, 0, RIGHT));
        while (!queue.isEmpty()) {
            counter++;
            var cell = queue.pop();
            var nextCells = traverse(grid, cell, walkedCellKeys);

            if (!nextCells.isEmpty()) {
                queue.addAll(nextCells);
            }
        }

        return counter;
    }

    private boolean isValid(char[][] grid, int x, int y) {
        var rowLen = grid.length;
        var colLen = grid[0].length;
        return (x >= 0 && x < rowLen && y >= 0 && y < colLen);
    }

    private List<Cell> getValidCell(char[][] grid, int x, int y, Dir dir) {
        var cells = new ArrayList<Cell>();
        if(isValid(grid, x, y)) {
            cells.add(new Cell(x, y, dir));
        }
        return cells;
    }


    private List<Cell> getValidSplitCells(char[][] grid, int x, int y, Dir dir, int x2, int y2, Dir dir2, Set<String> walkedCellKeys) {
        var cells = new ArrayList<Cell>();
        var key = x + "," + y;
        if(isValid(grid, x, y)) {
            if (!walkedCellKeys.contains(key)) {
                cells.add(new Cell(x, y, dir));
                walkedCellKeys.add(key);
            }
        }

        key = x2+ "," + y2;
        if(isValid(grid, x2, y2)) {
            if (!walkedCellKeys.contains(key)) {
                cells.add(new Cell(x2, y2, dir2));
                walkedCellKeys.add(key);
            }
        }

        return cells;
    }

    private List<Cell> traverse(char[][] grid, Cell cell, Set<String> walkedCellKeys) {
        //System.out.printf("Traverse: cells=%s\n", cells);
        // x = row = UP/DOWN = grid.length, y = col = RIGHT/LEFT = grid[0].length

        var cells = new ArrayList<Cell>();

        // cell is where it's from, and dir is where it needs to go.
        int x = cell.x, y = cell.y;
        var dir = cell.dir;
        var mirror = grid[x][y];
        System.out.printf("Processing cell=%s\n", cell + ", mirror=" + mirror);

         // Get the next target cell
        if (dir == RIGHT) {
            if (mirror == '.' || mirror == '-') {
                cells.addAll(getValidCell(grid, x, y + 1, dir));
            } else if (mirror == '\\') {
                cells.addAll(getValidCell(grid, x + 1, y, DOWN));
            } else if (mirror == '/') {
                cells.addAll(getValidCell(grid, x - 1, y, UP));
            } else if (mirror == '|') { // split
                cells.addAll(getValidSplitCells(grid, x - 1, y, UP, x + 1, y, DOWN, walkedCellKeys));
            }
        } else if (dir == LEFT) {
            if (mirror == '.' || mirror == '-') {
                cells.addAll(getValidCell(grid, x, y - 1, dir));
            } else if (mirror == '\\') {
                cells.addAll(getValidCell(grid, x - 1, y, UP));
            } else if (mirror == '/') {
                cells.addAll(getValidCell(grid, x + 1, y, DOWN));
            } else if (mirror == '|')  { // split
                cells.addAll(getValidSplitCells(grid, x - 1, y, UP, x + 1, y, DOWN, walkedCellKeys));
            }
        } else if (dir == UP) {
            if (mirror == '.' || mirror == '|') {
                cells.addAll(getValidCell(grid, x - 1, y, dir));
            } else if (mirror == '\\') {
                cells.addAll(getValidCell(grid, x, y - 1, LEFT));
            } else if (mirror == '/') {
                cells.addAll(getValidCell(grid, x, y + 1, RIGHT));
            } else if (mirror == '-') { // split
                cells.addAll(getValidSplitCells(grid, x, y - 1, LEFT, x, y + 1, RIGHT, walkedCellKeys));
            }
        } else if (dir == DOWN) {
            if (mirror == '.' || mirror == '|') {
                cells.addAll(getValidCell(grid, x + 1, y, dir));
            } else if (mirror == '\\') {
                cells.addAll(getValidCell(grid, x, y + 1, RIGHT));
            } else if (mirror == '/') {
                cells.addAll(getValidCell(grid, x, y - 1, LEFT));
            } else if (mirror == '-') {  // split
                cells.addAll(getValidSplitCells(grid, x, y - 1, LEFT, x, y + 1, RIGHT, walkedCellKeys));
            }
        }

        return cells;
    }


    static enum Dir { RIGHT, LEFT, UP, DOWN }

    static record Cell(int x, int y, Dir dir) {}

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day16-input1.txt");
        assertEquals(sum, 46);
    }
}
