package com.github.papayankey.AoC_2023.day_05;

import com.github.papayankey.AoC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SeedFertilizer {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 5);
        input = Arrays.stream(String.join("\n", input).split("\n\n")).toList();

        System.out.println(PartOne(input));
        System.out.println(PartTwo(input));
    }

    static long PartOne(List<String> input) {
        List<Long> seeds = getSeeds(input);
        List<List<Category>> categories = getCategories(input);

        List<Long> locations = new ArrayList<>();

        for (long value : seeds) {
            for (List<Category> category : categories) {
                value = nextMappedValue(value, category);
            }
            locations.add(value);
        }

        return locations.stream().min(Comparator.naturalOrder()).orElse(0L);
    }

    static long PartTwo(List<String> input) {
        List<Range> seeds = getSeedsWithRange(getSeeds(input));
        List<List<Category>> categories = getCategories(input);

        List<Long> locations = new ArrayList<>();

//        for (Range range : seeds) {
//            for (long i = range.start; i <= range.end; i++) {
//                long value = i;
//                for (List<Category> category : categories) {
//                    value = getNextCategoryValue(value, category);
//                }
//                locations.add(value);
//            }
//        }
//
//        System.out.println(STR."locations: \{locations}");
//        return locations.stream().mapToLong(Long::longValue).min().orElse(0L);

        return 0L;
    }

    static List<Range> getSeedsWithRange(List<Long> seeds) {
        List<Range> ranges = new ArrayList<>();

        int index = 0;
        while (index < seeds.size()) {
            int endIndex = index + 2; // exclusive
            List<Long> pair = seeds.subList(index, endIndex);
            ranges.add(new Range(pair.getFirst(), pair.getFirst() + pair.getLast() - 1));
            index = endIndex;
        }

        return ranges;
    }

    static List<Long> getSeeds(List<String> input) {
        return Arrays.stream(input.getFirst().replaceAll("seeds:\\s+", "").split("\\s+"))
                .map(Long::parseLong)
                .toList();
    }

    static long nextMappedValue(long value, List<Category> categories) {
        for (Category category : categories) {
            if (value >= category.src.start && value <= category.src.end) {
                return category.dest.start + (value - category.src.start);
            }
        }
        return value;
    }

    static List<List<Category>> getCategories(List<String> input) {
        List<List<Category>> categories = new ArrayList<>();

        List<List<String>> resources = input.stream()
                .skip(1)
                .map(line -> Arrays.stream(line.split("\n")).skip(1).toList())
                .toList();

        for (List<String> resource : resources) {
            List<Category> temp = new ArrayList<>();
            for (String str : resource) {
                long[] parts = Arrays.stream(str.split("\\s+")).mapToLong(Long::parseLong).toArray();
                Range destination = new Range(parts[0], parts[0] + parts[2] - 1);
                Range source = new Range(parts[1], parts[1] + parts[2] - 1);
                temp.add(new Category(destination, source));
            }
            categories.add(temp);
        }

        return categories;
    }

    record Category(Range dest, Range src) {
    }

    record Range(long start, long end) {
    }
}
