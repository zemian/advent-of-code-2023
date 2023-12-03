import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Day1b {
    public static void main(String[] args) throws Exception {
        var numToValMap = new HashMap<String, Integer>();
        numToValMap.putAll(Map.of("one", 1, "two", 2, "three", 3));
        numToValMap.putAll(Map.of("four", 4, "five", 5, "six", 6));
        numToValMap.putAll(Map.of("seven", 7, "eight", 8, "nine", 9));
        var inputFilename = "Day1-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        try (var reader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(inputFilename)))) {
            String line;
            var numbers = new ArrayList<Integer>();
            var firstDigitPattern = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
            var lastDigitPattern = Pattern.compile(".*(\\d|one|two|three|four|five|six|seven|eight|nine)");
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    //System.out.println(line);
                    String lineNum = "";
                    var matcher = firstDigitPattern.matcher(line);
                    if (matcher.find()) {
                        var s = matcher.group(1);
                        if (s.length() == 1) {
                            lineNum += s;
                        } else {
                            lineNum += numToValMap.get(s);
                        }
                    }
                    if (matcher.find()) { // Ensure there is at least second digit
                        var matcher2 = lastDigitPattern.matcher(line);
                        if (matcher2.find()) {
                            var s = matcher2.group(1);
                            if (s.length() == 1) {
                                lineNum += s;
                            } else {
                                lineNum += numToValMap.get(s);
                            }
                        }
                    } else {
                        // If there is single digit, duplicate it
                        lineNum += lineNum;
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
}
