package aoc2023;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import static aoc2023.Utils.assertEquals;

public class Day3b {
    public static void main(String[] args) {
        var program = new Day3b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day3-input2.txt");
        }
    }

    private Integer runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var startTime = Instant.now();
        var powerNums = new ArrayList<Integer>();
        char[][] grid = Utils.readGrid(fileName);

        // Find a pair of part nums next to a symbol that's connected
        // We will calculate the power of these two and the sum of all later.
        var symbolNumLocMap = new HashMap<String, NumLoc>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char val = grid[i][j];
                if (Character.isDigit(val)) {
                    var numLoc = findNumLoc(grid, i, j);
                    //System.out.println("\nFound PartNum: " + numLoc.partNum);
                    if (checkAndUpdateSymbol(grid, numLoc)) {
                        var key = numLoc.si + "," + numLoc.sj;
                        System.out.println("Found PartNum: " + numLoc.partNum +
                                " with symbol: " + numLoc.symbol + " at " + key);
                        if (symbolNumLocMap.containsKey(key)) {
                            var n1 = symbolNumLocMap.get(key);
                            var power = n1.partNum * numLoc.partNum;
//                                System.out.println("  Found power nums: " + n1.partNum + " * " + numLoc.partNum);
                            powerNums.add(power);
                            symbolNumLocMap.remove(key);
                        } else {
                            symbolNumLocMap.put(key, numLoc);
                        }
                    }
                    j = numLoc.endj;
                }
            }
        }

        var sum = powerNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        return sum;
    }

    // Part Two requires us to find and store the symbol grid location
    private static boolean checkAndUpdateSymbol(char[][] grid, NumLoc n) {
        // Check left of partNum
        if (n.j > 0) {
            if (isSymbol(grid[n.i][n.j - 1])) {
                n.symbol = grid[n.i][n.j - 1];
                n.si = n.i;
                n.sj = n.j - 1;
                return true;
            }
        }

        // Check right of partNum
        if (n.endj < grid[0].length - 1) {
            if (isSymbol(grid[n.i][n.endj + 1])) {
                n.symbol = grid[n.i][n.endj + 1];
                n.si = n.i;
                n.sj = n.endj + 1;
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
                    n.symbol = grid[rowIndex][i];
                    n.si = rowIndex;
                    n.sj = i;
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
                    n.symbol = grid[rowIndex][i];
                    n.si = rowIndex;
                    n.sj = i;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    private static NumLoc findNumLoc(char[][] grid, int i, int j) {
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
        public char symbol;
        public int si, sj; // symbol location
    }

    private void runTests() {
        testFindNumLocWithSymbol();
        testMain();
    }

    private void testFindNumLocWithSymbol() {
        char[][] grid = new char[][] {
                ".....".toCharArray(),
                ".467.".toCharArray(),
                "..*..".toCharArray(),
        };
        var numLoc = findNumLoc(grid, 1, 1);
        checkAndUpdateSymbol(grid, numLoc); // This must call to populate the symbol location
        assertEquals(numLoc.partNum, 467);
        assertEquals(numLoc.i, 1);
        assertEquals(numLoc.j, 1);
        assertEquals(numLoc.endj, 3);
        assertEquals(numLoc.symbol, '*');
        assertEquals(numLoc.si, 2);
        assertEquals(numLoc.sj, 2);

        grid = new char[][] {
                ".....".toCharArray(),
                "..467".toCharArray(),
                "....$".toCharArray(),
        };
        numLoc = findNumLoc(grid, 1, 2);
        checkAndUpdateSymbol(grid, numLoc); // This must call to populate the symbol location
        assertEquals(numLoc.partNum, 467);
        assertEquals(numLoc.i, 1);
        assertEquals(numLoc.j, 2);
        assertEquals(numLoc.endj, 4);
        assertEquals(numLoc.symbol, '$');
        assertEquals(numLoc.si, 2);
        assertEquals(numLoc.sj, 4);
    }

    private void testMain() {
        Integer sum = runMain("src/aoc2023/Day3-input1.txt");
        assertEquals(sum, 467835);

        sum = runMain("src/aoc2023/Day3-input2.txt");
        assertEquals(sum, 84584891);
    }
}
