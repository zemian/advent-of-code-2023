import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day1 {
    public static void main(String[] args) throws Exception {
        var inputFilename = "Day1-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        try (var reader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(inputFilename)))) {
            String line;
            var numbers = new ArrayList<Integer>();
            var firstDigitPattern = Pattern.compile("(\\d)");
            var lastDigitPattern = Pattern.compile(".*(\\d)");
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    //System.out.println(line);
                    String lineNum = "";
                    var matcher = firstDigitPattern.matcher(line);
                    if (matcher.find()) {
                        lineNum += matcher.group(1);
                    }
                    if (matcher.find()) { // Ensure there is at least second digit
                        var matcher2 = lastDigitPattern.matcher(line);
                        if (matcher2.find()) {
                            lineNum += matcher2.group(1);
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
            // 54632
        }
    }
}
