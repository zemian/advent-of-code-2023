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

    public static class Counter {
        public int val = 0;
        public Counter(int val) { this.val = val; }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var grid = Utils.readGrid(fileName);
        //Utils.printGrid(grid);

        var counter = new Counter(0);
        var queue = new Stack<Cell>();
        queue.add(new Cell(0, 0, RIGHT));
        while (!queue.isEmpty()) {
            counter.val++;
            var cell = queue.pop();
            var queueSize = queue.size();
            var nextCell = traverse(grid, queue, cell, counter);
            System.out.printf("Walk from cell=%s, nextCell=%s, queue=%s\n", cell, nextCell, queue);

            if (nextCell != null) {
                queue.add(nextCell);
            }
        }

        return counter.val;
    }

    private boolean isValid(char[][] grid, int x, int y) {
        var rowLen = grid.length;
        var colLen = grid[0].length;
        return (x >= 0 && x < rowLen && y >= 0 && y < colLen);
    }

    private Cell getValidCell(char[][] grid, int x, int y, Dir dir) {
        if(isValid(grid, x, y)) {
            return new Cell(x, y, dir);
        }
        return null;
    }


    private Cell getValidCellWithQueue(Stack<Cell> queue, char[][] grid, int x, int y, Dir dir, int x2, int y2, Dir dir2) {
        var cells = new ArrayList<Cell>();
        if(isValid(grid, x, y)) {
            cells.add(new Cell(x, y, dir));
        }
        if(isValid(grid, x2, y2)) {
            cells.add(new Cell(x2, y2, dir2));
        }

        if (cells.size() == 0) {
            return null;
        } else  if (cells.size() == 1) {
            return cells.get(0);
        } else if (cells.size() == 2) {
            queue.add(cells.get(0));
            return cells.get(1);
        }
        return null;
    }

    private Cell traverse(char[][] grid, Stack<Cell> queue, Cell cell, Counter counter) {
        //System.out.printf("Traverse: cells=%s\n", cells);
        // x = row = UP/DOWN = grid.length, y = col = RIGHT/LEFT = grid[0].length

        //System.out.printf("  Processing cell=%s\n", cell);
        // cell is where it's from, and dir is where it needs to go.
        int x = cell.x, y = cell.y;
        var dir = cell.dir;
        var mirror = grid[x][y];

         // Get the next target cell
        if (dir == RIGHT) {
            if (mirror == '.') {
                return getValidCell(grid, x, y + 1, dir);
            } else if (mirror == '\\') {
                return getValidCell(grid, x + 1, y, DOWN);
            } else if (mirror == '/') {
                return getValidCell(grid, x - 1, y, UP);
            }
            else if (mirror == '|') { // split
                return getValidCellWithQueue(queue, grid, x - 1, y, UP, x + 1, y, DOWN);
            } else if (mirror == '-') {
                return getValidCell(grid, x, y, dir);
            }
        } else if (dir == LEFT) {
            if (mirror == '.') {
                return new Cell(x, y - 1, dir);
            } else if (mirror == '\\') {
                return getValidCell(grid, x - 1, y, UP);
            }
            else if (mirror == '/') {
                return getValidCell(grid, x + 1, y, DOWN);
            } else if (mirror == '|')  { // split
                return getValidCellWithQueue(queue, grid, x + 1, y, UP, x - 1, y, DOWN);
            } else if (mirror == '-') {
                return getValidCell(grid, x, y, dir);
            }
        } else if (dir == UP) {
            if (mirror == '.') {
                return new Cell(x - 1, y, dir);
            } else if (mirror == '\\') {
                return getValidCell(grid, x, y - 1, LEFT);
            }
            else if (mirror == '/') {
                return getValidCell(grid, x, y + 1, RIGHT);
            }
            else if (mirror == '|') {
                return getValidCell(grid, x, y, dir);
            }
            else if (mirror == '-') { // split
                return getValidCellWithQueue(queue, grid, x, y - 1, LEFT, x, y + 1, RIGHT);
            }
        } else if (dir == DOWN) {
            if (mirror == '.') {
                return new Cell(x + 1, y, dir);
            } else if (mirror == '\\') {
                return getValidCell(grid, x, y + 1, RIGHT);
            }
            else if (mirror == '/') {
                return getValidCell(grid, x, y - 1, LEFT);
            }
            else if (mirror == '|') {
                return getValidCell(grid, x, y, dir);
            }
            else if (mirror == '-') {  // split
                return getValidCellWithQueue(queue, grid, x, y - 1, LEFT, x, y + 1, RIGHT);
            }
        }

        return null;
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
