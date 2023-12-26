package aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.function.Function;

import static java.util.function.Predicate.not;

public class Utils {
    public static void assertEquals(Object actual, Object expected) {
        if (!actual.equals(expected)) {
            throw new RuntimeException("assertEquals failed: " + actual + " not equals to " + expected);
        }
    }

    /** Read a text file and strip off empty lines. */
    public static List<String> readLines(String fileName) {
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            return reader.lines().filter(not(String::isEmpty)).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file " + fileName, e);
        }
    }

    /** Read a text file convert it into a 2D chars grid arrays. */
    public static char[][] readGrid(String fileName) {
        var lines = readLines(fileName);
        return lines.stream().map(String::toCharArray).toArray(char[][]::new);
    }

    public static void printGrid(char[][] grid) {
        System.out.println("=== " + grid.length + "x" + grid[0].length + " grid ===");
        for (char[] chars : grid) {
            for (char ch : chars) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        testReadLines();
        testReadGrid();
    }

    public static void testReadLines() {
        var lines = readLines("src/aoc2023/Day1-input1.txt");
        assertEquals(String.join("\n", lines), """
                1abc2
                pqr3stu8vwx
                a1b2c3d4e5f
                treb7uchet""");
    }
    public static void testReadGrid() {
        var grid = readGrid("src/aoc2023/Day10-input1a.txt");
        printGrid(grid);
    }

    public static record Tuple2<A, B>(A a, B b){}
}
