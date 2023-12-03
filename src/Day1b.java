import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Day1b {
    public static void main(String[] args) throws Exception {
        var inputFilename = "Day1-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            var numbers = new ArrayList<Integer>();
            var firstDigitPattern = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
            var lastDigitPattern = Pattern.compile(".*(\\d|one|two|three|four|five|six|seven|eight|nine)");
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String lineNum = "";
                    var matcher = firstDigitPattern.matcher(line);
                    if (matcher.find()) {
                        lineNum += convertNum(matcher.group(1));
                    }
                    var matcher2 = lastDigitPattern.matcher(line);
                    if (matcher2.find()) {
                        lineNum += convertNum(matcher2.group(1));
                    }
                    Integer num = Integer.parseInt(lineNum);
                    numbers.add(num);
                }
            }
            //System.out.println(numbers);
            int sum = numbers.stream().reduce(0, Integer::sum);
            System.out.println(sum);
            // 54019
        }
    }

    static Map<String, String> numToValMap = new HashMap<>();
    static {
        numToValMap.putAll(Map.of("one", "1", "two", "2", "three", "3"));
        numToValMap.putAll(Map.of("four", "4", "five", "5", "six", "6"));
        numToValMap.putAll(Map.of("seven", "7", "eight", "8", "nine", "9"));
    }
    private static String convertNum(String num) {
        return (num.length() == 1) ? num : numToValMap.get(num);
    }
}
