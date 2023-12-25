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

        List<Cell> energizedCells = new ArrayList<>();
        boolean done = false;
        List<Cell> cells = List.of(new Cell(0, 0, RIGHT));
        while (!done) {
            var nextCells = traverse(grid, cells);
            System.out.printf("  nextCells=%s\n", nextCells);

            if (nextCells.isEmpty()) {
                done = true;
            } else {
                energizedCells.addAll(cells);
                cells = nextCells;
            }
        }

        return energizedCells.size();
    }

    private List<Cell> traverse(char[][] grid, List<Cell> cells) {
        System.out.printf("Traverse: cells=%s\n", cells);
        // x = row = UP/DOWN = grid.length, y = col = RIGHT/LEFT = grid[0].length
        var rowLen = grid.length;
        var colLen = grid[0].length;

        List<Cell> result = new ArrayList<>();
        for (Cell cell : cells) {
            System.out.printf("  Processing cell=%s\n", cell);
            // cell is where it's from, and dir is where it needs to go.
            int x = cell.x, y = cell.y;
            var dir = cell.dir;

            // Skip invalid cell input - lazy way to let result return invalid indexes and remove them here.
            if (!(x >= 0 && x < rowLen && y >= 0 && y < colLen)) {
                System.out.println("  Traverse cell (cx,cy) is out of bound: " + x + "," + y);
                continue;
            }

            // Get the next target cell
            int cx = -1, cy = -1;
            if (dir == RIGHT) {
                cx = x;
                cy = y + 1;
            } else if (dir == LEFT) {
                cx = x; 
                cy = y - 1;
            } else if (dir == UP) {
                cx = x - 1; 
                cy = y;
            } else if (dir == DOWN) {
                cx = x + 1; 
                cy = y;
            }

            // We shouldn't process anything it's out side of the grid.
            if (!(cx >= 0 && cx < rowLen && cy >= 0 && cy < colLen)) {
                System.out.println("  Next cell (cx,cy) is out of bound: " + cx + "," + cy);
                continue;
            }

            // Get the next cell target mirror value
            var mirror = grid[cx][cy];

            // Continue if it's just a space
            if (mirror == '.') {
                result.add(new Cell(cx, cy, dir));
                continue;
            }

            if (dir == RIGHT) {
                if (mirror == '\\')
                    result.add(new Cell(cx + 1, cy, DOWN));
                else if (mirror == '/')
                    result.add(new Cell(cx - 1, cy, UP));
                else if (mirror == '|') { // split
                    result.add(new Cell(cx - 1, cy, UP));
                    result.add(new Cell(cx + 1, cy, DOWN));
                } else if (mirror == '-') {
                    result.add(new Cell(cx, cy + 1, dir));
                }
            } else if (dir == LEFT) {
                if (mirror == '\\')
                    result.add(new Cell(cx - 1, cy, UP));
                else if (mirror == '/')
                    result.add(new Cell(cx + 1, cy, DOWN));
                else if (mirror == '|')  { // split
                    result.add(new Cell(cx - 1, cy, UP));
                    result.add(new Cell(cx + 1, cy, DOWN));
                } else if (mirror == '-')
                    result.add(new Cell(cx, cy - 1, dir));
            } else if (dir == UP) {
                if (mirror == '\\')
                    result.add(new Cell(cx, cy - 1, LEFT));
                else if (mirror == '/')
                    result.add(new Cell(cx, cy + 1, RIGHT));
                else if (mirror == '|')
                    result.add(new Cell(cx + 1, cy, dir));
                else if (mirror == '-') { // split
                    result.add(new Cell(cx, cy - 1, LEFT));
                    result.add(new Cell(cx, cy + 1, RIGHT));
                }
            } else if (dir == DOWN) {
                if (mirror == '\\')
                    result.add(new Cell(cx, cy + 1, RIGHT));
                else if (mirror == '/')
                    result.add(new Cell(cx, cy - 1, LEFT));
                else if (mirror == '|')
                    result.add(new Cell(cx + 1, cy, dir));
                else if (mirror == '-') {  // split
                    result.add(new Cell(cx, cy - 1, LEFT));
                    result.add(new Cell(cx, cy + 1, RIGHT));
                }
            }
        }

        return result;
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
