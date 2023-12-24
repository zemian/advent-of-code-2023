package aoc2023;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import static aoc2023.Utils.assertEquals;

public class Day3 {
    public static void main(String[] args) {
        var program = new Day3();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day3-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var grid = Utils.readGrid(fileName);
        var startTime = Instant.now();
        var partNums = new ArrayList<Integer>();

        // Find part nums next to a symbol
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char val = grid[i][j];
                if (Character.isDigit(val)) {
                    var numLoc = findNumLoc(grid, i, j);
                    //System.out.println("\nFound PartNum: " + numLoc.partNum);
                    if (hasSymbolAround(grid, numLoc)) {
                        partNums.add(numLoc.partNum);
                        System.out.println("Found PartNum: " + numLoc.partNum + " with symbol");
                    }
                    j = numLoc.endj;
                }
            }
        }

        var sum = partNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        return sum;
    }

    private boolean hasSymbolAround(char[][] grid, NumLoc n) {
        // Check left of partNum
        if (n.j > 0) {
            if (isSymbol(grid[n.i][n.j - 1])) {
                return true;
            }
        }

        // Check right of partNum
        if (n.endj < grid[0].length - 1) {
            if (isSymbol(grid[n.i][n.endj + 1])) {
                return true;
            }
        }

        // Check row above partNum (including diagonal position)
        if (n.i > 0) {
            var rowIndex = (n.i > 0) ? n.i - 1 : n.i;
            var colIndex = (n.j > 0) ? n.j - 1 : n.j + 1;
            var colEndIndex = (n.endj < grid[0].length - 1) ? n.endj + 1 : n.endj;
            for (int i = colIndex; i <= colEndIndex; i++) {
                if (isSymbol(grid[rowIndex][i])) {
                    return true;
                }
            }
        }

        // Check row below partNum (including diagonal position)
        if (n.j < grid[0].length - 1) {
            var rowIndex = (n.i < grid[0].length - 1) ? n.i + 1 : n.i;
            var colIndex = (n.j > 0) ? n.j - 1 : n.j + 1;
            var colEndIndex = (n.endj < grid[0].length - 1) ? n.endj + 1 : n.endj;
            for (int i = colIndex; i <= colEndIndex; i++) {
                if (isSymbol(grid[rowIndex][i])) {
                    return true;
                }
            }
        }

        // No symbol found
        return false;
    }

    private boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    private NumLoc findNumLoc(char[][] grid, int i, int j) {
        var numLoc = new NumLoc();
        numLoc.i = i;
        numLoc.j = j;
        String partNum = "";
        int k;
        for (k = j; k < grid[i].length; k++) {
            var nextChar = grid[i][k];
            if (Character.isDigit(nextChar)) {
                partNum += nextChar;
            } else {
                numLoc.partNum = Integer.parseInt(partNum);
                numLoc.endj = k - 1;
                break;
            }
        }
        // Ensure partNum on end of line is captured properly
        if (k == grid[i].length) {
            numLoc.partNum = Integer.parseInt(partNum);
            numLoc.endj = k - 1;
        }

        return numLoc;
    }

    private static class NumLoc {
        public int partNum;
        public int i, j; // first digit location
        public int endj; // last digit position (on j).
    }



    private void runTests() {
        testFindNumLoc();
        testIsSymbol();
        testHasSymbolAround();
        testMain();
    }

    private void testFindNumLoc() {
        // Find partNum in middle of grid
        char[][] grid = new char[][] {
                ".....".toCharArray(),
                ".789.".toCharArray(),
                ".....".toCharArray(),
        };
        var numLoc = findNumLoc(grid, 1, 1);
        assertEquals(numLoc.partNum, 789);
        assertEquals(numLoc.i, 1);
        assertEquals(numLoc.j, 1);
        assertEquals(numLoc.endj, 3);

        // Find partNum on left side of the grid
        grid = new char[][] {
                ".....".toCharArray(),
                "689..".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 0);
        assertEquals(numLoc.partNum, 689);
        assertEquals(numLoc.i, 1);
        assertEquals(numLoc.j, 0);
        assertEquals(numLoc.endj, 2);

        // Find partNum on right side of the grid
        // This use case was not handled and cause the program to go into infinite loop!
        grid = new char[][] {
                ".....".toCharArray(),
                "..589".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 2);
        assertEquals(numLoc.partNum, 589);
        assertEquals(numLoc.i, 1);
        assertEquals(numLoc.j, 2);
        assertEquals(numLoc.endj, 4);
    }

    private void testHasSymbolAround() {
        char[][] grid = new char[][] {
                "..*..".toCharArray(),
                ".100.".toCharArray(),
                ".....".toCharArray(),
        };
        var numLoc = findNumLoc(grid, 1, 1);
        var found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        grid = new char[][] {
                ".....".toCharArray(),
                ".101.".toCharArray(),
                "..*..".toCharArray(),
        };

        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                ".....".toCharArray(),
                ".102.".toCharArray(),
                "*....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                "....*".toCharArray(),
                ".103.".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                "*....".toCharArray(),
                ".103.".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                ".....".toCharArray(),
                ".103.".toCharArray(),
                "....*".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                ".....".toCharArray(),
                "*103.".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);
        grid = new char[][] {
                ".....".toCharArray(),
                ".103*".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, true);

        grid = new char[][] {
                ".....".toCharArray(),
                ".104.".toCharArray(),
                ".....".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 1);
        found = hasSymbolAround(grid, numLoc);
        assertEquals(found, false);
    }

    private void testIsSymbol() {
        assertEquals(isSymbol('*'), true);
        assertEquals(isSymbol('#'), true);
        assertEquals(isSymbol('$'), true);
        assertEquals(isSymbol('.'), false);
        assertEquals(isSymbol('9'), false);
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day3-input1.txt");
        assertEquals(sum, 4361);

        sum = runMain("src/aoc2023/Day3-input1b.txt");
        assertEquals(sum, 4361);

        sum = runMain("src/aoc2023/Day3-input2.txt");
        assertEquals(sum, 540025);
    }
}
