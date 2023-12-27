package aoc2023;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static aoc2023.Utils.assertEquals;

public class Day14b {
    public static void main(String[] args) {
        var program = new Day14b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day14-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var startTime = Instant.now();
        var grid = Utils.readGrid(fileName);

        var gridMap = new HashMap<String, Long>();
        for (long i = 0; i < 1000000000L; i++) {
            cycle(grid);
            var gridLine = String.join("", Arrays.stream(grid).map(String::valueOf).toList());
            if (!gridMap.containsKey(gridLine)) {
                gridMap.put(gridLine, i);
            } else {
                System.out.println("Grid repeat at prev (i)=" + gridMap.get(gridLine) + " and current (i)=" + i);
                var remain = 1000000000L % (i - gridMap.get(gridLine));
                System.out.println("Re-run cycle remaining times: " + remain);
                grid = Utils.readGrid(fileName);
                for (int j = 0; j < remain; j++) {
                    cycle(grid);
                }
                break;
            }
        }

        int load = 0;
        for (int r = 1; r <= grid.length; r++) {
            var endIndex = grid.length - r;
            load += r * Utils.charsStream(grid[endIndex]).filter(x -> x == 'O').toList().size();
        }

        var duration = Duration.between(startTime, Instant.now());
        System.out.println("Load " + load + " time: " + duration);

        return load;
    }
    
    private void cycle(char[][] grid) {
        tiltNorth(grid);
        //Utils.printGrid(grid);
        tiltWest(grid);
        //Utils.printGrid(grid);
        tiltSouth(grid);
        //Utils.printGrid(grid);
        tiltEast(grid);
        //Utils.printGrid(grid);
    }

    private void tiltNorth(char[][] grid) {
        for (int c = 0; c < grid[0].length; c++) {
            for (int r = 0; r < grid.length; r++) {
                var val = grid[r][c];
                if (val == '.') {
                    for (int r2 = r + 1; r2 < grid.length; r2++) {
                        var val2 = grid[r2][c];
                        if (val2 == '#')
                            break;
                        if (val2 == 'O') {
                            swapVert(grid, c, r, r2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void tiltSouth(char[][] grid) {
        for (int c = 0; c < grid[0].length; c++) {
            for (int r = grid.length - 1; r >= 0 ; r--) {
                var val = grid[r][c];
                if (val == '.') {
                    for (int r2 = r - 1; r2 >= 0; r2--) {
                        var val2 = grid[r2][c];
                        if (val2 == '#')
                            break;
                        if (val2 == 'O') {
                            swapVert(grid, c, r, r2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void tiltEast(char[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = grid[0].length - 1; c >= 0; c--) {
                var val = grid[r][c];
                if (val == '.') {
                    for (int c2 = c - 1; c2 >= 0; c2--) {
                        var val2 = grid[r][c2];
                        if (val2 == '#')
                            break;
                        if (val2 == 'O') {
                            swapHorz(grid, r, c, c2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void tiltWest(char[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                var val = grid[r][c];
                if (val == '.') {
                    for (int c2 = c + 1; c2 < grid[0].length; c2++) {
                        var val2 = grid[r][c2];
                        if (val2 == '#')
                            break;
                        if (val2 == 'O') {
                            swapHorz(grid, r, c, c2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swapHorz(char[][] grid, int r, int c, int c2) {
        var temp = grid[r][c];
        grid[r][c] = grid[r][c2];
        grid[r][c2] = temp;
    }

    private void swapVert(char[][] grid, int c, int r, int r2) {
        var temp = grid[r][c];
        grid[r][c] = grid[r2][c];
        grid[r2][c] = temp;
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day14-input1.txt");
        assertEquals(sum, 64);

        sum = runMain("src/aoc2023/Day14-input2.txt");
        assertEquals(sum, 115966); // Wrong answer - too low :(
    }
}
