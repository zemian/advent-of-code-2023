package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Day3 {
    public static void main(String[] args) throws Exception {
        var startTime = Instant.now();
        var partNums = new ArrayList<Integer>();

//        char[][] grid = new char[10][10]; // input1
//        var inputFilename = "aoc2023/Day3-input1.txt";

//        char[][] grid = new char[11][10]; // input1
//        var inputFilename = "aoc2023/Day3-input1b.txt";

        char[][] grid = new char[140][140]; // input2
        var inputFilename = "aoc2023/Day3-input2.txt";

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

            // Find part nums
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    char val = grid[i][j];
                    if (Character.isDigit(val)) {
                        var numLoc = findNumLoc(grid, i, j);
                        System.out.print("\nFound PartNum: " + numLoc.partNum);
                        if (hasSymbolAround(grid, numLoc)) {
                            partNums.add(numLoc.partNum);
                            System.out.print("  + SYMBOL");
                        }
                        j = numLoc.endj;
                    }
                }
            }
            System.out.println("\n");
        }

        var sum = partNums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        // 540025
    }

    private static boolean hasSymbolAround(char[][] grid, NumLoc n) {
        // Check left of partNum
        if (n.j > 0) {
            if (isSymbol(grid[n.i][n.j - 1])) {
                return true;
            }
        }

        // Check right of partNum
        if (n.endj < grid[0].length) {
            if (isSymbol(grid[n.i][n.endj])) {
                return true;
            }
        }

        // Check row above partNum (including diagonal position)
        var rowIndex = (n.i > 0) ? n.i - 1 : n.i;
        var colIndex = (n.j > 0) ? n.j - 1 : n.j;
        var colEndIndex = (n.endj > grid[0].length) ? n.endj + 1 : n.endj;
        for (int i = colIndex; i <= colEndIndex; i++) {
            if (isSymbol(grid[rowIndex][i])) {
                return true;
            }
        }

        // Check row below partNum (including diagonal position)
        rowIndex = (n.i < grid[0].length - 1) ? n.i + 1 : n.i;
        colIndex = (n.j > 0) ? n.j - 1 : n.j;
        colEndIndex = (n.endj > grid[0].length) ? n.endj + 1 : n.endj;
        for (int i = colIndex; i <= colEndIndex; i++) {
            if (isSymbol(grid[rowIndex][i])) {
                return true;
            }
        }

        // No symbol found
        return false;
    }

    private static boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    public static NumLoc findNumLoc(char[][] grid, int i, int j) {
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
    public static class NumLoc {
        public int partNum;
        public int i, j; // first digit location
        public int endj; // last digit position (on j).
    }
}
