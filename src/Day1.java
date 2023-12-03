import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day1 {
    public static void main(String[] args) throws Exception {
        //testFirstAndLastDigit();
        run();
    }
    public static void run() throws Exception {
        var inputFilename = "Day1-input.txt";
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            var numbers = new ArrayList<Integer>();
            var firstDigitPattern = Pattern.compile("(\\d)");
            var lastDigitPattern = Pattern.compile(".*(\\d)");
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String lineNum = "";
                    var matcher = firstDigitPattern.matcher(line);
                    if (matcher.find()) {
                        lineNum += matcher.group(1);
                    }
                    var matcher2 = lastDigitPattern.matcher(line);
                    if (matcher2.find()) {
                        lineNum += matcher2.group(1);
                    }
                    Integer num = Integer.parseInt(lineNum);
                    numbers.add(num);
                }
            }
            int sum = numbers.stream().reduce(0, Integer::sum);
            System.out.println(sum);
            // 54632
        }
    }

    public static void testFirstAndLastDigit() throws Exception {
        // Test regex to get first and last digit.
        var s = "sdpgz3five4seven6fiveh";
        var p = Pattern.compile(".*(\\d)");
        var m = p.matcher(s);
        m.find();
        System.out.println(m.group(1));

        s = "sdpgz3";
        p = Pattern.compile(".*(\\d)");
        m = p.matcher(s);
        m.find();
        System.out.println(m.group(1));
    }
}
