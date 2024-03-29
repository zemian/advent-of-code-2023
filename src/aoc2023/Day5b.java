package aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static aoc2023.Utils.assertEquals;

public class Day5b {
    public static void main(String[] args) throws Exception {
        var program = new Day5b();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day5-input2.txt");
        }
    }

    private Long runMain(String fileName) throws Exception {
        // NOTE: Java Integer.MAX_VALUE can only handle 2_147_483_647, so we will use Long to handle larger num
        // NOTE2: Due to large input size, we can't just store entire mapping in variable or else it runs out
        //        of memory.
        // NOTE3: Be patient! It will take 13 mins to process the large input2  file!
        //        To see an faster implementation, see Day5b2.java
        List<Long> seeds = null;
        LazyMappingContainer
                seedToSoilMap = null,
                soilToFertilizerMap = null,
                fertilizerToWaterMap = null,
                waterToLightMap = null,
                lightToTemperatureMap = null,
                temperatureToHumidityMap = null,
                humidityToLocationMap = null;
        long minLocation = Long.MAX_VALUE;
        var startTime = Instant.now();

        System.out.println("Processing input: " + fileName);
        System.out.println("Started on: " + new Date());
        try (var reader = new BufferedReader(new FileReader(fileName))) {
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

        for (int i = 0; i < seeds.size(); i+=2) {
            var seedStart = seeds.get(i);
            var seedLen = seeds.get(i + 1);
            for (int j = 0; j < seedLen ; j++) {
                var seed = seedStart + j;
                var soil = seedToSoilMap.getMapping(seed);
                var fer = soilToFertilizerMap.getMapping(soil);
                var water = fertilizerToWaterMap.getMapping(fer);
                var light = waterToLightMap.getMapping(water);
                var temp = lightToTemperatureMap.getMapping(light);
                var humid = temperatureToHumidityMap.getMapping(temp);
                var loc = humidityToLocationMap.getMapping(humid);
                minLocation = Long.min(minLocation, loc);
                //System.out.println("Found location: " + loc + " for seed: " + seed);
            }
        }
        System.out.println("Min location: " + minLocation);
        System.out.println("Time: " + (Duration.between(startTime, Instant.now())));

        return minLocation;
    }

    private LazyMappingContainer parseMapping(BufferedReader reader) throws Exception {
        var lazyMappingContainer = new LazyMappingContainer();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }
            String[] parts = line.trim().split("\\s+");
            var destStart = Long.parseLong(parts[0]);
            var sourceStart = Long.parseLong(parts[1]);
            var len = Integer.parseInt(parts[2]);

            lazyMappingContainer.addMapping(new LazyMapping(destStart, sourceStart, len));
        }
        return lazyMappingContainer;
    }

    private class LazyMapping {
        private long destStart, sourceStart, len;
        public LazyMapping(long destStart, long sourceStart, long len) {
            this.destStart = destStart;
            this.sourceStart = sourceStart;
            this.len = len;
        }

        public long getMapping(long sourceVal) {
            if (!isInRange(sourceVal)) {
                return sourceVal;
            }

            return destStart + (sourceVal - sourceStart);
        }

        public boolean isInRange(long sourceVal) {
            return sourceVal >= sourceStart && sourceVal < (sourceStart + len);
        }
    }

    private class LazyMappingContainer {
        private List<LazyMapping> lazyMappings = new ArrayList<>();

        public void addMapping(LazyMapping mapping) {
            lazyMappings.add(mapping);
        }

        public long getMapping(long sourceVal) {
            // Ensure we search all the mapping list that is within range
            var mapping = lazyMappings.stream().filter(e -> e.isInRange(sourceVal)).findFirst();
            return mapping.map(lazyMapping -> lazyMapping.getMapping(sourceVal)).orElse(sourceVal);
        }
    }

    private void runTests() throws Exception {
        testLazyMapping();
        testParsingMapping();
        testMain();
    }

    private void testParsingMapping() throws Exception {
        var reader = new StringReader("""
                50 98 2
                52 50 48
                """);
        var lazyMappingContainer = parseMapping(new BufferedReader(reader));
        assertEquals(lazyMappingContainer.getMapping(98L), 50L);
        assertEquals(lazyMappingContainer.getMapping(99L), 51L);
        assertEquals(lazyMappingContainer.getMapping(50L), 52L);
        assertEquals(lazyMappingContainer.getMapping(51L), 53L);
        assertEquals(lazyMappingContainer.getMapping(53L), 55L);
    }

    private void testLazyMapping() {
        var lazyMapping = new LazyMapping(50, 98, 2);
        assertEquals(lazyMapping.getMapping(98L), 50L);
        assertEquals(lazyMapping.getMapping(98L), 50L);
        assertEquals(lazyMapping.getMapping(99L), 51L);

        lazyMapping = new LazyMapping(52, 50, 48);
        assertEquals(lazyMapping.getMapping(50L), 52L);
        assertEquals(lazyMapping.getMapping(51L), 53L);
        assertEquals(lazyMapping.getMapping(53L), 55L);
    }

    private void testMain() throws Exception {
        Long minLoc = runMain("src/aoc2023/Day5-input1.txt");
        assertEquals(minLoc, 46L);

        minLoc = runMain("src/aoc2023/Day5-input2.txt");
        assertEquals(minLoc, 37806486L);
    }
}
