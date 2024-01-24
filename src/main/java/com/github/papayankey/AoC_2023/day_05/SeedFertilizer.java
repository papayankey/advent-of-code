package com.github.papayankey.AoC_2023.day_05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeedFertilizer {
    public static void main(String[] args) {
//        var input = AoC.getInput(2023, 5);

        var input = """
                seeds: 79 14 55 13
                                
                seed-to-soil map:
                50 98 2
                52 50 48
                                
                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15
                                
                fertilizer-to-water map:
                49 53 8
                0 11 42
                42 0 7
                57 7 4
                                
                water-to-light map:
                88 18 7
                18 25 70
                                
                light-to-temperature map:
                45 77 23
                81 45 19
                68 64 13
                                
                temperature-to-humidity map:
                0 69 1
                1 0 69
                                
                humidity-to-location map:
                60 56 37
                56 93 4
                """;

        PartOne(input);
    }

    static void PartOne(String input) {
        List<String> groups = Arrays.stream(input.split("\n\n")).toList();

        List<Long> seeds = Arrays.stream(groups.getFirst().replaceAll("seeds:\\s+", "").split("\\s+"))
                .map(Long::parseLong)
                .toList();

        List<List<Resource>> resources = parseResources(groups.subList(1, groups.size()));
    }

    static void PartTwo(List<String> lines) {
    }

    static List<List<Resource>> parseResources(List<String> resourceGroups) {
        List<List<String>> resources = resourceGroups.stream()
                .map(line -> Arrays.stream(line.split("\n")).skip(1).toList())
                .toList();

        return List.of(
                getResources(resources.getFirst()), getResources(resources.get(1)),
                getResources(resources.get(2)), getResources(resources.get(3)),
                getResources(resources.get(4)), getResources(resources.get(5)), getResources(resources.get(6))
        );
    }

    static List<Resource> getResources(List<String> list) {
        List<Resource> resources = new ArrayList<>();
        for (var str : list) {
            long[] parts = Arrays.stream(str.split("\\s+")).mapToLong(Long::parseLong).toArray();
            Range destination = new Range(parts[0], parts[0] + parts[2] - 1);
            Range source = new Range(parts[1], parts[1] + parts[2] - 1);
            resources.add(new Resource(destination, source));
        }
        return resources;
    }

    record Resource(Range dest, Range src) {
    }

    record Range(long start, long end) {
    }
}
