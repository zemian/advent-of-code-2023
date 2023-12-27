package aoc2023;

import java.util.Arrays;

import static aoc2023.Utils.assertEquals;

public class Day14 {
    public static void main(String[] args) {
        var program = new Day14();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day14-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var grid = Utils.readGrid(fileName);
        //Utils.printGrid(grid);
        for (int c = 0; c < grid[0].length; c++) {
            for (int r = 0; r < grid.length; r++) {
                var val = grid[r][c];
                if (val == '.') {
                    for (int r2 = r + 1; r2 < grid.length; r2++) {
                        var val2 = grid[r2][c];
                        if (val2 == '#')
                            break;
                        if (val2 == 'O') {
                            swap(grid, c, r, r2);
                            break;
                        }
                    }
                }
            }
        }
        //System.out.println("After");
        //Utils.printGrid(grid);

        int load = 0;
        for (int r = 1; r <= grid.length; r++) {
            var endIndex = grid.length - r;
            load += r * Utils.charsStream(grid[endIndex]).filter(x -> x == 'O').toList().size();
        }

        System.out.println("Load " + load);
        return load;
    }

    private void swap(char[][] grid, int c, int r, int r2) {
        var temp = grid[r][c];
        grid[r][c] = grid[r2][c];
        grid[r2][c] = temp;
    }

    private void runTests() {
        testMain();
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day14-input1.txt");
        assertEquals(sum, 136);

        sum = runMain("src/aoc2023/Day14-input2.txt");
        assertEquals(sum, 113456);
    }
}
