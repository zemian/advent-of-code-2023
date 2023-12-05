package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static aoc2023.TestUtils.assertEquals;

public class Day3b {

    public static void main(String[] args) throws Exception {
        var program = new Day3b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day3-input2.txt", 140, 140);
        }
    }

    private void runTests() throws Exception {
        testMain();
    }

    private void testMain() throws Exception {
        Integer sum = (Integer)runMain("aoc2023/Day3-input1.txt", 10, 10);
        assertEquals(sum, 467835);

        sum = (Integer)runMain("aoc2023/Day3-input2.txt", 140, 140);
        assertEquals(sum, 84584891);
    }

    private Object runMain(String inputFilename, int gridRowSize, int gridColSize) throws Exception {
        var startTime = Instant.now();
        var powerNums = new ArrayList<Integer>();
        char[][] grid = new char[gridRowSize][gridColSize];

        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;

            //Populate grid
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    for (int i = 0; i < line.length(); i++) {
                        grid[lineCount][i] = line.charAt(i);
                    }
                    lineCount++;
                    //System.out.println(line + " " + new String(grid[lineCount - 1]));
                }
            }

            // Find part nums next to a symbol
            var symbolNumLocMap = new HashMap<String, NumLoc>();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    char val = grid[i][j];
                    if (Character.isDigit(val)) {
                        var numLoc = findNumLoc(grid, i, j);
                        //System.out.println("\nFound PartNum: " + numLoc.partNum);
                        if (hasSymbolAround(grid, numLoc)) {
                            System.out.println("Found PartNum: " + numLoc.partNum + " with symbol: " + numLoc.symbol);
                            var key = numLoc.si + "," + numLoc.sj;
                            if (symbolNumLocMap.containsKey(key)) {
                                var n1 = symbolNumLocMap.get(key);
                                var power = n1.partNum * numLoc.partNum;
                                powerNums.add(power);
                                symbolNumLocMap.remove(key); // Do I need this?
                            } else {
                                symbolNumLocMap.put(key, numLoc);
                            }
                        }
                        j = numLoc.endj;
                    }
                }
            }
            System.out.println("\n");
        }

        var sum = powerNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        return sum;
    }

    // Part Two requires us to find and store the symbol grid location
    private static boolean hasSymbolAround(char[][] grid, NumLoc n) {
        n.s = true; // start with found flag.

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
        if (n.endj < grid[0].length) {
            if (isSymbol(grid[n.i][n.endj])) {
                n.symbol = grid[n.i][n.endj];
                n.si = n.i;
                n.sj = n.endj;
                return true;
            }
        }

        // Check row above partNum (including diagonal position)
        var rowIndex = (n.i > 0) ? n.i - 1 : n.i;
        var colIndex = (n.j > 0) ? n.j - 1 : n.j;
        var colEndIndex = (n.endj > grid[0].length) ? n.endj + 1 : n.endj;
        for (int i = colIndex; i <= colEndIndex; i++) {
            if (isSymbol(grid[rowIndex][i])) {
                n.symbol = grid[rowIndex][i];
                n.si = rowIndex;
                n.sj = i;
                return true;
            }
        }

        // Check row below partNum (including diagonal position)
        rowIndex = (n.i < grid[0].length - 1) ? n.i + 1 : n.i;
        colIndex = (n.j > 0) ? n.j - 1 : n.j;
        colEndIndex = (n.endj > grid[0].length) ? n.endj + 1 : n.endj;
        for (int i = colIndex; i <= colEndIndex; i++) {
            if (isSymbol(grid[rowIndex][i])) {
                n.symbol = grid[rowIndex][i];
                n.si = rowIndex;
                n.sj = i;
                return true;
            }
        }

        // No symbol found
        n.s = false;

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
                numLoc.endj = k;
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
        public boolean s; // Symbol found or not
    }
}
