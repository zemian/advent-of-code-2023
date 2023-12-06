package aoc2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static aoc2023.TestUtils.assertEquals;

public class Day5 {
    public static void main(String[] args) throws Exception {
        var program = new Day5();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("aoc2023/Day5-input1.txt");
        }
    }

    private Long runMain(String inputFilename) throws Exception {
        // NOTE: Java Integer.MAX_VALUE can only handle 2_147_483_647, so we will use Long to handle larger num
        // NOTE2: Due to large input size, we can't just stored entire mapping in variable or else it runs out
        //        of memory.
        List<Long> seeds = null;
        Map<Long, Long>
                seedToSoilMap = null,
                soilToFertilizerMap = null,
                fertilizerToWaterMap = null,
                waterToLightMap = null,
                lightToTemperatureMap = null,
                temperatureToHumidityMap = null,
                humidityToLocationMap = null,
                seedToLocationMap = new HashMap<>();

        System.out.println("Processing input: " + inputFilename);
        var cl = Thread.currentThread().getContextClassLoader();
        var ins = cl.getResourceAsStream(inputFilename);
        try (var reader = new BufferedReader(new InputStreamReader(ins))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                // Process Line
                if (line.startsWith("seeds:")) {
                    String[] parts = line.split(": ");
                    var nums = Arrays.asList(parts[1].trim().split("\\s+"));
                    seeds = nums.stream().map(Long::valueOf).toList();
                } else if (line.startsWith("seed-to-soil map:")) {
                    seedToSoilMap = parseMapping(reader);
                } else if (line.startsWith("soil-to-fertilizer map:")) {
                    soilToFertilizerMap = parseMapping(reader);
                } else if (line.startsWith("fertilizer-to-water map:")) {
                    fertilizerToWaterMap = parseMapping(reader);
                } else if (line.startsWith("water-to-light map:")) {
                    waterToLightMap = parseMapping(reader);
                } else if (line.startsWith("light-to-temperature map:")) {
                    lightToTemperatureMap = parseMapping(reader);
                } else if (line.startsWith("temperature-to-humidity map:")) {
                    temperatureToHumidityMap = parseMapping(reader);
                } else if (line.startsWith("humidity-to-location map:")) {
                    humidityToLocationMap = parseMapping(reader);
                }
            }
        }

        for (var seed : seeds) {
            var soil = seedToSoilMap.getOrDefault(seed, seed);
            var fer = soilToFertilizerMap.getOrDefault(soil, soil);
            var water = fertilizerToWaterMap.getOrDefault(fer, fer);
            var light = waterToLightMap.getOrDefault(water, water);
            var temp = lightToTemperatureMap.getOrDefault(light, light);
            var humid = temperatureToHumidityMap.getOrDefault(temp, temp);
            var loc = humidityToLocationMap.getOrDefault(humid, humid);
            seedToLocationMap.put(seed, loc);
        }

        var minLoc = seedToLocationMap.values().stream().min(Long::compareTo);
        return minLoc.get();
    }

    private HashMap<Long, Long> parseMapping(BufferedReader reader) throws Exception {
        var mapping = new HashMap<Long, Long>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }
            String[] parts = line.trim().split("\\s+");
            var destStart = Long.parseLong(parts[0]);
            var sourceStart = Long.parseLong(parts[1]);
            var len = Integer.parseInt(parts[2]);

            for (int i = 0; i < len; i++) {
                var destVal = destStart + i;
                var sourceVal = sourceStart + i;
                mapping.put(sourceVal, destVal);
            }
        }
        return mapping;
    }

    private void runTests() throws Exception {
        testParsingMapping();
        testMain();
    }

    private void testParsingMapping() throws Exception {
        var reader = new StringReader("""
                50 98 2
                52 50 48
                """);
        var mapping = parseMapping(new BufferedReader(reader));
        assertEquals(mapping.get(98L), 50L);
        assertEquals(mapping.get(99L), 51L);
        assertEquals(mapping.get(50L), 52L);
        assertEquals(mapping.get(51L), 53L);
        assertEquals(mapping.get(53L), 55L);
    }

    private void testMain() throws Exception {
        Long minLoc = runMain("aoc2023/Day5-input1.txt");
        assertEquals(minLoc, 35L);

        minLoc = runMain("aoc2023/Day5-input2.txt");
        assertEquals(minLoc, -1L);
    }
}
