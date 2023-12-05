package aoc2023;

public class TestUtils {
    public static void assertEquals(Object actual, Object expected) {
        if (!actual.equals(expected)) {
            throw new RuntimeException("assertEquals failed: " + actual + " not equals to " + expected);
        }
    }
}
