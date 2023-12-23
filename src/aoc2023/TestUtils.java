package aoc2023;

public class TestUtils {
    public static void assertEquals(Object actual, Object expected) {
        if (!actual.equals(expected)) {
            throw new RuntimeException("assertEquals failed: " + actual + " not equals to " + expected);
        }
    }

    public static void printGrid(char[][] grid) {
        for (char[] chars : grid) {
            for (char ch : chars) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }
}
